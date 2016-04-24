package org.theproject.y2012.pokemon.domain;

import static java.lang.Math.abs;
import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.random;
import static java.lang.Math.rint;
import static java.lang.Math.round;

/**
 * Mostly based on http://db.gamefaqs.com/portable/gameboy/file/pokemon_ab.txt
 * Generally, just do all math as double until an int is truly called for.
 * @author lmulcahy
 *
 */
public class PokemonMath {
    /**
     * Return a random integer from 0 to n-1
     * 
     * @param n
     * @return
     */
    public static int rnd(int n) {
        int result = (int) floor(random() * n);
        return result;
    }
    
    // O = Original Attack/Special stat of the attacking Pokemon
    // M = Application of any stat modifiers such as Swords Dance or Amnesia
    // B = Badge bonus. If in cartridge in-game battles, this is equal to 1.125
    // if using a physical attack while in possession of the BoulderBadge, or
    // equal to 1.125 if using a special attack while in possession of the
    // VolcanoBadge, 1 otherwise.
    // E = Burn modifier. 0.5 if the attacking Pokemon is under BRN status and
    // uses a physical attack, 0.5 if the attacking Pokemon has been cured of
    // BRN status but hasn't switched out since it has been cured and it is
    // using a physical attack, 1 if the attacking Pokemon has used an Attack
    // boosting move like Swords Dance since it's been on the field, 1
    // otherwise.
    // K = Self-KO modifier. 0.5 if the attacking Pokemon uses Selfdestruct or
    // Explosion, 1 otherwise.
    
    // Compute ATK
    
    public static double computeAtk(double o, double m, double b, double e) {
        double result = (int)round(min(rint(rint(rint(o * m) * b) * e), 999.0));
        return result;
    }
    
    // Compute DEF
    
    public static double computeDef(double o, double m, double b, double k) {
        double result = (int)round(min(rint(rint(rint(o * m) * b) * k), 999.0));
        return result;        
    }
    
    // L = Level of the attacking Pokemon.
    // C = Critical hit modifier: 2 if a critical hit is scored, 1 otherwise.
    // B = 0.25 if Reflect (for physical attacks) or Light Screen (for special
    // attacks) is up on the opponent, 1 otherwise.
    // P = Base power of the attacking move.
    // E = 0.5 if Reflect (for physical attacks) or Light Screen (for special
    // attacks) is up on the opponent, 1 otherwise.
    // S Same-Type-Attack-Bonus (STAB). 1.5 if the attacker uses a move of the
    // same type as itself, 1 otherwise.
    // T = The type effectiveness of the move; super effective = 2, neutral = 1,
    // etc. as seen in the Type Effectiveness Chart.
    // R = A randomly determined number between 217 and 255. R is always 255 if
    // the formula up to this point is strictly greater than 767.

    // Compute damage
    
    public static double computeDamage(double l, double c, double b, 
            double p, double e, double s, double t,
            double p1o, double p1m, double p1b, double p1e,
            double p2o, double p2m, double p2b, double p2k,
            boolean hpGuard) {
        
        double result = 0;

        double atk = computeAtk(p1o, p1m, p1b, p1e);
        double def = computeDef(p2o, p2m, p2b, p2k);
        
        // If, after calculating A and D, either or both is strictly greater
        // than 255, then both A and D are divided by four, rounded down and
        // use the remainder mod 255.
        
        if ((atk > 255) || (def > 255)) {
            atk = gt255mod(atk);
            def = gt255mod(def);
        }
        
        result = computeDamageEquation(l, c, b, p, e, s, t, atk, def);
        
        double r = 255;
        if (result < 767) {
            r = 217 + rnd(38);
        }
        
        result = floor(result * r/255);
        
        if (hpGuard && (result <= 1)) {
            result = 1;
        }
        
        return result;
    }

    /**
     * Compute everything but the random part
     */
    public static double computeDamageEquation(double l, double c, double b, double p, double e,
            double s, double t, double atk, double def) {
        double result;
        
        double t1 = floor(floor(((l * c) % 256) * 0.4) + 2);
        double t2 = max(floor(atk*b), 1);
        double t3 = p/max((def * e), 1);
        double t4 = floor(t1 * t2 * t3);
        double t5 = floor(t4 / 50);
        double t6 = min(t5, 997);
       
        result = floor(floor(floor((t6 + 2)) * s) * t);
        return result;
    }
    
    public static double gt255mod(double i) {
        return floor(i/4) % 255;
    }

    public static double statModifier(int n) throws IllegalArgumentException {
        if ((n < -6) || (n > 6)) {
            throw new IllegalArgumentException();
        }
        double num = (n < 0) ? 2 : 2+n ;
        double denom = (n < 0) ? 2+abs(n) : 2;
        double result = num/denom;
        return result;
    }

}
