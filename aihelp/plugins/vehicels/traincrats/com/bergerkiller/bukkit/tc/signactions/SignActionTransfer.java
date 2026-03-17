package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.inventory.ItemParser;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.tc.InteractType;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import com.bergerkiller.bukkit.tc.utils.TransferSignUtil;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class SignActionTransfer extends TrainCartsSignAction {
   public static final String DEPOSIT = "deposit";
   public static final String COLLECT = "collect";
   public static final String KEY_TYPE_TARGET = "target";

   private static void setTargetConstant(TrainCarts traincarts, Collection<InventoryHolder> inventories) {
      HashSet<String> types = new HashSet();
      StringBuilder nameBuilder = new StringBuilder();
      Iterator var4 = inventories.iterator();

      while(var4.hasNext()) {
         InventoryHolder holder = (InventoryHolder)var4.next();
         ListIterator var6 = holder.getInventory().iterator();

         while(var6.hasNext()) {
            ItemStack item = (ItemStack)var6.next();
            if (!LogicUtil.nullOrEmpty(item)) {
               nameBuilder.setLength(0);
               nameBuilder.append(item.getType().toString().toLowerCase(Locale.ENGLISH));
               if ((Boolean)MaterialUtil.HASDATA.get(item)) {
                  nameBuilder.append(':');
                  nameBuilder.append(item.getDurability());
               }

               types.add(nameBuilder.toString());
            }
         }
      }

      ItemParser[] parsers = new ItemParser[types.size()];
      Iterator<String> iter = types.iterator();

      for(int i = 0; i < parsers.length; ++i) {
         parsers[i] = ItemParser.parse((String)iter.next());
      }

      traincarts.putParsers("target", parsers);
   }

   public SignActionTransfer() {
      super(InteractType.getAllUniqueTypeIdentifiers());
   }

   public void execute(SignActionEvent info) {
      if (info.isAction(SignActionType.MEMBER_ENTER, SignActionType.REDSTONE_ON, SignActionType.GROUP_ENTER)) {
         if (info.hasRails() && info.isPowered()) {
            boolean docart = info.isAction(SignActionType.MEMBER_ENTER, SignActionType.REDSTONE_ON) && info.isCartSign() && info.hasMember();
            boolean dotrain = !docart && info.isAction(SignActionType.GROUP_ENTER, SignActionType.REDSTONE_ON) && info.isTrainSign() && info.hasGroup();
            if (docart || dotrain) {
               boolean collect = true;
               Collection<InventoryHolder> otherInvs = TransferSignUtil.findBlocks(info, "collect");
               if (otherInvs.isEmpty()) {
                  collect = false;
                  otherInvs = TransferSignUtil.findBlocks(info, "deposit");
                  if (otherInvs.isEmpty()) {
                     return;
                  }
               }

               Collection<InventoryHolder> trainInvs = TransferSignUtil.getInventories(info);
               if (!trainInvs.isEmpty()) {
                  if (collect) {
                     setTargetConstant(info.getTrainCarts(), trainInvs);
                  } else {
                     setTargetConstant(info.getTrainCarts(), otherInvs);
                  }

                  ItemParser[] parsers = Util.getParsers(info.getLine(2), info.getLine(3));
                  info.getTrainCarts().putParsers("target", (ItemParser[])null);
                  int i;
                  if (collect) {
                     ItemParser[] var8 = parsers;
                     i = parsers.length;

                     for(int var10 = 0; var10 < i; ++var10) {
                        ItemParser parser = var8[var10];
                        TransferSignUtil.transferAllItems(info.getTrainCarts(), otherInvs, trainInvs, parser, false);
                     }
                  } else {
                     int fuelHalfIndex;
                     if (info.getLine(2).isEmpty()) {
                        fuelHalfIndex = 0;
                     } else if (info.getLine(3).isEmpty()) {
                        fuelHalfIndex = Integer.MAX_VALUE;
                     } else {
                        fuelHalfIndex = Util.getParsers(info.getLine(2)).length;
                     }

                     for(i = 0; i < parsers.length; ++i) {
                        TransferSignUtil.transferAllItems(info.getTrainCarts(), trainInvs, otherInvs, parsers[i], i >= fuelHalfIndex);
                     }
                  }

                  Iterator var13 = otherInvs.iterator();

                  while(var13.hasNext()) {
                     InventoryHolder holder = (InventoryHolder)var13.next();
                     if (holder instanceof BlockState) {
                        BlockUtil.applyPhysics(((BlockState)holder).getBlock(), Material.AIR);
                     }
                  }

               }
            }
         }
      }
   }

   public boolean build(SignChangeActionEvent event) {
      Collection<InteractType> typesToCheck = InteractType.parse("collect", event.getLine(1));
      boolean collect = true;
      if (typesToCheck.isEmpty()) {
         collect = false;
         typesToCheck = InteractType.parse("deposit", event.getLine(1));
      }

      String[] types = new String[typesToCheck.size()];
      int i = 0;

      for(Iterator var6 = typesToCheck.iterator(); var6.hasNext(); ++i) {
         InteractType mat = (InteractType)var6.next();
         types[i] = mat.toString().toLowerCase() + "s";
      }

      return collect ? SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_COLLECTOR).setName("storage minecart item collector").setDescription("take items from " + StringUtil.combineNames(types)).setTraincartsWIKIHelp("TrainCarts/Signs/Transfer").handle(event) : SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_DEPOSITOR).setName("storage minecart item depositor").setDescription("make trains put items into " + StringUtil.combineNames(types)).setTraincartsWIKIHelp("TrainCarts/Signs/Transfer").handle(event);
   }
}
