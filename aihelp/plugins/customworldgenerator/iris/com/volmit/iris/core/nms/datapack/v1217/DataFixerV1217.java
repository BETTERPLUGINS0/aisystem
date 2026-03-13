package com.volmit.iris.core.nms.datapack.v1217;

import com.volmit.iris.core.nms.datapack.IDataFixer;
import com.volmit.iris.core.nms.datapack.v1213.DataFixerV1213;
import com.volmit.iris.engine.object.IrisBiomeCustom;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;
import java.util.Iterator;
import java.util.Map;

public class DataFixerV1217 extends DataFixerV1213 {
   private static final Map<IDataFixer.Dimension, String> DIMENSIONS;

   public JSONObject fixCustomBiome(IrisBiomeCustom biome, JSONObject json) {
      var2 = super.fixCustomBiome(var1, var2);
      JSONObject var3 = var2.getJSONObject("effects");
      JSONObject var4 = new JSONObject();
      var4.put("minecraft:visual/fog_color", var3.remove("fog_color"));
      var4.put("minecraft:visual/sky_color", var3.remove("sky_color"));
      var4.put("minecraft:visual/water_fog_color", var3.remove("water_fog_color"));
      JSONObject var5 = (JSONObject)var3.remove("particle");
      if (var5 != null) {
         var4.put("minecraft:visual/ambient_particles", (Object)(new JSONArray()).put((Object)var5.getJSONObject("options").put("probability", var5.get("probability"))));
      }

      var2.put("attributes", (Object)var4);
      return var2;
   }

   public void fixDimension(IDataFixer.Dimension dimension, JSONObject json) {
      super.fixDimension(var1, var2);
      JSONObject var3 = new JSONObject();
      if ((Boolean)var2.remove("ultrawarm")) {
         var3.put("minecraft:gameplay/water_evaporates", true);
         var3.put("minecraft:gameplay/fast_lava", true);
         var3.put("minecraft:gameplay/snow_golem_melts", true);
         var3.put("minecraft:visual/default_dripstone_particle", (Object)(new JSONObject()).put("value", (Object)"minecraft:dripstone_drip_water_lava"));
      }

      if ((Boolean)var2.remove("bed_works")) {
         var3.put("minecraft:gameplay/bed_rule", (Object)(new JSONObject()).put("can_set_spawn", (Object)"always").put("can_sleep", (Object)"when_dark").put("error_message", (Object)(new JSONObject()).put("translate", (Object)"block.minecraft.bed.no_sleep")));
      } else {
         var3.put("minecraft:gameplay/bed_rule", (Object)(new JSONObject()).put("can_set_spawn", (Object)"never").put("can_sleep", (Object)"never").put("explodes", true));
      }

      var3.put("minecraft:gameplay/respawn_anchor_works", var2.remove("respawn_anchor_works"));
      var3.put("minecraft:gameplay/piglins_zombify", var2.remove("piglin_safe"));
      var3.put("minecraft:gameplay/can_start_raid", var2.remove("has_raids"));
      Object var4 = var2.remove("cloud_height");
      if (var4 != null) {
         var3.put("minecraft:visual/cloud_height", var4);
      }

      boolean var5 = (Boolean)var2.remove("natural");
      var3.put("minecraft:gameplay/nether_portal_spawns_piglin", var5);
      if (var5 != (var1 == IDataFixer.Dimension.OVERWORLD)) {
         var3.put("minecraft:gameplay/eyeblossom_open", var5);
         var3.put("minecraft:gameplay/creaking_active", var5);
      }

      var2.put("attributes", (Object)var3);
      var2.remove("effects");
      JSONObject var6 = new JSONObject((String)DIMENSIONS.get(var1));
      this.merge(var2, var6);
   }

   private void merge(JSONObject base, JSONObject override) {
      Iterator var3 = var2.keySet().iterator();

      while(true) {
         label39:
         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            Object var5 = var1.opt(var4);
            byte var6 = 0;

            while(true) {
               switch(((Class)var5).typeSwitch<invokedynamic>(var5, var6)) {
               case -1:
                  var1.put(var4, var2.opt(var4));
                  continue label39;
               case 0:
                  JSONObject var7 = (JSONObject)var5;
                  Object var14 = var2.opt(var4);
                  if (var14 instanceof JSONObject) {
                     JSONObject var8 = (JSONObject)var14;
                     this.merge(var7, var8);
                     continue label39;
                  }

                  var6 = 1;
                  break;
               case 1:
                  JSONArray var9 = (JSONArray)var5;
                  Object var11 = var2.opt(var4);
                  if (!(var11 instanceof JSONArray)) {
                     var6 = 2;
                     break;
                  } else {
                     JSONArray var10 = (JSONArray)var11;
                     Iterator var13 = var10.iterator();

                     while(var13.hasNext()) {
                        Object var12 = var13.next();
                        var9.put(var12);
                     }
                  }
               default:
                  continue label39;
               }
            }
         }

         return;
      }
   }

   static {
      DIMENSIONS = Map.of(IDataFixer.Dimension.OVERWORLD, "{\n  \"ambient_light\": 0.0,\n  \"attributes\": {\n    \"minecraft:audio/ambient_sounds\": {\n      \"mood\": {\n        \"block_search_extent\": 8,\n        \"offset\": 2.0,\n        \"sound\": \"minecraft:ambient.cave\",\n        \"tick_delay\": 6000\n      }\n    },\n    \"minecraft:audio/background_music\": {\n      \"creative\": {\n        \"max_delay\": 24000,\n        \"min_delay\": 12000,\n        \"sound\": \"minecraft:music.creative\"\n      },\n      \"default\": {\n        \"max_delay\": 24000,\n        \"min_delay\": 12000,\n        \"sound\": \"minecraft:music.game\"\n      }\n    },\n    \"minecraft:visual/cloud_color\": \"#ccffffff\",\n    \"minecraft:visual/fog_color\": \"#c0d8ff\",\n    \"minecraft:visual/sky_color\": \"#78a7ff\"\n  },\n  \"timelines\": \"#minecraft:in_overworld\"\n}", IDataFixer.Dimension.NETHER, "{\n  \"ambient_light\": 0.1,\n  \"attributes\": {\n    \"minecraft:gameplay/sky_light_level\": 4.0,\n    \"minecraft:gameplay/snow_golem_melts\": true,\n    \"minecraft:visual/fog_end_distance\": 96.0,\n    \"minecraft:visual/fog_start_distance\": 10.0,\n    \"minecraft:visual/sky_light_color\": \"#7a7aff\",\n    \"minecraft:visual/sky_light_factor\": 0.0\n  },\n  \"cardinal_light\": \"nether\",\n  \"skybox\": \"none\",\n  \"timelines\": \"#minecraft:in_nether\"\n}", IDataFixer.Dimension.END, "{\n  \"ambient_light\": 0.25,\n  \"attributes\": {\n    \"minecraft:audio/ambient_sounds\": {\n      \"mood\": {\n        \"block_search_extent\": 8,\n        \"offset\": 2.0,\n        \"sound\": \"minecraft:ambient.cave\",\n        \"tick_delay\": 6000\n      }\n    },\n    \"minecraft:audio/background_music\": {\n      \"default\": {\n        \"max_delay\": 24000,\n        \"min_delay\": 6000,\n        \"replace_current_music\": true,\n        \"sound\": \"minecraft:music.end\"\n      }\n    },\n    \"minecraft:visual/fog_color\": \"#181318\",\n    \"minecraft:visual/sky_color\": \"#000000\",\n    \"minecraft:visual/sky_light_color\": \"#e580ff\",\n    \"minecraft:visual/sky_light_factor\": 0.0\n  },\n  \"skybox\": \"end\",\n  \"timelines\": \"#minecraft:in_end\"\n}");
   }
}
