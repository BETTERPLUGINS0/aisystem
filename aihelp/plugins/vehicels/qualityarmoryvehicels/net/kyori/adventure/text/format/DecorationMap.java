/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Unmodifiable
 */
package net.kyori.adventure.text.format;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterators;
import java.util.stream.Stream;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

@Unmodifiable
final class DecorationMap
extends AbstractMap<TextDecoration, TextDecoration.State>
implements Examinable {
    static final TextDecoration[] DECORATIONS = TextDecoration.values();
    private static final TextDecoration.State[] STATES = TextDecoration.State.values();
    private static final int MAP_SIZE = DECORATIONS.length;
    private static final TextDecoration.State[] EMPTY_STATE_ARRAY = new TextDecoration.State[0];
    static final DecorationMap EMPTY = new DecorationMap(0);
    private static final KeySet KEY_SET = new KeySet();
    private final int bitSet;
    private volatile EntrySet entrySet = null;
    private volatile Values values = null;

    static DecorationMap fromMap(Map<TextDecoration, TextDecoration.State> map) {
        if (map instanceof DecorationMap) {
            return (DecorationMap)map;
        }
        int n = 0;
        for (TextDecoration textDecoration : DECORATIONS) {
            n |= map.getOrDefault(textDecoration, TextDecoration.State.NOT_SET).ordinal() * DecorationMap.offset(textDecoration);
        }
        return DecorationMap.withBitSet(n);
    }

    static DecorationMap merge(Map<TextDecoration, TextDecoration.State> map, Map<TextDecoration, TextDecoration.State> map2) {
        int n = 0;
        for (TextDecoration textDecoration : DECORATIONS) {
            n |= map.getOrDefault(textDecoration, map2.getOrDefault(textDecoration, TextDecoration.State.NOT_SET)).ordinal() * DecorationMap.offset(textDecoration);
        }
        return DecorationMap.withBitSet(n);
    }

    private static DecorationMap withBitSet(int n) {
        return n == 0 ? EMPTY : new DecorationMap(n);
    }

    private static int offset(TextDecoration textDecoration) {
        return 1 << textDecoration.ordinal() * 2;
    }

    private DecorationMap(int n) {
        this.bitSet = n;
    }

    @NotNull
    public DecorationMap with(@NotNull TextDecoration textDecoration, @NotNull TextDecoration.State state) {
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(textDecoration, "decoration");
        int n = DecorationMap.offset(textDecoration);
        return DecorationMap.withBitSet(this.bitSet & ~(3 * n) | state.ordinal() * n);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Arrays.stream(DECORATIONS).map(textDecoration -> ExaminableProperty.of(textDecoration.toString(), (Object)this.get(textDecoration)));
    }

    @Override
    public TextDecoration.State get(Object object) {
        if (object instanceof TextDecoration) {
            return STATES[this.bitSet >> ((TextDecoration)object).ordinal() * 2 & 3];
        }
        return null;
    }

    @Override
    public boolean containsKey(Object object) {
        return object instanceof TextDecoration;
    }

    @Override
    public int size() {
        return MAP_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    @NotNull
    public Set<Map.Entry<TextDecoration, TextDecoration.State>> entrySet() {
        if (this.entrySet == null) {
            DecorationMap decorationMap = this;
            synchronized (decorationMap) {
                if (this.entrySet == null) {
                    this.entrySet = new EntrySet();
                }
            }
        }
        return this.entrySet;
    }

    @Override
    @NotNull
    public Set<TextDecoration> keySet() {
        return KEY_SET;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    @NotNull
    public Collection<TextDecoration.State> values() {
        if (this.values == null) {
            DecorationMap decorationMap = this;
            synchronized (decorationMap) {
                if (this.values == null) {
                    this.values = new Values();
                }
            }
        }
        return this.values;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != DecorationMap.class) {
            return false;
        }
        return this.bitSet == ((DecorationMap)object).bitSet;
    }

    @Override
    public int hashCode() {
        return this.bitSet;
    }

    static /* synthetic */ KeySet access$000() {
        return KEY_SET;
    }

    static final class KeySet
    extends AbstractSet<TextDecoration> {
        KeySet() {
        }

        @Override
        public boolean contains(Object object) {
            return object instanceof TextDecoration;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Object @NotNull [] toArray() {
            return Arrays.copyOf(DECORATIONS, MAP_SIZE, Object[].class);
        }

        @Override
        public <T> T @NotNull [] toArray(T @NotNull [] TArray) {
            if (TArray.length < MAP_SIZE) {
                return Arrays.copyOf(DECORATIONS, MAP_SIZE, TArray.getClass());
            }
            System.arraycopy(DECORATIONS, 0, TArray, 0, MAP_SIZE);
            if (TArray.length > MAP_SIZE) {
                TArray[MAP_SIZE] = null;
            }
            return TArray;
        }

        @Override
        @NotNull
        public Iterator<TextDecoration> iterator() {
            return Spliterators.iterator(Arrays.spliterator(DECORATIONS));
        }

        @Override
        public int size() {
            return MAP_SIZE;
        }
    }

    final class Values
    extends AbstractCollection<TextDecoration.State> {
        Values() {
        }

        @Override
        @NotNull
        public Iterator<TextDecoration.State> iterator() {
            return Spliterators.iterator(Arrays.spliterator(this.toArray(EMPTY_STATE_ARRAY)));
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Object @NotNull [] toArray() {
            Object[] objectArray = new Object[MAP_SIZE];
            for (int i = 0; i < MAP_SIZE; ++i) {
                objectArray[i] = DecorationMap.this.get(DECORATIONS[i]);
            }
            return objectArray;
        }

        @Override
        public <T> T @NotNull [] toArray(T @NotNull [] TArray) {
            if (TArray.length < MAP_SIZE) {
                return Arrays.copyOf(this.toArray(), MAP_SIZE, TArray.getClass());
            }
            System.arraycopy(this.toArray(), 0, TArray, 0, MAP_SIZE);
            if (TArray.length > MAP_SIZE) {
                TArray[MAP_SIZE] = null;
            }
            return TArray;
        }

        @Override
        public boolean contains(Object object) {
            return object instanceof TextDecoration.State && super.contains(object);
        }

        @Override
        public int size() {
            return MAP_SIZE;
        }
    }

    final class EntrySet
    extends AbstractSet<Map.Entry<TextDecoration, TextDecoration.State>> {
        EntrySet() {
        }

        @Override
        @NotNull
        public Iterator<Map.Entry<TextDecoration, TextDecoration.State>> iterator() {
            return new Iterator<Map.Entry<TextDecoration, TextDecoration.State>>(){
                private final Iterator<TextDecoration> decorations = DecorationMap.access$000().iterator();
                private final Iterator<TextDecoration.State> states;
                {
                    this.states = DecorationMap.this.values().iterator();
                }

                @Override
                public boolean hasNext() {
                    return this.decorations.hasNext() && this.states.hasNext();
                }

                @Override
                public Map.Entry<TextDecoration, TextDecoration.State> next() {
                    if (this.hasNext()) {
                        return new AbstractMap.SimpleImmutableEntry<TextDecoration, TextDecoration.State>(this.decorations.next(), this.states.next());
                    }
                    throw new NoSuchElementException();
                }
            };
        }

        @Override
        public int size() {
            return MAP_SIZE;
        }
    }
}

