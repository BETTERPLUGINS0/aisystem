package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.Util;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.aliases.IAliasSet;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.bukkit.DyeColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.jetbrains.annotations.NotNull;

public class TropicalFish extends SubCmd {
   private static final String[] subCommands = new String[]{"pattern", "patterncolor", "bodycolor"};

   public TropicalFish(ItemEditCommand cmd) {
      super("tropicalfish", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      if (!(ItemUtils.getMeta(item) instanceof TropicalFishBucketMeta)) {
         Util.sendMessage(p, (String)this.getLanguageString("wrong-type", (String)null, sender, new String[0]));
      } else {
         try {
            if (args.length < 2) {
               throw new IllegalArgumentException("Wrong param number");
            } else {
               String var6 = args[1].toLowerCase(Locale.ENGLISH);
               byte var7 = -1;
               switch(var6.hashCode()) {
               case -791090288:
                  if (var6.equals("pattern")) {
                     var7 = 0;
                  }
                  break;
               case -194839085:
                  if (var6.equals("patterncolor")) {
                     var7 = 1;
                  }
                  break;
               case 1256565505:
                  if (var6.equals("bodycolor")) {
                     var7 = 2;
                  }
               }

               switch(var7) {
               case 0:
                  this.pattern(p, item, alias, args);
                  return;
               case 1:
                  this.patternColor(p, item, alias, args);
                  return;
               case 2:
                  this.bodyColor(p, item, alias, args);
                  return;
               default:
                  throw new IllegalArgumentException();
               }
            }
         } catch (Exception var8) {
            this.onFail(p, alias);
         }
      }
   }

   private void bodyColor(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         TropicalFishBucketMeta meta = (TropicalFishBucketMeta)ItemUtils.getMeta(item);
         DyeColor color = (DyeColor)Aliases.COLOR.convertAlias(args[2]);
         if (color == null) {
            this.onWrongAlias("wrong-color", p, Aliases.COLOR, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "bodycolor");
            return;
         }

         meta.setBodyColor(color);
         item.setItemMeta(meta);
         this.updateView(p);
      } catch (Exception var7) {
         this.sendFailFeedbackForSub(p, alias, "bodycolor");
      }

   }

   private void patternColor(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         TropicalFishBucketMeta meta = (TropicalFishBucketMeta)ItemUtils.getMeta(item);
         DyeColor color = (DyeColor)Aliases.COLOR.convertAlias(args[2]);
         if (color == null) {
            this.onWrongAlias("wrong-color", p, Aliases.COLOR, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "patterncolor");
            return;
         }

         meta.setPatternColor(color);
         item.setItemMeta(meta);
         this.updateView(p);
      } catch (Exception var7) {
         this.sendFailFeedbackForSub(p, alias, "patterncolor");
      }

   }

   private void pattern(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         TropicalFishBucketMeta meta = (TropicalFishBucketMeta)ItemUtils.getMeta(item);
         Pattern pattern = (Pattern)Aliases.TROPICALPATTERN.convertAlias(args[2]);
         if (pattern == null) {
            this.onWrongAlias("wrong-pattern", p, Aliases.TROPICALPATTERN, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "pattern");
            return;
         }

         meta.setPattern(pattern);
         item.setItemMeta(meta);
         this.updateView(p);
      } catch (Exception var7) {
         this.sendFailFeedbackForSub(p, alias, "pattern");
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      if (args.length == 2) {
         return CompleteUtility.complete(args[1], subCommands);
      } else {
         if (args.length == 3) {
            if (args[1].equalsIgnoreCase("patterncolor") || args[1].equalsIgnoreCase("bodycolor")) {
               return CompleteUtility.complete(args[2], (IAliasSet)Aliases.COLOR);
            }

            if (args[1].equalsIgnoreCase("pattern")) {
               return CompleteUtility.complete(args[2], (IAliasSet)Aliases.TROPICALPATTERN);
            }
         }

         return Collections.emptyList();
      }
   }
}
