package emanondev.itemedit.command;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.Util;
import emanondev.itemedit.utility.CompleteUtility;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ItemEditImportCommand implements TabExecutor {
   private final ItemEdit plugin = ItemEdit.get();
   private final String permission = "itemedit.itemeditimport";

   private static ItemStack fromBase64(String data) throws IOException {
      try {
         ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
         BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
         ItemStack item = (ItemStack)dataInput.readObject();
         dataInput.close();
         return item;
      } catch (ClassNotFoundException var4) {
         throw new IOException("Unable to decode class type.", var4);
      }
   }

   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      return args.length == 1 ? CompleteUtility.complete(args[0], (Collection)Collections.singletonList("itemeditor")) : Collections.emptyList();
   }

   public void sendPermissionLackMessage(@NotNull String permission, CommandSender sender) {
      Util.sendMessage(sender, this.plugin.getLanguageConfig(sender).loadMessage("lack-permission", "&cYou lack of permission %permission%", sender instanceof Player ? (Player)sender : null, true, "%permission%", permission));
   }

   public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (!sender.hasPermission(this.permission)) {
         this.sendPermissionLackMessage(this.permission, sender);
         return true;
      } else if (args.length == 0) {
         Util.sendMessage(sender, String.join("\n", this.plugin.getLanguageConfig(sender).loadMultiMessage("itemeditimport.help", new ArrayList())));
         return true;
      } else {
         String var5 = args[0].toLowerCase(Locale.ENGLISH);
         byte var6 = -1;
         switch(var5.hashCode()) {
         case -1794391488:
            if (var5.equals("itemeditor")) {
               var6 = 0;
            }
         default:
            switch(var6) {
            case 0:
               File[] files = (new File("plugins" + File.separator + "ItemEditor" + File.separator + "items")).listFiles();
               if (files != null && files.length != 0) {
                  List<String> importedIds = new ArrayList();
                  int max = files.length;
                  File[] var10 = files;
                  int var11 = files.length;

                  for(int var12 = 0; var12 < var11; ++var12) {
                     File file = var10[var12];
                     String name = file.getName().replace(".yml", "");

                     try {
                        ItemEdit.get().getServerStorage().validateID(name);
                     } catch (Exception var17) {
                        Util.sendMessage(sender, String.join("\n", this.plugin.getLanguageConfig(sender).loadMultiMessage("itemeditimport.itemeditor.invalid-id", new ArrayList(), (Player)null, true, "%id%", name)));
                        continue;
                     }

                     if (ItemEdit.get().getServerStorage().getItem(name) != null) {
                        Util.sendMessage(sender, String.join("\n", this.plugin.getLanguageConfig(sender).loadMultiMessage("itemeditimport.itemeditor.already-used-id", new ArrayList(), (Player)null, true, "%id%", name)));
                     } else {
                        try {
                           ItemStack item = fromBase64(YamlConfiguration.loadConfiguration(file).getString("Item"));
                           ItemEdit.get().getServerStorage().setItem(name, item);
                           importedIds.add(name);
                        } catch (Exception var16) {
                           Util.sendMessage(sender, String.join("\n", this.plugin.getLanguageConfig(sender).loadMultiMessage("itemeditimport.itemeditor.unable-to-get-item", new ArrayList(), (Player)null, true, "%id%", name)));
                           var16.printStackTrace();
                        }
                     }
                  }

                  if (importedIds.isEmpty()) {
                     Util.sendMessage(sender, String.join("\n", this.plugin.getLanguageConfig(sender).loadMultiMessage("itemeditimport.itemeditor.import-unsuccess", new ArrayList(), (Player)null, true, "%ids%", String.join(", ", importedIds), "%max%", String.valueOf(max), "%done%", String.valueOf(importedIds.size()))));
                  } else {
                     Util.sendMessage(sender, String.join("\n", this.plugin.getLanguageConfig(sender).loadMultiMessage("itemeditimport.itemeditor.import-success", new ArrayList(), (Player)null, true, "%ids%", String.join(", ", importedIds), "%max%", String.valueOf(max), "%done%", String.valueOf(importedIds.size()))));
                  }
               } else {
                  Util.sendMessage(sender, String.join("\n", this.plugin.getLanguageConfig(sender).loadMultiMessage("itemeditimport.itemeditor.import-empty", new ArrayList())));
               }

               return true;
            default:
               Util.sendMessage(sender, String.join("\n", this.plugin.getLanguageConfig(sender).loadMultiMessage("itemeditimport.help", new ArrayList())));
               return true;
            }
         }
      }
   }
}
