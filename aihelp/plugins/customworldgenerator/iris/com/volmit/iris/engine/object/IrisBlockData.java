package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListBlockType;
import com.volmit.iris.engine.object.annotations.RegistryMapBlockState;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.plugin.VolmitSender;
import java.util.Iterator;
import java.util.Map.Entry;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

@Desc("Represents Block Data")
public class IrisBlockData extends IrisRegistrant {
   private final transient AtomicCache<BlockData> blockdata = new AtomicCache();
   private final transient AtomicCache<String> realProperties = new AtomicCache();
   @RegistryListBlockType
   @Required
   @Desc("The block to use")
   private String block = "air";
   @Desc("Debug this block by printing it to the console when it's used. Must have debug turned on in settings.")
   private boolean debug = false;
   @MinNumber(1.0D)
   @MaxNumber(1000.0D)
   @Desc("The weight is used when this block data is inside of a list of blockdata. A weight of two is just as if you placed two of the same block data values in the same list making it more common when randomly picked.")
   private int weight = 1;
   @Desc("If the block cannot be created on this version, Iris will attempt to use this backup block data instead.")
   private IrisBlockData backup = null;
   @RegistryMapBlockState("block")
   @Desc("Optional properties for this block data such as 'waterlogged': true")
   private KMap<String, Object> data = new KMap();
   @Desc("Optional tile data for this block data")
   private KMap<String, Object> tileData = new KMap();

   public IrisBlockData(String b) {
      this.block = var1;
   }

   public static IrisBlockData from(String j) {
      IrisBlockData var1 = new IrisBlockData();
      String var2 = var0.toLowerCase().trim();
      if (var2.contains("[")) {
         KList var3 = new KList();
         String var4 = var2.split("\\Q[\\E")[1].replaceAll("\\Q]\\E", "");
         var1.setBlock(var2.split("\\Q[\\E")[0]);
         if (var4.contains(",")) {
            var3.add((Object[])var4.split("\\Q,\\E"));
         } else {
            var3.add((Object)var4);
         }

         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            Object var7 = filter(var6.split("\\Q=\\E")[1]);
            var1.data.put(var6.split("\\Q=\\E")[0], var7);
         }
      } else {
         var1.setBlock(var2);
      }

      return var1;
   }

   private static Object filter(String string) {
      if (var0.equals("true")) {
         return true;
      } else if (var0.equals("false")) {
         return false;
      } else {
         try {
            return Integer.parseInt(var0);
         } catch (Throwable var3) {
            try {
               return Double.valueOf(var0).intValue();
            } catch (Throwable var2) {
               return var0;
            }
         }
      }
   }

   public String computeProperties(KMap<String, Object> data) {
      if (var1.isEmpty()) {
         return "";
      } else {
         KList var2 = new KList();
         Iterator var3 = var1.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            String var10001 = (String)var4.getKey();
            var2.add((Object)(var10001 + "=" + String.valueOf(filter(var4.getValue().toString()))));
         }

         return "[" + var2.toString(",") + "]";
      }
   }

   public String computeProperties() {
      return this.computeProperties(this.getData());
   }

   public BlockData getBlockData(IrisData data) {
      return (BlockData)this.blockdata.aquire(() -> {
         BlockData var2 = null;
         IrisBlockData var3 = (IrisBlockData)var1.getBlockLoader().load(this.getBlock(), false);
         String var10000;
         String var4;
         if (var3 != null) {
            var2 = var3.getBlockData(var1);
            if (var2 != null) {
               var2 = var2.clone();
               var4 = var2.getAsString(true);
               if (var4.contains("[")) {
                  var4 = var4.split("\\Q[\\E")[0];
               }

               KMap var5 = var3.getData().copy();
               Iterator var6 = this.getData().keySet().iterator();

               while(var6.hasNext()) {
                  String var7 = (String)var6.next();
                  var5.put(var7, this.getData().get(var7));
               }

               var10000 = this.keyify(var4);
               String var8 = var10000 + this.computeProperties(var5);
               if (this.debug) {
                  Iris.debug("Block Data used " + var8 + " (CUSTOM)");
               }

               BlockData var9 = B.get(var8);
               if (var9 != null) {
                  return var9;
               }

               if (var2 != null) {
                  return var2;
               }
            }
         }

         var10000 = this.keyify(this.getBlock());
         var4 = var10000 + this.computeProperties();
         var2 = B.get(var4);
         if (this.debug) {
            Iris.debug("Block Data used " + var4);
         }

         if (var2 != null) {
            return var2;
         } else {
            return this.backup != null ? this.backup.getBlockData(var1) : B.get("AIR");
         }
      });
   }

   public TileData tryGetTile(IrisData data) {
      Material var2 = this.getBlockData(var1).getMaterial();
      if (var2 == Material.SPAWNER && this.data.containsKey("entitySpawn")) {
         String var3 = (String)this.data.get("entitySpawn");
         if (this.tileData == null) {
            this.tileData = new KMap();
         }

         KMap var4 = (KMap)this.tileData.computeIfAbsent("SpawnData", (var0) -> {
            return new KMap();
         });
         KMap var5 = (KMap)var4.computeIfAbsent("entity", (var0) -> {
            return new KMap();
         });
         var5.putIfAbsent("id", Identifier.fromString(var3).toString());
      }

      return INMS.get().hasTile(var2) && this.tileData != null && !this.tileData.isEmpty() ? new TileData(var2, this.tileData) : null;
   }

   private String keyify(String dat) {
      return var1.contains(":") ? var1 : "minecraft:" + var1;
   }

   public String getFolderName() {
      return "blocks";
   }

   public String getTypeName() {
      return "Block";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public IrisBlockData() {
   }

   @Generated
   public IrisBlockData(final String block, final boolean debug, final int weight, final IrisBlockData backup, final KMap<String, Object> data, final KMap<String, Object> tileData) {
      this.block = var1;
      this.debug = var2;
      this.weight = var3;
      this.backup = var4;
      this.data = var5;
      this.tileData = var6;
   }

   @Generated
   public AtomicCache<BlockData> getBlockdata() {
      return this.blockdata;
   }

   @Generated
   public AtomicCache<String> getRealProperties() {
      return this.realProperties;
   }

   @Generated
   public String getBlock() {
      return this.block;
   }

   @Generated
   public boolean isDebug() {
      return this.debug;
   }

   @Generated
   public int getWeight() {
      return this.weight;
   }

   @Generated
   public IrisBlockData getBackup() {
      return this.backup;
   }

   @Generated
   public KMap<String, Object> getData() {
      return this.data;
   }

   @Generated
   public KMap<String, Object> getTileData() {
      return this.tileData;
   }

   @Generated
   public IrisBlockData setBlock(final String block) {
      this.block = var1;
      return this;
   }

   @Generated
   public IrisBlockData setDebug(final boolean debug) {
      this.debug = var1;
      return this;
   }

   @Generated
   public IrisBlockData setWeight(final int weight) {
      this.weight = var1;
      return this;
   }

   @Generated
   public IrisBlockData setBackup(final IrisBlockData backup) {
      this.backup = var1;
      return this;
   }

   @Generated
   public IrisBlockData setData(final KMap<String, Object> data) {
      this.data = var1;
      return this;
   }

   @Generated
   public IrisBlockData setTileData(final KMap<String, Object> tileData) {
      this.tileData = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getBlockdata());
      return "IrisBlockData(blockdata=" + var10000 + ", realProperties=" + String.valueOf(this.getRealProperties()) + ", block=" + this.getBlock() + ", debug=" + this.isDebug() + ", weight=" + this.getWeight() + ", backup=" + String.valueOf(this.getBackup()) + ", data=" + String.valueOf(this.getData()) + ", tileData=" + String.valueOf(this.getTileData()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisBlockData)) {
         return false;
      } else {
         IrisBlockData var2 = (IrisBlockData)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isDebug() != var2.isDebug()) {
            return false;
         } else if (this.getWeight() != var2.getWeight()) {
            return false;
         } else {
            label64: {
               String var3 = this.getBlock();
               String var4 = var2.getBlock();
               if (var3 == null) {
                  if (var4 == null) {
                     break label64;
                  }
               } else if (var3.equals(var4)) {
                  break label64;
               }

               return false;
            }

            label57: {
               IrisBlockData var5 = this.getBackup();
               IrisBlockData var6 = var2.getBackup();
               if (var5 == null) {
                  if (var6 == null) {
                     break label57;
                  }
               } else if (var5.equals(var6)) {
                  break label57;
               }

               return false;
            }

            KMap var7 = this.getData();
            KMap var8 = var2.getData();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            KMap var9 = this.getTileData();
            KMap var10 = var2.getTileData();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisBlockData;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var7 = var2 * 59 + (this.isDebug() ? 79 : 97);
      var7 = var7 * 59 + this.getWeight();
      String var3 = this.getBlock();
      var7 = var7 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisBlockData var4 = this.getBackup();
      var7 = var7 * 59 + (var4 == null ? 43 : var4.hashCode());
      KMap var5 = this.getData();
      var7 = var7 * 59 + (var5 == null ? 43 : var5.hashCode());
      KMap var6 = this.getTileData();
      var7 = var7 * 59 + (var6 == null ? 43 : var6.hashCode());
      return var7;
   }
}
