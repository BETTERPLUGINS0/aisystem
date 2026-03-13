package me.gypopo.economyshopgui.providers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.events.stands.StandListener;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.StandStorage;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.stands.ChunkLoc;
import me.gypopo.economyshopgui.objects.stands.Stand;
import me.gypopo.economyshopgui.objects.stands.StandLoc;
import me.gypopo.economyshopgui.objects.stands.StandSettings;
import me.gypopo.economyshopgui.objects.stands.StandType;
import me.gypopo.economyshopgui.util.exceptions.StandLoadException;
import me.gypopo.economyshopgui.util.exceptions.StandUnloadException;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

public class StandProvider {
   private final EconomyShopGUI plugin;
   private final StandStorage storage;
   private final Set<Stand> stands;
   private final Map<UUID, Stand> loaded = new HashMap();
   private final Set<StandLoc> spawnLocations = new HashSet();

   public StandProvider(EconomyShopGUI plugin) {
      this.plugin = plugin;
      this.storage = new StandStorage(plugin);
      this.stands = this.storage.getStoredStands();
      this.plugin.getServer().getPluginManager().registerEvents(new StandListener(this.plugin, this), this.plugin);
      SendMessage.infoMessage(Lang.LOADED_SHOP_STAND_MODULE.get().replace("%count%", String.valueOf(this.stands.size())));
      this.plugin.runTaskLater(this::loadSpawnRegions, 20L);
   }

   public Stand getStand(UUID uuid) {
      return (Stand)this.loaded.get(uuid);
   }

   public Stand getStand(StandLoc loc) {
      return (Stand)this.stands.stream().filter((e) -> {
         return e.getLoc().equals(loc);
      }).findFirst().orElse((Object)null);
   }

   public Stand getStand(int id) {
      return (Stand)this.stands.stream().filter((e) -> {
         return e.getId() == id;
      }).findFirst().orElse((Object)null);
   }

   public Set<Stand> getStands() {
      return this.stands;
   }

   public void createStand(Block block, String item, StandType type) throws StandLoadException {
      StandLoc loc = new StandLoc(block);
      Stand stand = new Stand(this.stands.size() + 1, loc, item, type, new StandSettings());
      UUID uuid = stand.load();
      this.stands.add(stand);
      this.loaded.put(uuid, stand);
   }

   public void loadStands(ChunkLoc chunkLoc) {
      Set<Stand> stands = (Set)this.stands.stream().filter((e) -> {
         return chunkLoc.contains(e.getLoc());
      }).collect(Collectors.toSet());
      this.loadStands(stands);
   }

   public boolean reloadStand(Stand stand) {
      try {
         stand.unload();
         this.loaded.remove(stand.getStand());
      } catch (StandUnloadException var3) {
         SendMessage.errorMessage(Lang.SHOP_STAND_UNLOAD_FAILED.get().replace("%id%", String.valueOf(stand.getId())).replace("%reason%", var3.getMessage()));
         return false;
      }

      this.plugin.runTaskLater(() -> {
         try {
            UUID uuid = stand.load();
            this.loaded.put(uuid, stand);
         } catch (StandLoadException var3) {
            SendMessage.errorMessage(Lang.SHOP_STAND_LOAD_FAILED.get().replace("%id%", String.valueOf(stand.getId())).replace("%reason%", var3.getMessage()));
         }

      }, 2L);
      return true;
   }

   private void loadStands(Set<Stand> stands) {
      Iterator var2 = stands.iterator();

      while(var2.hasNext()) {
         Stand stand = (Stand)var2.next();

         try {
            UUID uuid = stand.load();
            this.loaded.put(uuid, stand);
         } catch (StandLoadException var5) {
            SendMessage.errorMessage(Lang.SHOP_STAND_LOAD_FAILED.get().replace("%id%", String.valueOf(stand.getId())).replace("%reason%", var5.getMessage()));
         }
      }

   }

   public void checkAndUnload(ChunkLoc chunkLoc) {
      Set<Stand> stands = (Set)this.loaded.values().stream().filter((e) -> {
         return chunkLoc.contains(e.getLoc());
      }).collect(Collectors.toSet());
      this.unloadStands(stands);
   }

   private void unloadStands(Set<Stand> stands) {
      Iterator var2 = stands.iterator();

      while(var2.hasNext()) {
         Stand stand = (Stand)var2.next();

         try {
            stand.unload();
         } catch (StandUnloadException var5) {
            SendMessage.errorMessage(Lang.SHOP_STAND_UNLOAD_FAILED.get().replace("%id%", String.valueOf(stand.getId())).replace("%reason%", var5.getMessage()));
         }
      }

   }

   private void loadSpawnRegions() {
      Iterator var1 = Bukkit.getWorlds().iterator();

      while(var1.hasNext()) {
         World world = (World)var1.next();
         this.spawnLocations.add(new StandLoc(world.getSpawnLocation()));
         Chunk[] var3 = world.getLoadedChunks();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Chunk chunk = var3[var5];
            this.loadStands(new ChunkLoc(chunk));
         }
      }

   }

   public void destroy(Stand stand) {
      try {
         stand.unload();
         stand.getLoc().toBukkit().getBlock().setType(Material.AIR);
         this.stands.remove(stand);
         this.loaded.remove(stand.getStand());
         SendMessage.infoMessage(Lang.REMOVED_SHOP_STAND.get().replace("%location%", stand.getLoc().toString()));
      } catch (StandUnloadException var3) {
         SendMessage.errorMessage(Lang.SHOP_STAND_UNLOAD_FAILED.get().replace("%id%", String.valueOf(stand.getId())).replace("%reason%", var3.getMessage()));
         SendMessage.errorMessage("Forcing removal...");
         this.unloadInvalid(stand, stand.getLoc().toBukkit());
      }

   }

   private void unloadInvalid(Stand stand, Location loc) {
      loc.add(0.5D, 0.5D, 0.5D);
      Collection<Entity> entitys = loc.getWorld().getNearbyEntities(loc, 1.0D, 1.0D, 1.0D);
      Iterator var4 = entitys.iterator();

      Entity e;
      while(var4.hasNext()) {
         e = (Entity)var4.next();
         if (e instanceof Item && this.matchesLoc(e.getLocation(), loc)) {
            e.remove();
         }
      }

      loc.subtract(0.0D, 0.5D, 0.0D);
      entitys = loc.getWorld().getNearbyEntities(loc, 1.0D, 1.0D, 1.0D);
      var4 = entitys.iterator();

      while(var4.hasNext()) {
         e = (Entity)var4.next();
         if (e instanceof ArmorStand && e.getLocation().equals(loc)) {
            e.remove();
         }
      }

      this.stands.remove(stand);
      this.loaded.remove(stand.getStand());
   }

   private boolean matchesLoc(Location loc1, Location loc2) {
      return loc1.getX() == loc2.getX() && loc1.getY() == loc2.getY() && loc1.getZ() == loc2.getZ();
   }

   public void reload() {
      this.unloadStands(new HashSet(this.loaded.values()));
      if (!this.loaded.isEmpty()) {
         this.loaded.clear();
      }

      this.loadSpawnRegions();
      SendMessage.infoMessage(Lang.LOADED_SHOP_STAND_MODULE.get().replace("%count%", String.valueOf(this.stands.size())));
   }

   public void stop() {
      Iterator var1 = this.loaded.values().iterator();

      while(var1.hasNext()) {
         Stand stand = (Stand)var1.next();

         try {
            stand.unload();
         } catch (StandUnloadException var4) {
            SendMessage.errorMessage(Lang.SHOP_STAND_UNLOAD_FAILED.get().replace("%id%", String.valueOf(stand.getId())).replace("%reason%", var4.getMessage()));
         }
      }

      this.storage.save(this.stands);
   }
}
