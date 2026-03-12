package ac.grim.grimac.shaded.kyori.examination;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.function.IntFunction;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public abstract class AbstractExaminer<R> implements Examiner<R> {
   @NotNull
   public R examine(@Nullable final Object value) {
      if (value == null) {
         return this.nil();
      } else if (value instanceof String) {
         return this.examine((String)((String)value));
      } else if (value instanceof Examinable) {
         return this.examine((Examinable)((Examinable)value));
      } else if (value instanceof Collection) {
         return this.collection((Collection)value);
      } else if (value instanceof Map) {
         return this.map((Map)value);
      } else if (value.getClass().isArray()) {
         Class<?> type = value.getClass().getComponentType();
         if (type.isPrimitive()) {
            if (type == Boolean.TYPE) {
               return this.examine((boolean[])value);
            }

            if (type == Byte.TYPE) {
               return this.examine((byte[])value);
            }

            if (type == Character.TYPE) {
               return this.examine((char[])value);
            }

            if (type == Double.TYPE) {
               return this.examine((double[])value);
            }

            if (type == Float.TYPE) {
               return this.examine((float[])value);
            }

            if (type == Integer.TYPE) {
               return this.examine((int[])value);
            }

            if (type == Long.TYPE) {
               return this.examine((long[])value);
            }

            if (type == Short.TYPE) {
               return this.examine((short[])value);
            }
         }

         return this.array((Object[])value);
      } else if (value instanceof Boolean) {
         return this.examine((Boolean)value);
      } else if (value instanceof Character) {
         return this.examine((Character)value);
      } else {
         if (value instanceof Number) {
            if (value instanceof Byte) {
               return this.examine((Byte)value);
            }

            if (value instanceof Double) {
               return this.examine((Double)value);
            }

            if (value instanceof Float) {
               return this.examine((Float)value);
            }

            if (value instanceof Integer) {
               return this.examine((Integer)value);
            }

            if (value instanceof Long) {
               return this.examine((Long)value);
            }

            if (value instanceof Short) {
               return this.examine((Short)value);
            }
         } else if (value instanceof BaseStream) {
            if (value instanceof Stream) {
               return this.stream((Stream)value);
            }

            if (value instanceof DoubleStream) {
               return this.stream((DoubleStream)value);
            }

            if (value instanceof IntStream) {
               return this.stream((IntStream)value);
            }

            if (value instanceof LongStream) {
               return this.stream((LongStream)value);
            }
         }

         return this.scalar(value);
      }
   }

   @NotNull
   private <E> R array(@NotNull final E[] array) {
      return this.array(array, Arrays.stream(array).map(this::examine));
   }

   @NotNull
   protected abstract <E> R array(@NotNull final E[] array, @NotNull final Stream<R> elements);

   @NotNull
   private <E> R collection(@NotNull final Collection<E> collection) {
      return this.collection(collection, collection.stream().map(this::examine));
   }

   @NotNull
   protected abstract <E> R collection(@NotNull final Collection<E> collection, @NotNull final Stream<R> elements);

   @NotNull
   public R examine(@NotNull final String name, @NotNull final Stream<? extends ExaminableProperty> properties) {
      return this.examinable(name, properties.map((property) -> {
         return new SimpleImmutableEntry(property.name(), property.examine(this));
      }));
   }

   @NotNull
   protected abstract R examinable(@NotNull final String name, @NotNull final Stream<Entry<String, R>> properties);

   @NotNull
   private <K, V> R map(@NotNull final Map<K, V> map) {
      return this.map(map, map.entrySet().stream().map((entry) -> {
         return new SimpleImmutableEntry(this.examine(entry.getKey()), this.examine(entry.getValue()));
      }));
   }

   @NotNull
   protected abstract <K, V> R map(@NotNull final Map<K, V> map, @NotNull final Stream<Entry<R, R>> entries);

   @NotNull
   protected abstract R nil();

   @NotNull
   protected abstract R scalar(@NotNull final Object value);

   @NotNull
   protected abstract <T> R stream(@NotNull final Stream<T> stream);

   @NotNull
   protected abstract R stream(@NotNull final DoubleStream stream);

   @NotNull
   protected abstract R stream(@NotNull final IntStream stream);

   @NotNull
   protected abstract R stream(@NotNull final LongStream stream);

   @NotNull
   public R examine(@Nullable final boolean[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   public R examine(@Nullable final byte[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   public R examine(@Nullable final char[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   public R examine(@Nullable final double[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   public R examine(@Nullable final float[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   public R examine(@Nullable final int[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   public R examine(@Nullable final long[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   public R examine(@Nullable final short[] values) {
      return values == null ? this.nil() : this.array(values.length, (index) -> {
         return this.examine(values[index]);
      });
   }

   @NotNull
   protected abstract R array(final int length, final IntFunction<R> value);
}
