package com.volmit.iris.util.matter.slices;

import com.volmit.iris.util.data.palette.Palette;
import com.volmit.iris.util.nbt.io.NBTUtil;
import com.volmit.iris.util.nbt.tag.Tag;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class NBTMatter<T extends Tag<?>> extends RawMatter<T> {
   public NBTMatter(int width, int height, int depth, Class<T> c, T e) {
      super(var1, var2, var3, var4);
   }

   public Palette<T> getGlobalPalette() {
      return null;
   }

   public void writeNode(T b, DataOutputStream dos) {
      NBTUtil.write((Tag)var1, (OutputStream)var2, false);
   }

   public T readNode(DataInputStream din) {
      return NBTUtil.read((InputStream)var1, false).getTag();
   }
}
