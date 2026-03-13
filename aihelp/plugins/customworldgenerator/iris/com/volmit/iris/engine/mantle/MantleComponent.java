package com.volmit.iris.engine.mantle;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.IrisComplex;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.mantle.flag.MantleFlag;
import com.volmit.iris.util.parallel.BurstExecutor;
import org.jetbrains.annotations.NotNull;

public interface MantleComponent extends Comparable<MantleComponent> {
   int getPriority();

   int getRadius();

   default IrisData getData() {
      return this.getEngineMantle().getData();
   }

   default IrisDimension getDimension() {
      return this.getEngineMantle().getEngine().getDimension();
   }

   default IrisComplex getComplex() {
      return this.getEngineMantle().getComplex();
   }

   default long seed() {
      return this.getEngineMantle().getEngine().getSeedManager().getMantle();
   }

   default BurstExecutor burst() {
      return this.getEngineMantle().getEngine().burst().burst();
   }

   EngineMantle getEngineMantle();

   default Mantle getMantle() {
      return this.getEngineMantle().getMantle();
   }

   MantleFlag getFlag();

   boolean isEnabled();

   void setEnabled(boolean b);

   void hotload();

   @ChunkCoordinates
   void generateLayer(MantleWriter writer, int x, int z, ChunkContext context);

   default int compareTo(@NotNull MantleComponent o) {
      return Integer.compare(this.getPriority(), o.getPriority());
   }
}
