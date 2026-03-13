package com.volmit.iris.util.nbt.io;

import com.volmit.iris.engine.data.io.ExceptionTriConsumer;
import com.volmit.iris.engine.data.io.MaxDepthIO;
import com.volmit.iris.util.nbt.tag.ByteArrayTag;
import com.volmit.iris.util.nbt.tag.ByteTag;
import com.volmit.iris.util.nbt.tag.CompoundTag;
import com.volmit.iris.util.nbt.tag.DoubleTag;
import com.volmit.iris.util.nbt.tag.EndTag;
import com.volmit.iris.util.nbt.tag.FloatTag;
import com.volmit.iris.util.nbt.tag.IntArrayTag;
import com.volmit.iris.util.nbt.tag.IntTag;
import com.volmit.iris.util.nbt.tag.ListTag;
import com.volmit.iris.util.nbt.tag.LongArrayTag;
import com.volmit.iris.util.nbt.tag.LongTag;
import com.volmit.iris.util.nbt.tag.ShortTag;
import com.volmit.iris.util.nbt.tag.StringTag;
import com.volmit.iris.util.nbt.tag.Tag;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class NBTOutputStream extends DataOutputStream implements MaxDepthIO {
   private static final Map<Byte, ExceptionTriConsumer<NBTOutputStream, Tag<?>, Integer, IOException>> writers = new HashMap();
   private static final Map<Class<?>, Byte> classIdMapping = new HashMap();

   public NBTOutputStream(OutputStream out) {
      super(var1);
   }

   private static void put(byte id, ExceptionTriConsumer<NBTOutputStream, Tag<?>, Integer, IOException> f, Class<?> clazz) {
      writers.put(var0, var1);
      classIdMapping.put(var2, var0);
   }

   static byte idFromClass(Class<?> clazz) {
      Byte var1 = (Byte)classIdMapping.get(var0);
      if (var1 == null) {
         throw new IllegalArgumentException("unknown Tag class " + var0.getName());
      } else {
         return var1;
      }
   }

   private static void writeByte(NBTOutputStream out, Tag<?> tag) {
      var0.writeByte(((ByteTag)var1).asByte());
   }

   private static void writeShort(NBTOutputStream out, Tag<?> tag) {
      var0.writeShort(((ShortTag)var1).asShort());
   }

   private static void writeInt(NBTOutputStream out, Tag<?> tag) {
      var0.writeInt(((IntTag)var1).asInt());
   }

   private static void writeLong(NBTOutputStream out, Tag<?> tag) {
      var0.writeLong(((LongTag)var1).asLong());
   }

   private static void writeFloat(NBTOutputStream out, Tag<?> tag) {
      var0.writeFloat(((FloatTag)var1).asFloat());
   }

   private static void writeDouble(NBTOutputStream out, Tag<?> tag) {
      var0.writeDouble(((DoubleTag)var1).asDouble());
   }

   private static void writeString(NBTOutputStream out, Tag<?> tag) {
      var0.writeUTF(((StringTag)var1).getValue());
   }

   private static void writeByteArray(NBTOutputStream out, Tag<?> tag) {
      var0.writeInt(((ByteArrayTag)var1).length());
      var0.write((byte[])((ByteArrayTag)var1).getValue());
   }

   private static void writeIntArray(NBTOutputStream out, Tag<?> tag) {
      var0.writeInt(((IntArrayTag)var1).length());
      int[] var2 = (int[])((IntArrayTag)var1).getValue();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var2[var4];
         var0.writeInt(var5);
      }

   }

   private static void writeLongArray(NBTOutputStream out, Tag<?> tag) {
      var0.writeInt(((LongArrayTag)var1).length());
      long[] var2 = (long[])((LongArrayTag)var1).getValue();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         long var5 = var2[var4];
         var0.writeLong(var5);
      }

   }

   private static void writeList(NBTOutputStream out, Tag<?> tag, int maxDepth) {
      var0.writeByte(idFromClass(((ListTag)var1).getTypeClass()));
      var0.writeInt(((ListTag)var1).size());
      Iterator var3 = ((ListTag)var1).iterator();

      while(var3.hasNext()) {
         Tag var4 = (Tag)var3.next();
         var0.writeRawTag(var4, var0.decrementMaxDepth(var2));
      }

   }

   private static void writeCompound(NBTOutputStream out, Tag<?> tag, int maxDepth) {
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

   public void writeTag(NamedTag tag, int maxDepth) {
      this.writeByte(var1.getTag().getID());
      if (var1.getTag().getID() != 0) {
         this.writeUTF(var1.getName() == null ? "" : var1.getName());
      }

      this.writeRawTag(var1.getTag(), var2);
   }

   public void writeTag(Tag<?> tag, int maxDepth) {
      this.writeByte(var1.getID());
      if (var1.getID() != 0) {
         this.writeUTF("");
      }

      this.writeRawTag(var1, var2);
   }

   public void writeRawTag(Tag<?> tag, int maxDepth) {
      ExceptionTriConsumer var3;
      if ((var3 = (ExceptionTriConsumer)writers.get(var1.getID())) == null) {
         throw new IOException("invalid tag \"" + var1.getID() + "\"");
      } else {
         var3.accept(this, var1, var2);
      }
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
      put((byte)9, NBTOutputStream::writeList, ListTag.class);
      put((byte)10, NBTOutputStream::writeCompound, CompoundTag.class);
      put((byte)11, (var0, var1, var2) -> {
         writeIntArray(var0, var1);
      }, IntArrayTag.class);
      put((byte)12, (var0, var1, var2) -> {
         writeLongArray(var0, var1);
      }, LongArrayTag.class);
   }
}
