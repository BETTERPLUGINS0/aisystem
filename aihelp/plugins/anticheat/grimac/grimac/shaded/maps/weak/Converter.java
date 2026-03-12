package ac.grim.grimac.shaded.maps.weak;

import ac.grim.grimac.shaded.maps.Fluent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Converter {
   private static final String DEFAULT_MAP_KEY = "value";
   private static final Map<Class<?>, Function<Object, ? extends Converter>> typeConverters = Collections.unmodifiableMap((new Fluent.LinkedHashMap()).append(Integer.class, Converter.IntConverter::new).append(Long.class, Converter.LongConverter::new).append(Double.class, Converter.DoubleConverter::new).append(BigDecimal.class, Converter.DecimalConverter::new).append(Weak.class, (o) -> {
      return convert(((Weak)o).asObject());
   }).append(OptionalWeak.class, (o) -> {
      return new Converter.OptionalConverter(((OptionalWeak)o).asObject());
   }).append(Map.class, Converter.MapConverter::new).append(Iterable.class, Converter.IterableConverter::new).append(Optional.class, Converter.OptionalConverter::new).append(Date.class, Converter.UtilDateInstantConverter::new).append(Object.class, Converter::new));
   protected final Object o;

   public static Converter convert(Object value) {
      Objects.requireNonNull(value);
      return value instanceof Object[] ? convert(Arrays.asList((Object[])((Object[])value))) : (Converter)((Function)typeConverters.getOrDefault(value.getClass(), typeConverters.entrySet().stream().filter((entry) -> {
         return !((Class)entry.getKey()).equals(Date.class) && ((Class)entry.getKey()).isInstance(value);
      }).findFirst().map(Entry::getValue).get())).apply(value);
   }

   private static boolean doesNotThrow(Supplier<?> method) {
      try {
         method.get();
         return true;
      } catch (RuntimeException var2) {
         return false;
      }
   }

   Converter(Object o) {
      this.o = o;
   }

   public String intoString() {
      return this.o instanceof String ? (String)this.o : this.o.toString();
   }

   public boolean intoStringWorks() {
      return doesNotThrow(this::intoString);
   }

   public int intoInteger() {
      return this.intoDecimal().setScale(0, RoundingMode.HALF_UP).intValueExact();
   }

   public boolean intoIntegerWorks() {
      return doesNotThrow(this::intoInteger);
   }

   public long intoLong() {
      return this.intoDecimal().setScale(0, RoundingMode.HALF_UP).longValueExact();
   }

   public boolean intoLongWorks() {
      return doesNotThrow(this::intoLong);
   }

   public double intoDouble() {
      return this.intoDecimal().doubleValue();
   }

   public boolean intoDoubleWorks() {
      return doesNotThrow(this::intoDouble);
   }

   public BigDecimal intoDecimal() {
      return new BigDecimal(this.intoString());
   }

   public boolean intoDecimalWorks() {
      return doesNotThrow(this::intoDecimal);
   }

   public Map intoMap() {
      return (new Fluent.HashMap()).append("value", this.o);
   }

   public boolean intoMapWorks() {
      return doesNotThrow(this::intoMap);
   }

   public List intoList() {
      List list = new ArrayList();
      list.add(this.o);
      return list;
   }

   public boolean intoListWorks() {
      return doesNotThrow(this::intoList);
   }

   public LocalDateTime intoLocalDateTime() {
      return LocalDateTime.from(ConverterTimeFormats.parseWithDefaults(this.intoString()));
   }

   public boolean intoLocalDateTimeWorks() {
      return doesNotThrow(this::intoLocalDateTime);
   }

   public ZonedDateTime intoZonedDateTime() {
      return ZonedDateTime.from(ConverterTimeFormats.parseWithDefaults(this.intoString()));
   }

   public boolean intoZonedDateTimeWorks() {
      return doesNotThrow(this::intoZonedDateTime);
   }

   public ZonedDateTime intoZonedDateTimeOrUse(ZoneId fallback) {
      try {
         return this.intoZonedDateTime();
      } catch (RuntimeException var3) {
         return this.intoLocalDateTime().atZone(fallback);
      }
   }

   public ConverterMaybe maybe() {
      return new ConverterMaybe(this.o);
   }

   static class UtilDateInstantConverter extends Converter.TypeConverter<Date> {
      UtilDateInstantConverter(Object o) {
         super(o);
      }

      public BigDecimal intoDecimal() {
         return new BigDecimal(this.intoLong());
      }

      public double intoDouble() {
         return (double)this.intoLong();
      }

      public long intoLong() {
         return ((Date)this.literal()).getTime();
      }

      public String intoString() {
         return String.valueOf(((Date)this.literal()).getTime());
      }
   }

   static class OptionalConverter extends Converter.TypeConverter<Optional<?>> {
      OptionalConverter(Object o) {
         super(o);
      }

      public String intoString() {
         return (String)((Optional)this.literal()).map((o) -> {
            return convert(o).intoString();
         }).orElseGet(() -> {
            return super.intoString();
         });
      }

      public int intoInteger() {
         return (Integer)((Optional)this.literal()).map((o) -> {
            return convert(o).intoInteger();
         }).orElseGet(() -> {
            return super.intoInteger();
         });
      }

      public long intoLong() {
         return (Long)((Optional)this.literal()).map((o) -> {
            return convert(o).intoLong();
         }).orElseGet(() -> {
            return super.intoLong();
         });
      }

      public double intoDouble() {
         return (Double)((Optional)this.literal()).map((o) -> {
            return convert(o).intoDouble();
         }).orElseGet(() -> {
            return super.intoDouble();
         });
      }

      public BigDecimal intoDecimal() {
         return (BigDecimal)((Optional)this.literal()).map((o) -> {
            return convert(o).intoDecimal();
         }).orElseGet(() -> {
            return super.intoDecimal();
         });
      }

      public Map intoMap() {
         return (Map)((Optional)this.literal()).map((o) -> {
            return convert(o).intoMap();
         }).orElseGet(LinkedHashMap::new);
      }

      public List intoList() {
         return (List)((Optional)this.literal()).map((o) -> {
            return convert(o).intoList();
         }).orElseGet(ArrayList::new);
      }
   }

   static class IterableConverter extends Converter.TypeConverter<Iterable<?>> {
      IterableConverter(Object o) {
         super(o);
      }

      private Optional<Object> onlyElement() {
         Iterator<?> iterator = ((Iterable)this.literal()).iterator();
         return Optional.ofNullable(iterator.hasNext() ? iterator.next() : null).filter((o) -> {
            return !iterator.hasNext();
         });
      }

      public String intoString() {
         return (String)this.onlyElement().map((o) -> {
            return convert(o).intoString();
         }).orElseGet(() -> {
            return super.intoString();
         });
      }

      public int intoInteger() {
         return (Integer)this.onlyElement().map((o) -> {
            return convert(o).intoInteger();
         }).orElseGet(() -> {
            return super.intoInteger();
         });
      }

      public long intoLong() {
         return (Long)this.onlyElement().map((o) -> {
            return convert(o).intoLong();
         }).orElseGet(() -> {
            return super.intoLong();
         });
      }

      public double intoDouble() {
         return (Double)this.onlyElement().map((o) -> {
            return convert(o).intoDouble();
         }).orElseGet(() -> {
            return super.intoDouble();
         });
      }

      public BigDecimal intoDecimal() {
         return (BigDecimal)this.onlyElement().map((o) -> {
            return convert(o).intoDecimal();
         }).orElseGet(() -> {
            return super.intoDecimal();
         });
      }

      public Map intoMap() {
         Map<Integer, Object> map = new LinkedHashMap();
         Iterator<?> iterator = ((Iterable)this.literal()).iterator();

         for(int i = 0; iterator.hasNext(); ++i) {
            map.put(i, iterator.next());
         }

         return map;
      }

      public List intoList() {
         return (List)StreamSupport.stream(((Iterable)this.literal()).spliterator(), false).collect(Collectors.toCollection(ArrayList::new));
      }
   }

   static class MapConverter extends Converter.TypeConverter<Map<?, ?>> {
      MapConverter(Object o) {
         super(o);
      }

      private Optional<Object> value() {
         return Optional.ofNullable(((Map)this.literal()).get("value"));
      }

      public String intoString() {
         return (String)this.value().map((o) -> {
            return convert(o).intoString();
         }).orElseGet(() -> {
            return super.intoString();
         });
      }

      public int intoInteger() {
         return (Integer)this.value().map((o) -> {
            return convert(o).intoInteger();
         }).orElseGet(() -> {
            return super.intoInteger();
         });
      }

      public long intoLong() {
         return (Long)this.value().map((o) -> {
            return convert(o).intoLong();
         }).orElseGet(() -> {
            return super.intoLong();
         });
      }

      public double intoDouble() {
         return (Double)this.value().map((o) -> {
            return convert(o).intoDouble();
         }).orElseGet(() -> {
            return super.intoDouble();
         });
      }

      public BigDecimal intoDecimal() {
         return (BigDecimal)this.value().map((o) -> {
            return convert(o).intoDecimal();
         }).orElseGet(() -> {
            return super.intoDecimal();
         });
      }

      public Map intoMap() {
         return new LinkedHashMap((Map)this.literal());
      }

      public List intoList() {
         return new ArrayList(((Map)this.literal()).values());
      }
   }

   static class DecimalConverter extends Converter.TypeConverter<BigDecimal> {
      DecimalConverter(Object o) {
         super(o);
      }

      public BigDecimal intoDecimal() {
         return (BigDecimal)this.literal();
      }
   }

   static class DoubleConverter extends Converter.TypeConverter<Double> {
      DoubleConverter(Object o) {
         super(o);
      }

      public double intoDouble() {
         return (Double)this.literal();
      }

      public BigDecimal intoDecimal() {
         return new BigDecimal((Double)this.literal());
      }
   }

   static class LongConverter extends Converter.TypeConverter<Long> {
      LongConverter(Object o) {
         super(o);
      }

      public int intoInteger() {
         if ((Long)this.literal() >= -2147483648L && (Long)this.literal() <= 2147483647L) {
            return ((Long)this.literal()).intValue();
         } else {
            throw new IllegalArgumentException(this.literal() + " too large/small to be cast to int");
         }
      }

      public long intoLong() {
         return (Long)this.literal();
      }

      public double intoDouble() {
         return (double)(Long)this.literal();
      }

      public BigDecimal intoDecimal() {
         return new BigDecimal((Long)this.literal());
      }
   }

   static class IntConverter extends Converter.TypeConverter<Integer> {
      IntConverter(Object o) {
         super(o);
      }

      public int intoInteger() {
         return (Integer)this.literal();
      }

      public long intoLong() {
         return (long)(Integer)this.literal();
      }

      public double intoDouble() {
         return (double)(Integer)this.literal();
      }

      public BigDecimal intoDecimal() {
         return new BigDecimal((Integer)this.literal());
      }
   }

   abstract static class TypeConverter<T> extends Converter {
      TypeConverter(Object o) {
         super(o);
      }

      protected T literal() {
         return this.o;
      }
   }
}
