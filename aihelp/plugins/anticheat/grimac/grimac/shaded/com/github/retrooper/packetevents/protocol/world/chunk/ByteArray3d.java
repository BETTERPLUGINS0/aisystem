package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk;

import java.util.Arrays;

public class ByteArray3d {
   private final byte[] data;

   public ByteArray3d(int size) {
      this.data = new byte[size];
   }

   public ByteArray3d(byte[] array) {
      this.data = array;
   }

   public byte[] getData() {
      return this.data;
   }

   public int get(int x, int y, int z) {
      return this.data[y << 8 | z << 4 | x] & 255;
   }

   public void set(int x, int y, int z, int val) {
      this.data[y << 8 | z << 4 | x] = (byte)val;
   }

   public void fill(int val) {
      Arrays.fill(this.data, (byte)val);
   }
}
