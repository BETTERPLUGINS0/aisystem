package ac.grim.grimac.utils.inventory.inventory;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.inventory.ClickAction;
import ac.grim.grimac.utils.inventory.Inventory;
import ac.grim.grimac.utils.inventory.slot.ResultSlot;
import ac.grim.grimac.utils.inventory.slot.Slot;
import ac.grim.grimac.utils.math.GrimMath;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.Generated;

public abstract class AbstractContainerMenu {
   protected final GrimPlayer player;
   private int quickcraftStatus = 0;
   private int quickcraftType = -1;
   private final Set<Slot> quickcraftSlots = Sets.newHashSet();
   private Inventory playerInventory;
   protected final List<Slot> slots = new ArrayList();
   @NotNull
   private ItemStack carriedItem;

   public AbstractContainerMenu(GrimPlayer player, Inventory playerInventory) {
      this.carriedItem = ItemStack.EMPTY;
      this.player = player;
      this.playerInventory = playerInventory;
   }

   public AbstractContainerMenu(GrimPlayer player) {
      this.carriedItem = ItemStack.EMPTY;
      this.player = player;
   }

   public static int calculateQuickcraftHeader(int p_38948_) {
      return p_38948_ & 3;
   }

   public static int calculateQuickcraftMask(int p_38931_, int p_38932_) {
      return p_38931_ & 3 | (p_38932_ & 3) << 2;
   }

   public static int calculateQuickcraftType(int p_38929_) {
      return p_38929_ >> 2 & 3;
   }

   public static boolean canItemQuickReplace(@Nullable Slot p_38900_, ItemStack p_38901_, boolean p_38902_) {
      boolean flag = p_38900_ == null || !p_38900_.hasItem();
      if (!flag && ItemStack.isSameItemSameTags(p_38901_, p_38900_.getItem())) {
         return p_38900_.getItem().getAmount() + (p_38902_ ? 0 : p_38901_.getAmount()) <= p_38901_.getMaxStackSize();
      } else {
         return flag;
      }
   }

   public static void getQuickCraftSlotCount(Set<Slot> p_38923_, int p_38924_, ItemStack p_38925_, int p_38926_) {
      switch(p_38924_) {
      case 0:
         p_38925_.setAmount(GrimMath.floor((double)((float)p_38925_.getAmount() / (float)p_38923_.size())));
         break;
      case 1:
         p_38925_.setAmount(1);
         break;
      case 2:
         p_38925_.setAmount(p_38925_.getType().getMaxAmount());
      }

      p_38925_.grow(p_38926_);
   }

   public Slot addSlot(Slot slot) {
      slot.slotListIndex = this.slots.size();
      this.slots.add(slot);
      return slot;
   }

   public void addFourRowPlayerInventory() {
      for(int slot = 9; slot < 45; ++slot) {
         this.addSlot(new Slot(this.playerInventory.getInventoryStorage(), slot));
      }

   }

   protected void resetQuickCraft() {
      this.quickcraftStatus = 0;
      this.quickcraftSlots.clear();
   }

   public boolean isValidQuickcraftType(int p_38863_) {
      if (p_38863_ == 0) {
         return true;
      } else if (p_38863_ == 1) {
         return true;
      } else {
         return p_38863_ == 2 && this.player.gamemode == GameMode.CREATIVE;
      }
   }

   public ItemStack getCarried() {
      return this.getCarriedItem();
   }

   public void setCarried(ItemStack stack) {
      this.carriedItem = stack == null ? ItemStack.EMPTY : stack;
   }

   public ItemStack getPlayerInventoryItem(int slot) {
      return this.playerInventory.getInventoryStorage().getItem(slot);
   }

   public void setPlayerInventoryItem(int slot, ItemStack stack) {
      this.playerInventory.getInventoryStorage().setItem(slot, stack);
   }

   public void doClick(int button, int slotID, WrapperPlayClientClickWindow.WindowClickType clickType) {
      ItemStack itemstack3;
      ItemStack slotItem;
      ItemStack itemstack2;
      int k1;
      Slot slot;
      int j1;
      if (clickType == WrapperPlayClientClickWindow.WindowClickType.QUICK_CRAFT) {
         int i = this.quickcraftStatus;
         this.quickcraftStatus = calculateQuickcraftHeader(button);
         if ((i != 1 || this.quickcraftStatus != 2) && i != this.quickcraftStatus) {
            this.resetQuickCraft();
         } else if (this.getCarried().isEmpty()) {
            this.resetQuickCraft();
         } else if (this.quickcraftStatus == 0) {
            this.quickcraftType = calculateQuickcraftType(button);
            if (this.isValidQuickcraftType(this.quickcraftType)) {
               this.quickcraftStatus = 1;
               this.quickcraftSlots.clear();
            } else {
               this.resetQuickCraft();
            }
         } else if (this.quickcraftStatus == 1) {
            if (slotID < 0) {
               return;
            }

            slot = (Slot)this.slots.get(slotID);
            slotItem = this.getCarried();
            if (canItemQuickReplace(slot, slotItem, true) && slot.mayPlace(slotItem) && (this.quickcraftType == 2 || slotItem.getAmount() > this.quickcraftSlots.size()) && this.canDragTo(slot)) {
               this.quickcraftSlots.add(slot);
            }
         } else if (this.quickcraftStatus == 2) {
            if (!this.quickcraftSlots.isEmpty()) {
               if (this.quickcraftSlots.size() == 1) {
                  k1 = ((Slot)this.quickcraftSlots.iterator().next()).slotListIndex;
                  this.resetQuickCraft();
                  this.doClick(this.quickcraftType, k1, WrapperPlayClientClickWindow.WindowClickType.PICKUP);
                  return;
               }

               itemstack3 = this.getCarried().copy();
               j1 = this.getCarried().getAmount();
               Iterator var19 = this.quickcraftSlots.iterator();

               label295:
               while(true) {
                  ItemStack itemstack1;
                  Slot slot1;
                  do {
                     do {
                        do {
                           do {
                              if (!var19.hasNext()) {
                                 itemstack3.setAmount(j1);
                                 this.setCarried(itemstack3);
                                 break label295;
                              }

                              slot1 = (Slot)var19.next();
                              itemstack1 = this.getCarried();
                           } while(slot1 == null);
                        } while(!canItemQuickReplace(slot1, itemstack1, true));
                     } while(!slot1.mayPlace(itemstack1));
                  } while(this.quickcraftType != 2 && itemstack1.getAmount() < this.quickcraftSlots.size());

                  if (this.canDragTo(slot1)) {
                     itemstack2 = itemstack3.copy();
                     int j = slot1.hasItem() ? slot1.getItem().getAmount() : 0;
                     getQuickCraftSlotCount(this.quickcraftSlots, this.quickcraftType, itemstack2, j);
                     int k = Math.min(itemstack2.getMaxStackSize(), slot1.getMaxStackSize(itemstack2));
                     if (itemstack2.getAmount() > k) {
                        itemstack2.setAmount(k);
                     }

                     j1 -= itemstack2.getAmount() - j;
                     slot1.set(itemstack2);
                  }
               }
            }

            this.resetQuickCraft();
         } else {
            this.resetQuickCraft();
         }
      } else if (this.quickcraftStatus != 0) {
         this.resetQuickCraft();
      } else {
         int j3;
         if ((clickType == WrapperPlayClientClickWindow.WindowClickType.PICKUP || clickType == WrapperPlayClientClickWindow.WindowClickType.QUICK_MOVE) && (button == 0 || button == 1)) {
            ClickAction clickAction = ClickAction.values()[button];
            if (slotID == -999) {
               if (!this.getCarried().isEmpty()) {
                  if (clickAction == ClickAction.PRIMARY) {
                     this.setCarried(ItemStack.EMPTY);
                  } else {
                     this.getCarried().split(1);
                  }
               }
            } else if (clickType == WrapperPlayClientClickWindow.WindowClickType.QUICK_MOVE) {
               if (slotID < 0) {
                  return;
               }

               slot = this.getSlot(slotID);
               if (!slot.mayPickup()) {
                  return;
               }

               for(slotItem = this.quickMoveStack(slotID); !slotItem.isEmpty() && ItemStack.isSameItemSameTags(slot.getItem(), slotItem); slotItem = this.quickMoveStack(slotID)) {
               }
            } else {
               if (slotID < 0) {
                  return;
               }

               slot = this.getSlot(slotID);
               slotItem = slot.getItem();
               ItemStack carriedItem = this.getCarried();
               if (slot instanceof ResultSlot) {
                  this.player.inventory.isPacketInventoryActive = false;
               }

               if (slotItem.isEmpty()) {
                  if (!carriedItem.isEmpty()) {
                     j3 = clickAction == ClickAction.PRIMARY ? carriedItem.getAmount() : 1;
                     this.setCarried(slot.safeInsert(carriedItem, j3));
                  }
               } else if (slot.mayPickup()) {
                  if (carriedItem.isEmpty()) {
                     j3 = clickAction == ClickAction.PRIMARY ? slotItem.getAmount() : (slotItem.getAmount() + 1) / 2;
                     Optional<ItemStack> optional1 = slot.tryRemove(j3, Integer.MAX_VALUE, this.player);
                     optional1.ifPresent((p_150421_) -> {
                        this.setCarried(p_150421_);
                        slot.onTake(this.player, p_150421_);
                     });
                  } else if (slot.mayPlace(carriedItem)) {
                     if (ItemStack.isSameItemSameTags(slotItem, carriedItem)) {
                        j3 = clickAction == ClickAction.PRIMARY ? carriedItem.getAmount() : 1;
                        this.setCarried(slot.safeInsert(carriedItem, j3));
                     } else if (carriedItem.getAmount() <= slot.getMaxStackSize(carriedItem)) {
                        slot.set(carriedItem);
                        this.setCarried(slotItem);
                     }
                  } else if (ItemStack.isSameItemSameTags(slotItem, carriedItem)) {
                     Optional<ItemStack> optional = slot.tryRemove(slotItem.getAmount(), carriedItem.getMaxStackSize() - carriedItem.getAmount(), this.player);
                     optional.ifPresent((p_150428_) -> {
                        carriedItem.grow(p_150428_.getAmount());
                        slot.onTake(this.player, p_150428_);
                     });
                  }
               }
            }
         } else {
            Slot hoveringSlot;
            int k2;
            if (clickType == WrapperPlayClientClickWindow.WindowClickType.SWAP) {
               hoveringSlot = (Slot)this.slots.get(slotID);
               if (button != 40 && (button < 0 || button >= 9)) {
                  return;
               }

               button = button == 40 ? 45 : button + 36;
               itemstack3 = this.getPlayerInventoryItem(button);
               slotItem = hoveringSlot.getItem();
               if (!itemstack3.isEmpty() || !slotItem.isEmpty()) {
                  if (itemstack3.isEmpty()) {
                     if (hoveringSlot.mayPickup(this.player)) {
                        this.setPlayerInventoryItem(button, slotItem);
                        hoveringSlot.set(ItemStack.EMPTY);
                        hoveringSlot.onTake(this.player, slotItem);
                     }
                  } else if (slotItem.isEmpty()) {
                     if (hoveringSlot.mayPlace(itemstack3)) {
                        k2 = hoveringSlot.getMaxStackSize(itemstack3);
                        if (itemstack3.getAmount() > k2) {
                           hoveringSlot.set(itemstack3.split(k2));
                        } else {
                           hoveringSlot.set(itemstack3);
                           this.setPlayerInventoryItem(button, ItemStack.EMPTY);
                        }
                     }
                  } else if (hoveringSlot.mayPickup(this.player) && hoveringSlot.mayPlace(itemstack3)) {
                     k2 = hoveringSlot.getMaxStackSize(itemstack3);
                     if (itemstack3.getAmount() > k2) {
                        hoveringSlot.set(itemstack3.split(k2));
                        hoveringSlot.onTake(this.player, slotItem);
                        this.playerInventory.add(slotItem);
                     } else {
                        hoveringSlot.set(itemstack3);
                        this.setPlayerInventoryItem(button, slotItem);
                        hoveringSlot.onTake(this.player, slotItem);
                     }
                  }
               }
            } else if (clickType == WrapperPlayClientClickWindow.WindowClickType.CLONE && this.player.gamemode == GameMode.CREATIVE && slotID >= 0 && this.carriedItem.isEmpty()) {
               hoveringSlot = this.getSlot(slotID);
               if (hoveringSlot.hasItem()) {
                  itemstack3 = hoveringSlot.getItem().copy();
                  itemstack3.setAmount(itemstack3.getMaxStackSize());
                  this.setCarried(itemstack3);
               }
            } else if (clickType == WrapperPlayClientClickWindow.WindowClickType.THROW && this.getCarried().isEmpty() && slotID >= 0) {
               hoveringSlot = this.getSlot(slotID);
               k1 = button == 0 ? 1 : hoveringSlot.getItem().getAmount();
               hoveringSlot.safeTake(k1, Integer.MAX_VALUE, this.player);
            } else if (clickType == WrapperPlayClientClickWindow.WindowClickType.PICKUP_ALL && slotID >= 0) {
               hoveringSlot = this.getSlot(slotID);
               if (!this.getCarried().isEmpty() && (!hoveringSlot.hasItem() || !hoveringSlot.mayPickup(this.player))) {
                  k1 = button == 0 ? 0 : this.slots.size() - 1;
                  j1 = button == 0 ? 1 : -1;

                  for(k2 = 0; k2 < 2; ++k2) {
                     for(j3 = k1; j3 >= 0 && j3 < this.slots.size() && this.getCarried().getAmount() < this.getCarried().getMaxStackSize(); j3 += j1) {
                        Slot slot8 = (Slot)this.slots.get(j3);
                        if (slot8.hasItem() && canItemQuickReplace(slot8, this.getCarried(), true) && slot8.mayPickup(this.player) && this.canTakeItemForPickAll(this.getCarried(), slot8)) {
                           itemstack2 = slot8.getItem();
                           if (k2 != 0 || itemstack2.getAmount() != itemstack2.getMaxStackSize()) {
                              ItemStack itemstack13 = slot8.safeTake(itemstack2.getAmount(), this.getCarried().getMaxStackSize() - this.getCarried().getAmount(), this.player);
                              this.getCarried().grow(itemstack13.getAmount());
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   protected boolean moveItemStackTo(ItemStack toMove, int min, int max, boolean reverse) {
      boolean flag = false;
      int i = min;
      if (reverse) {
         i = max - 1;
      }

      Slot slot1;
      ItemStack itemstack;
      if (toMove.getType().getMaxAmount() > 1) {
         while(!toMove.isEmpty()) {
            if (reverse) {
               if (i < min) {
                  break;
               }
            } else if (i >= max) {
               break;
            }

            slot1 = (Slot)this.slots.get(i);
            itemstack = slot1.getItem();
            if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(toMove, itemstack)) {
               int j = itemstack.getAmount() + toMove.getAmount();
               if (j <= toMove.getMaxStackSize()) {
                  toMove.setAmount(0);
                  itemstack.setAmount(j);
                  flag = true;
               } else if (itemstack.getAmount() < toMove.getMaxStackSize()) {
                  toMove.shrink(toMove.getMaxStackSize() - itemstack.getAmount());
                  itemstack.setAmount(toMove.getMaxStackSize());
                  flag = true;
               }
            }

            if (reverse) {
               --i;
            } else {
               ++i;
            }
         }
      }

      if (!toMove.isEmpty()) {
         if (reverse) {
            i = max - 1;
         } else {
            i = min;
         }

         while(true) {
            if (reverse) {
               if (i < min) {
                  break;
               }
            } else if (i >= max) {
               break;
            }

            slot1 = (Slot)this.slots.get(i);
            itemstack = slot1.getItem();
            if (itemstack.isEmpty() && slot1.mayPlace(toMove)) {
               if (toMove.getAmount() > slot1.getMaxStackSize()) {
                  slot1.set(toMove.split(slot1.getMaxStackSize()));
               } else {
                  slot1.set(toMove.split(toMove.getAmount()));
               }

               flag = true;
               break;
            }

            if (reverse) {
               --i;
            } else {
               ++i;
            }
         }
      }

      return flag;
   }

   public boolean canTakeItemForPickAll(ItemStack p_38908_, Slot p_38909_) {
      return true;
   }

   public ItemStack quickMoveStack(int slotID) {
      return ((Slot)this.slots.get(slotID)).getItem();
   }

   public Slot getSlot(int slotID) {
      try {
         return (Slot)this.slots.get(slotID);
      } catch (IndexOutOfBoundsException var3) {
         LogUtil.error("Tried to get slot " + slotID + " in a container with only " + this.slots.size() + " slots, container type: " + this.getClass().getName(), var3);
         throw var3;
      }
   }

   public boolean canDragTo(Slot slot) {
      return true;
   }

   public int getMaxStackSize() {
      return 64;
   }

   @Generated
   protected void setPlayerInventory(Inventory playerInventory) {
      this.playerInventory = playerInventory;
   }

   @Generated
   public List<Slot> getSlots() {
      return this.slots;
   }

   @NotNull
   @Generated
   public ItemStack getCarriedItem() {
      return this.carriedItem;
   }
}
