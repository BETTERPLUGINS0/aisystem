package com.nisovin.shopkeepers.util.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LogDetectionHandler extends Handler {
   @Nullable
   private LogRecord lastLogRecord = null;

   public boolean hasLogRecord() {
      return this.lastLogRecord != null;
   }

   @Nullable
   public LogRecord getLastLogRecord() {
      return this.lastLogRecord;
   }

   public void reset() {
      this.lastLogRecord = null;
   }

   public void publish(@Nullable LogRecord record) {
      assert record != null;

      this.lastLogRecord = record;
   }

   public void flush() {
   }

   public void close() throws SecurityException {
      this.reset();
   }
}
