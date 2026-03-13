package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.hunk.storage.MappedHunk;
import com.volmit.iris.util.hunk.storage.PaletteOrHunk;
import com.volmit.iris.util.matter.MatterReader;
import com.volmit.iris.util.matter.MatterSlice;
import com.volmit.iris.util.matter.MatterWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import lombok.Generated;

public abstract class RawMatter<T> extends PaletteOrHunk<T> implements MatterSlice<T> {
   protected final KMap<Class<?>, MatterWriter<?, T>> writers = new KMap();
   protected final KMap<Class<?>, MatterReader<?, T>> readers = new KMap();
   private final Class<T> type;

   public RawMatter(int width, int height, int depth, Class<T> type) {
      super(var1, var2, var3, true, () -> {
         return new MappedHunk(var1, var2, var3);
      });
      this.type = var4;
   }

   protected <W> void registerWriter(Class<W> mediumType, MatterWriter<W, T> injector) {
      this.writers.put(var1, var2);
   }

   protected <W> void registerReader(Class<W> mediumType, MatterReader<W, T> injector) {
      this.readers.put(var1, var2);
   }

   public <W> MatterWriter<W, T> writeInto(Class<W> mediumType) {
      return (MatterWriter)this.writers.get(var1);
   }

   public <W> MatterReader<W, T> readFrom(Class<W> mediumType) {
      return (MatterReader)this.readers.get(var1);
   }

   public abstract void writeNode(T b, DataOutputStream dos);

   public abstract T readNode(DataInputStream din);

   @Generated
   public Class<T> getType() {
      return this.type;
   }
}
