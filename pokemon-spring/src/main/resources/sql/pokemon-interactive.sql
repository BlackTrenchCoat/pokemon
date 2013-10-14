-- POKEDEX

select * from moves order by name;

select m.identifier, mep.short_effect, mep.effect
from moves m, move_effect_prose mep
where m.effect_id = mep.move_effect_id
and mep.local_language_id = 9
order by m.identifier;

select ps.identifier
from pokemon p, pokemon_species ps
where p.species_id = ps.id
and p.species_id = 1; -- Bulbasaur

select * from pokemon_species_names;

select * from pokemon_species_names
where local_language_id = 9;

-- Get name and species

select name, genus
from pokemon_species_names
where local_language_id = 9
and pokemon_species_id = 127;

-- Get the generation id of Mewtwo (150)

select generation_id
from pokemon_species
where identifier = 'mewtwo';

-- types

select * from pokemon_types;

-- get the 2 types of Mewtwo

select pt.slot, pt.type_id, t.identifier
from pokemon_types pt, types t
where pt.pokemon_id = 150
and pt.type_id = t.id;

-- Bulbasaur

select pt.slot, pt.type_id, t.identifier
from pokemon_types pt, types t
where pt.pokemon_id = 1
and pt.type_id = t.id;

-- Get color

select pc.identifier
from pokemon_species ps, pokemon_colors pc
where ps.id = 150
and ps.color_id = pc.id;

-- Get stats

select s.identifier, ps.base_stat
from pokemon_stats ps, stats s
where ps.pokemon_id = 21 -- Spearow
and ps.stat_id = s.id;

select * from pokemon_species;


-- POKEMON

select * from move;

select * from pokemon where pokedex_id = 167;

select * from move where pokemon_id in (2, 9);

select * from pokemon;

select count(*) from pokemon;

