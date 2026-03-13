package com.nisovin.shopkeepers.shopobjects.virtual;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;

public class VirtualShops {
   private final SKShopkeepersPlugin plugin;
   private final SKVirtualShopObjectType virtualShopObjectType = new SKVirtualShopObjectType((VirtualShops)Unsafe.initialized(this));

   public VirtualShops(SKShopkeepersPlugin plugin) {
      this.plugin = plugin;
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public SKVirtualShopObjectType getSignShopObjectType() {
      return this.virtualShopObjectType;
   }
}
