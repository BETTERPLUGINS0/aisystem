package com.badbones69.crazyauctions.api.enums.misc;

import com.badbones69.crazyauctions.CrazyAuctions;
import java.io.File;
import libs.com.ryderbelserion.vital.paper.api.files.FileManager;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Files {
   config("config.yml"),
   messages("messages.yml"),
   data("data.yml");

   private final String fileName;
   private final String strippedName;
   private final CrazyAuctions plugin = CrazyAuctions.get();
   private final FileManager fileManager;

   private Files(final String param3) {
      this.fileManager = this.plugin.getFileManager();
      this.fileName = fileName;
      this.strippedName = this.fileName.replace(".yml", "");
   }

   public final YamlConfiguration getConfiguration() {
      return this.fileManager.getFile(this.fileName).getConfiguration();
   }

   public final String getStrippedName() {
      return this.strippedName;
   }

   public final String getFileName() {
      return this.fileName;
   }

   public void reload() {
      this.fileManager.addFile(new File(this.plugin.getDataFolder(), this.fileName));
   }

   public void save() {
      this.fileManager.saveFile(this.fileName);
   }

   // $FF: synthetic method
   private static Files[] $values() {
      return new Files[]{config, messages, data};
   }
}
