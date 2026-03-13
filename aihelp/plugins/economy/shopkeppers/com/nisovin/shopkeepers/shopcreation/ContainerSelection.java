package com.nisovin.shopkeepers.shopcreation;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.container.ShopContainers;
import com.nisovin.shopkeepers.container.protection.ProtectedContainers;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.BlockLocation;
import com.nisovin.shopkeepers.util.bukkit.MutableBlockLocation;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.interaction.InteractionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ContainerSelection {
   private static final int MAX_TRACKED_CONTAINERS = 5;
   private static final MutableBlockLocation sharedBlockLocation = new MutableBlockLocation();
   private final ShopkeepersPlugin plugin;
   private final ProtectedContainers protectedContainers;
   private final RecentlyPlacedContainersListener containersListener = new RecentlyPlacedContainersListener((ContainerSelection)Unsafe.initialized(this));
   private final Map<UUID, Deque<BlockLocation>> recentlyPlacedContainers = new HashMap();
   private final Map<UUID, Block> selectedContainer = new HashMap();

   public ContainerSelection(ShopkeepersPlugin plugin, ProtectedContainers protectedContainers) {
      Validate.notNull(plugin, (String)"plugin is null");
      Validate.notNull(protectedContainers, (String)"protectedContainers is null");
      this.plugin = plugin;
      this.protectedContainers = protectedContainers;
   }

   public void onEnable() {
      Bukkit.getPluginManager().registerEvents(this.containersListener, this.plugin);
   }

   public void onDisable() {
      HandlerList.unregisterAll(this.containersListener);
      this.selectedContainer.clear();
   }

   public void onPlayerQuit(Player player) {
      assert player != null;

      UUID playerId = player.getUniqueId();
      this.selectedContainer.remove(playerId);
      this.recentlyPlacedContainers.remove(playerId);
   }

   private BlockLocation getSharedKey(Block block) {
      sharedBlockLocation.set(block);
      return sharedBlockLocation;
   }

   public void addRecentlyPlacedContainer(Player player, Block container) {
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(container, (String)"container is null");
      UUID playerId = player.getUniqueId();
      Deque<BlockLocation> recentlyPlaced = (Deque)this.recentlyPlacedContainers.computeIfAbsent(playerId, (key) -> {
         return new ArrayDeque(6);
      });

      assert recentlyPlaced != null;

      if (recentlyPlaced.size() == 5) {
         recentlyPlaced.removeFirst();
      }

      recentlyPlaced.addLast(BlockLocation.of(container));
   }

   public boolean isRecentlyPlacedContainer(Player player, Block container) {
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(container, (String)"container is null");
      UUID playerId = player.getUniqueId();
      Deque<BlockLocation> recentlyPlaced = (Deque)this.recentlyPlacedContainers.get(playerId);
      if (recentlyPlaced == null) {
         return false;
      } else {
         BlockLocation containerLocation = this.getSharedKey(container);
         return recentlyPlaced.contains(containerLocation);
      }
   }

   public void selectContainer(Player player, @Nullable Block container) {
      Validate.notNull(player, (String)"player is null");
      UUID playerId = player.getUniqueId();
      if (container == null) {
         this.selectedContainer.remove(playerId);
      } else {
         assert ShopContainers.isSupportedContainer(container.getType());

         this.selectedContainer.put(playerId, container);
      }

   }

   @Nullable
   public Block getSelectedContainer(Player player) {
      Validate.notNull(player, (String)"player is null");
      UUID playerId = player.getUniqueId();
      return (Block)this.selectedContainer.get(playerId);
   }

   public boolean validateContainer(Player player, Block containerBlock) {
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(containerBlock, (String)"containerBlock is null");
      if (this.protectedContainers.isContainerProtected(containerBlock, (Player)null)) {
         TextUtils.sendMessage(player, (Text)Messages.containerAlreadyInUse);
         return false;
      } else if (Settings.requireContainerRecentlyPlaced && !this.isRecentlyPlacedContainer(player, containerBlock)) {
         TextUtils.sendMessage(player, (Text)Messages.containerNotPlaced);
         return false;
      } else if (!InteractionUtils.checkBlockInteract(player, containerBlock, false)) {
         TextUtils.sendMessage(player, (Text)Messages.noContainerAccess);
         return false;
      } else {
         return true;
      }
   }
}
