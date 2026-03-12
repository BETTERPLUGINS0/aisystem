package emanondev.itemedit.storage;

import java.util.Arrays;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public enum StorageType {
   YAML,
   MONGODB;

   @NotNull
   public static Optional<StorageType> byName(@NotNull String name) {
      return Arrays.stream(values()).filter((storageType) -> {
         return storageType.name().equalsIgnoreCase(name);
      }).findAny();
   }

   // $FF: synthetic method
   private static StorageType[] $values() {
      return new StorageType[]{YAML, MONGODB};
   }
}
