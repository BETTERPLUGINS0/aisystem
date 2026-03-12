package com.nisovin.shopkeepers.api.user;

import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface User {
   UUID getUniqueId();

   String getLastKnownName();

   String getName();

   String getDisplayName();

   boolean isOnline();

   @Nullable
   Player getPlayer();

   OfflinePlayer getOfflinePlayer();
}
