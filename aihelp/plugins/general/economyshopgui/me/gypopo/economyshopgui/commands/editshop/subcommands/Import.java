package me.gypopo.economyshopgui.commands.editshop.subcommands;

import com.earth2me.essentials.Essentials;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.editshop.Methods;
import me.gypopo.economyshopgui.commands.editshop.SubCommad;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.objects.shops.ShopSection;
import me.gypopo.economyshopgui.util.PermissionsCache;
import me.gypopo.economyshopgui.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

public class Import extends SubCommad {
   private final Methods methods;
   private final EconomyShopGUI plugin;
   private Essentials ess;
   private final Map<String, List<String>> supported;
   String pluginName;
   String fileName;
   File file;

   public Import(EconomyShopGUI plugin, Methods methods) {
      this.plugin = plugin;
      this.methods = methods;
      this.supported = this.getSupportedFiles();
   }

   public String getName() {
      return "import";
   }

   public String getDescription() {
      return Lang.EDITSHOP_IMPORT_SUBCOMMAND_DESC.get().getLegacy();
   }

   public String getSyntax() {
      return Lang.EDITSHOP_IMPORT_SUBCOMMAND_SYNTAX.get().getLegacy();
   }

   public boolean hasPermission(CommandSender source) {
      return PermissionsCache.hasPermission(source, "EconomyShopGUI.eshop." + this.getName());
   }

   public void perform(Object logger, String[] args) {
      if (args.length > 1) {
         if (this.supported.containsKey(args[1].toLowerCase(Locale.ENGLISH))) {
            this.pluginName = args[1].toLowerCase(Locale.ENGLISH);
            if (args.length > 2) {
               this.fileName = args[2].toLowerCase(Locale.ENGLISH);
               this.file = this.getFile(this.pluginName, this.fileName);
               if (this.file != null && ((List)this.supported.get(this.pluginName)).contains(this.fileName)) {
                  this.importChanges(logger);
               } else {
                  SendMessage.sendMessage(logger, Lang.FILE_NOT_FOUND.get().replace("%fileName%", this.fileName).replace("%pluginName%", this.pluginName));
               }
            } else {
               SendMessage.sendMessage(logger, this.getSyntax());
            }
         } else {
            SendMessage.sendMessage(logger, Lang.PLUGIN_NOT_FOUND.get());
         }
      } else {
         SendMessage.sendMessage(logger, this.getSyntax());
      }
   }

   private Map<String, List<String>> getSupportedFiles() {
      Map<String, List<String>> supported = new HashMap();
      File dataFolder = new File(this.plugin.getDataFolder().getParent() + "/Essentials/");
      if (dataFolder.exists() && dataFolder.isDirectory()) {
         supported.put("essentials", Arrays.asList("worth.yml"));
      }

      return supported;
   }

   private File getFile(String pluginName, String fileName) {
      String var3 = pluginName.toLowerCase(Locale.ENGLISH);
      byte var4 = -1;
      switch(var3.hashCode()) {
      case 1190983397:
         if (var3.equals("essentials")) {
            var4 = 0;
         }
         break;
      case 1259758215:
         if (var3.equals("economyshopgui")) {
            var4 = 1;
         }
      }

      switch(var4) {
      case 0:
         File config = new File(this.plugin.getDataFolder().getParent() + "/Essentials/" + fileName);
         return config.exists() ? config : null;
      case 1:
         File config1 = new File(this.plugin.getDataFolder() + "/" + fileName);
         return config1.exists() ? config1 : null;
      default:
         return null;
      }
   }

   public List<String> getTabCompletion(String[] args) {
      ArrayList completions;
      switch(args.length) {
      case 2:
         if (!args[1].isEmpty()) {
            completions = new ArrayList();
            StringUtil.copyPartialMatches(args[1], this.supported.keySet(), completions);
            Collections.sort(completions);
            return completions;
         }

         return new ArrayList(this.supported.keySet());
      case 3:
         if (this.supported.containsKey(args[1].toLowerCase(Locale.ENGLISH))) {
            if (!args[2].isEmpty()) {
               completions = new ArrayList();
               StringUtil.copyPartialMatches(args[2], (Iterable)this.supported.get(args[1].toLowerCase(Locale.ENGLISH)), completions);
               Collections.sort(completions);
               return completions;
            }

            return (List)this.supported.get(args[1].toLowerCase(Locale.ENGLISH));
         }

         return null;
      default:
         return null;
      }
   }

   private XMaterial getMaterial(String item) {
      try {
         ItemStack is = this.ess.getItemDb().get(item);
         return XMaterial.matchXMaterial(is);
      } catch (Exception var3) {
         return null;
      }
   }

   private List<XMaterial> getMaterials(ConfigurationSection config, String item) {
      List<XMaterial> mats = new ArrayList();
      int d = 0;

      while(true) {
         Optional<XMaterial> mat = XMaterial.matchXMaterial(item + ":" + d);
         if (!mat.isPresent() || ((XMaterial)mat.get()).parseItem() == null || config.getDouble("worth." + item + ".*", -5.0D) == -5.0D) {
            return mats;
         }

         if (!config.getConfigurationSection("worth." + item).getKeys(false).contains(String.valueOf(d))) {
            mats.add((XMaterial)mat.get());
         }

         ++d;
      }
   }

   private XMaterial getMaterial(ConfigurationSection config, String item, String data) {
      Optional<XMaterial> mat = XMaterial.matchXMaterial(item + ":" + data);
      return mat.isPresent() && ((XMaterial)mat.get()).parseItem() != null && config.getDouble("worth." + item + "." + data, -5.0D) != -5.0D ? (XMaterial)mat.get() : null;
   }

   private void importChanges(Object logger) {
      if (this.pluginName.equalsIgnoreCase("essentials")) {
         this.ess = (Essentials)Bukkit.getServer().getPluginManager().getPlugin("Essentials");
         if (this.ess == null || !this.ess.getItemDb().isReady()) {
            SendMessage.sendMessage(logger, ChatColor.RED + "Could not load essentials, please make sure EssentialsX is installed with the latest version.");
            return;
         }

         SendMessage.infoMessage(logger, "Creating backup of current shop layout...");

         try {
            FileOutputStream outputStream = new FileOutputStream(this.createZipFile() + ".zip");
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            this.zipLayout(zipOutputStream);
            zipOutputStream.close();
            outputStream.close();
         } catch (IOException var12) {
            SendMessage.warnMessage(logger, "Failed to create backup of current shop layout.");
            var12.printStackTrace();
            return;
         }

         String var13 = this.fileName;
         byte var14 = -1;
         switch(var13.hashCode()) {
         case 1354424952:
            if (var13.equals("worth.yml")) {
               var14 = 0;
            }
         }

         label63:
         switch(var14) {
         case 0:
            FileConfiguration config1 = this.plugin.loadConfiguration(this.file, this.fileName);
            Map<XMaterial, Double> mats = new HashMap();
            Iterator var6 = config1.getConfigurationSection("worth").getKeys(false).iterator();

            while(true) {
               Iterator var17;
               while(var6.hasNext()) {
                  String item = (String)var6.next();
                  if (config1.getDouble("worth." + item, -5.0D) == -5.0D) {
                     var17 = config1.getConfigurationSection("worth." + item).getKeys(false).iterator();

                     while(var17.hasNext()) {
                        String data = (String)var17.next();
                        if (data.equals("*")) {
                           this.getMaterials(config1, item).forEach((matx) -> {
                              mats.put(matx, config1.getDouble("worth." + item + ".*"));
                           });
                        } else {
                           XMaterial mat = this.getMaterial(config1, item, data);
                           if (mat != null) {
                              mats.put(mat, config1.getDouble("worth." + item + "." + data));
                           }
                        }
                     }
                  } else {
                     XMaterial mat = this.getMaterial(item);
                     if (mat != null) {
                        mats.put(mat, config1.getDouble("worth." + item));
                     }
                  }
               }

               int count = 0;
               Set<String> updated = new HashSet();
               var17 = this.plugin.getSections().values().iterator();

               while(var17.hasNext()) {
                  ShopSection section = (ShopSection)var17.next();
                  Iterator var19 = section.getShopItems().iterator();

                  while(var19.hasNext()) {
                     ShopItem shopItem = (ShopItem)var19.next();
                     if (!shopItem.hasItemError() && !shopItem.isDisplayItem() && mats.containsKey(XMaterial.matchXMaterial(shopItem.getItemToGive()))) {
                        ConfigManager.getShop(section.getSection()).set("pages." + shopItem.itemLoc + ".buy", -ConfigManager.getShop(section.getSection()).getDouble("pages." + shopItem.itemLoc + ".buy", 1.0D));
                        ConfigManager.getShop(section.getSection()).set("pages." + shopItem.itemLoc + ".sell", mats.get(XMaterial.matchXMaterial(shopItem.getItemToGive())));
                        SendMessage.infoMessage("Imported item '" + XMaterial.matchXMaterial(shopItem.getItemToGive()).name() + "' from worth.yml with a sell price of '" + mats.get(XMaterial.matchXMaterial(shopItem.getItemToGive())) + "'");
                        updated.add(section.getSection());
                        ++count;
                     }
                  }
               }

               updated.forEach(ConfigManager::saveShop);
               SendMessage.sendMessage(logger, ChatColor.GREEN + "Import success, " + count + " items where imported. See the console for more details.");
               break label63;
            }
         default:
            SendMessage.sendMessage(logger, Lang.FILE_NOT_FOUND.get());
         }

         SendMessage.sendMessage(logger, Lang.EDITSHOP_RELOAD_SHOP_TO_SEE_CHANGES.get());
      }

   }

   private Path createZipFile() throws IOException {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
      LocalDateTime now = LocalDateTime.now();
      File backupFolder = new File(this.plugin.getDataFolder() + "/backups");
      if (!backupFolder.exists()) {
         backupFolder.mkdirs();
      }

      String path = this.plugin.getDataFolder() + "/backups/layout - " + dtf.format(now);

      int i;
      for(i = 0; (new File(path + (i > 0 ? "(" + i + ")" : ""))).exists(); ++i) {
      }

      return Paths.get(path + (i > 0 ? "(" + i + ")" : ""));
   }

   private void zipLayout(ZipOutputStream zipOutputStream) throws IOException {
      Iterator var2 = Arrays.asList("shops", "sections", "config.yml", "stands.json").iterator();

      while(true) {
         while(var2.hasNext()) {
            String file = (String)var2.next();
            String path = this.plugin.getDataFolder().getPath() + File.separator + file;
            if (path.contains("\\.")) {
               FileInputStream fis = new FileInputStream(path);
               zipOutputStream.putNextEntry(new ZipEntry(file));
               byte[] bytes = new byte[1024];

               int length;
               while((length = fis.read(bytes)) >= 0) {
                  zipOutputStream.write(bytes, 0, length);
               }

               fis.close();
            } else {
               this.zipFiles(zipOutputStream, file);
            }
         }

         return;
      }
   }

   private void zipFiles(ZipOutputStream zipOutputStream, String dir) throws IOException {
      File directory = new File(this.plugin.getDataFolder() + File.separator + dir);
      if (directory.exists() && directory.isDirectory()) {
         File[] var4 = directory.listFiles();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File file = var4[var6];
            if (!file.isDirectory()) {
               String path = file.getPath();
               FileInputStream fis = new FileInputStream(path);
               zipOutputStream.putNextEntry(new ZipEntry(dir + "/" + file.toPath().getFileName().toString()));
               byte[] bytes = new byte[1024];

               int length;
               while((length = fis.read(bytes)) >= 0) {
                  zipOutputStream.write(bytes, 0, length);
               }

               fis.close();
            }
         }
      }

   }
}
