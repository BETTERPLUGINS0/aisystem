package com.nisovin.shopkeepers.shopobjects.entity.base;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.config.Settings;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class BaseEntityShops {
   private final SKShopkeepersPlugin plugin;
   private final EntityAI entityAI;
   private final BaseEntityShopListener baseEntityShopListener;

   public BaseEntityShops(SKShopkeepersPlugin plugin) {
      this.plugin = plugin;
      this.entityAI = new EntityAI(plugin);
      this.baseEntityShopListener = new BaseEntityShopListener(plugin);
   }

   public void onEnable() {
      this.entityAI.onEnable();
      this.baseEntityShopListener.onEnable();
   }

   public void onDisable() {
      this.baseEntityShopListener.onDisable();
      this.entityAI.onDisable();
   }

   public EntityAI getEntityAI() {
      return this.entityAI;
   }

   void forceEntitySpawn(Location location, EntityType entityType) {
      if (Settings.bypassSpawnBlocking) {
         this.plugin.getForcingEntitySpawner().forceEntitySpawn(location, entityType);
      }

   }
}
