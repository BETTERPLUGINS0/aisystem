package com.nisovin.shopkeepers.shopobjects.citizens;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopkeeper.DefaultShopTypes;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperCreateException;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopType;
import com.nisovin.shopkeepers.api.shopobjects.DefaultShopObjectTypes;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class CitizensShopkeeperTrait extends Trait {
   public static final String TRAIT_NAME = "shopkeeper";

   public CitizensShopkeeperTrait() {
      super("shopkeeper");
   }

   public void load(@Nullable DataKey key) {
      assert key != null;

   }

   public void save(@Nullable DataKey key) {
      assert key != null;

   }

   @Nullable
   public AbstractShopkeeper getShopkeeper() {
      NPC npc = this.getNPC();
      if (npc == null) {
         return null;
      } else if (!SKShopkeepersPlugin.isPluginEnabled()) {
         return null;
      } else {
         SKShopkeepersPlugin shopkeepersPlugin = SKShopkeepersPlugin.getInstance();
         CitizensShops citizensShops = shopkeepersPlugin.getCitizensShops();
         return citizensShops.getShopkeeper(npc);
      }
   }

   public void onRemove() {
   }

   void onShopkeeperDeletion(Shopkeeper shopkeeper) {
      NPC npc = this.getNPC();

      assert npc != null;

      Log.debug(() -> {
         String var10000 = shopkeeper.getUniqueIdLogPrefix();
         return var10000 + "Removing the 'shopkeeper' trait from Citizens NPC " + CitizensShops.getNPCIdString(npc) + " due to shopkeeper deletion.";
      });
      npc.removeTrait(CitizensShopkeeperTrait.class);
      SKShopkeepersPlugin.getInstance().getCitizensShops().onNPCEdited(npc);
   }

   public void onTraitDeleted(@Nullable Player player) {
      Shopkeeper shopkeeper = this.getShopkeeper();
      if (shopkeeper != null && shopkeeper.isValid()) {
         NPC npc = this.getNPC();

         assert npc != null;

         Log.debug(() -> {
            String var10000 = shopkeeper.getUniqueIdLogPrefix();
            return var10000 + "Deletion due to the removal of the 'shopkeeper' trait from Citizens NPC " + CitizensShops.getNPCIdString(npc);
         });

         assert shopkeeper.getShopObject().getType() == DefaultShopObjectTypes.CITIZEN();

         SKCitizensShopObject shopObject = (SKCitizensShopObject)shopkeeper.getShopObject();
         shopObject.setKeepNPCOnDeletion();
         shopkeeper.delete(player);
         shopkeeper.save();
      }

   }

   void onTraitAdded(@Nullable Player player) {
      this.createShopkeeperIfMissing(player);
   }

   public void onAttach() {
      if (SKShopkeepersPlugin.isPluginEnabled()) {
         Bukkit.getScheduler().runTaskLater(SKShopkeepersPlugin.getInstance(), () -> {
            this.createShopkeeperIfMissing((Player)null);
         }, 5L);
      }
   }

   private void createShopkeeperIfMissing(@Nullable Player creator) {
      NPC npc = this.getNPC();
      if (npc != null && npc.hasTrait(CitizensShopkeeperTrait.class)) {
         if (this.getShopkeeper() == null) {
            if (SKShopkeepersPlugin.isPluginEnabled()) {
               ShopkeepersPlugin plugin = ShopkeepersPlugin.getInstance();
               Log.debug(() -> {
                  String var10000 = CitizensShops.getNPCIdString(npc);
                  return "Creating shopkeeper for Citizens NPC " + var10000 + (creator != null ? " and player '" + creator.getName() + "'" : "");
               });
               Entity entity = npc.getEntity();
               Location location;
               if (entity != null) {
                  location = entity.getLocation();
               } else {
                  location = npc.getStoredLocation();
               }

               String shopkeeperCreationError = null;
               if (location != null) {
                  ShopCreationData creationData = AdminShopCreationData.create(creator, (AdminShopType)DefaultShopTypes.ADMIN_REGULAR(), DefaultShopObjectTypes.CITIZEN(), location, (BlockFace)null);
                  creationData.setValue("CitizensNpcUUID", npc.getUniqueId());
                  Shopkeeper shopkeeper = null;
                  if (creator != null) {
                     shopkeeper = plugin.handleShopkeeperCreation(creationData);
                  } else {
                     ShopkeeperRegistry shopkeeperRegistry = plugin.getShopkeeperRegistry();

                     try {
                        shopkeeper = shopkeeperRegistry.createShopkeeper(creationData);

                        assert shopkeeper != null;

                        shopkeeper.save();
                     } catch (ShopkeeperCreateException var11) {
                        Log.warning((String)("Failed to create the shopkeeper for Citizens NPC " + CitizensShops.getNPCIdString(npc) + "!"), (Throwable)var11);
                     }
                  }

                  if (shopkeeper == null) {
                     shopkeeperCreationError = "Shopkeeper creation via the Citizens trait failed. Removing the trait again.";
                  }
               } else {
                  shopkeeperCreationError = "Shopkeeper creation via the Citizens trait failed due to missing NPC location. Removing the trait again.";
               }

               if (shopkeeperCreationError != null) {
                  Log.warning(shopkeeperCreationError);
                  if (creator != null) {
                     String var10001 = String.valueOf(ChatColor.RED);
                     TextUtils.sendMessage(creator, (String)(var10001 + shopkeeperCreationError));
                  }

                  Bukkit.getScheduler().runTask(plugin, () -> {
                     npc.removeTrait(CitizensShopkeeperTrait.class);
                  });
               }

            }
         }
      }
   }
}
