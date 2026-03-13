package com.volmit.iris.core.commands;

import com.volmit.iris.Iris;
import com.volmit.iris.core.pregenerator.TurboPregenerator;
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
   name = "turbopregen",
   aliases = {"turbo"},
   description = "Pregenerate your Iris worlds!"
)
public class CommandTurboPregen implements DecreeExecutor {
   public String worldName;

   @Decree(
      description = "Pregenerate a world"
   )
   public void start(@Param(description = "The radius of the pregen in blocks",aliases = {"size"}) int radius, @Param(description = "The world to pregen",contextual = true) World world, @Param(aliases = {"middle"},description = "The center location of the pregen. Use \"me\" for your current location",defaultValue = "0,0") Vector center) {
      this.worldName = var2.getName();
      File var4 = new File(Bukkit.getWorldContainer(), var2.getName());
      File var5 = new File(var4, "turbogen.json");
      if (var5.exists()) {
         if (TurboPregenerator.getInstance() != null) {
            this.sender().sendMessage(String.valueOf(C.BLUE) + "Turbo pregen is already in progress");
            Iris.info(String.valueOf(C.YELLOW) + "Turbo pregen is already in progress");
            return;
         }

         try {
            var5.delete();
         } catch (Exception var11) {
            Iris.error("Failed to delete the old instance file of Turbo Pregen!");
            return;
         }
      }

      try {
         if (this.sender().isPlayer() && this.access() == null) {
            this.sender().sendMessage(String.valueOf(C.RED) + "The engine access for this world is null!");
            this.sender().sendMessage(String.valueOf(C.RED) + "Please make sure the world is loaded & the engine is initialized. Generate a new chunk, for example.");
         }

         TurboPregenerator.TurboPregenJob var6 = TurboPregenerator.TurboPregenJob.builder().world(this.worldName).radiusBlocks(var1).position(0).build();
         File var7 = new File(var4, "turbogen.json");
         TurboPregenerator var8 = new TurboPregenerator(var6, var7);
         var8.start();
         String var10000 = String.valueOf(C.GREEN);
         String var9 = var10000 + "TurboPregen started in " + String.valueOf(C.GOLD) + this.worldName + String.valueOf(C.GREEN) + " of " + String.valueOf(C.GOLD) + var1 * 2 + String.valueOf(C.GREEN) + " by " + String.valueOf(C.GOLD) + var1 * 2 + String.valueOf(C.GREEN) + " blocks from " + String.valueOf(C.GOLD) + var3.getX() + "," + var3.getZ();
         this.sender().sendMessage(var9);
         Iris.info(var9);
      } catch (Throwable var10) {
         this.sender().sendMessage(String.valueOf(C.RED) + "Epic fail. See console.");
         Iris.reportError(var10);
         var10.printStackTrace();
      }

   }

   @Decree(
      description = "Stop the active pregeneration task",
      aliases = {"x"}
   )
   public void stop(@Param(aliases = {"world"},description = "The world to pause") World world) {
      TurboPregenerator var2 = TurboPregenerator.getInstance();
      File var3 = new File(Bukkit.getWorldContainer(), var1.getName());
      File var4 = new File(var3, "turbogen.json");
      VolmitSender var10000;
      String var10001;
      if (var2 != null) {
         var2.shutdownInstance(var1);
         var10000 = this.sender();
         var10001 = String.valueOf(C.LIGHT_PURPLE);
         var10000.sendMessage(var10001 + "Closed Turbogen instance for " + var1.getName());
      } else if (var4.exists() && var4.delete()) {
         var10000 = this.sender();
         var10001 = String.valueOf(C.LIGHT_PURPLE);
         var10000.sendMessage(var10001 + "Closed Turbogen instance for " + var1.getName());
      } else if (var4.exists()) {
         Iris.error("Failed to delete the old instance file of Turbo Pregen!");
      } else {
         this.sender().sendMessage(String.valueOf(C.YELLOW) + "No active pregeneration tasks to stop");
      }

   }

   @Decree(
      description = "Pause / continue the active pregeneration task",
      aliases = {"t", "resume", "unpause"}
   )
   public void pause(@Param(aliases = {"world"},description = "The world to pause") World world) {
      if (TurboPregenerator.getInstance() != null) {
         TurboPregenerator.setPausedTurbo(var1);
         VolmitSender var10000 = this.sender();
         String var10001 = String.valueOf(C.GREEN);
         var10000.sendMessage(var10001 + "Paused/unpaused Turbo Pregen, now: " + (TurboPregenerator.isPausedTurbo(var1) ? "Paused" : "Running") + ".");
      } else {
         File var2 = new File(Bukkit.getWorldContainer(), var1.getName());
         File var3 = new File(var2, "turbogen.json");
         if (var3.exists()) {
            TurboPregenerator.loadTurboGenerator(var1.getName());
            this.sender().sendMessage(String.valueOf(C.YELLOW) + "Started Turbo Pregen back up!");
         } else {
            this.sender().sendMessage(String.valueOf(C.YELLOW) + "No active Turbo Pregen tasks to pause/unpause.");
         }
      }

   }
}
