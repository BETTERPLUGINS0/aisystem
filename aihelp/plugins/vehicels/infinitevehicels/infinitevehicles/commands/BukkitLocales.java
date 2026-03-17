package me.PM2.infinitevehicles.commands;

import java.io.File;
import java.util.Iterator;
import java.util.Locale;
import me.PM2.infinitevehicles.locales.MessageKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class BukkitLocales extends Locales {
   private final BukkitCommandManager manager;

   public BukkitLocales(BukkitCommandManager manager) {
      super(var1);
      this.manager = var1;
      this.addBundleClassLoader(this.manager.getPlugin().getClass().getClassLoader());
   }

   public void loadLanguages() {
      super.loadLanguages();
      String var1 = "acf-" + this.manager.plugin.getDescription().getName();
      this.addMessageBundles(new String[]{"acf-minecraft", var1, var1.toLowerCase(Locale.ENGLISH)});
   }

   public boolean loadYamlLanguageFile(File file, Locale locale) {
      YamlConfiguration var3 = new YamlConfiguration();
      var3.load(var1);
      return this.loadLanguage(var3, var2);
   }

   public boolean loadYamlLanguageFile(String file, Locale locale) {
      YamlConfiguration var3 = new YamlConfiguration();
      var3.load(new File(this.manager.plugin.getDataFolder(), var1));
      return this.loadLanguage(var3, var2);
   }

   public boolean loadLanguage(FileConfiguration config, Locale locale) {
      boolean var3 = false;
      Iterator var4 = var1.getKeys(true).iterator();

      while(true) {
         String var5;
         do {
            if (!var4.hasNext()) {
               return var3;
            }

            var5 = (String)var4.next();
         } while(!var1.isString(var5) && !var1.isDouble(var5) && !var1.isLong(var5) && !var1.isInt(var5) && !var1.isBoolean(var5));

         String var6 = var1.getString(var5);
         if (var6 != null && !var6.isEmpty()) {
            this.addMessage(var2, MessageKey.of(var5), var6);
            var3 = true;
         }
      }
   }
}
