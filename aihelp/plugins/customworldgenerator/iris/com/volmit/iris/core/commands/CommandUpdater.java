package com.volmit.iris.core.commands;

import com.volmit.iris.Iris;
import com.volmit.iris.core.pregenerator.ChunkUpdater;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.util.decree.DecreeExecutor;
import com.volmit.iris.util.decree.DecreeOrigin;
import com.volmit.iris.util.decree.annotations.Decree;
import com.volmit.iris.util.decree.annotations.Param;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.plugin.VolmitSender;
import org.bukkit.World;

@Decree(
   name = "updater",
   origin = DecreeOrigin.BOTH,
   description = "Iris World Updater"
)
public class CommandUpdater implements DecreeExecutor {
   private final Object lock = new Object();
   private transient ChunkUpdater chunkUpdater;

   @Decree(
      description = "Updates all chunk in the specified world"
   )
   public void start(@Param(description = "World to update chunks at",contextual = true) World world) {
      if (!IrisToolbelt.isIrisWorld(var1)) {
         this.sender().sendMessage(String.valueOf(C.GOLD) + "This is not an Iris world");
      } else {
         synchronized(this.lock) {
            if (this.chunkUpdater != null) {
               this.chunkUpdater.stop();
            }

            this.chunkUpdater = new ChunkUpdater(var1);
            if (this.sender().isPlayer()) {
               VolmitSender var10000 = this.sender();
               String var10001 = String.valueOf(C.GREEN);
               var10000.sendMessage(var10001 + "Updating " + var1.getName() + String.valueOf(C.GRAY) + " Total chunks: " + Form.f(this.chunkUpdater.getChunks()));
            } else {
               String var5 = String.valueOf(C.GREEN);
               Iris.info(var5 + "Updating " + var1.getName() + String.valueOf(C.GRAY) + " Total chunks: " + Form.f(this.chunkUpdater.getChunks()));
            }

            this.chunkUpdater.start();
         }
      }
   }

   @Decree(
      description = "Pause the updater"
   )
   public void pause() {
      synchronized(this.lock) {
         if (this.chunkUpdater == null) {
            this.sender().sendMessage(String.valueOf(C.GOLD) + "You cant pause something that doesnt exist?");
         } else {
            boolean var2 = this.chunkUpdater.pause();
            if (this.sender().isPlayer()) {
               VolmitSender var10000;
               String var10001;
               if (var2) {
                  var10000 = this.sender();
                  var10001 = String.valueOf(C.IRIS);
                  var10000.sendMessage(var10001 + "Paused task for: " + String.valueOf(C.GRAY) + this.chunkUpdater.getName());
               } else {
                  var10000 = this.sender();
                  var10001 = String.valueOf(C.IRIS);
                  var10000.sendMessage(var10001 + "Unpause task for: " + String.valueOf(C.GRAY) + this.chunkUpdater.getName());
               }
            } else {
               String var5;
               if (var2) {
                  var5 = String.valueOf(C.IRIS);
                  Iris.info(var5 + "Paused task for: " + String.valueOf(C.GRAY) + this.chunkUpdater.getName());
               } else {
                  var5 = String.valueOf(C.IRIS);
                  Iris.info(var5 + "Unpause task for: " + String.valueOf(C.GRAY) + this.chunkUpdater.getName());
               }
            }

         }
      }
   }

   @Decree(
      description = "Stops the updater"
   )
   public void stop() {
      synchronized(this.lock) {
         if (this.chunkUpdater == null) {
            this.sender().sendMessage(String.valueOf(C.GOLD) + "You cant stop something that doesnt exist?");
         } else {
            if (this.sender().isPlayer()) {
               VolmitSender var10000 = this.sender();
               String var10001 = String.valueOf(C.GRAY);
               var10000.sendMessage("Stopping Updater for: " + var10001 + this.chunkUpdater.getName());
            } else {
               Iris.info("Stopping Updater for: " + String.valueOf(C.GRAY) + this.chunkUpdater.getName());
            }

            this.chunkUpdater.stop();
         }
      }
   }
}
