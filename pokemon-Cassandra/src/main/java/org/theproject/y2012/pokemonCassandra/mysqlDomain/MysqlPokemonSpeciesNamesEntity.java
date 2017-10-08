package org.theproject.y2012.pokemonCassandra.mysqlDomain;

import javax.persistence.*;

@Entity
@Table(name = "pokemon_species_names", schema = "pokedex2012", catalog = "")
@IdClass(MysqlPokemonSpeciesNamesEntityPK.class)
public class MysqlPokemonSpeciesNamesEntity {
    private Integer pokemonSpeciesId;
    private Integer localLanguageId;
    private String name;
    private String genus;

    @Id
    @Column(name = "pokemon_species_id")
    public Integer getPokemonSpeciesId() {
        return pokemonSpeciesId;
    }

    public void setPokemonSpeciesId(Integer pokemonSpeciesId) {
        this.pokemonSpeciesId = pokemonSpeciesId;
    }

    @Id
    @Column(name = "local_language_id")
    public Integer getLocalLanguageId() {
        return localLanguageId;
    }

    public void setLocalLanguageId(Integer localLanguageId) {
        this.localLanguageId = localLanguageId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "genus")
    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MysqlPokemonSpeciesNamesEntity that = (MysqlPokemonSpeciesNamesEntity) o;

        if (pokemonSpeciesId != null ? !pokemonSpeciesId.equals(that.pokemonSpeciesId) : that.pokemonSpeciesId != null)
            return false;
        if (localLanguageId != null ? !localLanguageId.equals(that.localLanguageId) : that.localLanguageId != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (genus != null ? !genus.equals(that.genus) : that.genus != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pokemonSpeciesId != null ? pokemonSpeciesId.hashCode() : 0;
        result = 31 * result + (localLanguageId != null ? localLanguageId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (genus != null ? genus.hashCode() : 0);
        return result;
    }
}
