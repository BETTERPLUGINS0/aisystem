package ac.grim.grimac.platform.bukkit.manager;

import ac.grim.grimac.platform.api.manager.MessagePlaceHolderManager;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.bukkit.player.BukkitPlatformPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.reflection.Reflection;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class BukkitMessagePlaceHolderManager implements MessagePlaceHolderManager {
   public static final boolean hasPlaceholderAPI = Reflection.getClassByNameWithoutException("me.clip.placeholderapi.PlaceholderAPI") != null;

   @NotNull
   public String replacePlaceholders(@Nullable PlatformPlayer player, @NotNull String string) {
      if (!hasPlaceholderAPI) {
         return string;
      } else {
         Player var10000;
         if (player instanceof BukkitPlatformPlayer) {
            BukkitPlatformPlayer bukkitPlatformPlayer = (BukkitPlatformPlayer)player;
            var10000 = bukkitPlatformPlayer.getBukkitPlayer();
         } else {
            var10000 = null;
         }

         return PlaceholderAPI.setPlaceholders(var10000, string);
      }
   }
}
