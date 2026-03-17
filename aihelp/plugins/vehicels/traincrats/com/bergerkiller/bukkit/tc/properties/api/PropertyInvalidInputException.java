package com.bergerkiller.bukkit.tc.properties.api;

import com.bergerkiller.bukkit.common.localization.LocalizationEnum;

public class PropertyInvalidInputException extends RuntimeException {
   private static final long serialVersionUID = -8618056967820261214L;

   public PropertyInvalidInputException(String message) {
      super(message);
   }

   public PropertyInvalidInputException(LocalizationEnum localization, String... arguments) {
      super(localization.get(arguments));
   }
}
