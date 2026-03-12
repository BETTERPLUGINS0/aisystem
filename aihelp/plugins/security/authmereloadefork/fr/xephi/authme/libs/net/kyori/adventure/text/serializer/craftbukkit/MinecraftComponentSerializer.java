package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.craftbukkit;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.NotNull;

/** @deprecated */
@Deprecated
public final class MinecraftComponentSerializer implements ComponentSerializer<Component, Component, Object> {
   private static final MinecraftComponentSerializer INSTANCE = new MinecraftComponentSerializer();
   private final fr.xephi.authme.libs.net.kyori.adventure.platform.bukkit.MinecraftComponentSerializer realSerial = fr.xephi.authme.libs.net.kyori.adventure.platform.bukkit.MinecraftComponentSerializer.get();

   public static boolean isSupported() {
      return fr.xephi.authme.libs.net.kyori.adventure.platform.bukkit.MinecraftComponentSerializer.isSupported();
   }

   @NotNull
   public static MinecraftComponentSerializer get() {
      return INSTANCE;
   }

   @NotNull
   public Component deserialize(@NotNull final Object input) {
      return this.realSerial.deserialize(input);
   }

   @NotNull
   public Object serialize(@NotNull final Component component) {
      return this.realSerial.serialize(component);
   }
}
