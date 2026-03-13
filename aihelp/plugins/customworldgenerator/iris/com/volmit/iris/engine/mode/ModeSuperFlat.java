package com.volmit.iris.engine.mode;

import com.volmit.iris.engine.actuator.IrisBiomeActuator;
import com.volmit.iris.engine.actuator.IrisTerrainNormalActuator;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineMode;
import com.volmit.iris.engine.framework.EngineStage;
import com.volmit.iris.engine.framework.IrisEngineMode;

public class ModeSuperFlat extends IrisEngineMode implements EngineMode {
   public ModeSuperFlat(Engine engine) {
      super(var1);
      IrisTerrainNormalActuator var2 = new IrisTerrainNormalActuator(this.getEngine());
      IrisBiomeActuator var3 = new IrisBiomeActuator(this.getEngine());
      this.registerStage(this.burst(new EngineStage[]{(var1x, var2x, var3x, var4, var5, var6) -> {
         var2.actuate(var1x, var2x, var3x, var5, var6);
      }, (var1x, var2x, var3x, var4, var5, var6) -> {
         var3.actuate(var1x, var2x, var4, var5, var6);
      }}));
   }
}
