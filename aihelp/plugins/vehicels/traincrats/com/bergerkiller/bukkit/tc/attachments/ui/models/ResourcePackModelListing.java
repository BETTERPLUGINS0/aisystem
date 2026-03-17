package com.bergerkiller.bukkit.tc.attachments.ui.models;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.map.MapResourcePack;
import com.bergerkiller.bukkit.common.map.MapResourcePack.ResourceType;
import com.bergerkiller.bukkit.common.map.util.ItemModelOverride;
import com.bergerkiller.bukkit.common.map.util.ModelInfoLookup;
import com.bergerkiller.bukkit.common.map.util.ItemModel.MinecraftModel;
import com.bergerkiller.bukkit.common.map.util.Model.ModelOverride;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.bukkit.tc.attachments.ui.models.listing.DialogBuilder;
import com.bergerkiller.bukkit.tc.attachments.ui.models.listing.DialogResult;
import com.bergerkiller.bukkit.tc.attachments.ui.models.listing.ListedItemModel;
import com.bergerkiller.bukkit.tc.attachments.ui.models.listing.ListedRoot;
import com.bergerkiller.bukkit.tc.attachments.ui.models.listing.ListedRootLoader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ResourcePackModelListing extends ListedRootLoader {
   private final Plugin plugin;
   private MapResourcePack resourcePack;
   private static final boolean CAN_SORT_DUPLICATE_ITEMS = Common.hasCapability("Common:CommonItemStack:ItemModel");

   public ResourcePackModelListing() {
      this((Plugin)null);
   }

   public ResourcePackModelListing(Plugin plugin) {
      this.plugin = plugin;
      this.resourcePack = null;
   }

   public DialogBuilder buildDialog(Player player) {
      if (this.plugin == null) {
         throw new IllegalStateException("No plugin was specified on constructor, cannot show dialog");
      } else {
         return new DialogBuilder(this.plugin, player, this);
      }
   }

   public DialogBuilder buildDialog(Player player, Plugin plugin) {
      if (plugin == null) {
         throw new IllegalArgumentException("Plugin is null");
      } else {
         return new DialogBuilder(plugin, player, this);
      }
   }

   public void showCreativeDialog(Player player) {
      this.buildDialog(player).asCreativeMenu().show();
   }

   public static void closeDialog(Player player) {
      ResourcePackModelListingDialog.close(player);
   }

   public static void closeAllDialogs() {
      ResourcePackModelListingDialog.closeAll();
   }

   public static void closeAllDialogs(Plugin plugin) {
      ResourcePackModelListingDialog.closeAllByPlugin(plugin);
   }

   public static CompletableFuture<DialogResult> showDialog(DialogBuilder dialogOptions) {
      return ResourcePackModelListingDialog.show(dialogOptions);
   }

   public boolean isEmpty() {
      return this.root.itemModels().isEmpty();
   }

   public void clear() {
      this.root = new ListedRoot();
   }

   public ListedRoot root() {
      return this.root;
   }

   public boolean isBareItem(ItemStack item) {
      return this.root.bareItemStacks().containsKey(item);
   }

   public ListedItemModel getBareItemModel(ItemStack bareItem) {
      return (ListedItemModel)this.root.bareItemStacks().get(bareItem);
   }

   public MapResourcePack loadedResourcePack() {
      return this.resourcePack;
   }

   public ResourcePackModelListing filter(String query) {
      ResourcePackModelListing filteredListing = new ResourcePackModelListing(this.plugin);
      filteredListing.resourcePack = this.resourcePack;
      filteredListing.loadFromListing(this.root, query);
      return filteredListing;
   }

   public void load(MapResourcePack resourcePack) {
      this.clear();
      this.resourcePack = resourcePack;
      int totalCount;
      if (Common.hasCapability("Common:ResourcePack:ItemModel")) {
         totalCount = this.loadModernItemModels(resourcePack);
      } else {
         totalCount = this.loadLegacyPredicates(resourcePack);
      }

      if (totalCount > 0) {
         this.logLoading("Resource pack item model lists loaded (" + totalCount + ")");
      }

   }

   private int loadModernItemModels(MapResourcePack resourcePack) {
      Set<String> allOverridedModels = resourcePack.listOverriddenItemModelNames();
      if (allOverridedModels.isEmpty()) {
         return 0;
      } else {
         this.logLoading("Loading resource pack item model lists");
         Map<String, List<CommonItemStack>> itemModels = new HashMap();
         Iterator var4 = allOverridedModels.iterator();

         label70:
         while(var4.hasNext()) {
            String itemModelName = (String)var4.next();
            Iterator var6 = resourcePack.getItemModelConfig(itemModelName).listAllOverrides().iterator();

            label68:
            while(true) {
               ItemModelOverride override;
               Optional itemStack;
               do {
                  if (!var6.hasNext()) {
                     continue label70;
                  }

                  override = (ItemModelOverride)var6.next();
                  itemStack = override.getItemStack();
               } while(!itemStack.isPresent());

               boolean strictNameSpaceCheck = override.isMatchingAlways();
               Iterator var10 = override.getOverrideModels().iterator();

               while(true) {
                  MinecraftModel model;
                  do {
                     do {
                        do {
                           if (!var10.hasNext()) {
                              continue label68;
                           }

                           model = (MinecraftModel)var10.next();
                        } while(!model.hasValidModels());
                     } while(model.model.startsWith("minecraft:"));
                  } while(strictNameSpaceCheck && !model.model.contains(":"));

                  ((List)itemModels.computeIfAbsent(model.model, (m) -> {
                     return new ArrayList();
                  })).add((CommonItemStack)itemStack.get());
               }
            }
         }

         var4 = itemModels.entrySet().iterator();

         while(var4.hasNext()) {
            Entry<String, List<CommonItemStack>> e = (Entry)var4.next();
            if (((List)e.getValue()).size() > 1 && CAN_SORT_DUPLICATE_ITEMS) {
               ((List)e.getValue()).sort(new ResourcePackModelListing.DuplicateItemComparator());
            }

            String credit = resourcePack.getModelInfo((String)e.getKey()).getCredit();
            this.root.addListedItem((String)e.getKey(), (CommonItemStack)((List)e.getValue()).get(0), credit);
         }

         return itemModels.size();
      }
   }

   /** @deprecated */
   @Deprecated
   private int loadLegacyPredicates(MapResourcePack resourcePack) {
      boolean logged = false;
      Set<String> allOverridedModels = new HashSet();

      for(MapResourcePack p = resourcePack; p != null && p != MapResourcePack.VANILLA; p = p.getBase()) {
         if (!logged) {
            logged = true;
            this.logLoading("Loading resource pack item model lists");
         }

         allOverridedModels.addAll(p.listResources(ResourceType.MODELS, "item", false));
      }

      if (allOverridedModels.isEmpty()) {
         return 0;
      } else {
         int totalCount = 0;
         Iterator var5 = ItemUtil.getItemTypes().iterator();

         label61:
         while(var5.hasNext()) {
            Material material = (Material)var5.next();
            Iterator var7 = ItemUtil.getItemVariants(material).iterator();

            label59:
            while(true) {
               ItemStack item;
               String path;
               do {
                  if (!var7.hasNext()) {
                     continue label61;
                  }

                  item = (ItemStack)var7.next();
                  path = "item/" + ModelInfoLookup.lookupItemRenderOptions(CommonItemStack.of(item)).lookupModelName();
               } while(!allOverridedModels.contains(path));

               Iterator var10 = resourcePack.getModelInfo(path).getOverrides().iterator();

               while(true) {
                  ModelOverride override;
                  while(true) {
                     do {
                        if (!var10.hasNext()) {
                           continue label59;
                        }

                        override = (ModelOverride)var10.next();
                     } while(override.model == null);

                     if (override.model.startsWith("minecraft:")) {
                        if (override.model.substring(10).equals(path)) {
                           continue;
                        }
                     } else if (override.model.equals(path)) {
                        continue;
                     }
                     break;
                  }

                  String credit = resourcePack.getModelInfo(override.model).getCredit();
                  ItemStack modelItem = override.applyToItem(item);
                  this.root.addListedItem(override.model, CommonItemStack.of(modelItem), credit);
                  ++totalCount;
               }
            }
         }

         return totalCount;
      }
   }

   private void logLoading(String message) {
      if (this.plugin != null) {
         this.plugin.getLogger().log(Level.INFO, "[Resource Pack Models] " + message);
      } else {
         System.out.println("[Resource Pack Models] " + message);
      }

   }

   private static class DuplicateItemComparator implements Comparator<CommonItemStack> {
      private DuplicateItemComparator() {
      }

      public int compare(CommonItemStack item1, CommonItemStack item2) {
         boolean hasItemModel = item1.hasItemModel();
         if (hasItemModel != item2.hasItemModel()) {
            return hasItemModel ? 1 : -1;
         } else {
            boolean hasCustomData = item1.hasCustomModelData();
            if (hasCustomData != item2.hasCustomModelData()) {
               return hasCustomData ? 1 : -1;
            } else {
               return 0;
            }
         }
      }

      // $FF: synthetic method
      DuplicateItemComparator(Object x0) {
         this();
      }
   }
}
