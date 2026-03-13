package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.RNG;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

@Snippet("block-drops")
@Desc("Represents a block drop list")
public class IrisBlockDrops {
   private final transient AtomicCache<KList<BlockData>> data = new AtomicCache();
   @Required
   @ArrayType(
      min = 1,
      type = IrisBlockData.class
   )
   @Desc("The blocks that drop loot")
   private KList<IrisBlockData> blocks = new KList();
   @Desc("If exact blocks is set to true, minecraft:barrel[axis=x] will only drop for that axis. When exact is false (default) any barrel will drop the defined drops.")
   private boolean exactBlocks = false;
   @Desc("Add in specific items to drop")
   @ArrayType(
      min = 1,
      type = IrisLoot.class
   )
   private KList<IrisLoot> drops = new KList();
   @Desc("If this is in a biome, setting skipParents to true will ignore the drops in the region and dimension for this block type. The default (false) will allow all three nodes to fire and add to a list of drops.")
   private boolean skipParents = false;
   @Desc("Removes the default vanilla block drops and only drops the given items & any parent loot tables specified for this block type.")
   private boolean replaceVanillaDrops = false;

   public boolean shouldDropFor(BlockData data, IrisData rdata) {
      KList var3 = (KList)this.data.aquire(() -> {
         KList var2x = new KList();
         Iterator var3 = this.getBlocks().iterator();

         while(var3.hasNext()) {
            IrisBlockData var4 = (IrisBlockData)var3.next();
            BlockData var5 = var4.getBlockData(var2);
            if (var5 != null) {
               var2x.add((Object)var5);
            }
         }

         return var2x.removeDuplicates();
      });
      Iterator var4 = var3.iterator();

      while(true) {
         if (!var4.hasNext()) {
            return false;
         }

         BlockData var5 = (BlockData)var4.next();
         if (this.exactBlocks) {
            if (var5.equals(var1)) {
               break;
            }
         } else if (var5.getMaterial().equals(var1.getMaterial())) {
            break;
         }
      }

      return true;
   }

   public void fillDrops(boolean debug, KList<ItemStack> d) {
      Iterator var3 = this.getDrops().iterator();

      while(var3.hasNext()) {
         IrisLoot var4 = (IrisLoot)var3.next();
         if (RNG.r.i(1, var4.getRarity()) == var4.getRarity()) {
            var2.add((Object)var4.get(var1, RNG.r));
         }
      }

   }

   @Generated
   public IrisBlockDrops() {
   }

   @Generated
   public IrisBlockDrops(final KList<IrisBlockData> blocks, final boolean exactBlocks, final KList<IrisLoot> drops, final boolean skipParents, final boolean replaceVanillaDrops) {
      this.blocks = var1;
      this.exactBlocks = var2;
      this.drops = var3;
      this.skipParents = var4;
      this.replaceVanillaDrops = var5;
   }

   @Generated
   public AtomicCache<KList<BlockData>> getData() {
      return this.data;
   }

   @Generated
   public KList<IrisBlockData> getBlocks() {
      return this.blocks;
   }

   @Generated
   public boolean isExactBlocks() {
      return this.exactBlocks;
   }

   @Generated
   public KList<IrisLoot> getDrops() {
      return this.drops;
   }

   @Generated
   public boolean isSkipParents() {
      return this.skipParents;
   }

   @Generated
   public boolean isReplaceVanillaDrops() {
      return this.replaceVanillaDrops;
   }

   @Generated
   public IrisBlockDrops setBlocks(final KList<IrisBlockData> blocks) {
      this.blocks = var1;
      return this;
   }

   @Generated
   public IrisBlockDrops setExactBlocks(final boolean exactBlocks) {
      this.exactBlocks = var1;
      return this;
   }

   @Generated
   public IrisBlockDrops setDrops(final KList<IrisLoot> drops) {
      this.drops = var1;
      return this;
   }

   @Generated
   public IrisBlockDrops setSkipParents(final boolean skipParents) {
      this.skipParents = var1;
      return this;
   }

   @Generated
   public IrisBlockDrops setReplaceVanillaDrops(final boolean replaceVanillaDrops) {
      this.replaceVanillaDrops = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisBlockDrops)) {
         return false;
      } else {
         IrisBlockDrops var2 = (IrisBlockDrops)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isExactBlocks() != var2.isExactBlocks()) {
            return false;
         } else if (this.isSkipParents() != var2.isSkipParents()) {
            return false;
         } else if (this.isReplaceVanillaDrops() != var2.isReplaceVanillaDrops()) {
            return false;
         } else {
            KList var3 = this.getBlocks();
            KList var4 = var2.getBlocks();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            KList var5 = this.getDrops();
            KList var6 = var2.getDrops();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisBlockDrops;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + (this.isExactBlocks() ? 79 : 97);
      var5 = var5 * 59 + (this.isSkipParents() ? 79 : 97);
      var5 = var5 * 59 + (this.isReplaceVanillaDrops() ? 79 : 97);
      KList var3 = this.getBlocks();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      KList var4 = this.getDrops();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getData());
      return "IrisBlockDrops(data=" + var10000 + ", blocks=" + String.valueOf(this.getBlocks()) + ", exactBlocks=" + this.isExactBlocks() + ", drops=" + String.valueOf(this.getDrops()) + ", skipParents=" + this.isSkipParents() + ", replaceVanillaDrops=" + this.isReplaceVanillaDrops() + ")";
   }
}
