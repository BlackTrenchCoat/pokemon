package org.theproject.y2012.domain;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
// JPA Named Query
@NamedQuery(name = "Pokemon.findAllByType", query = "select p from Pokemon p where p.type1 = :type or p.type2 = :type")
public class Pokemon {

    private Long id;
    private Integer pokedexId;
    private String name;
    private Integer attack;
    private Integer defense;
    private Integer specialAttack;
    private Integer specialDefense;
    private Integer hp;
    private Integer maxHp;
    private Integer speed;
    private Integer level;
    private String color;
    private Integer exp;
    private Integer happiness;
    private String habitat;
    private Integer height;
    private String species;
    private String type1;
    private String type2;
    private Integer weight;

    public Pokemon() {}

    public Pokemon(Integer pokedexId, String name, Integer attack, Integer defense, Integer specialAttack,
            Integer specialDefense, Integer hp, Integer maxHp, Integer speed, Integer level, String color, Integer exp,
            Integer happiness, String habitat, Integer height, String species, String type1, String type2, Integer weight) {
        this.pokedexId = pokedexId;
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.hp = hp;
        this.maxHp = maxHp;
        this.speed = speed;
        this.level = level;
        this.color = color;
        this.exp = exp;
        this.happiness = happiness;
        this.habitat = habitat;
        this.height = height;
        this.species = species;
        this.type1 = type1;
        this.type2 = type2;
        this.weight = weight;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }
    
    private Set<Move> moves;
    
    @OneToMany(mappedBy = "pokemonId", fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    public Set<Move> getMoves() {
        return moves;
    }

    public void setMoves(Set<Move> moves) {
        this.moves = moves;
    }


    public void setId(Long id) {
        this.id = id;
    }

    @Basic @Column(name = "pokedex_id") public Integer getPokedexId() {
        return pokedexId;
    }

    public void setPokedexId(Integer pokedexId) {
        this.pokedexId = pokedexId;
    }

    @Basic @Column(name = "name") public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic @Column(name = "attack") public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    @Basic @Column(name = "defense") public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    @Basic @Column(name = "special_attack") public Integer getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(Integer specialAttack) {
        this.specialAttack = specialAttack;
    }

    @Basic @Column(name = "special_defense") public Integer getSpecialDefense() {
        return specialDefense;
    }

    public void setSpecialDefense(Integer specialDefense) {
        this.specialDefense = specialDefense;
    }

    @Basic @Column(name = "hp") public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    @Basic @Column(name = "max_hp") public Integer getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(Integer maxHp) {
        this.maxHp = maxHp;
    }

    @Basic @Column(name = "speed") public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    @Basic @Column(name = "level") public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Basic @Column(name = "color") public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Basic @Column(name = "exp") public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    @Basic @Column(name = "happiness") public Integer getHappiness() {
        return happiness;
    }

    public void setHappiness(Integer happiness) {
        this.happiness = happiness;
    }

    @Basic @Column(name = "habitat") public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    @Basic @Column(name = "height") public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Basic @Column(name = "species") public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Basic @Column(name = "type1") public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    @Basic @Column(name = "type2") public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    @Basic @Column(name = "weight") public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pokemon pokemon = (Pokemon) o;

        if (id != null ? !id.equals(pokemon.id) : pokemon.id != null) {
            return false;
        }
        if (pokedexId != null ? !pokedexId.equals(pokemon.pokedexId) : pokemon.pokedexId != null) {
            return false;
        }
        if (name != null ? !name.equals(pokemon.name) : pokemon.name != null) {
            return false;
        }
        if (attack != null ? !attack.equals(pokemon.attack) : pokemon.attack != null) {
            return false;
        }
        if (defense != null ? !defense.equals(pokemon.defense) : pokemon.defense != null) {
            return false;
        }
        if (specialAttack != null ? !specialAttack.equals(pokemon.specialAttack) : pokemon.specialAttack != null) {
            return false;
        }
        if (specialDefense != null ? !specialDefense.equals(pokemon.specialDefense) : pokemon.specialDefense != null) {
            return false;
        }
        if (hp != null ? !hp.equals(pokemon.hp) : pokemon.hp != null) {
            return false;
        }
        if (maxHp != null ? !maxHp.equals(pokemon.maxHp) : pokemon.maxHp != null) {
            return false;
        }
        if (speed != null ? !speed.equals(pokemon.speed) : pokemon.speed != null) {
            return false;
        }
        if (level != null ? !level.equals(pokemon.level) : pokemon.level != null) {
            return false;
        }
        if (color != null ? !color.equals(pokemon.color) : pokemon.color != null) {
            return false;
        }
        if (exp != null ? !exp.equals(pokemon.exp) : pokemon.exp != null) {
            return false;
        }
        if (happiness != null ? !happiness.equals(pokemon.happiness) : pokemon.happiness != null) {
            return false;
        }
        if (habitat != null ? !habitat.equals(pokemon.habitat) : pokemon.habitat != null) {
            return false;
        }
        if (height != null ? !height.equals(pokemon.height) : pokemon.height != null) {
            return false;
        }
        if (species != null ? !species.equals(pokemon.species) : pokemon.species != null) {
            return false;
        }
        if (type1 != null ? !type1.equals(pokemon.type1) : pokemon.type1 != null) {
            return false;
        }
        if (type2 != null ? !type2.equals(pokemon.type2) : pokemon.type2 != null) {
            return false;
        }
        if (weight != null ? !weight.equals(pokemon.weight) : pokemon.weight != null) {
            return false;
        }

        return true;
    }

    @Override public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (pokedexId != null ? pokedexId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (attack != null ? attack.hashCode() : 0);
        result = 31 * result + (defense != null ? defense.hashCode() : 0);
        result = 31 * result + (specialAttack != null ? specialAttack.hashCode() : 0);
        result = 31 * result + (specialDefense != null ? specialDefense.hashCode() : 0);
        result = 31 * result + (hp != null ? hp.hashCode() : 0);
        result = 31 * result + (maxHp != null ? maxHp.hashCode() : 0);
        result = 31 * result + (speed != null ? speed.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (exp != null ? exp.hashCode() : 0);
        result = 31 * result + (happiness != null ? happiness.hashCode() : 0);
        result = 31 * result + (habitat != null ? habitat.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        result = 31 * result + (type1 != null ? type1.hashCode() : 0);
        result = 31 * result + (type2 != null ? type2.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }
}
