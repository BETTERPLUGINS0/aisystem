package com.volmit.iris.util.data;

import com.volmit.iris.util.collection.KList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Iterator;

public class DataPalette<T> {
   private final KList<T> palette;

   public DataPalette() {
      this(new KList(16));
   }

   public DataPalette(KList<T> palette) {
      this.palette = var1;
   }

   public static <T> DataPalette<T> getPalette(IOAdapter<T> adapter, DataInputStream din) {
      KList var2 = new KList();
      int var3 = var1.readShort() - -32768;

      for(int var4 = 0; var4 < var3; ++var4) {
         var2.add((Object)var0.read(var1));
      }

      return new DataPalette(var2);
   }

   public KList<T> getPalette() {
      return this.palette;
   }

   public T get(int index) {
      synchronized(this.palette) {
         return !this.palette.hasIndex(var1) ? null : this.palette.get(var1);
      }
   }

   public int getIndex(T t) {
      boolean var2 = false;
      synchronized(this.palette) {
         int var6 = this.palette.indexOf(var1);
         if (var6 == -1) {
            var6 = this.palette.size();
            this.palette.add((Object)var1);
         }

         return var6;
      }
   }

   public void write(IOAdapter<T> adapter, DataOutputStream dos) {
      synchronized(this.palette) {
         var2.writeShort(this.getPalette().size() + -32768);
         Iterator var4 = this.palette.iterator();

         while(var4.hasNext()) {
            Object var5 = var4.next();
            var1.write(var5, var2);
         }

      }
   }
}
