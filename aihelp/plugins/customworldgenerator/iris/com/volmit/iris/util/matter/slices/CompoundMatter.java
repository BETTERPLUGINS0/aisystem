package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.matter.Sliced;
import com.volmit.iris.util.nbt.tag.CompoundTag;

@Sliced
public class CompoundMatter extends NBTMatter<CompoundTag> {
   public static final CompoundTag EMPTY = new CompoundTag();

   public CompoundMatter() {
      this(1, 1, 1);
   }

   public CompoundMatter(int width, int height, int depth) {
      super(var1, var2, var3, CompoundTag.class, EMPTY);
   }
}
