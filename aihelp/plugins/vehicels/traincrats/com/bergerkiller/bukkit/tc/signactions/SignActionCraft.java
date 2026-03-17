package com.bergerkiller.bukkit.tc.signactions;

import com.bergerkiller.bukkit.common.inventory.ItemParser;
import com.bergerkiller.bukkit.common.permissions.IPermissionEnum;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.RecipeUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.itemanimation.ItemAnimatedInventory;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import com.bergerkiller.bukkit.tc.utils.TransferSignUtil;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.Inventory;

public class SignActionCraft extends TrainCartsSignAction {
   private static final Material WORKBENCH_TYPE = MaterialUtil.getFirst(new String[]{"CRAFTING_TABLE", "LEGACY_WORKBENCH"});

   public SignActionCraft() {
      super("craft");
   }

   public void execute(SignActionEvent info) {
      boolean docart = info.isAction(SignActionType.MEMBER_ENTER, SignActionType.REDSTONE_ON) && info.isCartSign() && info.hasMember();
      boolean dotrain = !docart && info.isAction(SignActionType.GROUP_ENTER, SignActionType.REDSTONE_ON) && info.isTrainSign() && info.hasGroup();
      if ((docart || dotrain) && info.hasRailedMember() && info.isPowered()) {
         int radY;
         int radZ;
         int radX = radY = radZ = ParseUtil.parseInt(info.getLine(1), TCConfig.defaultTransferRadius);
         BlockFace dir = info.getCartEnterFace();
         if (FaceUtil.isAlongX(dir)) {
            radX = 0;
         } else if (FaceUtil.isAlongZ(dir)) {
            radZ = 0;
         }

         World world = info.getWorld();
         Block m = info.getRails();
         Block w = null;

         int z;
         for(int x = -radX; x <= radX && w == null; ++x) {
            for(int y = -radY; y <= radY && w == null; ++y) {
               for(z = -radZ; z <= radZ && w == null; ++z) {
                  BlockData data = WorldUtil.getBlockData(world, m.getX() + x, m.getY() + y, m.getZ() + z);
                  if (data.isType(WORKBENCH_TYPE)) {
                     w = m.getRelative(x, y, z);
                  }
               }
            }
         }

         if (w != null || !TCConfig.craftingRequireWorkbench) {
            Inventory inventory = TransferSignUtil.getInventory(info);
            if (inventory == null) {
               return;
            }

            if (w != null && TCConfig.showTransferAnimations) {
               inventory = ItemAnimatedInventory.convert(inventory, info.getMember(), w);
            }

            ItemParser[] var17 = Util.getParsers(info.getLine(2), info.getLine(3));
            z = var17.length;

            for(int var18 = 0; var18 < z; ++var18) {
               ItemParser item = var17[var18];
               RecipeUtil.craftItems(item, inventory);
            }
         }

      }
   }

   public boolean build(SignChangeActionEvent event) {
      return SignBuildOptions.create().setPermission((IPermissionEnum)Permission.BUILD_CRAFTER).setName("workbench item crafter").setDescription("craft items inside storage minecarts").setTraincartsWIKIHelp("TrainCarts/Signs/Crafter").handle(event);
   }
}
