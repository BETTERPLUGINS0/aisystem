package github.nighter.smartspawner.spawner.gui.synchronization.services;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.spawner.gui.main.SpawnerMenuHolder;
import github.nighter.smartspawner.spawner.gui.main.SpawnerMenuUI;
import github.nighter.smartspawner.spawner.gui.synchronization.managers.SlotCacheManager;
import github.nighter.smartspawner.spawner.gui.synchronization.managers.ViewerTrackingManager;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiUpdateService {
   public static final int UPDATE_CHEST = 1;
   public static final int UPDATE_INFO = 2;
   public static final int UPDATE_EXP = 4;
   public static final int UPDATE_ALL = 7;
   private final SmartSpawner plugin;
   private final LanguageManager languageManager;
   private final SpawnerMenuUI spawnerMenuUI;
   private final SlotCacheManager slotCacheManager;
   private final Set<UUID> pendingUpdates = ConcurrentHashMap.newKeySet();
   private final Map<UUID, Integer> updateFlags = new ConcurrentHashMap();

   public GuiUpdateService(SmartSpawner plugin, SlotCacheManager slotCacheManager) {
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
      this.spawnerMenuUI = plugin.getSpawnerMenuUI();
      this.slotCacheManager = slotCacheManager;
   }

   public void scheduleUpdate(UUID playerId, int flags) {
      this.pendingUpdates.add(playerId);
      this.updateFlags.put(playerId, flags);
   }

   public void processPendingUpdates(Function<UUID, ?> viewerInfoGetter, Consumer<UUID> untrackViewer) {
      if (!this.pendingUpdates.isEmpty()) {
         Set<UUID> currentUpdates = new HashSet(this.pendingUpdates);
         this.pendingUpdates.clear();
         Iterator var4 = currentUpdates.iterator();

         while(true) {
            while(var4.hasNext()) {
               UUID playerId = (UUID)var4.next();
               Player player = Bukkit.getPlayer(playerId);
               if (player != null && player.isOnline()) {
                  Object info = viewerInfoGetter.apply(playerId);
                  if (info == null) {
                     this.updateFlags.remove(playerId);
                  } else {
                     int flags = (Integer)this.updateFlags.getOrDefault(playerId, 7);
                     this.updateFlags.remove(playerId);
                     Location loc = player.getLocation();
                     if (loc != null) {
                        SpawnerData spawner = this.extractSpawnerData(info);
                        if (spawner != null) {
                           Scheduler.runLocationTask(loc, () -> {
                              if (player.isOnline()) {
                                 Inventory openInv = player.getOpenInventory().getTopInventory();
                                 if (openInv != null && openInv.getHolder(false) instanceof SpawnerMenuHolder) {
                                    this.processInventoryUpdate(player, openInv, spawner, flags);
                                 }
                              }
                           });
                        }
                     }
                  }
               } else {
                  untrackViewer.accept(playerId);
                  this.updateFlags.remove(playerId);
               }
            }

            return;
         }
      }
   }

   private void processInventoryUpdate(Player player, Inventory inventory, SpawnerData spawner, int flags) {
      boolean needsUpdate = false;
      int expSlot;
      if ((flags & 1) != 0) {
         expSlot = this.slotCacheManager.getStorageSlot();
         if (expSlot >= 0) {
            this.updateChestItem(inventory, spawner, expSlot);
            needsUpdate = true;
         }
      }

      if ((flags & 2) != 0) {
         expSlot = this.slotCacheManager.getSpawnerInfoSlot();
         if (expSlot >= 0) {
            this.updateSpawnerInfoItem(inventory, spawner, player, expSlot);
            needsUpdate = true;
         }
      }

      if ((flags & 4) != 0) {
         expSlot = this.slotCacheManager.getExpSlot();
         if (expSlot >= 0) {
            this.updateExpItem(inventory, spawner, expSlot);
            needsUpdate = true;
         }
      }

      if (needsUpdate) {
         player.updateInventory();
      }

   }

   private void updateChestItem(Inventory inventory, SpawnerData spawner, int storageSlot) {
      if (storageSlot >= 0) {
         ItemStack currentChestItem = inventory.getItem(storageSlot);
         if (currentChestItem != null && currentChestItem.hasItemMeta()) {
            ItemStack newChestItem = this.spawnerMenuUI.createLootStorageItem(spawner);
            if (!areItemsEqual(currentChestItem, newChestItem)) {
               inventory.setItem(storageSlot, newChestItem);
            }

         }
      }
   }

   private void updateExpItem(Inventory inventory, SpawnerData spawner, int expSlot) {
      if (expSlot >= 0) {
         ItemStack currentExpItem = inventory.getItem(expSlot);
         if (currentExpItem != null && currentExpItem.hasItemMeta()) {
            ItemStack newExpItem = this.spawnerMenuUI.createExpItem(spawner);
            if (!areItemsEqual(currentExpItem, newExpItem)) {
               inventory.setItem(expSlot, newExpItem);
            }

         }
      }
   }

   private void updateSpawnerInfoItem(Inventory inventory, SpawnerData spawner, Player player, int spawnerInfoSlot) {
      if (spawnerInfoSlot >= 0) {
         ItemStack currentSpawnerItem = inventory.getItem(spawnerInfoSlot);
         if (currentSpawnerItem != null && currentSpawnerItem.hasItemMeta()) {
            ItemStack newSpawnerItem = this.spawnerMenuUI.createSpawnerInfoItem(player, spawner);
            if (!areItemsEqual(currentSpawnerItem, newSpawnerItem)) {
               this.preserveTimerInfo(currentSpawnerItem, newSpawnerItem);
               inventory.setItem(spawnerInfoSlot, newSpawnerItem);
            }

         }
      }
   }

   private void preserveTimerInfo(ItemStack currentItem, ItemStack newItem) {
      ItemMeta currentMeta = currentItem.getItemMeta();
      ItemMeta newMeta = newItem.getItemMeta();
      if (currentMeta != null && currentMeta.hasLore() && newMeta != null && newMeta.hasLore()) {
         List<String> currentLore = currentMeta.getLore();
         List<String> newLore = newMeta.getLore();
         if (currentLore != null && newLore != null) {
            int newTimerLineIndex = -1;

            for(int i = 0; i < newLore.size(); ++i) {
               if (((String)newLore.get(i)).contains("{time}")) {
                  newTimerLineIndex = i;
                  break;
               }
            }

            if (newTimerLineIndex != -1) {
               if (newTimerLineIndex < currentLore.size()) {
                  String currentLine = (String)currentLore.get(newTimerLineIndex);
                  String newLine = (String)newLore.get(newTimerLineIndex);
                  if (!currentLine.contains("{time}") && newLine.contains("{time}")) {
                     String currentTimerValue = this.extractTimerValue(currentLine, newLine);
                     if (currentTimerValue != null && !currentTimerValue.isEmpty()) {
                        Map<String, String> timerPlaceholder = Collections.singletonMap("time", currentTimerValue);
                        List<String> updatedLore = new ArrayList(newLore.size());
                        Iterator var13 = newLore.iterator();

                        while(var13.hasNext()) {
                           String line = (String)var13.next();
                           updatedLore.add(this.languageManager.applyOnlyPlaceholders(line, timerPlaceholder));
                        }

                        newMeta.setLore(updatedLore);
                        newItem.setItemMeta(newMeta);
                     }
                  }
               }

            }
         }
      }
   }

   public static boolean areItemsEqual(ItemStack item1, ItemStack item2) {
      if (item1 == item2) {
         return true;
      } else if (item1 != null && item2 != null) {
         if (item1.getType() != item2.getType()) {
            return false;
         } else {
            ItemMeta meta1 = item1.getItemMeta();
            ItemMeta meta2 = item2.getItemMeta();
            if (meta1 == null && meta2 == null) {
               return true;
            } else if (meta1 != null && meta2 != null) {
               if (meta1.hasDisplayName() != meta2.hasDisplayName()) {
                  return false;
               } else {
                  return meta1.hasDisplayName() && !meta1.getDisplayName().equals(meta2.getDisplayName()) ? false : areLoreListsEqual(meta1.getLore(), meta2.getLore());
               }
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private static boolean areLoreListsEqual(List<String> lore1, List<String> lore2) {
      if (lore1 == lore2) {
         return true;
      } else if (lore1 != null && lore2 != null) {
         int size = lore1.size();
         if (size != lore2.size()) {
            return false;
         } else {
            for(int i = 0; i < size; ++i) {
               String s1 = (String)lore1.get(i);
               String s2 = (String)lore2.get(i);
               if (s1 != s2) {
                  if (s1 == null || s2 == null) {
                     return false;
                  }

                  if (!s1.equals(s2)) {
                     return false;
                  }
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private String extractTimerValue(String currentLine, String newLine) {
      String newLineTemplate = newLine.replace("{time}", "TIMER_PLACEHOLDER");
      String cleanNewTemplate = ChatColor.stripColor(newLineTemplate);
      String cleanCurrentLine = ChatColor.stripColor(currentLine);
      int placeholderIndex = cleanNewTemplate.indexOf("TIMER_PLACEHOLDER");
      if (placeholderIndex >= 0 && cleanCurrentLine.length() >= placeholderIndex) {
         String beforePlaceholder = cleanNewTemplate.substring(0, placeholderIndex);
         String afterPlaceholder = cleanNewTemplate.substring(placeholderIndex + "TIMER_PLACEHOLDER".length());
         if (cleanCurrentLine.startsWith(beforePlaceholder) && cleanCurrentLine.endsWith(afterPlaceholder)) {
            int startIndex = beforePlaceholder.length();
            int endIndex = cleanCurrentLine.length() - afterPlaceholder.length();
            if (endIndex > startIndex) {
               return cleanCurrentLine.substring(startIndex, endIndex).trim();
            }
         }
      }

      return null;
   }

   private SpawnerData extractSpawnerData(Object info) {
      if (info instanceof ViewerTrackingManager.ViewerInfo) {
         ViewerTrackingManager.ViewerInfo viewerInfo = (ViewerTrackingManager.ViewerInfo)info;
         return viewerInfo.getSpawnerData();
      } else {
         return null;
      }
   }

   public void clearAllPendingUpdates() {
      this.pendingUpdates.clear();
      this.updateFlags.clear();
   }

   public void clearPlayerUpdates(UUID playerId) {
      this.pendingUpdates.remove(playerId);
      this.updateFlags.remove(playerId);
   }
}
