package com.nisovin.shopkeepers.api.shopkeeper.player;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import java.util.UUID;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface PlayerShopkeeper extends Shopkeeper {
   void setOwner(Player var1);

   void setOwner(UUID var1, String var2);

   UUID getOwnerUUID();

   String getOwnerName();

   String getOwnerString();

   boolean isOwner(Player var1);

   @Nullable
   Player getOwner();

   boolean isNotifyOnTrades();

   void setNotifyOnTrades(boolean var1);

   boolean isForHire();

   void setForHire(@Nullable ItemStack var1);

   void setForHire(@Nullable UnmodifiableItemStack var1);

   @Nullable
   UnmodifiableItemStack getHireCost();

   int getContainerX();

   int getContainerY();

   int getContainerZ();

   void setContainer(int var1, int var2, int var3);

   @Nullable
   Block getContainer();

   int getCurrencyInContainer();

   boolean openHireWindow(Player var1);

   boolean openContainerWindow(Player var1);
}
