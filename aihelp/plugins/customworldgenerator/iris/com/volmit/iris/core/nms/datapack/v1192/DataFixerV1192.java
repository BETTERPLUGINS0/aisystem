package com.volmit.iris.core.nms.datapack.v1192;

import com.volmit.iris.core.nms.datapack.IDataFixer;
import com.volmit.iris.engine.object.IrisDimensionTypeOptions;
import com.volmit.iris.util.json.JSONObject;
import java.util.Iterator;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public class DataFixerV1192 implements IDataFixer {
   private static final Map<IDataFixer.Dimension, IrisDimensionTypeOptions> OPTIONS;
   private static final Map<IDataFixer.Dimension, String> DIMENSIONS;

   public JSONObject resolve(IDataFixer.Dimension dimension, @Nullable IrisDimensionTypeOptions options) {
      return var2 == null ? ((IrisDimensionTypeOptions)OPTIONS.get(var1)).toJson() : var2.resolve((IrisDimensionTypeOptions)OPTIONS.get(var1)).toJson();
   }

   public void fixDimension(IDataFixer.Dimension dimension, JSONObject json) {
      JSONObject var3 = new JSONObject((String)DIMENSIONS.get(var1));
      Iterator var4 = var3.keySet().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         if (!var2.has(var5)) {
            var2.put(var5, var3.get(var5));
         }
      }

   }

   static {
      OPTIONS = Map.of(IDataFixer.Dimension.OVERWORLD, new IrisDimensionTypeOptions(IrisDimensionTypeOptions.TriState.FALSE, IrisDimensionTypeOptions.TriState.TRUE, IrisDimensionTypeOptions.TriState.FALSE, IrisDimensionTypeOptions.TriState.FALSE, IrisDimensionTypeOptions.TriState.TRUE, IrisDimensionTypeOptions.TriState.TRUE, IrisDimensionTypeOptions.TriState.TRUE, IrisDimensionTypeOptions.TriState.FALSE, 1.0D, 0.0F, (Long)null, 192, 0), IDataFixer.Dimension.NETHER, new IrisDimensionTypeOptions(IrisDimensionTypeOptions.TriState.TRUE, IrisDimensionTypeOptions.TriState.FALSE, IrisDimensionTypeOptions.TriState.TRUE, IrisDimensionTypeOptions.TriState.TRUE, IrisDimensionTypeOptions.TriState.FALSE, IrisDimensionTypeOptions.TriState.FALSE, IrisDimensionTypeOptions.TriState.FALSE, IrisDimensionTypeOptions.TriState.TRUE, 8.0D, 0.1F, 18000L, (Integer)null, 15), IDataFixer.Dimension.END, new IrisDimensionTypeOptions(IrisDimensionTypeOptions.TriState.FALSE, IrisDimensionTypeOptions.TriState.FALSE, IrisDimensionTypeOptions.TriState.FALSE, IrisDimensionTypeOptions.TriState.FALSE, IrisDimensionTypeOptions.TriState.FALSE, IrisDimensionTypeOptions.TriState.TRUE, IrisDimensionTypeOptions.TriState.FALSE, IrisDimensionTypeOptions.TriState.FALSE, 1.0D, 0.0F, 6000L, (Integer)null, 0));
      DIMENSIONS = Map.of(IDataFixer.Dimension.OVERWORLD, "{\n  \"effects\": \"minecraft:overworld\",\n  \"infiniburn\": \"#minecraft:infiniburn_overworld\",\n  \"monster_spawn_light_level\": {\n    \"type\": \"minecraft:uniform\",\n    \"value\": {\n      \"max_inclusive\": 7,\n      \"min_inclusive\": 0\n    }\n  }\n}", IDataFixer.Dimension.NETHER, "{\n  \"effects\": \"minecraft:the_nether\",\n  \"infiniburn\": \"#minecraft:infiniburn_nether\",\n  \"monster_spawn_light_level\": 7,\n}", IDataFixer.Dimension.END, "{\n  \"effects\": \"minecraft:the_end\",\n  \"infiniburn\": \"#minecraft:infiniburn_end\",\n  \"monster_spawn_light_level\": {\n    \"type\": \"minecraft:uniform\",\n    \"value\": {\n      \"max_inclusive\": 7,\n      \"min_inclusive\": 0\n    }\n  }\n}");
   }
}
