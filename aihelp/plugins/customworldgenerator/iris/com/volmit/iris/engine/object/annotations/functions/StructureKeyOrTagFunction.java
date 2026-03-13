package com.volmit.iris.engine.object.annotations.functions;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.engine.framework.ListFunction;
import com.volmit.iris.util.collection.KList;

public class StructureKeyOrTagFunction implements ListFunction<KList<String>> {
   public String key() {
      return "structure-key-or-tag";
   }

   public String fancyName() {
      return "Structure Key or Tag";
   }

   public KList<String> apply(IrisData irisData) {
      return INMS.get().getStructureKeys();
   }
}
