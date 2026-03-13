package com.volmit.iris.util.nbt.mca.palette;

import com.volmit.iris.util.nbt.tag.CompoundTag;
import java.util.function.Function;
import lombok.Generated;

public class MCAWrappedPalettedContainer<T> implements MCAPaletteAccess {
   private final MCAPalettedContainer<T> container;
   private final Function<T, CompoundTag> reader;
   private final Function<CompoundTag, T> writer;

   public void setBlock(int x, int y, int z, CompoundTag data) {
      this.container.set(var1, var2, var3, this.writer.apply(var4));
   }

   public CompoundTag getBlock(int x, int y, int z) {
      return (CompoundTag)this.reader.apply(this.container.get(var1, var2, var3));
   }

   public void writeToSection(CompoundTag tag) {
      this.container.write(var1, "Palette", "BlockStates");
   }

   public void readFromSection(CompoundTag tag) {
      this.container.read(var1.getListTag("Palette"), (long[])var1.getLongArrayTag("BlockStates").getValue());
   }

   @Generated
   public MCAWrappedPalettedContainer(final MCAPalettedContainer<T> container, final Function<T, CompoundTag> reader, final Function<CompoundTag, T> writer) {
      this.container = var1;
      this.reader = var2;
      this.writer = var3;
   }
}
