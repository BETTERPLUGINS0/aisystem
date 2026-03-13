package com.volmit.iris.core.commands;

import com.volmit.iris.Iris;
import com.volmit.iris.core.gui.PregeneratorJob;
import com.volmit.iris.core.pregenerator.PregenTask;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.util.decree.DecreeExecutor;
import com.volmit.iris.util.decree.annotations.Decree;
import com.volmit.iris.util.decree.annotations.Param;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.plugin.VolmitSender;
import org.bukkit.World;
import org.bukkit.util.Vector;

@Decree(
   name = "pregen",
   aliases = {"pregenerate"},
   description = "Pregenerate your Iris worlds!"
)
public class CommandPregen implements DecreeExecutor {
   @Decree(
      description = "Pregenerate a world"
   )
   public void start(@Param(description = "The radius of the pregen in blocks",aliases = {"size"}) int radius, @Param(description = "The world to pregen",contextual = true) World world, @Param(aliases = {"middle"},description = "The center location of the pregen. Use \"me\" for your current location",defaultValue = "0,0") Vector center, @Param(description = "Open the Iris pregen gui",defaultValue = "true") boolean gui) {
      try {
         if (this.sender().isPlayer() && this.access() == null) {
            this.sender().sendMessage(String.valueOf(C.RED) + "The engine access for this world is null!");
            this.sender().sendMessage(String.valueOf(C.RED) + "Please make sure the world is loaded & the engine is initialized. Generate a new chunk, for example.");
         }

         var1 = Math.max(var1, 1024);
         IrisToolbelt.pregenerate(PregenTask.builder().center(new Position2(var3.getBlockX(), var3.getBlockZ())).gui(var4).radiusX(var1).radiusZ(var1).build(), var2);
         String var10000 = String.valueOf(C.GREEN);
         String var5 = var10000 + "Pregen started in " + String.valueOf(C.GOLD) + var2.getName() + String.valueOf(C.GREEN) + " of " + String.valueOf(C.GOLD) + var1 * 2 + String.valueOf(C.GREEN) + " by " + String.valueOf(C.GOLD) + var1 * 2 + String.valueOf(C.GREEN) + " blocks from " + String.valueOf(C.GOLD) + var3.getX() + "," + var3.getZ();
         this.sender().sendMessage(var5);
         Iris.info(var5);
      } catch (Throwable var6) {
         this.sender().sendMessage(String.valueOf(C.RED) + "Epic fail. See console.");
         Iris.reportError(var6);
         var6.printStackTrace();
      }

   }

   @Decree(
      description = "Stop the active pregeneration task",
      aliases = {"x"}
   )
   public void stop() {
      if (PregeneratorJob.shutdownInstance()) {
         Iris.info(String.valueOf(C.BLUE) + "Finishing up mca region...");
      } else {
         this.sender().sendMessage(String.valueOf(C.YELLOW) + "No active pregeneration tasks to stop");
      }

   }

   @Decree(
      description = "Pause / continue the active pregeneration task",
      aliases = {"t", "resume", "unpause"}
   )
   public void pause() {
      if (PregeneratorJob.pauseResume()) {
         VolmitSender var10000 = this.sender();
         String var10001 = String.valueOf(C.GREEN);
         var10000.sendMessage(var10001 + "Paused/unpaused pregeneration task, now: " + (PregeneratorJob.isPaused() ? "Paused" : "Running") + ".");
      } else {
         this.sender().sendMessage(String.valueOf(C.YELLOW) + "No active pregeneration tasks to pause/unpause.");
      }

   }
}
