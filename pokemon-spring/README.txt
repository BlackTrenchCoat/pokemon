Generate DBRE .hbm.xml files.  
This step isn't necessary, might be helpful for debugging.

mvn hibernate3:hbm2hbmxml

Generate Java classes

mvn hibernate3:hbm2java

Compile:

mvn compile

To create target/hibernate3/generated-mappings/hibernate.cfg.xml:

mvn hibernate3:hbm2cfgxml

Typical build:

mvn clean hibernate3:hbm2java compile hibernate3:hbm2cfgxml

To run the main program:

mvn exec:java -Dexec.mainClass=pokemon.HibernatePokedexMain

Get documentation about the hibernate3 Maven plugin (DOESN'T WORK):

mvn help:describe -Dplugin=hibernate3-maven-plugin -Dfull

Re-download dependencies:

mvn dependency:purge-local-repository