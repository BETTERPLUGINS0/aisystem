package com.volmit.iris.engine.framework;

import com.google.gson.Gson;
import com.volmit.iris.Iris;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.util.io.IO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.Generated;

public class EngineData {
   private String dimension;
   private String lastVersion;
   private List<IrisPosition> strongholdPositions;

   public static EngineData load(File f) {
      try {
         var0.getParentFile().mkdirs();
         return (EngineData)(new Gson()).fromJson(IO.readAll(var0), EngineData.class);
      } catch (Throwable var2) {
         Iris.reportError(var2);
         return new EngineData();
      }
   }

   public void save(File f) {
      try {
         var1.getParentFile().mkdirs();
         IO.writeAll(var1, (Object)(new Gson()).toJson(this));
      } catch (IOException var3) {
         Iris.reportError(var3);
         var3.printStackTrace();
      }

   }

   @Generated
   public String getDimension() {
      return this.dimension;
   }

   @Generated
   public String getLastVersion() {
      return this.lastVersion;
   }

   @Generated
   public List<IrisPosition> getStrongholdPositions() {
      return this.strongholdPositions;
   }

   @Generated
   public void setDimension(final String dimension) {
      this.dimension = var1;
   }

   @Generated
   public void setLastVersion(final String lastVersion) {
      this.lastVersion = var1;
   }

   @Generated
   public void setStrongholdPositions(final List<IrisPosition> strongholdPositions) {
      this.strongholdPositions = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof EngineData)) {
         return false;
      } else {
         EngineData var2 = (EngineData)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label47: {
               String var3 = this.getDimension();
               String var4 = var2.getDimension();
               if (var3 == null) {
                  if (var4 == null) {
                     break label47;
                  }
               } else if (var3.equals(var4)) {
                  break label47;
               }

               return false;
            }

            String var5 = this.getLastVersion();
            String var6 = var2.getLastVersion();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            List var7 = this.getStrongholdPositions();
            List var8 = var2.getStrongholdPositions();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof EngineData;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      String var3 = this.getDimension();
      int var6 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      String var4 = this.getLastVersion();
      var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
      List var5 = this.getStrongholdPositions();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }

   @Generated
   public String toString() {
      String var10000 = this.getDimension();
      return "EngineData(dimension=" + var10000 + ", lastVersion=" + this.getLastVersion() + ", strongholdPositions=" + String.valueOf(this.getStrongholdPositions()) + ")";
   }
}
