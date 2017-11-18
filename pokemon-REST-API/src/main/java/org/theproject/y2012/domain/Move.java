package org.theproject.y2012.domain;

import javax.persistence.*;

@Entity public class Move {

    private Integer id;
    private Integer pokemonId;
    private String name;
    private Integer power;
    private Integer pp;
    private Integer accuracy;
    private String damageClass;
    private String effect;
    private Integer effectChance;
    private Integer level;
    private Integer priority;
    private String target;
    private String type;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="pokemon_id")
    private Pokemon pokemon;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic @Column(name = "pokemon_id") public Integer getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(Integer pokemonId) {
        this.pokemonId = pokemonId;
    }

    @Basic @Column(name = "name") public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic @Column(name = "power") public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    @Basic @Column(name = "pp") public Integer getPp() {
        return pp;
    }

    public void setPp(Integer pp) {
        this.pp = pp;
    }

    @Basic @Column(name = "accuracy") public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    @Basic @Column(name = "damage_class") public String getDamageClass() {
        return damageClass;
    }

    public void setDamageClass(String damageClass) {
        this.damageClass = damageClass;
    }

    @Basic @Column(name = "effect") public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    @Basic @Column(name = "effect_chance") public Integer getEffectChance() {
        return effectChance;
    }

    public void setEffectChance(Integer effectChance) {
        this.effectChance = effectChance;
    }

    @Basic @Column(name = "level") public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Basic @Column(name = "priority") public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Basic @Column(name = "target") public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Basic @Column(name = "type") public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Move move = (Move) o;

        if (id != null ? !id.equals(move.id) : move.id != null) {
            return false;
        }
        if (pokemonId != null ? !pokemonId.equals(move.pokemonId) : move.pokemonId != null) {
            return false;
        }
        if (name != null ? !name.equals(move.name) : move.name != null) {
            return false;
        }
        if (power != null ? !power.equals(move.power) : move.power != null) {
            return false;
        }
        if (pp != null ? !pp.equals(move.pp) : move.pp != null) {
            return false;
        }
        if (accuracy != null ? !accuracy.equals(move.accuracy) : move.accuracy != null) {
            return false;
        }
        if (damageClass != null ? !damageClass.equals(move.damageClass) : move.damageClass != null) {
            return false;
        }
        if (effect != null ? !effect.equals(move.effect) : move.effect != null) {
            return false;
        }
        if (effectChance != null ? !effectChance.equals(move.effectChance) : move.effectChance != null) {
            return false;
        }
        if (level != null ? !level.equals(move.level) : move.level != null) {
            return false;
        }
        if (priority != null ? !priority.equals(move.priority) : move.priority != null) {
            return false;
        }
        if (target != null ? !target.equals(move.target) : move.target != null) {
            return false;
        }
        if (type != null ? !type.equals(move.type) : move.type != null) {
            return false;
        }

        return true;
    }

    @Override public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (pokemonId != null ? pokemonId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (power != null ? power.hashCode() : 0);
        result = 31 * result + (pp != null ? pp.hashCode() : 0);
        result = 31 * result + (accuracy != null ? accuracy.hashCode() : 0);
        result = 31 * result + (damageClass != null ? damageClass.hashCode() : 0);
        result = 31 * result + (effect != null ? effect.hashCode() : 0);
        result = 31 * result + (effectChance != null ? effectChance.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
