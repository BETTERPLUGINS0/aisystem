package github.nighter.smartspawner.hooks.protections.api;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.world.Dimension;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class SuperiorSkyblock2 implements Listener {
   private static final String SPAWNER_STACK_PERM = "spawner_stack";
   private static final String SPAWNER_OPEN_MENU_PERM = "spawner_open_menu";
   private static IslandPrivilege SPAWNER_STACK;
   private static IslandPrivilege SPAWNER_OPEN_MENU;
   private static boolean registered = false;

   public SuperiorSkyblock2() {
      register();
   }

   public static void register() {
      if (!registered) {
         try {
            SPAWNER_STACK = IslandPrivilege.getByName("spawner_stack");
            SPAWNER_OPEN_MENU = IslandPrivilege.getByName("spawner_open_menu");
         } catch (NullPointerException var3) {
            IslandPrivilege.register("spawner_stack");
            IslandPrivilege.register("spawner_open_menu");

            try {
               SPAWNER_STACK = IslandPrivilege.getByName("spawner_stack");
               SPAWNER_OPEN_MENU = IslandPrivilege.getByName("spawner_open_menu");
            } catch (Exception var2) {
               SmartSpawner.getInstance().getLogger().severe("Failed to register SuperiorSkyblock Hook - please open a issue on Github or on Discord");
               var3.printStackTrace();
               return;
            }
         }

         registered = true;
      }
   }

   public static boolean canPlayerStackBlock(@NotNull Player player, @NotNull Location location) {
      Island island = SuperiorSkyblockAPI.getIslandAt(location);
      if (island != null) {
         return !island.hasPermission(SuperiorSkyblockAPI.getPlayer(player.getUniqueId()), SPAWNER_STACK);
      } else {
         return false;
      }
   }

   public static boolean canPlayerOpenMenu(@NotNull Player player, @NotNull Location location) {
      Island island = SuperiorSkyblockAPI.getIslandAt(location);
      if (island != null) {
         return !island.hasPermission(SuperiorSkyblockAPI.getPlayer(player.getUniqueId()), SPAWNER_OPEN_MENU);
      } else {
         return false;
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onIslandDisband(IslandDisbandEvent event) {
      if (!event.isCancelled() && event.getIsland() != null) {
         Island island = event.getIsland();
         Iterator var3 = Dimension.values().iterator();

         while(var3.hasNext()) {
            Dimension dimension = (Dimension)var3.next();

            try {
               island.getAllChunksAsync(dimension, 3, (chunk) -> {
                  Iterator var1 = chunk.getTileEntities((block) -> {
                     return block.getType() == Material.SPAWNER;
                  }, false).iterator();

                  while(var1.hasNext()) {
                     BlockState state = (BlockState)var1.next();
                     SpawnerData spawner = SmartSpawner.getInstance().getSpawnerManager().getSpawnerByLocation(state.getBlock().getLocation());
                     if (spawner != null) {
                        SmartSpawner.getInstance().getSpawnerManager().removeGhostSpawner(spawner.getSpawnerId());
                     }
                  }

               });
            } catch (NullPointerException var6) {
            }
         }

      }
   }
}
