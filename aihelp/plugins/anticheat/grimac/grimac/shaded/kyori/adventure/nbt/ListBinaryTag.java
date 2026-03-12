package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.Range;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface ListBinaryTag extends ListTagSetter<ListBinaryTag, BinaryTag>, BinaryTag, Iterable<BinaryTag> {
   @NotNull
   static ListBinaryTag empty() {
      return ListBinaryTagImpl.EMPTY;
   }

   @NotNull
   static ListBinaryTag from(@NotNull final Iterable<? extends BinaryTag> tags) {
      return ((ListBinaryTag.Builder)builder().add(tags)).build();
   }

   @NotNull
   static ListBinaryTag.Builder<BinaryTag> builder() {
      return new ListTagBuilder(false);
   }

   @NotNull
   static ListBinaryTag.Builder<BinaryTag> builder(@Range(from = 0L,to = 2147483647L) final int initialCapacity) {
      return new ListTagBuilder(false, initialCapacity);
   }

   @NotNull
   static ListBinaryTag.Builder<BinaryTag> heterogeneousListBinaryTag() {
      return new ListTagBuilder(true);
   }

   @NotNull
   static ListBinaryTag.Builder<BinaryTag> heterogeneousListBinaryTag(@Range(from = 0L,to = 2147483647L) final int initialCapacity) {
      return new ListTagBuilder(true, initialCapacity);
   }

   @NotNull
   static <T extends BinaryTag> ListBinaryTag.Builder<T> builder(@NotNull final BinaryTagType<T> type) {
      if (type == BinaryTagTypes.END) {
         throw new IllegalArgumentException("Cannot create a list of " + BinaryTagTypes.END);
      } else {
         return new ListTagBuilder(false, type);
      }
   }

   @NotNull
   static <T extends BinaryTag> ListBinaryTag.Builder<T> builder(@NotNull final BinaryTagType<T> type, @Range(from = 0L,to = 2147483647L) final int initialCapacity) {
      if (type == BinaryTagTypes.END) {
         throw new IllegalArgumentException("Cannot create a list of " + BinaryTagTypes.END);
      } else {
         return new ListTagBuilder(false, type, initialCapacity);
      }
   }

   @NotNull
   static ListBinaryTag listBinaryTag(@NotNull final BinaryTagType<? extends BinaryTag> type, @NotNull final List<BinaryTag> tags) {
      if (tags.isEmpty()) {
         return empty();
      } else if (type == BinaryTagTypes.END) {
         throw new IllegalArgumentException("Cannot create a list of " + BinaryTagTypes.END);
      } else {
         ListBinaryTagImpl.validateTagType(tags, type == BinaryTagTypes.LIST_WILDCARD);
         return new ListBinaryTagImpl(type, type == BinaryTagTypes.LIST_WILDCARD, new ArrayList(tags));
      }
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static ListBinaryTag of(@NotNull final BinaryTagType<? extends BinaryTag> type, @NotNull final List<BinaryTag> tags) {
      return listBinaryTag(type, tags);
   }

   @NotNull
   static Collector<BinaryTag, ?, ListBinaryTag> toListTag() {
      return toListTag((ListBinaryTag)null);
   }

   @NotNull
   static Collector<BinaryTag, ?, ListBinaryTag> toListTag(@Nullable final ListBinaryTag initial) {
      return Collector.of(initial == null ? ListBinaryTag::builder : () -> {
         return (ListBinaryTag.Builder)builder().add(initial);
      }, ListTagSetter::add, (l, r) -> {
         return (ListBinaryTag.Builder)l.add(r.build());
      }, ListBinaryTag.Builder::build);
   }

   @NotNull
   default BinaryTagType<ListBinaryTag> type() {
      return BinaryTagTypes.LIST;
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   default BinaryTagType<? extends BinaryTag> listType() {
      return this.elementType();
   }

   @NotNull
   BinaryTagType<? extends BinaryTag> elementType();

   int size();

   boolean isEmpty();

   @NotNull
   BinaryTag get(@Range(from = 0L,to = 2147483647L) final int index);

   @NotNull
   ListBinaryTag set(final int index, @NotNull final BinaryTag tag, @Nullable final Consumer<? super BinaryTag> removed);

   @NotNull
   ListBinaryTag remove(final int index, @Nullable final Consumer<? super BinaryTag> removed);

   default byte getByte(@Range(from = 0L,to = 2147483647L) final int index) {
      return this.getByte(index, (byte)0);
   }

   default byte getByte(@Range(from = 0L,to = 2147483647L) final int index, final byte defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type().numeric() ? ((NumberBinaryTag)tag).byteValue() : defaultValue;
   }

   default short getShort(@Range(from = 0L,to = 2147483647L) final int index) {
      return this.getShort(index, (short)0);
   }

   default short getShort(@Range(from = 0L,to = 2147483647L) final int index, final short defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type().numeric() ? ((NumberBinaryTag)tag).shortValue() : defaultValue;
   }

   default int getInt(@Range(from = 0L,to = 2147483647L) final int index) {
      return this.getInt(index, 0);
   }

   default int getInt(@Range(from = 0L,to = 2147483647L) final int index, final int defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type().numeric() ? ((NumberBinaryTag)tag).intValue() : defaultValue;
   }

   default long getLong(@Range(from = 0L,to = 2147483647L) final int index) {
      return this.getLong(index, 0L);
   }

   default long getLong(@Range(from = 0L,to = 2147483647L) final int index, final long defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type().numeric() ? ((NumberBinaryTag)tag).longValue() : defaultValue;
   }

   default float getFloat(@Range(from = 0L,to = 2147483647L) final int index) {
      return this.getFloat(index, 0.0F);
   }

   default float getFloat(@Range(from = 0L,to = 2147483647L) final int index, final float defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type().numeric() ? ((NumberBinaryTag)tag).floatValue() : defaultValue;
   }

   default double getDouble(@Range(from = 0L,to = 2147483647L) final int index) {
      return this.getDouble(index, 0.0D);
   }

   default double getDouble(@Range(from = 0L,to = 2147483647L) final int index, final double defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type().numeric() ? ((NumberBinaryTag)tag).doubleValue() : defaultValue;
   }

   @NotNull
   default byte[] getByteArray(@Range(from = 0L,to = 2147483647L) final int index) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.BYTE_ARRAY ? ((ByteArrayBinaryTag)tag).value() : new byte[0];
   }

   @Contract("_, !null -> !null")
   @Nullable
   default byte[] getByteArray(@Range(from = 0L,to = 2147483647L) final int index, @Nullable final byte[] defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.BYTE_ARRAY ? ((ByteArrayBinaryTag)tag).value() : defaultValue;
   }

   @NotNull
   default String getString(@Range(from = 0L,to = 2147483647L) final int index) {
      return this.getString(index, "");
   }

   @Contract("_, !null -> !null")
   @Nullable
   default String getString(@Range(from = 0L,to = 2147483647L) final int index, @Nullable final String defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.STRING ? ((StringBinaryTag)tag).value() : defaultValue;
   }

   @NotNull
   default ListBinaryTag getList(@Range(from = 0L,to = 2147483647L) final int index) {
      return this.getList(index, (BinaryTagType)null, empty());
   }

   @NotNull
   default ListBinaryTag getList(@Range(from = 0L,to = 2147483647L) final int index, @Nullable final BinaryTagType<?> elementType) {
      return this.getList(index, elementType, empty());
   }

   @Contract("_, !null -> !null")
   @Nullable
   default ListBinaryTag getList(@Range(from = 0L,to = 2147483647L) final int index, @Nullable final ListBinaryTag defaultValue) {
      return this.getList(index, (BinaryTagType)null, defaultValue);
   }

   @Contract("_, _, !null -> !null")
   @Nullable
   default ListBinaryTag getList(@Range(from = 0L,to = 2147483647L) final int index, @Nullable final BinaryTagType<?> elementType, @Nullable final ListBinaryTag defaultValue) {
      BinaryTag tag = this.get(index);
      if (tag.type() == BinaryTagTypes.LIST) {
         ListBinaryTag list = (ListBinaryTag)tag;
         if (elementType == null || list.elementType() == elementType) {
            return list;
         }
      }

      return defaultValue;
   }

   @NotNull
   default CompoundBinaryTag getCompound(@Range(from = 0L,to = 2147483647L) final int index) {
      return this.getCompound(index, CompoundBinaryTag.empty());
   }

   @Contract("_, !null -> !null")
   @Nullable
   default CompoundBinaryTag getCompound(@Range(from = 0L,to = 2147483647L) final int index, @Nullable final CompoundBinaryTag defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.COMPOUND ? (CompoundBinaryTag)tag : defaultValue;
   }

   @NotNull
   default int[] getIntArray(@Range(from = 0L,to = 2147483647L) final int index) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.INT_ARRAY ? ((IntArrayBinaryTag)tag).value() : new int[0];
   }

   @Contract("_, !null -> !null")
   @Nullable
   default int[] getIntArray(@Range(from = 0L,to = 2147483647L) final int index, @Nullable final int[] defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.INT_ARRAY ? ((IntArrayBinaryTag)tag).value() : defaultValue;
   }

   @NotNull
   default long[] getLongArray(@Range(from = 0L,to = 2147483647L) final int index) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.LONG_ARRAY ? ((LongArrayBinaryTag)tag).value() : new long[0];
   }

   @Contract("_, !null -> !null")
   @Nullable
   default long[] getLongArray(@Range(from = 0L,to = 2147483647L) final int index, @Nullable final long[] defaultValue) {
      BinaryTag tag = this.get(index);
      return tag.type() == BinaryTagTypes.LONG_ARRAY ? ((LongArrayBinaryTag)tag).value() : defaultValue;
   }

   @NotNull
   Stream<BinaryTag> stream();

   @NotNull
   ListBinaryTag unwrapHeterogeneity();

   @NotNull
   ListBinaryTag wrapHeterogeneity();

   public interface Builder<T extends BinaryTag> extends ListTagSetter<ListBinaryTag.Builder<T>, T> {
      @NotNull
      ListBinaryTag build();
   }
}
