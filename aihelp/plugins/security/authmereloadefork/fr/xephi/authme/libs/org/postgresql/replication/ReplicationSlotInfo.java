package fr.xephi.authme.libs.org.postgresql.replication;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class ReplicationSlotInfo {
   private final String slotName;
   private final ReplicationType replicationType;
   private final LogSequenceNumber consistentPoint;
   @Nullable
   private final String snapshotName;
   @Nullable
   private final String outputPlugin;

   public ReplicationSlotInfo(String slotName, ReplicationType replicationType, LogSequenceNumber consistentPoint, @Nullable String snapshotName, @Nullable String outputPlugin) {
      this.slotName = slotName;
      this.replicationType = replicationType;
      this.consistentPoint = consistentPoint;
      this.snapshotName = snapshotName;
      this.outputPlugin = outputPlugin;
   }

   public String getSlotName() {
      return this.slotName;
   }

   public ReplicationType getReplicationType() {
      return this.replicationType;
   }

   public LogSequenceNumber getConsistentPoint() {
      return this.consistentPoint;
   }

   @Nullable
   public String getSnapshotName() {
      return this.snapshotName;
   }

   @Nullable
   public String getOutputPlugin() {
      return this.outputPlugin;
   }
}
