package fr.xephi.authme.libs.org.postgresql.replication.fluent;

import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.logical.ChainedLogicalCreateSlotBuilder;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.logical.LogicalCreateSlotBuilder;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.physical.ChainedPhysicalCreateSlotBuilder;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.physical.PhysicalCreateSlotBuilder;

public class ReplicationCreateSlotBuilder implements ChainedCreateReplicationSlotBuilder {
   private final BaseConnection baseConnection;

   public ReplicationCreateSlotBuilder(BaseConnection baseConnection) {
      this.baseConnection = baseConnection;
   }

   public ChainedLogicalCreateSlotBuilder logical() {
      return new LogicalCreateSlotBuilder(this.baseConnection);
   }

   public ChainedPhysicalCreateSlotBuilder physical() {
      return new PhysicalCreateSlotBuilder(this.baseConnection);
   }
}
