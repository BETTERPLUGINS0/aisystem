package com.badbones69.crazyauctions.api.enums;

import com.badbones69.crazyauctions.CrazyAuctions;
import libs.com.ryderbelserion.vital.paper.api.files.FileManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public enum Files {
   config("config.yml"),
   messages("messages.yml"),
   data("data.yml");

   private final String fileName;
   private final String strippedName;
   @NotNull
   private final CrazyAuctions plugin = (CrazyAuctions)JavaPlugin.getPlugin(CrazyAuctions.class);
   @NotNull
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

   public void save() {
      this.fileManager.saveFile(this.fileName);
   }

   public void reload() {
      this.fileManager.addFile(this.fileName);
   }

   // $FF: synthetic method
   private static Files[] $values() {
      return new Files[]{config, messages, data};
   }
}
