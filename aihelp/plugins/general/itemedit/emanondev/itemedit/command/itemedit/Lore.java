package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.Util;
import emanondev.itemedit.UtilsString;
import emanondev.itemedit.YMLConfig;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Lore extends SubCmd {
   private static final String[] loreSub = new String[]{"add", "set", "remove", "reset", "insert", "copy", "copybook", "copyfile", "paste", "replace"};
   private final Map<UUID, List<String>> copies = new HashMap();
   private final YMLConfig loreCopy = ItemEdit.get().getConfig("loreCopy");
   private int lineLimit = this.getPlugin().getConfig().getInt("blocked.lore-line-limit", 16);
   private int lengthLimit = this.getPlugin().getConfig().getInt("blocked.lore-length-limit", 120);

   public Lore(ItemEditCommand cmd) {
      super("lore", cmd, true, true);
   }

   public void reload() {
      super.reload();
      this.lineLimit = this.getPlugin().getConfig().getInt("blocked.lore-line-limit", 16);
      this.lengthLimit = this.getPlugin().getConfig().getInt("blocked.lore-length-limit", 120);
   }

   private boolean allowedLineLimit(Player who, int lines) {
      if (this.lineLimit >= 0 && !who.hasPermission("itemedit.bypass.lore_line_limit")) {
         return lines <= this.lineLimit;
      } else {
         return true;
      }
   }

   private boolean allowedLengthLimit(Player who, String text) {
      if (this.lengthLimit >= 0 && !who.hasPermission("itemedit.bypass.lore_length_limit")) {
         return text.length() <= this.lengthLimit;
      } else {
         return true;
      }
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      if (args.length == 1) {
         this.onFail(p, alias);
      } else {
         String var6 = args[1].toLowerCase(Locale.ENGLISH);
         byte var7 = -1;
         switch(var6.hashCode()) {
         case -1183792455:
            if (var6.equals("insert")) {
               var7 = 2;
            }
            break;
         case -934610812:
            if (var6.equals("remove")) {
               var7 = 4;
            }
            break;
         case -505534498:
            if (var6.equals("copybook")) {
               var7 = 6;
            }
            break;
         case -505421199:
            if (var6.equals("copyfile")) {
               var7 = 7;
            }
            break;
         case 96417:
            if (var6.equals("add")) {
               var7 = 1;
            }
            break;
         case 113762:
            if (var6.equals("set")) {
               var7 = 0;
            }
            break;
         case 3059573:
            if (var6.equals("copy")) {
               var7 = 5;
            }
            break;
         case 106438291:
            if (var6.equals("paste")) {
               var7 = 8;
            }
            break;
         case 108404047:
            if (var6.equals("reset")) {
               var7 = 3;
            }
            break;
         case 1094496948:
            if (var6.equals("replace")) {
               var7 = 9;
            }
         }

         switch(var7) {
         case 0:
            if (!Util.isAllowedChangeLore(sender, item.getType())) {
               return;
            }

            this.loreSet(p, item, alias, args);
            return;
         case 1:
            if (!Util.isAllowedChangeLore(sender, item.getType())) {
               return;
            }

            this.loreAdd(p, item, alias, args);
            return;
         case 2:
            if (!Util.isAllowedChangeLore(sender, item.getType())) {
               return;
            }

            this.loreInsert(p, item, alias, args);
            return;
         case 3:
            if (!Util.isAllowedChangeLore(sender, item.getType())) {
               return;
            }

            this.loreReset(p, item, alias, args);
            return;
         case 4:
            if (!Util.isAllowedChangeLore(sender, item.getType())) {
               return;
            }

            this.loreRemove(p, item, alias, args);
            return;
         case 5:
            if (!sender.hasPermission(this.getPermission() + ".copy")) {
               this.getCommand().sendPermissionLackMessage(this.getPermission() + ".copy", sender);
               return;
            }

            this.loreCopy(p, item, alias, args);
            return;
         case 6:
            if (!sender.hasPermission(this.getPermission() + ".copy")) {
               this.getCommand().sendPermissionLackMessage(this.getPermission() + ".copy", sender);
               return;
            }

            this.loreCopyBook(p, item, alias, args);
            return;
         case 7:
            if (!sender.hasPermission(this.getPermission() + ".copy")) {
               this.getCommand().sendPermissionLackMessage(this.getPermission() + ".copy", sender);
               return;
            }

            this.loreCopyFile(p, item, alias, args);
            return;
         case 8:
            if (!sender.hasPermission(this.getPermission() + ".copy")) {
               this.getCommand().sendPermissionLackMessage(this.getPermission() + ".copy", sender);
               return;
            } else {
               if (!Util.isAllowedChangeLore(sender, item.getType())) {
                  return;
               }

               this.lorePaste(p, item, alias, args);
               return;
            }
         case 9:
            if (!Util.isAllowedChangeLore(sender, item.getType())) {
               return;
            }

            this.loreReplace(p, item, alias, args);
            return;
         default:
            this.onFail(p, alias);
         }
      }
   }

   private void loreReplace(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length < 4) {
            this.sendFailFeedbackForSub(p, alias, "replace");
            return;
         }

         if (!item.hasItemMeta()) {
            return;
         }

         ItemMeta meta = ItemUtils.getMeta(item);
         if (!meta.hasLore()) {
            return;
         }

         List<String> lore = meta.getLore();
         String from;
         String to;
         String rawText;
         if (args.length == 4) {
            from = args[2];
            to = args[3];
         } else {
            StringBuilder raw = new StringBuilder();

            for(int i = 2; i < args.length; ++i) {
               raw.append(" ").append(args[i]);
            }

            rawText = raw.substring(1);
            int i1 = rawText.indexOf("{");
            if (i1 != 0) {
               this.sendFailFeedbackForSub(p, alias, "replace");
               return;
            }

            int i2 = rawText.indexOf("}", i1);
            if (i2 == -1) {
               this.sendFailFeedbackForSub(p, alias, "replace");
               return;
            }

            int i3 = rawText.indexOf("{", i2);
            if (i3 == -1 || i2 + 2 != i3) {
               this.sendFailFeedbackForSub(p, alias, "replace");
               return;
            }

            int i4 = rawText.indexOf("}", i3);
            if (i4 != rawText.length() - 1) {
               this.sendFailFeedbackForSub(p, alias, "replace");
               return;
            }

            from = rawText.substring(1, i2);
            to = rawText.substring(i3 + 1, i4);
         }

         from = UtilsString.fix((String)from, (Player)null, true);
         to = UtilsString.fix((String)to, (Player)null, true);
         Iterator var16 = lore.iterator();

         while(var16.hasNext()) {
            rawText = (String)var16.next();
            String text = rawText.replace(from, to);
            if (!this.allowedLengthLimit(p, ChatColor.stripColor(text))) {
               Util.sendMessage(p, (String)ItemEdit.get().getLanguageConfig(p).loadMessage("blocked-by-lore-length-limit", "", (Player)null, true, "%limit%", String.valueOf(this.lengthLimit)));
               return;
            }
         }

         for(int i = 0; i < lore.size(); ++i) {
            lore.set(i, ((String)lore.get(i)).replace(from, to));
         }

         meta.setLore(lore);
         item.setItemMeta(meta);
         this.updateView(p);
      } catch (Exception var15) {
         this.sendFailFeedbackForSub(p, alias, "replace");
      }

   }

   private void lorePaste(Player p, ItemStack item, String alias, String[] args) {
      if (!this.copies.containsKey(p.getUniqueId())) {
         Util.sendMessage(p, (String)this.getLanguageString("paste.no-copy", (String)null, p, new String[0]));
      } else {
         ItemMeta meta = ItemUtils.getMeta(item);
         meta.setLore((List)this.copies.get(p.getUniqueId()));
         item.setItemMeta(meta);
         Util.sendMessage(p, (String)this.getLanguageString("paste.feedback", (String)null, p, new String[0]));
         this.updateView(p);
      }
   }

   private void loreCopy(Player p, ItemStack item, String alias, String[] args) {
      ArrayList lore;
      if (item.hasItemMeta()) {
         ItemMeta itemMeta = ItemUtils.getMeta(item);
         if (itemMeta.hasLore()) {
            lore = new ArrayList(itemMeta.getLore());
         } else {
            lore = new ArrayList();
         }
      } else {
         lore = new ArrayList();
      }

      this.copies.put(p.getUniqueId(), lore);
      Util.sendMessage(p, (String)this.getLanguageString("copy.feedback", (String)null, p, new String[0]));
   }

   private void loreCopyBook(Player p, ItemStack item, String alias, String[] args) {
      ArrayList lore;
      if (item.hasItemMeta()) {
         ItemMeta itemMeta = ItemUtils.getMeta(item);
         if (!(itemMeta instanceof BookMeta)) {
            Util.sendMessage(p, (String)this.getLanguageString("copyBook.wrong-type", (String)null, p, new String[0]));
            return;
         }

         BookMeta meta = (BookMeta)itemMeta;
         List<String> pages = meta.getPages();
         lore = new ArrayList();
         if (pages != null) {
            Iterator var9 = pages.iterator();

            while(var9.hasNext()) {
               String page = (String)var9.next();
               if (page != null) {
                  lore.addAll(Arrays.asList(page.split("\n")));
               }
            }
         }
      } else {
         lore = new ArrayList();
      }

      for(int i = 0; i < lore.size(); ++i) {
         lore.set(i, Util.formatText(p, (String)lore.get(i), this.getPermission()));
      }

      this.copies.put(p.getUniqueId(), lore);
      Util.sendMessage(p, (String)this.getLanguageString("copyBook.feedback", (String)null, p, new String[0]));
   }

   private void loreCopyFile(Player p, ItemStack item, String alias, String[] args) {
      if (args.length < 2) {
         Util.sendMessage(p, (String)this.getLanguageString("copyFile.no-path", (String)null, p, new String[0]));
      } else if (!this.loreCopy.contains(args[2])) {
         Util.sendMessage(p, (String)this.getLanguageString("copyFile.wrong-path", (String)null, p, new String[0]));
      } else {
         List<String> lore = new ArrayList(this.loreCopy.getStringList(args[2]));

         for(int i = 0; i < lore.size(); ++i) {
            lore.set(i, Util.formatText(p, (String)lore.get(i), this.getPermission()));
         }

         this.copies.put(p.getUniqueId(), lore);
         Util.sendMessage(p, (String)this.getLanguageString("copyFile.feedback", (String)null, p, new String[0]));
      }
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      String var3;
      byte var4;
      switch(args.length) {
      case 2:
         return CompleteUtility.complete(args[1], loreSub);
      case 3:
         var3 = args[1].toLowerCase(Locale.ENGLISH);
         var4 = -1;
         switch(var3.hashCode()) {
         case -934610812:
            if (var3.equals("remove")) {
               var4 = 0;
            }
            break;
         case -505421199:
            if (var3.equals("copyfile")) {
               var4 = 2;
            }
            break;
         case 113762:
            if (var3.equals("set")) {
               var4 = 1;
            }
         }

         switch(var4) {
         case 0:
         case 1:
            if (!(sender instanceof Player)) {
               return Collections.emptyList();
            } else {
               Player player = (Player)sender;
               ItemStack item = this.getItemInHand(player);
               if (ItemUtils.isAirOrNull(item)) {
                  return Collections.emptyList();
               } else if (!item.hasItemMeta()) {
                  return CompleteUtility.complete(args[2], (Collection)Arrays.asList("1", "last"));
               } else {
                  ItemMeta meta = ItemUtils.getMeta(item);
                  if (!meta.hasLore()) {
                     return CompleteUtility.complete(args[2], (Collection)Arrays.asList("1", "last"));
                  }

                  List<String> list = new ArrayList();

                  for(int i = 0; i < meta.getLore().size(); ++i) {
                     list.add(String.valueOf(i + 1));
                  }

                  list.add("last");
                  return CompleteUtility.complete(args[2], (Collection)list);
               }
            }
         case 2:
            return CompleteUtility.complete(args[2], (Collection)this.loreCopy.getKeys(false));
         default:
            return Collections.emptyList();
         }
      case 4:
         var3 = args[1].toLowerCase(Locale.ENGLISH);
         var4 = -1;
         switch(var3.hashCode()) {
         case 113762:
            if (var3.equals("set")) {
               var4 = 0;
            }
         default:
            switch(var4) {
            case 0:
               if (sender instanceof Player) {
                  ItemStack item = this.getItemInHand((Player)sender);
                  if (item != null && item.hasItemMeta()) {
                     ItemMeta meta = ItemUtils.getMeta(item);
                     if (meta.hasLore()) {
                        List lore = meta.getLore();

                        try {
                           int line = args[2].equalsIgnoreCase("last") ? lore.size() - 1 : Integer.parseInt(args[2]) - 1;
                           if (line >= 0 && line < lore.size()) {
                              return CompleteUtility.complete(args[3], ((String)lore.get(line)).replace('§', '&'));
                           }

                           return Collections.emptyList();
                        } catch (NumberFormatException var10) {
                           return Collections.emptyList();
                        }
                     }
                  }
               }
            }
         }
      default:
         return Collections.emptyList();
      }
   }

   private void loreAdd(Player p, ItemStack item, String alias, String[] args) {
      StringBuilder text = new StringBuilder();
      if (args.length > 2) {
         text = new StringBuilder(args[2]);

         for(int i = 3; i < args.length; ++i) {
            text.append(" ").append(args[i]);
         }
      }

      ItemMeta itemMeta = ItemUtils.getMeta(item);
      ArrayList lore;
      if (itemMeta.hasLore()) {
         lore = new ArrayList(itemMeta.getLore());
      } else {
         lore = new ArrayList();
      }

      if (!this.allowedLineLimit(p, lore.size() + 1)) {
         Util.sendMessage(p, (String)ItemEdit.get().getLanguageConfig(p).loadMessage("blocked-by-lore-line-limit", "", (Player)null, true, "%limit%", String.valueOf(this.lineLimit)));
      } else {
         String lineText = Util.formatText(p, text.toString(), this.getPermission());
         if (!this.allowedLengthLimit(p, ChatColor.stripColor(lineText))) {
            Util.sendMessage(p, (String)ItemEdit.get().getLanguageConfig(p).loadMessage("blocked-by-lore-length-limit", "", (Player)null, true, "%limit%", String.valueOf(this.lengthLimit)));
         } else if (!Util.hasBannedWords(p, lineText)) {
            lore.add(lineText);
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            this.updateView(p);
         }
      }
   }

   private void loreInsert(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length < 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         StringBuilder text = new StringBuilder();
         int line;
         if (args.length > 3) {
            text = new StringBuilder(args[3]);

            for(line = 4; line < args.length; ++line) {
               text.append(" ").append(args[line]);
            }
         }

         line = Integer.parseInt(args[2]) - 1;
         if (line < 0) {
            throw new IllegalArgumentException("Wrong line number");
         }

         ItemMeta itemMeta = ItemUtils.getMeta(item);
         ArrayList lore;
         if (itemMeta.hasLore()) {
            lore = new ArrayList(itemMeta.getLore());
         } else {
            lore = new ArrayList();
         }

         if (!this.allowedLineLimit(p, Math.max(lore.size() + 1, line + 1))) {
            Util.sendMessage(p, (String)ItemEdit.get().getLanguageConfig(p).loadMessage("blocked-by-lore-line-limit", "", (Player)null, true, "%limit%", String.valueOf(this.lineLimit)));
            return;
         }

         String lineText = Util.formatText(p, text.toString(), this.getPermission());
         if (!this.allowedLengthLimit(p, ChatColor.stripColor(lineText))) {
            Util.sendMessage(p, (String)ItemEdit.get().getLanguageConfig(p).loadMessage("blocked-by-lore-length-limit", "", (Player)null, true, "%limit%", String.valueOf(this.lengthLimit)));
            return;
         }

         for(int i = lore.size(); i < line; ++i) {
            lore.add("");
         }

         if (Util.hasBannedWords(p, lineText)) {
            return;
         }

         lore.add(line, lineText);
         itemMeta.setLore(lore);
         item.setItemMeta(itemMeta);
         this.updateView(p);
      } catch (Exception var11) {
         this.sendFailFeedbackForSub(p, alias, "insert");
      }

   }

   private void loreSet(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length < 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         StringBuilder text = new StringBuilder();
         if (args.length > 3) {
            text = new StringBuilder(args[3]);

            for(int i = 4; i < args.length; ++i) {
               text.append(" ").append(args[i]);
            }
         }

         String lineText = Util.formatText(p, text.toString(), this.getPermission());
         ItemMeta itemMeta = ItemUtils.getMeta(item);
         ArrayList lore;
         if (itemMeta.hasLore()) {
            lore = new ArrayList(itemMeta.getLore());
         } else {
            lore = new ArrayList();
         }

         int line = args[2].equalsIgnoreCase("last") ? lore.size() - 1 : Integer.parseInt(args[2]) - 1;
         if (line < 0) {
            throw new IllegalArgumentException("Wrong line number");
         }

         if (lore.size() <= line && !this.allowedLineLimit(p, line + 1)) {
            Util.sendMessage(p, (String)ItemEdit.get().getLanguageConfig(p).loadMessage("blocked-by-lore-line-limit", "", (Player)null, true, "%limit%", String.valueOf(this.lineLimit)));
            return;
         }

         if (!this.allowedLengthLimit(p, ChatColor.stripColor(lineText))) {
            Util.sendMessage(p, (String)ItemEdit.get().getLanguageConfig(p).loadMessage("blocked-by-lore-length-limit", "", (Player)null, true, "%limit%", String.valueOf(this.lengthLimit)));
            return;
         }

         for(int i = lore.size(); i <= line; ++i) {
            lore.add("");
         }

         if (Util.hasBannedWords(p, lineText)) {
            return;
         }

         lore.set(line, lineText);
         itemMeta.setLore(lore);
         item.setItemMeta(itemMeta);
         this.updateView(p);
      } catch (Exception var11) {
         this.sendFailFeedbackForSub(p, alias, "set");
      }

   }

   private void loreRemove(Player p, ItemStack item, String alias, String[] args) {
      try {
         if (args.length < 3) {
            throw new IllegalArgumentException("Wrong param number");
         }

         if (!item.hasItemMeta()) {
            return;
         }

         ItemMeta itemMeta = ItemUtils.getMeta(item);
         if (!itemMeta.hasLore() || itemMeta.getLore().isEmpty()) {
            return;
         }

         List<String> lore = new ArrayList(itemMeta.getLore());
         int line;
         if (args[2].equalsIgnoreCase("last")) {
            line = lore.size() - 1;
         } else {
            line = Integer.parseInt(args[2]) - 1;
         }

         if (line < 0) {
            throw new IllegalArgumentException("Wrong line number");
         }

         if (lore.size() < line) {
            return;
         }

         lore.remove(line);
         itemMeta.setLore(lore);
         item.setItemMeta(itemMeta);
         this.updateView(p);
      } catch (Exception var8) {
         this.sendFailFeedbackForSub(p, alias, "remove");
      }

   }

   private void loreReset(Player p, ItemStack item, String alias, String[] args) {
      ItemMeta meta = ItemUtils.getMeta(item);
      meta.setLore((List)null);
      item.setItemMeta(meta);
      this.updateView(p);
   }
}
