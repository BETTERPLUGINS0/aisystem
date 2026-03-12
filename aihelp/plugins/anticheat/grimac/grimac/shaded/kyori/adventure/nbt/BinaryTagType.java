package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class BinaryTagType<T extends BinaryTag> implements Predicate<BinaryTagType<? extends BinaryTag>> {
   private static final List<BinaryTagType<? extends BinaryTag>> TYPES = new ArrayList();

   public abstract byte id();

   abstract boolean numeric();

   @NotNull
   public abstract T read(@NotNull final DataInput input) throws IOException;

   public abstract void write(@NotNull final T tag, @NotNull final DataOutput output) throws IOException;

   static <T extends BinaryTag> void writeUntyped(final BinaryTagType<? extends BinaryTag> type, final T tag, final DataOutput output) throws IOException {
      type.write(tag, output);
   }

   @NotNull
   static BinaryTagType<? extends BinaryTag> binaryTagType(final byte id) {
      for(int i = 0; i < TYPES.size(); ++i) {
         BinaryTagType<? extends BinaryTag> type = (BinaryTagType)TYPES.get(i);
         if (type.id() == id) {
            return type;
         }
      }

      throw new IllegalArgumentException(String.valueOf(id));
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static BinaryTagType<? extends BinaryTag> of(final byte id) {
      return binaryTagType(id);
   }

   @NotNull
   static <T extends BinaryTag> BinaryTagType<T> register(final Class<T> type, final byte id, final BinaryTagType.Reader<T> reader, @Nullable final BinaryTagType.Writer<T> writer) {
      return register(new BinaryTagType.Impl(type, id, reader, writer));
   }

   @NotNull
   static <T extends NumberBinaryTag> BinaryTagType<T> registerNumeric(final Class<T> type, final byte id, final BinaryTagType.Reader<T> reader, final BinaryTagType.Writer<T> writer) {
      return register(new BinaryTagType.Impl.Numeric(type, id, reader, writer));
   }

   private static <T extends BinaryTag, Y extends BinaryTagType<T>> Y register(final Y type) {
      TYPES.add(type);
      return type;
   }

   public boolean test(final BinaryTagType<? extends BinaryTag> that) {
      return this == that || this.numeric() && that.numeric();
   }

   static class Impl<T extends BinaryTag> extends BinaryTagType<T> {
      final Class<T> type;
      final byte id;
      private final BinaryTagType.Reader<T> reader;
      @Nullable
      private final BinaryTagType.Writer<T> writer;

      Impl(final Class<T> type, final byte id, final BinaryTagType.Reader<T> reader, @Nullable final BinaryTagType.Writer<T> writer) {
         this.type = type;
         this.id = id;
         this.reader = reader;
         this.writer = writer;
      }

      @NotNull
      public final T read(@NotNull final DataInput input) throws IOException {
         return this.reader.read(input);
      }

      public final void write(@NotNull final T tag, @NotNull final DataOutput output) throws IOException {
         if (this.writer != null) {
            this.writer.write(tag, output);
         }

      }

      public final byte id() {
         return this.id;
      }

      boolean numeric() {
         return false;
      }

      public String toString() {
         return BinaryTagType.class.getSimpleName() + '[' + this.type.getSimpleName() + " " + this.id + "]";
      }

      static class Numeric<T extends BinaryTag> extends BinaryTagType.Impl<T> {
         Numeric(final Class<T> type, final byte id, final BinaryTagType.Reader<T> reader, @Nullable final BinaryTagType.Writer<T> writer) {
            super(type, id, reader, writer);
         }

         boolean numeric() {
            return true;
         }

         public String toString() {
            return BinaryTagType.class.getSimpleName() + '[' + this.type.getSimpleName() + " " + this.id + " (numeric)]";
         }
      }
   }

   interface Reader<T extends BinaryTag> {
      @NotNull
      T read(@NotNull final DataInput input) throws IOException;
   }

   interface Writer<T extends BinaryTag> {
      void write(@NotNull final T tag, @NotNull final DataOutput output) throws IOException;
   }
}
