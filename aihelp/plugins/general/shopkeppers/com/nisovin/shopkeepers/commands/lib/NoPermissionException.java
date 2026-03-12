package com.nisovin.shopkeepers.commands.lib;

import com.nisovin.shopkeepers.text.Text;

public class NoPermissionException extends CommandException {
   private static final long serialVersionUID = -4918361876059852723L;

   public NoPermissionException(Text message) {
      super(message);
   }

   public NoPermissionException(Text message, Throwable cause) {
      super(message, cause);
   }
}
