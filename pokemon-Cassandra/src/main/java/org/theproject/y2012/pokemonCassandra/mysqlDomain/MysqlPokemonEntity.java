package org.theproject.y2012.pokemonCassandra.mysqlDomain;

import javax.persistence.*;

@Entity
@Table(name = "pokemon", schema = "pokedex2012", catalog = "")
public class MysqlPokemonEntity {
    private Integer id;
    private Integer speciesId;
    private Integer height;
    private Integer weight;
    private Integer baseExperience;
    private Integer order;
    private Byte isDefault;

    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "species_id")
    public Integer getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(Integer speciesId) {
        this.speciesId = speciesId;
    }

    @Basic
    @Column(name = "height")
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Basic
    @Column(name = "weight")
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Basic
    @Column(name = "base_experience")
    public Integer getBaseExperience() {
        return baseExperience;
    }

    public void setBaseExperience(Integer baseExperience) {
        this.baseExperience = baseExperience;
    }

    @Basic
    @Column(name = "order")
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Basic
    @Column(name = "is_default")
    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MysqlPokemonEntity that = (MysqlPokemonEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (speciesId != null ? !speciesId.equals(that.speciesId) : that.speciesId != null) return false;
        if (height != null ? !height.equals(that.height) : that.height != null) return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;
        if (baseExperience != null ? !baseExperience.equals(that.baseExperience) : that.baseExperience != null)
            return false;
        if (order != null ? !order.equals(that.order) : that.order != null) return false;
        if (isDefault != null ? !isDefault.equals(that.isDefault) : that.isDefault != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (speciesId != null ? speciesId.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (baseExperience != null ? baseExperience.hashCode() : 0);
        result = 31 * result + (order != null ? order.hashCode() : 0);
        result = 31 * result + (isDefault != null ? isDefault.hashCode() : 0);
        return result;
    }
}
