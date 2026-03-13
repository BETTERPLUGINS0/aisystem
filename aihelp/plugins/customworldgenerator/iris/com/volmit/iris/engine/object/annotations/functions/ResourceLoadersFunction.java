package com.volmit.iris.engine.object.annotations.functions;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.ResourceLoader;
import com.volmit.iris.engine.framework.ListFunction;
import com.volmit.iris.util.collection.KList;

public class ResourceLoadersFunction implements ListFunction<KList<String>> {
   public String key() {
      return "resource-loader";
   }

   public String fancyName() {
      return "Resource Loader";
   }

   public KList<String> apply(IrisData data) {
      return (KList)var1.getLoaders().values().stream().filter((var0) -> {
         return ResourceLoader.class.equals(var0.getClass());
      }).map(ResourceLoader::getFolderName).collect(KList.collector());
   }
}
