package com.volmit.iris.core.nms.datapack;

import com.volmit.iris.engine.object.IrisBiomeCustom;
import com.volmit.iris.engine.object.IrisDimensionTypeOptions;
import com.volmit.iris.util.json.JSONObject;
import org.jetbrains.annotations.Nullable;

public interface IDataFixer {
   default JSONObject fixCustomBiome(IrisBiomeCustom biome, JSONObject json) {
      return json;
   }

   JSONObject resolve(IDataFixer.Dimension dimension, @Nullable IrisDimensionTypeOptions options);

   void fixDimension(IDataFixer.Dimension dimension, JSONObject json);

   default JSONObject createDimension(IDataFixer.Dimension base, int minY, int height, int logicalHeight, @Nullable IrisDimensionTypeOptions options) {
      JSONObject obj = this.resolve(base, options);
      obj.put("min_y", minY);
      obj.put("height", height);
      obj.put("logical_height", logicalHeight);
      this.fixDimension(base, obj);
      return obj;
   }

   public static enum Dimension {
      OVERWORLD,
      NETHER,
      END;

      // $FF: synthetic method
      private static IDataFixer.Dimension[] $values() {
         return new IDataFixer.Dimension[]{OVERWORLD, NETHER, END};
      }
   }
}
