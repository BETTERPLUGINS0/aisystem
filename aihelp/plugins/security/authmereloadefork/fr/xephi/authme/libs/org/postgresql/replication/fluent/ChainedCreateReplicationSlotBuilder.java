package fr.xephi.authme.libs.org.postgresql.replication.fluent;

import fr.xephi.authme.libs.org.postgresql.replication.fluent.logical.ChainedLogicalCreateSlotBuilder;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.physical.ChainedPhysicalCreateSlotBuilder;

public interface ChainedCreateReplicationSlotBuilder {
   ChainedLogicalCreateSlotBuilder logical();

   ChainedPhysicalCreateSlotBuilder physical();
}
