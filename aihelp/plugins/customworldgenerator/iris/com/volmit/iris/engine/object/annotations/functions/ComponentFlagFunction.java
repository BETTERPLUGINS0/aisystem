package com.volmit.iris.engine.object.annotations.functions;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.ListFunction;
import com.volmit.iris.engine.mantle.ComponentFlag;
import com.volmit.iris.engine.mantle.MantleComponent;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.mantle.flag.MantleFlag;
import java.util.Objects;
import java.util.stream.Stream;

public class ComponentFlagFunction implements ListFunction<KList<String>> {
   public String key() {
      return "component-flag";
   }

   public String fancyName() {
      return "Component Flag";
   }

   public KList<String> apply(IrisData data) {
      Engine var2 = var1.getEngine();
      if (var2 != null) {
         return var2.getMantle().getComponentFlags().toStringList();
      } else {
         Stream var10000 = Iris.getClasses("com.volmit.iris.engine.mantle.components", ComponentFlag.class).stream();
         Objects.requireNonNull(MantleComponent.class);
         return (KList)var10000.filter(MantleComponent.class::isAssignableFrom).map((var0) -> {
            return (ComponentFlag)var0.getDeclaredAnnotation(ComponentFlag.class);
         }).filter(Objects::nonNull).map(ComponentFlag::value).map(MantleFlag::toString).collect(KList.collector());
      }
   }
}
