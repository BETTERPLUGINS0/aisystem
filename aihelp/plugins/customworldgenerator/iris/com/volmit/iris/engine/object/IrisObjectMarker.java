package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.block.data.BlockData;

@Snippet("object-marker")
@Desc("Find blocks to mark")
public class IrisObjectMarker {
   private final transient AtomicCache<KList<BlockData>> findData = new AtomicCache();
   @ArrayType(
      min = 1,
      type = IrisBlockData.class
   )
   @Required
   @Desc("Find block types to mark")
   private KList<IrisBlockData> mark = new KList();
   @MinNumber(1.0D)
   @MaxNumber(16.0D)
   @Desc("The maximum amount of markers to place. Use these sparingly!")
   private int maximumMarkers = 8;
   @Desc("If true, markers will only be placed if the block matches the mark list perfectly.")
   private boolean exact = false;
   @Required
   @RegistryListResource(IrisMarker.class)
   @Desc("The marker to add")
   private String marker;

   public KList<BlockData> getMark(IrisData rdata) {
      return (KList)this.findData.aquire(() -> {
         KList var2 = new KList();
         Iterator var3 = this.mark.iterator();

         while(var3.hasNext()) {
            IrisBlockData var4 = (IrisBlockData)var3.next();
            BlockData var5 = var4.getBlockData(var1);
            if (var5 != null) {
               var2.add((Object)var5);
            }
         }

         return var2;
      });
   }

   @Generated
   public IrisObjectMarker() {
   }

   @Generated
   public IrisObjectMarker(final KList<IrisBlockData> mark, final int maximumMarkers, final boolean exact, final String marker) {
      this.mark = var1;
      this.maximumMarkers = var2;
      this.exact = var3;
      this.marker = var4;
   }

   @Generated
   public AtomicCache<KList<BlockData>> getFindData() {
      return this.findData;
   }

   @Generated
   public KList<IrisBlockData> getMark() {
      return this.mark;
   }

   @Generated
   public int getMaximumMarkers() {
      return this.maximumMarkers;
   }

   @Generated
   public boolean isExact() {
      return this.exact;
   }

   @Generated
   public String getMarker() {
      return this.marker;
   }

   @Generated
   public IrisObjectMarker setMark(final KList<IrisBlockData> mark) {
      this.mark = var1;
      return this;
   }

   @Generated
   public IrisObjectMarker setMaximumMarkers(final int maximumMarkers) {
      this.maximumMarkers = var1;
      return this;
   }

   @Generated
   public IrisObjectMarker setExact(final boolean exact) {
      this.exact = var1;
      return this;
   }

   @Generated
   public IrisObjectMarker setMarker(final String marker) {
      this.marker = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisObjectMarker)) {
         return false;
      } else {
         IrisObjectMarker var2 = (IrisObjectMarker)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getMaximumMarkers() != var2.getMaximumMarkers()) {
            return false;
         } else if (this.isExact() != var2.isExact()) {
            return false;
         } else {
            label40: {
               KList var3 = this.getMark();
               KList var4 = var2.getMark();
               if (var3 == null) {
                  if (var4 == null) {
                     break label40;
                  }
               } else if (var3.equals(var4)) {
                  break label40;
               }

               return false;
            }

            String var5 = this.getMarker();
            String var6 = var2.getMarker();
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
      return var1 instanceof IrisObjectMarker;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + this.getMaximumMarkers();
      var5 = var5 * 59 + (this.isExact() ? 79 : 97);
      KList var3 = this.getMark();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      String var4 = this.getMarker();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getFindData());
      return "IrisObjectMarker(findData=" + var10000 + ", mark=" + String.valueOf(this.getMark()) + ", maximumMarkers=" + this.getMaximumMarkers() + ", exact=" + this.isExact() + ", marker=" + this.getMarker() + ")";
   }
}
