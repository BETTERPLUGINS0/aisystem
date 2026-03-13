package com.nisovin.shopkeepers.api.ui;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface UISession {
   UIType getUIType();

   Player getPlayer();

   @Nullable
   Shopkeeper getShopkeeper();

   boolean isUIActive();

   void deactivateUI();

   void activateUI();

   boolean isValid();

   void close();

   void closeDelayed();

   void closeDelayedAndRunTask(@Nullable Runnable var1);

   void abort();

   void abortDelayed();

   void abortDelayedAndRunTask(@Nullable Runnable var1);
}
