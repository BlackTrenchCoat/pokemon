create keyspace if not exists pokedex2012 
with replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1 };
