package fr.xephi.authme.libs.org.postgresql.replication.fluent;

import fr.xephi.authme.libs.org.postgresql.replication.LogSequenceNumber;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface CommonOptions {
   @Nullable
   String getSlotName();

   LogSequenceNumber getStartLSNPosition();

   int getStatusInterval();
}
