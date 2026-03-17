package me.PM2.infinitevehicles.commands;

import me.PM2.infinitevehicles.locales.MessageKey;
import me.PM2.infinitevehicles.locales.MessageKeyProvider;

public class ConditionFailedException extends InvalidCommandArgument {
   public ConditionFailedException() {
      super(false);
   }

   public ConditionFailedException(MessageKeyProvider key, String... replacements) {
      super(var1, false, var2);
   }

   public ConditionFailedException(MessageKey key, String... replacements) {
      super(var1, false, var2);
   }

   public ConditionFailedException(String message) {
      super(var1, false);
   }
}
