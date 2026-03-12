package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.Util;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.aliases.IAliasSet;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.gui.BannerEditor;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.NotNull;

public class Banner extends SubCmd {
   private static final String[] subCommands = new String[]{"add", "set", "remove", "color"};

   public Banner(@NotNull ItemEditCommand cmd) {
      super("banner", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      if (!(item.getItemMeta() instanceof BannerMeta)) {
         Util.sendMessage(p, (String)this.getLanguageString("wrong-type", (String)null, sender, new String[0]));
      } else if (args.length == 1) {
         p.openInventory((new BannerEditor(p, item)).getInventory());
      } else {
         String var6 = args[1].toLowerCase(Locale.ENGLISH);
         byte var7 = -1;
         switch(var6.hashCode()) {
         case -934610812:
            if (var6.equals("remove")) {
               var7 = 2;
            }
            break;
         case 96417:
            if (var6.equals("add")) {
               var7 = 0;
            }
            break;
         case 113762:
            if (var6.equals("set")) {
               var7 = 1;
            }
            break;
         case 94842723:
            if (var6.equals("color")) {
               var7 = 3;
            }
         }

         switch(var7) {
         case 0:
            this.addPattern(p, item, alias, args);
            return;
         case 1:
            this.setPattern(p, item, alias, args);
            return;
         case 2:
            this.removePattern(p, item, alias, args);
            return;
         case 3:
            this.colorPattern(p, item, alias, args);
            return;
         default:
            this.onFail(p, alias);
         }
      }
   }

   private void colorPattern(@NotNull Player p, @NotNull ItemStack item, @NotNull String alias, String[] args) {
      try {
         BannerMeta meta = (BannerMeta)ItemUtils.getMeta(item);
         int id = Integer.parseInt(args[2]) - 1;
         PatternType type = meta.getPattern(id).getPattern();
         DyeColor color = (DyeColor)Aliases.COLOR.convertAlias(args[3]);
         if (color == null) {
            this.onWrongAlias("wrong-color", p, Aliases.COLOR, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "color");
            return;
         }

         meta.setPattern(id, new Pattern(color, type));
         item.setItemMeta(meta);
         this.updateView(p);
      } catch (Exception var9) {
         this.sendFailFeedbackForSub(p, alias, "color");
      }

   }

   private void removePattern(@NotNull Player p, @NotNull ItemStack item, @NotNull String alias, String[] args) {
      try {
         BannerMeta meta = (BannerMeta)ItemUtils.getMeta(item);
         int id = Integer.parseInt(args[2]) - 1;
         List<Pattern> list = new ArrayList(meta.getPatterns());
         list.remove(id);
         meta.setPatterns(list);
         item.setItemMeta(meta);
         this.updateView(p);
      } catch (Exception var8) {
         this.sendFailFeedbackForSub(p, alias, "remove");
      }

   }

   private void setPattern(@NotNull Player p, @NotNull ItemStack item, @NotNull String alias, String[] args) {
      try {
         BannerMeta meta = (BannerMeta)ItemUtils.getMeta(item);
         PatternType type = (PatternType)Aliases.PATTERN_TYPE.convertAlias(args[2]);
         DyeColor color = (DyeColor)Aliases.COLOR.convertAlias(args[3]);
         if (type == null || color == null) {
            if (type == null) {
               this.onWrongAlias("wrong-pattern", p, Aliases.PATTERN_TYPE, new String[0]);
            }

            if (color == null) {
               this.onWrongAlias("wrong-color", p, Aliases.COLOR, new String[0]);
            }

            this.sendFailFeedbackForSub(p, alias, "set");
            return;
         }

         int id = Integer.parseInt(args[4]) - 1;
         meta.setPattern(id, new Pattern(color, type));
         item.setItemMeta(meta);
         this.updateView(p);
      } catch (Exception var9) {
         this.sendFailFeedbackForSub(p, alias, "set");
      }

   }

   private void addPattern(@NotNull Player p, @NotNull ItemStack item, @NotNull String alias, String[] args) {
      try {
         BannerMeta meta = (BannerMeta)ItemUtils.getMeta(item);
         PatternType type = (PatternType)Aliases.PATTERN_TYPE.convertAlias(args[2]);
         DyeColor color = (DyeColor)Aliases.COLOR.convertAlias(args[3]);
         if (type == null || color == null) {
            if (type == null) {
               this.onWrongAlias("wrong-pattern", p, Aliases.PATTERN_TYPE, new String[0]);
            }

            if (color == null) {
               this.onWrongAlias("wrong-color", p, Aliases.COLOR, new String[0]);
            }

            this.sendFailFeedbackForSub(p, alias, "add");
            return;
         }

         meta.addPattern(new Pattern(color, type));
         item.setItemMeta(meta);
         this.updateView(p);
      } catch (Exception var8) {
         this.sendFailFeedbackForSub(p, alias, "add");
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      switch(args.length) {
      case 2:
         return CompleteUtility.complete(args[1], subCommands);
      case 3:
         return !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("set") ? Collections.emptyList() : CompleteUtility.complete(args[2], (IAliasSet)Aliases.PATTERN_TYPE);
      case 4:
         if (args[1].equalsIgnoreCase("color") || args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("set")) {
            return CompleteUtility.complete(args[3], (IAliasSet)Aliases.COLOR);
         }
      default:
         return Collections.emptyList();
      }
   }
}
