package com.nisovin.shopkeepers.util.logging;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class NullLogger extends Logger {
   private static final Logger INSTANCE = new NullLogger();

   public static Logger getInstance() {
      return INSTANCE;
   }

   private NullLogger() {
      super((String)Unsafe.assertNonNull(NullLogger.class.getCanonicalName()), (String)null);
      super.setLevel(Level.OFF);
   }

   public void setLevel(@Nullable Level level) {
      throw new UnsupportedOperationException("This logger cannot be modified!");
   }

   public void log(@Nullable LogRecord logRecord) {
      assert logRecord != null;

   }
}
