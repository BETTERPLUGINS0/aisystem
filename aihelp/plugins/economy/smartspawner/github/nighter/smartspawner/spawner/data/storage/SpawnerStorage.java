package github.nighter.smartspawner.spawner.data.storage;

import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.Map;

public interface SpawnerStorage {
   boolean initialize();

   void shutdown();

   Map<String, SpawnerData> loadAllSpawnersRaw();

   SpawnerData loadSpecificSpawner(String var1);

   void markSpawnerModified(String var1);

   void markSpawnerDeleted(String var1);

   void queueSpawnerForSaving(String var1);

   void flushChanges();

   String getRawLocationString(String var1);
}
