package me.gypopo.economyshopgui.objects.stands;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.util.ServerInfo;
import me.gypopo.economyshopgui.util.XMaterial;
import me.gypopo.economyshopgui.util.exceptions.StandLoadException;
import me.gypopo.economyshopgui.util.exceptions.StandUnloadException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;

public class Stand {
   private final int id;
   private final String item;
   private final StandLoc loc;
   private final StandType type;
   private final StandSettings settings;
   private UUID displayItem;
   private UUID stand;
   private boolean loaded;

   public Stand(int id, StandLoc loc, String item, StandType type, StandSettings settings) throws StandLoadException {
      this.loc = loc;
      this.item = item;
      this.type = type;
      this.settings = settings;
      this.id = id;
   }

   public UUID load() throws StandLoadException {
      if (this.loaded) {
         return this.stand;
      } else {
         Location location = this.loc.toBukkit();
         if (!this.isStillValid(location)) {
            throw new StandLoadException(Lang.BLOCK_FROM_SHOP_STAND_NOT_FOUND.get().replace("%location%", this.loc.toString()).replace("%type%", this.type.name()));
         } else {
            ShopItem shopItem = EconomyShopGUI.getInstance().getShopItem(this.item);
            if (shopItem == null) {
               throw new StandLoadException(Lang.ITEM_FROM_SHOP_STAND_NOT_FOUND.get().replace("%item%", this.item));
            } else {
               this.displayItem = this.getDisplayItem(this.loc.toBukkit());
               if (this.displayItem == null) {
                  this.loadDisplayItem(shopItem, this.loc.toBukkit());
               }

               this.stand = this.getStand(this.loc.toBukkit());
               if (this.stand == null) {
                  this.loadStand(shopItem, this.loc.toBukkit());
               }

               this.loaded = true;
               return this.stand;
            }
         }
      }
   }

   public void unload() throws StandUnloadException {
      if (this.loaded) {
         Location location = this.loc.toBukkit();
         if (!this.isStillValid(location)) {
            throw new StandUnloadException(Lang.BLOCK_FROM_SHOP_STAND_NOT_FOUND.get().replace("%location%", this.loc.toString()).replace("%type%", this.type.name()));
         } else if (!Bukkit.getWorld(this.loc.world).isChunkLoaded(this.loc.toBukkit().getBlockX() >> 4, this.loc.toBukkit().getBlockZ() >> 4)) {
            throw new StandUnloadException(Lang.SHOP_STAND_INSIDE_UNLOADED_CHUNK.get());
         } else {
            this.unloadDisplayItem();
            this.unloadStand();
            this.loaded = false;
         }
      }
   }

   public boolean isLoaded() {
      return this.loaded;
   }

   public int getId() {
      return this.id;
   }

   public StandLoc getLoc() {
      return this.loc;
   }

   public String getItem() {
      return this.item;
   }

   public StandType getType() {
      return this.type;
   }

   public UUID getStand() {
      return this.stand;
   }

   public UUID getDisplayItem() {
      return this.displayItem;
   }

   public StandSettings getSettings() {
      return this.settings;
   }

   public boolean isStillValid(Location loc) {
      Block block = loc.getWorld().getBlockAt(loc);
      return this.type.getType() == XMaterial.matchXMaterial(block);
   }

   private void loadDisplayItem(ShopItem shopItem, Location loc) throws StandLoadException {
      loc.add(0.5D, 0.5D, 0.5D);
      Item item = loc.getWorld().dropItem(loc, new ItemStack(shopItem.getShopItem()));
      item.setPickupDelay(Integer.MAX_VALUE);
      item.setVelocity(new Vector(0, 0, 0));
      item.setMetadata("SHOP_STAND", new FixedMetadataValue(EconomyShopGUI.getInstance(), this.id));
      item.setCustomName(shopItem.getDisplayname());
      if (this.settings.isHologram() && ConfigManager.getConfig().getBoolean("shop-stands.holograms")) {
         item.setCustomNameVisible(true);
      }

      if (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_21_R2)) {
         item.setUnlimitedLifetime(true);
      } else {
         this.setLived(item, -32768);
      }

      if (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_10_R1)) {
         item.setGravity(false);
      }

      if (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_13_R1)) {
         item.setPersistent(false);
      }

      this.displayItem = item.getUniqueId();
   }

   private void setLived(Item item, int lived) {
      try {
         Class<?> ITEM_ENTITY_CLASS = !ServerInfo.isNMS() ? Class.forName("net.minecraft.server." + ServerInfo.getVersion().name() + ".EntityItem") : Class.forName("net.minecraft.world.entity.item.EntityItem");
         Field field = ITEM_ENTITY_CLASS.getDeclaredField(ServerInfo.supportsComponents() ? "i" : (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_19_R1) ? "g" : (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_17_R1) ? "ao" : "age")));
         field.setAccessible(true);
         Class<?> CRAFT_ENTITY_CLASS = Class.forName("org.bukkit.craftbukkit." + ServerInfo.getVersion().name() + ".entity.CraftEntity");
         Field field2 = ServerInfo.getVersion().getSimple() >= ServerInfo.Version.v1_20_R1.getSimple() ? CRAFT_ENTITY_CLASS.getDeclaredField("entity") : item.getClass().getDeclaredField("item");
         field2.setAccessible(true);
         Object handle = field2.get(item);
         field.set(handle, lived);
      } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException var8) {
         SendMessage.errorMessage("Failed to create display item for shop stand");
         var8.printStackTrace();
      }

   }

   private UUID getDisplayItem(Location loc) {
      loc.add(0.5D, 0.5D, 0.5D);
      Collection<Entity> entitys = loc.getWorld().getNearbyEntities(loc, 1.0D, 1.0D, 1.0D);
      Iterator var3 = entitys.iterator();

      Entity e;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         e = (Entity)var3.next();
      } while(!(e instanceof Item) || !this.matchesLoc(e.getLocation(), loc) || !this.matchTag(e));

      return e.getUniqueId();
   }

   private void loadStand(ShopItem item, Location loc) {
      loc.add(0.5D, 0.0D, 0.5D);
      ArmorStand stand = (ArmorStand)loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
      stand.setVisible(false);
      stand.setArms(false);
      stand.setBasePlate(false);
      stand.setMetadata("SHOP_STAND", new FixedMetadataValue(EconomyShopGUI.getInstance(), this.id));
      if (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_10_R1)) {
         stand.setGravity(false);
      }

      if (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_13_R1)) {
         stand.setPersistent(false);
      }

      this.stand = stand.getUniqueId();
   }

   private UUID getStand(Location loc) {
      loc.add(0.5D, 0.0D, 0.5D);
      Collection<Entity> entitys = loc.getWorld().getNearbyEntities(loc, 1.0D, 1.0D, 1.0D);
      Iterator var3 = entitys.iterator();

      Entity e;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         e = (Entity)var3.next();
      } while(!(e instanceof ArmorStand) || !e.getLocation().equals(loc) || !this.matchTag(e));

      return e.getUniqueId();
   }

   private void unloadDisplayItem() {
      Iterator var1 = Bukkit.getWorld(this.loc.world).getNearbyEntities(this.loc.toBukkit(), 1.0D, 1.0D, 1.0D).iterator();

      while(var1.hasNext()) {
         Entity entity = (Entity)var1.next();
         if (entity.getUniqueId().equals(this.displayItem)) {
            entity.remove();
         }
      }

   }

   private void unloadStand() {
      Iterator var1 = Bukkit.getWorld(this.loc.world).getNearbyEntities(this.loc.toBukkit(), 1.0D, 1.0D, 1.0D).iterator();

      while(var1.hasNext()) {
         Entity entity = (Entity)var1.next();
         if (entity.getUniqueId().equals(this.stand)) {
            entity.remove();
         }
      }

   }

   private boolean matchesLoc(Location loc1, Location loc2) {
      return loc1.getX() == loc2.getX() && loc1.getY() == loc2.getY() && loc1.getZ() == loc2.getZ();
   }

   private boolean matchTag(Entity e) {
      if (ServerInfo.getVersion().olderOrEqualAs(ServerInfo.Version.v1_12_R1)) {
         return true;
      } else {
         Iterator var2 = e.getMetadata("SHOP_STAND").iterator();

         MetadataValue meta;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            meta = (MetadataValue)var2.next();
         } while(meta.asInt() != this.id);

         return true;
      }
   }

   public JSONObject serialize() {
      Map<String, Object> keys = new HashMap();
      keys.put("stand_id", this.id);
      keys.put("location", this.loc.getSimple());
      keys.put("item", this.item);
      keys.put("type", this.type.name());
      return new JSONObject(keys);
   }
}
