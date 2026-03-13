package org.terraform.schematic;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;

public class SchematicParser {
   private static final EnumSet<Material> fragile;
   private final HashMap<SimpleBlock, BlockData> delayed = new HashMap();
   private boolean isDelayedApply = false;

   public void applyData(@NotNull SimpleBlock block, @NotNull BlockData data) {
      if (!this.isDelayedApply && fragile.contains(data.getMaterial())) {
         this.delayed.put(block, data);
      } else {
         block.setBlockData(data);
      }

   }

   public void applyDelayedData() {
      this.isDelayedApply = true;
      Iterator var1 = this.delayed.entrySet().iterator();

      while(var1.hasNext()) {
         Entry<SimpleBlock, BlockData> entry = (Entry)var1.next();
         this.applyData((SimpleBlock)entry.getKey(), (BlockData)entry.getValue());
      }

   }

   static {
      fragile = EnumSet.of(Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.BROWN_CARPET, Material.RED_CARPET, Material.WHITE_CARPET, Material.SOUL_FIRE, Material.REDSTONE_WIRE, Material.REDSTONE_TORCH, Material.REPEATER, Material.RAIL, Material.LEVER, Material.POTATOES, Material.KELP);
   }
}
