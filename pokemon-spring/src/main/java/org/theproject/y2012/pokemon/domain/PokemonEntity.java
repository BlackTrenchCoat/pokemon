package org.theproject.y2012.pokemon.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "pokemon")
public class PokemonEntity {

    private static Logger logger = Logger.getLogger("PokemonEntity");
    
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Id
    int id;
    
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

    public Set<Move> getMoves() {
        return moves;
    }

    public void setMoves(Set<Move> moves) {
        this.moves = moves;
    }

    // Where did I get this volatile/non-volatile stuff?
    
    // Non-volatile status 
    @Enumerated(EnumType.STRING)
    @Column(name = "non_volatile_status")
    NonVolatileStatus nonVolatileStatus = NonVolatileStatus.NOTHING;
    
    // Volatile status
    // Boolean attributes that apply in the middle of a battle, doesn't really
    // make sense to save them, though
    @Transient
    boolean confusion = false;
    @Transient
    boolean curse = false;
    @Transient
    boolean encore = false;
    @Transient
    boolean flinch = false;
    @Transient
    boolean identify = false;
    @Transient
    boolean infatuation = false;
    @Transient
    boolean leechSeed = false;
    @Transient
    boolean partiallyTrapped = false; // For Partial Trapping moves like Bind, Clamp, Fire Spin and Wrap

    @Column(name = "lock_on")
    boolean lockOn = false;
    
    @Column(name = "mind_reader")
    boolean mindReader = false;

    boolean nightmare = false;
    
    @Column(name = "perish_song")
    boolean perishSong = false;
    boolean taunt = false;
    boolean torment = false;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PokemonEntity [id=").append(id).append(", name=").append(name)
                .append(", height=").append(height).append(", weight=").append(weight)
                .append(", type1=").append(type1).append(", type2=").append(type2)
                .append(", species=").append(species).append(", color=").append(color)
                .append(", habitat=").append(habitat).append(", attack=").append(attack)
                .append(", defense=").append(defense).append(", specialAttack=")
                .append(specialAttack).append(", specialDefense=").append(specialDefense)
                .append(", speed=").append(speed).append(", hp=").append(hp).append(", exp=")
                .append(exp).append(", level=").append(level).append(", moves=").append(moves)
                .append(", nonVolatileStatus=").append(nonVolatileStatus).append(", confusion=")
                .append(confusion).append(", curse=").append(curse).append(", encore=")
                .append(encore).append(", flinch=").append(flinch).append(", identify=")
                .append(identify).append(", infatuation=").append(infatuation).append(", lockOn=")
                .append(lockOn).append(", mindReader=").append(mindReader).append(", nightmare=")
                .append(nightmare).append(", partiallyTrapped=").append(partiallyTrapped)
                .append(", perishSong=").append(perishSong).append(", taunt=").append(taunt)
                .append(", torment=").append(torment).append("]");
        return builder.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(int specialAttack) {
        this.specialAttack = specialAttack;
    }

    public int getSpecialDefense() {
        return specialDefense;
    }

    public void setSpecialDefense(int specialDefense) {
        this.specialDefense = specialDefense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getEvasion() {
        return evasion;
    }

    public void setEvasion(int evasion) {
        this.evasion = evasion;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public NonVolatileStatus getNonVolatileStatus() {
        return nonVolatileStatus;
    }

    public void setNonVolatileStatus(NonVolatileStatus nonVolatileStatus) {
        this.nonVolatileStatus = nonVolatileStatus;
    }

    public boolean isConfusion() {
        return confusion;
    }

    public void setConfusion(boolean confusion) {
        this.confusion = confusion;
    }

    public boolean isCurse() {
        return curse;
    }

    public void setCurse(boolean curse) {
        this.curse = curse;
    }

    public boolean isEncore() {
        return encore;
    }

    public void setEncore(boolean encore) {
        this.encore = encore;
    }

    public boolean isFlinch() {
        return flinch;
    }

    public void setFlinch(boolean flinch) {
        this.flinch = flinch;
    }

    public boolean isIdentify() {
        return identify;
    }

    public void setIdentify(boolean identify) {
        this.identify = identify;
    }

    public boolean isInfatuation() {
        return infatuation;
    }

    public void setInfatuation(boolean infatuation) {
        this.infatuation = infatuation;
    }

    public boolean isLeechSeed() {
        return leechSeed;
    }

    public void setLeechSeed(boolean leechSeed) {
        this.leechSeed = leechSeed;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

}
