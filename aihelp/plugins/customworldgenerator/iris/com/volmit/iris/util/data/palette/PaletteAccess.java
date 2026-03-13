package com.volmit.iris.util.data.palette;

import com.volmit.iris.util.nbt.tag.CompoundTag;

public interface PaletteAccess {
   void setBlock(int x, int y, int z, CompoundTag data);

   CompoundTag getBlock(int x, int y, int z);

   void writeToSection(CompoundTag tag);

   void readFromSection(CompoundTag tag);
}
