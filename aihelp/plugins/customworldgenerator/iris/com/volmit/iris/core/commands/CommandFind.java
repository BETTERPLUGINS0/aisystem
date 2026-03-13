package com.volmit.iris.core.commands;

import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisJigsawStructure;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.decree.DecreeExecutor;
import com.volmit.iris.util.decree.DecreeOrigin;
import com.volmit.iris.util.decree.annotations.Decree;
import com.volmit.iris.util.decree.annotations.Param;
import com.volmit.iris.util.decree.specialhandlers.ObjectHandler;
import com.volmit.iris.util.format.C;

@Decree(
   name = "find",
   origin = DecreeOrigin.PLAYER,
   description = "Iris Find commands",
   aliases = {"goto"}
)
public class CommandFind implements DecreeExecutor {
   @Decree(
      description = "Find a biome"
   )
   public void biome(@Param(description = "The biome to look for") IrisBiome biome, @Param(description = "Should you be teleported",defaultValue = "true") boolean teleport) {
      Engine var3 = this.engine();
      if (var3 == null) {
         this.sender().sendMessage(String.valueOf(C.GOLD) + "Not in an Iris World!");
      } else {
         var3.gotoBiome(var1, this.player(), var2);
      }
   }

   @Decree(
      description = "Find a region"
   )
   public void region(@Param(description = "The region to look for") IrisRegion region, @Param(description = "Should you be teleported",defaultValue = "true") boolean teleport) {
      Engine var3 = this.engine();
      if (var3 == null) {
         this.sender().sendMessage(String.valueOf(C.GOLD) + "Not in an Iris World!");
      } else {
         var3.gotoRegion(var1, this.player(), var2);
      }
   }

   @Decree(
      description = "Find a structure"
   )
   public void structure(@Param(description = "The structure to look for") IrisJigsawStructure structure, @Param(description = "Should you be teleported",defaultValue = "true") boolean teleport) {
      Engine var3 = this.engine();
      if (var3 == null) {
         this.sender().sendMessage(String.valueOf(C.GOLD) + "Not in an Iris World!");
      } else {
         var3.gotoJigsaw(var1, this.player(), var2);
      }
   }

   @Decree(
      description = "Find a point of interest."
   )
   public void poi(@Param(description = "The type of PoI to look for.") String type, @Param(description = "Should you be teleported",defaultValue = "true") boolean teleport) {
      Engine var3 = this.engine();
      if (var3 == null) {
         this.sender().sendMessage(String.valueOf(C.GOLD) + "Not in an Iris World!");
      } else {
         var3.gotoPOI(var1, this.player(), var2);
      }
   }

   @Decree(
      description = "Find an object"
   )
   public void object(@Param(description = "The object to look for",customHandler = ObjectHandler.class) String object, @Param(description = "Should you be teleported",defaultValue = "true") boolean teleport) {
      Engine var3 = this.engine();
      if (var3 == null) {
         this.sender().sendMessage(String.valueOf(C.GOLD) + "Not in an Iris World!");
      } else {
         var3.gotoObject(var1, this.player(), var2);
      }
   }
}
