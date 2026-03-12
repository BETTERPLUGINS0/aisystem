package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.Util;
import emanondev.itemedit.UtilLegacy;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.aliases.IAliasSet;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.VersionUtils;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class PotionEffectEditor extends SubCmd {
   private static final String[] subCommands = new String[]{"add", "remove", "reset"};

   public PotionEffectEditor(ItemEditCommand cmd) {
      super("potioneffect", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      String msg;
      if (!(ItemUtils.getMeta(item) instanceof PotionMeta) && (VersionUtils.isVersionUpTo(1, 14) || !(ItemUtils.getMeta(item) instanceof SuspiciousStewMeta))) {
         Util.sendMessage(p, (String)this.getLanguageString("wrong-type", (String)null, sender, new String[0]));
         if (p.hasPermission("itemedit.admin")) {
            msg = this.getLanguageString("itemtag-tip", (String)null, sender, new String[0]);
            if (msg != null && !msg.isEmpty()) {
               Util.sendMessage(p, (BaseComponent[])(new ComponentBuilder(msg)).event(new HoverEvent(Action.SHOW_TEXT, (new ComponentBuilder(String.join("\n", this.getLanguageStringList("itemtag-tip-hover", (List)null, p, new String[0])))).create())).event(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, "https://modrinth.com/plugin/itemtag")).create());
            }
         }

      } else {
         try {
            if (args.length < 2) {
               throw new IllegalArgumentException("Wrong param number");
            } else {
               msg = args[1].toLowerCase(Locale.ENGLISH);
               byte var7 = -1;
               switch(msg.hashCode()) {
               case -934610812:
                  if (msg.equals("remove")) {
                     var7 = 2;
                  }
                  break;
               case 96417:
                  if (msg.equals("add")) {
                     var7 = 1;
                  }
                  break;
               case 108404047:
                  if (msg.equals("reset")) {
                     var7 = 0;
                  }
               }

               switch(var7) {
               case 0:
                  this.potioneffectClear(p, item, alias, args);
                  return;
               case 1:
                  this.potioneffectAdd(p, item, alias, args);
                  return;
               case 2:
                  this.potioneffectRemove(p, item, alias, args);
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

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      switch(args.length) {
      case 2:
         return CompleteUtility.complete(args[1], subCommands);
      case 3:
         if (!args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("remove")) {
            return Collections.emptyList();
         }

         return CompleteUtility.complete(args[2], (IAliasSet)Aliases.POTION_EFFECT);
      case 4:
         if (args[1].equalsIgnoreCase("add")) {
            return CompleteUtility.complete(args[3], "infinite", "instant", "∞", "90", "180", "480");
         }

         return Collections.emptyList();
      case 5:
         if (args[1].equalsIgnoreCase("add")) {
            return CompleteUtility.complete(args[4], "1", "2", "3");
         }

         return Collections.emptyList();
      case 6:
      case 7:
         if (args[1].equalsIgnoreCase("add")) {
            return CompleteUtility.complete(args[args.length - 1], (IAliasSet)Aliases.BOOLEAN);
         }

         return Collections.emptyList();
      case 8:
         if (VersionUtils.isVersionAfter(1, 13) && args[1].equalsIgnoreCase("add")) {
            return CompleteUtility.complete(args[args.length - 1], (IAliasSet)Aliases.BOOLEAN);
         }

         return Collections.emptyList();
      default:
         return Collections.emptyList();
      }
   }

   private void potioneffectRemove(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         PotionEffectType effect = (PotionEffectType)Aliases.POTION_EFFECT.convertAlias(args[2].toUpperCase());
         if (effect == null) {
            this.onWrongAlias("wrong-effect", p, Aliases.POTION_EFFECT, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "remove");
            return;
         }

         ItemMeta rawMeta = ItemUtils.getMeta(item);
         if (rawMeta instanceof PotionMeta) {
            PotionMeta meta = (PotionMeta)rawMeta;
            meta.removeCustomEffect(effect);
            item.setItemMeta(meta);
         } else {
            SuspiciousStewMeta meta = (SuspiciousStewMeta)rawMeta;
            meta.removeCustomEffect(effect);
            item.setItemMeta(meta);
         }

         this.updateView(p);
      } catch (Exception var8) {
         this.sendFailFeedbackForSub(p, alias, "remove");
      }

   }

   private void potioneffectAdd(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length != 4 && args.length != 5 && args.length != 6 && args.length != 7 && args.length != 8) {
            throw new IllegalArgumentException("Wrong param number");
         }

         int level = 0;
         PotionEffectType effect = (PotionEffectType)Aliases.POTION_EFFECT.convertAlias(args[2]);
         if (effect == null) {
            this.onWrongAlias("wrong-effect", p, Aliases.POTION_EFFECT, new String[0]);
            this.sendFailFeedbackForSub(p, alias, "add");
            return;
         }

         int duration = UtilLegacy.readPotionEffectDurationSecondsToTicks(args[3]);
         if (args.length >= 5) {
            level = Integer.parseInt(args[4]) - 1;
            if (level < 0 || level > 127) {
               throw new IllegalArgumentException();
            }
         }

         boolean particles = true;
         if (args.length >= 6) {
            particles = (Boolean)Aliases.BOOLEAN.convertAlias(args[5]);
         }

         boolean ambient = false;
         if (args.length >= 7) {
            ambient = (Boolean)Aliases.BOOLEAN.convertAlias(args[6]);
         }

         boolean icon = true;
         if (VersionUtils.isVersionAfter(1, 13) && args.length == 8) {
            icon = (Boolean)Aliases.BOOLEAN.convertAlias(args[7]);
         }

         if (!p.hasPermission(this.getPermission() + ".bypass_limits")) {
            level = Math.min(level, 1);
         }

         ItemMeta rawMeta = ItemUtils.getMeta(item);
         if (rawMeta instanceof PotionMeta) {
            PotionMeta meta = (PotionMeta)rawMeta;
            if (VersionUtils.isVersionAfter(1, 13)) {
               meta.addCustomEffect(new PotionEffect(effect, duration, level, ambient, particles, icon), true);
            } else {
               meta.addCustomEffect(new PotionEffect(effect, duration, level, ambient, particles), true);
            }

            item.setItemMeta(meta);
         } else {
            SuspiciousStewMeta meta = (SuspiciousStewMeta)rawMeta;
            meta.addCustomEffect(new PotionEffect(effect, duration, level, ambient, particles, icon), true);
            item.setItemMeta(meta);
         }

         this.updateView(p);
      } catch (Exception var13) {
         this.sendFailFeedbackForSub(p, alias, "add");
      }

   }

   private void potioneffectClear(Player p, ItemStack item, String alias, String[] args) {
      try {
         ItemMeta rawMeta = ItemUtils.getMeta(item);
         if (rawMeta instanceof PotionMeta) {
            PotionMeta meta = (PotionMeta)rawMeta;
            meta.clearCustomEffects();
            item.setItemMeta(meta);
         } else {
            SuspiciousStewMeta meta = (SuspiciousStewMeta)rawMeta;
            meta.clearCustomEffects();
            item.setItemMeta(meta);
         }

         this.updateView(p);
      } catch (Exception var7) {
      }

   }
}
