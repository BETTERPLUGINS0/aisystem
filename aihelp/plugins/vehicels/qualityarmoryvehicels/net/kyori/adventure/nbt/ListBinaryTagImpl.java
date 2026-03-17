/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Debug$Renderer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 *  org.jetbrains.annotations.Range
 */
package net.kyori.adventure.nbt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.kyori.adventure.nbt.AbstractBinaryTag;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

@Debug.Renderer(text="\"ListBinaryTag[type=\" + this.type.toString() + \"]\"", childrenArray="this.tags.toArray()", hasChildren="!this.tags.isEmpty()")
final class ListBinaryTagImpl
extends AbstractBinaryTag
implements ListBinaryTag {
    static final ListBinaryTag EMPTY = new ListBinaryTagImpl(BinaryTagTypes.END, Collections.emptyList());
    private final List<BinaryTag> tags;
    private final BinaryTagType<? extends BinaryTag> elementType;
    private final int hashCode;

    ListBinaryTagImpl(BinaryTagType<? extends BinaryTag> binaryTagType, List<BinaryTag> list) {
        this.tags = Collections.unmodifiableList(list);
        this.elementType = binaryTagType;
        this.hashCode = list.hashCode();
    }

    @Override
    @NotNull
    public BinaryTagType<? extends BinaryTag> elementType() {
        return this.elementType;
    }

    @Override
    public int size() {
        return this.tags.size();
    }

    @Override
    @NotNull
    public BinaryTag get(@Range(from=0L, to=0x7FFFFFFFL) int n) {
        return this.tags.get(n);
    }

    @Override
    @NotNull
    public ListBinaryTag set(int n, @NotNull BinaryTag binaryTag, @Nullable Consumer<? super BinaryTag> consumer) {
        return this.edit(list -> {
            BinaryTag binaryTag2 = list.set(n, binaryTag);
            if (consumer != null) {
                consumer.accept(binaryTag2);
            }
        }, binaryTag.type());
    }

    @Override
    @NotNull
    public ListBinaryTag remove(int n, @Nullable Consumer<? super BinaryTag> consumer) {
        return this.edit(list -> {
            BinaryTag binaryTag = (BinaryTag)list.remove(n);
            if (consumer != null) {
                consumer.accept(binaryTag);
            }
        }, null);
    }

    @Override
    @NotNull
    public ListBinaryTag add(BinaryTag binaryTag) {
        ListBinaryTagImpl.noAddEnd(binaryTag);
        if (this.elementType != BinaryTagTypes.END) {
            ListBinaryTagImpl.mustBeSameType(binaryTag, this.elementType);
        }
        return this.edit(list -> list.add(binaryTag), binaryTag.type());
    }

    @Override
    @NotNull
    public ListBinaryTag add(Iterable<? extends BinaryTag> iterable) {
        if (iterable instanceof Collection && ((Collection)iterable).isEmpty()) {
            return this;
        }
        BinaryTagType<?> binaryTagType = ListBinaryTagImpl.mustBeSameType(iterable);
        return this.edit(list -> {
            for (BinaryTag binaryTag : iterable) {
                list.add(binaryTag);
            }
        }, binaryTagType);
    }

    static void noAddEnd(BinaryTag binaryTag) {
        if (binaryTag.type() == BinaryTagTypes.END) {
            throw new IllegalArgumentException(String.format("Cannot add a %s to a %s", BinaryTagTypes.END, BinaryTagTypes.LIST));
        }
    }

    static BinaryTagType<?> mustBeSameType(Iterable<? extends BinaryTag> iterable) {
        BinaryTagType<? extends BinaryTag> binaryTagType = null;
        for (BinaryTag binaryTag : iterable) {
            if (binaryTagType == null) {
                binaryTagType = binaryTag.type();
                continue;
            }
            ListBinaryTagImpl.mustBeSameType(binaryTag, binaryTagType);
        }
        return binaryTagType;
    }

    static void mustBeSameType(BinaryTag binaryTag, BinaryTagType<? extends BinaryTag> binaryTagType) {
        if (binaryTag.type() != binaryTagType) {
            throw new IllegalArgumentException(String.format("Trying to add tag of type %s to list of %s", binaryTag.type(), binaryTagType));
        }
    }

    private ListBinaryTag edit(Consumer<List<BinaryTag>> consumer, @Nullable BinaryTagType<? extends BinaryTag> binaryTagType) {
        ArrayList<BinaryTag> arrayList = new ArrayList<BinaryTag>(this.tags);
        consumer.accept(arrayList);
        BinaryTagType<? extends BinaryTag> binaryTagType2 = this.elementType;
        if (binaryTagType != null && binaryTagType2 == BinaryTagTypes.END) {
            binaryTagType2 = binaryTagType;
        }
        return new ListBinaryTagImpl(binaryTagType2, new ArrayList<BinaryTag>(arrayList));
    }

    @Override
    @NotNull
    public Stream<BinaryTag> stream() {
        return this.tags.stream();
    }

    @Override
    public Iterator<BinaryTag> iterator() {
        final Iterator<BinaryTag> iterator = this.tags.iterator();
        return new Iterator<BinaryTag>(){

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public BinaryTag next() {
                return (BinaryTag)iterator.next();
            }

            @Override
            public void forEachRemaining(Consumer<? super BinaryTag> consumer) {
                iterator.forEachRemaining(consumer);
            }
        };
    }

    @Override
    public void forEach(Consumer<? super BinaryTag> consumer) {
        this.tags.forEach(consumer);
    }

    @Override
    public Spliterator<BinaryTag> spliterator() {
        return Spliterators.spliterator(this.tags, 1040);
    }

    public boolean equals(Object object) {
        return this == object || object instanceof ListBinaryTagImpl && this.tags.equals(((ListBinaryTagImpl)object).tags);
    }

    public int hashCode() {
        return this.hashCode;
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("tags", this.tags), ExaminableProperty.of("type", this.elementType));
    }
}

