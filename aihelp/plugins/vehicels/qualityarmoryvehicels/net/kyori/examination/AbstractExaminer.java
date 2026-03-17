/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.examination;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.Examiner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractExaminer<R>
implements Examiner<R> {
    @Override
    @NotNull
    public R examine(@Nullable Object object) {
        if (object == null) {
            return this.nil();
        }
        if (object instanceof String) {
            return this.examine((String)object);
        }
        if (object instanceof Examinable) {
            return this.examine((Examinable)object);
        }
        if (object instanceof Collection) {
            return this.collection((Collection)object);
        }
        if (object instanceof Map) {
            return this.map((Map)object);
        }
        if (object.getClass().isArray()) {
            Class<?> clazz = object.getClass().getComponentType();
            if (clazz.isPrimitive()) {
                if (clazz == Boolean.TYPE) {
                    return this.examine((boolean[])object);
                }
                if (clazz == Byte.TYPE) {
                    return this.examine((byte[])object);
                }
                if (clazz == Character.TYPE) {
                    return this.examine((char[])object);
                }
                if (clazz == Double.TYPE) {
                    return this.examine((double[])object);
                }
                if (clazz == Float.TYPE) {
                    return this.examine((float[])object);
                }
                if (clazz == Integer.TYPE) {
                    return this.examine((int[])object);
                }
                if (clazz == Long.TYPE) {
                    return this.examine((long[])object);
                }
                if (clazz == Short.TYPE) {
                    return this.examine((short[])object);
                }
            }
            return this.array((Object[])object);
        }
        if (object instanceof Boolean) {
            return this.examine((Boolean)object);
        }
        if (object instanceof Character) {
            return this.examine(((Character)object).charValue());
        }
        if (object instanceof Number) {
            if (object instanceof Byte) {
                return this.examine((Byte)object);
            }
            if (object instanceof Double) {
                return this.examine((Double)object);
            }
            if (object instanceof Float) {
                return this.examine(((Float)object).floatValue());
            }
            if (object instanceof Integer) {
                return this.examine((Integer)object);
            }
            if (object instanceof Long) {
                return this.examine((Long)object);
            }
            if (object instanceof Short) {
                return this.examine((Short)object);
            }
        } else if (object instanceof BaseStream) {
            if (object instanceof Stream) {
                return this.stream((Stream)object);
            }
            if (object instanceof DoubleStream) {
                return this.stream((DoubleStream)object);
            }
            if (object instanceof IntStream) {
                return this.stream((IntStream)object);
            }
            if (object instanceof LongStream) {
                return this.stream((LongStream)object);
            }
        }
        return this.scalar(object);
    }

    @NotNull
    private <E> R array(E @NotNull [] EArray) {
        return (R)this.array(EArray, Arrays.stream(EArray).map(this::examine));
    }

    @NotNull
    protected abstract <E> R array(E @NotNull [] var1, @NotNull Stream<R> var2);

    @NotNull
    private <E> R collection(@NotNull Collection<E> collection) {
        return (R)this.collection(collection, collection.stream().map(this::examine));
    }

    @NotNull
    protected abstract <E> R collection(@NotNull Collection<E> var1, @NotNull Stream<R> var2);

    @Override
    @NotNull
    public R examine(@NotNull String string, @NotNull Stream<? extends ExaminableProperty> stream) {
        return this.examinable(string, stream.map((? super T examinableProperty) -> new AbstractMap.SimpleImmutableEntry(examinableProperty.name(), examinableProperty.examine(this))));
    }

    @NotNull
    protected abstract R examinable(@NotNull String var1, @NotNull Stream<Map.Entry<String, R>> var2);

    @NotNull
    private <K, V> R map(@NotNull Map<K, V> map) {
        return this.map(map, map.entrySet().stream().map((? super T entry) -> new AbstractMap.SimpleImmutableEntry<R, R>(this.examine(entry.getKey()), this.examine(entry.getValue()))));
    }

    @NotNull
    protected abstract <K, V> R map(@NotNull Map<K, V> var1, @NotNull Stream<Map.Entry<R, R>> var2);

    @NotNull
    protected abstract R nil();

    @NotNull
    protected abstract R scalar(@NotNull Object var1);

    @NotNull
    protected abstract <T> R stream(@NotNull Stream<T> var1);

    @NotNull
    protected abstract R stream(@NotNull DoubleStream var1);

    @NotNull
    protected abstract R stream(@NotNull IntStream var1);

    @NotNull
    protected abstract R stream(@NotNull LongStream var1);

    @Override
    @NotNull
    public R examine(boolean @Nullable [] blArray) {
        if (blArray == null) {
            return this.nil();
        }
        return (R)this.array(blArray.length, (int n) -> this.examine(blArray[n]));
    }

    @Override
    @NotNull
    public R examine(byte @Nullable [] byArray) {
        if (byArray == null) {
            return this.nil();
        }
        return (R)this.array(byArray.length, (int n) -> this.examine(byArray[n]));
    }

    @Override
    @NotNull
    public R examine(char @Nullable [] cArray) {
        if (cArray == null) {
            return this.nil();
        }
        return (R)this.array(cArray.length, (int n) -> this.examine(cArray[n]));
    }

    @Override
    @NotNull
    public R examine(double @Nullable [] dArray) {
        if (dArray == null) {
            return this.nil();
        }
        return (R)this.array(dArray.length, (int n) -> this.examine(dArray[n]));
    }

    @Override
    @NotNull
    public R examine(float @Nullable [] fArray) {
        if (fArray == null) {
            return this.nil();
        }
        return (R)this.array(fArray.length, (int n) -> this.examine(fArray[n]));
    }

    @Override
    @NotNull
    public R examine(int @Nullable [] nArray) {
        if (nArray == null) {
            return this.nil();
        }
        return (R)this.array(nArray.length, (int n) -> this.examine(nArray[n]));
    }

    @Override
    @NotNull
    public R examine(long @Nullable [] lArray) {
        if (lArray == null) {
            return this.nil();
        }
        return (R)this.array(lArray.length, (int n) -> this.examine(lArray[n]));
    }

    @Override
    @NotNull
    public R examine(short @Nullable [] sArray) {
        if (sArray == null) {
            return this.nil();
        }
        return (R)this.array(sArray.length, (int n) -> this.examine(sArray[n]));
    }

    @NotNull
    protected abstract R array(int var1, IntFunction<R> var2);
}

