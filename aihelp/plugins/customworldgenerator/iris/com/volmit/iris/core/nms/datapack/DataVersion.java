package com.volmit.iris.core.nms.datapack;

import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.nms.datapack.v1192.DataFixerV1192;
import com.volmit.iris.core.nms.datapack.v1206.DataFixerV1206;
import com.volmit.iris.core.nms.datapack.v1213.DataFixerV1213;
import com.volmit.iris.core.nms.datapack.v1217.DataFixerV1217;
import com.volmit.iris.util.collection.KMap;
import java.util.function.Supplier;
import lombok.Generated;

public enum DataVersion {
   UNSUPPORTED("0.0.0", 0, () -> {
      return null;
   }),
   V1_19_2("1.19.2", 10, DataFixerV1192::new),
   V1_20_5("1.20.6", 41, DataFixerV1206::new),
   V1_21_3("1.21.3", 57, DataFixerV1213::new),
   V1_21_11("1.21.11", 75, DataFixerV1217::new);

   private static final KMap<DataVersion, IDataFixer> cache = new KMap();
   private final Supplier<IDataFixer> constructor;
   private final String version;
   private final int packFormat;

   private DataVersion(String version, int packFormat, Supplier<IDataFixer> constructor) {
      this.constructor = var5;
      this.packFormat = var4;
      this.version = var3;
   }

   public IDataFixer get() {
      return (IDataFixer)cache.computeIfAbsent(this, (var1) -> {
         return (IDataFixer)this.constructor.get();
      });
   }

   public static IDataFixer getDefault() {
      return INMS.get().getDataVersion().get();
   }

   public static DataVersion getLatest() {
      return values()[values().length - 1];
   }

   @Generated
   public String getVersion() {
      return this.version;
   }

   @Generated
   public int getPackFormat() {
      return this.packFormat;
   }

   // $FF: synthetic method
   private static DataVersion[] $values() {
      return new DataVersion[]{UNSUPPORTED, V1_19_2, V1_20_5, V1_21_3, V1_21_11};
   }
}
