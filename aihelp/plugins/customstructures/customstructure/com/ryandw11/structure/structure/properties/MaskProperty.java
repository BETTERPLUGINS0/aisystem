package com.ryandw11.structure.structure.properties;

import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.NullExtent;
import com.sk89q.worldedit.function.mask.AbstractExtentMask;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.world.block.BlockType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class MaskProperty {
   private final List<Mask> masks;
   private MaskProperty.MaskUnion unionType;

   public MaskProperty(String name, FileConfiguration configuration) {
      this.masks = new ArrayList();
      if (configuration.contains(name)) {
         ConfigurationSection cs = configuration.getConfigurationSection(name);

         assert cs != null;

         if (cs.contains("Enabled") && cs.getBoolean("Enabled")) {
            if (cs.contains("UnionType")) {
               this.unionType = MaskProperty.MaskUnion.valueOf(((String)Objects.requireNonNull(cs.getString("UnionType"))).toUpperCase());
            } else {
               this.unionType = MaskProperty.MaskUnion.AND;
            }

            this.processBlockTypeMask(cs);
            this.processNegateBlockTypeMask(cs);
         }
      }
   }

   public MaskProperty(List<Mask> masks, MaskProperty.MaskUnion unionType) {
      this.masks = masks;
      this.unionType = unionType;
   }

   public MaskProperty.MaskUnion getUnionType() {
      return this.unionType;
   }

   public void setUnionType(MaskProperty.MaskUnion type) {
      this.unionType = type;
   }

   public void addMask(Mask mask) {
      this.masks.add(mask);
   }

   public List<Mask> getMasks() {
      return this.masks;
   }

   public List<Mask> getMasks(Extent extent) {
      List<Mask> output = new ArrayList(this.getMasks());
      Iterator var3 = output.iterator();

      while(var3.hasNext()) {
         Mask mask = (Mask)var3.next();
         ((AbstractExtentMask)mask).setExtent(extent);
      }

      return output;
   }

   private void processBlockTypeMask(ConfigurationSection cs) {
      if (cs.contains("BlockTypeMask")) {
         List<BlockType> blockTypes = new ArrayList();
         List<String> blockTypeStrings = cs.getStringList("BlockTypeMask");
         Iterator var4 = blockTypeStrings.iterator();

         while(var4.hasNext()) {
            String s = (String)var4.next();
            blockTypes.add((BlockType)BlockType.REGISTRY.get(s.toLowerCase()));
         }

         BlockTypeMask blockTypeMask = new BlockTypeMask(new NullExtent(), blockTypes);
         this.addMask(blockTypeMask);
      }
   }

   private void processNegateBlockTypeMask(ConfigurationSection cs) {
      if (cs.contains("NegatedBlockMask")) {
         List<BlockType> blockTypes = new ArrayList(BlockType.REGISTRY.values());
         List<String> blockTypeStrings = cs.getStringList("NegatedBlockMask");
         Iterator var4 = blockTypeStrings.iterator();

         while(var4.hasNext()) {
            String s = (String)var4.next();
            blockTypes.remove(BlockType.REGISTRY.get(s.toLowerCase()));
         }

         BlockTypeMask blockTypeMask = new BlockTypeMask(new NullExtent(), blockTypes);
         this.addMask(blockTypeMask);
      }
   }

   public static enum MaskUnion {
      AND,
      OR;

      // $FF: synthetic method
      private static MaskProperty.MaskUnion[] $values() {
         return new MaskProperty.MaskUnion[]{AND, OR};
      }
   }
}
