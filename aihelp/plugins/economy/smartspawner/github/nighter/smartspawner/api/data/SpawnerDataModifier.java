package github.nighter.smartspawner.api.data;

public interface SpawnerDataModifier {
   int getStackSize();

   int getMaxStackSize();

   SpawnerDataModifier setMaxStackSize(int var1);

   int getBaseMaxStoragePages();

   SpawnerDataModifier setBaseMaxStoragePages(int var1);

   int getBaseMinMobs();

   SpawnerDataModifier setBaseMinMobs(int var1);

   int getBaseMaxMobs();

   SpawnerDataModifier setBaseMaxMobs(int var1);

   int getBaseMaxStoredExp();

   SpawnerDataModifier setBaseMaxStoredExp(int var1);

   long getBaseSpawnerDelay();

   SpawnerDataModifier setBaseSpawnerDelay(long var1);

   void applyChanges();
}
