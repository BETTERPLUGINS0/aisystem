package com.bergerkiller.bukkit.tc.pathfinding;

import com.bergerkiller.bukkit.common.utils.FaceUtil;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.bukkit.block.BlockFace;

public class PathConnection {
   public final double distance;
   public final String junctionName;
   public final PathNode destination;

   public PathConnection(PathNode destination, DataInputStream stream) throws IOException {
      this.destination = destination;
      int dist_in = stream.readInt();
      if (dist_in == Integer.MAX_VALUE) {
         this.distance = Math.max(1.0E-5D, stream.readDouble());
      } else {
         this.distance = (double)Math.max(1, dist_in);
      }

      byte n = stream.readByte();
      if (n == -1) {
         this.junctionName = stream.readUTF();
      } else {
         BlockFace f = FaceUtil.notchToFace(n << 1);
         switch(f) {
         case NORTH:
            this.junctionName = "n";
            break;
         case EAST:
            this.junctionName = "e";
            break;
         case SOUTH:
            this.junctionName = "s";
            break;
         case WEST:
            this.junctionName = "w";
            break;
         case UP:
            this.junctionName = "u";
            break;
         case DOWN:
            this.junctionName = "d";
            break;
         default:
            this.junctionName = "n";
         }
      }

   }

   public PathConnection(PathNode destination, double distance, String junctionName) {
      this.destination = destination;
      this.distance = Math.max(1.0E-4D, distance);
      this.junctionName = junctionName;
   }

   public String toString() {
      return "to " + this.destination.toString() + " going " + this.junctionName + " distance " + this.distance;
   }

   public void writeTo(DataOutputStream stream) throws IOException {
      stream.writeInt(this.destination.index);
      stream.writeInt(Integer.MAX_VALUE);
      stream.writeDouble(this.distance);
      stream.writeByte(255);
      stream.writeUTF(this.junctionName);
   }
}
