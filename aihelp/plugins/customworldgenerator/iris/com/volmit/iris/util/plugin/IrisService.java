package com.volmit.iris.util.plugin;

import com.volmit.iris.Iris;
import org.bukkit.event.Listener;

public interface IrisService extends Listener {
   void onEnable();

   void onDisable();

   default void postShutdown(Runnable r) {
      Iris.instance.postShutdown(r);
   }
}
