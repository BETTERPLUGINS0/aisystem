package com.volmit.iris.engine.object;

import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.data.DataProvider;
import java.util.Iterator;
import lombok.Generated;

@Snippet("loot-registry")
@Desc("Represents a loot entry")
public class IrisLootReference {
   private final transient AtomicCache<KList<IrisLootTable>> tt = new AtomicCache();
   @Desc("Add = add on top of parent tables, Replace = clear first then add these. Clear = Remove all and dont add loot from this or parent.")
   private IrisLootMode mode;
   @RegistryListResource(IrisLootTable.class)
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("Add loot table registries here")
   private KList<String> tables;
   @MinNumber(0.0D)
   @Desc("Increase the chance of loot in this area")
   private double multiplier;

   public KList<IrisLootTable> getLootTables(DataProvider g) {
      return (KList)this.tt.aquire(() -> {
         KList var2 = new KList();
         Iterator var3 = this.tables.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.add((Object)((IrisLootTable)var1.getData().getLootLoader().load(var4)));
         }

         return var2;
      });
   }

   @Generated
   public IrisLootReference() {
      this.mode = IrisLootMode.ADD;
      this.tables = new KList();
      this.multiplier = 1.0D;
   }

   @Generated
   public IrisLootReference(final IrisLootMode mode, final KList<String> tables, final double multiplier) {
      this.mode = IrisLootMode.ADD;
      this.tables = new KList();
      this.multiplier = 1.0D;
      this.mode = var1;
      this.tables = var2;
      this.multiplier = var3;
   }

   @Generated
   public AtomicCache<KList<IrisLootTable>> getTt() {
      return this.tt;
   }

   @Generated
   public IrisLootMode getMode() {
      return this.mode;
   }

   @Generated
   public KList<String> getTables() {
      return this.tables;
   }

   @Generated
   public double getMultiplier() {
      return this.multiplier;
   }

   @Generated
   public IrisLootReference setMode(final IrisLootMode mode) {
      this.mode = var1;
      return this;
   }

   @Generated
   public IrisLootReference setTables(final KList<String> tables) {
      this.tables = var1;
      return this;
   }

   @Generated
   public IrisLootReference setMultiplier(final double multiplier) {
      this.multiplier = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisLootReference)) {
         return false;
      } else {
         IrisLootReference var2 = (IrisLootReference)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getMultiplier(), var2.getMultiplier()) != 0) {
            return false;
         } else {
            IrisLootMode var3 = this.getMode();
            IrisLootMode var4 = var2.getMode();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            KList var5 = this.getTables();
            KList var6 = var2.getTables();
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
      return var1 instanceof IrisLootReference;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getMultiplier());
      int var7 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      IrisLootMode var5 = this.getMode();
      var7 = var7 * 59 + (var5 == null ? 43 : var5.hashCode());
      KList var6 = this.getTables();
      var7 = var7 * 59 + (var6 == null ? 43 : var6.hashCode());
      return var7;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getTt());
      return "IrisLootReference(tt=" + var10000 + ", mode=" + String.valueOf(this.getMode()) + ", tables=" + String.valueOf(this.getTables()) + ", multiplier=" + this.getMultiplier() + ")";
   }
}
