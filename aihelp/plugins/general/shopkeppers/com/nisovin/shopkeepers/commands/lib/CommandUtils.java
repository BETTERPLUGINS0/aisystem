package com.nisovin.shopkeepers.commands.lib;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Locale;

public final class CommandUtils {
   public static String normalize(String label) {
      Validate.notNull(label, (String)"label is null");
      return label.toLowerCase(Locale.ROOT);
   }

   private CommandUtils() {
   }
}
