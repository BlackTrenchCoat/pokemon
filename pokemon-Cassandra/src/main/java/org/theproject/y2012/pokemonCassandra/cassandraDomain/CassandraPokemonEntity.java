package org.theproject.y2012.pokemonCassandra.cassandraDomain;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "pokemon")
public class CassandraPokemonEntity {

    @PrimaryKey
    private final Integer id;

    @Column(value="species_id")
    private final Integer speciesId;

    private final Integer height;
    private final Integer weight;

    @Column(value="base_experience")
    private final Integer baseExperience;

    @Column(value="p_order")
    private final Integer order;

    @Column(value="is_default")
    private final Boolean isDefault;

    public CassandraPokemonEntity(Integer id, Integer speciesId, Integer height, Integer weight, Integer baseExperience, Integer order, Boolean isDefault) {
        this.id = id;
        this.speciesId = speciesId;
        this.height = height;
        this.weight = weight;
        this.baseExperience = baseExperience;
        this.order = order;
        this.isDefault = isDefault;
    }

    public Integer getId() {
        return id;
    }

    public Integer getSpeciesId() {
        return speciesId;
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

    public Integer getOrder() {
        return order;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        return "CassandraPokemonEntity{" +
                "id=" + id +
                ", speciesId=" + speciesId +
                ", height=" + height +
                ", weight=" + weight +
                ", baseExperience=" + baseExperience +
                ", order=" + order +
                ", isDefault=" + isDefault +
                '}';
    }
}
