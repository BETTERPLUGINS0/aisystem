package com.volmit.iris.util.matter.slices;

import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.core.loader.ResourceLoader;
import com.volmit.iris.util.context.IrisContext;
import com.volmit.iris.util.data.palette.Palette;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class RegistryMatter<T extends IrisRegistrant> extends RawMatter<T> {
   public RegistryMatter(int width, int height, int depth, Class<T> c, T e) {
      super(var1, var2, var3, var4);
   }

   public Palette<T> getGlobalPalette() {
      return null;
   }

   public void writeNode(T b, DataOutputStream dos) {
      var2.writeUTF(var1.getLoadKey());
   }

   public T readNode(DataInputStream din) {
      IrisContext var2 = IrisContext.get();
      return ((ResourceLoader)var2.getData().getLoaders().get(this.getType())).load(var1.readUTF());
   }
}
