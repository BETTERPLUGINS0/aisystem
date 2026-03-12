package fr.xephi.authme.data.limbo.persistence;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.limbo.LimboPlayer;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.com.google.common.io.Files;
import fr.xephi.authme.libs.com.google.common.reflect.TypeToken;
import fr.xephi.authme.libs.com.google.gson.Gson;
import fr.xephi.authme.libs.com.google.gson.GsonBuilder;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.LimboSettings;
import fr.xephi.authme.util.FileUtils;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import org.bukkit.entity.Player;

class DistributedFilesPersistenceHandler implements LimboPersistenceHandler {
   private static final Type LIMBO_MAP_TYPE = (new TypeToken<Map<String, LimboPlayer>>() {
   }).getType();
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(DistributedFilesPersistenceHandler.class);
   private final File cacheFolder;
   private final Gson gson;
   private final SegmentNameBuilder segmentNameBuilder;

   @Inject
   DistributedFilesPersistenceHandler(@DataFolder File dataFolder, BukkitService bukkitService, Settings settings) {
      this.cacheFolder = new File(dataFolder, "playerdata");
      FileUtils.createDirectory(this.cacheFolder);
      this.gson = (new GsonBuilder()).registerTypeAdapter(LimboPlayer.class, new LimboPlayerSerializer()).registerTypeAdapter(LimboPlayer.class, new LimboPlayerDeserializer(bukkitService)).setPrettyPrinting().create();
      this.segmentNameBuilder = new SegmentNameBuilder((SegmentSize)settings.getProperty(LimboSettings.DISTRIBUTION_SIZE));
      this.convertOldDataToCurrentSegmentScheme();
      this.deleteEmptyFiles();
   }

   public LimboPlayer getLimboPlayer(Player player) {
      String uuid = player.getUniqueId().toString();
      File file = this.getPlayerSegmentFile(uuid);
      Map<String, LimboPlayer> entries = this.readLimboPlayers(file);
      return entries == null ? null : (LimboPlayer)entries.get(uuid);
   }

   public void saveLimboPlayer(Player player, LimboPlayer limbo) {
      String uuid = player.getUniqueId().toString();
      File file = this.getPlayerSegmentFile(uuid);
      Map<String, LimboPlayer> entries = null;
      if (file.exists()) {
         entries = this.readLimboPlayers(file);
      } else {
         FileUtils.create(file);
      }

      if (entries == null) {
         entries = new HashMap();
      }

      ((Map)entries).put(uuid, limbo);
      this.saveEntries((Map)entries, file);
   }

   public void removeLimboPlayer(Player player) {
      String uuid = player.getUniqueId().toString();
      File file = this.getPlayerSegmentFile(uuid);
      if (file.exists()) {
         Map<String, LimboPlayer> entries = this.readLimboPlayers(file);
         if (entries != null && entries.remove(uuid) != null) {
            this.saveEntries(entries, file);
         }
      }

   }

   public LimboPersistenceType getType() {
      return LimboPersistenceType.DISTRIBUTED_FILES;
   }

   private void saveEntries(Map<String, LimboPlayer> entries, File file) {
      try {
         FileWriter fw = new FileWriter(file);

         try {
            this.gson.toJson((Object)entries, (Appendable)fw);
         } catch (Throwable var7) {
            try {
               fw.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }

            throw var7;
         }

         fw.close();
      } catch (Exception var8) {
         this.logger.logException("Could not write to '" + file + "':", var8);
      }

   }

   private Map<String, LimboPlayer> readLimboPlayers(File file) {
      if (!file.exists()) {
         return null;
      } else {
         try {
            return (Map)this.gson.fromJson(Files.asCharSource(file, StandardCharsets.UTF_8).read(), LIMBO_MAP_TYPE);
         } catch (Exception var3) {
            this.logger.logException("Failed reading '" + file + "':", var3);
            return null;
         }
      }
   }

   private File getPlayerSegmentFile(String uuid) {
      String segment = this.segmentNameBuilder.createSegmentName(uuid);
      return this.getSegmentFile(segment);
   }

   private File getSegmentFile(String segmentId) {
      return new File(this.cacheFolder, segmentId + "-limbo.json");
   }

   private void convertOldDataToCurrentSegmentScheme() {
      String currentPrefix = this.segmentNameBuilder.getPrefix();
      File[] files = this.listFiles(this.cacheFolder);
      Map<String, LimboPlayer> allLimboPlayers = new HashMap();
      List<File> migratedFiles = new ArrayList();
      File[] var5 = files;
      int var6 = files.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         File file = var5[var7];
         if (isLimboJsonFile(file) && !file.getName().startsWith(currentPrefix)) {
            Map<String, LimboPlayer> data = this.readLimboPlayers(file);
            if (data != null) {
               allLimboPlayers.putAll(data);
               migratedFiles.add(file);
            }
         }
      }

      if (!allLimboPlayers.isEmpty()) {
         this.saveToNewSegments(allLimboPlayers);
         migratedFiles.forEach(FileUtils::delete);
      }

   }

   private void saveToNewSegments(Map<String, LimboPlayer> limbosFromOldSegments) {
      Map<String, Map<String, LimboPlayer>> limboBySegment = this.groupBySegment(limbosFromOldSegments);
      this.logger.info("Saving " + limbosFromOldSegments.size() + " LimboPlayers from old segments into " + limboBySegment.size() + " current segments");
      Iterator var3 = limboBySegment.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, Map<String, LimboPlayer>> entry = (Entry)var3.next();
         File file = this.getSegmentFile((String)entry.getKey());
         Map<String, LimboPlayer> limbosToSave = (Map)Optional.ofNullable(this.readLimboPlayers(file)).orElseGet(HashMap::new);
         limbosToSave.putAll((Map)entry.getValue());
         this.saveEntries(limbosToSave, file);
      }

   }

   private Map<String, Map<String, LimboPlayer>> groupBySegment(Map<String, LimboPlayer> readLimboPlayers) {
      Map<String, Map<String, LimboPlayer>> limboBySegment = new HashMap();
      Iterator var3 = readLimboPlayers.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, LimboPlayer> entry = (Entry)var3.next();
         String segmentId = this.segmentNameBuilder.createSegmentName((String)entry.getKey());
         ((Map)limboBySegment.computeIfAbsent(segmentId, (s) -> {
            return new HashMap();
         })).put((String)entry.getKey(), (LimboPlayer)entry.getValue());
      }

      return limboBySegment;
   }

   private void deleteEmptyFiles() {
      File[] files = this.listFiles(this.cacheFolder);
      long deletedFiles = Arrays.stream(files).filter((f) -> {
         return isLimboJsonFile(f) && f.length() < 3L;
      }).peek(FileUtils::delete).count();
      this.logger.debug("Limbo: Deleted {0} empty segment files", (Object)deletedFiles);
   }

   private static boolean isLimboJsonFile(File file) {
      String name = file.getName();
      return name.startsWith("seg") && name.endsWith("-limbo.json");
   }

   private File[] listFiles(File folder) {
      File[] files = folder.listFiles();
      if (files == null) {
         this.logger.warning("Could not get files of '" + folder + "'");
         return new File[0];
      } else {
         return files;
      }
   }
}
