package com.volmit.iris.util.matter;

import com.volmit.iris.util.data.DataPalette;
import com.volmit.iris.util.data.IOAdapter;
import com.volmit.iris.util.data.Varint;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MatterPalette<T> implements IOAdapter<T> {
   private final MatterSlice<T> slice;
   private final DataPalette<T> palette;

   public MatterPalette(MatterSlice<T> slice) {
      this.slice = var1;
      this.palette = new DataPalette();
   }

   public MatterPalette(MatterSlice<T> slice, DataInputStream din) {
      this.slice = var1;
      this.palette = DataPalette.getPalette(this, var2);
   }

   public void writeNode(T t, DataOutputStream dos) {
      Varint.writeUnsignedVarInt(this.palette.getIndex(var1), var2);
   }

   public T readNode(DataInputStream din) {
      return this.palette.get(Varint.readUnsignedVarInt((DataInput)var1));
   }

   public void writePalette(DataOutputStream dos) {
      this.palette.write(this, var1);
   }

   public void write(T t, DataOutputStream dos) {
      this.slice.writeNode(var1, var2);
   }

   public T read(DataInputStream din) {
      return this.slice.readNode(var1);
   }

   public void assign(T b) {
      this.palette.getIndex(var1);
   }
}
