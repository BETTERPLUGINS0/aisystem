package com.bergerkiller.bukkit.tc.attachments.ui.models.listing;

import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.attachments.ui.models.ResourcePackModelListing;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public final class DialogBuilder implements Cloneable {
   private static final ItemStack DEFAULT_NAMESPACE_ITEM = createDefaultNamespaceItem();
   private static final ItemStack DEFAULT_DIRECTORY_ITEM = createDefaultDirectoryItem();
   private final Plugin plugin;
   private final Player player;
   private final ResourcePackModelListing listing;
   private boolean creativeMenu = false;
   private boolean compactingEnabled;
   private String title;
   private String query;
   private String browsedLocation;
   private int browsedPage;
   private boolean cancelOnRootRightClick;
   private ItemStack namespaceItem;
   private ItemStack directoryItem;

   public DialogBuilder(Plugin plugin, Player player, ResourcePackModelListing listing) {
      this.compactingEnabled = TCConfig.modelSearchCompactFolders;
      this.title = "Resource Pack Models";
      this.query = "";
      this.browsedLocation = "";
      this.browsedPage = 0;
      this.cancelOnRootRightClick = false;
      this.namespaceItem = DEFAULT_NAMESPACE_ITEM;
      this.directoryItem = DEFAULT_DIRECTORY_ITEM;
      this.plugin = plugin;
      this.player = player;
      this.listing = listing;
   }

   public Plugin plugin() {
      return this.plugin;
   }

   public Player player() {
      return this.player;
   }

   public ResourcePackModelListing listing() {
      return this.listing;
   }

   public DialogBuilder navigate(String path, int page) {
      this.browsedLocation = path;
      this.browsedPage = page;
      return this;
   }

   public String getBrowsedPath() {
      return this.browsedLocation;
   }

   public int getBrowsedPage() {
      return this.browsedPage;
   }

   public DialogBuilder asCreativeMenu() {
      this.creativeMenu = true;
      return this;
   }

   public boolean isCreativeMenu() {
      return this.creativeMenu;
   }

   public DialogBuilder setCompactingEnabled(boolean compact) {
      this.compactingEnabled = compact;
      return this;
   }

   public boolean isCompactingEnabled() {
      return this.compactingEnabled;
   }

   public DialogBuilder title(String title) {
      this.title = title;
      return this;
   }

   public String getTitle() {
      return this.title;
   }

   public DialogBuilder namespaceIconItem(ItemStack item) {
      if (item == null) {
         throw new IllegalArgumentException("Item may not be null");
      } else {
         this.namespaceItem = item;
         return this;
      }
   }

   public ItemStack getNamespaceIconItem() {
      return this.namespaceItem;
   }

   public DialogBuilder directoryIconItem(ItemStack item) {
      if (item == null) {
         throw new IllegalArgumentException("Item may not be null");
      } else {
         this.directoryItem = item;
         return this;
      }
   }

   public ItemStack getDirectoryIconItem() {
      return this.directoryItem;
   }

   public DialogBuilder cancelOnRootRightClick() {
      return this.cancelOnRootRightClick(true);
   }

   public DialogBuilder cancelOnRootRightClick(boolean cancel) {
      this.cancelOnRootRightClick = cancel;
      return this;
   }

   public boolean isCancelOnRootRightClick() {
      return this.cancelOnRootRightClick;
   }

   public DialogBuilder query(String query) {
      this.query = query;
      return this;
   }

   public String getQuery() {
      return this.query;
   }

   public CompletableFuture<DialogResult> show() {
      return ResourcePackModelListing.showDialog(this);
   }

   public DialogBuilder clone() {
      DialogBuilder clone = new DialogBuilder(this.plugin, this.player, this.listing);
      clone.browsedLocation = this.browsedLocation;
      clone.browsedPage = this.browsedPage;
      clone.creativeMenu = this.creativeMenu;
      clone.title = this.title;
      clone.query = this.query;
      clone.cancelOnRootRightClick = this.cancelOnRootRightClick;
      clone.namespaceItem = this.namespaceItem;
      clone.directoryItem = this.directoryItem;
      clone.compactingEnabled = this.compactingEnabled;
      return clone;
   }

   private static ItemStack createDefaultNamespaceItem() {
      return CommonItemStack.create(MaterialUtil.getFirst(new String[]{"NAME_TAG", "LEGACY_NAME_TAG"}), 1).hideAllAttributes().toBukkit();
   }

   private static ItemStack createDefaultDirectoryItem() {
      return CommonItemStack.create(MaterialUtil.getFirst(new String[]{"CHEST", "LEGACY_CHEST"}), 1).hideAllAttributes().toBukkit();
   }
}
