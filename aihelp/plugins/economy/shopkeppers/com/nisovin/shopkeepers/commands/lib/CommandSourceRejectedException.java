package com.nisovin.shopkeepers.commands.lib;

import com.nisovin.shopkeepers.text.Text;

public class CommandSourceRejectedException extends CommandException {
   private static final long serialVersionUID = -3136267542969449218L;

   public CommandSourceRejectedException(Text message) {
      super(message);
   }

   public CommandSourceRejectedException(Text message, Throwable cause) {
      super(message, cause);
   }
}
