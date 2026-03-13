package com.volmit.iris.core.commands;

import com.volmit.iris.Iris;
import com.volmit.iris.core.edit.JigsawEditor;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.framework.placer.WorldObjectPlacer;
import com.volmit.iris.engine.jigsaw.PlannedStructure;
import com.volmit.iris.engine.object.IrisJigsawPiece;
import com.volmit.iris.engine.object.IrisJigsawStructure;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.util.decree.DecreeExecutor;
import com.volmit.iris.util.decree.DecreeOrigin;
import com.volmit.iris.util.decree.annotations.Decree;
import com.volmit.iris.util.decree.annotations.Param;
import com.volmit.iris.util.decree.specialhandlers.ObjectHandler;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.io.File;
import org.bukkit.World;

@Decree(
   name = "jigsaw",
   origin = DecreeOrigin.PLAYER,
   studio = true,
   description = "Iris jigsaw commands"
)
public class CommandJigsaw implements DecreeExecutor {
   @Decree(
      description = "Edit a jigsaw piece"
   )
   public void edit(@Param(description = "The jigsaw piece to edit") IrisJigsawPiece piece) {
      File var2 = var1.getLoadFile();
      new JigsawEditor(this.player(), var1, IrisData.loadAnyObject(var1.getObject(), this.data()), var2);
   }

   @Decree(
      description = "Place a jigsaw structure"
   )
   public void place(@Param(description = "The jigsaw structure to place") IrisJigsawStructure structure) {
      PrecisionStopwatch var2 = PrecisionStopwatch.start();

      String var10001;
      try {
         World var3 = this.world();
         WorldObjectPlacer var4 = new WorldObjectPlacer(var3);
         PlannedStructure var5 = new PlannedStructure(var1, new IrisPosition(this.player().getLocation().add(0.0D, (double)var3.getMinHeight(), 0.0D)), new RNG(), true);
         VolmitSender var6 = this.sender();
         var10001 = String.valueOf(C.GREEN);
         var6.sendMessage(var10001 + "Generated " + var5.getPieces().size() + " pieces in " + Form.duration(var2.getMilliseconds(), 2));
         var5.place(var4, (var1x) -> {
            var6.sendMessage(var1x ? String.valueOf(C.GREEN) + "Placed the structure!" : String.valueOf(C.RED) + "Failed to place the structure!");
         });
      } catch (IllegalArgumentException var7) {
         VolmitSender var10000 = this.sender();
         var10001 = String.valueOf(C.RED);
         var10000.sendMessage(var10001 + "Failed to place the structure: " + var7.getMessage());
      }

   }

   @Decree(
      description = "Create a jigsaw piece"
   )
   public void create(@Param(description = "The name of the jigsaw piece") String piece, @Param(description = "The project to add the jigsaw piece to") String project, @Param(description = "The object to use for this piece",customHandler = ObjectHandler.class) String object) {
      IrisObject var4 = IrisData.loadAnyObject(var3, this.data());
      if (var3 == null) {
         this.sender().sendMessage(String.valueOf(C.RED) + "Failed to find existing object");
      } else {
         File var5 = Iris.instance.getDataFile(new String[]{"packs", var2, "jigsaw-pieces", var1 + ".json"});
         new JigsawEditor(this.player(), (IrisJigsawPiece)null, var4, var5);
         this.sender().sendMessage(String.valueOf(C.GRAY) + "* Right Click blocks to make them connectors");
         this.sender().sendMessage(String.valueOf(C.GRAY) + "* Right Click connectors to orient them");
         this.sender().sendMessage(String.valueOf(C.GRAY) + "* Shift + Right Click connectors to remove them");
         this.sender().sendMessage(String.valueOf(C.GREEN) + "Remember to use /iris jigsaw save");
      }
   }

   @Decree(
      description = "Exit the current jigsaw editor"
   )
   public void exit() {
      JigsawEditor var1 = (JigsawEditor)JigsawEditor.editors.get(this.player());
      if (var1 == null) {
         this.sender().sendMessage(String.valueOf(C.GOLD) + "You don't have any pieces open to exit!");
      } else {
         var1.exit();
         this.sender().sendMessage(String.valueOf(C.GREEN) + "Exited Jigsaw Editor");
      }
   }

   @Decree(
      description = "Save & Exit the current jigsaw editor"
   )
   public void save() {
      JigsawEditor var1 = (JigsawEditor)JigsawEditor.editors.get(this.player());
      if (var1 == null) {
         this.sender().sendMessage(String.valueOf(C.GOLD) + "You don't have any pieces open to save!");
      } else {
         var1.close();
         this.sender().sendMessage(String.valueOf(C.GREEN) + "Saved & Exited Jigsaw Editor");
      }
   }
}
