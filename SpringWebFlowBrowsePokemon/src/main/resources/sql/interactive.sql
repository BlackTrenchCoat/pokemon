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