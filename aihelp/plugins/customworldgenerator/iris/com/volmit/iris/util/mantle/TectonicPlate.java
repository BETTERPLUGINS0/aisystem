package com.volmit.iris.util.mantle;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.EnginePanic;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.util.data.Varint;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.io.CountingDataInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceArray;
import lombok.Generated;

public class TectonicPlate {
   private static final ThreadLocal<Boolean> errors = ThreadLocal.withInitial(() -> {
      return false;
   });
   public static final int MISSING = -1;
   public static final int CURRENT = 1;
   private final int sectionHeight;
   private final AtomicReferenceArray<MantleChunk> chunks;
   private final AtomicBoolean closed;
   private final int x;
   private final int z;

   public TectonicPlate(int worldHeight, int x, int z) {
      this.sectionHeight = var1 >> 4;
      this.chunks = new AtomicReferenceArray(1024);
      this.closed = new AtomicBoolean(false);
      this.x = var2;
      this.z = var3;
   }

   public TectonicPlate(int worldHeight, CountingDataInputStream din, boolean versioned) {
      this(var1, var2.readInt(), var2.readInt());
      if (!var2.markSupported()) {
         throw new IOException("Mark not supported!");
      } else {
         int var4 = var3 ? Varint.readUnsignedVarInt((DataInput)var2) : -1;

         for(int var5 = 0; var5 < this.chunks.length(); ++var5) {
            long var6 = (long)var2.readInt();
            if (var6 != 0L) {
               long var8 = var2.count();

               try {
                  Iris.addPanic("read-chunk", "Chunk[" + var5 + "]");
                  this.chunks.set(var5, new MantleChunk(var4, this.sectionHeight, var2));
                  EnginePanic.saveLast();
               } catch (Throwable var13) {
                  long var11 = var8 + var6;
                  Iris.error("Failed to read chunk, creating a new chunk instead.");
                  Iris.addPanic("read.byte.range", var8 + " " + var11);
                  Iris.addPanic("read.byte.current", var2.count().makeConcatWithConstants<invokedynamic>(var2.count()));
                  Iris.reportError(var13);
                  var13.printStackTrace();
                  Iris.panic();
                  var2.skipTo(var11);
                  addError();
               }
            }
         }

      }
   }

   public boolean inUse() {
      for(int var1 = 0; var1 < this.chunks.length(); ++var1) {
         MantleChunk var2 = (MantleChunk)this.chunks.get(var1);
         if (var2 != null && var2.inUse()) {
            return true;
         }
      }

      return false;
   }

   public void close() {
      this.closed.set(true);

      for(int var1 = 0; var1 < this.chunks.length(); ++var1) {
         MantleChunk var2 = (MantleChunk)this.chunks.get(var1);
         if (var2 != null) {
            var2.close();
         }
      }

   }

   public boolean isClosed() {
      return this.closed.get();
   }

   @ChunkCoordinates
   public boolean exists(int x, int z) {
      return this.get(var1, var2) != null;
   }

   @ChunkCoordinates
   public MantleChunk get(int x, int z) {
      return (MantleChunk)this.chunks.get(this.index(var1, var2));
   }

   public void clear() {
      for(int var1 = 0; var1 < this.chunks.length(); ++var1) {
         this.chunks.set(var1, (Object)null);
      }

   }

   @ChunkCoordinates
   public void delete(int x, int z) {
      this.chunks.set(this.index(var1, var2), (Object)null);
   }

   @ChunkCoordinates
   public MantleChunk getOrCreate(int x, int z) {
      int var3 = this.index(var1, var2);
      MantleChunk var4 = (MantleChunk)this.chunks.get(var3);
      if (var4 != null) {
         return var4;
      } else {
         MantleChunk var5 = new MantleChunk(this.sectionHeight, var1 & 31, var2 & 31);
         MantleChunk var6 = (MantleChunk)this.chunks.compareAndExchange(var3, (Object)null, var5);
         return var6 == null ? var5 : var6;
      }
   }

   @ChunkCoordinates
   private int index(int x, int z) {
      return Cache.to1D(var1, var2, 0, 32, 32);
   }

   public void write(DataOutputStream dos) {
      var1.writeInt(this.x);
      var1.writeInt(this.z);
      Varint.writeUnsignedVarInt(1, var1);
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(8192);
      DataOutputStream var3 = new DataOutputStream(var2);

      for(int var4 = 0; var4 < this.chunks.length(); ++var4) {
         MantleChunk var5 = (MantleChunk)this.chunks.get(var4);
         if (var5 != null) {
            try {
               var5.write(var3);
               var1.writeInt(var2.size());
               var2.writeTo(var1);
            } finally {
               var2.reset();
            }
         } else {
            var1.writeInt(0);
         }
      }

   }

   public static void addError() {
      errors.set(true);
   }

   public static boolean hasError() {
      boolean var0;
      try {
         var0 = (Boolean)errors.get();
      } finally {
         errors.remove();
      }

      return var0;
   }

   @Generated
   public int getX() {
      return this.x;
   }

   @Generated
   public int getZ() {
      return this.z;
   }
}
