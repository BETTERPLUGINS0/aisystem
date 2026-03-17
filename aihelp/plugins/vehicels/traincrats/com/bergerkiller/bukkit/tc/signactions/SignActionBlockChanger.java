package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.inventory.ItemParser;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.properties.standard.type.TrainDisplayedBlocks;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Material;

public class SignActionBlockChanger extends TrainCartsSignAction {
   public static final int BLOCK_OFFSET_NONE = Integer.MAX_VALUE;

   public static void setBlocks(Collection<MinecartMember<?>> members, TrainDisplayedBlocks config) {
      if (config.getBlockTypesPattern().isEmpty()) {
         setBlocks(members, new ItemParser[0], config.getOffset());
      } else {
         setBlocks(members, config.getBlockTypesPattern(), config.getOffset());
      }

   }

   public static void setBlocks(Collection<MinecartMember<?>> members, String blocksText, int blockOffset) {
      setBlocks(members, Util.getParsers(blocksText), blockOffset);
   }

   public static void setBlocks(Collection<MinecartMember<?>> members, ItemParser[] blocks, int blockOffset) {
      Iterator<MinecartMember<?>> iter = members.iterator();
      if (blocks != null && blocks.length > 0) {
         while(true) {
            ItemParser[] var12 = blocks;
            int var13 = blocks.length;

            for(int var6 = 0; var6 < var13; ++var6) {
               ItemParser block = var12[var6];
               int amount = block.hasAmount() ? block.getAmount() : 1;

               for(int i = 0; i < amount; ++i) {
                  if (!iter.hasNext()) {
                     return;
                  }

                  CommonMinecart<?> entity = (CommonMinecart)((MinecartMember)iter.next()).getEntity();
                  if (block.hasType() || block.hasData()) {
                     Material type = block.hasType() ? block.getType() : entity.getBlockType();
                     if (block.hasData()) {
                        entity.setBlock(type, block.getData());
                     } else {
                        entity.setBlock(type);
                     }

                     if (blockOffset != Integer.MAX_VALUE) {
                        entity.setBlockOffset(blockOffset);
                     }
                  }
               }
            }
         }
      } else {
         if (blockOffset != Integer.MAX_VALUE) {
            Iterator var4 = members.iterator();

            while(var4.hasNext()) {
               MinecartMember<?> member = (MinecartMember)var4.next();
               ((CommonMinecart)member.getEntity()).setBlockOffset(blockOffset);
            }
         }

      }
   }

   public SignActionBlockChanger() {
      super("blockchanger", "setblock", "changeblock");
   }

   public void execute(SignActionEvent info) {
      if (info.isPowered()) {
         ItemParser[] blocks = Util.getParsers(info.getLine(2), info.getLine(3));
         int blockOffset = ParseUtil.parseInt(info.getLine(1), Integer.MAX_VALUE);
         if (info.isTrainSign() && info.hasGroup() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.GROUP_ENTER)) {
            setBlocks(info.getGroup(), (ItemParser[])blocks, blockOffset);
         } else if (info.isCartSign() && info.hasMember() && info.isAction(SignActionType.REDSTONE_ON, SignActionType.MEMBER_ENTER)) {
            List<MinecartMember<?>> tmp = new ArrayList(1);
            tmp.add(info.getMember());
            setBlocks(tmp, (ItemParser[])blocks, blockOffset);
         } else if (info.isRCSign() && info.isAction(SignActionType.REDSTONE_ON)) {
            Iterator var4 = info.getRCTrainGroups().iterator();

            while(var4.hasNext()) {
               MinecartGroup group = (MinecartGroup)var4.next();
               setBlocks(group, (ItemParser[])blocks, blockOffset);
            }
         }

      }
   }

   public boolean build(SignChangeActionEvent event) {
      SignBuildOptions opt = SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_BLOCKCHANGER).setName(event.isCartSign() ? "cart block changer" : "train block changer").setTraincartsWIKIHelp("TrainCarts/Signs/BlockChanger");
      if (event.isTrainSign()) {
         opt.setDescription("change the blocks displayed in a train");
      } else if (event.isCartSign()) {
         opt.setDescription("change the block displayed in a minecart");
      } else if (event.isRCSign()) {
         opt.setDescription("change the blocks displayed in a train remotely");
      }

      return opt.handle(event);
   }

   public boolean canSupportRC() {
      return true;
   }
}
