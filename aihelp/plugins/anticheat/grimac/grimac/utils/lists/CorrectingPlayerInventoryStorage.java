package ac.grim.grimac.utils.lists;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.utils.inventory.InventoryStorage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class CorrectingPlayerInventoryStorage extends InventoryStorage {
   private static final Set<String> SUPPORTED_INVENTORIES = new HashSet(Arrays.asList("CHEST", "DISPENSER", "DROPPER", "PLAYER", "ENDER_CHEST", "SHULKER_BOX", "BARREL", "CRAFTING", "CREATIVE"));
   private final GrimPlayer player;
   private final Map<Integer, Integer> serverIsCurrentlyProcessingThesePredictions = new ConcurrentHashMap();
   private final Map<Integer, Integer> pendingFinalizedSlot = new ConcurrentHashMap();

   public CorrectingPlayerInventoryStorage(GrimPlayer player, int size) {
      super(size);
      this.player = player;
   }

   public void handleClientClaimedSlotSet(int slotID) {
      if (slotID >= 0 && slotID <= 45) {
         this.pendingFinalizedSlot.put(slotID, GrimAPI.INSTANCE.getTickManager().currentTick + 5);
      }

   }

   public void handleServerCorrectSlot(int slotID) {
      if (slotID >= 0 && slotID <= 45) {
         this.serverIsCurrentlyProcessingThesePredictions.put(slotID, this.player.lastTransactionSent.get());
      }

   }

   public void setItem(int item, ItemStack stack) {
      int finalTransaction = (Integer)this.serverIsCurrentlyProcessingThesePredictions.getOrDefault(item, -1);
      if (finalTransaction == -1 || this.player.lastTransactionReceived.get() >= finalTransaction) {
         this.pendingFinalizedSlot.put(item, GrimAPI.INSTANCE.getTickManager().currentTick + 5);
         this.serverIsCurrentlyProcessingThesePredictions.remove(item);
      }

      super.setItem(item, stack);
   }

   private void checkThatBukkitIsSynced(int slot) {
      if (this.player.platformPlayer != null) {
         if (this.player.inventory.isPacketInventoryActive) {
            int bukkitSlot = this.player.inventory.getBukkitSlot(slot);
            if (bukkitSlot != -1) {
               ItemStack existing = this.getItem(slot);
               ItemStack toPE = this.player.platformPlayer.getInventory().getStack(bukkitSlot, slot);
               if (existing.getType() != toPE.getType() || existing.getAmount() != toPE.getAmount()) {
                  GrimAPI.INSTANCE.getScheduler().getEntityScheduler().execute(this.player.platformPlayer, GrimAPI.INSTANCE.getGrimPlugin(), () -> {
                     this.player.platformPlayer.updateInventory();
                  }, (Runnable)null, 0L);
                  this.setItem(slot, toPE);
               }
            }

         }
      }
   }

   public void tickWithBukkit() {
      if (this.player.platformPlayer != null) {
         int tickID = GrimAPI.INSTANCE.getTickManager().currentTick;
         Iterator it = this.pendingFinalizedSlot.entrySet().iterator();

         while(it.hasNext()) {
            Entry<Integer, Integer> entry = (Entry)it.next();
            if ((Integer)entry.getValue() <= tickID) {
               this.checkThatBukkitIsSynced((Integer)entry.getKey());
               it.remove();
            }
         }

         if (this.player.inventory.needResend) {
            GrimAPI.INSTANCE.getScheduler().getEntityScheduler().execute(this.player.platformPlayer, GrimAPI.INSTANCE.getGrimPlugin(), () -> {
               if (this.player.inventory.needResend) {
                  if (SUPPORTED_INVENTORIES.contains(this.player.platformPlayer.getInventory().getOpenInventoryKey().toUpperCase(Locale.ROOT))) {
                     this.player.inventory.needResend = false;
                     this.player.platformPlayer.updateInventory();
                  }

               }
            }, (Runnable)null, 0L);
         }

         if (tickID % 5 == 0) {
            int slotToCheck = tickID / 5 % this.getSize();
            if (!this.pendingFinalizedSlot.containsKey(slotToCheck) && !this.serverIsCurrentlyProcessingThesePredictions.containsKey(slotToCheck)) {
               this.checkThatBukkitIsSynced(slotToCheck);
            }
         }

      }
   }
}
