package com.volmit.iris.util.matter;

import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.util.data.Varint;
import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.data.palette.PaletteType;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.hunk.bits.DataContainer;
import com.volmit.iris.util.hunk.bits.Writable;
import com.volmit.iris.util.hunk.storage.PaletteOrHunk;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.util.BlockVector;

public interface MatterSlice<T> extends Hunk<T>, PaletteType<T>, Writable<T> {
   Class<T> getType();

   Palette<T> getGlobalPalette();

   default void writePaletteNode(DataOutputStream dos, T s) throws IOException {
      this.writeNode(s, dos);
   }

   default void writeNodeData(DataOutputStream dos, T s) throws IOException {
      this.writeNode(s, dos);
   }

   default T readPaletteNode(DataInputStream din) throws IOException {
      return this.readNode(din);
   }

   default T readNodeData(DataInputStream din) throws IOException {
      return this.readNode(din);
   }

   default void applyFilter(MatterFilter<T> filter) {
      Objects.requireNonNull(filter);
      this.updateSync(filter::update);
   }

   default void inject(MatterSlice<T> slice) {
      Objects.requireNonNull(slice);
      this.iterateSync(slice::set);
   }

   default void forceInject(MatterSlice<?> slice) {
      this.inject(slice);
   }

   void writeNode(T b, DataOutputStream dos) throws IOException;

   T readNode(DataInputStream din) throws IOException;

   <W> MatterWriter<W, T> writeInto(Class<W> mediumType);

   <W> MatterReader<W, T> readFrom(Class<W> mediumType);

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

   default boolean writeInto(Location location) {
      return this.writeInto(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
   }

   default <W> boolean writeInto(W w, int x, int y, int z) {
      MatterWriter<W, T> injector = this.writeInto(this.getClass(w));
      if (injector == null) {
         return false;
      } else {
         this.iterateSync((a, b, c, t) -> {
            injector.writeMatter(w, t, a + x, b + y, c + z);
         });
         return true;
      }
   }

   default boolean readFrom(Location location) {
      return this.readFrom(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
   }

   default <W> boolean readFrom(W w, int x, int y, int z) {
      MatterReader<W, T> ejector = this.readFrom(this.getClass(w));
      if (ejector == null) {
         return false;
      } else {
         for(int i = x; i < x + this.getWidth(); ++i) {
            for(int j = y; j < y + this.getHeight(); ++j) {
               for(int k = z; k < z + this.getDepth(); ++k) {
                  T v = ejector.readMatter(w, i, j, k);
                  if (v != null) {
                     this.set(i - x, j - y, k - z, v);
                  }
               }
            }
         }

         return true;
      }
   }

   default boolean canWrite(Class<?> mediumType) {
      return this.writeInto(mediumType) != null;
   }

   default boolean canRead(Class<?> mediumType) {
      return this.readFrom(mediumType) != null;
   }

   default int getBitsPer(int needed) {
      int target = 1;

      for(int i = 1; i < 8; ++i) {
         if (Math.pow(2.0D, (double)i) > (double)needed) {
            target = i;
            break;
         }
      }

      return target;
   }

   default void write(DataOutputStream dos) throws IOException {
      dos.writeUTF(this.getType().getCanonicalName());
      if (this instanceof PaletteOrHunk) {
         PaletteOrHunk f = (PaletteOrHunk)this;
         if (f.isPalette()) {
            f.palette().writeDos(dos);
            return;
         }
      }

      int w = this.getWidth();
      int h = this.getHeight();
      MatterPalette<T> palette = new MatterPalette(this);
      this.iterateSync((x, y, z, b) -> {
         palette.assign(b);
      });
      palette.writePalette(dos);
      dos.writeBoolean(this.isMapped());
      if (this.isMapped()) {
         Varint.writeUnsignedVarInt(this.getEntryCount(), dos);
         this.iterateSyncIO((x, y, z, b) -> {
            Varint.writeUnsignedVarInt(Cache.to1D(x, y, z, w, h), dos);
            palette.writeNode(b, dos);
         });
      } else {
         this.iterateSyncIO((x, y, z, b) -> {
            palette.writeNode(b, dos);
         });
      }

   }

   default void read(DataInputStream din) throws IOException {
      if (this instanceof PaletteOrHunk) {
         PaletteOrHunk f = (PaletteOrHunk)this;
         if (f.isPalette()) {
            f.setPalette(new DataContainer(din, this));
            return;
         }
      }

      int w = this.getWidth();
      int h = this.getHeight();
      MatterPalette<T> palette = new MatterPalette(this, din);
      if (din.readBoolean()) {
         int var5 = Varint.readUnsignedVarInt((DataInput)din);

         while(var5-- > 0) {
            int[] pos = Cache.to3D(Varint.readUnsignedVarInt((DataInput)din), w, h);
            this.setRaw(pos[0], pos[1], pos[2], palette.readNode(din));
         }
      } else {
         this.iterateSyncIO((x, y, z, b) -> {
            this.setRaw(x, y, z, palette.readNode(din));
         });
      }

   }

   default void rotateSliceInto(Matter n, double x, double y, double z) {
      this.rotate(x, y, z, (_x, _y, _z) -> {
         return n.slice(this.getType());
      });
   }

   default boolean containsKey(BlockVector v) {
      return this.get(v.getBlockX(), v.getBlockY(), v.getBlockZ()) != null;
   }

   default void put(BlockVector v, T d) {
      this.set(v.getBlockX(), v.getBlockY(), v.getBlockZ(), d);
   }

   default T get(BlockVector v) {
      return this.get(v.getBlockX(), v.getBlockY(), v.getBlockZ());
   }
}
