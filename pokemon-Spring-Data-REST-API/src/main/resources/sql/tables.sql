-- dev

use pokemon2012;

drop table if exists move;

create table move (
	id int not null auto_increment, -- Unique automatically generated value
	pokedex_id int not null,	-- pokedex.move.id
	pokemon_id int,                 -- FK to pokemon.id
	name varchar(20),
	power int,
	pp int,
	accuracy int,
	damage_class varchar(16),
	effect varchar(5120),
	effect_chance int,
	level int,
	priority int,
	target varchar(32),
	type varchar(16),
	primary key(id)
);

drop table if exists pokemon;

create table pokemon (
	id int not null auto_increment, -- Unique automatically generated value
	pokedex_id int not null,	-- pokedex.pokemon.id
	name varchar(20),
	-- stats
	attack int,
	defense int,
	special_attack int,
	special_defense int,
	hp int,
	max_hp int,
	speed int,
	-- end stats
	level int,
	color varchar(6),
	exp int,
	happiness int,
	habitat varchar(16),
	height int,
	lock_on boolean,
	mind_reader boolean,
	nightmare boolean,
	non_volatile_status varchar(10),
	perish_song boolean,
	species varchar(16),
	taunt boolean,
	torment boolean,
	type1 varchar(8),
	type2 varchar(8),
	weight int,
	DTYPE varchar(20),	-- some questions about this
	primary key(id)
);

alter table move
add constraint fk_move
foreign key (pokemon_id) references pokemon(id)
on update cascade
on delete cascade;

-- test

use pokemon2012_test;

drop table if exists move;

create table move (
	id int not null auto_increment, -- Unique automatically generated value
	pokedex_id int not null,	-- pokedex.move.id
	pokemon_id int,                 -- FK to pokemon.id
	name varchar(20),
	power int,
	pp int,
	accuracy int,
	damage_class varchar(16),
	effect varchar(5120),
	effect_chance int,
	level int,
	priority int,
	target varchar(32),
	type varchar(16),
	primary key(id)
);

drop table if exists pokemon;

create table pokemon (
	id int not null auto_increment, -- Unique automatically generated value
	pokedex_id int not null,	-- pokedex.pokemon.id
	name varchar(20),
	-- stats
	attack int,
	defense int,
	special_attack int,
	special_defense int,
	hp int,
	max_hp int,
	speed int,
	-- end stats
	level int,
	color varchar(6),
	exp int,
	happiness int,
	habitat varchar(16),
	height int,
	lock_on boolean,
	mind_reader boolean,
	nightmare boolean,
	non_volatile_status varchar(10),
	perish_song boolean,
	species varchar(16),
	taunt boolean,
	torment boolean,
	type1 varchar(8),
	type2 varchar(8),
	weight int,
	DTYPE varchar(20),	-- some questions about this
	primary key(id)
);

alter table move
add constraint fk_move
foreign key (pokemon_id) references pokemon(id)
on update cascade
on delete cascade;
