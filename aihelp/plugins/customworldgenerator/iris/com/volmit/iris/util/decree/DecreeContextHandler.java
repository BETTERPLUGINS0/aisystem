package com.volmit.iris.util.decree;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.plugin.VolmitSender;

public interface DecreeContextHandler<T> {
   KMap<Class<?>, DecreeContextHandler<?>> contextHandlers = buildContextHandlers();

   static KMap<Class<?>, DecreeContextHandler<?>> buildContextHandlers() {
      KMap contextHandlers = new KMap();

      try {
         Iris.initialize("com.volmit.iris.util.decree.context").forEach((i) -> {
            contextHandlers.put(((DecreeContextHandler)i).getType(), (DecreeContextHandler)i);
         });
      } catch (Throwable var2) {
         Iris.reportError(var2);
         var2.printStackTrace();
      }

      return contextHandlers;
   }

   Class<T> getType();

   T handle(VolmitSender sender);
}
