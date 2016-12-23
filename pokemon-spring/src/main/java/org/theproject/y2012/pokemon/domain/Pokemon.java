package org.theproject.y2012.pokemon.domain;

import static java.lang.Math.floor;
import static java.lang.Math.max;
import static org.theproject.y2012.pokemon.domain.PokemonMath.rnd;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import org.apache.log4j.Logger;

@Entity
public class Pokemon extends PokemonEntity {

    private static Logger logger = Logger.getLogger("Pokemon");
    
    public static void battle(Pokemon p1, Pokemon p2) {
        
        logger.info("In battle...");
        
        p1.setAccuracy(0);
        p1.setEvasion(0);
        p2.setAccuracy(0);
        p2.setEvasion(0);
        
        // The battle continues until one of the Pokemon has fainted, or until
        // both have run out of moves
        while ((p1.getHp() > 0) && (p2.getHp() > 0)
                && (p1.hasAMoveWithPP() || p2.hasAMoveWithPP())) {
            // Whoever is faster attacks first
            if (p1.getSpeed() > p2.getSpeed()) {
                attack(p1, p2);
                attack(p2, p1);
            } else {
                attack(p2, p1);
                attack(p1, p2);
            }
        }
        
        if (p1.getHp() <= 0) {
            logger.info(p1.getName() + " fainted!  " + p2.getName() + " wins!");
        } else if (p2.getHp() <= 0) {
            logger.info(p2.getName() + " fainted!  " + p1.getName() + " wins!");
        } else {
            logger.info("Both pokemon exhausted the PP of all their moves!  It's a draw!");
        }
        
    }
    

    public static void attack(Pokemon attacker, Pokemon defender) {
        Set<Move> attackerMoves = attacker.getMoves();
        String attackerName = attacker.getName();

        if (!attacker.hasAMoveWithPP()) {
            logger.info(attacker.getName() + " has exhausted the PP of all his moves!  "
                    + attacker.getName() + " stands there helplessly!");
            return;
        }
        
        // Choose a move randomly
        Move move = attacker.chooseRandomPoweredMove();
        logger.info(attackerName + " attacks " + defender.getName() + " with "
                + move.getName());

        // Charge the attacker one PP
        // This DOES change the PP of the move in attacker.getMoves()
        move.setPp(move.getPp() - 1);

        // Check accuracy
        if (!attacker.checkAccuracy(move)) { // or should checkAccuracy be
                                             // static?
            return;
        }

        // TODO: Check for critical hit
        double critical = 1;

        // 1.5X damage bonus if the Pokemon is of the same type as the move it's
        // using
        double stab = 1; // "same type attack bonus"
        if (move.getType().equals(attacker.getType1())
                || move.getType().equals(attacker.getType2())) {
            logger.info(attackerName + " is the same type as " + move.getName());
            stab = 1.5;
        } else {
            logger.info(attackerName + " is not the same type as " + move.getName());
        }
        
        // TODO: use priority and speed to determine which attack takes place first
        
        // TODO: This needs a little more research
        // K Self-KO modifier. 0.5 if the attacking Pokemon uses
        // Selfdestruct or Explosion, 1 otherwise.
        double k = 1;

        doMove(attacker, defender, move, critical, stab, k);
    }

    public static void doMove(Pokemon attacker, Pokemon defender, Move move, double critical,
            double stab, double k) {
        
        // TODO: In many attacks I'm ignoring the effect of 'substitute', so far.

        // We will probably not do badge bonuses since they pertain to the
        // trainer, not the pokemon
        double badgeBonus = 1;

        // TODO: E 0.5 if Reflect (for physical attacks) or Light Screen (for
        // special attacks) is up on the opponent, 1 otherwise.
        double e = 1;
        
        // TODO: T The type effectiveness of the move; super effective = 2,
        // neutral = 1, etc. as seen in the Type Effectiveness Chart.
        double typeEffectiveness = 1;
        
        // TODO: M Application of any stat modifiers as seen in section M04
        // above, such as Swords Dance or Amnesia.
        double attackerStatModifiers = 1;
        double defenderStatModifiers = 1;

        // TODO: E Burn modifier. 0.5 if the attacking Pokemon is under BRN
        // status and uses a physical attack, 0.5 if the attacking Pokemon has
        // been cured of BRN status but hasn't switched out since it has been
        // cured and it is using a physical attack, 1 if the attacking Pokemon
        // has used an Attack boosting move like Swords Dance since it's been on
        // the field, 1 otherwise.
        double attackerBurnModifier = 1;

        MoveName moveId = MoveName.numberToMoveName(move.getPokedexId());
        
        double damage;
        boolean effectHappens;
        
        // DEBUG
        // moveId = Move.Headbutt;
        
        switch (moveId) {
        case Absorb:
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k, badgeBonus, e,
                    typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);
            
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Attacker recovers 50% of the damage inflicted on the target. The
                // minimum health that can be restored is 1HP, but it will not
                // restore any health to the user if it breaks a substitute.

                double recover = floor(max(1, damage / 2));
                int newHp = attacker.getHp() + (int) recover;

                logger.info("Attacker " + attacker.getName() + " recovers " + recover + " HP from "
                        + attacker.getHp() + " to " + newHp);

                // TODO: can HP be increased without any limit? Is there a max
                // HP?
                attacker.setHp(newHp);
            }
            
            break;
        case Acid:
            // Inflicts [regular damage]{mechanic}. Has a $effect_chance% chance
            // to lower the target's [Special Defense]{mechanic} by one
            // [stage]{mechanic}.
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k, badgeBonus, e,
                    typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);

            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Lowers target's defense by one stage
                double statsModifier = PokemonMath.statModifier(-1);
                defender.adjustSpecialDefense(statsModifier);
            }
            break;
        case AcidArmor:
        case Barrier:
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Increase defense 2 stages
                double statsModifier = PokemonMath.statModifier(2);
                attacker.adjustDefense(statsModifier);
            }            
            
            break;
        case Aeroblast:
            // 'Inflicts regular damage.  User's critical hit rate is one level higher when using this move.' 
            logger.info("Aeroblast not implemented yet.");
            break;
        case Agility:
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Increase speed 2 stages
                double statsModifier = PokemonMath.statModifier(2);
                attacker.adjustSpeed(statsModifier);
            }            
            break;
        case Amnesia:
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Increase special defense 2 stages 
                double statsModifier = PokemonMath.statModifier(2);
                attacker.adjustSpecialDefense(statsModifier);
            }            
            break;
        case AncientPower:
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k, badgeBonus, e,
                    typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);

            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Raise all the attacker's stats one stage
                // What exactly is 'all stats'?  HP, Atk, Def, Speed, Special
                double statsModifier = PokemonMath.statModifier(1);
                attacker.adjustAttack(statsModifier);
                attacker.adjustDefense(statsModifier);
                attacker.adjustSpecialAttack(statsModifier);
                attacker.adjustSpecialDefense(statsModifier);
                attacker.adjustSpeed(statsModifier);
                attacker.adjustHP(statsModifier);
            }            
            break;
        case Attract:

            // Kind of a weird one.
            // "Target falls in love if it has the opposite gender, and has a 50% chance to refuse attacking the user."

            logger.info("Attract not implemented yet.");
            break;
        case AuroraBeam:
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k, badgeBonus, e,
                    typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);

            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Lowers defenders attack by 1 stage
                double statsModifier = PokemonMath.statModifier(-1);
                defender.adjustAttack(statsModifier);
            }
            break;
        case Barrage:
        case BoneRush:
        case CometPunch:
        case DoubleSlap:
        case FuryAttack:
        case FurySwipes:
        case PinMissile:
        case SpikeCannon:
            // Inflicts [regular damage]{mechanic}. Hits 2–5 times in one turn.
            // Has a 3/8 chance each to hit 2 or 3 times, and a 1/8 chance each
            // to hit 4 or 5 times. Averages to 3 hits per use.
            damage = multiDamage(attacker, defender, move, critical, stab, k, badgeBonus, e,
                    typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);
            break;
        case BatonPass:
            // TODO: Allows the trainer to switch out the user and pass effects along
            // to its replacement.
            // Unclear how to implement this in a world without trainers
            logger.info("BatonPass not implemented yet.");
            break;
        case BeatUp:
            // TODO: Hits once for every conscious Pokémon the trainer has.
            // Unclear how to implement this in a world without trainers
            logger.info("BeatUp not implemented yet.");
            break;
        case BellyDrum:
            // User pays half its max [HP]{mechanic} to raise its
            // [Attack]{mechanic} to +6 [stages]{mechanic:stage}. If the user
            // cannot pay the [HP]{mechanic} cost, this move will fail.

            // This test should probably be up where the move is chosen,
            // so if it's impossible a different move can be chosen instead.
            int half = attacker.getMaxHp() / 2;
            if (attacker.getHp() < half) {
                logger.info("Belly Drum fails because atacker " + attacker.getName() + " has less than half of his max HP remaining.");
            } else {
                // TODO: use gender
                attacker.setHp(attacker.getHp() - half);
                logger.info("atacker " + attacker.getName() + " spends half of his max HP, " + half + ", to max out his attack");
                int saveAtk = attacker.getAttack();
                attacker.setAttack((int)(saveAtk * PokemonMath.statModifier(6)));
                damage = multiDamage(attacker, defender, move, critical, stab, k, badgeBonus, e,
                        typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                        attackerBurnModifier, false);
                attacker.setAttack(saveAtk);
            }

            break;
        case Bide:
            // Move with a complex effect that lasts for 3 turns
            logger.info("Bide not implemented yet.");
            break;
        case Bind:
            // Partial Trapping Moves
            logger.info("Bind not implemented yet.");
            break;
        case Bite:
        case BoneClub:
            // Inflicts [regular damage]{mechanic}. Has a $effect_chance% chance
            // to make the target flinch.
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k, badgeBonus, e,
                    typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);

            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                logger.info("defender " + defender.getName() + " flinches");
                defender.setFlinch(true);
            }
            break;
        case Blizzard:
            // Inflicts [regular damage]{mechanic}. Has a $effect_chance% chance
            // to [freeze]{mechanic} the target.

            // During [Hail]{move}, this move has 100% accuracy. It also has a
            // (100 - accuracy)% chance to break through the protection of
            // [Protect]{move} and [Detect]{move}.
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k, badgeBonus, e,
                    typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);

            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                if (defender.getNonVolatileStatus() == NonVolatileStatus.NOTHING) {
                    logger.info("defender " + defender.getName() + " is frozen");
                    defender.setNonVolatileStatus(NonVolatileStatus.FREEZE);
                } else {
                    logger.info("Can't freeze defender " + defender.getName() + " because defender already has a "
                            + defender.getNonVolatileStatus() + " status");
                }
            }
            break;
        case BodySlam:
        case DragonBreath:
            // Inflicts [regular damage]{mechanic}. Has a $effect_chance% chance
            // to [paralyze]{mechanic} the target.
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k, badgeBonus, e,
                    typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);

            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                if (defender.getNonVolatileStatus() == NonVolatileStatus.NOTHING) {
                    logger.info("defender " + defender.getName() + " is paralyzed");
                    defender.setNonVolatileStatus(NonVolatileStatus.PARALYSIS);
                } else {
                    logger.info("Can't paralyze defender " + defender.getName()
                            + " because defender already has a " + defender.getNonVolatileStatus()
                            + " status");
                }
            }
            break;
        case Bonemerang:
        case DoubleKick:
            damage = doubleDamage(attacker, defender, move, critical, stab, k, badgeBonus, e,
                    typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);
            break;
        case Bubble:
        case BubbleBeam:
            // Inflicts [regular damage]{mechanic}. Has a $effect_chance% chance
            // to lower the target's [Speed]{mechanic} by one [stage]{mechanic}.
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k,
                    badgeBonus, e, typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);

            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Decrease speed 1 stage
                double statsModifier = PokemonMath.statModifier(-1);
                defender.adjustSpeed(statsModifier);
            }
            break;
        case Charm:
            // Lowers the target's [Attack]{mechanic} by two [stages]{mechanic:stage}.
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Decrease attack 2 stages
                double statsModifier = PokemonMath.statModifier(-2);
                defender.adjustAttack(statsModifier);
            }
            break;
        case Clamp:
            // Partial Trapping Moves
            logger.info("Clamp not implemented yet.");
            break;
        case ConfuseRay:
            // [Confuse]{mechanic}s the target.
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                logger.info("Defender " + defender.getName() + " is confused!");
                defender.setConfusion(true);
            }
            break;
        case Confusion:
        case DizzyPunch:
        case DynamicPunch:
            // Inflicts [regular damage]{mechanic}. Has a $effect_chance% chance
            // to [confuse]{mechanic} the target
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k,
                    badgeBonus, e, typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                logger.info("Defender " + defender.getName() + " is confused!");
                defender.setConfusion(true);
            }
            break;
        case Constrict:
            // Inflicts [regular damage]{mechanic}. Has a $effect_chance% chance
            // to lower the target's [Speed]{mechanic} by one [stage]{mechanic}.
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k,
                    badgeBonus, e, typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Decrease speed 1 stage
                double statsModifier = PokemonMath.statModifier(-1);
                defender.adjustSpeed(statsModifier);
            }
            logger.info("Constrict not implemented yet.");
            break;
        case Conversion:
            // User's [type]{mechanic} changes to the [type]{mechanic} of one of
            // its moves, selected at random. [Hidden Power]{move} and [Weather
            // Ball]{move} are treated as [Normal]{type}. Only moves with a
            // different [type]{mechanic} are eligible, and [Curse]{move} is
            // never eligible. If the user has no suitable moves, this move will
            // fail.
            logger.info("Conversion not implemented yet.");
            break;
        case Conversion2:
            // Changes the user's type to a type either resistant or immune to
            // the last damaging move that hit it. The new type is selected at
            // random and cannot be a type the user already is. If there is no
            // eligible new type, this move will fail.
            logger.info("Conversion2 not implemented yet.");
            break;
        case CottonSpore:
            // Lowers the target's [Speed]{mechanic} by two [stages]{mechanic:stage}.
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Decrease speed 1 stage
                double statsModifier = PokemonMath.statModifier(-2);
                defender.adjustSpeed(statsModifier);
            }
            break;
        case Counter:
            // Targets the last opposing Pokémon to hit the user with a
            // [physical]{mechanic} move this turn. Inflicts twice the damage
            // that move did to the user. If there is no eligible target, this
            // move will [fail]{mechanic}. Type immunity applies, but other type
            // effects are ignored.

            // This move cannot be copied by [Mirror Move]{move}, nor selected
            // by [Assist]{move} or [Metronome]{move}.
            logger.info("Counter not implemented yet.");
            break;
        case Crabhammer:
            // Inflicts [regular damage]{mechanic}. User's [critical
            // hit]{mechanic} rate is one level higher when using this move.
            
            // According to Eevee's pokedex, this is the same as Aeroblast.
            // However, according to Zerokid's pokemon FAQ, this increases the
            // chances of a critical hit by a factor of 8.
            logger.info("Crabhammer not implemented yet.");
            break;
        case CrossChop:
            // Inflicts [regular damage]{mechanic}. User's [critical
            // hit]{mechanic} rate is one level higher when using this move.
            
            // According to Eevee, same as Aeroblast and Crabhammer
            logger.info("CrossChop not implemented yet.");
            break;
        case Crunch:
            // Inflicts [regular damage]{mechanic}. Has a $effect_chance% chance
            // to lower the target's [Defense]{mechanic} by one
            // [stage]{mechanic}.
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k,
                    badgeBonus, e, typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);
            
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Lowers target's defense by one stage
                double statsModifier = PokemonMath.statModifier(-1);
                defender.adjustDefense(statsModifier);
            }
            break;
        case Curse:
            // If the user is a [Ghost]{type}: user pays half its max
            // [HP]{mechanic} to place a curse on the target, damaging it for
            // 1/4 its max [HP]{mechanic} every turn.
            // Otherwise: Lowers the user's [Speed]{mechanic} by one
            // [stage]{mechanic}, and raises its [Attack]{mechanic} and
            // [Defense]{mechanic} by one [stage]{mechanic} each.

            // The curse effect is passed on by [Baton Pass]{move}.

            // This move cannot be copied by [Mirror Move]{move}.
            logger.info("Curse not implemented yet.");
            break;
        case Cut:
        case DrillPeck:
        case EggBomb:
        case ExtremeSpeed:
            // Inflicts [regular damage]{mechanic}.
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k,
                    badgeBonus, e, typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);
            break;
        case DefenseCurl:
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Raise defense 1 stage
                double statsModifier = PokemonMath.statModifier(1);
                attacker.adjustDefense(statsModifier);
            }            
            break;
        case DestinyBond:
            // User's [critical hit]{mechanic} rate is two levels higher until
            // it leaves the field. If the user has already used [Focus
            // Energy]{move} since entering the field, this move will fail.

            //This effect is passed on by [Baton Pass]{move}.
            logger.info("DestinyBond not implemented yet.");
            break;
        case Detect:
            // No moves will hit the user for the remainder of this turn. If the
            // user is last to act this turn, this move will fail.

            // If the user successfully used [Detect]{move}, [Endure]{move},
            // [fast guard]{move}, [Protect]{move}, or [wide guard]{move} on the
            // last turn, this move has a 50% chance to fail.

            // [Lock-On]{move}, [Mind Reader]{move}, and [No Guard]{ability}
            // provide a (100 – accuracy)% chance for moves to break through
            // this move. This does not apply to one-hit KO moves
            // ([Fissure]{move}, [Guillotine]{move}, [Horn Drill]{move}, and
            // [Sheer Cold]{move}); those are always blocked by this move.

            // [Thunder]{move} during [Rain Dance]{move} and [Blizzard]{move}
            // during [Hail]{move} have a 30% chance to break through this move.

            // The following effects are not prevented by this move:

            // * [Acupressure]{move} from an ally
            // * [Curse]{move}'s curse effect
            // * Delayed damage from [Doom Desire]{move} and [Future
            // Sight]{move}; however, these moves will be prevented if they are
            // used this turn
            // * [Feint]{move}, which will also end this move's protection after it hits
            // * [Imprison]{move}
            // * [Perish Song]{move}
            // * [Shadow Force]{move}
            // * Moves that merely copy the user, such as [Transform]{move} or [Psych Up]{move}

            // This move cannot be selected by [Assist]{move} or [Metronome]{move}.
            logger.info("Detect not implemented yet.");
            break;
        case Dig:
            // Inflicts [regular damage]{mechanic}. User digs underground for
            // one turn, becoming immune to attack, and hits on the second turn.

            // During the immune turn, [Earthquake]{move}, [Fissure]{move}, and
            // [Magnitude]{move} still hit the user normally, and their power is
            // doubled if appropriate.

            // The user may be hit during its immune turn if under the effect of
            // [Lock-On]{move}, [Mind Reader]{move}, or [No Guard]{ability}.

            // This move cannot be selected by [Sleep Talk]{move}.
            logger.info("Dig not implemented yet.");
            break;
        case Disable:
            // Disables the target's last used move, preventing its use for 4–7
            // turns, selected at random, or until the target leaves the
            // [field]{mechanic}. If the target hasn't used a move since
            // entering the [field]{mechanic}, if it tried to use a move this
            // turn and [failed]{mechanic}, if its last used move has 0 PP
            // remaining, or if it already has a move disabled, this move will
            // fail.
            logger.info("Disable not implemented yet.");
            break;
        case DoubleEdge:
            // Inflicts [regular damage]{mechanic}. User takes 1/3 the damage it
            // inflicts in recoil.

            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k,
                    badgeBonus, e, typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);
            
            // Is this considered an effect?
            double oneThirdDamage = floor(damage/3);
            logger.info("Attacker " + attacker.getName() + " gets hit by the backlash!");
            performDamage(attacker, oneThirdDamage);
            
            break;
        case DoubleTeam:
            // Raises the user's [evasion]{mechanic} by one [stage]{mechanic}.
            // Evasion?  What the heck is evasion?
            logger.info("DoubleTeam not implemented yet.");
            break;
        case DragonRage:
            // Inflicts exactly 40 damage.
            damage = 40;
            performDamage(defender, damage);
            break;
        case DreamEater:
            // Only works on [sleep]{mechanic}ing Pokémon. Inflicts [regular
            // damage]{mechanic}. Heals the user for half the damage inflicted.
            logger.info("DreamEater not implemented yet.");
            break;
        case Earthquake:
            // Inflicts [regular damage]{mechanic}.
            // If the target is in the first turn of [Dig]{move}, this move will
            // hit with double power.
            logger.info("Earthquake not implemented yet.");
            break;
        case Ember:
        case FireBlast:
        case FirePunch:
        case Flamethrower:
            // Inflicts [regular damage]{mechanic}. Has a $effect_chance% chance
            // to [burn]{mechanic} the target.
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k,
                    badgeBonus, e, typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);
            
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                if (defender.getNonVolatileStatus() == NonVolatileStatus.NOTHING) {
                    logger.info("defender " + defender.getName() + " is on fire");
                    defender.setNonVolatileStatus(NonVolatileStatus.BURN);
                } else {
                    logger.info("Can't burn defender " + defender.getName()
                            + " because defender already has a " + defender.getNonVolatileStatus()
                            + " status");
                }
            }
            break;
        case Encore:
            // The next 4–8 times (selected at random) the target attempts to
            // move, it is forced to repeat its last used move. If the selected
            // move allows the trainer to select a target, an opponent will be
            // selected at random each turn. If the target is prevented from
            // using the selected move by some other effect, [Struggle]{move}
            // will be used instead. This effect ends if the selected move runs
            // out of [PP]{mechanic}.

            // If the target hasn't used a move since entering the
            // [field]{mechanic}, if it tried to use a move this turn and
            // [failed]{mechanic}, if it does not know the selected move, or if
            // the selected move has 0 [PP]{mechanic} remaining, this move will
            // fail. If the target's last used move was [Encore]{move},
            // [Mimic]{move}, [Mirror Move]{move}, [Sketch]{move},
            // [Struggle]{move}, or [Transform]{move}, this move will fail.
            logger.info("Encore not implemented yet.");
            break;
        case Endure:
            // The user's [HP]{mechanic} cannot be lowered below 1 by any means
            // for the remainder of this turn.

            // If the user successfully used [Detect]{move}, [Endure]{move},
            // [fast guard]{move}, [Protect]{move}, or [wide guard]{move} on the
            // last turn, this move has a 50% chance to fail.

            // This move cannot be selected by [Assist]{move} or [Metronome]{move}.
            logger.info("Endure not implemented yet.");
            break;
        case Explosion:
            // User [faint]{mechanic}s, even if the attack [fail]{mechanic}s or
            // [miss]{mechanic}es. Inflicts [regular damage]{mechanic}.
            logger.info("Explosion not implemented yet.");
            break;
        case FaintAttack:
            // Inflicts [regular damage]{mechanic}. Ignores [accuracy]{mechanic}
            // and [evasion]{mechanic} modifiers.
            // (never misses)
            logger.info("FaintAttack not implemented yet.");
            break;
        case FalseSwipe:
            // nflicts [regular damage]{mechanic}. Will not reduce the target's
            // [HP]{mechanic} below 1.
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k,
                    badgeBonus, e, typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, true);
            break;
        case FireSpin:
            // Partial Trapping Moves
            // Inflicts [regular damage]{mechanic}. For the next 2–5 turns, the
            // target cannot leave the field and is damaged for 1/16 its max HP
            // at the end of each turn. The user continues to use other moves
            // during this time. If the user leaves the [field]{mechanic}, this
            // effect ends.

            // Has a 3/8 chance each to hit 2 or 3 times, and a 1/8 chance each
            // to hit 4 or 5 times. Averages to 3 hits per use.

            // Rapid Spin]{move} cancels this effect.
            logger.info("FireSpin not implemented yet.");
            break;
        case Fissure:
            // Inflicts damage equal to the target's max [HP]{mechanic}. Ignores
            // [accuracy]{mechanic} and [evasion]{mechanic} modifiers. This
            // move's [accuracy]{mechanic} is 30% plus 1% for each level the
            // user is higher than the target. If the user is a lower level than
            // the target, this move will [fail]{mechanic}.

            // Because this move inflicts a specific and finite amount of
            // damage, [Endure]{move} still prevents the target from fainting.

            // The effects of [Lock-On]{move}, [Mind Reader]{move}, and [No
            // Guard]{ability} still apply, as long as the user is equal or
            // higher level than the target. However, they will not give this
            // move a chance to break through [Detect]{move} or [Protect]{move}.
            logger.info("Fissure not implemented yet.");
            break;
        case Flail:
            // Inflicts [regular damage]{mechanic}. Power varies inversely with
            // the user's proportional remaining [HP]{mechanic}.

            // 64 * current HP / max HP | Power
            // -----------------------: | ----:
            //  0– 1                    |  200
            //  2– 5                    |  150
            //  6–12                    |  100
            // 13–21                    |   80
            // 22–42                    |   40
            // 43–64                    |   20
            logger.info("Flail not implemented yet.");
            break;
        case FlameWheel:
            // Inflicts [regular damage]{mechanic}. Has a $effect_chance% chance
            // to [burn]{mechanic} the target. [Frozen]{mechanic} Pokémon may
            // use this move, in which case they will thaw.
            logger.info("FlameWheel not implemented yet.");
            break;
        case Flash:
            // Lowers the target's [accuracy]{mechanic} by one [stage]{mechanic}.
            logger.info("Flash not implemented yet.");
            break;
        case Fly:
            logger.info("Fly not implemented yet.");
            break;
        case FocusEnergy:
            // User's [critical hit]{mechanic} rate is two levels higher until
            // it leaves the field. If the user has already used [Focus
            // Energy]{move} since entering the field, this move will fail.

            //This effect is passed on by [Baton Pass]{move}.
            logger.info("FocusEnergy not implemented yet.");
            break;
        case Foresight:
            // Resets the target's [evasion]{mechanic:evasion} to normal and
            // prevents any further boosting until the target leaves the
            // [field]{mechanic:field}. A []{type:ghost} under this effect takes
            // normal damage from []{type:normal} and []{type:fighting} moves.
            // This move itself ignores [accuracy]{mechanic:accuracy} and
            // [evasion]{mechanic:evasion} modifiers.
            logger.info("Foresight not implemented yet.");
            break;
        case Frustration:
            // Inflicts [regular damage]{mechanic:regular-damage}. Power
            // increases inversely with [happiness]{mechanic:happiness}, given
            // by `(255 - happiness) * 2 / 5`, to a maximum of 102. Power
            // bottoms out at 1.
            logger.info("Frustration not implemented yet.");
            break;
        case FuryCutter:
            // Inflicts [regular damage]{mechanic:regular-damage}. Power doubles
            // after every time this move is used, whether consecutively or not,
            // maxing out at 16x. If this move misses or the user leaves the
            // [field]{mechanic:field}, power resets.
            logger.info("FuryCutter not implemented yet.");
            break;
        case FutureSight:
            logger.info("FutureSight not implemented yet.");
            break;
        case GigaDrain:
            logger.info("GigaDrain not implemented yet.");
            break;
        case Glare:
            logger.info("Glare not implemented yet.");
            break;
        case Growl:
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Lowers target's attack by one stage
                double statsModifier = PokemonMath.statModifier(-1);
                defender.adjustAttack(statsModifier);
            }
            break;
        case Growth:
            // Raises the user's [Attack]{mechanic:attack} and [Special
            // Attack]{mechanic:special-attack} by one [stage]{mechanic:stage}
            // each. During []{move:sunny-day}, raises both stats by two stages.
            // TODO: Implement the Sunny Day part
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Increase attack and special attack one stage
                double statsModifier = PokemonMath.statModifier(1);
                defender.adjustAttack(statsModifier);
                defender.adjustSpecialAttack(statsModifier);
            }
            break;
        case Guillotine:
            // Inflicts damage equal to the target's max [HP]{mechanic:hp}.
            // Ignores [accuracy]{mechanic:accuracy} and
            // [evasion]{mechanic:evasion} modifiers. This move's
            // [accuracy]{mechanic:accuracy} is 30% plus 1% for each level the
            // user is higher than the target. If the user is a lower level than
            // the target, this move will [fail]{mechanic:fail}.

            // Because this move inflicts a specific and finite amount of
            // damage, []{move:endure} still prevents the target from fainting.

            // The effects of []{move:lock-on}, []{move:mind-reader}, and
            // []{ability:no-guard} still apply, as long as the user is equal or
            // higher level than the target. However, they will not give this
            // move a chance to break through []{move:detect} or
            // []{move:protect}.
            logger.info("Guillotine not implemented yet.");
            break;
        case Gust:
            logger.info("Gust not implemented yet.");
            break;
        case Harden:
            logger.info("Harden not implemented yet.");
            break;
        case Haze:
            logger.info("Haze not implemented yet.");
            break;
        case Headbutt:
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k, badgeBonus, e,
                    typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);

            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                logger.info(defender.getName() + " flinches!");
                defender.setFlinch(true);
            }
            
            break;
        case HealBell:
            logger.info("HealBell not implemented yet.");
            break;
        case HiJumpKick:
            logger.info("HiJumpKick not implemented yet.");
            break;
        case HiddenPower:
            logger.info("HiddenPower not implemented yet.");
            break;
        case HornAttack:
            logger.info("HornAttack not implemented yet.");
            break;
        case HornDrill:
            logger.info("HornDrill not implemented yet.");
            break;
        case HydroPump:
            logger.info("HydroPump not implemented yet.");
            break;
        case HyperBeam:
            logger.info("HyperBeam not implemented yet.");
            break;
        case HyperFang:
            logger.info("HyperFang not implemented yet.");
            break;
        case Hypnosis:
            logger.info("Hypnosis not implemented yet.");
            break;
        case IceBeam:
            logger.info("IceBeam not implemented yet.");
            break;
        case IcePunch:
            logger.info("IcePunch not implemented yet.");
            break;
        case IcyWind:
            logger.info("IcyWind not implemented yet.");
            break;
        case IronTail:
            logger.info("IronTail not implemented yet.");
            break;
        case JumpKick:
            logger.info("JumpKick not implemented yet.");
            break;
        case KarateChop:
            logger.info("KarateChop not implemented yet.");
            break;
        case Kinesis:
            logger.info("Kinesis not implemented yet.");
            break;
        case LeechLife:
            logger.info("LeechLife not implemented yet.");
            break;
        case LeechSeed:
            logger.info("LeechSeed not implemented yet.");
            break;
        case Leer:
            // Lower's target's defense by one stage
            // TODO: The stat lowering effect cannot occur if the foe is behind
            // a substitute or under the effects of Mist.
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Lowers target's defense by one stage
                double statsModifier = PokemonMath.statModifier(-1);
                defender.adjustDefense(statsModifier);
            }
            break;
        case Lick:
            logger.info("Lick not implemented yet.");
            break;
        case LightScreen:
            logger.info("LightScreen not implemented yet.");
            break;
        case LockOn:
            logger.info("LockOn not implemented yet.");
            break;
        case LovelyKiss:
            logger.info("LovelyKiss not implemented yet.");
            break;
        case LowKick:
            logger.info("LowKick not implemented yet.");
            break;
        case MachPunch:
            logger.info("MachPunch not implemented yet.");
            break;
        case Magnitude:
            logger.info("Magnitude not implemented yet.");
            break;
        case MeanLook:
            logger.info("MeanLook not implemented yet.");
            break;
        case Meditate:
            logger.info("Meditate not implemented yet.");
            break;
        case MegaDrain:
            logger.info("MegaDrain not implemented yet.");
            break;
        case MegaKick:
            logger.info("MegaKick not implemented yet.");
            break;
        case MegaPunch:
            logger.info("MegaPunch not implemented yet.");
            break;
        case Megahorn:
            logger.info("Megahorn not implemented yet.");
            break;
        case MetalClaw:
            logger.info("MetalClaw not implemented yet.");
            break;
        case Metronome:
            logger.info("Metronome not implemented yet.");
            break;
        case MilkDrink:
            logger.info("MilkDrink not implemented yet.");
            break;
        case Mimic:
            logger.info("Mimic not implemented yet.");
            break;
        case MindReader:
            logger.info("MindReader not implemented yet.");
            break;
        case Minimize:
            logger.info("Minimize not implemented yet.");
            break;
        case MirrorCoat:
            logger.info("MirrorCoat not implemented yet.");
            break;
        case MirrorMove:
            logger.info("MirrorMove not implemented yet.");
            break;
        case Mist:
            logger.info("Mist not implemented yet.");
            break;
        case Moonlight:
            logger.info("Moonlight not implemented yet.");
            break;
        case MorningSun:
            logger.info("MorningSun not implemented yet.");
            break;
        case MudSlap:
            logger.info("MudSlap not implemented yet.");
            break;
        case NightShade:
            logger.info("NightShade not implemented yet.");
            break;
        case Nightmare:
            logger.info("Nightmare not implemented yet.");
            break;
        case Octazooka:
            logger.info("Octazooka not implemented yet.");
            break;
        case Outrage:
            logger.info("Outrage not implemented yet.");
            break;
        case PainSplit:
            logger.info("PainSplit not implemented yet.");
            break;
        case PayDay:
            logger.info("PayDay not implemented yet.");
            break;
        case Peck:
            logger.info("Peck not implemented yet.");
            break;
        case PerishSong:
            logger.info("PerishSong not implemented yet.");
            break;
        case PetalDance:
            logger.info("PetalDance not implemented yet.");
            break;
        case PoisonGas:
            logger.info("PoisonGas not implemented yet.");
            break;
        case PoisonPowder:
            logger.info("PoisonPowder not implemented yet.");
            break;
        case PoisonSting:
            logger.info("PoisonSting not implemented yet.");
            break;
        case Pound:
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k, badgeBonus, e,
                    typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);
            break;
        case PowderSnow:
            logger.info("PowderSnow not implemented yet.");
            break;
        case Present:
            logger.info("Present not implemented yet.");
            break;
        case Protect:
            logger.info("Protect not implemented yet.");
            break;
        case Psybeam:
            logger.info("Psybeam not implemented yet.");
            break;
        case PsychUp:
            logger.info("PsychUp not implemented yet.");
            break;
        case Psychic:
            logger.info("Psychic not implemented yet.");
            break;
        case Psywave:
            logger.info("Psywave not implemented yet.");
            break;
        case Pursuit:
            // Inflicts [regular damage]{mechanic}. If the target attempts to
            // [switch out]{mechanic} this turn before the user moves, and the
            // user is not prevented from doing so, the user will hit the target
            // with doubled power as it leaves. Otherwise, this move does not
            // act specially.

            // This effect can still hit a Pokémon that [switches out]{mechanic}
            // when it has a [Substitute]{move} up or when an ally has used
            // [Follow Me]{move}.
            logger.info("Pursuit not implemented yet.");
            break;
        case QuickAttack:
            logger.info("QuickAttack not implemented yet.");
            break;
        case Rage:
            logger.info("Rage not implemented yet.");
            break;
        case RainDance:
            logger.info("RainDance not implemented yet.");
            break;
        case RapidSpin:
            logger.info("RapidSpin not implemented yet.");
            break;
        case RazorLeaf:
            logger.info("RazorLeaf not implemented yet.");
            break;
        case RazorWind:
            logger.info("RazorWind not implemented yet.");
            break;
        case Recover:
            logger.info("Recover not implemented yet.");
            break;
        case Reflect:
            logger.info("Reflect not implemented yet.");
            break;
        case Rest:
            logger.info("Rest not implemented yet.");
            break;
        case Return:
            logger.info("Return not implemented yet.");
            break;
        case Reversal:
            logger.info("Reversal not implemented yet.");
            break;
        case Roar:
            logger.info("Roar not implemented yet.");
            break;
        case RockSlide:
            logger.info("RockSlide not implemented yet.");
            break;
        case RockSmash:
            logger.info("RockSmash not implemented yet.");
            break;
        case RockThrow:
            logger.info("RockThrow not implemented yet.");
            break;
        case RollingKick:
            logger.info("RollingKick not implemented yet.");
            break;
        case Rollout:
            logger.info("Rollout not implemented yet.");
            break;
        case SacredFire:
            logger.info("SacredFire not implemented yet.");
            break;
        case Safeguard:
            logger.info("Safeguard not implemented yet.");
            break;
        case SandAttack:
            logger.info("SandAttack not implemented yet.");
            break;
        case Sandstorm:
            logger.info("Sandstorm not implemented yet.");
            break;
        case ScaryFace:
            logger.info("ScaryFace not implemented yet.");
            break;
        case Scratch:
        case Tackle:
            //Inflicts [regular damage]{mechanic}.            
            damage = computeAndPerformDamage(attacker, defender, move, critical, stab, k, badgeBonus, e,
                    typeEffectiveness, attackerStatModifiers, defenderStatModifiers,
                    attackerBurnModifier, false);
            break;
        case Screech:
            logger.info("Screech not implemented yet.");
            break;
        case SeismicToss:
            logger.info("SeismicToss not implemented yet.");
            break;
        case Selfdestruct:
            logger.info("Selfdestruct not implemented yet.");
            break;
        case ShadowBall:
            logger.info("ShadowBall not implemented yet.");
            break;
        case Sharpen:
            logger.info("Sharpen not implemented yet.");
            break;
        case Sing:
            logger.info("Sing not implemented yet.");
            break;
        case Sketch:
            logger.info("Sketch not implemented yet.");
            break;
        case SkullBash:
            logger.info("SkullBash not implemented yet.");
            break;
        case SkyAttack:
            logger.info("SkyAttack not implemented yet.");
            break;
        case Slam:
            logger.info("Slam not implemented yet.");
            break;
        case Slash:
            logger.info("Slash not implemented yet.");
            break;
        case SleepPowder:
            // Puts the target to [sleep]{mechanic:sleep}.
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                if (defender.getNonVolatileStatus() == NonVolatileStatus.NOTHING) {
                    logger.info("defender " + defender.getName() + " goes to sleep");
                    defender.setNonVolatileStatus(NonVolatileStatus.SLEEP);
                } else {
                    logger.info("Can't put defender " + defender.getName()
                            + " to sleep because defender already has a "
                            + defender.getNonVolatileStatus() + " status");
                }
            }
            
            break;
        case SleepTalk:
            logger.info("SleepTalk not implemented yet.");
            break;
        case Sludge:
            logger.info("Sludge not implemented yet.");
            break;
        case SludgeBomb:
            logger.info("SludgeBomb not implemented yet.");
            break;
        case Smog:
            logger.info("Smog not implemented yet.");
            break;
        case SmokeScreen:
            logger.info("SmokeScreen not implemented yet.");
            break;
        case Snore:
            logger.info("Snore not implemented yet.");
            break;
        case Softboiled:
            logger.info("Softboiled not implemented yet.");
            break;
        case SolarBeam:
            logger.info("SolarBeam not implemented yet.");
            break;
        case SonicBoom:
            logger.info("SonicBoom not implemented yet.");
            break;
        case Spark:
            logger.info("Spark not implemented yet.");
            break;
        case SpiderWeb:
            logger.info("SpiderWeb not implemented yet.");
            break;
        case Spikes:
            logger.info("Spikes not implemented yet.");
            break;
        case Spite:
            logger.info("Spite not implemented yet.");
            break;
        case Splash:
            logger.info("Splash not implemented yet.");
            break;
        case Spore:
            logger.info("Spore not implemented yet.");
            break;
        case SteelWing:
            logger.info("SteelWing not implemented yet.");
            break;
        case Stomp:
            logger.info("Stomp not implemented yet.");
            break;
        case Strength:
            logger.info("Strength not implemented yet.");
            break;
        case StringShot:
            logger.info("StringShot not implemented yet.");
            break;
        case Struggle:
            logger.info("Struggle not implemented yet.");
            break;
        case StunSpore:
            logger.info("StunSpore not implemented yet.");
            break;
        case Submission:
            logger.info("Submission not implemented yet.");
            break;
        case Substitute:
            logger.info("Substitute not implemented yet.");
            break;
        case SunnyDay:
            logger.info("SunnyDay not implemented yet.");
            break;
        case SuperFang:
            logger.info("SuperFang not implemented yet.");
            break;
        case Supersonic:
            logger.info("Supersonic not implemented yet.");
            break;
        case Surf:
            logger.info("Surf not implemented yet.");
            break;
        case Swagger:
            logger.info("Swagger not implemented yet.");
            break;
        case SweetKiss:
            logger.info("SweetKiss not implemented yet.");
            break;
        case SweetScent:
            logger.info("SweetScent not implemented yet.");
            break;
        case Swift:
            logger.info("Swift not implemented yet.");
            break;
        case SwordsDance:
            logger.info("SwordsDance not implemented yet.");
            break;
        case Synthesis:
            logger.info("Synthesis not implemented yet.");
            break;
        case TailWhip:
            // Effect
            effectHappens = computeEffectChance(move);
            if (effectHappens) {
                // Lowers target's defense by one stage
                double statsModifier = PokemonMath.statModifier(-1);
                defender.adjustDefense(statsModifier);
            }
            break;
        case TakeDown:
            logger.info("TakeDown not implemented yet.");
            break;
        case Teleport:
            logger.info("Teleport not implemented yet.");
            break;
        case Thief:
            logger.info("Thief not implemented yet.");
            break;
        case Thrash:
            logger.info("Thrash not implemented yet.");
            break;
        case Thunder:
            logger.info("Thunder not implemented yet.");
            break;
        case ThunderPunch:
            logger.info("ThunderPunch not implemented yet.");
            break;
        case ThunderShock:
            logger.info("ThunderShock not implemented yet.");
            break;
        case ThunderWave:
            logger.info("ThunderWave not implemented yet.");
            break;
        case Thunderbolt:
            logger.info("Thunderbolt not implemented yet.");
            break;
        case Toxic:
            logger.info("Toxic not implemented yet.");
            break;
        case Transform:
            logger.info("Transform not implemented yet.");
            break;
        case TriAttack:
            logger.info("TriAttack not implemented yet.");
            break;
        case TripleKick:
            logger.info("TripleKick not implemented yet.");
            break;
        case Twineedle:
            logger.info("Twineedle not implemented yet.");
            break;
        case Twister:
            logger.info("Twister not implemented yet.");
            break;
        case ViceGrip:
            logger.info("ViceGrip not implemented yet.");
            break;
        case VineWhip:
            logger.info("VineWhip not implemented yet.");
            break;
        case VitalThrow:
            logger.info("VitalThrow not implemented yet.");
            break;
        case WaterGun:
            logger.info("WaterGun not implemented yet.");
            break;
        case Waterfall:
            logger.info("Waterfall not implemented yet.");
            break;
        case Whirlpool:
            logger.info("Whirlpool not implemented yet.");
            break;
        case Whirlwind:
            logger.info("Whirlwind not implemented yet.");
            break;
        case WingAttack:
            logger.info("WingAttack not implemented yet.");
            break;
        case Withdraw:
            logger.info("Withdraw not implemented yet.");
            break;
        case Wrap:
            // Partial Trapping Moves
            logger.info("Wrap not implemented yet.");
            break;
        case ZapCannon:
            logger.info("ZapCannon not implemented yet.");
            break;
        }
    }

    private static double computeAndPerformDamage(Pokemon attacker, Pokemon defender, Move move,
            double critical, double stab, double k, double badgeBonus, double e,
            double typeEffectiveness, double attackerStatModifiers, double defenderStatModifiers,
            double attackerBurnModifier, boolean hpGuard) {
        double damage;
        damage = PokemonMath.computeDamage(attacker.getLevel(), critical, badgeBonus,
                move.getPower(), e, stab, typeEffectiveness, attacker.getAttack(),
                attackerStatModifiers, badgeBonus, attackerBurnModifier, defender.getDefense(),
                defenderStatModifiers, badgeBonus, k, hpGuard);

        performDamage(defender, damage);
        return damage;
    }

    private static void performDamage(Pokemon defender, double damage) {
        int defenderHP = defender.getHp() - (int)damage;
        
        logger.info(defender.getName() + " takes " + damage + " points of damage.  "
                + defender.getName() + "'s HP decreases from " + defender.getHp() + " to "
                + defenderHP);
        
        defender.setHp(defenderHP);
        if (defender.getHp() <= 0) {
            defender.faints();
        }
    }

    // Used for attacks like Barrage, Comet Punch, Double Slap, Fury Attack,
    // Fury Swipes, Pin Missile, Spike Cannon
    // First hit always occurs, hits 2 and 3 have a 3/8 chance of hitting, hits
    // 4 and 5 have a 1/8 chance of hitting.
    private static double multiDamage (Pokemon attacker, Pokemon defender, Move move,
            double critical, double stab, double k, double badgeBonus, double e,
            double typeEffectiveness, double attackerStatModifiers, double defenderStatModifiers,
            double attackerBurnModifier, boolean hpGuard) {
        
        double damage = PokemonMath.computeDamage(attacker.getLevel(), critical, badgeBonus,
                move.getPower(), e, stab, typeEffectiveness, attacker.getAttack(),
                attackerStatModifiers, badgeBonus, attackerBurnModifier, defender.getDefense(),
                defenderStatModifiers, badgeBonus, k, hpGuard);

        for (int i=0; i<5; i++) {
            if (
                    (
                            (
                                    (i==1) || (i==2)
                            )
                            &&
                            (rnd(8) > 2)
                    )
                    ||
                    (
                            (
                                    (i==3) || (i==4)
                            )
                            &&
                            (rnd(8) > 0)
                    )
            ) {
                break;
            }
            
            performDamage(defender, damage);

        }
        
        return damage;
        
    }
    
    // Similar to multiDamage, but there are exactly 2 hits 
    // Bonemerang, Double kick, Twinneedle
    private static double doubleDamage(Pokemon attacker, Pokemon defender, Move move,
            double critical, double stab, double k, double badgeBonus, double e,
            double typeEffectiveness, double attackerStatModifiers, double defenderStatModifiers,
            double attackerBurnModifier, boolean hpGuard) {
        
        double damage = PokemonMath.computeDamage(attacker.getLevel(), critical, badgeBonus,
                move.getPower(), e, stab, typeEffectiveness, attacker.getAttack(),
                attackerStatModifiers, badgeBonus, attackerBurnModifier, defender.getDefense(),
                defenderStatModifiers, badgeBonus, k, hpGuard);

        for (int i = 0; i < 2; i++) {
            performDamage(defender, damage);
        }
        
        return damage;
    }

    private static boolean computeEffectChance(Move move) {
        int effectChance = move.getEffectChance();
        boolean effectHappens = false;
        if (effectChance == 0) {
            logger.info("Effect always takes place");
            effectHappens = true;
        } else {
            logger.info(effectChance + "% chance of effect taking place");
            if (effectChance > rnd(100)) {
                effectHappens = true;
            }
        }
        return effectHappens;
    }

    /**
     * Returns true if move connects, else false
     * 
     * @param move
     * @return
     */
    private boolean checkAccuracy(Move move) {
        // If target is user, accuracy isn't an issue
        if (move.getTarget().equals("User")) {
            logger.info("You can't miss yourself!");
            return true;
        }

        // Move never misses
        if ((move.getAccuracy() == 100) || "Never misses".equals(move.getEffect())) {
            logger.info("This move never misses!");
            return true;
        }

        int roll = rnd(100) + 1; // random number 1-100
        if (move.getAccuracy() < roll) {
            logger.info("Missed!");
            return false;
        } else {
            logger.info("Hit!");
            return true;
        }
    }

    public void addMove(Move move) {
        if (this.moves == null) {
            this.moves = new HashSet<Move>();
        }
        this.moves.add(move);
        if (move.getPokemon() != this) {
            move.setPokemon(this);
        }
    }
    
    // TODO: These all need checks for min/max values.  For example, HP can't go over 999.
    
    private void adjustAttack(double statsModifier) {
        String changeVerb = (statsModifier > 1) ? "increased" : "decreased";
        int newAttack = (int) (new Double(this.getAttack()).doubleValue() * statsModifier);
        logger.info(this.getName() + "'s attack is " + changeVerb + " from " + this.getAttack()
                + " to " + newAttack);
        this.setAttack(newAttack);
    }

    private void adjustDefense(double statsModifier) {
        String changeVerb = (statsModifier > 1) ? "increased" : "decreased";
        int newDefense = (int) (new Double(this.getDefense()).doubleValue() * statsModifier);
        logger.info(this.getName() + "'s defense is " + changeVerb + " from " + this.getDefense()
                + " to " + newDefense);
        this.setDefense(newDefense);
    }

    private void adjustSpecialAttack(double statsModifier) {
        String changeVerb = (statsModifier > 1) ? "increased" : "decreased";
        int newSpecialAttack = (int) (new Double(this.getSpecialAttack()).doubleValue() * statsModifier);
        logger.info(this.getName() + "'s special attack is " + changeVerb + " from " + this.getSpecialAttack()
                + " to " + newSpecialAttack);
        this.setSpecialAttack(newSpecialAttack);
    }

    private void adjustSpecialDefense(double statsModifier) {
        String changeVerb = (statsModifier > 1) ? "increased" : "decreased";
        int newSpecialDefense = (int) (new Double(this.getSpecialDefense()).doubleValue() * statsModifier);
        logger.info(this.getName() + "'s special defense is " + changeVerb + " from " + this.getSpecialDefense()
                + " to " + newSpecialDefense);
        this.setSpecialDefense(newSpecialDefense);
    }

    private void adjustSpeed(double statsModifier) {
        String changeVerb = (statsModifier > 1) ? "increased" : "decreased";
        int newSpeed = (int) (new Double(this.getSpeed()).doubleValue() * statsModifier);
        logger.info(this.getName() + "'s speed is " + changeVerb + " from " + this.getSpeed()
                + " to " + newSpeed);
        this.setSpeed(newSpeed);
    }

    private void adjustHP(double statsModifier) {
        String changeVerb = (statsModifier > 1) ? "increased" : "decreased";
        int newHP = (int) (new Double(this.getHp()).doubleValue() * statsModifier);
        logger.info(this.getName() + "'s HP is " + changeVerb + " from " + this.getHp()
                + " to " + newHP);
        this.setHp(newHP);
    }
    
    private void faints() {
        logger.info(this.getName() + " faints!");
    }
    
    // Does this pokemon have a move that is not depleted?
    private boolean hasAMoveWithPP() {
        boolean result = false;
        for (Move move : getMoves()) {
            if (move.getPp() > 0) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    // Choose a random move which still has PP
    // Assumes that some move still has PP
    private Move chooseRandomPoweredMove() {
        Move result = null;
        Set<Move> poweredMoves = new HashSet<Move>();
        for (Move move : getMoves()) {
            if (move.getPp() > 0) {
                poweredMoves.add(move);
            }
        }
        result = (Move)poweredMoves.toArray()[rnd(poweredMoves.size())];
        return result;
    }
    
}
