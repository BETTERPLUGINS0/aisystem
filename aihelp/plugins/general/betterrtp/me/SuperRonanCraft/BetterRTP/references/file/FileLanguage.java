package me.SuperRonanCraft.BetterRTP.references.file;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class FileLanguage implements FileData {
   private final YamlConfiguration config = new YamlConfiguration();
   private final String[] defaultLangs = new String[]{"en.yml", "br.yml", "chs.yml", "cht.yml", "cs.yml", "da.yml", "de.yml", "es.yml", "fr.yml", "he.yml", "hu.yml", "it.yml", "ja.yml", "nl.yml", "no.yml", "pl.yml", "ro.yml", "ru.yml", "tr.yml", "vi.yml"};

   public YamlConfiguration getConfig() {
      return this.config;
   }

   public File getFile() {
      return null;
   }

   public String fileName() {
      return null;
   }

   public Plugin plugin() {
      return BetterRTP.getInstance();
   }

   public void load() {
      this.generateDefaults();
      String fileName = "lang" + File.separator + FileOther.FILETYPE.CONFIG.getString("Language-File");
      File file = new File(this.plugin().getDataFolder(), fileName);
      if (!file.exists()) {
         fileName = "lang" + File.separator + this.defaultLangs[0];
         file = new File(this.plugin().getDataFolder(), fileName);
      }

      try {
         this.config.load(file);
         InputStream in = this.plugin().getResource(fileName);
         if (in == null) {
            in = this.plugin().getResource(fileName.replace(File.separator, "/"));
         }

         if (in != null) {
            this.config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(in)));
            this.config.options().copyDefaults(true);
            in.close();
         }

         this.config.save(file);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   private void generateDefaults() {
      String[] var1 = this.defaultLangs;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String yaml = var1[var3];
         this.generateDefaultConfig(yaml, yaml);
         if (!yaml.equals(this.defaultLangs[0])) {
            this.generateDefaultConfig(yaml, this.defaultLangs[0]);
         }
      }

   }

   private void generateDefaultConfig(String fName, String fNameDef) {
      String fileName = "lang" + File.separator + fName;
      File file = new File(this.plugin().getDataFolder(), fileName);
      if (!file.exists()) {
         this.plugin().saveResource(fileName, false);
      }

      try {
         YamlConfiguration config = new YamlConfiguration();
         config.load(file);
         String fileNameDef = "lang" + File.separator + fNameDef;
         InputStream in = this.plugin().getResource(fileNameDef);
         if (in == null) {
            in = this.plugin().getResource(fileNameDef.replace(File.separator, "/"));
         }

         if (in != null) {
            config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(in, StandardCharsets.UTF_8)));
            config.options().copyDefaults(true);
            in.close();
         }

         config.save(file);
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }
}
