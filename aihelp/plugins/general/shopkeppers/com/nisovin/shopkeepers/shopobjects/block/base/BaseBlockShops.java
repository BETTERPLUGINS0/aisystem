package com.nisovin.shopkeepers.shopobjects.block.base;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopobjects.ShopObjectType;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.util.bukkit.BlockLocation;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Iterator;
import org.bukkit.block.Block;

public class BaseBlockShops {
   private final SKShopkeepersPlugin plugin;
   private final SKShopkeeperRegistry shopkeeperRegistry;
   private final BaseBlockShopListener blockShopListener;

   public BaseBlockShops(SKShopkeepersPlugin plugin) {
      this.plugin = plugin;
      this.shopkeeperRegistry = plugin.getShopkeeperRegistry();
      this.blockShopListener = new BaseBlockShopListener(plugin, (BaseBlockShops)Unsafe.initialized(this));
   }

   public void onEnable() {
      if (this.shallEnable()) {
         this.blockShopListener.onEnable();
      }
   }

   private boolean shallEnable() {
      Iterator var1 = this.plugin.getShopObjectTypeRegistry().getRegisteredTypes().iterator();

      ShopObjectType shopObjectType;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         shopObjectType = (ShopObjectType)var1.next();
      } while(!(shopObjectType instanceof BaseBlockShopObjectType) || !shopObjectType.isEnabled());

      return true;
   }

   public void onDisable() {
      this.blockShopListener.onDisable();
   }

   public boolean isBaseBlockShop(Shopkeeper shopkeeper) {
      return shopkeeper.getShopObject() instanceof BaseBlockShopObject;
   }

   public boolean isBaseBlockShop(Block block) {
      Validate.notNull(block, (String)"block is null");
      return this.isBaseBlockShop(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
   }

   public boolean isBaseBlockShop(String worldName, int blockX, int blockY, int blockZ) {
      Shopkeeper shopkeeper = this.shopkeeperRegistry.getShopkeeperByBlock(worldName, blockX, blockY, blockZ);
      return shopkeeper != null && this.isBaseBlockShop((Shopkeeper)shopkeeper);
   }

   public void addBlockPhysicsCancellation(Block block) {
      this.blockShopListener.addBlockPhysicsCancellation(block);
   }

   public void removeBlockPhysicsCancellation(Block block) {
      this.blockShopListener.removeBlockPhysicsCancellation(block);
   }

   public void addBlockPhysicsCancellation(BlockLocation blockLocation) {
      this.blockShopListener.addBlockPhysicsCancellation(blockLocation);
   }

   public void removeBlockPhysicsCancellation(BlockLocation blockLocation) {
      this.blockShopListener.removeBlockPhysicsCancellation(blockLocation);
   }
}
