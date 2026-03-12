package com.nisovin.shopkeepers.util.data.serialization.bukkit;

import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class UnknownMaterialException extends InvalidDataException {
   private static final long serialVersionUID = 501465265297838645L;

   public UnknownMaterialException(@Nullable String message) {
      super(message);
   }

   public UnknownMaterialException(@Nullable String message, @Nullable Throwable cause) {
      super(message, cause);
   }
}
