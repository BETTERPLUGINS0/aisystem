package ac.grim.grimac.shaded.maps.weak;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ConverterMaybe {
   protected final Object o;

   ConverterMaybe(Object o) {
      this.o = o;
   }

   private <T> Optional<T> optional(Function<Converter, T> fn) {
      try {
         return Optional.ofNullable(fn.apply(Converter.convert(this.o)));
      } catch (RuntimeException var3) {
         return Optional.empty();
      }
   }

   public Optional<String> intoString() {
      return this.optional(Converter::intoString);
   }

   public Optional<Integer> intoInteger() {
      return this.optional(Converter::intoInteger);
   }

   public Optional<Long> intoLong() {
      return this.optional(Converter::intoLong);
   }

   public Optional<Double> intoDouble() {
      return this.optional(Converter::intoDouble);
   }

   public Optional<BigDecimal> intoDecimal() {
      return this.optional(Converter::intoDecimal);
   }

   public Optional<Map> intoMap() {
      return this.optional(Converter::intoMap);
   }

   public Optional<List> intoList() {
      return this.optional(Converter::intoList);
   }

   public Optional<LocalDateTime> intoLocalDateTime() {
      return this.optional(Converter::intoLocalDateTime);
   }

   public Optional<ZonedDateTime> intoZonedDateTime() {
      return this.optional(Converter::intoZonedDateTime);
   }

   public Optional<ZonedDateTime> intoZonedDateTimeOrUse(ZoneId fallback) {
      return this.optional((c) -> {
         return c.intoZonedDateTimeOrUse(fallback);
      });
   }
}
