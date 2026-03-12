package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public final class BinaryTagTypes {
   public static final BinaryTagType<EndBinaryTag> END = BinaryTagType.register(EndBinaryTag.class, (byte)0, (input) -> {
      return EndBinaryTag.get();
   }, (BinaryTagType.Writer)null);
   public static final BinaryTagType<ByteBinaryTag> BYTE = BinaryTagType.registerNumeric(ByteBinaryTag.class, (byte)1, (input) -> {
      return ByteBinaryTag.of(input.readByte());
   }, (tag, output) -> {
      output.writeByte(tag.value());
   });
   public static final BinaryTagType<ShortBinaryTag> SHORT = BinaryTagType.registerNumeric(ShortBinaryTag.class, (byte)2, (input) -> {
      return ShortBinaryTag.of(input.readShort());
   }, (tag, output) -> {
      output.writeShort(tag.value());
   });
   public static final BinaryTagType<IntBinaryTag> INT = BinaryTagType.registerNumeric(IntBinaryTag.class, (byte)3, (input) -> {
      return IntBinaryTag.of(input.readInt());
   }, (tag, output) -> {
      output.writeInt(tag.value());
   });
   public static final BinaryTagType<LongBinaryTag> LONG = BinaryTagType.registerNumeric(LongBinaryTag.class, (byte)4, (input) -> {
      return LongBinaryTag.of(input.readLong());
   }, (tag, output) -> {
      output.writeLong(tag.value());
   });
   public static final BinaryTagType<FloatBinaryTag> FLOAT = BinaryTagType.registerNumeric(FloatBinaryTag.class, (byte)5, (input) -> {
      return FloatBinaryTag.of(input.readFloat());
   }, (tag, output) -> {
      output.writeFloat(tag.value());
   });
   public static final BinaryTagType<DoubleBinaryTag> DOUBLE = BinaryTagType.registerNumeric(DoubleBinaryTag.class, (byte)6, (input) -> {
      return DoubleBinaryTag.of(input.readDouble());
   }, (tag, output) -> {
      output.writeDouble(tag.value());
   });
   public static final BinaryTagType<ByteArrayBinaryTag> BYTE_ARRAY = BinaryTagType.register(ByteArrayBinaryTag.class, (byte)7, (input) -> {
      int length = input.readInt();
      BinaryTagScope ignored = TrackingDataInput.enter(input, (long)length);

      ByteArrayBinaryTag var4;
      try {
         byte[] value = new byte[length];
         input.readFully(value);
         var4 = ByteArrayBinaryTag.of(value);
      } catch (Throwable var6) {
         if (ignored != null) {
            try {
               ignored.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (ignored != null) {
         ignored.close();
      }

      return var4;
   }, (tag, output) -> {
      byte[] value = ByteArrayBinaryTagImpl.value(tag);
      output.writeInt(value.length);
      output.write(value);
   });
   public static final BinaryTagType<StringBinaryTag> STRING = BinaryTagType.register(StringBinaryTag.class, (byte)8, (input) -> {
      return StringBinaryTag.of(input.readUTF());
   }, (tag, output) -> {
      output.writeUTF(tag.value());
   });
   public static final BinaryTagType<ListBinaryTag> LIST = BinaryTagType.register(ListBinaryTag.class, (byte)9, (input) -> {
      BinaryTagType<? extends BinaryTag> type = BinaryTagType.of(input.readByte());
      int length = input.readInt();
      BinaryTagScope ignored = TrackingDataInput.enter(input, (long)length * 8L);

      ListBinaryTag var8;
      try {
         List<BinaryTag> tags = new ArrayList(length);
         int i = 0;

         while(true) {
            if (i >= length) {
               var8 = ListBinaryTag.of(type, tags);
               break;
            }

            tags.add(type.read(input));
            ++i;
         }
      } catch (Throwable var7) {
         if (ignored != null) {
            try {
               ignored.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignored != null) {
         ignored.close();
      }

      return var8;
   }, (tag, output) -> {
      output.writeByte(tag.elementType().id());
      int size = tag.size();
      output.writeInt(size);
      Iterator var3 = tag.iterator();

      while(var3.hasNext()) {
         BinaryTag item = (BinaryTag)var3.next();
         BinaryTagType.writeUntyped(item.type(), item, output);
      }

   });
   public static final BinaryTagType<CompoundBinaryTag> COMPOUND = BinaryTagType.register(CompoundBinaryTag.class, (byte)10, (input) -> {
      BinaryTagScope ignored = TrackingDataInput.enter(input);

      CompoundBinaryTagImpl var8;
      try {
         HashMap tags = new HashMap();

         while(true) {
            BinaryTagType type;
            if ((type = BinaryTagType.of(input.readByte())) == END) {
               var8 = new CompoundBinaryTagImpl(tags);
               break;
            }

            String key = input.readUTF();
            BinaryTag tag = type.read(input);
            tags.put(key, tag);
         }
      } catch (Throwable var7) {
         if (ignored != null) {
            try {
               ignored.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignored != null) {
         ignored.close();
      }

      return var8;
   }, (tag, output) -> {
      Iterator var2 = tag.iterator();

      while(var2.hasNext()) {
         Entry<String, ? extends BinaryTag> entry = (Entry)var2.next();
         BinaryTag value = (BinaryTag)entry.getValue();
         if (value != null) {
            BinaryTagType<? extends BinaryTag> type = value.type();
            output.writeByte(type.id());
            if (type != END) {
               output.writeUTF((String)entry.getKey());
               BinaryTagType.writeUntyped(type, value, output);
            }
         }
      }

      output.writeByte(END.id());
   });
   public static final BinaryTagType<IntArrayBinaryTag> INT_ARRAY = BinaryTagType.register(IntArrayBinaryTag.class, (byte)11, (input) -> {
      int length = input.readInt();
      BinaryTagScope ignored = TrackingDataInput.enter(input, (long)length * 4L);

      IntArrayBinaryTag var7;
      try {
         int[] value = new int[length];
         int i = 0;

         while(true) {
            if (i >= length) {
               var7 = IntArrayBinaryTag.of(value);
               break;
            }

            value[i] = input.readInt();
            ++i;
         }
      } catch (Throwable var6) {
         if (ignored != null) {
            try {
               ignored.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (ignored != null) {
         ignored.close();
      }

      return var7;
   }, (tag, output) -> {
      int[] value = IntArrayBinaryTagImpl.value(tag);
      int length = value.length;
      output.writeInt(length);

      for(int i = 0; i < length; ++i) {
         output.writeInt(value[i]);
      }

   });
   public static final BinaryTagType<LongArrayBinaryTag> LONG_ARRAY = BinaryTagType.register(LongArrayBinaryTag.class, (byte)12, (input) -> {
      int length = input.readInt();
      BinaryTagScope ignored = TrackingDataInput.enter(input, (long)length * 8L);

      LongArrayBinaryTag var7;
      try {
         long[] value = new long[length];
         int i = 0;

         while(true) {
            if (i >= length) {
               var7 = LongArrayBinaryTag.of(value);
               break;
            }

            value[i] = input.readLong();
            ++i;
         }
      } catch (Throwable var6) {
         if (ignored != null) {
            try {
               ignored.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (ignored != null) {
         ignored.close();
      }

      return var7;
   }, (tag, output) -> {
      long[] value = LongArrayBinaryTagImpl.value(tag);
      int length = value.length;
      output.writeInt(length);

      for(int i = 0; i < length; ++i) {
         output.writeLong(value[i]);
      }

   });

   private BinaryTagTypes() {
   }
}
