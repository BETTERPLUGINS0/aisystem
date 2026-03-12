package fr.xephi.authme.libs.org.postgresql.replication.fluent;

import fr.xephi.authme.libs.org.postgresql.replication.LogSequenceNumber;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractStreamBuilder<T extends ChainedCommonStreamBuilder<T>> implements ChainedCommonStreamBuilder<T> {
   private static final int DEFAULT_STATUS_INTERVAL;
   protected int statusIntervalMs;
   protected LogSequenceNumber startPosition;
   @Nullable
   protected String slotName;

   public AbstractStreamBuilder() {
      this.statusIntervalMs = DEFAULT_STATUS_INTERVAL;
      this.startPosition = LogSequenceNumber.INVALID_LSN;
   }

   protected abstract T self();

   public T withStatusInterval(int time, TimeUnit format) {
      this.statusIntervalMs = (int)TimeUnit.MILLISECONDS.convert((long)time, format);
      return this.self();
   }

   public T withStartPosition(LogSequenceNumber lsn) {
      this.startPosition = lsn;
      return this.self();
   }

   public T withSlotName(String slotName) {
      this.slotName = slotName;
      return this.self();
   }

   static {
      DEFAULT_STATUS_INTERVAL = (int)TimeUnit.SECONDS.toMillis(10L);
   }
}
