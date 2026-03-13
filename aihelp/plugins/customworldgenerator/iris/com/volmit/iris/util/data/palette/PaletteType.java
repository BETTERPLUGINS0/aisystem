package com.volmit.iris.util.data.palette;

import com.volmit.iris.util.data.Varint;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface PaletteType<T> {
   void writePaletteNode(DataOutputStream dos, T t) throws IOException;

   T readPaletteNode(DataInputStream din) throws IOException;

   default void writeList(DataOutputStream dos, List<T> list) throws IOException {
      Varint.writeUnsignedVarInt(list.size(), dos);
      Iterator var3 = list.iterator();

      while(var3.hasNext()) {
         T i = var3.next();
         this.writePaletteNode(dos, i);
      }

   }

   default List<T> readList(DataInputStream din) throws IOException {
      int v = Varint.readUnsignedVarInt((DataInput)din);
      List<T> t = new ArrayList();

      for(int i = 0; i < v; ++i) {
         t.add(this.readPaletteNode(din));
      }

      return t;
   }
}
