package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public final class SequentialNBTReader implements NBTReader<NBT, DataInputStream> {
   public static final SequentialNBTReader INSTANCE = new SequentialNBTReader();
   private static final Map<NBTType<?>, SequentialNBTReader.TagSkip> TAG_SKIPS = new HashMap(16);
   private static final Map<NBTType<?>, SequentialNBTReader.TagBinaryReader> TAG_BINARY_READERS = new HashMap(16);

   public NBT deserializeTag(NBTLimiter limiter, DataInputStream from, boolean named) throws IOException {
      NBTType<?> type = DefaultNBTSerializer.INSTANCE.readTagType(limiter, from);
      if (named) {
         int len = from.readUnsignedShort();
         from.skipBytes(len);
      }

      Object nbt;
      if (type == NBTType.COMPOUND) {
         nbt = new SequentialNBTReader.Compound(from, limiter, () -> {
         });
      } else if (type == NBTType.LIST) {
         nbt = new SequentialNBTReader.List(from, limiter, () -> {
         });
      } else {
         nbt = DefaultNBTSerializer.INSTANCE.readTag(limiter, from, type);
      }

      return (NBT)nbt;
   }

   private static void checkReadable(NBT lastRead) {
      if (lastRead != null) {
         if (lastRead instanceof Iterator && ((Iterator)lastRead).hasNext()) {
            throw new IllegalStateException("Previous nbt has not been read completely");
         }
      }
   }

   public static void intToBytes(byte[] array, int val, int offset) {
      array[offset] = (byte)(val >>> 24 & 255);
      array[offset + 1] = (byte)(val >>> 16 & 255);
      array[offset + 2] = (byte)(val >>> 8 & 255);
      array[offset + 3] = (byte)(val & 255);
   }

   static {
      TAG_SKIPS.put(NBTType.BYTE, (limiter, in) -> {
         limiter.increment(9);
         in.skipBytes(1);
      });
      TAG_SKIPS.put(NBTType.SHORT, (limiter, in) -> {
         limiter.increment(10);
         in.skipBytes(2);
      });
      TAG_SKIPS.put(NBTType.INT, (limiter, in) -> {
         limiter.increment(12);
         in.skipBytes(4);
      });
      TAG_SKIPS.put(NBTType.LONG, (limiter, in) -> {
         limiter.increment(16);
         in.skipBytes(8);
      });
      TAG_SKIPS.put(NBTType.FLOAT, (limiter, in) -> {
         limiter.increment(12);
         in.skipBytes(4);
      });
      TAG_SKIPS.put(NBTType.DOUBLE, (limiter, in) -> {
         limiter.increment(16);
         in.skipBytes(8);
      });
      TAG_SKIPS.put(NBTType.BYTE_ARRAY, (limiter, in) -> {
         limiter.increment(24);
         int length = in.readInt();
         limiter.increment(length * 1);
         limiter.checkReadability(length * 1);
         in.skipBytes(length);
      });
      TAG_SKIPS.put(NBTType.STRING, (limiter, in) -> {
         limiter.increment(36);
         int length = in.readUnsignedShort();
         limiter.increment(length * 2);
         in.skipBytes(length);
      });
      TAG_SKIPS.put(NBTType.LIST, (limiter, in) -> {
         limiter.enterDepth();

         try {
            limiter.increment(36);
            NBTType<?> listType = DefaultNBTSerializer.INSTANCE.readTagType(limiter, in);
            int length = in.readInt();
            limiter.increment(length * 4);

            for(int i = 0; i < length; ++i) {
               ((SequentialNBTReader.TagSkip)TAG_SKIPS.get(listType)).skip(limiter, in);
            }
         } finally {
            limiter.exitDepth();
         }

      });
      TAG_SKIPS.put(NBTType.COMPOUND, (limiter, in) -> {
         limiter.enterDepth();

         try {
            limiter.increment(48);

            NBTType valueType;
            for(HashSet names = new HashSet(); (valueType = DefaultNBTSerializer.INSTANCE.readTagType(limiter, in)) != NBTType.END; ((SequentialNBTReader.TagSkip)TAG_SKIPS.get(valueType)).skip(limiter, in)) {
               String name = DefaultNBTSerializer.readString(limiter, in);
               if (names.add(name)) {
                  limiter.increment(36);
               }
            }
         } finally {
            limiter.exitDepth();
         }

      });
      TAG_SKIPS.put(NBTType.INT_ARRAY, (limiter, in) -> {
         limiter.increment(24);
         int length = in.readInt();
         limiter.increment(length * 4);
         limiter.checkReadability(length * 4);
         in.skipBytes(length * 4);
      });
      TAG_SKIPS.put(NBTType.LONG_ARRAY, (limiter, in) -> {
         limiter.increment(24);
         int length = in.readInt();
         limiter.increment(length * 8);
         limiter.checkReadability(length * 8);
         in.skipBytes(length * 8);
      });
      TAG_BINARY_READERS.put(NBTType.BYTE, (limiter, in) -> {
         limiter.increment(9);
         return new byte[]{in.readByte()};
      });
      TAG_BINARY_READERS.put(NBTType.SHORT, (limiter, in) -> {
         limiter.increment(10);
         return new byte[]{in.readByte(), in.readByte()};
      });
      TAG_BINARY_READERS.put(NBTType.INT, (limiter, in) -> {
         limiter.increment(12);
         byte[] bytes = new byte[4];
         in.readFully(bytes);
         return bytes;
      });
      TAG_BINARY_READERS.put(NBTType.LONG, (limiter, in) -> {
         limiter.increment(16);
         byte[] bytes = new byte[8];
         in.readFully(bytes);
         return bytes;
      });
      TAG_BINARY_READERS.put(NBTType.FLOAT, (limiter, in) -> {
         limiter.increment(12);
         byte[] bytes = new byte[4];
         in.readFully(bytes);
         return bytes;
      });
      TAG_BINARY_READERS.put(NBTType.DOUBLE, (limiter, in) -> {
         limiter.increment(16);
         byte[] bytes = new byte[8];
         in.readFully(bytes);
         return bytes;
      });
      TAG_BINARY_READERS.put(NBTType.BYTE_ARRAY, (limiter, in) -> {
         limiter.increment(24);
         int len = in.readInt();
         if (len >= 16777216) {
            throw new IllegalArgumentException("Byte array length is too large: " + len);
         } else {
            limiter.increment(len * 1);
            limiter.checkReadability(len * 1);
            byte[] array = new byte[4 + len];
            intToBytes(array, len, 0);
            in.readFully(array, 4, len);
            return array;
         }
      });
      TAG_BINARY_READERS.put(NBTType.STRING, (limiter, in) -> {
         limiter.increment(36);
         String string = in.readUTF();
         limiter.increment(string.length() * 2);
         ByteArrayOutputStream bytes = new ByteArrayOutputStream();

         byte[] var5;
         try {
            DataOutputStream out = new DataOutputStream(bytes);

            try {
               out.writeUTF(string);
               var5 = bytes.toByteArray();
            } catch (Throwable var9) {
               try {
                  out.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }

               throw var9;
            }

            out.close();
         } catch (Throwable var10) {
            try {
               bytes.close();
            } catch (Throwable var7) {
               var10.addSuppressed(var7);
            }

            throw var10;
         }

         bytes.close();
         return var5;
      });
      TAG_BINARY_READERS.put(NBTType.LIST, (limiter, in) -> {
         limiter.enterDepth();

         byte[] var20;
         try {
            limiter.increment(36);
            byte type = in.readByte();
            NBTType<? extends NBT> nbtType = (NBTType)DefaultNBTSerializer.INSTANCE.idToType.get(Integer.valueOf(type));
            int len = in.readInt();
            limiter.increment(len * 4);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            try {
               DataOutputStream out = new DataOutputStream(bytes);

               try {
                  out.write(type);
                  out.writeInt(len);
                  int i = 0;

                  while(true) {
                     if (i >= len) {
                        var20 = bytes.toByteArray();
                        break;
                     }

                     out.write(((SequentialNBTReader.TagBinaryReader)TAG_BINARY_READERS.get(nbtType)).read(limiter, in));
                     ++i;
                  }
               } catch (Throwable var17) {
                  try {
                     out.close();
                  } catch (Throwable var16) {
                     var17.addSuppressed(var16);
                  }

                  throw var17;
               }

               out.close();
            } catch (Throwable var18) {
               try {
                  bytes.close();
               } catch (Throwable var15) {
                  var18.addSuppressed(var15);
               }

               throw var18;
            }

            bytes.close();
         } finally {
            limiter.exitDepth();
         }

         return var20;
      });
      TAG_BINARY_READERS.put(NBTType.COMPOUND, (limiter, in) -> {
         limiter.enterDepth();

         byte[] var18;
         try {
            limiter.increment(48);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            try {
               DataOutputStream out = new DataOutputStream(bytes);

               try {
                  HashSet names = new HashSet();

                  byte type;
                  while((type = in.readByte()) != 0) {
                     out.write(type);
                     String name = DefaultNBTSerializer.readString(limiter, in);
                     out.writeUTF(name);
                     out.write(((SequentialNBTReader.TagBinaryReader)TAG_BINARY_READERS.get(DefaultNBTSerializer.INSTANCE.idToType.get(Integer.valueOf(type)))).read(limiter, in));
                     if (names.add(name)) {
                        limiter.increment(36);
                     }
                  }

                  out.write(type);
                  var18 = bytes.toByteArray();
               } catch (Throwable var15) {
                  try {
                     out.close();
                  } catch (Throwable var14) {
                     var15.addSuppressed(var14);
                  }

                  throw var15;
               }

               out.close();
            } catch (Throwable var16) {
               try {
                  bytes.close();
               } catch (Throwable var13) {
                  var16.addSuppressed(var13);
               }

               throw var16;
            }

            bytes.close();
         } finally {
            limiter.exitDepth();
         }

         return var18;
      });
      TAG_BINARY_READERS.put(NBTType.INT_ARRAY, (limiter, in) -> {
         limiter.increment(24);
         int len = in.readInt();
         limiter.increment(len * 4);
         limiter.checkReadability(len * 4);
         byte[] array = new byte[4 + len * 4];
         intToBytes(array, len, 0);
         in.readFully(array, 4, len * 4);
         return array;
      });
      TAG_BINARY_READERS.put(NBTType.LONG_ARRAY, (limiter, in) -> {
         limiter.increment(24);
         int len = in.readInt();
         limiter.increment(len * 8);
         limiter.checkReadability(len * 8);
         byte[] array = new byte[4 + len * 8];
         intToBytes(array, len, 0);
         in.readFully(array, 4, len * 8);
         return array;
      });
   }

   public static class Compound extends NBT implements Iterator<Entry<String, NBT>>, Iterable<Entry<String, NBT>>, SequentialNBTReader.Skippable, Closeable {
      private final DataInputStream stream;
      private final NBTLimiter limiter;
      private final Runnable onComplete;
      private NBTType<?> nextType;
      private NBT lastRead;
      private boolean hasReadType;

      private Compound(DataInputStream stream, NBTLimiter limiter, Runnable onComplete) {
         this.stream = stream;
         this.limiter = limiter;
         this.onComplete = onComplete;
         limiter.increment(48);
         this.runCompleted();
      }

      public NBTType<?> getType() {
         return NBTType.COMPOUND;
      }

      public boolean equals(Object other) {
         return this == other;
      }

      public int hashCode() {
         return 0;
      }

      public NBT copy() {
         throw new UnsupportedOperationException();
      }

      public boolean hasNext() {
         SequentialNBTReader.checkReadable(this.lastRead);
         if (!this.hasReadType) {
            try {
               this.nextType = DefaultNBTSerializer.INSTANCE.readTagType(this.limiter, this.stream);
               this.hasReadType = true;
            } catch (IOException var2) {
               throw new RuntimeException(var2);
            }
         }

         return this.nextType != NBTType.END;
      }

      public Entry<String, NBT> next() {
         if (!this.hasNext()) {
            throw new IllegalStateException("No more elements in compound");
         } else {
            try {
               this.hasReadType = false;
               String name = DefaultNBTSerializer.readString(this.limiter, this.stream);
               if (this.nextType == NBTType.COMPOUND) {
                  this.lastRead = new SequentialNBTReader.Compound(this.stream, this.limiter, this::runCompleted);
               } else if (this.nextType == NBTType.LIST) {
                  this.lastRead = new SequentialNBTReader.List(this.stream, this.limiter, this::runCompleted);
               } else {
                  this.lastRead = DefaultNBTSerializer.INSTANCE.readTag(this.limiter, this.stream, this.nextType);
                  this.runCompleted();
               }

               this.limiter.increment(36);
               return new SimpleEntry(name, this.lastRead);
            } catch (IOException var2) {
               throw new RuntimeException(var2);
            }
         }
      }

      private void runCompleted() {
         if (!this.hasNext()) {
            this.onComplete.run();
         }

      }

      @NotNull
      public Iterator<Entry<String, NBT>> iterator() {
         return this;
      }

      public void skip() {
         if (this.lastRead instanceof SequentialNBTReader.Skippable) {
            ((SequentialNBTReader.Skippable)this.lastRead).skip();
         }

         if (this.hasNext()) {
            try {
               int len = this.stream.readUnsignedShort();
               this.stream.skipBytes(len);
               ((SequentialNBTReader.TagSkip)SequentialNBTReader.TAG_SKIPS.get(this.nextType)).skip(this.limiter, this.stream);
               this.limiter.increment(36);
               ((SequentialNBTReader.TagSkip)SequentialNBTReader.TAG_SKIPS.get(NBTType.COMPOUND)).skip(this.limiter, this.stream);
               this.hasReadType = true;
               this.nextType = NBTType.END;
            } catch (IOException var2) {
               throw new RuntimeException(var2);
            }

            this.runCompleted();
         }
      }

      public void skipOne() {
         SequentialNBTReader.checkReadable(this.lastRead);
         if (this.hasNext()) {
            try {
               int len = this.stream.readUnsignedShort();
               this.stream.skipBytes(len);
               ((SequentialNBTReader.TagSkip)SequentialNBTReader.TAG_SKIPS.get(this.nextType)).skip(this.limiter, this.stream);
               this.limiter.increment(36);
               this.hasReadType = false;
            } catch (IOException var2) {
               throw new RuntimeException(var2);
            }

            this.runCompleted();
         }
      }

      public NBTCompound readFully() {
         try {
            if (this.lastRead instanceof SequentialNBTReader.Skippable) {
               ((SequentialNBTReader.Skippable)this.lastRead).skip();
            }

            if (!this.hasNext()) {
               return new NBTCompound();
            } else {
               NBTCompound compound = new NBTCompound();

               do {
                  String name = DefaultNBTSerializer.readString(this.limiter, this.stream);
                  NBT nbt = DefaultNBTSerializer.INSTANCE.readTag(this.limiter, this.stream, this.nextType);
                  this.limiter.increment(36);
                  compound.setTag(name, nbt);
               } while((this.nextType = DefaultNBTSerializer.INSTANCE.readTagType(this.limiter, this.stream)) != NBTType.END);

               this.hasReadType = true;
               this.runCompleted();
               return compound;
            }
         } catch (IOException var4) {
            throw new RuntimeException(var4);
         }
      }

      public byte[] readFullyAsBytes() {
         try {
            if (this.lastRead instanceof SequentialNBTReader.Skippable) {
               ((SequentialNBTReader.Skippable)this.lastRead).skip();
            }

            if (!this.hasNext()) {
               return new byte[]{10, 0};
            } else {
               ByteArrayOutputStream bytes = new ByteArrayOutputStream();

               byte[] name;
               try {
                  DataOutputStream out = new DataOutputStream(bytes);

                  try {
                     out.write(10);

                     do {
                        out.write((Integer)DefaultNBTSerializer.INSTANCE.typeToId.get(this.nextType));
                        name = ((SequentialNBTReader.TagBinaryReader)SequentialNBTReader.TAG_BINARY_READERS.get(NBTType.STRING)).read(NBTLimiter.noop(), this.stream);
                        this.limiter.increment(name.length * 2 + 28);
                        out.write(name);
                        byte[] nbt = ((SequentialNBTReader.TagBinaryReader)SequentialNBTReader.TAG_BINARY_READERS.get(this.nextType)).read(this.limiter, this.stream);
                        this.limiter.increment(36);
                        out.write(nbt);
                     } while((this.nextType = DefaultNBTSerializer.INSTANCE.readTagType(this.limiter, this.stream)) != NBTType.END);

                     out.write(0);
                     this.hasReadType = true;
                     this.runCompleted();
                     name = bytes.toByteArray();
                  } catch (Throwable var7) {
                     try {
                        out.close();
                     } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                     }

                     throw var7;
                  }

                  out.close();
               } catch (Throwable var8) {
                  try {
                     bytes.close();
                  } catch (Throwable var5) {
                     var8.addSuppressed(var5);
                  }

                  throw var8;
               }

               bytes.close();
               return name;
            }
         } catch (IOException var9) {
            throw new RuntimeException(var9);
         }
      }

      public void close() throws IOException {
         this.stream.close();
      }

      // $FF: synthetic method
      Compound(DataInputStream x0, NBTLimiter x1, Runnable x2, Object x3) {
         this(x0, x1, x2);
      }
   }

   public static class List extends NBT implements Iterator<NBT>, Iterable<NBT>, SequentialNBTReader.Skippable, Closeable {
      private final DataInputStream stream;
      private final NBTLimiter limiter;
      private final Runnable onComplete;
      private final NBTType<?> listType;
      private NBT lastRead;
      public int remaining;

      private List(DataInputStream stream, NBTLimiter limiter, Runnable onComplete) {
         this.stream = stream;
         this.limiter = limiter;
         this.onComplete = onComplete;
         limiter.increment(37);

         try {
            this.listType = DefaultNBTSerializer.INSTANCE.readTagType(limiter, stream);
            this.remaining = stream.readInt();
            limiter.increment(this.remaining * 4);
         } catch (IOException var5) {
            throw new RuntimeException(var5);
         }

         this.runCompleted();
      }

      public NBTType<?> getType() {
         return NBTType.LIST;
      }

      public boolean equals(Object other) {
         return this == other;
      }

      public int hashCode() {
         return 1;
      }

      public NBT copy() {
         throw new UnsupportedOperationException();
      }

      public boolean hasNext() {
         return this.remaining > 0;
      }

      public NBT next() {
         SequentialNBTReader.checkReadable(this.lastRead);
         if (!this.hasNext()) {
            throw new IllegalStateException("No more elements in list");
         } else {
            try {
               --this.remaining;
               if (this.listType == NBTType.COMPOUND) {
                  this.lastRead = new SequentialNBTReader.Compound(this.stream, this.limiter, this::runCompleted);
               } else if (this.listType == NBTType.LIST) {
                  this.lastRead = new SequentialNBTReader.List(this.stream, this.limiter, this::runCompleted);
               } else {
                  this.lastRead = DefaultNBTSerializer.INSTANCE.readTag(this.limiter, this.stream, this.listType);
                  this.runCompleted();
               }

               return this.lastRead;
            } catch (IOException var2) {
               throw new RuntimeException(var2);
            }
         }
      }

      private void runCompleted() {
         if (!this.hasNext()) {
            this.onComplete.run();
         }

      }

      @NotNull
      public Iterator<NBT> iterator() {
         return this;
      }

      public void skip() {
         if (this.lastRead instanceof SequentialNBTReader.Skippable) {
            ((SequentialNBTReader.Skippable)this.lastRead).skip();
         }

         if (this.hasNext()) {
            try {
               SequentialNBTReader.TagSkip typeSkip = (SequentialNBTReader.TagSkip)SequentialNBTReader.TAG_SKIPS.get(this.listType);

               for(int i = 0; i < this.remaining; ++i) {
                  typeSkip.skip(this.limiter, this.stream);
               }
            } catch (IOException var3) {
               throw new RuntimeException(var3);
            }

            this.remaining = 0;
            this.runCompleted();
         }
      }

      public void skipOne() {
         SequentialNBTReader.checkReadable(this.lastRead);
         if (this.hasNext()) {
            try {
               ((SequentialNBTReader.TagSkip)SequentialNBTReader.TAG_SKIPS.get(this.listType)).skip(this.limiter, this.stream);
            } catch (IOException var2) {
               throw new RuntimeException(var2);
            }

            --this.remaining;
            this.runCompleted();
         }
      }

      public NBTList<NBT> readFully() {
         try {
            if (this.lastRead instanceof SequentialNBTReader.Skippable) {
               ((SequentialNBTReader.Skippable)this.lastRead).skip();
            }

            if (!this.hasNext()) {
               return new NBTList(this.listType, 0);
            } else {
               NBTList<NBT> list = new NBTList(this.listType, this.remaining);

               for(int i = 0; i < this.remaining; ++i) {
                  list.addTag(DefaultNBTSerializer.INSTANCE.readTag(this.limiter, this.stream, this.listType));
               }

               this.remaining = 0;
               this.runCompleted();
               return list;
            }
         } catch (IOException var3) {
            throw new RuntimeException(var3);
         }
      }

      public byte[] readFullyAsBinary() {
         try {
            if (this.lastRead instanceof SequentialNBTReader.Skippable) {
               ((SequentialNBTReader.Skippable)this.lastRead).skip();
            }

            if (!this.hasNext()) {
               return new byte[]{9};
            } else {
               byte[] array = null;

               for(int i = 0; i < this.remaining; ++i) {
                  byte[] element = ((SequentialNBTReader.TagBinaryReader)SequentialNBTReader.TAG_BINARY_READERS.get(this.listType)).read(this.limiter, this.stream);
                  if (array == null) {
                     array = new byte[6 + this.remaining * element.length];
                     array[0] = 9;
                     array[1] = ((Integer)DefaultNBTSerializer.INSTANCE.typeToId.get(this.listType)).byteValue();
                     array[2] = (byte)(this.remaining >>> 24);
                     array[3] = (byte)(this.remaining >>> 16);
                     array[4] = (byte)(this.remaining >>> 8);
                     array[5] = (byte)this.remaining;
                  }

                  System.arraycopy(element, 0, array, 5 + i * element.length, element.length);
               }

               this.remaining = 0;
               this.runCompleted();
               return array;
            }
         } catch (IOException var4) {
            throw new RuntimeException(var4);
         }
      }

      public void close() throws IOException {
         this.stream.close();
      }

      // $FF: synthetic method
      List(DataInputStream x0, NBTLimiter x1, Runnable x2, Object x3) {
         this(x0, x1, x2);
      }
   }

   @FunctionalInterface
   private interface TagBinaryReader {
      byte[] read(NBTLimiter limiter, DataInput in) throws IOException;
   }

   @FunctionalInterface
   private interface TagSkip {
      void skip(NBTLimiter limiter, DataInput in) throws IOException;
   }

   interface Skippable {
      void skip();

      void skipOne();
   }
}
