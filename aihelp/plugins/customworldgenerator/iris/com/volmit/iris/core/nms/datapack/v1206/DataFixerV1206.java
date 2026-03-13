package com.volmit.iris.core.nms.datapack.v1206;

import com.volmit.iris.core.nms.datapack.IDataFixer;
import com.volmit.iris.core.nms.datapack.v1192.DataFixerV1192;
import com.volmit.iris.engine.object.IrisBiomeCustom;
import com.volmit.iris.engine.object.IrisBiomeCustomSpawn;
import com.volmit.iris.engine.object.IrisBiomeCustomSpawnType;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;
import java.util.Iterator;
import java.util.Locale;

public class DataFixerV1206 extends DataFixerV1192 {
   public JSONObject fixCustomBiome(IrisBiomeCustom biome, JSONObject json) {
      int var3 = var1.getSpawnRarity();
      if (var3 > 0) {
         var2.put("creature_spawn_probability", Math.min((double)var3 / 20.0D, 0.9999999D));
      } else {
         var2.remove("creature_spawn_probability");
      }

      KList var4 = var1.getSpawns();
      if (var4 != null && var4.isNotEmpty()) {
         JSONObject var5 = new JSONObject();
         KMap var6 = new KMap();
         Iterator var7 = var4.iterator();

         while(var7.hasNext()) {
            IrisBiomeCustomSpawn var8 = (IrisBiomeCustomSpawn)var7.next();
            JSONArray var9 = (JSONArray)var6.computeIfAbsent(var8.getGroup(), (var0) -> {
               return new JSONArray();
            });
            JSONObject var10 = new JSONObject();
            var10.put("type", (Object)var8.getType().getKey());
            var10.put("weight", var8.getWeight());
            var10.put("minCount", var8.getMinCount());
            var10.put("maxCount", var8.getMaxCount());
            var9.put((Object)var10);
         }

         var7 = var6.k().iterator();

         while(var7.hasNext()) {
            IrisBiomeCustomSpawnType var11 = (IrisBiomeCustomSpawnType)var7.next();
            var5.put(var11.name().toLowerCase(Locale.ROOT), var6.get(var11));
         }

         var2.put("spawners", (Object)var5);
      }

      return var2;
   }

   public void fixDimension(IDataFixer.Dimension dimension, JSONObject json) {
      super.fixDimension(var1, var2);
      Object var4 = var2.get("monster_spawn_light_level");
      if (var4 instanceof JSONObject) {
         JSONObject var3 = (JSONObject)var4;
         JSONObject var5 = (JSONObject)var3.remove("value");
         var3.put("max_inclusive", var5.get("max_inclusive"));
         var3.put("min_inclusive", var5.get("min_inclusive"));
      }
   }
}
