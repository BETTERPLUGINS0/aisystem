package fr.xephi.authme.libs.org.postgresql.replication.fluent.physical;

import fr.xephi.authme.libs.org.postgresql.replication.LogSequenceNumber;
import fr.xephi.authme.libs.org.postgresql.replication.PGReplicationStream;
import fr.xephi.authme.libs.org.postgresql.replication.fluent.AbstractStreamBuilder;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PhysicalStreamBuilder extends AbstractStreamBuilder<ChainedPhysicalStreamBuilder> implements ChainedPhysicalStreamBuilder, PhysicalReplicationOptions {
   private final StartPhysicalReplicationCallback startCallback;

   public PhysicalStreamBuilder(StartPhysicalReplicationCallback startCallback) {
      this.startCallback = startCallback;
   }

   protected ChainedPhysicalStreamBuilder self() {
      return this;
   }

   public PGReplicationStream start() throws SQLException {
      return this.startCallback.start(this);
   }

   @Nullable
   public String getSlotName() {
      return this.slotName;
   }

   public LogSequenceNumber getStartLSNPosition() {
      return this.startPosition;
   }

   public int getStatusInterval() {
      return this.statusIntervalMs;
   }
}
