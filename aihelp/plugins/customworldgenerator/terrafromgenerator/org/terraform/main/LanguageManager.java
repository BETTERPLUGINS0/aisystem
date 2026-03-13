package org.terraform.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.main.config.TConfig;

public class LanguageManager {
   private final File file;
   @NotNull
   private final HashMap<String, String> cache = new HashMap();
   private FileConfiguration langFile;

   public LanguageManager(@NotNull TerraformGeneratorPlugin plugin, @NotNull TConfig config) {
      this.file = new File(plugin.getDataFolder(), config.LANGUAGE_FILE);
      this.reloadLangFile();
      this.loadDefaults();
   }

   private void loadDefaults() {
      this.fetchLang("permissions.insufficient", "&cYou don't have enough permissions to perform this action!");
      this.fetchLang("command.wrong-arg-length", "&cToo many or too little arguments provided!");
      this.fetchLang("command.unknown", "&cUnknown subcommand.");
      this.fetchLang("command.help.postive-pages", "&cThe page specified must be a positive number!");
      this.fetchLang("permissions.console-cannot-exec", "&cOnly players can execute this command.");
   }

   public String fetchLang(@NotNull String langKey) {
      return this.fetchLang(langKey, (String)null);
   }

   public String fetchLang(@NotNull String langKey, @Nullable String def) {
      if (this.cache.containsKey(langKey)) {
         return (String)this.cache.get(langKey);
      } else {
         String value = this.langFile.getString(langKey);
         if (value == null) {
            value = def == null ? langKey : def;
            this.langFile.set(langKey, value);
            this.saveLangFile();
         }

         value = ChatColor.translateAlternateColorCodes('&', value);
         this.cache.put(langKey, value);
         return value;
      }
   }

   public void saveLangFile() {
      try {
         this.langFile.save(this.file);
      } catch (IOException var2) {
         TerraformGeneratorPlugin.logger.stackTrace(var2);
      }

   }

   public void reloadLangFile() {
      this.cache.clear();
      this.langFile = YamlConfiguration.loadConfiguration(this.file);
   }
}
