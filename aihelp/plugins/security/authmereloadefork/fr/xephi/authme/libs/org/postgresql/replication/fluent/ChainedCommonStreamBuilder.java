package fr.xephi.authme.libs.org.postgresql.replication.fluent;

import fr.xephi.authme.libs.org.postgresql.replication.LogSequenceNumber;
import java.util.concurrent.TimeUnit;

public interface ChainedCommonStreamBuilder<T extends ChainedCommonStreamBuilder<T>> {
   T withSlotName(String var1);

   T withStatusInterval(int var1, TimeUnit var2);

   T withStartPosition(LogSequenceNumber var1);
}
