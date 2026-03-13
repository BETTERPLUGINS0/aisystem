package com.volmit.iris.util.matter;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.io.CountingDataInputStream;
import com.volmit.iris.util.mantle.TectonicPlate;
import com.volmit.iris.util.math.BlockPosition;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.util.BlockVector;

public interface Matter {
   int VERSION = 1;

   static long convert(File folder) {
      if (!folder.isDirectory()) {
         IrisObject object = new IrisObject(1, 1, 1);

         try {
            long fs = folder.length();
            object.read(folder);
            from(object).write(folder);
            Iris.info("Converted " + folder.getPath() + " Saved " + (fs - folder.length()));
         } catch (Throwable var7) {
            Iris.error("Failed to convert " + folder.getPath());
            var7.printStackTrace();
         }

         return 0L;
      } else {
         long v = 0L;
         File[] var3 = folder.listFiles();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File i = var3[var5];
            v += convert(i);
         }

         return v;
      }
   }

   static Matter from(IrisObject object) {
      object.clean();
      object.shrinkwrap();
      BlockVector min = new BlockVector();
      Matter m = new IrisMatter(Math.max(object.getW(), 1) + 1, Math.max(object.getH(), 1) + 1, Math.max(object.getD(), 1) + 1);
      Iterator var3 = object.getBlocks().keys().iterator();

      BlockVector i;
      while(var3.hasNext()) {
         i = (BlockVector)var3.next();
         min.setX(Math.min(min.getX(), i.getX()));
         min.setY(Math.min(min.getY(), i.getY()));
         min.setZ(Math.min(min.getZ(), i.getZ()));
      }

      var3 = object.getBlocks().keys().iterator();

      while(var3.hasNext()) {
         i = (BlockVector)var3.next();
         m.slice(BlockData.class).set(i.getBlockX() - min.getBlockX(), i.getBlockY() - min.getBlockY(), i.getBlockZ() - min.getBlockZ(), object.getBlocks().get(i));
      }

      return m;
   }

   static Matter read(File f) throws IOException {
      FileInputStream in = new FileInputStream(f);

      Matter var2;
      try {
         var2 = read((InputStream)in);
      } catch (Throwable var5) {
         try {
            in.close();
         } catch (Throwable var4) {
            var5.addSuppressed(var4);
         }

         throw var5;
      }

      in.close();
      return var2;
   }

   static Matter read(InputStream in) throws IOException {
      return read(in, (b) -> {
         return new IrisMatter(b.getX(), b.getY(), b.getZ());
      });
   }

   static Matter readDin(CountingDataInputStream in) throws IOException {
      return readDin(in, (b) -> {
         return new IrisMatter(b.getX(), b.getY(), b.getZ());
      });
   }

   static Matter read(InputStream in, Function<BlockPosition, Matter> matterFactory) throws IOException {
      return readDin(CountingDataInputStream.wrap(in), matterFactory);
   }

   static Matter readDin(CountingDataInputStream din, Function<BlockPosition, Matter> matterFactory) throws IOException {
      Matter matter = (Matter)matterFactory.apply(new BlockPosition(din.readInt(), din.readInt(), din.readInt()));
      int var10001 = matter.getWidth();
      Iris.addPanic("read.matter.size", var10001 + "x" + matter.getHeight() + "x" + matter.getDepth());
      int sliceCount = din.readByte();
      Iris.addPanic("read.matter.slicecount", sliceCount.makeConcatWithConstants<invokedynamic>(sliceCount));
      matter.getHeader().read(din);
      Iris.addPanic("read.matter.header", matter.getHeader().toString());

      for(int i = 0; i < sliceCount; ++i) {
         long size = (long)din.readInt();
         if (size != 0L) {
            long start = din.count();
            long end = start + size;
            Iris.addPanic("read.matter.slice", i.makeConcatWithConstants<invokedynamic>(i));

            try {
               String cn = din.readUTF();
               Iris.addPanic("read.matter.slice.class", cn);
               Class<?> type = Class.forName(cn);
               MatterSlice<?> slice = matter.createSlice(type, matter);
               slice.read(din);
               if (din.count() < end) {
                  throw new IOException("Matter slice read size mismatch!");
               }

               matter.putSlice(type, slice);
            } catch (Throwable var14) {
               if (!(var14 instanceof ClassNotFoundException)) {
                  Iris.error("Failed to read matter slice, skipping it.");
                  Iris.addPanic("read.byte.range", start + " " + end);
                  Iris.addPanic("read.byte.current", din.count().makeConcatWithConstants<invokedynamic>(din.count()));
                  Iris.reportError(var14);
                  var14.printStackTrace();
                  Iris.panic();
                  TectonicPlate.addError();
               }

               din.skipTo(end);
            }

            if (din.count() != end) {
               throw new IOException("Matter slice read size mismatch!");
            }
         }
      }

      return matter;
   }

   default Matter copy() {
      Matter m = new IrisMatter(this.getWidth(), this.getHeight(), this.getDepth());
      this.getSliceMap().forEach((k, v) -> {
         m.slice(k).forceInject(v);
      });
      return m;
   }

   MatterHeader getHeader();

   int getWidth();

   int getHeight();

   int getDepth();

   default BlockPosition getCenter() {
      return new BlockPosition(this.getCenterX(), this.getCenterY(), this.getCenterZ());
   }

   <T> MatterSlice<T> createSlice(Class<T> type, Matter matter);

   default BlockPosition getSize() {
      return new BlockPosition(this.getWidth(), this.getHeight(), this.getDepth());
   }

   default int getCenterX() {
      return (int)Math.round((double)this.getWidth() / 2.0D);
   }

   default int getCenterY() {
      return (int)Math.round((double)this.getHeight() / 2.0D);
   }

   default int getCenterZ() {
      return (int)Math.round((double)this.getDepth() / 2.0D);
   }

   default <T> MatterSlice<T> getSlice(Class<T> t) {
      return (MatterSlice)this.getSliceMap().get(t);
   }

   default <T> MatterSlice<T> deleteSlice(Class<?> c) {
      return (MatterSlice)this.getSliceMap().remove(c);
   }

   default <T> MatterSlice<T> putSlice(Class<?> c, MatterSlice<T> slice) {
      return (MatterSlice)this.getSliceMap().put(c, slice);
   }

   default Class<?> getClass(Object w) {
      Class<?> c = w.getClass();
      if (w instanceof World) {
         c = World.class;
      } else if (w instanceof BlockData) {
         c = BlockData.class;
      } else if (w instanceof Entity) {
         c = Entity.class;
      }

      return c;
   }

   default <T> MatterSlice<T> slice(Class<?> c) {
      MatterSlice<T> slice = this.getSlice(c);
      if (slice == null) {
         slice = this.createSlice(c, this);
         if (slice == null) {
            try {
               throw new RuntimeException("Bad slice " + c.getCanonicalName());
            } catch (Throwable var4) {
               var4.printStackTrace();
               return null;
            }
         }

         this.putSlice(c, slice);
      }

      return slice;
   }

   default Matter rotate(double x, double y, double z) {
      IrisPosition rs = Hunk.rotatedBounding(this.getWidth(), this.getHeight(), this.getDepth(), x, y, z);
      Matter n = new IrisMatter(rs.getX(), rs.getY(), rs.getZ());
      n.getHeader().setAuthor(this.getHeader().getAuthor());
      n.getHeader().setCreatedAt(this.getHeader().getCreatedAt());
      Iterator var9 = this.getSliceTypes().iterator();

      while(var9.hasNext()) {
         Class<?> i = (Class)var9.next();
         this.getSlice(i).rotateSliceInto(n, x, y, z);
      }

      return n;
   }

   default boolean hasSlice(Class<?> c) {
      return this.getSlice(c) != null;
   }

   default void clearSlices() {
      this.getSliceMap().clear();
   }

   default Set<Class<?>> getSliceTypes() {
      return this.getSliceMap().keySet();
   }

   Map<Class<?>, MatterSlice<?>> getSliceMap();

   default void write(File f) throws IOException {
      OutputStream out = new FileOutputStream(f);
      this.write((OutputStream)out);
      out.close();
   }

   default void trimSlices() {
      Set<Class<?>> drop = null;
      Iterator var2 = this.getSliceTypes().iterator();

      Class i;
      while(var2.hasNext()) {
         i = (Class)var2.next();
         if (this.getSlice(i).getEntryCount() == 0) {
            if (drop == null) {
               drop = new KSet(new Class[0]);
            }

            drop.add(i);
         }
      }

      if (drop != null) {
         var2 = drop.iterator();

         while(var2.hasNext()) {
            i = (Class)var2.next();
            this.deleteSlice(i);
         }
      }

   }

   default void write(OutputStream out) throws IOException {
      this.writeDos(new DataOutputStream(out));
   }

   default void writeDos(DataOutputStream dos) throws IOException {
      this.trimSlices();
      dos.writeInt(this.getWidth());
      dos.writeInt(this.getHeight());
      dos.writeInt(this.getDepth());
      dos.writeByte(this.getSliceTypes().size());
      this.getHeader().write(dos);
      ByteArrayOutputStream bytes = new ByteArrayOutputStream(1024);
      DataOutputStream sub = new DataOutputStream(bytes);
      Iterator var4 = this.getSliceTypes().iterator();

      while(var4.hasNext()) {
         Class i = (Class)var4.next();

         try {
            this.getSlice(i).write(sub);
            dos.writeInt(bytes.size());
            bytes.writeTo(dos);
         } finally {
            bytes.reset();
         }
      }

   }

   default int getTotalCount() {
      int m = 0;

      MatterSlice i;
      for(Iterator var2 = this.getSliceMap().values().iterator(); var2.hasNext(); m += i.getEntryCount()) {
         i = (MatterSlice)var2.next();
      }

      return m;
   }
}
