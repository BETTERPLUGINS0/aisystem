package github.nighter.smartspawner.spawner.gui.synchronization.services;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.spawner.gui.main.SpawnerMenuHolder;
import github.nighter.smartspawner.spawner.gui.synchronization.managers.SlotCacheManager;
import github.nighter.smartspawner.spawner.gui.synchronization.managers.ViewerTrackingManager;
import github.nighter.smartspawner.spawner.gui.synchronization.utils.LootPreGenerationHelper;
import github.nighter.smartspawner.spawner.gui.synchronization.utils.TimerFormatter;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TimerUpdateService {
   private static final int MAX_PLAYERS_PER_BATCH = 10;
   private static final Pattern TIMER_PATTERN = Pattern.compile("\\d{2}:\\d{2}");
   private final SmartSpawner plugin;
   private final LanguageManager languageManager;
   private final LootPreGenerationHelper lootHelper;
   private final ViewerTrackingManager viewerTrackingManager;
   private final SlotCacheManager slotCacheManager;
   private String cachedInactiveText;
   private String cachedFullText;
   private String cachedNoLootText;
   private volatile Boolean hasTimerPlaceholders = null;
   private final Map<UUID, Long> lastTimerUpdate = new ConcurrentHashMap();
   private final Map<UUID, String> lastTimerValue = new ConcurrentHashMap();
   private final Map<String, Integer> timerLineIndexCache = new ConcurrentHashMap();

   public TimerUpdateService(SmartSpawner plugin, ViewerTrackingManager viewerTrackingManager, SlotCacheManager slotCacheManager) {
      this.plugin = plugin;
      this.languageManager = plugin.getLanguageManager();
      this.lootHelper = new LootPreGenerationHelper(plugin);
      this.viewerTrackingManager = viewerTrackingManager;
      this.slotCacheManager = slotCacheManager;
      this.initializeCachedStrings();
   }

   private void initializeCachedStrings() {
      this.cachedInactiveText = this.languageManager.getGuiItemName("spawner_info_item.lore_inactive");
      this.cachedFullText = this.languageManager.getGuiItemName("spawner_info_item.lore_full");
      this.cachedNoLootText = this.languageManager.getGuiItemName("spawner_info_item.lore_no_loot");
      this.checkTimerPlaceholderUsage();
   }

   private void checkTimerPlaceholderUsage() {
      try {
         String[] loreLines = this.languageManager.getGuiItemLore("spawner_info_item.lore");
         String[] loreNoShopLines = this.languageManager.getGuiItemLore("spawner_info_item.lore_no_shop");
         boolean hasTimers = false;
         String[] var4;
         int var5;
         int var6;
         String line;
         if (loreLines != null) {
            var4 = loreLines;
            var5 = loreLines.length;

            for(var6 = 0; var6 < var5; ++var6) {
               line = var4[var6];
               if (line != null && line.contains("{time}")) {
                  hasTimers = true;
                  break;
               }
            }
         }

         if (!hasTimers && loreNoShopLines != null) {
            var4 = loreNoShopLines;
            var5 = loreNoShopLines.length;

            for(var6 = 0; var6 < var5; ++var6) {
               line = var4[var6];
               if (line != null && line.contains("{time}")) {
                  hasTimers = true;
                  break;
               }
            }
         }

         this.hasTimerPlaceholders = hasTimers;
      } catch (Exception var8) {
         this.hasTimerPlaceholders = true;
      }

   }

   public boolean isTimerPlaceholdersEnabled() {
      return this.hasTimerPlaceholders == null || this.hasTimerPlaceholders;
   }

   public boolean shouldProcessTimerUpdates() {
      return this.hasTimerPlaceholders == null || this.hasTimerPlaceholders;
   }

   public void recheckTimerPlaceholders() {
      this.timerLineIndexCache.clear();
      this.lastTimerUpdate.clear();
      this.lastTimerValue.clear();
      this.initializeCachedStrings();
   }

   public String calculateTimerDisplay(SpawnerData spawner, Player player) {
      if (!this.isTimerPlaceholdersEnabled()) {
         return "";
      } else if (player != null && player.getGameMode() == GameMode.SPECTATOR) {
         return this.cachedInactiveText;
      } else if (spawner.hasNoLootOrExperience()) {
         return this.cachedNoLootText;
      } else if (spawner.getIsAtCapacity()) {
         return this.cachedFullText;
      } else {
         long timeUntilNextSpawn = this.calculateTimeUntilNextSpawn(spawner);
         return timeUntilNextSpawn == -1L ? this.cachedInactiveText : TimerFormatter.formatTime(timeUntilNextSpawn);
      }
   }

   public void processTimerUpdates() {
      if (this.hasTimerPlaceholders == null || this.hasTimerPlaceholders) {
         if (this.viewerTrackingManager.hasMainMenuViewers()) {
            long currentTime = System.currentTimeMillis();
            Map<String, List<TimerUpdateService.PlayerViewerContext>> spawnerViewers = new HashMap();
            Map<UUID, ViewerTrackingManager.ViewerInfo> mainMenuViewers = this.viewerTrackingManager.getMainMenuViewers();
            Iterator var5 = mainMenuViewers.entrySet().iterator();

            while(true) {
               while(true) {
                  UUID playerId;
                  SpawnerData spawner;
                  Long lastUpdate;
                  do {
                     if (!var5.hasNext()) {
                        int processedPlayers = 0;
                        Iterator var19 = spawnerViewers.entrySet().iterator();

                        while(var19.hasNext()) {
                           Entry<String, List<TimerUpdateService.PlayerViewerContext>> spawnerGroup = (Entry)var19.next();
                           List<TimerUpdateService.PlayerViewerContext> viewers = (List)spawnerGroup.getValue();
                           if (!viewers.isEmpty()) {
                              TimerUpdateService.PlayerViewerContext firstViewer = (TimerUpdateService.PlayerViewerContext)viewers.get(0);
                              ViewerTrackingManager.ViewerInfo viewerInfo = (ViewerTrackingManager.ViewerInfo)mainMenuViewers.get(firstViewer.playerId);
                              if (viewerInfo != null) {
                                 SpawnerData spawner = viewerInfo.getSpawnerData();
                                 String newTimerValue = this.calculateTimerDisplayInternal(spawner);
                                 Iterator var13 = viewers.iterator();

                                 while(var13.hasNext()) {
                                    TimerUpdateService.PlayerViewerContext context = (TimerUpdateService.PlayerViewerContext)var13.next();
                                    if (processedPlayers >= 10) {
                                       break;
                                    }

                                    String lastValue = (String)this.lastTimerValue.get(context.playerId);
                                    if (lastValue == null || !lastValue.equals(newTimerValue)) {
                                       this.lastTimerUpdate.put(context.playerId, currentTime);
                                       this.lastTimerValue.put(context.playerId, newTimerValue);
                                       ++processedPlayers;
                                       if (context.location != null) {
                                          UUID finalPlayerId = context.playerId;
                                          Scheduler.runLocationTask(context.location, () -> {
                                             if (context.player.isOnline() && mainMenuViewers.containsKey(finalPlayerId)) {
                                                Inventory currentInv = context.player.getOpenInventory().getTopInventory();
                                                if (currentInv != null && currentInv.getHolder(false) instanceof SpawnerMenuHolder) {
                                                   int spawnerInfoSlot = this.slotCacheManager.getSpawnerInfoSlot();
                                                   if (spawnerInfoSlot >= 0) {
                                                      this.updateSpawnerInfoItemTimer(currentInv, spawner, newTimerValue, spawnerInfoSlot);
                                                      context.player.updateInventory();
                                                   }

                                                } else {
                                                   this.viewerTrackingManager.untrackViewer(finalPlayerId);
                                                }
                                             }
                                          });
                                       }
                                    }
                                 }

                                 if (processedPlayers >= 10) {
                                    break;
                                 }
                              }
                           }
                        }

                        return;
                     }

                     Entry<UUID, ViewerTrackingManager.ViewerInfo> entry = (Entry)var5.next();
                     playerId = (UUID)entry.getKey();
                     ViewerTrackingManager.ViewerInfo viewerInfo = (ViewerTrackingManager.ViewerInfo)entry.getValue();
                     spawner = viewerInfo.getSpawnerData();
                     lastUpdate = (Long)this.lastTimerUpdate.get(playerId);
                  } while(lastUpdate != null && currentTime - lastUpdate < 800L);

                  Player player = Bukkit.getPlayer(playerId);
                  if (player != null && player.isOnline()) {
                     Inventory openInventory = player.getOpenInventory().getTopInventory();
                     if (!(openInventory.getHolder(false) instanceof SpawnerMenuHolder)) {
                        this.viewerTrackingManager.untrackViewer(playerId);
                     } else {
                        ((List)spawnerViewers.computeIfAbsent(spawner.getSpawnerId(), (k) -> {
                           return new ArrayList();
                        })).add(new TimerUpdateService.PlayerViewerContext(playerId, player, openInventory, player.getLocation()));
                     }
                  } else {
                     this.viewerTrackingManager.untrackViewer(playerId);
                  }
               }
            }
         }
      }
   }

   public void forceStateChangeUpdate(SpawnerData spawner) {
      if (this.isTimerPlaceholdersEnabled()) {
         Set<UUID> mainMenuViewerSet = this.viewerTrackingManager.getMainMenuViewersForSpawner(spawner.getSpawnerId());
         if (mainMenuViewerSet != null && !mainMenuViewerSet.isEmpty()) {
            Iterator var3 = mainMenuViewerSet.iterator();

            while(var3.hasNext()) {
               UUID viewerId = (UUID)var3.next();
               this.lastTimerUpdate.remove(viewerId);
               this.lastTimerValue.remove(viewerId);
            }

            this.updateMainMenuViewers(spawner);
         }
      }
   }

   public void forceTimerUpdateInactive(Player player, SpawnerData spawner) {
      spawner.clearPreGeneratedLoot();
      if (this.isTimerPlaceholdersEnabled()) {
         if (this.isValidGuiSession(player)) {
            Location playerLocation = player.getLocation();
            if (playerLocation != null) {
               Scheduler.runLocationTask(playerLocation, () -> {
                  if (player.isOnline()) {
                     Inventory openInventory = player.getOpenInventory().getTopInventory();
                     if (openInventory != null && openInventory.getHolder(false) instanceof SpawnerMenuHolder) {
                        int spawnerInfoSlot = this.slotCacheManager.getSpawnerInfoSlot();
                        if (spawnerInfoSlot >= 0) {
                           String timerValue = this.cachedInactiveText;
                           this.updateSpawnerInfoItemTimer(openInventory, spawner, timerValue, spawnerInfoSlot);
                           player.updateInventory();
                        }

                     }
                  }
               });
            }
         }
      }
   }

   private String calculateTimerDisplayInternal(SpawnerData spawner) {
      if (spawner.hasNoLootOrExperience()) {
         return this.cachedNoLootText;
      } else if (spawner.getIsAtCapacity()) {
         return this.cachedFullText;
      } else if (spawner.getSpawnerStop().get()) {
         spawner.clearPreGeneratedLoot();
         return this.cachedInactiveText;
      } else {
         long timeUntilNextSpawn = this.calculateTimeUntilNextSpawn(spawner);
         return TimerFormatter.formatTime(timeUntilNextSpawn);
      }
   }

   private void updateMainMenuViewers(SpawnerData spawner) {
      Set<UUID> mainMenuViewerSet = this.viewerTrackingManager.getMainMenuViewersForSpawner(spawner.getSpawnerId());
      if (mainMenuViewerSet != null && !mainMenuViewerSet.isEmpty()) {
         String timerValue = this.calculateTimerDisplayInternal(spawner);
         Iterator var4 = (new HashSet(mainMenuViewerSet)).iterator();

         while(var4.hasNext()) {
            UUID viewerId = (UUID)var4.next();
            Player viewer = Bukkit.getPlayer(viewerId);
            if (!this.isValidGuiSession(viewer)) {
               this.viewerTrackingManager.untrackViewer(viewerId);
            } else {
               Location loc = viewer.getLocation();
               if (loc != null) {
                  Scheduler.runLocationTask(loc, () -> {
                     if (viewer.isOnline() && this.viewerTrackingManager.getMainMenuViewers().containsKey(viewerId)) {
                        Inventory openInv = viewer.getOpenInventory().getTopInventory();
                        if (openInv != null && openInv.getHolder(false) instanceof SpawnerMenuHolder) {
                           this.updateSpawnerInfoItemTimer(openInv, spawner, timerValue, this.slotCacheManager.getSpawnerInfoSlot());
                           viewer.updateInventory();
                        } else {
                           this.viewerTrackingManager.untrackViewer(viewerId);
                        }
                     }
                  });
               }
            }
         }

      }
   }

   private long calculateTimeUntilNextSpawn(SpawnerData spawner) {
      long cachedDelay = spawner.getCachedSpawnDelay();
      if (cachedDelay == 0L) {
         cachedDelay = (spawner.getSpawnDelay() + 20L) * 50L;
         spawner.setCachedSpawnDelay(cachedDelay);
      }

      long currentTime = System.currentTimeMillis();
      long lastSpawnTime = spawner.getLastSpawnTime();
      long timeElapsed = currentTime - lastSpawnTime;
      long timeUntilNextSpawn = cachedDelay - timeElapsed;
      timeUntilNextSpawn = Math.max(0L, Math.min(timeUntilNextSpawn, cachedDelay));
      if (this.lootHelper.shouldPreGenerateLoot(timeUntilNextSpawn)) {
         this.lootHelper.preGenerateLoot(spawner);
      }

      if (this.lootHelper.shouldAddLootEarly(timeUntilNextSpawn)) {
         this.lootHelper.addPreGeneratedLootEarly(spawner, cachedDelay);
      }

      return timeUntilNextSpawn;
   }

   private void updateSpawnerInfoItemTimer(Inventory inventory, SpawnerData spawner, String timeDisplay, int spawnerInfoSlot) {
      if (spawnerInfoSlot >= 0) {
         ItemStack spawnerItem = inventory.getItem(spawnerInfoSlot);
         if (spawnerItem != null) {
            ItemMeta meta = spawnerItem.getItemMeta();
            if (meta != null) {
               List<String> lore = meta.getLore();
               if (lore != null && !lore.isEmpty()) {
                  String spawnerId = spawner.getSpawnerId();
                  Integer cachedIndex = (Integer)this.timerLineIndexCache.get(spawnerId);
                  boolean needsUpdate = false;
                  int updatedIndex = -1;
                  String updatedLine = null;
                  if (cachedIndex != null && cachedIndex >= 0 && cachedIndex < lore.size()) {
                     String line = (String)lore.get(cachedIndex);
                     updatedLine = this.tryUpdateTimerLine(line, timeDisplay);
                     if (updatedLine != null && !updatedLine.equals(line)) {
                        updatedIndex = cachedIndex;
                        needsUpdate = true;
                     } else if (updatedLine == null) {
                        this.timerLineIndexCache.remove(spawnerId);
                        cachedIndex = null;
                     }
                  }

                  if (!needsUpdate && cachedIndex == null) {
                     for(int i = 0; i < lore.size(); ++i) {
                        String line = (String)lore.get(i);
                        updatedLine = this.tryUpdateTimerLine(line, timeDisplay);
                        if (updatedLine != null && !updatedLine.equals(line)) {
                           updatedIndex = i;
                           needsUpdate = true;
                           this.timerLineIndexCache.put(spawnerId, i);
                           break;
                        }
                     }
                  }

                  if (needsUpdate && updatedIndex >= 0) {
                     lore.set(updatedIndex, updatedLine);
                     meta.setLore(lore);
                     spawnerItem.setItemMeta(meta);
                     inventory.setItem(spawnerInfoSlot, spawnerItem);
                  }

               }
            }
         }
      }
   }

   private String tryUpdateTimerLine(String line, String timeDisplay) {
      if (line.contains("{time}")) {
         return line.replace("{time}", timeDisplay);
      } else if (line.indexOf(58) == -1) {
         return null;
      } else {
         String updatedLine = this.updateExistingTimerLine(line, timeDisplay);
         return !updatedLine.equals(line) ? updatedLine : null;
      }
   }

   private String updateExistingTimerLine(String line, String newTimeDisplay) {
      int colonIndex = line.indexOf(58);
      if (colonIndex == -1) {
         return line;
      } else {
         return TIMER_PATTERN.matcher(line).find() ? TIMER_PATTERN.matcher(line).replaceFirst(newTimeDisplay) : line;
      }
   }

   private boolean isValidGuiSession(Player player) {
      return player != null && player.isOnline();
   }

   public void clearPlayerTracking(UUID playerId) {
      this.lastTimerUpdate.remove(playerId);
      this.lastTimerValue.remove(playerId);
   }

   public void clearAllTracking() {
      this.lastTimerUpdate.clear();
      this.lastTimerValue.clear();
   }

   private static class PlayerViewerContext {
      final UUID playerId;
      final Player player;
      final Inventory inventory;
      final Location location;

      PlayerViewerContext(UUID playerId, Player player, Inventory inventory, Location location) {
         this.playerId = playerId;
         this.player = player;
         this.inventory = inventory;
         this.location = location;
      }
   }
}
