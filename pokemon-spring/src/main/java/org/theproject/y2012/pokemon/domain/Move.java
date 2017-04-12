package org.theproject.y2012.pokemon.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "move")
public class Move {


    public Move() {
        super();
    }

    public Move(int pokedexId, String name, String type, int level, int power, int pp,
            int accuracy, int priority, String target, String damageClass, String effect, 
            int effectChance) {
        super();
        this.pokedexId = pokedexId;
        this.name = name;
        this.type = type;
        this.level = level;
        this.power = power;
        this.pp = pp;
        this.accuracy = accuracy;
        this.priority = priority;
        this.target = target;
        this.damageClass = damageClass;
        this.effectChance = effectChance;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
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
    private PokemonEntity pokemon;
    
    public PokemonEntity getPokemon() {
        return pokemon;
    }

    public void setPokemon(PokemonEntity pokemon) {
        this.pokemon = pokemon;
        if (!pokemon.getMoves().contains(this)) {
            pokemon.getMoves().add(this);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPokedexId() {
        return pokedexId;
    }

    public void setPokedexId(int pokedexId) {
        this.pokedexId = pokedexId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDamageClass() {
        return damageClass;
    }

    public void setDamageClass(String damageClass) {
        this.damageClass = damageClass;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public int getEffectChance() {
        return effectChance;
    }

    public void setEffectChance(int effectChance) {
        this.effectChance = effectChance;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Move [id=").append(id).append(", pokedexId=").append(pokedexId)
                .append(", name=").append(name).append(", type=").append(type).append(", level=")
                .append(level).append(", power=").append(power).append(", pp=").append(pp)
                .append(", accuracy=").append(accuracy).append(", priority=").append(priority)
                .append(", target=").append(target).append(", damageClass=").append(damageClass)
                .append(", effect=").append(effect).append(", effectChance=").append(effectChance)
                .append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + pokedexId;
        return result;
    }

    // Equality is based on pokedexId (?)
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Move other = (Move) obj;
        if (pokedexId != other.pokedexId)
            return false;
        return true;
    }


}
