package github.nighter.smartspawner.spawner.lootgen;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.gui.synchronization.SpawnerGuiViewManager;
import github.nighter.smartspawner.spawner.lootgen.loot.LootItem;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import github.nighter.smartspawner.spawner.properties.VirtualInventory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class SpawnerLootGenerator {
   private final SmartSpawner plugin;
   private final SpawnerGuiViewManager spawnerGuiViewManager;
   private final SpawnerManager spawnerManager;
   private final Random random;

   public SpawnerLootGenerator(SmartSpawner plugin) {
      this.plugin = plugin;
      this.spawnerGuiViewManager = plugin.getSpawnerGuiViewManager();
      this.spawnerManager = plugin.getSpawnerManager();
      this.random = new Random();
   }

   public void spawnLootToSpawner(SpawnerData spawner) {
      boolean lockAcquired = spawner.getLootGenerationLock().tryLock();
      if (lockAcquired) {
         try {
            try {
               if (!spawner.getDataLock().tryLock(50L, TimeUnit.MILLISECONDS)) {
                  return;
               }
            } catch (InterruptedException var19) {
               Thread.currentThread().interrupt();
               return;
            }

            long currentTime = System.currentTimeMillis();

            long spawnTime;
            int minMobs;
            int maxMobs;
            AtomicInteger usedSlots;
            AtomicInteger maxSlots;
            try {
               usedSlots = new AtomicInteger(spawner.getVirtualInventory().getUsedSlots());
               maxSlots = new AtomicInteger(spawner.getMaxSpawnerLootSlots());
               if (usedSlots.get() >= maxSlots.get() && spawner.getSpawnerExp() >= spawner.getMaxStoredExp()) {
                  if (!spawner.getIsAtCapacity()) {
                     spawner.setIsAtCapacity(true);
                  }

                  return;
               }

               minMobs = spawner.getMinMobs();
               maxMobs = spawner.getMaxMobs();
               spawnTime = currentTime;
            } finally {
               spawner.getDataLock().unlock();
            }

            Scheduler.runTaskAsync(() -> {
               LootResult loot = this.generateLoot(minMobs, maxMobs, spawner);
               if (!loot.items().isEmpty() || loot.experience() != 0) {
                  Scheduler.runLocationTask(spawner.getSpawnerLocation(), () -> {
                     boolean updateLockAcquired = spawner.getLootGenerationLock().tryLock();
                     if (updateLockAcquired) {
                        try {
                           boolean changed = false;
                           int totalRequiredSlots;
                           if (loot.experience() > 0 && spawner.getSpawnerExp() < spawner.getMaxStoredExp()) {
                              int currentExp = spawner.getSpawnerExp();
                              totalRequiredSlots = spawner.getMaxStoredExp();
                              int newExp = Math.min(currentExp + loot.experience(), totalRequiredSlots);
                              if (newExp != currentExp) {
                                 spawner.setSpawnerExp(newExp);
                                 changed = true;
                              }
                           }

                           maxSlots.set(spawner.getMaxSpawnerLootSlots());
                           usedSlots.set(spawner.getVirtualInventory().getUsedSlots());
                           if (!loot.items().isEmpty() && usedSlots.get() < maxSlots.get()) {
                              List<ItemStack> itemsToAdd = new ArrayList(loot.items());
                              totalRequiredSlots = this.calculateRequiredSlots((List)itemsToAdd, spawner.getVirtualInventory());
                              if (totalRequiredSlots > maxSlots.get()) {
                                 itemsToAdd = this.limitItemsToAvailableSlots((List)itemsToAdd, spawner);
                              }

                              if (!((List)itemsToAdd).isEmpty()) {
                                 spawner.addItemsAndUpdateSellValue((List)itemsToAdd);
                                 changed = true;
                              }
                           }

                           if (changed) {
                              boolean updateDataLockAcquired = spawner.getDataLock().tryLock();
                              if (updateDataLockAcquired) {
                                 try {
                                    spawner.setLastSpawnTime(spawnTime);
                                 } finally {
                                    spawner.getDataLock().unlock();
                                 }
                              }

                              spawner.updateCapacityStatus();
                              this.handleGuiUpdates(spawner);
                              this.spawnerManager.markSpawnerModified(spawner.getSpawnerId());
                              return;
                           }
                        } finally {
                           spawner.getLootGenerationLock().unlock();
                        }

                     }
                  });
               }
            });
         } finally {
            spawner.getLootGenerationLock().unlock();
         }

      }
   }

   public LootResult generateLoot(int minMobs, int maxMobs, SpawnerData spawner) {
      int mobCount = this.random.nextInt(maxMobs - minMobs + 1) + minMobs;
      int totalExperience = spawner.getEntityExperienceValue() * mobCount;
      List<LootItem> validItems = spawner.getValidLootItems();
      if (validItems.isEmpty()) {
         return new LootResult(Collections.emptyList(), totalExperience);
      } else {
         Map<ItemStack, Integer> consolidatedLoot = new HashMap();
         Iterator var8 = validItems.iterator();

         while(true) {
            LootItem lootItem;
            int successfulDrops;
            int totalAmount;
            ItemStack prototype;
            do {
               do {
                  if (!var8.hasNext()) {
                     List<ItemStack> finalLoot = new ArrayList(consolidatedLoot.size());
                     Iterator var15 = consolidatedLoot.entrySet().iterator();

                     while(var15.hasNext()) {
                        Entry<ItemStack, Integer> entry = (Entry)var15.next();
                        prototype = ((ItemStack)entry.getKey()).clone();
                        prototype.setAmount(Math.min((Integer)entry.getValue(), prototype.getMaxStackSize()));
                        finalLoot.add(prototype);

                        ItemStack extraStack;
                        for(totalAmount = (Integer)entry.getValue() - prototype.getMaxStackSize(); totalAmount > 0; totalAmount -= extraStack.getAmount()) {
                           extraStack = prototype.clone();
                           extraStack.setAmount(Math.min(totalAmount, prototype.getMaxStackSize()));
                           finalLoot.add(extraStack);
                        }
                     }

                     return new LootResult(finalLoot, totalExperience);
                  }

                  lootItem = (LootItem)var8.next();
                  successfulDrops = 0;

                  for(int i = 0; i < mobCount; ++i) {
                     if (this.random.nextDouble() * 100.0D <= lootItem.chance()) {
                        ++successfulDrops;
                     }
                  }
               } while(successfulDrops <= 0);

               prototype = lootItem.createItemStack(this.random);
            } while(prototype == null);

            totalAmount = 0;

            for(int i = 0; i < successfulDrops; ++i) {
               totalAmount += lootItem.generateAmount(this.random);
            }

            if (totalAmount > 0) {
               consolidatedLoot.merge(prototype, totalAmount, Integer::sum);
            }
         }
      }
   }

   private List<ItemStack> limitItemsToAvailableSlots(List<ItemStack> items, SpawnerData spawner) {
      VirtualInventory currentInventory = spawner.getVirtualInventory();
      int maxSlots = spawner.getMaxSpawnerLootSlots();
      if (currentInventory.getUsedSlots() >= maxSlots) {
         return Collections.emptyList();
      } else {
         Map<VirtualInventory.ItemSignature, Long> simulatedInventory = new HashMap(currentInventory.getConsolidatedItems());
         List<ItemStack> acceptedItems = new ArrayList();
         items.sort(Comparator.comparing((itemx) -> {
            return itemx.getType().name();
         }));
         Iterator var7 = items.iterator();

         while(var7.hasNext()) {
            ItemStack item = (ItemStack)var7.next();
            if (item != null && item.getAmount() > 0) {
               Map<VirtualInventory.ItemSignature, Long> tempSimulation = new HashMap(simulatedInventory);
               VirtualInventory.ItemSignature sig = VirtualInventory.getSignature(item);
               tempSimulation.merge(sig, (long)item.getAmount(), Long::sum);
               int slotsNeeded = this.calculateSlots(tempSimulation);
               if (slotsNeeded > maxSlots) {
                  int maxStackSize = item.getMaxStackSize();
                  long currentAmount = (Long)simulatedInventory.getOrDefault(sig, 0L);
                  int remainingSlots = maxSlots - this.calculateSlots(simulatedInventory);
                  if (remainingSlots > 0) {
                     long maxAddAmount = (long)remainingSlots * (long)maxStackSize - currentAmount % (long)maxStackSize;
                     if (maxAddAmount > 0L) {
                        ItemStack partialItem = item.clone();
                        partialItem.setAmount((int)Math.min(maxAddAmount, (long)item.getAmount()));
                        acceptedItems.add(partialItem);
                        simulatedInventory.merge(sig, (long)partialItem.getAmount(), Long::sum);
                     }
                  }
                  break;
               }

               acceptedItems.add(item);
               simulatedInventory = tempSimulation;
            }
         }

         return acceptedItems;
      }
   }

   private int calculateSlots(Map<VirtualInventory.ItemSignature, Long> items) {
      return items.entrySet().stream().mapToInt((entry) -> {
         long amount = (Long)entry.getValue();
         int maxStackSize = ((VirtualInventory.ItemSignature)entry.getKey()).getTemplateRef().getMaxStackSize();
         return (int)((amount + (long)maxStackSize - 1L) / (long)maxStackSize);
      }).sum();
   }

   private int calculateRequiredSlots(List<ItemStack> items, VirtualInventory inventory) {
      Map<VirtualInventory.ItemSignature, Long> simulatedItems = new HashMap();
      if (inventory != null) {
         simulatedItems.putAll(inventory.getConsolidatedItems());
      }

      Iterator var4 = items.iterator();

      while(var4.hasNext()) {
         ItemStack item = (ItemStack)var4.next();
         if (item != null && item.getAmount() > 0) {
            VirtualInventory.ItemSignature sig = VirtualInventory.getSignature(item);
            simulatedItems.merge(sig, (long)item.getAmount(), Long::sum);
         }
      }

      return this.calculateSlots(simulatedItems);
   }

   private void handleGuiUpdates(SpawnerData spawner) {
      this.spawnerGuiViewManager.updateSpawnerMenuViewers(spawner);
      if (this.plugin.getConfig().getBoolean("particle.spawner_generate_loot", true)) {
         Location loc = spawner.getSpawnerLocation();
         World world = loc.getWorld();
         if (world != null) {
            Scheduler.runLocationTask(loc, () -> {
               world.spawnParticle(Particle.HAPPY_VILLAGER, loc.clone().add(0.5D, 0.5D, 0.5D), 10, 0.3D, 0.3D, 0.3D, 0.0D);
            });
         }
      }

      if (this.plugin.getConfig().getBoolean("hologram.enabled", false)) {
         spawner.updateHologramData();
      }

   }

   public void preGenerateLoot(SpawnerData spawner, SpawnerLootGenerator.LootGenerationCallback callback) {
      if (!spawner.getLootGenerationLock().tryLock()) {
         callback.onLootGenerated(Collections.emptyList(), 0);
      } else {
         try {
            try {
               if (!spawner.getDataLock().tryLock(50L, TimeUnit.MILLISECONDS)) {
                  callback.onLootGenerated(Collections.emptyList(), 0);
                  return;
               }
            } catch (InterruptedException var16) {
               Thread.currentThread().interrupt();
               callback.onLootGenerated(Collections.emptyList(), 0);
               return;
            }

            int minMobs;
            int maxMobs;
            try {
               int usedSlots = spawner.getVirtualInventory().getUsedSlots();
               int maxSlots = spawner.getMaxSpawnerLootSlots();
               boolean atCapacity = usedSlots >= maxSlots && spawner.getSpawnerExp() >= spawner.getMaxStoredExp();
               if (atCapacity) {
                  callback.onLootGenerated(Collections.emptyList(), 0);
                  return;
               }

               minMobs = spawner.getMinMobs();
               maxMobs = spawner.getMaxMobs();
            } finally {
               spawner.getDataLock().unlock();
            }

            Scheduler.runTaskAsync(() -> {
               LootResult loot = this.generateLoot(minMobs, maxMobs, spawner);
               callback.onLootGenerated((List)(loot.items() != null ? new ArrayList(loot.items()) : Collections.emptyList()), loot.experience());
            });
         } finally {
            spawner.getLootGenerationLock().unlock();
         }
      }
   }

   public void addPreGeneratedLoot(SpawnerData spawner, List<ItemStack> items, int experience) {
      this.addPreGeneratedLoot(spawner, items, experience, System.currentTimeMillis());
   }

   public void addPreGeneratedLoot(SpawnerData spawner, List<ItemStack> items, int experience, long spawnTime) {
      if (items != null && !items.isEmpty() || experience != 0) {
         Location spawnerLocation = spawner.getSpawnerLocation();
         if (spawnerLocation != null) {
            Scheduler.runLocationTask(spawnerLocation, () -> {
               if (spawner.getLootGenerationLock().tryLock()) {
                  try {
                     try {
                        if (!spawner.getDataLock().tryLock(50L, TimeUnit.MILLISECONDS)) {
                           return;
                        }
                     } catch (InterruptedException var18) {
                        Thread.currentThread().interrupt();
                        return;
                     }

                     try {
                        int usedSlots = spawner.getVirtualInventory().getUsedSlots();
                        int maxSlots = spawner.getMaxSpawnerLootSlots();
                        boolean capacityCheck = usedSlots >= maxSlots && spawner.getSpawnerExp() >= spawner.getMaxStoredExp();
                        if (capacityCheck) {
                           return;
                        }
                     } finally {
                        spawner.getDataLock().unlock();
                     }

                     Scheduler.runTaskAsync(() -> {
                        boolean changed = false;
                        if (experience > 0 && spawner.getSpawnerExp() < spawner.getMaxStoredExp()) {
                           int currentExp = spawner.getSpawnerExp();
                           int maxExp = spawner.getMaxStoredExp();
                           int newExp = Math.min(currentExp + experience, maxExp);
                           if (newExp != currentExp) {
                              spawner.setSpawnerExp(newExp);
                              changed = true;
                           }
                        }

                        if (items != null && !items.isEmpty()) {
                           List<ItemStack> validItems = new ArrayList();
                           Iterator var14 = items.iterator();

                           while(var14.hasNext()) {
                              ItemStack item = (ItemStack)var14.next();
                              if (item != null && item.getType() != Material.AIR) {
                                 validItems.add(item.clone());
                              }
                           }

                           if (!validItems.isEmpty()) {
                              spawner.addItemsAndUpdateSellValue(validItems);
                              changed = true;
                           }
                        }

                        if (changed) {
                           if (spawner.getDataLock().tryLock()) {
                              try {
                                 spawner.setLastSpawnTime(spawnTime);
                              } finally {
                                 spawner.getDataLock().unlock();
                              }
                           }

                           spawner.updateCapacityStatus();
                           this.handleGuiUpdates(spawner);
                           this.spawnerManager.markSpawnerModified(spawner.getSpawnerId());
                        }
                     });
                  } finally {
                     spawner.getLootGenerationLock().unlock();
                  }
               }
            });
         }
      }
   }

   @FunctionalInterface
   public interface LootGenerationCallback {
      void onLootGenerated(List<ItemStack> var1, int var2);
   }
}
