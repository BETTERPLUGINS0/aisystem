package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.Range;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.Collector.Characteristics;

public interface CompoundBinaryTag extends BinaryTag, CompoundTagSetter<CompoundBinaryTag>, Iterable<Entry<String, ? extends BinaryTag>> {
   @NotNull
   static CompoundBinaryTag empty() {
      return CompoundBinaryTagImpl.EMPTY;
   }

   @NotNull
   static CompoundBinaryTag from(@NotNull final Map<String, ? extends BinaryTag> tags) {
      return (CompoundBinaryTag)(tags.isEmpty() ? empty() : new CompoundBinaryTagImpl(new HashMap(tags)));
   }

   @NotNull
   static Collector<Entry<String, ? extends BinaryTag>, ?, CompoundBinaryTag> toCompoundTag() {
      return toCompoundTag(Entry::getKey, Entry::getValue);
   }

   @NotNull
   static <T> Collector<T, ?, CompoundBinaryTag> toCompoundTag(@NotNull final Function<T, String> keyLens, @NotNull final Function<T, ? extends BinaryTag> valueLens) {
      Objects.requireNonNull(keyLens, "keyLens");
      Objects.requireNonNull(valueLens, "valueLens");
      return Collector.of(CompoundBinaryTag::builder, (b, ent) -> {
         b.put((String)keyLens.apply(ent), (BinaryTag)valueLens.apply(ent));
      }, (l, r) -> {
         return (CompoundBinaryTag.Builder)l.put(r.build());
      }, CompoundBinaryTag.Builder::build, Characteristics.UNORDERED);
   }

   @NotNull
   static Collector<Entry<String, ? extends BinaryTag>, ?, CompoundBinaryTag> toCompoundTag(@NotNull final CompoundBinaryTag initial) {
      return toCompoundTag(initial, Entry::getKey, Entry::getValue);
   }

   @NotNull
   static <T> Collector<T, ?, CompoundBinaryTag> toCompoundTag(@NotNull final CompoundBinaryTag initial, @NotNull final Function<T, String> keyLens, @NotNull final Function<T, ? extends BinaryTag> valueLens) {
      Objects.requireNonNull(initial, "initial");
      Objects.requireNonNull(keyLens, "keyLens");
      Objects.requireNonNull(valueLens, "valueLens");
      return Collector.of(() -> {
         return (CompoundBinaryTag.Builder)builder().put(initial);
      }, (b, ent) -> {
         b.put((String)keyLens.apply(ent), (BinaryTag)valueLens.apply(ent));
      }, (l, r) -> {
         return (CompoundBinaryTag.Builder)l.put(r.build());
      }, CompoundBinaryTag.Builder::build, Characteristics.UNORDERED);
   }

   @NotNull
   static CompoundBinaryTag.Builder builder() {
      return new CompoundTagBuilder();
   }

   @NotNull
   static CompoundBinaryTag.Builder builder(@Range(from = 0L,to = 2147483647L) final int initialCapacity) {
      return new CompoundTagBuilder(initialCapacity);
   }

   @NotNull
   default BinaryTagType<CompoundBinaryTag> type() {
      return BinaryTagTypes.COMPOUND;
   }

   boolean contains(@NotNull final String key);

   boolean contains(@NotNull final String key, @NotNull final BinaryTagType<?> type);

   @NotNull
   Set<String> keySet();

   @Nullable
   BinaryTag get(final String key);

   int size();

   boolean isEmpty();

   default boolean getBoolean(@NotNull final String key) {
      return this.getBoolean(key, false);
   }

   default boolean getBoolean(@NotNull final String key, final boolean defaultValue) {
      BinaryTag tag = this.get(key);
      if (tag instanceof ByteBinaryTag) {
         return ((ByteBinaryTag)tag).value() != 0;
      } else {
         return defaultValue;
      }
   }

   default byte getByte(@NotNull final String key) {
      return this.getByte(key, (byte)0);
   }

   byte getByte(@NotNull final String key, final byte defaultValue);

   default short getShort(@NotNull final String key) {
      return this.getShort(key, (short)0);
   }

   short getShort(@NotNull final String key, final short defaultValue);

   default int getInt(@NotNull final String key) {
      return this.getInt(key, 0);
   }

   int getInt(@NotNull final String key, final int defaultValue);

   default long getLong(@NotNull final String key) {
      return this.getLong(key, 0L);
   }

   long getLong(@NotNull final String key, final long defaultValue);

   default float getFloat(@NotNull final String key) {
      return this.getFloat(key, 0.0F);
   }

   float getFloat(@NotNull final String key, final float defaultValue);

   default double getDouble(@NotNull final String key) {
      return this.getDouble(key, 0.0D);
   }

   double getDouble(@NotNull final String key, final double defaultValue);

   @NotNull
   byte[] getByteArray(@NotNull final String key);

   @Contract("_, !null -> !null")
   @Nullable
   byte[] getByteArray(@NotNull final String key, @Nullable final byte[] defaultValue);

   @NotNull
   default String getString(@NotNull final String key) {
      return this.getString(key, "");
   }

   @Contract("_, !null -> !null")
   @Nullable
   String getString(@NotNull final String key, @Nullable final String defaultValue);

   @NotNull
   default ListBinaryTag getList(@NotNull final String key) {
      return this.getList(key, ListBinaryTag.empty());
   }

   @Contract("_, !null -> !null")
   @Nullable
   ListBinaryTag getList(@NotNull final String key, @Nullable final ListBinaryTag defaultValue);

   @NotNull
   default ListBinaryTag getList(@NotNull final String key, @NotNull final BinaryTagType<? extends BinaryTag> expectedType) {
      return this.getList(key, expectedType, ListBinaryTag.empty());
   }

   @Contract("_, _, !null -> !null")
   @Nullable
   ListBinaryTag getList(@NotNull final String key, @NotNull final BinaryTagType<? extends BinaryTag> expectedType, @Nullable final ListBinaryTag defaultValue);

   @NotNull
   default CompoundBinaryTag getCompound(@NotNull final String key) {
      return this.getCompound(key, empty());
   }

   @Contract("_, !null -> !null")
   @Nullable
   CompoundBinaryTag getCompound(@NotNull final String key, @Nullable final CompoundBinaryTag defaultValue);

   @NotNull
   int[] getIntArray(@NotNull final String key);

   @Contract("_, !null -> !null")
   @Nullable
   int[] getIntArray(@NotNull final String key, @Nullable final int[] defaultValue);

   @NotNull
   long[] getLongArray(@NotNull final String key);

   @Contract("_, !null -> !null")
   @Nullable
   long[] getLongArray(@NotNull final String key, @Nullable final long[] defaultValue);

   Stream<Entry<String, ? extends BinaryTag>> stream();

   public interface Builder extends CompoundTagSetter<CompoundBinaryTag.Builder> {
      @NotNull
      CompoundBinaryTag build();
   }
}
