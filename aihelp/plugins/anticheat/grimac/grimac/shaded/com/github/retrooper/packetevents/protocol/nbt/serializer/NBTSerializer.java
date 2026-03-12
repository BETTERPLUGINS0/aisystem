package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@ApiStatus.NonExtendable
public class NBTSerializer<IN, OUT> implements NBTReader<NBT, IN>, NBTWriter<NBT, OUT> {
   protected final NBTSerializer.IdReader<IN> idReader;
   protected final NBTSerializer.IdWriter<OUT> idWriter;
   protected final NBTSerializer.NameReader<IN> nameReader;
   protected final NBTSerializer.NameWriter<OUT> nameWriter;
   protected final Map<Integer, NBTType<? extends NBT>> idToType = new HashMap();
   protected final Map<NBTType<? extends NBT>, Integer> typeToId = new HashMap();
   protected final Map<NBTType<? extends NBT>, NBTSerializer.TagReader<IN, ? extends NBT>> typeReaders = new HashMap();
   protected final Map<NBTType<? extends NBT>, NBTSerializer.TagWriter<OUT, ? extends NBT>> typeWriters = new HashMap();

   public NBTSerializer(NBTSerializer.IdReader<IN> idReader, NBTSerializer.IdWriter<OUT> idWriter, NBTSerializer.NameReader<IN> nameReader, NBTSerializer.NameWriter<OUT> nameWriter) {
      this.idReader = idReader;
      this.idWriter = idWriter;
      this.nameReader = nameReader;
      this.nameWriter = nameWriter;
   }

   public NBT deserializeTag(NBTLimiter limiter, IN from, boolean named) throws IOException {
      NBTType<?> type = this.readTagType(limiter, from);
      if (type == NBTType.END) {
         return null;
      } else {
         if (named) {
            this.readTagName(limiter, from);
         }

         return this.readTag(limiter, from, type);
      }
   }

   public void serializeTag(OUT to, NBT tag, boolean named) throws IOException {
      NBTType<?> type = tag.getType();
      this.writeTagType(to, type);
      if (tag.getType() != NBTType.END) {
         if (named) {
            this.writeTagName(to, "");
         }

         this.writeTag(to, tag);
      }
   }

   protected <T extends NBT> void registerType(NBTType<T> type, int id, NBTSerializer.TagReader<IN, T> typeReader, NBTSerializer.TagWriter<OUT, T> typeWriter) {
      if (this.typeToId.containsKey(type)) {
         throw new IllegalArgumentException(MessageFormat.format("Nbt type {0} is already registered", type));
      } else if (this.idToType.containsKey(id)) {
         throw new IllegalArgumentException(MessageFormat.format("Nbt type id {0} is already registered", id));
      } else {
         this.idToType.put(id, type);
         this.typeToId.put(type, id);
         this.typeReaders.put(type, typeReader);
         this.typeWriters.put(type, typeWriter);
      }
   }

   NBTType<?> readTagType(NBTLimiter limiter, IN from) throws IOException {
      int id = this.idReader.readId(limiter, from);
      NBTType<?> type = (NBTType)this.idToType.get(id);
      if (type == null) {
         throw new IOException(MessageFormat.format("Unknown nbt type id {0}", id));
      } else {
         return type;
      }
   }

   @ApiStatus.Internal
   String readTagName(NBTLimiter limiter, IN from) throws IOException {
      return this.nameReader.readName(limiter, from);
   }

   NBT readTag(NBTLimiter limiter, IN from, NBTType<?> type) throws IOException {
      NBTSerializer.TagReader<IN, ? extends NBT> f = (NBTSerializer.TagReader)this.typeReaders.get(type);
      if (f == null) {
         throw new IOException(MessageFormat.format("No reader registered for nbt type {0}", type));
      } else {
         return f.readTag(limiter, from);
      }
   }

   void writeTagType(OUT stream, NBTType<?> type) throws IOException {
      int id = (Integer)this.typeToId.getOrDefault(type, -1);
      if (id == -1) {
         throw new IOException(MessageFormat.format("Unknown nbt type {0}", type));
      } else {
         this.idWriter.writeId(stream, id);
      }
   }

   void writeTagName(OUT stream, String name) throws IOException {
      this.nameWriter.writeName(stream, name);
   }

   void writeTag(OUT stream, NBT tag) throws IOException {
      NBTSerializer.TagWriter<OUT, NBT> f = (NBTSerializer.TagWriter)this.typeWriters.get(tag.getType());
      if (f == null) {
         throw new IOException(MessageFormat.format("No writer registered for nbt type {0}", tag.getType()));
      } else {
         f.writeTag(stream, tag);
      }
   }

   @FunctionalInterface
   protected interface IdReader<T> {
      int readId(NBTLimiter limiter, T from) throws IOException;
   }

   @FunctionalInterface
   protected interface IdWriter<T> {
      void writeId(T to, int id) throws IOException;
   }

   @FunctionalInterface
   protected interface NameReader<T> {
      String readName(NBTLimiter limiter, T from) throws IOException;
   }

   @FunctionalInterface
   protected interface NameWriter<T> {
      void writeName(T to, String name) throws IOException;
   }

   @FunctionalInterface
   protected interface TagReader<IN, T extends NBT> {
      T readTag(NBTLimiter limiter, IN from) throws IOException;
   }

   @FunctionalInterface
   public interface TagWriter<OUT, T extends NBT> {
      void writeTag(OUT to, T tag) throws IOException;
   }
}
