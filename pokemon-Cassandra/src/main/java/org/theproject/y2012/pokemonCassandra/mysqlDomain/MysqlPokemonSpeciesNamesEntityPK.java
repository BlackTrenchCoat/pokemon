package org.theproject.y2012.pokemonCassandra.mysqlDomain;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class MysqlPokemonSpeciesNamesEntityPK implements Serializable {
    private Integer pokemonSpeciesId;
    private Integer localLanguageId;

    @Column(name = "pokemon_species_id")
    @Id
    public Integer getPokemonSpeciesId() {
        return pokemonSpeciesId;
    }

    public void setPokemonSpeciesId(Integer pokemonSpeciesId) {
        this.pokemonSpeciesId = pokemonSpeciesId;
    }

    @Column(name = "local_language_id")
    @Id
    public Integer getLocalLanguageId() {
        return localLanguageId;
    }

    public void setLocalLanguageId(Integer localLanguageId) {
        this.localLanguageId = localLanguageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MysqlPokemonSpeciesNamesEntityPK that = (MysqlPokemonSpeciesNamesEntityPK) o;

        if (pokemonSpeciesId != null ? !pokemonSpeciesId.equals(that.pokemonSpeciesId) : that.pokemonSpeciesId != null)
            return false;
        if (localLanguageId != null ? !localLanguageId.equals(that.localLanguageId) : that.localLanguageId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pokemonSpeciesId != null ? pokemonSpeciesId.hashCode() : 0;
        result = 31 * result + (localLanguageId != null ? localLanguageId.hashCode() : 0);
        return result;
    }
}
