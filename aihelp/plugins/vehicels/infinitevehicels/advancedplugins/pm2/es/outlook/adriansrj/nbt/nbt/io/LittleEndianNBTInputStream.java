package es.outlook.adriansrj.nbt.nbt.io;

import es.outlook.adriansrj.nbt.io.ExceptionBiFunction;
import es.outlook.adriansrj.nbt.io.MaxDepthIO;
import es.outlook.adriansrj.nbt.nbt.tag.ByteArrayTag;
import es.outlook.adriansrj.nbt.nbt.tag.ByteTag;
import es.outlook.adriansrj.nbt.nbt.tag.CompoundTag;
import es.outlook.adriansrj.nbt.nbt.tag.DoubleTag;
import es.outlook.adriansrj.nbt.nbt.tag.EndTag;
import es.outlook.adriansrj.nbt.nbt.tag.FloatTag;
import es.outlook.adriansrj.nbt.nbt.tag.IntArrayTag;
import es.outlook.adriansrj.nbt.nbt.tag.IntTag;
import es.outlook.adriansrj.nbt.nbt.tag.ListTag;
import es.outlook.adriansrj.nbt.nbt.tag.LongArrayTag;
import es.outlook.adriansrj.nbt.nbt.tag.LongTag;
import es.outlook.adriansrj.nbt.nbt.tag.ShortTag;
import es.outlook.adriansrj.nbt.nbt.tag.StringTag;
import es.outlook.adriansrj.nbt.nbt.tag.Tag;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LittleEndianNBTInputStream implements DataInput, NBTInput, MaxDepthIO, Closeable {
   private final DataInputStream input;
   private static Map<Byte, ExceptionBiFunction<LittleEndianNBTInputStream, Integer, ? extends Tag<?>, IOException>> readers = new HashMap();
   private static Map<Byte, Class<?>> idClassMapping = new HashMap();

   private static void put(byte var0, ExceptionBiFunction<LittleEndianNBTInputStream, Integer, ? extends Tag<?>, IOException> var1, Class<?> var2) {
      readers.put(var0, var1);
      idClassMapping.put(var0, var2);
   }

   public LittleEndianNBTInputStream(InputStream var1) {
      this.input = new DataInputStream(var1);
   }

   public LittleEndianNBTInputStream(DataInputStream var1) {
      this.input = var1;
   }

   public NamedTag readTag(int var1) {
      byte var2 = this.readByte();
      return new NamedTag(this.readUTF(), this.readTag(var2, var1));
   }

   public Tag<?> readRawTag(int var1) {
      byte var2 = this.readByte();
      return this.readTag(var2, var1);
   }

   private Tag<?> readTag(byte var1, int var2) {
      ExceptionBiFunction var3;
      if ((var3 = (ExceptionBiFunction)readers.get(var1)) == null) {
         throw new IOException("invalid tag id \"" + var1 + "\"");
      } else {
         return (Tag)var3.accept(this, var2);
      }
   }

   private static ByteTag readByte(LittleEndianNBTInputStream var0) {
      return new ByteTag(var0.readByte());
   }

   private static ShortTag readShort(LittleEndianNBTInputStream var0) {
      return new ShortTag(var0.readShort());
   }

   private static IntTag readInt(LittleEndianNBTInputStream var0) {
      return new IntTag(var0.readInt());
   }

   private static LongTag readLong(LittleEndianNBTInputStream var0) {
      return new LongTag(var0.readLong());
   }

   private static FloatTag readFloat(LittleEndianNBTInputStream var0) {
      return new FloatTag(var0.readFloat());
   }

   private static DoubleTag readDouble(LittleEndianNBTInputStream var0) {
      return new DoubleTag(var0.readDouble());
   }

   private static StringTag readString(LittleEndianNBTInputStream var0) {
      return new StringTag(var0.readUTF());
   }

   private static ByteArrayTag readByteArray(LittleEndianNBTInputStream var0) {
      ByteArrayTag var1 = new ByteArrayTag(new byte[var0.readInt()]);
      var0.readFully((byte[])var1.getValue());
      return var1;
   }

   private static IntArrayTag readIntArray(LittleEndianNBTInputStream var0) {
      int var1 = var0.readInt();
      int[] var2 = new int[var1];
      IntArrayTag var3 = new IntArrayTag(var2);

      for(int var4 = 0; var4 < var1; ++var4) {
         var2[var4] = var0.readInt();
      }

      return var3;
   }

   private static LongArrayTag readLongArray(LittleEndianNBTInputStream var0) {
      int var1 = var0.readInt();
      long[] var2 = new long[var1];
      LongArrayTag var3 = new LongArrayTag(var2);

      for(int var4 = 0; var4 < var1; ++var4) {
         var2[var4] = var0.readLong();
      }

      return var3;
   }

   private static ListTag<?> readListTag(LittleEndianNBTInputStream var0, int var1) {
      byte var2 = var0.readByte();
      ListTag var3 = ListTag.createUnchecked((Class)idClassMapping.get(var2));
      int var4 = var0.readInt();
      if (var4 < 0) {
         var4 = 0;
      }

      for(int var5 = 0; var5 < var4; ++var5) {
         var3.addUnchecked(var0.readTag(var2, var0.decrementMaxDepth(var1)));
      }

      return var3;
   }

   private static CompoundTag readCompound(LittleEndianNBTInputStream var0, int var1) {
      CompoundTag var2 = new CompoundTag();

      for(int var3 = var0.readByte() & 255; var3 != 0; var3 = var0.readByte() & 255) {
         String var4 = var0.readUTF();
         Tag var5 = var0.readTag((byte)var3, var0.decrementMaxDepth(var1));
         var2.put(var4, var5);
      }

      return var2;
   }

   public void readFully(byte[] var1) {
      this.input.readFully(var1);
   }

   public void readFully(byte[] var1, int var2, int var3) {
      this.input.readFully(var1, var2, var3);
   }

   public int skipBytes(int var1) {
      return this.input.skipBytes(var1);
   }

   public boolean readBoolean() {
      return this.input.readBoolean();
   }

   public byte readByte() {
      return this.input.readByte();
   }

   public int readUnsignedByte() {
      return this.input.readUnsignedByte();
   }

   public short readShort() {
      return Short.reverseBytes(this.input.readShort());
   }

   public int readUnsignedShort() {
      return Short.toUnsignedInt(Short.reverseBytes(this.input.readShort()));
   }

   public char readChar() {
      return Character.reverseBytes(this.input.readChar());
   }

   public int readInt() {
      return Integer.reverseBytes(this.input.readInt());
   }

   public long readLong() {
      return Long.reverseBytes(this.input.readLong());
   }

   public float readFloat() {
      return Float.intBitsToFloat(Integer.reverseBytes(this.input.readInt()));
   }

   public double readDouble() {
      return Double.longBitsToDouble(Long.reverseBytes(this.input.readLong()));
   }

   /** @deprecated */
   @Deprecated
   public String readLine() {
      return this.input.readLine();
   }

   public void close() {
      this.input.close();
   }

   public String readUTF() {
      byte[] var1 = new byte[this.readUnsignedShort()];
      this.readFully(var1);
      return new String(var1, StandardCharsets.UTF_8);
   }

   static {
      put((byte)0, (var0, var1) -> {
         return EndTag.INSTANCE;
      }, EndTag.class);
      put((byte)1, (var0, var1) -> {
         return readByte(var0);
      }, ByteTag.class);
      put((byte)2, (var0, var1) -> {
         return readShort(var0);
      }, ShortTag.class);
      put((byte)3, (var0, var1) -> {
         return readInt(var0);
      }, IntTag.class);
      put((byte)4, (var0, var1) -> {
         return readLong(var0);
      }, LongTag.class);
      put((byte)5, (var0, var1) -> {
         return readFloat(var0);
      }, FloatTag.class);
      put((byte)6, (var0, var1) -> {
         return readDouble(var0);
      }, DoubleTag.class);
      put((byte)7, (var0, var1) -> {
         return readByteArray(var0);
      }, ByteArrayTag.class);
      put((byte)8, (var0, var1) -> {
         return readString(var0);
      }, StringTag.class);
      put((byte)9, LittleEndianNBTInputStream::readListTag, ListTag.class);
      put((byte)10, LittleEndianNBTInputStream::readCompound, CompoundTag.class);
      put((byte)11, (var0, var1) -> {
         return readIntArray(var0);
      }, IntArrayTag.class);
      put((byte)12, (var0, var1) -> {
         return readLongArray(var0);
      }, LongArrayTag.class);
   }
}
