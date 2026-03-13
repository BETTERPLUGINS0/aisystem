package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.PredicateUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class EntityUtils {
   private static final double GROUND_DISTANCE_CHECK_OFFSET = 0.98D;
   private static final double GROUND_DISTANCE_CHECK_RANGE = 1.0D;
   private static final Location SHARED_LOCATION = new Location((World)null, 0.0D, 0.0D, 0.0D);
   private static final Set<Material> LAVA;
   private static final int ENTITY_TARGET_RANGE = 10;

   public static Set<? extends Material> getCollidableFluids(EntityType entityType) {
      switch(entityType) {
      case STRIDER:
      case MAGMA_CUBE:
      case BLAZE:
         return LAVA;
      default:
         return Collections.emptySet();
      }
   }

   @Nullable
   public static Location getStandingLocation(EntityType entityType, Block block) {
      Location var5;
      try {
         Location location = ((Location)Unsafe.assertNonNull(block.getLocation(SHARED_LOCATION))).add(0.5D, 0.98D, 0.5D);
         double distanceToGround = WorldUtils.getCollisionDistanceToGround(location, 1.0D, getCollidableFluids(entityType));
         if (distanceToGround != 1.0D) {
            location.add(0.0D, -distanceToGround, 0.0D);
            var5 = location.clone();
            return var5;
         }

         var5 = null;
      } finally {
         SHARED_LOCATION.setWorld((World)null);
      }

      return var5;
   }

   public static boolean burnsInSunlight(EntityType entityType) {
      if (entityType == EntityType.HUSK) {
         return false;
      } else if (entityType == EntityType.PHANTOM) {
         return true;
      } else {
         Class<? extends Entity> entityClass = entityType.getEntityClass();
         if (entityClass == null) {
            return false;
         } else {
            return Zombie.class.isAssignableFrom(entityClass) || Skeleton.class.isAssignableFrom(entityClass);
         }
      }
   }

   public static boolean isRemovedOnPeacefulDifficulty(EntityType entityType) {
      assert entityType != null;

      switch(entityType) {
      case PIGLIN:
         return false;
      case SLIME:
      case GHAST:
      case PHANTOM:
         return true;
      default:
         Class<?> entityClass = entityType.getEntityClass();
         return entityClass != null ? Monster.class.isAssignableFrom(entityClass) : false;
      }
   }

   public static boolean canFly(EntityType entityType) {
      String var1 = entityType.name();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1942082154:
         if (var1.equals("PARROT")) {
            var2 = 8;
         }
         break;
      case -1734240269:
         if (var1.equals("WITHER")) {
            var2 = 11;
         }
         break;
      case -1430253686:
         if (var1.equals("ENDER_DRAGON")) {
            var2 = 5;
         }
         break;
      case -1125519454:
         if (var1.equals("HAPPY_GHAST")) {
            var2 = 7;
         }
         break;
      case 65525:
         if (var1.equals("BAT")) {
            var2 = 1;
         }
         break;
      case 65634:
         if (var1.equals("BEE")) {
            var2 = 2;
         }
         break;
      case 84873:
         if (var1.equals("VEX")) {
            var2 = 10;
         }
         break;
      case 62368121:
         if (var1.equals("ALLAY")) {
            var2 = 0;
         }
         break;
      case 63281826:
         if (var1.equals("BLAZE")) {
            var2 = 4;
         }
         break;
      case 67780065:
         if (var1.equals("GHAST")) {
            var2 = 6;
         }
         break;
      case 109585133:
         if (var1.equals("PHANTOM")) {
            var2 = 9;
         }
         break;
      case 1463990677:
         if (var1.equals("CHICKEN")) {
            var2 = 3;
         }
      }

      switch(var2) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
         return true;
      default:
         return false;
      }
   }

   public static boolean isValidMannequinPose(Pose pose) {
      switch(pose) {
      case STANDING:
      case SNEAKING:
      case SWIMMING:
      case FALL_FLYING:
      case SLEEPING:
         return true;
      default:
         return false;
      }
   }

   public static Entity resolveComplexEntity(Entity entity) {
      return (Entity)(entity instanceof ComplexEntityPart ? ((ComplexEntityPart)entity).getParent() : entity);
   }

   @Nullable
   public static EntityType parseEntityType(@Nullable String entityTypeName) {
      return (EntityType)MinecraftEnumUtils.parseEnum(EntityType.class, entityTypeName);
   }

   public static void printEntityCounts(Chunk chunk) {
      Map<EntityType, Integer> entityCounts = new EnumMap(EntityType.class);
      Entity[] entities = chunk.getEntities();
      Entity[] var3 = entities;
      int var4 = entities.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Entity entity = var3[var5];
         EntityType entityType = entity.getType();
         Integer entityCount = (Integer)entityCounts.get(entityType);
         if (entityCount == null) {
            entityCount = 0;
         }

         entityCount = entityCount + 1;
         entityCounts.put(entityType, entityCount);
      }

      String var10000 = TextUtils.getChunkString(chunk);
      Log.info("Entities of chunk " + var10000 + " (total: " + entities.length + "): " + String.valueOf(entityCounts));
   }

   public static Predicate<Entity> filterByType(Set<? extends EntityType> acceptedTypes) {
      return acceptedTypes.isEmpty() ? PredicateUtils.alwaysTrue() : (entity) -> {
         return acceptedTypes.contains(entity.getType());
      };
   }

   @Nullable
   public static Player getNearestPlayer(Location location, double radius) {
      return getNearestPlayer(location, radius, PredicateUtils.alwaysTrue());
   }

   @Nullable
   public static Player getNearestPlayer(Location location, double radius, Predicate<? super Player> filter) {
      World world = location.getWorld();
      if (world == null) {
         return null;
      } else {
         double radiusSq = radius * radius;
         Player nearestPlayer = null;
         double nearestDistanceSq = Double.MAX_VALUE;

         for(Iterator var10 = world.getPlayers().iterator(); var10.hasNext(); SHARED_LOCATION.setWorld((World)null)) {
            Player player = (Player)var10.next();
            Location playerLocation = (Location)Unsafe.assertNonNull(player.getLocation(SHARED_LOCATION));
            double distanceSq = LocationUtils.getDistanceSquared(playerLocation, location);
            if (distanceSq <= radiusSq && distanceSq < nearestDistanceSq && filter.test(player)) {
               nearestPlayer = player;
               nearestDistanceSq = distanceSq;
            }
         }

         return nearestPlayer;
      }
   }

   public static List<Player> getNearbyPlayers(Location location, double radius) {
      return getNearbyPlayers(location, radius, PredicateUtils.alwaysTrue());
   }

   public static List<Player> getNearbyPlayers(Location location, double radius, Predicate<? super Player> filter) {
      List<Player> players = new ArrayList();
      World world = location.getWorld();
      if (world == null) {
         return players;
      } else {
         double radiusSq = radius * radius;
         world.getPlayers().forEach((player) -> {
            assert player != null;

            Location playerLocation = (Location)Unsafe.assertNonNull(player.getLocation(SHARED_LOCATION));
            if (LocationUtils.getDistanceSquared(playerLocation, location) <= radiusSq && filter.test(Unsafe.assertNonNull(player))) {
               players.add(player);
            }

            SHARED_LOCATION.setWorld((World)null);
         });
         return players;
      }
   }

   public static List<Entity> getNearbyEntities(Location location, double radius, boolean loadChunks, Set<? extends EntityType> searchedTypes) {
      return getNearbyEntities(location, radius, loadChunks, filterByType(searchedTypes));
   }

   public static List<Entity> getNearbyEntities(Location location, double radius, boolean loadChunks, Predicate<? super Entity> filter) {
      Validate.notNull(filter, (String)"filter is null");
      World world = LocationUtils.getWorld(location);
      int centerChunkX = location.getBlockX() >> 4;
      int centerChunkZ = location.getBlockZ() >> 4;
      int chunkRadius = (int)(radius / 16.0D) + 1;
      double radius2 = radius * radius;
      Predicate<Entity> combinedFilter = (entity) -> {
         Location entityLoc = entity.getLocation();
         return entityLoc.distanceSquared(location) > radius2 ? false : filter.test(entity);
      };
      return getNearbyChunkEntities(world, centerChunkX, centerChunkZ, chunkRadius, loadChunks, combinedFilter);
   }

   public static List<Entity> getNearbyChunkEntities(Chunk chunk, int chunkRadius, boolean loadChunks, Set<? extends EntityType> searchedTypes) {
      return getNearbyChunkEntities(chunk, chunkRadius, loadChunks, filterByType(searchedTypes));
   }

   public static List<Entity> getNearbyChunkEntities(Chunk chunk, int chunkRadius, boolean loadChunks, Predicate<? super Entity> filter) {
      Validate.notNull(chunk, (String)"chunk is null");
      return getNearbyChunkEntities(chunk.getWorld(), chunk.getX(), chunk.getZ(), chunkRadius, loadChunks, filter);
   }

   public static List<Entity> getNearbyChunkEntities(World world, int centerChunkX, int centerChunkZ, int chunkRadius, boolean loadChunks, Predicate<? super Entity> filter) {
      Validate.notNull(world, (String)"world is null");
      Validate.notNull(filter, (String)"filter is null");
      List<Entity> entities = new ArrayList();
      if (chunkRadius < 0) {
         return entities;
      } else {
         int startX = centerChunkX - chunkRadius;
         int endX = centerChunkX + chunkRadius;
         int startZ = centerChunkZ - chunkRadius;
         int endZ = centerChunkZ + chunkRadius;

         for(int chunkX = startX; chunkX <= endX; ++chunkX) {
            for(int chunkZ = startZ; chunkZ <= endZ; ++chunkZ) {
               if (loadChunks || world.isChunkLoaded(chunkX, chunkZ)) {
                  Chunk currentChunk = world.getChunkAt(chunkX, chunkZ);
                  if (loadChunks || currentChunk.isEntitiesLoaded()) {
                     Entity[] var14 = currentChunk.getEntities();
                     int var15 = var14.length;

                     for(int var16 = 0; var16 < var15; ++var16) {
                        Entity entity = var14[var16];
                        if (!entity.getWorld().equals(world)) {
                           Log.debug(() -> {
                              String var10000 = String.valueOf(currentChunk);
                              return "Found an entity which reports to be in a different world than the chunk we got it from: Chunk=" + var10000 + ", ChunkWorld=" + String.valueOf(currentChunk.getWorld()) + ", entityType=" + String.valueOf(entity.getType()) + ", entityLocation=" + String.valueOf(entity.getLocation());
                           });
                        } else if (filter.test(entity)) {
                           entities.add(entity);
                        }
                     }
                  }
               }
            }
         }

         return entities;
      }
   }

   @Nullable
   public static Entity getTargetedEntity(Player player) {
      return getTargetedEntity(player, PredicateUtils.alwaysTrue());
   }

   @Nullable
   public static Entity getTargetedEntity(Player player, Predicate<? super Entity> filter) {
      Validate.notNull(filter, (String)"filter is null");
      Location playerLoc = player.getEyeLocation();
      World world = (World)Unsafe.assertNonNull(playerLoc.getWorld());
      Vector viewDirection = playerLoc.getDirection();
      RayTraceResult rayTraceResult = world.rayTrace(playerLoc, viewDirection, 10.0D, FluidCollisionMode.NEVER, true, 0.0D, (entity) -> {
         if (entity.isDead()) {
            return false;
         } else {
            return entity.equals(player) ? false : filter.test(entity);
         }
      });
      return rayTraceResult != null ? rayTraceResult.getHitEntity() : null;
   }

   public static Stream<Player> getOnlinePlayersStream() {
      return (Stream)Unsafe.castNonNull(Bukkit.getOnlinePlayers().stream());
   }

   private EntityUtils() {
   }

   static {
      LAVA = Collections.singleton(Material.LAVA);
   }
}
