package org.apache.commons.io.input;

import java.io.DataInput;
import java.io.EOFException;
import java.io.InputStream;
import org.apache.commons.io.EndianUtils;

public class SwappedDataInputStream extends ProxyInputStream implements DataInput {
   public SwappedDataInputStream(InputStream var1) {
      super(var1);
   }

   public boolean readBoolean() {
      return 0 != this.readByte();
   }

   public byte readByte() {
      return (byte)this.in.read();
   }

   public char readChar() {
      return (char)this.readShort();
   }

   public double readDouble() {
      return EndianUtils.readSwappedDouble(this.in);
   }

   public float readFloat() {
      return EndianUtils.readSwappedFloat(this.in);
   }

   public void readFully(byte[] var1) {
      this.readFully(var1, 0, var1.length);
   }

   public void readFully(byte[] var1, int var2, int var3) {
      int var6;
      for(int var4 = var3; var4 > 0; var4 -= var6) {
         int var5 = var2 + var3 - var4;
         var6 = this.read(var1, var5, var4);
         if (-1 == var6) {
            throw new EOFException();
         }
      }

   }

   public int readInt() {
      return EndianUtils.readSwappedInteger(this.in);
   }

   public String readLine() {
      throw UnsupportedOperationExceptions.method("readLine");
   }

   public long readLong() {
      return EndianUtils.readSwappedLong(this.in);
   }

   public short readShort() {
      return EndianUtils.readSwappedShort(this.in);
   }

   public int readUnsignedByte() {
      return this.in.read();
   }

   public int readUnsignedShort() {
      return EndianUtils.readSwappedUnsignedShort(this.in);
   }

   public String readUTF() {
      throw UnsupportedOperationExceptions.method("readUTF");
   }

   public int skipBytes(int var1) {
      return (int)this.in.skip((long)var1);
   }
}
