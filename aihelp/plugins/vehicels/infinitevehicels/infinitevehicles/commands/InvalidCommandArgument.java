package me.PM2.infinitevehicles.commands;

import me.PM2.infinitevehicles.locales.MessageKey;
import me.PM2.infinitevehicles.locales.MessageKeyProvider;

public class InvalidCommandArgument extends RuntimeException {
   final boolean showSyntax;
   final MessageKey key;
   final String[] replacements;

   public InvalidCommandArgument() {
      this((String)null, true);
   }

   public InvalidCommandArgument(boolean showSyntax) {
      this((String)null, var1);
   }

   public InvalidCommandArgument(MessageKeyProvider key, String... replacements) {
      this(var1.getMessageKey(), var2);
   }

   public InvalidCommandArgument(MessageKey key, String... replacements) {
      this(var1, true, var2);
   }

   public InvalidCommandArgument(MessageKeyProvider key, boolean showSyntax, String... replacements) {
      this(var1.getMessageKey(), var2, var3);
   }

   public InvalidCommandArgument(MessageKey key, boolean showSyntax, String... replacements) {
      super(var1.getKey(), (Throwable)null, false, false);
      this.showSyntax = var2;
      this.key = var1;
      this.replacements = var3;
   }

   public InvalidCommandArgument(String message) {
      this(var1, true);
   }

   public InvalidCommandArgument(String message, boolean showSyntax) {
      super(var1, (Throwable)null, false, false);
      this.showSyntax = var2;
      this.replacements = null;
      this.key = null;
   }
}
