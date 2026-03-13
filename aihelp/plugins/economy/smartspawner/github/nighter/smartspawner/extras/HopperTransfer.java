package github.nighter.smartspawner.extras;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.gui.synchronization.SpawnerGuiViewManager;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import github.nighter.smartspawner.spawner.properties.VirtualInventory;
import github.nighter.smartspawner.utils.BlockPos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HopperTransfer {
   private final SmartSpawner plugin;
   private final SpawnerManager spawnerManager;
   private final SpawnerGuiViewManager guiManager;

   public HopperTransfer(SmartSpawner plugin) {
      this.plugin = plugin;
      this.spawnerManager = plugin.getSpawnerManager();
      this.guiManager = plugin.getSpawnerGuiViewManager();
   }

   public void process(BlockPos hopperPos) {
      Location hopperLoc = hopperPos.toLocation();
      if (hopperLoc != null) {
         Block hopperBlock = hopperLoc.getBlock();
         if (hopperBlock.getType() == Material.HOPPER) {
            Block spawnerBlock = hopperBlock.getRelative(BlockFace.UP);
            if (spawnerBlock.getType() == Material.SPAWNER) {
               this.transferItems(hopperLoc, spawnerBlock.getLocation());
            }
         }
      }
   }

   private void transferItems(Location hopperLoc, Location spawnerLoc) {
      SpawnerData spawner = this.spawnerManager.getSpawnerByLocation(spawnerLoc);
      if (spawner != null) {
         ReentrantLock lock = spawner.getInventoryLock();
         if (lock.tryLock()) {
            try {
               VirtualInventory virtualInv = spawner.getVirtualInventory();
               if (virtualInv == null) {
                  return;
               }

               BlockState state = hopperLoc.getBlock().getState(false);
               if (!(state instanceof Hopper)) {
                  return;
               }

               Hopper hopper = (Hopper)state;
               Map<Integer, ItemStack> displayItems = virtualInv.getDisplayInventory();
               if (displayItems != null && !displayItems.isEmpty()) {
                  Inventory hopperInv = hopper.getInventory();
                  int transferred = 0;
                  List<ItemStack> removed = new ArrayList();
                  Iterator var12 = displayItems.values().iterator();

                  while(var12.hasNext()) {
                     ItemStack item = (ItemStack)var12.next();
                     if (transferred >= this.plugin.getHopperConfig().getStackPerTransfer()) {
                        break;
                     }

                     if (item != null && item.getType() != Material.AIR) {
                        ItemStack clone = item.clone();
                        int originalAmount = clone.getAmount();
                        HashMap<Integer, ItemStack> leftovers = hopperInv.addItem(new ItemStack[]{clone});
                        int insertedAmount = originalAmount;
                        if (!leftovers.isEmpty()) {
                           insertedAmount = originalAmount - ((ItemStack)leftovers.values().iterator().next()).getAmount();
                        }

                        if (insertedAmount > 0) {
                           ItemStack toRemove = item.clone();
                           toRemove.setAmount(insertedAmount);
                           removed.add(toRemove);
                           ++transferred;
                        }
                     }
                  }

                  if (!removed.isEmpty()) {
                     spawner.removeItemsAndUpdateSellValue(removed);
                     this.guiManager.updateSpawnerMenuViewers(spawner);
                  }

                  return;
               }
            } catch (Exception var22) {
               this.plugin.getLogger().log(Level.WARNING, "Error transferring items from spawner to hopper at " + String.valueOf(hopperLoc), var22);
               return;
            } finally {
               lock.unlock();
            }

         }
      }
   }
}
