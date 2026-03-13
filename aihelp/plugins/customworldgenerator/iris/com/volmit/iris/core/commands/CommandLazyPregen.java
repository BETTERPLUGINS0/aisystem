package com.volmit.iris.core.commands;

import com.volmit.iris.Iris;
import com.volmit.iris.core.pregenerator.LazyPregenerator;
import com.volmit.iris.util.decree.DecreeExecutor;
import com.volmit.iris.util.decree.annotations.Decree;
import com.volmit.iris.util.decree.annotations.Param;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.plugin.VolmitSender;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.Vector;

@Decree(
   name = "lazypregen",
   aliases = {"lazy"},
   description = "Pregenerate your Iris worlds!"
)
public class CommandLazyPregen implements DecreeExecutor {
   public String worldName;

   @Decree(
      description = "Pregenerate a world"
   )
   public void start(@Param(description = "The radius of the pregen in blocks",aliases = {"size"}) int radius, @Param(description = "The world to pregen",contextual = true) World world, @Param(aliases = {"middle"},description = "The center location of the pregen. Use \"me\" for your current location",defaultValue = "0,0") Vector center, @Param(aliases = {"maxcpm"},description = "Limit the chunks per minute the pregen will generate",defaultValue = "999999999") int cpm, @Param(aliases = {"silent"},description = "Silent generation",defaultValue = "false") boolean silent) {
      this.worldName = var2.getName();
      File var6 = new File(Bukkit.getWorldContainer(), var2.getName());
      File var7 = new File(var6, "lazygen.json");
      if (var7.exists()) {
         this.sender().sendMessage(String.valueOf(C.BLUE) + "Lazy pregen is already in progress");
         Iris.info(String.valueOf(C.YELLOW) + "Lazy pregen is already in progress");
      } else {
         try {
            if (this.sender().isPlayer() && this.access() == null) {
               this.sender().sendMessage(String.valueOf(C.RED) + "The engine access for this world is null!");
               this.sender().sendMessage(String.valueOf(C.RED) + "Please make sure the world is loaded & the engine is initialized. Generate a new chunk, for example.");
            }

            LazyPregenerator.LazyPregenJob var8 = LazyPregenerator.LazyPregenJob.builder().world(this.worldName).healingPosition(0).healing(false).chunksPerMinute(var4).radiusBlocks(var1).position(0).silent(var5).build();
            File var9 = new File(var6, "lazygen.json");
            LazyPregenerator var10 = new LazyPregenerator(var8, var9);
            var10.start();
            String var10000 = String.valueOf(C.GREEN);
            String var11 = var10000 + "LazyPregen started in " + String.valueOf(C.GOLD) + this.worldName + String.valueOf(C.GREEN) + " of " + String.valueOf(C.GOLD) + var1 * 2 + String.valueOf(C.GREEN) + " by " + String.valueOf(C.GOLD) + var1 * 2 + String.valueOf(C.GREEN) + " blocks from " + String.valueOf(C.GOLD) + var3.getX() + "," + var3.getZ();
            this.sender().sendMessage(var11);
            Iris.info(var11);
         } catch (Throwable var12) {
            this.sender().sendMessage(String.valueOf(C.RED) + "Epic fail. See console.");
            Iris.reportError(var12);
            var12.printStackTrace();
         }

      }
   }

   @Decree(
      description = "Stop the active pregeneration task",
      aliases = {"x"}
   )
   public void stop(@Param(aliases = {"world"},description = "The world to pause") World world) {
      if (LazyPregenerator.getInstance() != null) {
         LazyPregenerator.getInstance().shutdownInstance(var1);
         VolmitSender var10000 = this.sender();
         String var10001 = String.valueOf(C.LIGHT_PURPLE);
         var10000.sendMessage(var10001 + "Closed lazygen instance for " + var1.getName());
      } else {
         this.sender().sendMessage(String.valueOf(C.YELLOW) + "No active pregeneration tasks to stop");
      }

   }

   @Decree(
      description = "Pause / continue the active pregeneration task",
      aliases = {"t", "resume", "unpause"}
   )
   public void pause(@Param(aliases = {"world"},description = "The world to pause") World world) {
      if (LazyPregenerator.getInstance() != null) {
         LazyPregenerator.getInstance();
         LazyPregenerator.setPausedLazy(var1);
         VolmitSender var10000 = this.sender();
         String var10001 = String.valueOf(C.GREEN);
         LazyPregenerator.getInstance();
         var10000.sendMessage(var10001 + "Paused/unpaused Lazy Pregen, now: " + (LazyPregenerator.isPausedLazy(var1) ? "Paused" : "Running") + ".");
      } else {
         this.sender().sendMessage(String.valueOf(C.YELLOW) + "No active Lazy Pregen tasks to pause/unpause.");
      }

   }
}
