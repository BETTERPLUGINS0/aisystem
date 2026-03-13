package com.volmit.iris.engine.framework;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.IrisComplex;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.util.math.RollingSequence;
import com.volmit.iris.util.parallel.MultiBurst;
import org.bukkit.event.Listener;

public interface EngineComponent {
   Engine getEngine();

   RollingSequence getMetrics();

   String getName();

   default MultiBurst burst() {
      return this.getEngine().burst();
   }

   default void close() {
      try {
         if (this instanceof Listener) {
            Iris.instance.unregisterListener((Listener)this);
         }
      } catch (Throwable var2) {
         Iris.reportError(var2);
      }

   }

   default IrisData getData() {
      return this.getEngine().getData();
   }

   default EngineTarget getTarget() {
      return this.getEngine().getTarget();
   }

   default IrisDimension getDimension() {
      return this.getEngine().getDimension();
   }

   default long getSeed() {
      return this.getEngine().getSeedManager().getComponent();
   }

   default int getParallelism() {
      return this.getEngine().getParallelism();
   }

   default IrisComplex getComplex() {
      return this.getEngine().getComplex();
   }
}
