package com.nisovin.shopkeepers.util.trading;

import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.bukkit.Ticks;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TradeMerger {
   private static final long DEFAULT_MERGE_DURATION_TICKS = 300L;
   private static final long DEFAULT_NEXT_MERGE_TIMEOUT_TICKS = 100L;
   private static final long NEXT_MERGE_TIMEOUT_THRESHOLD_NANOS;
   private final Plugin plugin;
   private final Consumer<MergedTrades> mergedTradesConsumer;
   private final TradeMerger.MergeMode mergeMode;
   private long mergeDurationTicks;
   private long mergeDurationNanos;
   private long nextMergeTimeoutTicks;
   private long nextMergeTimeoutNanos;
   @Nullable
   private MergedTrades previousTrades = null;
   private long mergeEndNanos;
   private long lastMergedTradeNanos;
   @Nullable
   private BukkitTask mergeDurationTask = null;
   @Nullable
   private BukkitTask nextMergeTimeoutTask = null;
   private long nextMergeTimeoutStartNanos;

   public TradeMerger(Plugin plugin, TradeMerger.MergeMode mergeMode, Consumer<MergedTrades> mergedTradesConsumer) {
      Validate.notNull(plugin, (String)"plugin is null");
      Validate.notNull(mergeMode, (String)"mergeMode is null");
      Validate.notNull(mergedTradesConsumer, (String)"mergedTradesConsumer is null");
      this.plugin = plugin;
      this.mergedTradesConsumer = mergedTradesConsumer;
      this.mergeMode = mergeMode;
      if (mergeMode == TradeMerger.MergeMode.SAME_CLICK_EVENT) {
         this.setMergeDurations(1L, 1L);
      } else {
         this.setMergeDurations(300L, 100L);
      }

   }

   public TradeMerger withMergeDurations(long mergeDurationTicks, long nextMergeTimeoutTicks) {
      Validate.State.isTrue(this.previousTrades == null, "This TradeMerger cannot be reconfigured while it is already merging trades.");
      Validate.State.isTrue(this.mergeMode == TradeMerger.MergeMode.DURATION, "Calling this method is only valid when using MergeMode DURATION.");
      this.setMergeDurations(mergeDurationTicks, nextMergeTimeoutTicks);
      return this;
   }

   private void setMergeDurations(long mergeDurationTicks, long nextMergeTimeoutTicks) {
      Validate.isTrue(mergeDurationTicks >= 0L, "mergeDurationTicks cannot be negative");
      Validate.isTrue(nextMergeTimeoutTicks >= 0L, "nextMergeTimeoutTicks cannot be negative");
      this.mergeDurationTicks = mergeDurationTicks;
      this.mergeDurationNanos = Ticks.toNanos(mergeDurationTicks);
      this.nextMergeTimeoutTicks = nextMergeTimeoutTicks;
      this.nextMergeTimeoutNanos = Ticks.toNanos(nextMergeTimeoutTicks);
   }

   public void onEnable() {
   }

   public void onDisable() {
      this.processPreviousTrades();
   }

   public void mergeTrade(ShopkeeperTradeEvent tradeEvent) {
      Validate.notNull(tradeEvent, (String)"tradeEvent is null");
      long nowNanos = System.nanoTime();
      MergedTrades previousTrades = this.previousTrades;
      if (previousTrades == null) {
         this.previousTrades = new MergedTrades(tradeEvent);
         this.mergeEndNanos = nowNanos + this.mergeDurationNanos;
         this.lastMergedTradeNanos = nowNanos;
         this.startDelayedTasks();
      } else if (previousTrades.canMerge(tradeEvent, this.mergeMode == TradeMerger.MergeMode.SAME_CLICK_EVENT)) {
         previousTrades.addTrades(1);
         this.lastMergedTradeNanos = nowNanos;
      } else {
         this.processPreviousTrades();

         assert this.previousTrades == null;

         this.previousTrades = new MergedTrades(tradeEvent);
         this.mergeEndNanos = nowNanos + this.mergeDurationNanos;
         this.lastMergedTradeNanos = nowNanos;
         this.startDelayedTasks();
      }

   }

   private void endDelayedTasks() {
      this.endMergeDurationTask();
      this.endNextMergeTimeoutTask();
   }

   private void endMergeDurationTask() {
      if (this.mergeDurationTask != null) {
         this.mergeDurationTask.cancel();
         this.mergeDurationTask = null;
      }

   }

   private void endNextMergeTimeoutTask() {
      if (this.nextMergeTimeoutTask != null) {
         this.nextMergeTimeoutTask.cancel();
         this.nextMergeTimeoutTask = null;
      }

   }

   private void startDelayedTasks() {
      this.endDelayedTasks();
      if (this.mergeDurationTicks == 0L) {
         this.processPreviousTrades();
      } else {
         this.startMergeDurationTask();
         this.startNextMergeTimeoutTask();
      }
   }

   private void startMergeDurationTask() {
      this.endMergeDurationTask();
      this.mergeDurationTask = Bukkit.getScheduler().runTaskLater(this.plugin, new TradeMerger.MaxMergeDurationTimeoutTask(), this.mergeDurationTicks);
   }

   private void startNextMergeTimeoutTask() {
      if (this.nextMergeTimeoutTicks != 0L && this.nextMergeTimeoutTicks < this.mergeDurationTicks) {
         assert this.mergeMode == TradeMerger.MergeMode.DURATION;

         this.endNextMergeTimeoutTask();
         long nowNanos = System.nanoTime();
         long nanosSinceLastMergedTrade = nowNanos - this.lastMergedTradeNanos;

         assert nanosSinceLastMergedTrade >= 0L;

         long remainingTimeoutNanos = this.nextMergeTimeoutNanos - nanosSinceLastMergedTrade;
         if (remainingTimeoutNanos <= NEXT_MERGE_TIMEOUT_THRESHOLD_NANOS) {
            this.processPreviousTrades();
         } else if (this.mergeEndNanos > nowNanos + remainingTimeoutNanos + NEXT_MERGE_TIMEOUT_THRESHOLD_NANOS) {
            long taskDelayTicks = Ticks.fromNanos(remainingTimeoutNanos);

            assert taskDelayTicks >= 1L;

            this.nextMergeTimeoutStartNanos = this.lastMergedTradeNanos;
            this.nextMergeTimeoutTask = Bukkit.getScheduler().runTaskLater(this.plugin, new TradeMerger.NextMergeTimeoutTask(), taskDelayTicks);
         }
      }
   }

   public void processPreviousTrades() {
      if (this.previousTrades != null) {
         this.endDelayedTasks();
         this.mergedTradesConsumer.accept((MergedTrades)Unsafe.assertNonNull(this.previousTrades));
         this.previousTrades = null;
      }
   }

   static {
      NEXT_MERGE_TIMEOUT_THRESHOLD_NANOS = TimeUnit.MILLISECONDS.toNanos(500L);
   }

   public static enum MergeMode {
      SAME_CLICK_EVENT,
      DURATION;

      // $FF: synthetic method
      private static TradeMerger.MergeMode[] $values() {
         return new TradeMerger.MergeMode[]{SAME_CLICK_EVENT, DURATION};
      }
   }

   private class MaxMergeDurationTimeoutTask implements Runnable {
      public void run() {
         assert TradeMerger.this.previousTrades != null;

         TradeMerger.this.mergeDurationTask = null;
         TradeMerger.this.processPreviousTrades();
      }
   }

   private class NextMergeTimeoutTask implements Runnable {
      public void run() {
         assert TradeMerger.this.previousTrades != null;

         TradeMerger.this.nextMergeTimeoutTask = null;
         if (TradeMerger.this.lastMergedTradeNanos != TradeMerger.this.nextMergeTimeoutStartNanos) {
            TradeMerger.this.startNextMergeTimeoutTask();
         } else {
            TradeMerger.this.processPreviousTrades();
         }

      }
   }
}
