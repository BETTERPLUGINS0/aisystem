package github.nighter.smartspawner.utils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.entity.EntityType;

public class DynamicEntityValidator {
   private static final Set<EntityType> VALID_SPAWNABLE_ENTITIES = (Set)Arrays.stream(EntityType.values()).filter((type) -> {
      return type != EntityType.PLAYER;
   }).collect(Collectors.toUnmodifiableSet());

   public static boolean isValidSpawnerEntity(EntityType type) {
      return VALID_SPAWNABLE_ENTITIES.contains(type);
   }

   public static Set<EntityType> getValidEntities() {
      return VALID_SPAWNABLE_ENTITIES;
   }
}
