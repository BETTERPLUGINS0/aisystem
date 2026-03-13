package org.terraform.structure.pillager.mansion;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.room.CubeRoom;

public abstract class MansionRoomPopulator {
   private final HashMap<BlockFace, MansionInternalWallState> internalWalls;
   private final CubeRoom room;

   public MansionRoomPopulator(CubeRoom room, HashMap<BlockFace, MansionInternalWallState> internalWalls) {
      this.internalWalls = internalWalls;
      this.room = room;
   }

   public abstract void decorateRoom(PopulatorDataAbstract var1, Random var2);

   public CubeRoom getRoom() {
      return this.room;
   }

   @NotNull
   public MansionRoomPopulator getInstance(CubeRoom room, HashMap<BlockFace, MansionInternalWallState> internalWalls) {
      try {
         MansionRoomPopulator pop = (MansionRoomPopulator)this.getClass().getConstructors()[0].newInstance(room, internalWalls);
         return pop;
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | InstantiationException var5) {
         TerraformGeneratorPlugin.logger.stackTrace(var5);
         return null;
      }
   }

   public void decorateWall(Random rand, Wall w) {
   }

   public void decorateWindow(Random rand, Wall w) {
   }

   public void decorateEntrance(Random rand, Wall w) {
   }

   public void decorateExit(Random rand, Wall w) {
   }

   public HashMap<BlockFace, MansionInternalWallState> getInternalWalls() {
      return this.internalWalls;
   }

   public abstract MansionRoomSize getSize();

   public int[] getSpawnLocation() {
      return new int[]{this.room.getX(), this.room.getY() + 1, this.room.getZ()};
   }
}
