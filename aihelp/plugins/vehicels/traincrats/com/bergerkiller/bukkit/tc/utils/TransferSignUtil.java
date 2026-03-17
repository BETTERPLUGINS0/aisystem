package com.bergerkiller.bukkit.tc.utils;

import com.bergerkiller.bukkit.common.bases.IntVector2;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartChest;
import com.bergerkiller.bukkit.common.inventory.CommonItemStack;
import com.bergerkiller.bukkit.common.inventory.InventoryBase;
import com.bergerkiller.bukkit.common.inventory.ItemParser;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.RecipeUtil;
import com.bergerkiller.bukkit.tc.InteractType;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberChest;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.itemanimation.ItemAnimatedInventory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class TransferSignUtil {
   private static final HashSet<InventoryHolder> chestsBuffer = new HashSet();

   public static Inventory getInventory(SignActionEvent info) {
      if (info.isCartSign()) {
         return info.getMember() instanceof MinecartMemberChest ? ((CommonMinecartChest)((MinecartMemberChest)info.getMember()).getEntity()).getInventory() : null;
      } else {
         return info.getGroup().getInventory();
      }
   }

   public static Collection<InventoryHolder> getInventories(SignActionEvent info) {
      if (info.isCartSign()) {
         return info.getMember() instanceof MinecartMemberChest ? Collections.singletonList((InventoryHolder)((CommonMinecart)info.getMember().getEntity()).getEntity()) : Collections.emptyList();
      } else {
         Collection<InventoryHolder> trainInvs = new ArrayList(info.getGroup().size());
         Iterator var2 = info.getGroup().iterator();

         while(var2.hasNext()) {
            MinecartMember<?> member = (MinecartMember)var2.next();
            if (member instanceof MinecartMemberChest) {
               trainInvs.add((InventoryHolder)((CommonMinecart)member.getEntity()).getEntity());
            }
         }

         return trainInvs;
      }
   }

   public static int depositInFurnace(TrainCarts traincarts, Inventory from, Furnace toFurnace, ItemParser parser, boolean isFuelPreferred) {
      Inventory to = toFurnace.getInventory();
      List<ItemParser> heatables = new ArrayList();
      List<ItemParser> fuels = new ArrayList();
      int amountToTransfer;
      ItemParser p;
      if (!parser.hasType()) {
         ItemParser[] var8 = traincarts.getParsers("heatable", 1);
         amountToTransfer = var8.length;

         int var10;
         for(var10 = 0; var10 < amountToTransfer; ++var10) {
            p = var8[var10];
            if (p == null || !p.hasType()) {
               heatables.clear();
               break;
            }

            heatables.add(p);
         }

         var8 = traincarts.getParsers("fuel", 1);
         amountToTransfer = var8.length;

         for(var10 = 0; var10 < amountToTransfer; ++var10) {
            p = var8[var10];
            if (p == null || !p.hasType()) {
               fuels.clear();
               break;
            }

            fuels.add(p);
         }

         if (heatables.isEmpty() && fuels.isEmpty()) {
            return 0;
         }
      } else {
         ItemStack parseritem = parser.getItemStack(1);
         boolean heatable = RecipeUtil.isHeatableItem(parseritem);
         boolean fuel = RecipeUtil.isFuelItem(parseritem);
         if (heatable && fuel) {
            if (isFuelPreferred) {
               fuels.add(parser);
            } else {
               heatables.add(parser);
            }
         } else if (heatable) {
            heatables.add(parser);
         } else {
            if (!fuel) {
               return 0;
            }

            fuels.add(parser);
         }
      }

      int startAmount = parser.hasAmount() ? parser.getAmount() : Integer.MAX_VALUE;
      amountToTransfer = startAmount;
      Iterator var21 = heatables.iterator();

      while(var21.hasNext()) {
         p = (ItemParser)var21.next();
         CommonItemStack item = CommonItemStack.of(to.getItem(0));
         int numTransferred = CommonItemStack.transfer(from, item, p, amountToTransfer);
         if (numTransferred > 0) {
            amountToTransfer -= numTransferred;
            to.setItem(0, item.toBukkit());
         }
      }

      var21 = fuels.iterator();

      while(var21.hasNext()) {
         p = (ItemParser)var21.next();
         if (p != null) {
            if (amountToTransfer == 0) {
               break;
            }

            int transferCount = amountToTransfer;
            CommonItemStack fuel = CommonItemStack.of(to.getItem(1));
            if (!p.hasAmount()) {
               ItemStack cookeditem = to.getItem(0);
               if (cookeditem == null || cookeditem.getType() == Material.AIR) {
                  continue;
               }

               int fuelNeeded = cookeditem.getAmount() * 200;
               if (fuelNeeded == 0) {
                  continue;
               }

               fuelNeeded -= toFurnace.getCookTime();
               if (fuelNeeded <= 0) {
                  continue;
               }

               int fuelPerItem;
               if (fuel.getType() == Material.AIR) {
                  fuelPerItem = RecipeUtil.getFuelTime(p.getItemStack(1));
               } else {
                  fuelPerItem = RecipeUtil.getFuelTime(fuel.toBukkit());
               }

               if (fuelPerItem == 0) {
                  continue;
               }

               fuelNeeded -= fuelPerItem * fuel.getAmount();
               if (fuelNeeded <= 0) {
                  continue;
               }

               transferCount = Math.min(amountToTransfer, (int)Math.ceil((double)fuelNeeded / (double)fuelPerItem));
            }

            amountToTransfer -= CommonItemStack.transfer(from, fuel, p, transferCount);
            to.setItem(1, fuel.toBukkit());
         }
      }

      return startAmount - amountToTransfer;
   }

   public static IntVector2 readRadius(String text) {
      int radWidth = TCConfig.defaultTransferRadius;
      int radHeight = TCConfig.defaultTransferRadius;
      int radStartIndex = text.lastIndexOf(32);
      if (radStartIndex != -1) {
         String radText = text.substring(radStartIndex + 1);
         String[] parts = radText.split(":");
         if (parts.length == 1) {
            radWidth = radHeight = ParseUtil.parseInt(radText, TCConfig.defaultTransferRadius);
         } else if (parts.length == 2) {
            radWidth = ParseUtil.parseInt(parts[0], TCConfig.defaultTransferRadius);
            radHeight = ParseUtil.parseInt(parts[1], TCConfig.defaultTransferRadius);
         }
      }

      radWidth = MathUtil.clamp(radWidth, TCConfig.maxTransferRadius);
      radHeight = MathUtil.clamp(radHeight, TCConfig.maxTransferRadius);
      return new IntVector2(radWidth, radHeight);
   }

   public static Collection<BlockState> getBlockStates(SignActionEvent info, IntVector2 radius) {
      return getBlockStates(info, radius.x, radius.z);
   }

   public static Collection<BlockState> getBlockStates(SignActionEvent info, int radWidth, int radHeight) {
      final Block centerBlock = info.getRails();
      int radX = Math.abs(radWidth);
      int radY = Math.abs(radHeight);
      int radZ = Math.abs(radWidth);
      BlockFace dir = info.getCartEnterFace();
      if (FaceUtil.isVertical(dir)) {
         radY = 0;
      } else if (FaceUtil.isAlongX(dir)) {
         radX = 0;
      } else if (FaceUtil.isAlongZ(dir)) {
         radZ = 0;
      }

      ArrayList states = new ArrayList(BlockUtil.getBlockStates(centerBlock, radX, radY, radZ));

      try {
         Iterator iter = states.iterator();

         while(iter.hasNext()) {
            BlockState next = (BlockState)iter.next();
            if (next instanceof Chest) {
               DoubleChestInventory inventory = (DoubleChestInventory)CommonUtil.tryCast(((Chest)next).getInventory(), DoubleChestInventory.class);
               if (inventory != null && (!chestsBuffer.add(inventory.getLeftSide().getHolder()) || !chestsBuffer.add(inventory.getRightSide().getHolder()))) {
                  iter.remove();
               }
            }
         }
      } finally {
         chestsBuffer.clear();
      }

      final boolean widthInv = radWidth < 0;
      final boolean heightInv = radHeight < 0;
      Collections.sort(states, new Comparator<BlockState>() {
         public int getIndex(BlockState state) {
            int dx = MathUtil.invert(Math.abs(centerBlock.getX() - state.getX()), widthInv);
            int dy = MathUtil.invert(Math.abs(centerBlock.getY() - state.getY()), heightInv);
            int dz = MathUtil.invert(Math.abs(centerBlock.getZ() - state.getZ()), widthInv);
            return dx + 16 * dz + 256 * dy;
         }

         public int compare(BlockState o1, BlockState o2) {
            return this.getIndex(o1) - this.getIndex(o2);
         }
      });
      return states;
   }

   public static Collection<InventoryHolder> findBlocks(SignActionEvent info, String mode) {
      Collection<InteractType> typesToCheck = InteractType.parse(mode, info.getLine(1));
      if (typesToCheck.isEmpty()) {
         return Collections.emptyList();
      } else {
         IntVector2 radius = readRadius(info.getLine(1));
         Collection<BlockState> found = getBlockStates(info, radius);
         if (found.isEmpty()) {
            return Collections.emptyList();
         } else {
            List<InventoryHolder> rval = new ArrayList(found.size());
            Iterator var6 = typesToCheck.iterator();

            while(true) {
               label70:
               while(var6.hasNext()) {
                  InteractType type = (InteractType)var6.next();
                  Iterator var8;
                  BlockState state;
                  switch(type) {
                  case CHEST:
                     var8 = found.iterator();

                     while(true) {
                        if (!var8.hasNext()) {
                           continue label70;
                        }

                        state = (BlockState)var8.next();
                        if (state instanceof Chest) {
                           rval.add((Chest)state);
                        }
                     }
                  case FURNACE:
                     var8 = found.iterator();

                     while(true) {
                        if (!var8.hasNext()) {
                           continue label70;
                        }

                        state = (BlockState)var8.next();
                        if (state instanceof Furnace) {
                           rval.add((Furnace)state);
                        }
                     }
                  case DISPENSER:
                     var8 = found.iterator();

                     while(true) {
                        if (!var8.hasNext()) {
                           continue label70;
                        }

                        state = (BlockState)var8.next();
                        if (state instanceof Dispenser) {
                           rval.add((Dispenser)state);
                        }
                     }
                  case DROPPER:
                     var8 = found.iterator();

                     while(true) {
                        if (!var8.hasNext()) {
                           continue label70;
                        }

                        state = (BlockState)var8.next();
                        if (state instanceof Dropper) {
                           rval.add((Dropper)state);
                        }
                     }
                  case GROUNDITEM:
                     rval.add(new GroundItemsState(info.getRails(), Math.abs(radius.x)));
                  }
               }

               return rval;
            }
         }
      }
   }

   public static int transferAllItems(TrainCarts traincarts, Collection<InventoryHolder> fromHolders, Collection<InventoryHolder> toHolders, ItemParser itemParser, boolean isFuelPreferred) {
      int transferred = 0;
      Iterator var7 = fromHolders.iterator();

      while(true) {
         while(var7.hasNext()) {
            InventoryHolder fromHolder = (InventoryHolder)var7.next();
            Inventory from = fromHolder.getInventory();
            int amount;
            if (itemParser instanceof AveragedItemParser) {
               int totalAmount = itemParser.hasAmount() ? itemParser.getAmount() : Integer.MAX_VALUE;
               ItemParser single = itemParser.setAmount(1);
               int transferredAmount = 0;

               while(true) {
                  boolean continueTransferring = false;
                  Iterator var14 = toHolders.iterator();

                  while(var14.hasNext()) {
                     InventoryHolder toHolder = (InventoryHolder)var14.next();
                     Inventory to = toHolder.getInventory();
                     amount = transferItems(traincarts, from, to, single, isFuelPreferred);
                     if (amount > 0) {
                        transferred += amount;
                        transferredAmount += amount;
                        if (!(continueTransferring = transferredAmount < totalAmount)) {
                           break;
                        }
                     }
                  }

                  if (!continueTransferring) {
                     break;
                  }
               }
            } else {
               Iterator var10 = toHolders.iterator();

               while(var10.hasNext()) {
                  InventoryHolder toHolder = (InventoryHolder)var10.next();
                  Inventory to = toHolder.getInventory();
                  amount = transferItems(traincarts, from, to, itemParser, isFuelPreferred);
                  transferred += amount;
                  if (amount > 0 && itemParser.hasAmount()) {
                     itemParser = itemParser.setAmount(itemParser.getAmount() - amount);
                  }
               }
            }
         }

         return transferred;
      }
   }

   public static int transferItems(TrainCarts traincarts, Inventory from, Inventory to, ItemParser itemParser, boolean isFuelPreferred) {
      InventoryHolder toHolder = to.getHolder();
      InventoryHolder fromHolder = ((Inventory)from).getHolder();
      if (from instanceof FurnaceInventory) {
         final FurnaceInventory finv = (FurnaceInventory)from;
         from = new InventoryBase() {
            public int getSize() {
               return 1;
            }

            public ItemStack getItem(int index) {
               return finv.getResult();
            }

            public void setItem(int index, ItemStack item) {
               finv.setResult(item);
            }
         };
      }

      if (TCConfig.showTransferAnimations && !(from instanceof GroundItemsInventory)) {
         from = ItemAnimatedInventory.convert((Inventory)from, fromHolder, toHolder);
      }

      return toHolder instanceof Furnace ? depositInFurnace(traincarts, (Inventory)from, (Furnace)toHolder, itemParser, isFuelPreferred) : ItemUtil.transfer((Inventory)from, to, itemParser, itemParser.getAmount());
   }
}
