package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class LogManager {
   protected static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-ORX]");

   protected void log(Level level, @Nullable NamedTextColor color, String message) {
      message = STRIP_COLOR_PATTERN.matcher(message).replaceAll("");
      PacketEvents.getAPI().getLogger().log(level, color != null ? color.toString() : "" + message);
   }

   public void info(String message) {
      this.log(Level.INFO, (NamedTextColor)null, message);
   }

   public void warn(final String message) {
      this.log(Level.WARNING, (NamedTextColor)null, message);
   }

   public void severe(String message) {
      this.log(Level.SEVERE, (NamedTextColor)null, message);
   }

   public void debug(String message) {
      if (this.isDebug()) {
         this.log(Level.FINE, (NamedTextColor)null, message);
      }

   }

   public boolean isDebug() {
      return PacketEvents.getAPI().getSettings().isDebugEnabled();
   }
}
