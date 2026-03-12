package emanondev.itemtag.actions;

import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.utility.CompleteUtility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoundAction extends Action {
   public SoundAction() {
      super("sound");
   }

   public void validateInfo(String text) {
      if (text.isEmpty()) {
         throw new IllegalStateException();
      } else {
         String[] args = text.split(" ");
         if (args.length > 4) {
            throw new IllegalStateException();
         } else {
            Sound.valueOf(args[0].toUpperCase());
            if (args.length >= 2 && Float.parseFloat(args[1]) <= 0.0F) {
               throw new IllegalStateException();
            } else if (args.length >= 3 && Float.parseFloat(args[2]) <= 0.0F) {
               throw new IllegalStateException();
            } else {
               if (args.length == 4) {
                  String var3 = args[3].toLowerCase(Locale.ENGLISH);
                  byte var4 = -1;
                  switch(var3.hashCode()) {
                  case 3569038:
                     if (var3.equals("true")) {
                        var4 = 0;
                     }
                     break;
                  case 97196323:
                     if (var3.equals("false")) {
                        var4 = 1;
                     }
                  }

                  switch(var4) {
                  case 0:
                  case 1:
                     break;
                  default:
                     throw new IllegalStateException();
                  }
               }

            }
         }
      }
   }

   public void execute(Player player, String text) {
      String[] args = text.split(" ");
      boolean self = false;
      float volume = 1.0F;
      float pitch = 1.0F;
      Sound sound = Sound.valueOf(args[0].toUpperCase());
      if (args.length >= 2) {
         volume = Float.parseFloat(args[1]);
      }

      if (args.length >= 3) {
         pitch = Float.parseFloat(args[2]);
      }

      if (args.length >= 4) {
         self = Boolean.parseBoolean(args[3]);
      }

      if (self) {
         player.playSound(player.getLocation(), sound, volume, pitch);
      } else {
         player.getLocation().getWorld().playSound(player.getLocation(), sound, volume, pitch);
      }

   }

   public List<String> tabComplete(CommandSender sender, List<String> params) {
      switch(params.size()) {
      case 1:
         if (Aliases.SOUND != null) {
            return CompleteUtility.complete((String)params.get(0), Aliases.SOUND);
         }

         return Collections.emptyList();
      case 2:
         return CompleteUtility.complete((String)params.get(1), Arrays.asList("0.5", "1"));
      case 3:
         return CompleteUtility.complete((String)params.get(2), Arrays.asList("1", "2", "5", "10"));
      case 4:
         return CompleteUtility.complete((String)params.get(3), Arrays.asList("true", "false"));
      default:
         return Collections.emptyList();
      }
   }

   public List<String> getInfo() {
      ArrayList<String> list = new ArrayList();
      list.add("&b" + this.getID() + " &e<sound> [volume] [pitch] [self]");
      list.add("&e<sound> &bthe sound to play");
      list.add("&e[volume] &bthe volume of the sound, default 1");
      list.add("&e[pitch] &bthe pitch of the sound, default 1");
      list.add("&e[self] &bonly player sould heard?, by default false");
      return list;
   }
}
