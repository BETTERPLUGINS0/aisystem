package fr.xephi.authme.libs.org.postgresql.replication.fluent;

import fr.xephi.authme.libs.org.postgresql.replication.fluent.logical.ChainedLogicalStreamBuilder;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.physical.ChainedPhysicalStreamBuilder;

public interface ChainedStreamBuilder {
   ChainedLogicalStreamBuilder logical();

   ChainedPhysicalStreamBuilder physical();
}
