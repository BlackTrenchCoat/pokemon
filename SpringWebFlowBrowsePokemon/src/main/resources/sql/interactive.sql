select ps.id, ps.identifier, g.identifier
from pokemon_species ps, generations g
where ps.generation_id = g.id
and g.identifier in ('generation-i', 'generation-ii')
order by ps.identifier;

-- Get fields to display

select p.height, p.weight, p.base_experience, 
pspecies.identifier, pspecies.base_happiness,
pc.identifier, pshapes.identifier, ph.identifier
from pokemon p, pokemon_species pspecies, pokemon_colors pc, pokemon_shapes pshapes, pokemon_habitats ph
where p.species_id = 65 -- alakazam
and p.species_id = pspecies.id
and pspecies.color_id = pc.id
and pspecies.shape_id = pshapes.id
and pspecies.habitat_id = ph.id;

-- This fails because pokemon_species.habitat_id is null:

select p.species_id, p.height, p.weight, p.base_experience, 
pspecies.identifier, pspecies.base_happiness,
pc.identifier, pshapes.identifier, ph.identifier
from pokemon p, pokemon_species pspecies, pokemon_colors pc, pokemon_shapes pshapes, pokemon_habitats ph
where p.species_id = 626 -- bouffalant
and p.species_id = pspecies.id
and pspecies.color_id = pc.id
and pspecies.shape_id = pshapes.id
and pspecies.habitat_id = ph.id;

select p.species_id, p.height, p.weight, p.base_experience, 
pspecies.identifier, pspecies.base_happiness,
pc.identifier, pshapes.identifier, ph.identifier
from pokemon p, pokemon_species pspecies, pokemon_colors pc, pokemon_shapes pshapes, pokemon_habitats ph
where p.species_id = 626 -- bouffalant
and p.species_id = pspecies.id
and pspecies.color_id = pc.id
and pspecies.shape_id = pshapes.id
and (pspecies.habitat_id = ph.id or pspecies.habitat_id is null);

select * from pokemon where id = 626;

select p.species_id, p.height, p.weight, p.base_experience,
species.identifier, species.base_happiness
from pokemon p, pokemon_species species
where p.species_id = 626 -- bouffalant
and p.species_id = species.id;

select p.species_id, p.height, p.weight, p.base_experience,
species.identifier, species.base_happiness, 
pc.identifier
from pokemon p, pokemon_species species, pokemon_colors pc
where p.species_id = 626 -- bouffalant
and p.species_id = species.id
and species.color_id = pc.id;

select p.species_id, p.height, p.weight, p.base_experience,
species.identifier, species.base_happiness, 
pc.identifier, shapes.identifier
from pokemon p, pokemon_species species, pokemon_colors pc, pokemon_shapes shapes
where p.species_id = 626 -- bouffalant
and p.species_id = species.id
and species.color_id = pc.id
and species.shape_id = shapes.id;

select * from pokemon_species;

-- Minimal query to reproduce the problem

select species.id, species.identifier, ph.identifier
from pokemon_species species, pokemon_habitats ph
where species.id = 626
and species.habitat_id = ph.id;

-- The same thing With 'inner join' syntax

select species.id, species.identifier, ph.identifier
from pokemon_species species inner join pokemon_habitats ph
on species.habitat_id = ph.id
and species.id = 626;

-- Outer join
-- This doesn't seem to do what I want.  It produces many rows of output.
-- I just want one with habitat = null

select species.id, species.identifier, ph.identifier
from pokemon_species species right outer join pokemon_habitats ph
on species.habitat_id = ph.id
and species.id = 626;

-- Add stats.  Also, do habitat separately.

select p.species_id, p.height, p.weight, p.base_experience, 
pspecies.identifier, pspecies.base_happiness,
pc.identifier, pshapes.identifier
from pokemon p, pokemon_species pspecies, pokemon_colors pc, pokemon_shapes pshapes
where p.species_id = 626 -- bouffalant
and p.species_id = pspecies.id
and pspecies.color_id = pc.id
and pspecies.shape_id = pshapes.id;

select * from stat_names
where local_language_id = 9
order by stat_id;