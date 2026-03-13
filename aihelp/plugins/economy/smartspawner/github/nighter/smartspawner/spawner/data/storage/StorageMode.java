package github.nighter.smartspawner.spawner.data.storage;

public enum StorageMode {
   YAML,
   MYSQL,
   SQLITE;

   // $FF: synthetic method
   private static StorageMode[] $values() {
      return new StorageMode[]{YAML, MYSQL, SQLITE};
   }
}
