package me.SuperRonanCraft.BetterRTP.references.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class FileOther {
   List<FileOther.FILETYPE> types = new ArrayList();

   void load() {
      this.types.clear();
      FileOther.FILETYPE[] var1 = FileOther.FILETYPE.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         FileOther.FILETYPE type = var1[var3];
         type.load();
         this.types.add(type);
      }

   }

   public static enum FILETYPE implements FileData {
      CONFIG("config"),
      ECO("economy"),
      SIGNS("signs"),
      EFFECTS("effects"),
      LOCATIONS("locations"),
      PLACEHOLDERS("placeholders");

      private final String fileName;
      private final YamlConfiguration config = new YamlConfiguration();
      private final File file;

      private FILETYPE(String str) {
         this.fileName = str + ".yml";
         this.file = new File(this.plugin().getDataFolder(), this.fileName);
      }

      public Plugin plugin() {
         return BetterRTP.getInstance();
      }

      public YamlConfiguration getConfig() {
         return this.config;
      }

      public File getFile() {
         return this.file;
      }

      public String fileName() {
         return this.fileName;
      }

      // $FF: synthetic method
      private static FileOther.FILETYPE[] $values() {
         return new FileOther.FILETYPE[]{CONFIG, ECO, SIGNS, EFFECTS, LOCATIONS, PLACEHOLDERS};
      }
   }
}
