package org.the_project.y2012.pokemon.domain;

import static org.junit.Assert.*;
import static java.lang.Math.rint;

import org.junit.Test;

public class TestPokemonMath {

    @Test
    public void testComputeAtkCharizard() {
        double o = 268;
        double m = 1;
        double b = 1;
        double e = 1;
        double atk = PokemonMath.computeAtk(o, m, b, e);
        assertEquals("ATK expected value 268", 268, atk, 1e-09);
    }

    @Test
    public void testComputeDefRhydon() {
        double o = 188;
        double m = 1;
        double b = 1;
        double k = 1;
        double def = PokemonMath.computeDef(o, m, b, k);
        assertEquals("DEF expected value 188", 188, def, 1e-09);
    }

    @Test
    public void testGt55Mod349() {
        double m = PokemonMath.gt255mod(349);
        assertEquals("Result is not an integer", m, rint(m), 1e-09);
        assertEquals("gt55mod expected value 87", 87, m, 1e-09);
    }

    @Test
    public void testGt55Mod268() {
        double m = PokemonMath.gt255mod(268);
        assertEquals("Result is not an integer", m, rint(m), 1e-09);
        assertEquals("gt55mod expected value 67", 67, m, 1e-09);
    }
    
    @Test
    public void testCharizardAttacksRhydon() {
        double l = 100;
        double c = 1;
        double b = 1;
        double p = 120;
        double e = 1;
        double s = 1.5;
        double t = 0.5;
        double atk = 67;
        double def = 47;
        double damage = PokemonMath.computeDamageEquation(l, c, b, p, e, s, t, atk, def);
        assertEquals("computeDamageEquation expected value 108", 108, damage, 1e-09);
    }

    @Test
    public void testRhydonAttacksCharizard() {
        double l = 100;
        double c = 1;
        double b = 1;
        double p = 75;
        double e = 1;
        double s = 1.5;
        double t = 4;
        double atk = 179;
        double def = 254;
        double damage = PokemonMath.computeDamageEquation(l, c, b, p, e, s, t, atk, def);
        assertEquals("computeDamageEquation expected value 276", 276, damage, 1e-09);
    }

    @Test
    public void testMinus6StatModifier() {
        double statModifier = PokemonMath.statModifier(-6);
        assertEquals("statModifier expected value 0.25", 0.25, statModifier, 1e-09);
    }
    
    @Test
    public void testPlus6StatModifier() {
        double statModifier = PokemonMath.statModifier(6);
        assertEquals("statModifier expected value 4", 4, statModifier, 1e-09);
        
    }
}
