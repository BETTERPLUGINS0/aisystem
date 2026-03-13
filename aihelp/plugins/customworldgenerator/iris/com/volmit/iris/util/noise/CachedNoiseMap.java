package com.volmit.iris.util.noise;

import com.volmit.iris.util.hunk.bits.Writable;
import com.volmit.iris.util.matter.IrisMatter;
import com.volmit.iris.util.matter.Matter;
import com.volmit.iris.util.matter.MatterSlice;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;

public class CachedNoiseMap implements Writable<Integer> {
   private final Matter noise;
   private final MatterSlice<Integer> slice;

   public CachedNoiseMap(int size, NoiseGenerator cng) {
      this.noise = new IrisMatter(var1, var1, 1);
      this.slice = this.noise.slice(Integer.class);

      for(int var3 = 0; var3 < this.slice.getWidth(); ++var3) {
         for(int var4 = 0; var4 < this.slice.getHeight(); ++var4) {
            this.set(var3, var4, var2.noise((double)var3, (double)var4));
         }
      }

   }

   public CachedNoiseMap(File file) {
      this.noise = Matter.read(var1);
      this.slice = this.noise.slice(Integer.class);
   }

   void write(File file) {
      this.noise.write(var1);
   }

   void set(int x, int y, double value) {
      this.slice.set(var1 % this.slice.getWidth(), var2 % this.slice.getHeight(), 0, Float.floatToIntBits((float)var3));
   }

   double get(int x, int y) {
      Integer var3 = (Integer)this.slice.get(var1 % this.slice.getWidth(), var2 % this.slice.getHeight(), 0);
      return var3 == null ? 0.0D : (double)Float.intBitsToFloat(var3);
   }

   public Integer readNodeData(DataInputStream din) {
      return var1.readInt();
   }

   public void writeNodeData(DataOutputStream dos, Integer integer) {
      var1.writeInt(var2);
   }
}
