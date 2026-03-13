package com.nisovin.shopkeepers.tradelog.base;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.tradelog.TradeLogStorageType;
import com.nisovin.shopkeepers.tradelog.TradeLogUtils;
import com.nisovin.shopkeepers.tradelog.TradeLogger;
import com.nisovin.shopkeepers.tradelog.data.TradeRecord;
import com.nisovin.shopkeepers.util.bukkit.PermissionUtils;
import com.nisovin.shopkeepers.util.bukkit.SchedulerUtils;
import com.nisovin.shopkeepers.util.bukkit.SingletonTask;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.Retry;
import com.nisovin.shopkeepers.util.java.ThrowableUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractSingleWriterTradeLogger implements TradeLogger {
   private static final int DELAYED_SAVE_TICKS = 200;
   private static final int SAVE_MAX_ATTEMPTS = 20;
   private static final long SAVE_RETRY_DELAY_MILLIS = 25L;
   private static final long SAVE_ERROR_MSG_THROTTLE_MILLIS;
   protected final Plugin plugin;
   protected final TradeLogStorageType storageType;
   protected final String logPrefix;
   private final AbstractSingleWriterTradeLogger.SetupTask setupTask;
   private boolean setupCompleted = false;
   private boolean enabled = true;
   private List<TradeRecord> pending = new ArrayList();
   private final AbstractSingleWriterTradeLogger.SaveTask saveTask;
   @Nullable
   private BukkitTask delayedSaveTask = null;
   private boolean logItemMetadata;

   public AbstractSingleWriterTradeLogger(Plugin plugin, TradeLogStorageType storageType) {
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
      this.storageType = storageType;
      this.logPrefix = storageType.toString() + " trade log: ";
      this.setupTask = new AbstractSingleWriterTradeLogger.SetupTask(plugin);
      this.saveTask = new AbstractSingleWriterTradeLogger.SaveTask(plugin);
   }

   public final void setup() {
      if (!this.setupCompleted) {
         if (!this.setupTask.isRunning()) {
            this.setupTask.run();
         }
      }
   }

   protected void preSetup() {
   }

   protected void asyncSetup() {
   }

   protected void postSetup() {
   }

   protected final void disable(String reason) {
      if (!SchedulerUtils.isMainThread()) {
         throw new IllegalStateException("This must be called from the server's main thread!");
      } else {
         Log.severe(this.logPrefix + "Disabled (trades won't be logged)! Reason: " + reason);
         this.enabled = false;
         this.cancelDelayedSave();
         this.pending.clear();
      }
   }

   public void logTrade(TradeRecord trade) {
      if (this.enabled) {
         this.pending.add(trade);
         this.savePendingDelayed();
      }
   }

   public void flush() {
      this.setupTask.awaitExecutions();
      this.savePending();
      this.saveTask.awaitExecutions();
   }

   private boolean hasPending() {
      return !this.pending.isEmpty();
   }

   private void savePendingDelayed() {
      if (this.setupCompleted) {
         if (this.hasPending()) {
            if (this.delayedSaveTask == null) {
               this.delayedSaveTask = SchedulerUtils.runTaskLaterOrOmit(this.plugin, new AbstractSingleWriterTradeLogger.DelayedSaveTask(), 200L);
            }
         }
      }
   }

   private void cancelDelayedSave() {
      if (this.delayedSaveTask != null) {
         this.delayedSaveTask.cancel();
         this.delayedSaveTask = null;
      }

   }

   private void savePending() {
      assert this.setupCompleted;

      if (this.hasPending()) {
         this.saveTask.run();
      }
   }

   protected String getItemMetadata(UnmodifiableItemStack itemStack) {
      assert itemStack != null;

      return !this.logItemMetadata ? "" : TradeLogUtils.getItemMetadata(itemStack);
   }

   private boolean writeTradesWithRetry(AbstractSingleWriterTradeLogger.SaveContext saveContext) {
      try {
         Retry.retry(() -> {
            this.writeTrades(saveContext);
         }, 20, (attemptNumber, exception, retry) -> {
            assert exception != null;

            String errorMsg = this.logPrefix + "Failed to log trades (attempt " + attemptNumber + ")";
            if (attemptNumber == 1) {
               Log.severe((String)errorMsg, (Throwable)exception);
            } else {
               String issue = ThrowableUtils.getDescription(exception);
               Log.severe(errorMsg + ": " + issue);
            }

            if (retry) {
               try {
                  Thread.sleep(25L);
               } catch (InterruptedException var6) {
                  Thread.currentThread().interrupt();
               }
            }

         });
         return true;
      } catch (Exception var3) {
         Log.severe((String)(this.logPrefix + "Failed to log trades! Data might have been lost! :("), (Throwable)var3);
         return false;
      }
   }

   protected abstract void writeTrades(AbstractSingleWriterTradeLogger.SaveContext var1) throws Exception;

   static {
      SAVE_ERROR_MSG_THROTTLE_MILLIS = TimeUnit.MINUTES.toMillis(5L);
   }

   private class SetupTask extends SingletonTask {
      private SetupTask(Plugin param2) {
         super(plugin);
      }

      protected AbstractSingleWriterTradeLogger.SetupTask.InternalAsyncTask createInternalAsyncTask() {
         return new AbstractSingleWriterTradeLogger.SetupTask.InternalAsyncTask(this);
      }

      protected AbstractSingleWriterTradeLogger.SetupTask.InternalSyncCallbackTask createInternalSyncCallbackTask() {
         return new AbstractSingleWriterTradeLogger.SetupTask.InternalSyncCallbackTask(this);
      }

      protected void prepare() {
         AbstractSingleWriterTradeLogger.this.preSetup();
      }

      protected void execute() {
         AbstractSingleWriterTradeLogger.this.asyncSetup();
      }

      protected void syncCallback() {
         AbstractSingleWriterTradeLogger.this.postSetup();
         AbstractSingleWriterTradeLogger.this.setupCompleted = true;
         AbstractSingleWriterTradeLogger.this.savePending();
      }

      private class InternalAsyncTask extends SingletonTask.InternalAsyncTask {
         private InternalAsyncTask(final AbstractSingleWriterTradeLogger.SetupTask param1) {
            super();
         }
      }

      private class InternalSyncCallbackTask extends SingletonTask.InternalSyncCallbackTask {
         private InternalSyncCallbackTask(final AbstractSingleWriterTradeLogger.SetupTask param1) {
            super();
         }
      }
   }

   private class SaveTask extends SingletonTask {
      private List<TradeRecord> saving = new ArrayList();
      @Nullable
      private AbstractSingleWriterTradeLogger.SaveContext saveContext = null;
      private boolean saveSucceeded = false;
      private long lastSaveErrorMsgMillis = 0L;

      private SaveTask(Plugin param2) {
         super(plugin);
      }

      protected AbstractSingleWriterTradeLogger.SaveTask.InternalAsyncTask createInternalAsyncTask() {
         return new AbstractSingleWriterTradeLogger.SaveTask.InternalAsyncTask(this);
      }

      protected AbstractSingleWriterTradeLogger.SaveTask.InternalSyncCallbackTask createInternalSyncCallbackTask() {
         return new AbstractSingleWriterTradeLogger.SaveTask.InternalSyncCallbackTask(this);
      }

      protected void prepare() {
         AbstractSingleWriterTradeLogger.this.cancelDelayedSave();
         AbstractSingleWriterTradeLogger.this.logItemMetadata = Settings.logItemMetadata;

         assert this.saving.isEmpty();

         List<TradeRecord> temp = this.saving;
         this.saving = AbstractSingleWriterTradeLogger.this.pending;
         AbstractSingleWriterTradeLogger.this.pending = temp;

         assert this.saveContext == null;

         this.saveContext = new AbstractSingleWriterTradeLogger.SaveContext(this.saving);
      }

      protected void execute() {
         AbstractSingleWriterTradeLogger.SaveContext saveContext = (AbstractSingleWriterTradeLogger.SaveContext)Unsafe.assertNonNull(this.saveContext);
         this.saveSucceeded = AbstractSingleWriterTradeLogger.this.writeTradesWithRetry(saveContext);
         if (!$assertionsDisabled) {
            if (this.saveSucceeded) {
               if (saveContext.hasUnsavedTrades()) {
                  throw new AssertionError();
               }
            } else if (!saveContext.hasUnsavedTrades()) {
               throw new AssertionError();
            }
         }

      }

      protected void syncCallback() {
         AbstractSingleWriterTradeLogger.SaveContext saveContext = (AbstractSingleWriterTradeLogger.SaveContext)Unsafe.assertNonNull(this.saveContext);
         this.printDebugInfo();
         if (!this.saveSucceeded) {
            AbstractSingleWriterTradeLogger.this.pending.addAll(0, saveContext.getUnsavedTrades());
            AbstractSingleWriterTradeLogger.this.savePendingDelayed();
            long nowMillis = System.currentTimeMillis();
            if (Math.abs(nowMillis - this.lastSaveErrorMsgMillis) > AbstractSingleWriterTradeLogger.SAVE_ERROR_MSG_THROTTLE_MILLIS) {
               this.lastSaveErrorMsgMillis = nowMillis;
               String var10000 = String.valueOf(ChatColor.DARK_RED);
               String errorMsg = var10000 + "[Shopkeepers] " + String.valueOf(ChatColor.RED) + AbstractSingleWriterTradeLogger.this.logPrefix + "Failed to log trades! Please check the server logs and look into the issue!";
               Iterator var5 = Bukkit.getOnlinePlayers().iterator();

               while(var5.hasNext()) {
                  Player player = (Player)var5.next();

                  assert player != null;

                  if (PermissionUtils.hasPermission(player, "shopkeeper.admin")) {
                     player.sendMessage(errorMsg);
                  }
               }
            }
         }

         this.saveContext = null;
         this.saving.clear();
      }

      private void printDebugInfo() {
         Log.debug(() -> {
            AbstractSingleWriterTradeLogger.SaveContext saveContext = (AbstractSingleWriterTradeLogger.SaveContext)Unsafe.assertNonNull(this.saveContext);
            StringBuilder sb = new StringBuilder();
            sb.append("Logged trades to the ");
            sb.append(AbstractSingleWriterTradeLogger.this.storageType);
            sb.append(" trade log (");
            sb.append(this.saving.size()).append(" records");
            if (saveContext.hasUnsavedTrades()) {
               sb.append(", ").append(saveContext.getUnsavedTrades().size()).append(" failed to log");
            }

            sb.append("): ");
            sb.append(this.getExecutionTimingString());
            if (!this.saveSucceeded) {
               if (saveContext.getUnsavedTrades().size() == this.saving.size()) {
                  sb.append(" -- Logging failed!");
               } else {
                  sb.append(" -- Logging partially failed!");
               }
            }

            return sb.toString();
         });
      }

      private class InternalAsyncTask extends SingletonTask.InternalAsyncTask {
         private InternalAsyncTask(final AbstractSingleWriterTradeLogger.SaveTask param1) {
            super();
         }
      }

      private class InternalSyncCallbackTask extends SingletonTask.InternalSyncCallbackTask {
         private InternalSyncCallbackTask(final AbstractSingleWriterTradeLogger.SaveTask param1) {
            super();
         }
      }
   }

   private class DelayedSaveTask implements Runnable {
      public void run() {
         AbstractSingleWriterTradeLogger.this.delayedSaveTask = null;
         AbstractSingleWriterTradeLogger.this.savePending();
      }
   }

   protected static class SaveContext {
      private final List<? extends TradeRecord> trades;
      private int nextUnsaved = 0;

      private SaveContext(List<? extends TradeRecord> trades) {
         assert trades != null && !CollectionUtils.containsNull(trades);

         this.trades = trades;
      }

      public boolean hasUnsavedTrades() {
         return this.nextUnsaved < this.trades.size();
      }

      @Nullable
      public TradeRecord getNextUnsavedTrade() {
         return !this.hasUnsavedTrades() ? null : (TradeRecord)this.trades.get(this.nextUnsaved);
      }

      private List<? extends TradeRecord> getUnsavedTrades() {
         return !this.hasUnsavedTrades() ? Collections.emptyList() : this.trades.subList(this.nextUnsaved, this.trades.size());
      }

      public void onTradeSuccessfullySaved() {
         ++this.nextUnsaved;
      }
   }
}
