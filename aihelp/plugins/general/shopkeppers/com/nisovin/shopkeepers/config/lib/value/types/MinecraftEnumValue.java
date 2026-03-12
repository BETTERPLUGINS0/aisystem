package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.util.bukkit.MinecraftEnumUtils;

public class MinecraftEnumValue<E extends Enum<E>> extends EnumValue<E> {
   public MinecraftEnumValue(Class<E> enumClass) {
      super(enumClass);
   }

   protected String normalize(String input) {
      return MinecraftEnumUtils.normalizeEnumName(input);
   }
}
