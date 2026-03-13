package org.terraform.structure.room.path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.PathPopulatorAbstract;
import org.terraform.structure.room.RoomLayoutGenerator;

public class PathState {
   @NotNull
   public final HashSet<PathState.PathNode> nodes = new HashSet();
   private final boolean ignoreWithinRooms = true;
   @NotNull
   private final RoomLayoutGenerator generator;
   @NotNull
   public PathWriter writer = new CavePathWriter(0.0F, 0.0F, 0.0F, 0, 0, 0);
   private final int pathRadius;
   private final int pathHeight;
   private final int maxBend;

   public PathState(@NotNull RoomLayoutGenerator generator, @NotNull TerraformWorld tw) {
      this.generator = generator;
      if (generator.getPathPop() != null) {
         this.pathRadius = generator.getPathPop().getPathWidth();
         this.pathHeight = generator.getPathPop().getPathHeight();
         this.maxBend = generator.getPathPop().getPathMaxBend();
      } else {
         this.pathRadius = 3;
         this.pathHeight = 3;
         this.maxBend = -1;
      }

      if (generator.genPaths()) {
         PathState.PathNode[] baseNodes = new PathState.PathNode[generator.getRooms().size()];
         ArrayList<CubeRoom> rooms = new ArrayList(generator.getRooms());

         int i;
         for(i = 0; i < generator.getRooms().size(); ++i) {
            CubeRoom room = (CubeRoom)rooms.get(i);
            SimpleLocation loc = new SimpleLocation(room.getX(), room.getY(), room.getZ());
            baseNodes[i] = new PathState.PathNode(loc, this.pathRadius, generator.getPathPop(), new BlockFace[0]);
         }

         assert baseNodes.length >= 2;

         for(i = 0; i < baseNodes.length - 1; ++i) {
            this.connectNodes(baseNodes[i], baseNodes[i + 1], tw, this.nodes);
         }

         this.nodes.addAll(Arrays.asList(baseNodes));
      }
   }

   private void connectNodes(@NotNull PathState.PathNode one, @NotNull PathState.PathNode two, @NotNull TerraformWorld tw, @NotNull HashSet<PathState.PathNode> toAdd) {
      BlockFace oneConn;
      if (one.center.getX() - two.center.getX() == 0) {
         oneConn = one.center.getZ() > two.center.getZ() ? BlockFace.NORTH : BlockFace.SOUTH;
         one.connected.add(oneConn);
         two.connected.add(oneConn.getOppositeFace());
      } else {
         if (one.center.getZ() - two.center.getZ() != 0) {
            PathState.PathNode newNode = new PathState.PathNode(tw.getHashedRand((long)one.center.getX(), two.center.getZ(), 1890341).nextBoolean() ? new SimpleLocation(one.center.getX(), one.center.getY(), two.center.getZ()) : new SimpleLocation(two.center.getX(), one.center.getY(), one.center.getZ()), this.pathRadius, this.generator.getPathPop(), new BlockFace[0]);
            toAdd.add(newNode);
            this.connectNodes(newNode, one, tw, toAdd);
            this.connectNodes(newNode, two, tw, toAdd);
            return;
         }

         oneConn = one.center.getX() > two.center.getX() ? BlockFace.WEST : BlockFace.EAST;
         one.connected.add(oneConn);
         two.connected.add(oneConn.getOppositeFace());
      }

      for(int i = 1; (float)i < one.center.distance(two.center); ++i) {
         toAdd.add(new PathState.PathNode(one.center.getRelative(oneConn, i), this.pathRadius, this.generator.getPathPop(), new BlockFace[]{oneConn}));
      }

   }

   public static class PathNode {
      public final int pathRadius;
      @NotNull
      public final SimpleLocation center;
      public final PathPopulatorAbstract populator;
      public final HashSet<BlockFace> connected = new HashSet();

      public PathNode(@NotNull SimpleLocation center, int pathWidth, PathPopulatorAbstract populator, BlockFace... connections) {
         this.pathRadius = pathWidth;
         int newX = center.getX() / pathWidth * pathWidth;
         int newZ = center.getZ() / pathWidth * pathWidth;
         this.center = new SimpleLocation(newX, center.y(), newZ);
         Collections.addAll(this.connected, connections);
         this.populator = populator;
      }

      public boolean equals(Object o) {
         if (o instanceof PathState.PathNode) {
            PathState.PathNode pn = (PathState.PathNode)o;
            return pn.center.equals(this.center);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.center.hashCode();
      }
   }
}
