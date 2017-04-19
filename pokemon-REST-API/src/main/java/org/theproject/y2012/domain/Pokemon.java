package org.theproject.y2012.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Pokemon implements Serializable {
    
    private static final long serialVersionUID = 4028536732551570913L;

    public Pokemon() {
    }
    
	public Pokemon(int pokedexId, String name, int height, int weight, String type1, String type2, String species,
            int happiness, String color, String habitat, int attack, int defense, int specialAttack, int specialDefense,
            int speed, int hp, int maxHp, int exp, int level) {
        super();
        this.pokedexId = pokedexId;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.type1 = type1;
        this.type2 = type2;
        this.species = species;
        this.happiness = happiness;
        this.color = color;
        this.habitat = habitat;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
        this.hp = hp;
        this.maxHp = maxHp;
        this.exp = exp;
        this.level = level;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	
    @Column(name = "pokedex_id")
    int pokedexId;

    String name;
    int height;
    int weight;
    String type1;
    String type2;
    String species;
    int happiness;
    String color;
    String habitat;

    // Stats
    int attack;
    int defense;
    
    @Column(name = "special_attack")
    int specialAttack;

    @Column(name = "special_defense")
    int specialDefense;

    int speed;
    int hp;
    @Column(name = "max_hp")
    int maxHp;
    
    @Transient
    int accuracy;
    @Transient
    int evasion;
    
    // End of stats
    
    int exp;
    int level;
    
    @OneToMany(mappedBy = "pokemon", fetch = FetchType.EAGER)
    @Cascade({CascadeType.ALL})
    Set<Move> moves;

	@Override
	public String toString() {
		return "Pokemon [id=" + id + ", pokedexId=" + pokedexId + ", name=" + name + ", height=" + height + ", weight="
				+ weight + ", type1=" + type1 + ", type2=" + type2 + ", species=" + species + ", happiness=" + happiness
				+ ", color=" + color + ", habitat=" + habitat + ", attack=" + attack + ", defense=" + defense
				+ ", specialAttack=" + specialAttack + ", specialDefense=" + specialDefense + ", speed=" + speed
				+ ", hp=" + hp + ", maxHp=" + maxHp + ", accuracy=" + accuracy + ", evasion=" + evasion + ", exp=" + exp
				+ ", level=" + level + "]";
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
        this.id = id;
    }

    public int getPokedexId() {
		return pokedexId;
	}
	public String getName() {
		return name;
	}
	public int getHeight() {
		return height;
	}
	public int getWeight() {
		return weight;
	}
	public String getType1() {
		return type1;
	}
	public String getType2() {
		return type2;
	}
	public String getSpecies() {
		return species;
	}
	public int getHappiness() {
		return happiness;
	}
	public String getColor() {
		return color;
	}
	public String getHabitat() {
		return habitat;
	}
	public int getAttack() {
		return attack;
	}
	public int getDefense() {
		return defense;
	}
	public int getSpecialAttack() {
		return specialAttack;
	}
	public int getSpecialDefense() {
		return specialDefense;
	}
	public int getSpeed() {
		return speed;
	}
	public int getHp() {
		return hp;
	}
	public int getMaxHp() {
		return maxHp;
	}
	public int getAccuracy() {
		return accuracy;
	}
	public int getEvasion() {
		return evasion;
	}
	public int getExp() {
		return exp;
	}
	public int getLevel() {
		return level;
	}

	public Set<Move> getMoves() {
		return moves;
	}
    
}
