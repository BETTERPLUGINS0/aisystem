package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.craftbukkit;

import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

/** @deprecated */
@Deprecated
public final class BukkitComponentSerializer {
   private BukkitComponentSerializer() {
   }

   @NotNull
   public static LegacyComponentSerializer legacy() {
      return fr.xephi.authme.libs.net.kyori.adventure.platform.bukkit.BukkitComponentSerializer.legacy();
   }

   @NotNull
   public static GsonComponentSerializer gson() {
      return fr.xephi.authme.libs.net.kyori.adventure.platform.bukkit.BukkitComponentSerializer.gson();
   }
}
