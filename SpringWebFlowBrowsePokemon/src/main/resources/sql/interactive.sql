select ps.identifier
from pokemon_species ps, generations g
where ps.generation_id = g.id
and g.identifier in ('generation-i', 'generation-ii')
order by ps.identifier;