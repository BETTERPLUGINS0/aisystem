package com.volmit.iris.engine.object;

import com.volmit.iris.core.nms.datapack.IDataFixer;
import com.volmit.iris.util.data.Varint;
import com.volmit.iris.util.io.IO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lombok.Generated;
import lombok.NonNull;

public final class IrisDimensionType {
   @NonNull
   private final String key;
   @NonNull
   private final IDataFixer.Dimension base;
   @NonNull
   private final IrisDimensionTypeOptions options;
   private final int logicalHeight;
   private final int height;
   private final int minY;

   public IrisDimensionType(@NonNull IDataFixer.Dimension base, @NonNull IrisDimensionTypeOptions options, int logicalHeight, int height, int minY) {
      if (var1 == null) {
         throw new NullPointerException("base is marked non-null but is null");
      } else if (var2 == null) {
         throw new NullPointerException("options is marked non-null but is null");
      } else if (var3 > var4) {
         throw new IllegalArgumentException("Logical height cannot be greater than height");
      } else if (var3 < 0) {
         throw new IllegalArgumentException("Logical height cannot be less than zero");
      } else if (var4 >= 16 && var4 <= 4064) {
         if ((var4 & 15) != 0) {
            throw new IllegalArgumentException("Height must be a multiple of 16");
         } else if (var5 >= -2032 && var5 <= 2031) {
            if ((var5 & 15) != 0) {
               throw new IllegalArgumentException("Min Y must be a multiple of 16");
            } else {
               this.base = var1;
               this.options = var2;
               this.logicalHeight = var3;
               this.height = var4;
               this.minY = var5;
               this.key = this.createKey();
            }
         } else {
            throw new IllegalArgumentException("Min Y must be between -2032 and 2031");
         }
      } else {
         throw new IllegalArgumentException("Height must be between 16 and 4064");
      }
   }

   public static IrisDimensionType fromKey(String key) {
      ByteArrayInputStream var1 = new ByteArrayInputStream(IO.decode(var0.replace(".", "=").toUpperCase()));

      try {
         DataInputStream var2 = new DataInputStream(var1);

         IrisDimensionType var3;
         try {
            var3 = new IrisDimensionType(IDataFixer.Dimension.values()[var2.readUnsignedByte()], (new IrisDimensionTypeOptions()).read(var2), Varint.readUnsignedVarInt((DataInput)var2), Varint.readUnsignedVarInt((DataInput)var2), Varint.readSignedVarInt((DataInput)var2));
         } catch (Throwable var6) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         var2.close();
         return var3;
      } catch (IOException var7) {
         throw new RuntimeException("This is impossible", var7);
      }
   }

   public String toJson(IDataFixer fixer) {
      return var1.createDimension(this.base, this.minY, this.height, this.logicalHeight, this.options.copy()).toString(4);
   }

   private String createKey() {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream(41);

      try {
         DataOutputStream var2 = new DataOutputStream(var1);

         try {
            var2.writeByte(this.base.ordinal());
            this.options.write(var2);
            Varint.writeUnsignedVarInt(this.logicalHeight, var2);
            Varint.writeUnsignedVarInt(this.height, var2);
            Varint.writeSignedVarInt(this.minY, var2);
         } catch (Throwable var6) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         var2.close();
      } catch (IOException var7) {
         throw new RuntimeException("This is impossible", var7);
      }

      return IO.encode(var1.toByteArray()).replace("=", ".").toLowerCase();
   }

   public IrisDimensionTypeOptions options() {
      return this.options.copy();
   }

   @NonNull
   @Generated
   public String key() {
      return this.key;
   }

   @NonNull
   @Generated
   public IDataFixer.Dimension base() {
      return this.base;
   }

   @Generated
   public int logicalHeight() {
      return this.logicalHeight;
   }

   @Generated
   public int height() {
      return this.height;
   }

   @Generated
   public int minY() {
      return this.minY;
   }

   @Generated
   public String toString() {
      String var10000 = this.key();
      return "IrisDimensionType(key=" + var10000 + ", base=" + String.valueOf(this.base()) + ", options=" + String.valueOf(this.options()) + ", logicalHeight=" + this.logicalHeight() + ", height=" + this.height() + ", minY=" + this.minY() + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisDimensionType)) {
         return false;
      } else {
         IrisDimensionType var2 = (IrisDimensionType)var1;
         if (this.logicalHeight() != var2.logicalHeight()) {
            return false;
         } else if (this.height() != var2.height()) {
            return false;
         } else if (this.minY() != var2.minY()) {
            return false;
         } else {
            label52: {
               String var3 = this.key();
               String var4 = var2.key();
               if (var3 == null) {
                  if (var4 == null) {
                     break label52;
                  }
               } else if (var3.equals(var4)) {
                  break label52;
               }

               return false;
            }

            IDataFixer.Dimension var5 = this.base();
            IDataFixer.Dimension var6 = var2.base();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            IrisDimensionTypeOptions var7 = this.options();
            IrisDimensionTypeOptions var8 = var2.options();
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
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var6 = var2 * 59 + this.logicalHeight();
      var6 = var6 * 59 + this.height();
      var6 = var6 * 59 + this.minY();
      String var3 = this.key();
      var6 = var6 * 59 + (var3 == null ? 43 : var3.hashCode());
      IDataFixer.Dimension var4 = this.base();
      var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
      IrisDimensionTypeOptions var5 = this.options();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }
}
