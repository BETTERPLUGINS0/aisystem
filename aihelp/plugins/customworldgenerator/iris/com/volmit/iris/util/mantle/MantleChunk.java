package com.volmit.iris.util.mantle;

import com.volmit.iris.Iris;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.documentation.ChunkRelativeBlockCoordinates;
import com.volmit.iris.util.function.Consumer4;
import com.volmit.iris.util.io.CountingDataInputStream;
import com.volmit.iris.util.matter.IrisMatter;
import com.volmit.iris.util.matter.Matter;
import com.volmit.iris.util.matter.MatterSlice;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceArray;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

public class MantleChunk extends FlaggedChunk {
   private final int x;
   private final int z;
   private final AtomicReferenceArray<Matter> sections;
   private final Semaphore ref;
   private final AtomicBoolean closed;

   @ChunkCoordinates
   public MantleChunk(int sectionHeight, int x, int z) {
      this.ref = new Semaphore(Integer.MAX_VALUE, true);
      this.closed = new AtomicBoolean(false);
      this.sections = new AtomicReferenceArray(var1);
      this.x = var2;
      this.z = var3;
   }

   public MantleChunk(int version, int sectionHeight, CountingDataInputStream din) {
      this(var2, var3.readByte(), var3.readByte());
      byte var4 = var3.readByte();
      this.readFlags(var1, var3);

      for(int var5 = 0; var5 < var4; ++var5) {
         Iris.addPanic("read.section", "Section[" + var5 + "]");
         long var6 = (long)var3.readInt();
         if (var6 != 0L) {
            long var8 = var3.count();
            if (var5 >= var2) {
               var3.skipTo(var8 + var6);
            } else {
               try {
                  this.sections.set(var5, Matter.readDin(var3));
               } catch (IOException var13) {
                  long var11 = var8 + var6;
                  Iris.error("Failed to read chunk section, skipping it.");
                  Iris.addPanic("read.byte.range", var8 + " " + var11);
                  Iris.addPanic("read.byte.current", var3.count().makeConcatWithConstants<invokedynamic>(var3.count()));
                  Iris.reportError(var13);
                  var13.printStackTrace();
                  Iris.panic();
                  var3.skipTo(var11);
                  TectonicPlate.addError();
               }

               if (var3.count() != var8 + var6) {
                  throw new IOException("Chunk section read size mismatch!");
               }
            }
         }
      }

   }

   public void close() {
      try {
         this.closed.set(true);
         this.ref.acquire(Integer.MAX_VALUE);
         this.ref.release(Integer.MAX_VALUE);
      } catch (Throwable var2) {
         throw var2;
      }
   }

   public boolean inUse() {
      return this.ref.availablePermits() < Integer.MAX_VALUE;
   }

   public MantleChunk use() {
      if (this.closed.get()) {
         throw new IllegalStateException("Chunk is closed!");
      } else {
         this.ref.acquireUninterruptibly();
         if (this.closed.get()) {
            this.ref.release();
            throw new IllegalStateException("Chunk is closed!");
         } else {
            return this;
         }
      }
   }

   public void release() {
      this.ref.release();
   }

   public void copyFrom(MantleChunk chunk) {
      this.use();
      super.copyFrom(var1, () -> {
         for(int var2 = 0; var2 < this.sections.length(); ++var2) {
            this.sections.set(var2, var1.get(var2));
         }

      });
      this.release();
   }

   @ChunkCoordinates
   public boolean exists(int section) {
      return this.get(var1) != null;
   }

   @ChunkCoordinates
   public Matter get(int section) {
      return (Matter)this.sections.get(var1);
   }

   @Nullable
   @ChunkRelativeBlockCoordinates
   public <T> T get(int x, int y, int z, Class<T> type) {
      return this.getOrCreate(var2 >> 4).slice(var4).get(var1 & 15, var2 & 15, var3 & 15);
   }

   public void clear() {
      for(int var1 = 0; var1 < this.sections.length(); ++var1) {
         this.delete(var1);
      }

   }

   @ChunkCoordinates
   public void delete(int section) {
      this.sections.set(var1, (Object)null);
   }

   @ChunkCoordinates
   public Matter getOrCreate(int section) {
      Matter var2 = this.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         IrisMatter var3 = new IrisMatter(16, 16, 16);
         Matter var4 = (Matter)this.sections.compareAndExchange(var1, (Object)null, var3);
         return (Matter)(var4 == null ? var3 : var4);
      }
   }

   public void write(DataOutputStream dos) {
      this.close();
      var1.writeByte(this.x);
      var1.writeByte(this.z);
      var1.writeByte(this.sections.length());
      this.writeFlags(var1);
      ByteArrayOutputStream var2 = new ByteArrayOutputStream(8192);
      DataOutputStream var3 = new DataOutputStream(var2);

      for(int var4 = 0; var4 < this.sections.length(); ++var4) {
         this.trimSlice(var4);
         if (this.exists(var4)) {
            try {
               Matter var5 = this.get(var4);
               var5.writeDos(var3);
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

   private void trimSlice(int i) {
      if (this.exists(var1)) {
         Matter var2 = this.get(var1);
         if (var2.getSliceMap().isEmpty()) {
            this.sections.set(var1, (Object)null);
         } else {
            var2.trimSlices();
            if (var2.getSliceMap().isEmpty()) {
               this.sections.set(var1, (Object)null);
            }
         }
      }

   }

   public <T> void iterate(Class<T> type, Consumer4<Integer, Integer, Integer, T> iterator) {
      for(int var3 = 0; var3 < this.sections.length(); ++var3) {
         int var4 = var3 << 4;
         Matter var5 = this.get(var3);
         if (var5 != null) {
            MatterSlice var6 = var5.getSlice(var1);
            if (var6 != null) {
               var6.iterateSync((var2x, var3x, var4x, var5x) -> {
                  var2.accept(var2x, var3x + var4, var4x, var5x);
               });
            }
         }
      }

   }

   public void deleteSlices(Class<?> c) {
      if (!IrisToolbelt.isRetainingMantleDataForSlice(var1.getCanonicalName())) {
         for(int var2 = 0; var2 < this.sections.length(); ++var2) {
            Matter var3 = (Matter)this.sections.get(var2);
            if (var3 != null && var3.hasSlice(var1)) {
               var3.deleteSlice(var1);
            }
         }

      }
   }

   public void trimSlices() {
      for(int var1 = 0; var1 < this.sections.length(); ++var1) {
         if (this.exists(var1)) {
            this.trimSlice(var1);
         }
      }

   }

   public boolean isClosed() {
      return this.closed.get();
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
