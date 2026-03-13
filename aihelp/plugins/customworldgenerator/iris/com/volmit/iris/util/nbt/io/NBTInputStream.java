package com.volmit.iris.util.nbt.io;

import com.volmit.iris.engine.data.io.ExceptionBiFunction;
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
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NBTInputStream extends DataInputStream implements MaxDepthIO {
   private static final Map<Byte, ExceptionBiFunction<NBTInputStream, Integer, ? extends Tag<?>, IOException>> readers = new HashMap();
   private static final Map<Byte, Class<?>> idClassMapping = new HashMap();

   public NBTInputStream(InputStream in) {
      super(var1);
   }

   private static void put(byte id, ExceptionBiFunction<NBTInputStream, Integer, ? extends Tag<?>, IOException> reader, Class<?> clazz) {
      readers.put(var0, var1);
      idClassMapping.put(var0, var2);
   }

   private static ByteTag readByte(NBTInputStream in) {
      return new ByteTag(var0.readByte());
   }

   private static ShortTag readShort(NBTInputStream in) {
      return new ShortTag(var0.readShort());
   }

   private static IntTag readInt(NBTInputStream in) {
      return new IntTag(var0.readInt());
   }

   private static LongTag readLong(NBTInputStream in) {
      return new LongTag(var0.readLong());
   }

   private static FloatTag readFloat(NBTInputStream in) {
      return new FloatTag(var0.readFloat());
   }

   private static DoubleTag readDouble(NBTInputStream in) {
      return new DoubleTag(var0.readDouble());
   }

   private static StringTag readString(NBTInputStream in) {
      return new StringTag(var0.readUTF());
   }

   private static ByteArrayTag readByteArray(NBTInputStream in) {
      ByteArrayTag var1 = new ByteArrayTag(new byte[var0.readInt()]);
      var0.readFully((byte[])var1.getValue());
      return var1;
   }

   private static IntArrayTag readIntArray(NBTInputStream in) {
      int var1 = var0.readInt();
      int[] var2 = new int[var1];
      IntArrayTag var3 = new IntArrayTag(var2);

      for(int var4 = 0; var4 < var1; ++var4) {
         var2[var4] = var0.readInt();
      }

      return var3;
   }

   private static LongArrayTag readLongArray(NBTInputStream in) {
      int var1 = var0.readInt();
      long[] var2 = new long[var1];
      LongArrayTag var3 = new LongArrayTag(var2);

      for(int var4 = 0; var4 < var1; ++var4) {
         var2[var4] = var0.readLong();
      }

      return var3;
   }

   private static ListTag<?> readListTag(NBTInputStream in, int maxDepth) {
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

   private static CompoundTag readCompound(NBTInputStream in, int maxDepth) {
      CompoundTag var2 = new CompoundTag();

      for(int var3 = var0.readByte() & 255; var3 != 0; var3 = var0.readByte() & 255) {
         String var4 = var0.readUTF();
         Tag var5 = var0.readTag((byte)var3, var0.decrementMaxDepth(var1));
         var2.put(var4, var5);
      }

      return var2;
   }

   public NamedTag readTag(int maxDepth) {
      byte var2 = this.readByte();
      return new NamedTag(this.readUTF(), this.readTag(var2, var1));
   }

   public Tag<?> readRawTag(int maxDepth) {
      byte var2 = this.readByte();
      return this.readTag(var2, var1);
   }

   private Tag<?> readTag(byte type, int maxDepth) {
      ExceptionBiFunction var3;
      if ((var3 = (ExceptionBiFunction)readers.get(var1)) == null) {
         throw new IOException("invalid tag id \"" + var1 + "\"");
      } else {
         return (Tag)var3.accept(this, var2);
      }
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
      put((byte)9, NBTInputStream::readListTag, ListTag.class);
      put((byte)10, NBTInputStream::readCompound, CompoundTag.class);
      put((byte)11, (var0, var1) -> {
         return readIntArray(var0);
      }, IntArrayTag.class);
      put((byte)12, (var0, var1) -> {
         return readLongArray(var0);
      }, LongArrayTag.class);
   }
}
