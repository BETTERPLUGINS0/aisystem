package com.nisovin.shopkeepers.config.lib.value;

import java.util.Collections;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ValueLoadException extends Exception {
   private static final long serialVersionUID = -3068903999888105245L;
   private final List<? extends String> extraMessages;

   public ValueLoadException(@Nullable String message) {
      this(message, (List)null, (Throwable)null);
   }

   public ValueLoadException(@Nullable String message, @Nullable Throwable cause) {
      this(message, (List)null, cause);
   }

   public ValueLoadException(@Nullable String message, @Nullable List<? extends String> extraMessages) {
      this(message, extraMessages, (Throwable)null);
   }

   public ValueLoadException(@Nullable String message, @Nullable List<? extends String> extraMessages, @Nullable Throwable cause) {
      super(message, cause);
      this.extraMessages = extraMessages != null ? extraMessages : Collections.emptyList();
   }

   public List<? extends String> getExtraMessages() {
      return this.extraMessages;
   }
}
