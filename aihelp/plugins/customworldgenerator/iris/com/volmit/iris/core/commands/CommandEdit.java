package com.volmit.iris.core.commands;

import com.volmit.iris.Iris;
import com.volmit.iris.core.service.StudioSVC;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisCave;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisJigsawPiece;
import com.volmit.iris.engine.object.IrisJigsawPool;
import com.volmit.iris.engine.object.IrisJigsawStructure;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.decree.DecreeExecutor;
import com.volmit.iris.util.decree.DecreeOrigin;
import com.volmit.iris.util.decree.annotations.Decree;
import com.volmit.iris.util.decree.annotations.Param;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.plugin.VolmitSender;
import java.awt.Desktop;
import java.awt.GraphicsEnvironment;

@Decree(
   name = "edit",
   origin = DecreeOrigin.PLAYER,
   studio = true,
   description = "Edit something"
)
public class CommandEdit implements DecreeExecutor {
   private boolean noStudio() {
      if (!this.sender().isPlayer()) {
         this.sender().sendMessage(String.valueOf(C.RED) + "Players only!");
         return true;
      } else if (!((StudioSVC)Iris.service(StudioSVC.class)).isProjectOpen()) {
         this.sender().sendMessage(String.valueOf(C.RED) + "No studio world is open!");
         return true;
      } else if (!this.engine().isStudio()) {
         this.sender().sendMessage(String.valueOf(C.RED) + "You must be in a studio world!");
         return true;
      } else if (GraphicsEnvironment.isHeadless()) {
         this.sender().sendMessage(String.valueOf(C.RED) + "Cannot open files in headless environments!");
         return true;
      } else if (!Desktop.isDesktopSupported()) {
         this.sender().sendMessage(String.valueOf(C.RED) + "Desktop is not supported by this environment!");
         return true;
      } else {
         return false;
      }
   }

   @Decree(
      description = "Edit the biome you specified",
      aliases = {"b"},
      origin = DecreeOrigin.PLAYER
   )
   public void biome(@Param(contextual = false,description = "The biome to edit") IrisBiome biome) {
      if (!this.noStudio()) {
         try {
            if (var1 == null || var1.getLoadFile() == null) {
               this.sender().sendMessage(String.valueOf(C.GOLD) + "Cannot find the file; Perhaps it was not loaded directly from a file?");
               return;
            }

            Desktop.getDesktop().open(var1.getLoadFile());
            VolmitSender var10000 = this.sender();
            String var10001 = String.valueOf(C.GREEN);
            var10000.sendMessage(var10001 + "Opening " + var1.getTypeName() + " " + var1.getLoadFile().getName().split("\\Q.\\E")[0] + " in VSCode! ");
         } catch (Throwable var3) {
            Iris.reportError(var3);
            this.sender().sendMessage(String.valueOf(C.RED) + "Cant find the file. Or registrant does not exist");
         }

      }
   }

   @Decree(
      description = "Edit the region you specified",
      aliases = {"r"},
      origin = DecreeOrigin.PLAYER
   )
   public void region(@Param(contextual = false,description = "The region to edit") IrisRegion region) {
      if (!this.noStudio()) {
         try {
            if (var1 == null || var1.getLoadFile() == null) {
               this.sender().sendMessage(String.valueOf(C.GOLD) + "Cannot find the file; Perhaps it was not loaded directly from a file?");
               return;
            }

            Desktop.getDesktop().open(var1.getLoadFile());
            VolmitSender var10000 = this.sender();
            String var10001 = String.valueOf(C.GREEN);
            var10000.sendMessage(var10001 + "Opening " + var1.getTypeName() + " " + var1.getLoadFile().getName().split("\\Q.\\E")[0] + " in VSCode! ");
         } catch (Throwable var3) {
            Iris.reportError(var3);
            this.sender().sendMessage(String.valueOf(C.RED) + "Cant find the file. Or registrant does not exist");
         }

      }
   }

   @Decree(
      description = "Edit the dimension you specified",
      aliases = {"d"},
      origin = DecreeOrigin.PLAYER
   )
   public void dimension(@Param(contextual = false,description = "The dimension to edit") IrisDimension dimension) {
      if (!this.noStudio()) {
         try {
            if (var1 == null || var1.getLoadFile() == null) {
               this.sender().sendMessage(String.valueOf(C.GOLD) + "Cannot find the file; Perhaps it was not loaded directly from a file?");
               return;
            }

            Desktop.getDesktop().open(var1.getLoadFile());
            VolmitSender var10000 = this.sender();
            String var10001 = String.valueOf(C.GREEN);
            var10000.sendMessage(var10001 + "Opening " + var1.getTypeName() + " " + var1.getLoadFile().getName().split("\\Q.\\E")[0] + " in VSCode! ");
         } catch (Throwable var3) {
            Iris.reportError(var3);
            this.sender().sendMessage(String.valueOf(C.RED) + "Cant find the file. Or registrant does not exist");
         }

      }
   }

   @Decree(
      description = "Edit the cave file you specified",
      aliases = {"c"},
      origin = DecreeOrigin.PLAYER
   )
   public void cave(@Param(contextual = false,description = "The cave to edit") IrisCave cave) {
      if (!this.noStudio()) {
         try {
            if (var1 == null || var1.getLoadFile() == null) {
               this.sender().sendMessage(String.valueOf(C.GOLD) + "Cannot find the file; Perhaps it was not loaded directly from a file?");
               return;
            }

            Desktop.getDesktop().open(var1.getLoadFile());
            VolmitSender var10000 = this.sender();
            String var10001 = String.valueOf(C.GREEN);
            var10000.sendMessage(var10001 + "Opening " + var1.getTypeName() + " " + var1.getLoadFile().getName().split("\\Q.\\E")[0] + " in VSCode! ");
         } catch (Throwable var3) {
            Iris.reportError(var3);
            this.sender().sendMessage(String.valueOf(C.RED) + "Cant find the file. Or registrant does not exist");
         }

      }
   }

   @Decree(
      description = "Edit the structure file you specified",
      aliases = {"jigsawstructure", "structure"},
      origin = DecreeOrigin.PLAYER
   )
   public void jigsaw(@Param(contextual = false,description = "The jigsaw structure to edit") IrisJigsawStructure jigsaw) {
      if (!this.noStudio()) {
         try {
            if (var1 == null || var1.getLoadFile() == null) {
               this.sender().sendMessage(String.valueOf(C.GOLD) + "Cannot find the file; Perhaps it was not loaded directly from a file?");
               return;
            }

            Desktop.getDesktop().open(var1.getLoadFile());
            VolmitSender var10000 = this.sender();
            String var10001 = String.valueOf(C.GREEN);
            var10000.sendMessage(var10001 + "Opening " + var1.getTypeName() + " " + var1.getLoadFile().getName().split("\\Q.\\E")[0] + " in VSCode! ");
         } catch (Throwable var3) {
            Iris.reportError(var3);
            this.sender().sendMessage(String.valueOf(C.RED) + "Cant find the file. Or registrant does not exist");
         }

      }
   }

   @Decree(
      description = "Edit the pool file you specified",
      aliases = {"jigsawpool", "pool"},
      origin = DecreeOrigin.PLAYER
   )
   public void jigsawPool(@Param(contextual = false,description = "The jigsaw pool to edit") IrisJigsawPool pool) {
      if (!this.noStudio()) {
         try {
            if (var1 == null || var1.getLoadFile() == null) {
               this.sender().sendMessage(String.valueOf(C.GOLD) + "Cannot find the file; Perhaps it was not loaded directly from a file?");
               return;
            }

            Desktop.getDesktop().open(var1.getLoadFile());
            VolmitSender var10000 = this.sender();
            String var10001 = String.valueOf(C.GREEN);
            var10000.sendMessage(var10001 + "Opening " + var1.getTypeName() + " " + var1.getLoadFile().getName().split("\\Q.\\E")[0] + " in VSCode! ");
         } catch (Throwable var3) {
            Iris.reportError(var3);
            this.sender().sendMessage(String.valueOf(C.RED) + "Cant find the file. Or registrant does not exist");
         }

      }
   }

   @Decree(
      description = "Edit the jigsaw piece file you specified",
      aliases = {"jigsawpiece", "piece"},
      origin = DecreeOrigin.PLAYER
   )
   public void jigsawPiece(@Param(contextual = false,description = "The jigsaw piece to edit") IrisJigsawPiece piece) {
      if (!this.noStudio()) {
         try {
            if (var1 == null || var1.getLoadFile() == null) {
               this.sender().sendMessage(String.valueOf(C.GOLD) + "Cannot find the file; Perhaps it was not loaded directly from a file?");
               return;
            }

            Desktop.getDesktop().open(var1.getLoadFile());
            VolmitSender var10000 = this.sender();
            String var10001 = String.valueOf(C.GREEN);
            var10000.sendMessage(var10001 + "Opening " + var1.getTypeName() + " " + var1.getLoadFile().getName().split("\\Q.\\E")[0] + " in VSCode! ");
         } catch (Throwable var3) {
            Iris.reportError(var3);
            this.sender().sendMessage(String.valueOf(C.RED) + "Cant find the file. Or registrant does not exist");
         }

      }
   }
}
