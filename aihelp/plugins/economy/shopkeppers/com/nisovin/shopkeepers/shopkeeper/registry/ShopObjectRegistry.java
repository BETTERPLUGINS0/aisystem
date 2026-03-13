package com.nisovin.shopkeepers.shopkeeper.registry;

import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObject;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShopObjectRegistry {
   private final Map<Object, AbstractShopkeeper> shopkeepersByObjectId = new HashMap();

   ShopObjectRegistry() {
   }

   public void onEnable() {
   }

   public void onDisable() {
      this.ensureEmpty();
   }

   private void ensureEmpty() {
      if (!this.shopkeepersByObjectId.isEmpty()) {
         Log.warning("Some spawned shop objects were not properly unregistered!");
         this.shopkeepersByObjectId.clear();
      }

   }

   public boolean isRegistered(AbstractShopkeeper shopkeeper) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      Object objectId = shopkeeper.getShopObject().getLastId();

      assert objectId == null || this.getShopkeeperByObjectId(objectId) == shopkeeper;

      return objectId != null;
   }

   @Nullable
   public AbstractShopkeeper getShopkeeperByObjectId(Object objectId) {
      return (AbstractShopkeeper)this.shopkeepersByObjectId.get(objectId);
   }

   public void updateShopObjectRegistration(AbstractShopkeeper shopkeeper) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      Validate.isTrue(!shopkeeper.isVirtual(), "shopkeeper is virtual");
      AbstractShopObject shopObject = shopkeeper.getShopObject();
      Object lastObjectId = shopObject.getLastId();
      Object currentObjectId = shopObject.getId();
      if (!Objects.equals(lastObjectId, currentObjectId)) {
         this.registerShopObject(shopkeeper);
      }
   }

   private void registerShopObject(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      AbstractShopObject shopObject = shopkeeper.getShopObject();
      this.unregisterShopObject(shopkeeper);

      assert shopObject.getLastId() == null;

      Object objectId = shopObject.getId();
      if (objectId != null) {
         Log.debug(DebugOptions.shopkeeperActivation, () -> {
            String var10000 = shopkeeper.getLogPrefix();
            return var10000 + "Registering object with id '" + String.valueOf(objectId) + "'.";
         });
         AbstractShopkeeper otherShopkeeper = (AbstractShopkeeper)this.shopkeepersByObjectId.putIfAbsent(objectId, shopkeeper);

         assert otherShopkeeper != shopkeeper;

         if (otherShopkeeper != null) {
            String var10000 = shopkeeper.getLogPrefix();
            Log.warning(var10000 + "Object registration failed! Object id '" + String.valueOf(objectId) + "' is already used by shopkeeper " + otherShopkeeper.getId() + ".");
         } else {
            shopObject.setLastId(objectId);
         }
      }
   }

   private void unregisterShopObject(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      AbstractShopObject shopObject = shopkeeper.getShopObject();
      Object objectId = shopObject.getLastId();
      if (objectId != null) {
         Log.debug(DebugOptions.shopkeeperActivation, () -> {
            String var10000 = shopkeeper.getLogPrefix();
            return var10000 + "Unregistering object with id '" + String.valueOf(objectId) + "'.";
         });

         assert this.shopkeepersByObjectId.get(objectId) == shopkeeper;

         this.shopkeepersByObjectId.remove(objectId);
         shopObject.setLastId((Object)null);
      }
   }
}
