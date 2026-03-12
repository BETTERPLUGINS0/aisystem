package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.ColorUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.LogManager;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public class BukkitLogManager extends LogManager {
   private final String prefixText;

   public BukkitLogManager() {
      this.prefixText = ColorUtil.toString(NamedTextColor.AQUA) + "[packetevents] " + ColorUtil.toString(NamedTextColor.WHITE);
   }

   protected void log(Level level, @Nullable NamedTextColor color, String message) {
      Bukkit.getConsoleSender().sendMessage(this.prefixText + ColorUtil.toString(color) + message);
   }

   public void info(String message) {
      this.log(Level.INFO, NamedTextColor.WHITE, message);
   }

   public void warn(final String message) {
      this.log(Level.WARNING, NamedTextColor.YELLOW, message);
   }

   public void severe(String message) {
      this.log(Level.SEVERE, NamedTextColor.RED, message);
   }

   public void debug(String message) {
      if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
         this.log(Level.FINE, NamedTextColor.GRAY, message);
      }

   }
}
