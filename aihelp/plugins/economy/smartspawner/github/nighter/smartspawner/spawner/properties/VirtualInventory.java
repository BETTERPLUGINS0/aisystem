package github.nighter.smartspawner.spawner.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class VirtualInventory {
   private final Map<VirtualInventory.ItemSignature, Long> consolidatedItems;
   private int maxSlots;
   private final Map<Integer, ItemStack> displayInventoryCache;
   private boolean displayCacheDirty;
   private int usedSlotsCache;
   private long totalItemsCache;
   private boolean metricsCacheDirty;
   private List<Entry<VirtualInventory.ItemSignature, Long>> sortedEntriesCache;
   private Material preferredSortMaterial;
   private static final int ITEM_CACHE_SIZE = 128;
   private static final Map<ItemStack, VirtualInventory.ItemSignature> signatureCache = Collections.synchronizedMap(new LinkedHashMap<ItemStack, VirtualInventory.ItemSignature>(128, 0.75F, true) {
      protected boolean removeEldestEntry(Entry<ItemStack, VirtualInventory.ItemSignature> eldest) {
         return this.size() > 128;
      }
   });

   public VirtualInventory(int maxSlots) {
      this.maxSlots = maxSlots;
      this.consolidatedItems = new ConcurrentHashMap();
      this.displayInventoryCache = new HashMap(maxSlots);
      this.displayCacheDirty = true;
      this.metricsCacheDirty = true;
      this.usedSlotsCache = 0;
      this.totalItemsCache = 0L;
      this.sortedEntriesCache = null;
      this.preferredSortMaterial = null;
   }

   public static VirtualInventory.ItemSignature getSignature(ItemStack item) {
      VirtualInventory.ItemSignature cachedSig = (VirtualInventory.ItemSignature)signatureCache.get(item);
      if (cachedSig != null) {
         return cachedSig;
      } else {
         VirtualInventory.ItemSignature newSig = new VirtualInventory.ItemSignature(item);
         signatureCache.put(item.clone(), newSig);
         return newSig;
      }
   }

   public void addItems(List<ItemStack> items) {
      if (!items.isEmpty()) {
         Map<VirtualInventory.ItemSignature, Long> itemBatch = new HashMap(items.size());
         Iterator var3 = items.iterator();

         while(var3.hasNext()) {
            ItemStack item = (ItemStack)var3.next();
            if (item != null && item.getAmount() > 0) {
               VirtualInventory.ItemSignature sig = getSignature(item);
               itemBatch.merge(sig, (long)item.getAmount(), Long::sum);
            }
         }

         if (!itemBatch.isEmpty()) {
            var3 = itemBatch.entrySet().iterator();

            while(var3.hasNext()) {
               Entry<VirtualInventory.ItemSignature, Long> entry = (Entry)var3.next();
               this.consolidatedItems.merge((VirtualInventory.ItemSignature)entry.getKey(), (Long)entry.getValue(), Long::sum);
            }

            this.displayCacheDirty = true;
            this.metricsCacheDirty = true;
            this.sortedEntriesCache = null;
         }

      }
   }

   public boolean removeItems(List<ItemStack> items) {
      if (items.isEmpty()) {
         return true;
      } else {
         Map<VirtualInventory.ItemSignature, Long> toRemove = new HashMap();
         Iterator var3 = items.iterator();

         while(var3.hasNext()) {
            ItemStack item = (ItemStack)var3.next();
            if (item != null && item.getAmount() > 0) {
               VirtualInventory.ItemSignature sig = getSignature(item);
               toRemove.merge(sig, (long)item.getAmount(), Long::sum);
            }
         }

         if (toRemove.isEmpty()) {
            return true;
         } else {
            var3 = toRemove.entrySet().iterator();

            while(var3.hasNext()) {
               Entry<VirtualInventory.ItemSignature, Long> entry = (Entry)var3.next();
               Long currentAmount = (Long)this.consolidatedItems.getOrDefault(entry.getKey(), 0L);
               if (currentAmount < (Long)entry.getValue()) {
                  return false;
               }
            }

            boolean updated = false;

            for(Iterator var11 = toRemove.entrySet().iterator(); var11.hasNext(); updated = true) {
               Entry<VirtualInventory.ItemSignature, Long> entry = (Entry)var11.next();
               VirtualInventory.ItemSignature sig = (VirtualInventory.ItemSignature)entry.getKey();
               long amountToRemove = (Long)entry.getValue();
               this.consolidatedItems.computeIfPresent(sig, (key, current) -> {
                  long newAmount = current - amountToRemove;
                  return newAmount <= 0L ? null : newAmount;
               });
            }

            if (updated) {
               this.displayCacheDirty = true;
               this.metricsCacheDirty = true;
               this.sortedEntriesCache = null;
            }

            return true;
         }
      }
   }

   public Map<Integer, ItemStack> getDisplayInventory() {
      if (!this.displayCacheDirty) {
         return Collections.unmodifiableMap(this.displayInventoryCache);
      } else {
         this.displayInventoryCache.clear();
         if (this.consolidatedItems.isEmpty()) {
            this.displayCacheDirty = false;
            this.usedSlotsCache = 0;
            return Collections.emptyMap();
         } else {
            if (this.sortedEntriesCache == null) {
               this.sortedEntriesCache = new ArrayList(this.consolidatedItems.entrySet());
               if (this.preferredSortMaterial != null) {
                  this.sortedEntriesCache.sort((e1, e2) -> {
                     boolean e1Preferred = ((VirtualInventory.ItemSignature)e1.getKey()).getTemplateRef().getType() == this.preferredSortMaterial;
                     boolean e2Preferred = ((VirtualInventory.ItemSignature)e2.getKey()).getTemplateRef().getType() == this.preferredSortMaterial;
                     if (e1Preferred && !e2Preferred) {
                        return -1;
                     } else {
                        return !e1Preferred && e2Preferred ? 1 : ((VirtualInventory.ItemSignature)e1.getKey()).getMaterialName().compareTo(((VirtualInventory.ItemSignature)e2.getKey()).getMaterialName());
                     }
                  });
               } else {
                  this.sortedEntriesCache.sort(Comparator.comparing((e) -> {
                     return ((VirtualInventory.ItemSignature)e.getKey()).getMaterialName();
                  }));
               }
            }

            int currentSlot = 0;
            Iterator var2 = this.sortedEntriesCache.iterator();

            while(var2.hasNext()) {
               Entry<VirtualInventory.ItemSignature, Long> entry = (Entry)var2.next();
               if (currentSlot >= this.maxSlots) {
                  break;
               }

               VirtualInventory.ItemSignature sig = (VirtualInventory.ItemSignature)entry.getKey();
               long totalAmount = (Long)entry.getValue();
               ItemStack templateItem = sig.getTemplateRef();

               for(int maxStackSize = templateItem.getMaxStackSize(); totalAmount > 0L && currentSlot < this.maxSlots; ++currentSlot) {
                  int stackSize = (int)Math.min(totalAmount, (long)maxStackSize);
                  ItemStack displayItem = templateItem.clone();
                  displayItem.setAmount(stackSize);
                  this.displayInventoryCache.put(currentSlot, displayItem);
                  totalAmount -= (long)stackSize;
               }
            }

            this.displayCacheDirty = false;
            this.usedSlotsCache = this.displayInventoryCache.size();
            return Collections.unmodifiableMap(this.displayInventoryCache);
         }
      }
   }

   public long getTotalItems() {
      if (this.metricsCacheDirty) {
         this.updateMetricsCache();
      }

      return this.totalItemsCache;
   }

   public Map<VirtualInventory.ItemSignature, Long> getConsolidatedItems() {
      return new HashMap(this.consolidatedItems);
   }

   public int getUsedSlots() {
      if (this.displayCacheDirty) {
         if (this.consolidatedItems.isEmpty()) {
            return 0;
         } else {
            int estimatedSlots = 0;
            Iterator var2 = this.consolidatedItems.entrySet().iterator();

            do {
               if (!var2.hasNext()) {
                  return estimatedSlots;
               }

               Entry<VirtualInventory.ItemSignature, Long> entry = (Entry)var2.next();
               long amount = (Long)entry.getValue();
               int maxStackSize = ((VirtualInventory.ItemSignature)entry.getKey()).getTemplateRef().getMaxStackSize();
               estimatedSlots += (int)Math.ceil((double)amount / (double)maxStackSize);
            } while(estimatedSlots < this.maxSlots);

            return this.maxSlots;
         }
      } else {
         return this.usedSlotsCache;
      }
   }

   private void updateMetricsCache() {
      this.totalItemsCache = this.consolidatedItems.values().stream().mapToLong(Long::longValue).sum();
      this.metricsCacheDirty = false;
   }

   public boolean isDirty() {
      return this.displayCacheDirty;
   }

   public void sortItems(Material preferredMaterial) {
      this.preferredSortMaterial = preferredMaterial;
      this.sortedEntriesCache = null;
      if (this.consolidatedItems.isEmpty()) {
         this.displayCacheDirty = true;
      } else {
         if (preferredMaterial != null) {
            this.sortedEntriesCache = (List)this.consolidatedItems.entrySet().stream().sorted((e1, e2) -> {
               boolean e1Preferred = ((VirtualInventory.ItemSignature)e1.getKey()).getTemplateRef().getType() == preferredMaterial;
               boolean e2Preferred = ((VirtualInventory.ItemSignature)e2.getKey()).getTemplateRef().getType() == preferredMaterial;
               if (e1Preferred && !e2Preferred) {
                  return -1;
               } else {
                  return !e1Preferred && e2Preferred ? 1 : ((VirtualInventory.ItemSignature)e1.getKey()).getMaterialName().compareTo(((VirtualInventory.ItemSignature)e2.getKey()).getMaterialName());
               }
            }).collect(Collectors.toList());
         } else {
            this.sortedEntriesCache = (List)this.consolidatedItems.entrySet().stream().sorted(Comparator.comparing((e) -> {
               return ((VirtualInventory.ItemSignature)e.getKey()).getMaterialName();
            })).collect(Collectors.toList());
         }

         this.displayCacheDirty = true;
      }
   }

   public void resize(int newMaxSlots) {
      if (newMaxSlots != this.maxSlots) {
         this.maxSlots = newMaxSlots;
         this.displayCacheDirty = true;
         if (newMaxSlots < this.usedSlotsCache) {
         }

      }
   }

   @Generated
   public int getMaxSlots() {
      return this.maxSlots;
   }

   public static class ItemSignature {
      private final ItemStack template;
      private final int hashCode;
      private final String materialName;

      public ItemSignature(ItemStack item) {
         this.template = item.clone();
         this.template.setAmount(1);
         this.materialName = item.getType().name();
         this.hashCode = this.calculateHashCode();
      }

      private int calculateHashCode() {
         int result = 31 * this.template.getType().ordinal();
         result = 31 * result + this.template.getDurability();
         if (this.template.hasItemMeta()) {
            ItemMeta meta = this.template.getItemMeta();
            result = 31 * result + (meta.hasDisplayName() ? meta.getDisplayName().hashCode() : 0);
            result = 31 * result + (meta.hasLore() ? meta.getLore().hashCode() : 0);
            result = 31 * result + (meta.hasEnchants() ? meta.getEnchants().hashCode() : 0);
         }

         return result;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (!(o instanceof VirtualInventory.ItemSignature)) {
            return false;
         } else {
            VirtualInventory.ItemSignature that = (VirtualInventory.ItemSignature)o;
            if (this.template.getType() == that.template.getType() && this.template.getDurability() == that.template.getDurability()) {
               boolean thisHasMeta = this.template.hasItemMeta();
               boolean thatHasMeta = that.template.hasItemMeta();
               if (thisHasMeta != thatHasMeta) {
                  return false;
               } else {
                  return !thisHasMeta ? true : this.template.isSimilar(that.template);
               }
            } else {
               return false;
            }
         }
      }

      public int hashCode() {
         return this.hashCode;
      }

      public ItemStack getTemplate() {
         return this.template.clone();
      }

      public ItemStack getTemplateRef() {
         return this.template;
      }

      @Generated
      public String getMaterialName() {
         return this.materialName;
      }
   }
}
