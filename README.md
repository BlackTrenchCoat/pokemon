pokemon
=======

Four Pokemon-related Spring projects based on Alex Munro's Pokedex
database (See https://github.com/veekun/pokedex).

pokemon-REST-API was written in 2017 and I am still tinkering with it.
It uses modern tools and techniques such as Spring Boot, Spring Data JPA
and @RestController.

In pokemon-Spring-Data-REST-API I reimplemented pokemon-REST-API using
Spring Data REST.

pokemon-spring and SpringWebFlowBrowsePokemon are spare-time projects
from 2012 and 2013 which in a lot of ways don't represent modern best
practices.

Both were originally configured with a combination of annotations and
XML.  In 2016 I rewrote SpringWebFlowBrowsePokemon to use Java
configuration.

Both of these projects use ancient Hibernate 3 and employ Hibernate
Tools database reverse engineering relying on a DBRE Maven plugin.
