package emanondev.itemtag.activity.action;

import emanondev.itemtag.activity.ActionType;
import java.util.Locale;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlaySoundActionType extends ActionType {
   public PlaySoundActionType() {
      super("playsound");
   }

   @NotNull
   public ActionType.Action read(@NotNull String info) {
      return new PlaySoundActionType.Action(info);
   }

   private class Action extends ActionType.Action {
      private final Sound sound;
      private final float volume;
      private final float pitch;
      private final boolean self;
      private final SoundCategory category;

      public Action(@NotNull String param2) {
         super(info);
         String[] values = info.split(" ");
         if (values.length == 0) {
            throw new IllegalArgumentException("Invalid format '" + info + "' must be '<sound> [volume] [pitch] [category]'");
         } else {
            try {
               this.sound = Sound.valueOf(values[0].toUpperCase(Locale.ENGLISH));
            } catch (Exception var7) {
               throw new IllegalArgumentException("Invalid sound '" + values[0] + "' must be '<sound> [volume] [pitch] [category]' you can find sound here https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html");
            }

            try {
               this.volume = values.length <= 1 ? 1.0F : Float.parseFloat(values[1]);
               if (this.volume <= 0.0F) {
                  throw new IllegalArgumentException();
               }
            } catch (Exception var8) {
               throw new IllegalArgumentException("Invalid volume '" + values[1] + "' must be '<sound> [volume] [pitch] [category]' volume must be bigger than 0");
            }

            try {
               this.pitch = values.length <= 2 ? 1.0F : Float.parseFloat(values[2]);
               if (this.pitch <= 0.0F) {
                  throw new IllegalArgumentException();
               }
            } catch (Exception var6) {
               throw new IllegalArgumentException("Invalid pitch '" + values[2] + "' must be '<sound> [volume] [pitch] [category]' pitch must be bigger than 0.2 and lower than 2");
            }

            this.self = values.length > 3 && Boolean.parseBoolean(values[3]);

            try {
               this.category = values.length <= 4 ? SoundCategory.MASTER : SoundCategory.valueOf(values[4].toLowerCase(Locale.ENGLISH));
            } catch (Exception var5) {
               throw new IllegalArgumentException("Invalid sound category '" + values[4] + "' must be '<sound> [volume] [pitch] [category]' you can find sound here https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/SoundCategory.html");
            }
         }
      }

      public boolean execute(@NotNull Player player, @NotNull ItemStack item, Event event) {
         if (this.self) {
            player.playSound(player.getLocation(), this.sound, this.category, this.volume, this.pitch);
         } else {
            player.getWorld().playSound(player.getLocation(), this.sound, this.category, this.volume, this.pitch);
         }

         return true;
      }
   }
}
