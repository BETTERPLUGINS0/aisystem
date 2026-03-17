/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Debug$Renderer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.nbt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.kyori.adventure.nbt.AbstractBinaryTag;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.ByteArrayBinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.IntArrayBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.LongArrayBinaryTag;
import net.kyori.adventure.nbt.NumberBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Debug.Renderer(text="\"CompoundBinaryTag[length=\" + this.tags.size() + \"]\"", childrenArray="this.tags.entrySet().toArray()", hasChildren="!this.tags.isEmpty()")
final class CompoundBinaryTagImpl
extends AbstractBinaryTag
implements CompoundBinaryTag {
    static final CompoundBinaryTag EMPTY = new CompoundBinaryTagImpl(Collections.emptyMap());
    private final Map<String, BinaryTag> tags;
    private final int hashCode;

    CompoundBinaryTagImpl(Map<String, BinaryTag> map) {
        this.tags = Collections.unmodifiableMap(map);
        this.hashCode = map.hashCode();
    }

    public boolean contains(@NotNull String string, @NotNull BinaryTagType<?> binaryTagType) {
        @Nullable BinaryTag binaryTag = this.tags.get(string);
        return binaryTag != null && binaryTagType.test(binaryTag.type());
    }

    @Override
    @NotNull
    public Set<String> keySet() {
        return Collections.unmodifiableSet(this.tags.keySet());
    }

    @Override
    @Nullable
    public BinaryTag get(String string) {
        return this.tags.get(string);
    }

    @Override
    public int size() {
        return this.tags.size();
    }

    @Override
    @NotNull
    public CompoundBinaryTag put(@NotNull String string, @NotNull BinaryTag binaryTag) {
        return this.edit(map -> map.put(string, binaryTag));
    }

    @Override
    @NotNull
    public CompoundBinaryTag put(@NotNull CompoundBinaryTag compoundBinaryTag) {
        return this.edit(map -> {
            for (String string : compoundBinaryTag.keySet()) {
                map.put(string, compoundBinaryTag.get(string));
            }
        });
    }

    @Override
    @NotNull
    public CompoundBinaryTag put(@NotNull Map<String, ? extends BinaryTag> map) {
        return this.edit(map2 -> map2.putAll(map));
    }

    @Override
    @NotNull
    public CompoundBinaryTag remove(@NotNull String string, @Nullable Consumer<? super BinaryTag> consumer) {
        if (!this.tags.containsKey(string)) {
            return this;
        }
        return this.edit(map -> {
            BinaryTag binaryTag = (BinaryTag)map.remove(string);
            if (consumer != null) {
                consumer.accept(binaryTag);
            }
        });
    }

    @Override
    public byte getByte(@NotNull String string, byte by) {
        if (this.contains(string, BinaryTagTypes.BYTE)) {
            return ((NumberBinaryTag)this.tags.get(string)).byteValue();
        }
        return by;
    }

    @Override
    public short getShort(@NotNull String string, short s) {
        if (this.contains(string, BinaryTagTypes.SHORT)) {
            return ((NumberBinaryTag)this.tags.get(string)).shortValue();
        }
        return s;
    }

    @Override
    public int getInt(@NotNull String string, int n) {
        if (this.contains(string, BinaryTagTypes.INT)) {
            return ((NumberBinaryTag)this.tags.get(string)).intValue();
        }
        return n;
    }

    @Override
    public long getLong(@NotNull String string, long l) {
        if (this.contains(string, BinaryTagTypes.LONG)) {
            return ((NumberBinaryTag)this.tags.get(string)).longValue();
        }
        return l;
    }

    @Override
    public float getFloat(@NotNull String string, float f) {
        if (this.contains(string, BinaryTagTypes.FLOAT)) {
            return ((NumberBinaryTag)this.tags.get(string)).floatValue();
        }
        return f;
    }

    @Override
    public double getDouble(@NotNull String string, double d) {
        if (this.contains(string, BinaryTagTypes.DOUBLE)) {
            return ((NumberBinaryTag)this.tags.get(string)).doubleValue();
        }
        return d;
    }

    @Override
    public byte @NotNull [] getByteArray(@NotNull String string) {
        if (this.contains(string, BinaryTagTypes.BYTE_ARRAY)) {
            return ((ByteArrayBinaryTag)this.tags.get(string)).value();
        }
        return new byte[0];
    }

    @Override
    public byte @NotNull [] getByteArray(@NotNull String string, byte @NotNull [] byArray) {
        if (this.contains(string, BinaryTagTypes.BYTE_ARRAY)) {
            return ((ByteArrayBinaryTag)this.tags.get(string)).value();
        }
        return byArray;
    }

    @Override
    @NotNull
    public String getString(@NotNull String string, @NotNull String string2) {
        if (this.contains(string, BinaryTagTypes.STRING)) {
            return ((StringBinaryTag)this.tags.get(string)).value();
        }
        return string2;
    }

    @Override
    @NotNull
    public ListBinaryTag getList(@NotNull String string, @NotNull ListBinaryTag listBinaryTag) {
        if (this.contains(string, BinaryTagTypes.LIST)) {
            return (ListBinaryTag)this.tags.get(string);
        }
        return listBinaryTag;
    }

    @Override
    @NotNull
    public ListBinaryTag getList(@NotNull String string, @NotNull BinaryTagType<? extends BinaryTag> binaryTagType, @NotNull ListBinaryTag listBinaryTag) {
        ListBinaryTag listBinaryTag2;
        if (this.contains(string, BinaryTagTypes.LIST) && binaryTagType.test((listBinaryTag2 = (ListBinaryTag)this.tags.get(string)).elementType())) {
            return listBinaryTag2;
        }
        return listBinaryTag;
    }

    @Override
    @NotNull
    public CompoundBinaryTag getCompound(@NotNull String string, @NotNull CompoundBinaryTag compoundBinaryTag) {
        if (this.contains(string, BinaryTagTypes.COMPOUND)) {
            return (CompoundBinaryTag)this.tags.get(string);
        }
        return compoundBinaryTag;
    }

    @Override
    public int @NotNull [] getIntArray(@NotNull String string) {
        if (this.contains(string, BinaryTagTypes.INT_ARRAY)) {
            return ((IntArrayBinaryTag)this.tags.get(string)).value();
        }
        return new int[0];
    }

    @Override
    public int @NotNull [] getIntArray(@NotNull String string, int @NotNull [] nArray) {
        if (this.contains(string, BinaryTagTypes.INT_ARRAY)) {
            return ((IntArrayBinaryTag)this.tags.get(string)).value();
        }
        return nArray;
    }

    @Override
    public long @NotNull [] getLongArray(@NotNull String string) {
        if (this.contains(string, BinaryTagTypes.LONG_ARRAY)) {
            return ((LongArrayBinaryTag)this.tags.get(string)).value();
        }
        return new long[0];
    }

    @Override
    public long @NotNull [] getLongArray(@NotNull String string, long @NotNull [] lArray) {
        if (this.contains(string, BinaryTagTypes.LONG_ARRAY)) {
            return ((LongArrayBinaryTag)this.tags.get(string)).value();
        }
        return lArray;
    }

    private CompoundBinaryTag edit(Consumer<Map<String, BinaryTag>> consumer) {
        HashMap<String, BinaryTag> hashMap = new HashMap<String, BinaryTag>(this.tags);
        consumer.accept(hashMap);
        return new CompoundBinaryTagImpl(new HashMap<String, BinaryTag>(hashMap));
    }

    public boolean equals(Object object) {
        return this == object || object instanceof CompoundBinaryTagImpl && this.tags.equals(((CompoundBinaryTagImpl)object).tags);
    }

    public int hashCode() {
        return this.hashCode;
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("tags", this.tags));
    }

    @Override
    @NotNull
    public Iterator<Map.Entry<String, ? extends BinaryTag>> iterator() {
        return this.tags.entrySet().iterator();
    }

    @Override
    public void forEach(@NotNull Consumer<? super Map.Entry<String, ? extends BinaryTag>> consumer) {
        this.tags.entrySet().forEach(Objects.requireNonNull(consumer, "action"));
    }
}

