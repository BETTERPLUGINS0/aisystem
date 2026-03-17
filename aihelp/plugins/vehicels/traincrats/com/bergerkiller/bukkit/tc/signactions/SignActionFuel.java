package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecartFurnace;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberFurnace;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.itemanimation.ItemAnimation;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import com.bergerkiller.bukkit.tc.utils.TransferSignUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SignActionFuel extends TrainCartsSignAction {
   public SignActionFuel() {
      super("fuel");
   }

   public void execute(SignActionEvent info) {
      if (info.isAction(SignActionType.MEMBER_ENTER, SignActionType.REDSTONE_ON, SignActionType.GROUP_ENTER)) {
         boolean docart = info.isAction(SignActionType.MEMBER_ENTER, SignActionType.REDSTONE_ON) && info.isCartSign() && info.hasMember();
         boolean dotrain = !docart && info.isAction(SignActionType.GROUP_ENTER, SignActionType.REDSTONE_ON) && info.isTrainSign() && info.hasGroup();
         if (docart || dotrain) {
            if (info.isPowered()) {
               int radius = ParseUtil.parseInt(info.getLine(1), TCConfig.defaultTransferRadius);
               List<Chest> chests = new ArrayList();
               Iterator var6 = TransferSignUtil.getBlockStates(info, radius, radius).iterator();

               while(var6.hasNext()) {
                  BlockState state = (BlockState)var6.next();
                  if (state instanceof Chest) {
                     chests.add((Chest)state);
                  }
               }

               if (!chests.isEmpty()) {
                  Object carts;
                  if (dotrain) {
                     carts = info.getGroup();
                  } else {
                     carts = new ArrayList(1);
                     ((List)carts).add(info.getMember());
                  }

                  Iterator var9 = ((List)carts).iterator();

                  while(true) {
                     MinecartMemberFurnace member;
                     do {
                        MinecartMember cart;
                        do {
                           if (!var9.hasNext()) {
                              return;
                           }

                           cart = (MinecartMember)var9.next();
                        } while(!(cart instanceof MinecartMemberFurnace));

                        member = (MinecartMemberFurnace)cart;
                     } while(((CommonMinecartFurnace)member.getEntity()).hasFuel());

                     boolean found = false;
                     Iterator var12 = chests.iterator();

                     while(var12.hasNext()) {
                        Chest chest = (Chest)var12.next();
                        Inventory inv = chest.getInventory();

                        for(int i = 0; i < inv.getSize(); ++i) {
                           ItemStack item = inv.getItem(i);
                           if (!LogicUtil.nullOrEmpty(item) && item.getType() == Material.COAL) {
                              ItemUtil.subtractAmount(item, 1);
                              inv.setItem(i, item);
                              found = true;
                              member.addFuelTicks(3600);
                              if (TCConfig.showTransferAnimations) {
                                 ItemAnimation.start(chest, member, (ItemStack)(new ItemStack(Material.COAL, 1)));
                              }
                              break;
                           }
                        }

                        if (found) {
                           break;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public boolean build(SignChangeActionEvent event) {
      SignBuildOptions opt = SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_COLLECTOR).setName("powered minecart coal collector").setDescription("fuel the powered minecart using coal from a chest");
      return opt.handle(event);
   }
}
