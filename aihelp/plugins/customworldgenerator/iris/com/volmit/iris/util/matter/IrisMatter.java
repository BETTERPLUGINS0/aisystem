package com.volmit.iris.util.matter;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.plugin.VolmitSender;
import java.util.Iterator;
import java.util.Objects;
import lombok.Generated;

public class IrisMatter extends IrisRegistrant implements Matter {
   protected static final KMap<Class<?>, MatterSlice<?>> slicers = buildSlicers();
   private final MatterHeader header;
   private final int width;
   private final int height;
   private final int depth;
   private final KMap<Class<?>, MatterSlice<?>> sliceMap;

   public IrisMatter(int width, int height, int depth) {
      if (var1 >= 1 && var2 >= 1 && var3 >= 1) {
         this.width = var1;
         this.height = var2;
         this.depth = var3;
         this.header = new MatterHeader();
         this.sliceMap = new KMap();
      } else {
         throw new RuntimeException("Invalid Matter Size " + var1 + "x" + var2 + "x" + var3);
      }
   }

   private static KMap<Class<?>, MatterSlice<?>> buildSlicers() {
      KMap var0 = new KMap();
      Iterator var1 = Iris.initialize("com.volmit.iris.util.matter.slices", Sliced.class).iterator();

      while(var1.hasNext()) {
         Object var2 = var1.next();
         MatterSlice var3 = (MatterSlice)var2;
         var0.put(var3.getType(), var3);
      }

      return var0;
   }

   public <T> MatterSlice<T> slice(Class<?> c) {
      return (MatterSlice)this.sliceMap.computeIfAbsent(var1, (var2) -> {
         return (MatterSlice)Objects.requireNonNull(this.createSlice(var1, this), "Bad slice " + var1.getCanonicalName());
      });
   }

   public <T> MatterSlice<T> createSlice(Class<T> type, Matter m) {
      MatterSlice var3 = (MatterSlice)slicers.get(var1);
      if (var3 == null) {
         return null;
      } else {
         try {
            return (MatterSlice)var3.getClass().getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(this.getWidth(), this.getHeight(), this.getDepth());
         } catch (Throwable var5) {
            var5.printStackTrace();
            return null;
         }
      }
   }

   public String getFolderName() {
      return "matter";
   }

   public String getTypeName() {
      return "matter";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public MatterHeader getHeader() {
      return this.header;
   }

   @Generated
   public int getWidth() {
      return this.width;
   }

   @Generated
   public int getHeight() {
      return this.height;
   }

   @Generated
   public int getDepth() {
      return this.depth;
   }

   @Generated
   public KMap<Class<?>, MatterSlice<?>> getSliceMap() {
      return this.sliceMap;
   }
}
