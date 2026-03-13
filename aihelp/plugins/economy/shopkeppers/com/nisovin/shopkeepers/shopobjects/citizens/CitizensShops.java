package com.nisovin.shopkeepers.shopobjects.citizens;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.dependencies.citizens.CitizensDependency;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.TimeUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;
import net.citizensnpcs.api.trait.trait.MobType;
import net.citizensnpcs.trait.LookClose;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public class CitizensShops {
   private final SKShopkeepersPlugin plugin;
   private final SKCitizensShopObjectType citizensShopObjectType = new SKCitizensShopObjectType((CitizensShops)Unsafe.initialized(this));
   private final PluginListener pluginListener = new PluginListener((CitizensShops)Unsafe.initialized(this));
   private final CitizensListener citizensListener;
   private boolean citizensShopsEnabled = false;
   @Nullable
   private TraitInfo shopkeeperTrait = null;
   private final Map<UUID, List<AbstractShopkeeper>> shopkeepersByNpcId = new HashMap();

   public CitizensShops(SKShopkeepersPlugin plugin) {
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
      this.citizensListener = new CitizensListener(plugin, (CitizensShops)Unsafe.initialized(this));
   }

   public void onEnable() {
      this.enable();
      Bukkit.getPluginManager().registerEvents(this.pluginListener, this.plugin);
   }

   public void onDisable() {
      this.disable();
      HandlerList.unregisterAll(this.pluginListener);
      this.shopkeepersByNpcId.clear();
   }

   public SKCitizensShopObjectType getCitizensShopObjectType() {
      return this.citizensShopObjectType;
   }

   public boolean isEnabled() {
      this.verifyCitizensAPIAvailable();
      return this.citizensShopsEnabled;
   }

   private void verifyCitizensAPIAvailable() {
      if (this.citizensShopsEnabled) {
         if (!this.isCitizensAPIAvailable()) {
            Log.debug("No valid Citizens API implementation available. Disabling the Citizen shops.");
            this.disable();
         }
      }
   }

   private boolean isCitizensAPIAvailable() {
      assert CitizensDependency.isPluginEnabled();

      return CitizensAPI.hasImplementation() && CitizensAPI.getNPCRegistry() != null;
   }

   void enable() {
      if (this.isEnabled()) {
         this.disable();
      }

      if (Settings.enableCitizenShops) {
         if (!CitizensDependency.isPluginEnabled()) {
            Log.debug("Citizen shops enabled, but Citizens plugin not found or disabled.");
         } else if (!this.isCitizensAPIAvailable()) {
            Log.debug("Citizen shops enabled, but Citizens API not available. Did the Citizens plugin enable correctly? Or did you try to reload the Citizens plugin?");
         } else {
            Log.info("Citizens found: Enabling NPC shopkeepers.");
            this.registerShopkeeperTrait();
            Bukkit.getPluginManager().registerEvents(this.citizensListener, this.plugin);
            this.citizensListener.onEnable();
            Bukkit.getScheduler().runTaskLater(this.plugin, new CitizensShops.DelayedSetupTask(), 3L);
            this.citizensShopsEnabled = true;
         }
      }
   }

   void disable() {
      if (this.isEnabled()) {
         this.shopkeepersByNpcId.values().stream().flatMap(Collection::stream).forEach((shopkeeper) -> {
            ((SKCitizensShopObject)shopkeeper.getShopObject()).onCitizensShopsDisabled();
         });
         Plugin citizensPlugin = CitizensDependency.getPlugin();
         if (citizensPlugin != null) {
            this.unregisterShopkeeperTrait();
         }

         this.citizensListener.onDisable();
         HandlerList.unregisterAll(this.citizensListener);
         this.citizensShopsEnabled = false;
      }
   }

   private void registerShopkeeperTrait() {
      assert this.shopkeeperTrait == null;

      TraitInfo shopkeeperTrait = TraitInfo.create(CitizensShopkeeperTrait.class).withName("shopkeeper");
      this.shopkeeperTrait = shopkeeperTrait;

      try {
         CitizensAPI.getTraitFactory().registerTrait(shopkeeperTrait);
      } catch (Throwable var3) {
         Log.debug("Shopkeeper trait registration failed!", var3);
      }

   }

   private void unregisterShopkeeperTrait() {
      TraitInfo shopkeeperTrait = this.shopkeeperTrait;
      if (shopkeeperTrait != null) {
         try {
            CitizensAPI.getTraitFactory().deregisterTrait(shopkeeperTrait);
         } catch (Throwable var6) {
            Log.debug("Shopkeeper trait deregistration failed!", var6);
         } finally {
            this.shopkeeperTrait = null;
         }
      }

   }

   void onCitizensReloaded() {
      if (!this.shopkeepersByNpcId.isEmpty()) {
         Log.debug("Citizens plugin has been reloaded.");
         this.shopkeepersByNpcId.values().stream().flatMap(Collection::stream).forEach((shopkeeper) -> {
            ((SKCitizensShopObject)shopkeeper.getShopObject()).onCitizensReloaded();
         });
      }
   }

   void registerCitizensShopkeeper(SKCitizensShopObject citizensShop, UUID npcId) {
      assert citizensShop != null && npcId != null;

      AbstractShopkeeper shopkeeper = citizensShop.getShopkeeper();
      List<AbstractShopkeeper> shopkeepers = (List)this.shopkeepersByNpcId.computeIfAbsent(npcId, (key) -> {
         return new ArrayList(1);
      });

      assert shopkeepers != null;

      shopkeepers.add(shopkeeper);
   }

   void unregisterCitizensShopkeeper(SKCitizensShopObject citizensShop, UUID npcId) {
      assert citizensShop != null && npcId != null;

      AbstractShopkeeper shopkeeper = citizensShop.getShopkeeper();
      this.shopkeepersByNpcId.computeIfPresent(npcId, (key, shopkeepers) -> {
         shopkeepers.remove(shopkeeper);
         return shopkeepers.isEmpty() ? (List)Unsafe.uncheckedNull() : shopkeepers;
      });
   }

   public boolean isShopkeeper(NPC npc) {
      return !this.getShopkeepers(npc).isEmpty();
   }

   @Nullable
   public AbstractShopkeeper getShopkeeper(NPC npc) {
      List<? extends AbstractShopkeeper> shopkeepers = this.getShopkeepers(npc);
      return shopkeepers.isEmpty() ? null : (AbstractShopkeeper)shopkeepers.get(0);
   }

   public List<? extends AbstractShopkeeper> getShopkeepers(NPC npc) {
      Validate.notNull(npc, (String)"npc is null");
      UUID npcId = npc.getUniqueId();

      assert npcId != null;

      return this.getShopkeepers(npcId);
   }

   private List<? extends AbstractShopkeeper> getShopkeepers(UUID npcId) {
      Validate.notNull(npcId, (String)"npcId is null");
      List<AbstractShopkeeper> shopkeepers = (List)this.shopkeepersByNpcId.get(npcId);
      return shopkeepers != null ? shopkeepers : Collections.emptyList();
   }

   public static String getNPCIdString(NPC npc) {
      int var10000 = npc.getId();
      return var10000 + " (" + String.valueOf(npc.getUniqueId()) + ")";
   }

   @Nullable
   public UUID getNPCUniqueId(Entity entity) {
      if (this.isEnabled()) {
         NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
         return npc != null ? npc.getUniqueId() : null;
      } else {
         return null;
      }
   }

   @Nullable
   public NPC createNPC(@Nullable Location location, EntityType entityType, String name) {
      if (!this.isEnabled()) {
         return null;
      } else {
         NPC npc = CitizensAPI.getNPCRegistry().createNPC(entityType, name);
         if (npc == null) {
            return null;
         } else {
            MobType mobType = (MobType)Unsafe.assertNonNull((MobType)npc.getOrAddTrait(MobType.class));
            mobType.setType(entityType);
            LookClose lookClose = (LookClose)Unsafe.assertNonNull((LookClose)npc.getOrAddTrait(LookClose.class));
            lookClose.lookClose(true);
            if (location != null) {
               npc.spawn(location);
            }

            return npc;
         }
      }
   }

   void onNPCEdited(NPC npc) {
      if (Settings.saveCitizenNpcsInstantly) {
         this.saveNPCs();
      }

   }

   public void saveNPCs() {
      if (this.isEnabled()) {
         long startNanos = System.nanoTime();
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "citizens save -a");
         double durationMillis = TimeUtils.convert((double)(System.nanoTime() - startNanos), TimeUnit.NANOSECONDS, TimeUnit.MILLISECONDS);
         Log.debug(() -> {
            return "Saved Citizens NPCs (" + TextUtils.format(durationMillis) + " ms).";
         });
      }
   }

   public int validateCitizenShopkeepers(boolean deleteInvalidShopkeepers, boolean silent) {
      if (!this.isEnabled()) {
         return 0;
      } else {
         SKShopkeeperRegistry shopkeeperRegistry = this.plugin.getShopkeeperRegistry();
         List<Shopkeeper> invalidShopkeepers = new ArrayList();
         shopkeeperRegistry.getAllShopkeepers().forEach((shopkeeperx) -> {
            if (shopkeeperx.getShopObject() instanceof SKCitizensShopObject) {
               SKCitizensShopObject citizensShop = (SKCitizensShopObject)shopkeeperx.getShopObject();
               UUID npcUniqueId = citizensShop.getNPCUniqueId();
               if (npcUniqueId == null) {
                  invalidShopkeepers.add(shopkeeperx);
                  if (!silent) {
                     Log.warning(shopkeeperx.getLogPrefix() + "There is no Citizens NPC associated.");
                  }

               } else {
                  String var10000;
                  if (CitizensAPI.getNPCRegistry().getByUniqueId(npcUniqueId) == null) {
                     invalidShopkeepers.add(shopkeeperx);
                     if (!silent) {
                        var10000 = shopkeeperx.getLogPrefix();
                        Log.warning(var10000 + "There is no Citizens NPC with unique id " + String.valueOf(npcUniqueId));
                     }

                  } else {
                     List<? extends AbstractShopkeeper> shopkeepers = this.getShopkeepers(npcUniqueId);
                     if (shopkeepers.size() > 1) {
                        Shopkeeper mainShopkeeper = (Shopkeeper)shopkeepers.get(0);
                        if (mainShopkeeper != shopkeeperx) {
                           citizensShop.setKeepNPCOnDeletion();
                           invalidShopkeepers.add(shopkeeperx);
                           if (!silent) {
                              var10000 = shopkeeperx.getLogPrefix();
                              Log.warning(var10000 + "Shopkeeper " + mainShopkeeper.getId() + " is already using the same Citizens NPC with unique id " + String.valueOf(npcUniqueId));
                           }

                           return;
                        }
                     }

                  }
               }
            }
         });
         if (!invalidShopkeepers.isEmpty()) {
            if (deleteInvalidShopkeepers) {
               Iterator var5 = invalidShopkeepers.iterator();

               while(var5.hasNext()) {
                  Shopkeeper shopkeeper = (Shopkeeper)var5.next();
                  shopkeeper.delete();
               }

               this.plugin.getShopkeeperStorage().save();
               if (!silent) {
                  Log.warning("Deleted " + invalidShopkeepers.size() + " invalid Citizen shopkeepers!");
               }
            } else if (!silent) {
               Log.warning("Found " + invalidShopkeepers.size() + " invalid Citizen shopkeepers! Either enable the setting 'delete-invalid-citizen-shopkeepers' inside the config, or use the command '/shopkeepers cleanupCitizenShopkeepers' to automatically delete these shopkeepers and get rid of these warnings.");
            }
         }

         return invalidShopkeepers.size();
      }
   }

   private class DelayedSetupTask implements Runnable {
      public void run() {
         if (CitizensShops.this.isEnabled()) {
            CitizensShops.this.validateCitizenShopkeepers(Settings.deleteInvalidCitizenShopkeepers, false);
            CitizensShops.this.shopkeepersByNpcId.values().stream().flatMap(Collection::stream).forEach((shopkeeper) -> {
               ((SKCitizensShopObject)shopkeeper.getShopObject()).onCitizensShopsEnabled();
            });
         }
      }
   }
}
