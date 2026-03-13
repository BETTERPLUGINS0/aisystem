package com.volmit.iris.engine.object;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.volmit.iris.Iris;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.util.collection.KMap;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lombok.Generated;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;

public class TileData implements Cloneable {
   private static final Gson gson = (new GsonBuilder()).disableHtmlEscaping().setLenient().create();
   @NonNull
   private Material material;
   @NonNull
   private KMap<String, Object> properties;

   public static boolean setTileState(Block block, TileData data) {
      return var0.getState() instanceof TileState && var1.isApplicable(var0.getBlockData()) ? var1.toBukkitTry(var0) : false;
   }

   public static TileData getTileState(Block block, boolean useLegacy) {
      if (!INMS.get().hasTile(var0.getType())) {
         return null;
      } else {
         if (var1) {
            LegacyTileData var2 = LegacyTileData.fromBukkit(var0.getState());
            if (var2 != null) {
               return var2;
            }
         }

         return (new TileData()).fromBukkit(var0);
      }
   }

   public static TileData read(DataInputStream in) {
      if (!var0.markSupported()) {
         throw new IOException("Mark not supported");
      } else {
         var0.mark(Integer.MAX_VALUE);

         LegacyTileData var2;
         try {
            TileData var1 = new TileData(Material.matchMaterial(var0.readUTF()), (KMap)gson.fromJson(var0.readUTF(), KMap.class));
            return var1;
         } catch (Throwable var6) {
            var0.reset();
            var2 = new LegacyTileData(var0);
         } finally {
            var0.mark(0);
         }

         return var2;
      }
   }

   public boolean isApplicable(BlockData data) {
      return this.material != null && var1.getMaterial() == this.material;
   }

   public void toBukkit(Block block) {
      if (this.material == null) {
         throw new IllegalStateException("Material not set");
      } else if (var1.getType() != this.material) {
         String var10002 = String.valueOf(var1.getType());
         throw new IllegalStateException("Material mismatch: " + var10002 + " vs " + String.valueOf(this.material));
      } else {
         INMS.get().deserializeTile(this.properties, var1.getLocation());
      }
   }

   public TileData fromBukkit(Block block) {
      if (this.material != null && var1.getType() != this.material) {
         String var10002 = String.valueOf(var1.getType());
         throw new IllegalStateException("Material mismatch: " + var10002 + " vs " + String.valueOf(this.material));
      } else {
         if (this.material == null) {
            this.material = var1.getType();
         }

         this.properties = INMS.get().serializeTile(var1.getLocation());
         return this;
      }
   }

   public boolean toBukkitTry(Block block) {
      try {
         this.toBukkit(var1);
         return true;
      } catch (Throwable var3) {
         Iris.reportError(var3);
         return false;
      }
   }

   public boolean fromBukkitTry(Block block) {
      try {
         this.fromBukkit(var1);
         return true;
      } catch (Throwable var3) {
         Iris.reportError(var3);
         return false;
      }
   }

   public void toBinary(DataOutputStream out) {
      var1.writeUTF(this.material == null ? "" : this.material.getKey().toString());
      var1.writeUTF(gson.toJson(this.properties));
   }

   public TileData clone() {
      TileData var1 = new TileData();
      var1.material = this.material;
      var1.properties = this.properties.copy();
      return var1;
   }

   public String toString() {
      String var10000 = String.valueOf(this.material.getKey());
      return var10000 + gson.toJson(this.properties);
   }

   @NonNull
   @Generated
   public Material getMaterial() {
      return this.material;
   }

   @NonNull
   @Generated
   public KMap<String, Object> getProperties() {
      return this.properties;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof TileData)) {
         return false;
      } else {
         TileData var2 = (TileData)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            Material var3 = this.getMaterial();
            Material var4 = var2.getMaterial();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            KMap var5 = this.getProperties();
            KMap var6 = var2.getProperties();
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
      return var1 instanceof TileData;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      Material var3 = this.getMaterial();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      KMap var4 = this.getProperties();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public TileData(@NonNull final Material material, @NonNull final KMap<String, Object> properties) {
      if (var1 == null) {
         throw new NullPointerException("material is marked non-null but is null");
      } else if (var2 == null) {
         throw new NullPointerException("properties is marked non-null but is null");
      } else {
         this.material = var1;
         this.properties = var2;
      }
   }

   @Generated
   protected TileData() {
   }
}
