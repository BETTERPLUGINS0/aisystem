package com.nisovin.shopkeepers.commands.lib;

import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public class CommandException extends Exception {
   private static final long serialVersionUID = 3021047528891246476L;
   private final Text messageText;

   public CommandException(Text message) {
      this(message, (Throwable)null);
   }

   private static String validateMessage(Text message) {
      Validate.notNull(message, (String)"message is null");
      String plainMessage = message.toPlainText();
      Validate.notEmpty(plainMessage, "message is empty");
      return plainMessage;
   }

   public CommandException(Text message, @Nullable Throwable cause) {
      super(validateMessage(message), cause);
      this.messageText = message.copy();
   }

   public final Text getMessageText() {
      return this.messageText;
   }
}
