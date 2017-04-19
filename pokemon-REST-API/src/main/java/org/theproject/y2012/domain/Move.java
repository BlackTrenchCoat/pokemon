package org.theproject.y2012.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Move implements Serializable {

    @Id
    int id;

    @Column(name = "pokedex_id")
    int pokedexId;

    String name;
    String type;
    int level;
    int power;
    int pp;
    int accuracy;
    int priority;
    String target;
    
    @Column(name = "damage_class")
    String damageClass;
    
    String effect;
    
    @Column(name = "effect_chance")
    int effectChance;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="pokemon_id")
    private Pokemon pokemon;

	public int getId() {
		return id;
	}

	public int getPokedexId() {
		return pokedexId;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getLevel() {
		return level;
	}

	public int getPower() {
		return power;
	}

	public int getPp() {
		return pp;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public int getPriority() {
		return priority;
	}

	public String getTarget() {
		return target;
	}

	public String getDamageClass() {
		return damageClass;
	}

	public String getEffect() {
		return effect;
	}

	public int getEffectChance() {
		return effectChance;
	}

}
