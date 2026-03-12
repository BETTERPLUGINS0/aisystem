package ac.grim.grimac.api;

import ac.grim.grimac.api.common.BasicReloadable;
import ac.grim.grimac.api.config.ConfigReloadable;
import ac.grim.grimac.api.feature.FeatureManager;
import ac.grim.grimac.api.handler.UserHandlerHolder;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.UUID;

public interface GrimUser extends ConfigReloadable, BasicReloadable, UserHandlerHolder, GrimIdentity {
   String getName();

   String getBrand();

   @Nullable
   String getWorldName();

   @Nullable
   UUID getWorldUID();

   PacketWorld getPacketWorld();

   int getTransactionPing();

   int getKeepAlivePing();

   String getVersionName();

   double getHorizontalSensitivity();

   double getVerticalSensitivity();

   boolean isVanillaMath();

   void updatePermissions();

   Collection<? extends AbstractCheck> getChecks();

   void runSafely(Runnable var1);

   int getLastTransactionReceived();

   int getLastTransactionSent();

   void addRealTimeTask(int var1, Runnable var2);

   default void addRealTimeTaskNow(Runnable runnable) {
      this.addRealTimeTask(this.getLastTransactionSent(), runnable);
   }

   default void addRealTimeTaskNext(Runnable runnable) {
      this.addRealTimeTask(this.getLastTransactionSent() + 1, runnable);
   }

   FeatureManager getFeatureManager();

   void sendMessage(String var1);

   boolean hasPermission(String var1);
}
