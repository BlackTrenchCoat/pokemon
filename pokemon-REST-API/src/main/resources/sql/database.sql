create database pokemon2012_dev;
use pokemon2012_dev;

grant all on pokemon2012_dev.* to pokemon2012@localhost identified by 'pokemon2012';

create database pokemon2012_test;
use pokemon2012_test;

grant all on pokemon2012_test.* to pokemon2012@localhost identified by 'pokemon2012';
