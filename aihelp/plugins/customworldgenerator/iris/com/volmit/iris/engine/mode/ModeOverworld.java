package com.volmit.iris.engine.mode;

import com.volmit.iris.engine.actuator.IrisBiomeActuator;
import com.volmit.iris.engine.actuator.IrisDecorantActuator;
import com.volmit.iris.engine.actuator.IrisTerrainNormalActuator;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineMode;
import com.volmit.iris.engine.framework.EngineStage;
import com.volmit.iris.engine.framework.IrisEngineMode;
import com.volmit.iris.engine.modifier.IrisCarveModifier;
import com.volmit.iris.engine.modifier.IrisCustomModifier;
import com.volmit.iris.engine.modifier.IrisDepositModifier;
import com.volmit.iris.engine.modifier.IrisPerfectionModifier;
import com.volmit.iris.engine.modifier.IrisPostModifier;
import org.bukkit.block.data.BlockData;

public class ModeOverworld extends IrisEngineMode implements EngineMode {
   public ModeOverworld(Engine engine) {
      super(var1);
      IrisTerrainNormalActuator var2 = new IrisTerrainNormalActuator(this.getEngine());
      IrisBiomeActuator var3 = new IrisBiomeActuator(this.getEngine());
      IrisDecorantActuator var4 = new IrisDecorantActuator(this.getEngine());
      IrisCarveModifier var5 = new IrisCarveModifier(this.getEngine());
      IrisPostModifier var6 = new IrisPostModifier(this.getEngine());
      IrisDepositModifier var7 = new IrisDepositModifier(this.getEngine());
      IrisPerfectionModifier var8 = new IrisPerfectionModifier(this.getEngine());
      IrisCustomModifier var9 = new IrisCustomModifier(this.getEngine());
      EngineStage var10 = (var1x, var2x, var3x, var4x, var5x, var6x) -> {
         var3.actuate(var1x, var2x, var4x, var5x, var6x);
      };
      EngineStage var11 = (var1x, var2x, var3x, var4x, var5x, var6x) -> {
         this.generateMatter(var1x >> 4, var2x >> 4, var5x, var6x);
      };
      EngineStage var12 = (var1x, var2x, var3x, var4x, var5x, var6x) -> {
         var2.actuate(var1x, var2x, var3x, var5x, var6x);
      };
      EngineStage var13 = (var1x, var2x, var3x, var4x, var5x, var6x) -> {
         var4.actuate(var1x, var2x, var3x, var5x, var6x);
      };
      EngineStage var14 = (var1x, var2x, var3x, var4x, var5x, var6x) -> {
         var5.modify(var1x >> 4, var2x >> 4, var3x, var5x, var6x);
      };
      EngineStage var15 = (var1x, var2x, var3x, var4x, var5x, var6x) -> {
         var7.modify(var1x, var2x, var3x, var5x, var6x);
      };
      EngineStage var16 = (var1x, var2x, var3x, var4x, var5x, var6x) -> {
         var6.modify(var1x, var2x, var3x, var5x, var6x);
      };
      EngineStage var17 = (var1x, var2x, var3x, var4x, var5x, var6x) -> {
         this.getMantle().insertMatter(var1x >> 4, var2x >> 4, BlockData.class, var3x, var5x);
      };
      EngineStage var18 = (var1x, var2x, var3x, var4x, var5x, var6x) -> {
         var8.modify(var1x, var2x, var3x, var5x, var6x);
      };
      EngineStage var19 = (var1x, var2x, var3x, var4x, var5x, var6x) -> {
         var9.modify(var1x, var2x, var3x, var5x, var6x);
      };
      this.registerStage(this.burst(new EngineStage[]{var11, var12}));
      this.registerStage(this.burst(new EngineStage[]{var14, var16}));
      this.registerStage(this.burst(new EngineStage[]{var15, var17, var13}));
      this.registerStage(var18);
      this.registerStage(var19);
   }
}
