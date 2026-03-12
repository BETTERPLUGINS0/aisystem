package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

public final class BinaryTagIO {
   private BinaryTagIO() {
   }

   @NotNull
   public static BinaryTagIO.Reader unlimitedReader() {
      return BinaryTagReaderImpl.UNLIMITED;
   }

   @NotNull
   public static BinaryTagIO.Reader reader() {
      return BinaryTagReaderImpl.DEFAULT_LIMIT;
   }

   @NotNull
   public static BinaryTagIO.Reader reader(final long sizeLimitBytes) {
      if (sizeLimitBytes <= 0L) {
         throw new IllegalArgumentException("The size limit must be greater than zero");
      } else {
         return new BinaryTagReaderImpl(sizeLimitBytes);
      }
   }

   @NotNull
   public static BinaryTagIO.Writer writer() {
      return BinaryTagWriterImpl.INSTANCE;
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static CompoundBinaryTag readPath(@NotNull final Path path) throws IOException {
      return reader().read(path);
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static CompoundBinaryTag readInputStream(@NotNull final InputStream input) throws IOException {
      return reader().read(input);
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static CompoundBinaryTag readCompressedPath(@NotNull final Path path) throws IOException {
      return reader().read(path, BinaryTagIO.Compression.GZIP);
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static CompoundBinaryTag readCompressedInputStream(@NotNull final InputStream input) throws IOException {
      return reader().read(input, BinaryTagIO.Compression.GZIP);
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public static CompoundBinaryTag readDataInput(@NotNull final DataInput input) throws IOException {
      return reader().read(input);
   }

   /** @deprecated */
   @Deprecated
   public static void writePath(@NotNull final CompoundBinaryTag tag, @NotNull final Path path) throws IOException {
      writer().write(tag, path);
   }

   /** @deprecated */
   @Deprecated
   public static void writeOutputStream(@NotNull final CompoundBinaryTag tag, @NotNull final OutputStream output) throws IOException {
      writer().write(tag, output);
   }

   /** @deprecated */
   @Deprecated
   public static void writeCompressedPath(@NotNull final CompoundBinaryTag tag, @NotNull final Path path) throws IOException {
      writer().write(tag, path, BinaryTagIO.Compression.GZIP);
   }

   /** @deprecated */
   @Deprecated
   public static void writeCompressedOutputStream(@NotNull final CompoundBinaryTag tag, @NotNull final OutputStream output) throws IOException {
      writer().write(tag, output, BinaryTagIO.Compression.GZIP);
   }

   /** @deprecated */
   @Deprecated
   public static void writeDataOutput(@NotNull final CompoundBinaryTag tag, @NotNull final DataOutput output) throws IOException {
      writer().write(tag, output);
   }

   static {
      BinaryTagTypes.COMPOUND.id();
   }

   public interface Reader {
      @NotNull
      default CompoundBinaryTag read(@NotNull final Path path) throws IOException {
         return this.read(path, BinaryTagIO.Compression.NONE);
      }

      @NotNull
      CompoundBinaryTag read(@NotNull final Path path, @NotNull final BinaryTagIO.Compression compression) throws IOException;

      @NotNull
      default CompoundBinaryTag read(@NotNull final InputStream input) throws IOException {
         return this.read(input, BinaryTagIO.Compression.NONE);
      }

      @NotNull
      CompoundBinaryTag read(@NotNull final InputStream input, @NotNull final BinaryTagIO.Compression compression) throws IOException;

      @NotNull
      CompoundBinaryTag read(@NotNull final DataInput input) throws IOException;

      @NotNull
      default CompoundBinaryTag readNameless(@NotNull final Path path) throws IOException {
         return this.readNameless(path, BinaryTagIO.Compression.NONE);
      }

      @NotNull
      CompoundBinaryTag readNameless(@NotNull final Path path, @NotNull final BinaryTagIO.Compression compression) throws IOException;

      @NotNull
      default CompoundBinaryTag readNameless(@NotNull final InputStream input) throws IOException {
         return this.readNameless(input, BinaryTagIO.Compression.NONE);
      }

      @NotNull
      CompoundBinaryTag readNameless(@NotNull final InputStream input, @NotNull final BinaryTagIO.Compression compression) throws IOException;

      @NotNull
      CompoundBinaryTag readNameless(@NotNull final DataInput input) throws IOException;

      @NotNull
      default Entry<String, CompoundBinaryTag> readNamed(@NotNull final Path path) throws IOException {
         return this.readNamed(path, BinaryTagIO.Compression.NONE);
      }

      @NotNull
      Entry<String, CompoundBinaryTag> readNamed(@NotNull final Path path, @NotNull final BinaryTagIO.Compression compression) throws IOException;

      @NotNull
      default Entry<String, CompoundBinaryTag> readNamed(@NotNull final InputStream input) throws IOException {
         return this.readNamed(input, BinaryTagIO.Compression.NONE);
      }

      @NotNull
      Entry<String, CompoundBinaryTag> readNamed(@NotNull final InputStream input, @NotNull final BinaryTagIO.Compression compression) throws IOException;

      @NotNull
      Entry<String, CompoundBinaryTag> readNamed(@NotNull final DataInput input) throws IOException;
   }

   public interface Writer {
      default void write(@NotNull final CompoundBinaryTag tag, @NotNull final Path path) throws IOException {
         this.write(tag, path, BinaryTagIO.Compression.NONE);
      }

      void write(@NotNull final CompoundBinaryTag tag, @NotNull final Path path, @NotNull final BinaryTagIO.Compression compression) throws IOException;

      default void write(@NotNull final CompoundBinaryTag tag, @NotNull final OutputStream output) throws IOException {
         this.write(tag, output, BinaryTagIO.Compression.NONE);
      }

      void write(@NotNull final CompoundBinaryTag tag, @NotNull final OutputStream output, @NotNull final BinaryTagIO.Compression compression) throws IOException;

      void write(@NotNull final CompoundBinaryTag tag, @NotNull final DataOutput output) throws IOException;

      default void writeNameless(@NotNull final CompoundBinaryTag tag, @NotNull final Path path) throws IOException {
         this.writeNameless(tag, path, BinaryTagIO.Compression.NONE);
      }

      void writeNameless(@NotNull final CompoundBinaryTag tag, @NotNull final Path path, @NotNull final BinaryTagIO.Compression compression) throws IOException;

      default void writeNameless(@NotNull final CompoundBinaryTag tag, @NotNull final OutputStream output) throws IOException {
         this.writeNameless(tag, output, BinaryTagIO.Compression.NONE);
      }

      void writeNameless(@NotNull final CompoundBinaryTag tag, @NotNull final OutputStream output, @NotNull final BinaryTagIO.Compression compression) throws IOException;

      void writeNameless(@NotNull final CompoundBinaryTag tag, @NotNull final DataOutput output) throws IOException;

      default void writeNamed(@NotNull final Entry<String, CompoundBinaryTag> tag, @NotNull final Path path) throws IOException {
         this.writeNamed(tag, path, BinaryTagIO.Compression.NONE);
      }

      void writeNamed(@NotNull final Entry<String, CompoundBinaryTag> tag, @NotNull final Path path, @NotNull final BinaryTagIO.Compression compression) throws IOException;

      default void writeNamed(@NotNull final Entry<String, CompoundBinaryTag> tag, @NotNull final OutputStream output) throws IOException {
         this.writeNamed(tag, output, BinaryTagIO.Compression.NONE);
      }

      void writeNamed(@NotNull final Entry<String, CompoundBinaryTag> tag, @NotNull final OutputStream output, @NotNull final BinaryTagIO.Compression compression) throws IOException;

      void writeNamed(@NotNull final Entry<String, CompoundBinaryTag> tag, @NotNull final DataOutput output) throws IOException;
   }

   public abstract static class Compression {
      public static final BinaryTagIO.Compression NONE = new BinaryTagIO.Compression() {
         @NotNull
         InputStream decompress(@NotNull final InputStream is) {
            return is;
         }

         @NotNull
         OutputStream compress(@NotNull final OutputStream os) {
            return os;
         }

         public String toString() {
            return "Compression.NONE";
         }
      };
      public static final BinaryTagIO.Compression GZIP = new BinaryTagIO.Compression() {
         @NotNull
         InputStream decompress(@NotNull final InputStream is) throws IOException {
            return new GZIPInputStream(is);
         }

         @NotNull
         OutputStream compress(@NotNull final OutputStream os) throws IOException {
            return new GZIPOutputStream(os);
         }

         public String toString() {
            return "Compression.GZIP";
         }
      };
      public static final BinaryTagIO.Compression ZLIB = new BinaryTagIO.Compression() {
         @NotNull
         InputStream decompress(@NotNull final InputStream is) {
            return new InflaterInputStream(is);
         }

         @NotNull
         OutputStream compress(@NotNull final OutputStream os) {
            return new DeflaterOutputStream(os);
         }

         public String toString() {
            return "Compression.ZLIB";
         }
      };

      @NotNull
      abstract InputStream decompress(@NotNull final InputStream is) throws IOException;

      @NotNull
      abstract OutputStream compress(@NotNull final OutputStream os) throws IOException;
   }
}
