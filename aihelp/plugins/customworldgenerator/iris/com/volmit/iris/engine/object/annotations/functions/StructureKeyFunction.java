package com.volmit.iris.engine.object.annotations.functions;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.engine.framework.ListFunction;
import com.volmit.iris.util.collection.KList;

public class StructureKeyFunction implements ListFunction<KList<String>> {
   public String key() {
      return "structure-key";
   }

   public String fancyName() {
      return "Structure Key";
   }

   public KList<String> apply(IrisData irisData) {
      return INMS.get().getStructureKeys().removeWhere((var0) -> {
         return var0.startsWith("#");
      });
   }
}
