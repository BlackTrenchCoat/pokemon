package org.theproject.y2012.pokemon.domain;

/**
 * 
 * @author lmulcahy
 * These are "major" afflictions
 * Displayed with a 3-letter abbreviation
 * Only one of these conditions can be in effect at a time
 * "Linger until they are cured"
 */
public enum NonVolatileStatus {
    NOTHING (""), 
    BURN ("BRN"), 
    FREEZE ("FRZ"), 
    PARALYSIS ("PAR"), 
    POISON ("PSN"), 
    SLEEP ("SLP"),
    TOXIC_POISON ("PSN");
    
    String abbreviation;
    
    NonVolatileStatus(String abbreviation) {
        this.abbreviation = abbreviation;
    }
};
