package fr.xephi.authme.data.limbo.persistence;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.limbo.LimboPlayer;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.com.google.common.io.Files;
import fr.xephi.authme.libs.com.google.gson.Gson;
import fr.xephi.authme.libs.com.google.gson.GsonBuilder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.bukkit.entity.Player;

class IndividualFilesPersistenceHandler implements LimboPersistenceHandler {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(IndividualFilesPersistenceHandler.class);
   private final Gson gson;
   private final File cacheDir;

   @Inject
   IndividualFilesPersistenceHandler(@DataFolder File dataFolder, BukkitService bukkitService) {
      this.cacheDir = new File(dataFolder, "playerdata");
      if (!this.cacheDir.exists() && !this.cacheDir.isDirectory() && !this.cacheDir.mkdir()) {
         this.logger.warning("Failed to create playerdata directory '" + this.cacheDir + "'");
      }

      this.gson = (new GsonBuilder()).registerTypeAdapter(LimboPlayer.class, new LimboPlayerSerializer()).registerTypeAdapter(LimboPlayer.class, new LimboPlayerDeserializer(bukkitService)).setPrettyPrinting().create();
   }

   public LimboPlayer getLimboPlayer(Player player) {
      String id = player.getUniqueId().toString();
      File file = new File(this.cacheDir, id + File.separator + "data.json");
      if (!file.exists()) {
         return null;
      } else {
         try {
            String str = Files.asCharSource(file, StandardCharsets.UTF_8).read();
            return (LimboPlayer)this.gson.fromJson(str, LimboPlayer.class);
         } catch (IOException var5) {
            this.logger.logException("Could not read player data on disk for '" + player.getName() + "'", var5);
            return null;
         }
      }
   }

   public void saveLimboPlayer(Player player, LimboPlayer limboPlayer) {
      String id = player.getUniqueId().toString();

      try {
         File file = new File(this.cacheDir, id + File.separator + "data.json");
         Files.createParentDirs(file);
         Files.touch(file);
         Files.write(this.gson.toJson((Object)limboPlayer), file, StandardCharsets.UTF_8);
      } catch (IOException var5) {
         this.logger.logException("Failed to write " + player.getName() + " data:", var5);
      }

   }

   public void removeLimboPlayer(Player player) {
      String id = player.getUniqueId().toString();
      File file = new File(this.cacheDir, id);
      if (file.exists()) {
         FileUtils.purgeDirectory(file);
         FileUtils.delete(file);
      }

   }

   public LimboPersistenceType getType() {
      return LimboPersistenceType.INDIVIDUAL_FILES;
   }
}
