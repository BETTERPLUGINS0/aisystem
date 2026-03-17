package es.outlook.adriansrj.nbt.nbt.io;

import es.outlook.adriansrj.nbt.io.ExceptionTriConsumer;
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
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class LittleEndianNBTOutputStream implements DataOutput, NBTOutput, MaxDepthIO, Closeable {
   private final DataOutputStream output;
   private static Map<Byte, ExceptionTriConsumer<LittleEndianNBTOutputStream, Tag<?>, Integer, IOException>> writers = new HashMap();
   private static Map<Class<?>, Byte> classIdMapping = new HashMap();

   private static void put(byte var0, ExceptionTriConsumer<LittleEndianNBTOutputStream, Tag<?>, Integer, IOException> var1, Class<?> var2) {
      writers.put(var0, var1);
      classIdMapping.put(var2, var0);
   }

   public LittleEndianNBTOutputStream(OutputStream var1) {
      this.output = new DataOutputStream(var1);
   }

   public LittleEndianNBTOutputStream(DataOutputStream var1) {
      this.output = var1;
   }

   public void writeTag(NamedTag var1, int var2) {
      this.writeByte(var1.getTag().getID());
      if (var1.getTag().getID() != 0) {
         this.writeUTF(var1.getName() == null ? "" : var1.getName());
      }

      this.writeRawTag(var1.getTag(), var2);
   }

   public void writeTag(Tag<?> var1, int var2) {
      this.writeByte(var1.getID());
      if (var1.getID() != 0) {
         this.writeUTF("");
      }

      this.writeRawTag(var1, var2);
   }

   public void writeRawTag(Tag<?> var1, int var2) {
      ExceptionTriConsumer var3;
      if ((var3 = (ExceptionTriConsumer)writers.get(var1.getID())) == null) {
         throw new IOException("invalid tag \"" + var1.getID() + "\"");
      } else {
         var3.accept(this, var1, var2);
      }
   }

   static byte idFromClass(Class<?> var0) {
      Byte var1 = (Byte)classIdMapping.get(var0);
      if (var1 == null) {
         throw new IllegalArgumentException("unknown Tag class " + var0.getName());
      } else {
         return var1;
      }
   }

   private static void writeByte(LittleEndianNBTOutputStream var0, Tag<?> var1) {
      var0.writeByte(((ByteTag)var1).asByte());
   }

   private static void writeShort(LittleEndianNBTOutputStream var0, Tag<?> var1) {
      var0.writeShort(((ShortTag)var1).asShort());
   }

   private static void writeInt(LittleEndianNBTOutputStream var0, Tag<?> var1) {
      var0.writeInt(((IntTag)var1).asInt());
   }

   private static void writeLong(LittleEndianNBTOutputStream var0, Tag<?> var1) {
      var0.writeLong(((LongTag)var1).asLong());
   }

   private static void writeFloat(LittleEndianNBTOutputStream var0, Tag<?> var1) {
      var0.writeFloat(((FloatTag)var1).asFloat());
   }

   private static void writeDouble(LittleEndianNBTOutputStream var0, Tag<?> var1) {
      var0.writeDouble(((DoubleTag)var1).asDouble());
   }

   private static void writeString(LittleEndianNBTOutputStream var0, Tag<?> var1) {
      var0.writeUTF(((StringTag)var1).getValue());
   }

   private static void writeByteArray(LittleEndianNBTOutputStream var0, Tag<?> var1) {
      var0.writeInt(((ByteArrayTag)var1).length());
      var0.write((byte[])((ByteArrayTag)var1).getValue());
   }

   private static void writeIntArray(LittleEndianNBTOutputStream var0, Tag<?> var1) {
      var0.writeInt(((IntArrayTag)var1).length());
      int[] var2 = (int[])((IntArrayTag)var1).getValue();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var2[var4];
         var0.writeInt(var5);
      }

   }

   private static void writeLongArray(LittleEndianNBTOutputStream var0, Tag<?> var1) {
      var0.writeInt(((LongArrayTag)var1).length());
      long[] var2 = (long[])((LongArrayTag)var1).getValue();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         long var5 = var2[var4];
         var0.writeLong(var5);
      }

   }

   private static void writeList(LittleEndianNBTOutputStream var0, Tag<?> var1, int var2) {
      var0.writeByte(idFromClass(((ListTag)var1).getTypeClass()));
      var0.writeInt(((ListTag)var1).size());
      Iterator var3 = ((ListTag)var1).iterator();

      while(var3.hasNext()) {
         Tag var4 = (Tag)var3.next();
         var0.writeRawTag(var4, var0.decrementMaxDepth(var2));
      }

   }

   private static void writeCompound(LittleEndianNBTOutputStream var0, Tag<?> var1, int var2) {
      Iterator var3 = ((CompoundTag)var1).iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         if (((Tag)var4.getValue()).getID() == 0) {
            throw new IOException("end tag not allowed");
         }

         var0.writeByte(((Tag)var4.getValue()).getID());
         var0.writeUTF((String)var4.getKey());
         var0.writeRawTag((Tag)var4.getValue(), var0.decrementMaxDepth(var2));
      }

      var0.writeByte(0);
   }

   public void close() {
      this.output.close();
   }

   public void flush() {
      this.output.flush();
   }

   public void write(int var1) {
      this.output.write(var1);
   }

   public void write(byte[] var1) {
      this.output.write(var1);
   }

   public void write(byte[] var1, int var2, int var3) {
      this.output.write(var1, var2, var3);
   }

   public void writeBoolean(boolean var1) {
      this.output.writeBoolean(var1);
   }

   public void writeByte(int var1) {
      this.output.writeByte(var1);
   }

   public void writeShort(int var1) {
      this.output.writeShort(Short.reverseBytes((short)var1));
   }

   public void writeChar(int var1) {
      this.output.writeChar(Character.reverseBytes((char)var1));
   }

   public void writeInt(int var1) {
      this.output.writeInt(Integer.reverseBytes(var1));
   }

   public void writeLong(long var1) {
      this.output.writeLong(Long.reverseBytes(var1));
   }

   public void writeFloat(float var1) {
      this.output.writeInt(Integer.reverseBytes(Float.floatToIntBits(var1)));
   }

   public void writeDouble(double var1) {
      this.output.writeLong(Long.reverseBytes(Double.doubleToLongBits(var1)));
   }

   public void writeBytes(String var1) {
      this.output.writeBytes(var1);
   }

   public void writeChars(String var1) {
      this.output.writeChars(var1);
   }

   public void writeUTF(String var1) {
      byte[] var2 = var1.getBytes(StandardCharsets.UTF_8);
      this.writeShort(var2.length);
      this.write(var2);
   }

   static {
      put((byte)0, (var0, var1, var2) -> {
      }, EndTag.class);
      put((byte)1, (var0, var1, var2) -> {
         writeByte(var0, var1);
      }, ByteTag.class);
      put((byte)2, (var0, var1, var2) -> {
         writeShort(var0, var1);
      }, ShortTag.class);
      put((byte)3, (var0, var1, var2) -> {
         writeInt(var0, var1);
      }, IntTag.class);
      put((byte)4, (var0, var1, var2) -> {
         writeLong(var0, var1);
      }, LongTag.class);
      put((byte)5, (var0, var1, var2) -> {
         writeFloat(var0, var1);
      }, FloatTag.class);
      put((byte)6, (var0, var1, var2) -> {
         writeDouble(var0, var1);
      }, DoubleTag.class);
      put((byte)7, (var0, var1, var2) -> {
         writeByteArray(var0, var1);
      }, ByteArrayTag.class);
      put((byte)8, (var0, var1, var2) -> {
         writeString(var0, var1);
      }, StringTag.class);
      put((byte)9, LittleEndianNBTOutputStream::writeList, ListTag.class);
      put((byte)10, LittleEndianNBTOutputStream::writeCompound, CompoundTag.class);
      put((byte)11, (var0, var1, var2) -> {
         writeIntArray(var0, var1);
      }, IntArrayTag.class);
      put((byte)12, (var0, var1, var2) -> {
         writeLongArray(var0, var1);
      }, LongArrayTag.class);
   }
}
