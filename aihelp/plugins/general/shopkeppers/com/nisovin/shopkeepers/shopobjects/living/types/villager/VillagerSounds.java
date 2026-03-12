package com.nisovin.shopkeepers.shopobjects.living.types.villager;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopobjects.living.LivingShopObject;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.api.ui.UISession;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.shopobjects.living.SKLivingShopObject;
import com.nisovin.shopkeepers.ui.trading.Trade;
import com.nisovin.shopkeepers.ui.trading.TradingContext;
import com.nisovin.shopkeepers.ui.trading.TradingListener;
import com.nisovin.shopkeepers.util.bukkit.LocationUtils;
import com.nisovin.shopkeepers.util.bukkit.Ticks;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.MathUtils;
import com.nisovin.shopkeepers.util.java.TimeUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;

public class VillagerSounds extends TradingListener {
   private static final int INPUT_SLOT_1 = 0;
   private static final int INPUT_SLOT_2 = 1;
   private static final int RESULT_SLOT = 2;
   private static final long AMBIENT_SOUND_DELAY_NANOS = Ticks.toNanos(80L);
   private static final long TRADE_INTERACTION_SOUND_DELAY_NANOS = Ticks.toNanos(20L);
   private static final int TRADING_PLAYER_MAX_DISTANCE = 8;
   private static final int TRADING_PLAYER_MAX_DISTANCE_SQ = 64;
   protected final Shopkeeper shopkeeper;
   private final LivingShopObject shopObject;
   private long lastSoundNanos = System.nanoTime();
   @Nullable
   private BukkitTask tradeInteractionTask = null;

   public VillagerSounds(SKLivingShopObject<? extends AbstractVillager> shopObject) {
      Validate.notNull(shopObject, (String)"shopObject is null");

      assert AbstractVillager.class.isAssignableFrom((Class)Unsafe.assertNonNull(shopObject.getEntityType().getEntityClass()));

      this.shopObject = shopObject;
      this.shopkeeper = shopObject.getShopkeeper();
      Validate.notNull(this.shopkeeper, (String)"shopObject is not associated with any shopkeeper yet");
   }

   protected Sound getAmbientSound() {
      return this.isShopkeeperTrading() ? Sound.ENTITY_VILLAGER_TRADE : Sound.ENTITY_VILLAGER_AMBIENT;
   }

   protected final boolean isShopkeeperTrading() {
      return !ShopkeepersAPI.getUIRegistry().getUISessions(this.shopkeeper, DefaultUITypes.TRADING()).isEmpty();
   }

   protected Sound getTradeSound() {
      return Sound.ENTITY_VILLAGER_YES;
   }

   protected Sound getTradeInteractionSound(@Nullable ItemStack resultItem) {
      return ItemUtils.isEmpty(resultItem) ? Sound.ENTITY_VILLAGER_NO : Sound.ENTITY_VILLAGER_YES;
   }

   @Nullable
   private AbstractVillager getVillager() {
      return (AbstractVillager)this.shopObject.getEntity();
   }

   private boolean isTradingPlayerCloseToVillager(Location playerLocation, Location villagerLocation) {
      assert playerLocation != null && villagerLocation != null;

      return LocationUtils.getDistanceSquared(playerLocation, villagerLocation) <= 64.0D;
   }

   private void throttleSounds() {
      this.lastSoundNanos = System.nanoTime();
   }

   private float getPitch(AbstractVillager villager) {
      return (villager.isAdult() ? 1.0F : 1.5F) + MathUtils.randomFloatInRange(-0.2F, 0.2F);
   }

   private void tryPlayVillagerTradingSound(Player tradingPlayer, String context, Sound sound, boolean playGlobal) {
      assert tradingPlayer != null && context != null && sound != null;

      AbstractVillager villager = this.getVillager();
      if (villager != null) {
         Location playerLocation = tradingPlayer.getLocation();
         Location villagerLocation = villager.getLocation();
         if (this.isTradingPlayerCloseToVillager(playerLocation, villagerLocation)) {
            Player receiver = null;
            if (!playGlobal || Settings.simulateTradingSoundsOnlyForTheTradingPlayer) {
               receiver = tradingPlayer;
            }

            this.playVillagerSound(villager, villagerLocation, context, sound, receiver);
         }
      }
   }

   private void playVillagerSound(AbstractVillager villager, Location villagerLocation, String context, Sound sound, @Nullable Player receivingPlayer) {
      assert villager != null && villagerLocation != null && context != null && sound != null;

      float pitch = this.getPitch(villager);
      if (receivingPlayer == null) {
         villager.getWorld().playSound(villagerLocation, sound, SoundCategory.NEUTRAL, 1.0F, pitch);
      } else {
         receivingPlayer.playSound(villagerLocation, sound, SoundCategory.NEUTRAL, 1.0F, pitch);
      }

      Log.debug(DebugOptions.regularTickActivities, () -> {
         return this.shopkeeper.getLogPrefix() + "Playing " + context + " sound: " + String.valueOf(sound);
      });
   }

   public void onInventoryClick(UISession uiSession, InventoryClickEvent event) {
      if (!event.isCancelled()) {
         int slot = event.getRawSlot();
         if (slot == 0 || slot == 1 || slot == 2) {
            assert event.getClickedInventory() instanceof MerchantInventory;

            this.onTradeInteraction(uiSession);
         }
      }
   }

   public void onTradeSelect(UISession uiSession, TradeSelectEvent event) {
      assert event.getView().getTopInventory() instanceof MerchantInventory;

      this.onTradeInteraction(uiSession);
   }

   private void onTradeInteraction(UISession uiSession) {
      if (this.tradeInteractionTask == null) {
         this.tradeInteractionTask = Bukkit.getScheduler().runTask(SKShopkeepersPlugin.getInstance(), new VillagerSounds.ProcessTradeInteractionTask(uiSession));
      }
   }

   private void onPostTradeInteraction(UISession uiSession) {
      assert uiSession.isValid();

      Player player = uiSession.getPlayer();
      MerchantInventory merchantInventory = (MerchantInventory)player.getOpenInventory().getTopInventory();
      ItemStack slot1 = merchantInventory.getItem(0);
      ItemStack slot2 = merchantInventory.getItem(1);
      if (!ItemUtils.isEmpty(slot1) || !ItemUtils.isEmpty(slot2)) {
         long nanosSinceLastSound = System.nanoTime() - this.lastSoundNanos;
         if (nanosSinceLastSound >= TRADE_INTERACTION_SOUND_DELAY_NANOS) {
            this.throttleSounds();
            ItemStack resultItem = merchantInventory.getItem(2);
            Sound sound = this.getTradeInteractionSound(resultItem);
            this.tryPlayVillagerTradingSound(player, "trade interaction", sound, true);
         }
      }
   }

   public void onTradeCompleted(Trade trade, boolean silent) {
      if (!silent) {
         this.handleTradeSound(trade);
      }

      this.throttleSounds();
   }

   private void handleTradeSound(Trade trade) {
      long nanosSinceLastSound = System.nanoTime() - this.lastSoundNanos;
      if (nanosSinceLastSound >= TRADE_INTERACTION_SOUND_DELAY_NANOS) {
         Sound sound = this.getTradeSound();
         Player player = trade.getTradingPlayer();
         this.tryPlayVillagerTradingSound(player, "trade", sound, true);
      }
   }

   public void onTradeAborted(TradingContext tradingContext, boolean silent) {
   }

   public void tick() {
      this.checkPlayAmbientSound();
   }

   private void checkPlayAmbientSound() {
      AbstractVillager villager = this.getVillager();
      if (villager != null) {
         long nanosSinceAmbientSoundThreshold = System.nanoTime() - (this.lastSoundNanos + AMBIENT_SOUND_DELAY_NANOS);
         if (nanosSinceAmbientSoundThreshold >= 0L) {
            double baseTimeSeconds = TimeUtils.convert((double)(nanosSinceAmbientSoundThreshold - TimeUtils.NANOS_PER_SECOND), TimeUnit.NANOSECONDS, TimeUnit.SECONDS);
            double skipProbability = 1.0D;

            for(int t = 0; t < 20; ++t) {
               double timeSeconds = baseTimeSeconds + (double)t * 0.05D;
               if (!(timeSeconds <= 0.0D)) {
                  skipProbability *= 1.0D - timeSeconds / 50.0D;
               }
            }

            if (!(ThreadLocalRandom.current().nextDouble() < skipProbability)) {
               Sound sound = this.getAmbientSound();
               this.playVillagerSound(villager, villager.getLocation(), "ambient", sound, (Player)null);
               this.throttleSounds();
            }
         }
      }
   }

   private class ProcessTradeInteractionTask implements Runnable {
      private final UISession uiSession;

      private ProcessTradeInteractionTask(UISession param2) {
         assert uiSession != null;

         this.uiSession = uiSession;
      }

      public void run() {
         VillagerSounds.this.tradeInteractionTask = null;
         if (this.uiSession.isValid()) {
            VillagerSounds.this.onPostTradeInteraction(this.uiSession);
         }
      }
   }
}
