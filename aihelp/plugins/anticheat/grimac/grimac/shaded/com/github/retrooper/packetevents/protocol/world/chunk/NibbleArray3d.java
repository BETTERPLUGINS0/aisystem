package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class NibbleArray3d {
   private final byte[] data;

   public NibbleArray3d(byte[] data) {
      this.data = data;
   }

   public NibbleArray3d(int size) {
      this(new byte[size >> 1]);
   }

   /** @deprecated */
   @Deprecated
   public NibbleArray3d(NetStreamInput in, int size) {
      this(in.readBytes(size));
   }

   public NibbleArray3d(PacketWrapper<?> wrapper, int size) {
      this(wrapper.readBytes(size));
   }

   public byte[] getData() {
      return this.data;
   }

   public int get(int x, int y, int z) {
      int key = y << 8 | z << 4 | x;
      int index = key >> 1;
      int part = key & 1;
      return part == 0 ? this.data[index] & 15 : this.data[index] >> 4 & 15;
   }

   public void set(int x, int y, int z, int val) {
      int key = y << 8 | z << 4 | x;
      int index = key >> 1;
      int part = key & 1;
      if (part == 0) {
         this.data[index] = (byte)(this.data[index] & 240 | val & 15);
      } else {
         this.data[index] = (byte)(this.data[index] & 15 | (val & 15) << 4);
      }

   }
}
