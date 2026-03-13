package com.nisovin.shopkeepers.api.types;

import java.util.Collection;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface Type {
   String getIdentifier();

   Collection<? extends String> getAliases();

   default String getDisplayName() {
      return this.getIdentifier();
   }

   @Nullable
   String getPermission();

   boolean hasPermission(Player var1);

   boolean isEnabled();

   boolean matches(String var1);
}
