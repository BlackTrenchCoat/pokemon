package org.theproject.browsepokemon;

import java.io.Serializable;

public class PokemonDisplayObject implements Serializable {
    
    private static final long serialVersionUID = -6945380070527876293L;

    String name;
    Integer height;
    Integer weight;
    Integer baseExperience;
    Integer baseHappiness;
    String color;
    String shape;
    String habitat;
    
    // Stats
    
    Integer hp;
    Integer attack;
    Integer defense;
    Integer specialAttack;
    Integer specialDefense;
    Integer speed;

    public PokemonDisplayObject(String name, Integer height, Integer weight,
            Integer baseExperience, Integer baseHappiness, String color, String shape) {
        super();
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.baseExperience = baseExperience;
        this.baseHappiness = baseHappiness;
        this.color = color;
        this.shape = shape;
    }

    public String getName() {
        return name;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getBaseExperience() {
        return baseExperience;
    }

    public Integer getBaseHappiness() {
        return baseHappiness;
    }

    public String getColor() {
        return color;
    }

    public String getShape() {
        return shape;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public Integer getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(Integer specialAttack) {
        this.specialAttack = specialAttack;
    }

    public Integer getSpecialDefense() {
        return specialDefense;
    }

    public void setSpecialDefense(Integer specialDefense) {
        this.specialDefense = specialDefense;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

}