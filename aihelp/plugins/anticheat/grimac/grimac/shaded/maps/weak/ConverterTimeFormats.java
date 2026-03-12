package ac.grim.grimac.shaded.maps.weak;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.function.Function;

public class ConverterTimeFormats {
   public static DateTimeFormatter ISO_PERMISSIVE;
   public static DateTimeFormatter ISO_PERMISSIVE_WITH_DEFAULTS;
   public static DateTimeFormatter ISO_LONESOME_YEAR;
   public static DateTimeFormatter ISO_LONESOME_YEAR_WITH_DEFAULTS;
   public static DateTimeFormatter DAY_MONTH_YEAR_PERMISSIVE_DASH;
   public static DateTimeFormatter DAY_MONTH_YEAR_PERMISSIVE_DASH_WITH_DEFAULTS;
   public static DateTimeFormatter DAY_MONTH_YEAR_PERMISSIVE_SLASH;
   public static DateTimeFormatter DAY_MONTH_YEAR_PERMISSIVE_SLASH_WITH_DEFAULTS;
   public static DateTimeFormatter UTIL_DATE_TO_STRING;
   public static DateTimeFormatter UTIL_DATE_WITHOUT_ZONE_TO_STRING;
   static final Function<CharSequence, TemporalAccessor> EPOCH_MILLIS_PARSER;
   public static final Function<CharSequence, TemporalAccessor> ALL_PARSER;
   public static final Function<CharSequence, TemporalAccessor> ALL_PARSER_WITH_DEFAULTS;

   @SafeVarargs
   public static Function<CharSequence, TemporalAccessor> orderedParseAttempter(Function<CharSequence, TemporalAccessor>... parsers) {
      return (date) -> {
         RuntimeException first = null;
         Function[] var3 = parsers;
         int var4 = parsers.length;
         int var5 = 0;

         while(var5 < var4) {
            Function parser = var3[var5];

            try {
               return (TemporalAccessor)parser.apply(date);
            } catch (RuntimeException var8) {
               if (first == null) {
                  first = var8;
               }

               ++var5;
            }
         }

         if (first == null) {
            throw new IllegalStateException("Empty parse attempter");
         } else {
            throw first;
         }
      };
   }

   public static TemporalAccessor parse(CharSequence dateChars) {
      return (TemporalAccessor)ALL_PARSER.apply(dateChars);
   }

   public static TemporalAccessor parseWithDefaults(CharSequence dateChars) {
      return (TemporalAccessor)ALL_PARSER_WITH_DEFAULTS.apply(dateChars);
   }

   private ConverterTimeFormats() {
   }

   static {
      ISO_PERMISSIVE = (new DateTimeFormatterBuilder()).parseLenient().parseCaseInsensitive().appendValue(ChronoField.YEAR, 1, 10, SignStyle.EXCEEDS_PAD).appendLiteral('-').appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE).optionalStart().appendLiteral('-').appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE).optionalStart().appendLiteral('T').optionalEnd().optionalStart().appendLiteral(' ').optionalEnd().optionalStart().appendValue(ChronoField.HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE).optionalStart().appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 1, 2, SignStyle.NOT_NEGATIVE).optionalEnd().optionalStart().appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 1, 2, SignStyle.NOT_NEGATIVE).optionalEnd().optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).parseDefaulting(ChronoField.NANO_OF_SECOND, 0L).optionalEnd().optionalStart().appendZoneOrOffsetId().optionalEnd().optionalStart().appendOffset("+HHmm", "+0000").optionalEnd().optionalStart().appendLiteral('[').parseCaseSensitive().appendZoneRegionId().appendLiteral(']').toFormatter();
      ISO_PERMISSIVE_WITH_DEFAULTS = (new DateTimeFormatterBuilder()).append(ISO_PERMISSIVE).parseDefaulting(ChronoField.DAY_OF_MONTH, 1L).parseDefaulting(ChronoField.HOUR_OF_DAY, 0L).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0L).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0L).parseDefaulting(ChronoField.NANO_OF_SECOND, 0L).toFormatter();
      ISO_LONESOME_YEAR = (new DateTimeFormatterBuilder()).parseStrict().appendValue(ChronoField.YEAR, 1, 4, SignStyle.NORMAL).toFormatter();
      ISO_LONESOME_YEAR_WITH_DEFAULTS = (new DateTimeFormatterBuilder()).append(ISO_LONESOME_YEAR).parseDefaulting(ChronoField.MONTH_OF_YEAR, 1L).parseDefaulting(ChronoField.DAY_OF_MONTH, 1L).parseDefaulting(ChronoField.HOUR_OF_DAY, 0L).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0L).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0L).parseDefaulting(ChronoField.NANO_OF_SECOND, 0L).toFormatter();
      DAY_MONTH_YEAR_PERMISSIVE_DASH = (new DateTimeFormatterBuilder()).parseCaseInsensitive().parseLenient().appendPattern("dd").appendLiteral('-').appendPattern("MMM").appendLiteral('-').appendValue(ChronoField.YEAR, 1, 10, SignStyle.EXCEEDS_PAD).optionalStart().appendLiteral(' ').appendValue(ChronoField.HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE).optionalStart().appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 1, 2, SignStyle.NOT_NEGATIVE).optionalStart().appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 1, 2, SignStyle.NOT_NEGATIVE).optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalStart().appendOffsetId().toFormatter();
      DAY_MONTH_YEAR_PERMISSIVE_DASH_WITH_DEFAULTS = (new DateTimeFormatterBuilder()).append(DAY_MONTH_YEAR_PERMISSIVE_DASH).parseDefaulting(ChronoField.HOUR_OF_DAY, 0L).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0L).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0L).parseDefaulting(ChronoField.NANO_OF_SECOND, 0L).toFormatter();
      DAY_MONTH_YEAR_PERMISSIVE_SLASH = (new DateTimeFormatterBuilder()).parseCaseInsensitive().parseLenient().appendPattern("dd").appendLiteral('/').appendPattern("MMM").appendLiteral('/').appendValue(ChronoField.YEAR, 1, 10, SignStyle.EXCEEDS_PAD).optionalStart().appendLiteral(' ').appendValue(ChronoField.HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE).optionalStart().appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 1, 2, SignStyle.NOT_NEGATIVE).optionalStart().appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 1, 2, SignStyle.NOT_NEGATIVE).optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalStart().appendOffsetId().toFormatter();
      DAY_MONTH_YEAR_PERMISSIVE_SLASH_WITH_DEFAULTS = (new DateTimeFormatterBuilder()).append(DAY_MONTH_YEAR_PERMISSIVE_SLASH).parseDefaulting(ChronoField.HOUR_OF_DAY, 0L).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0L).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0L).parseDefaulting(ChronoField.NANO_OF_SECOND, 0L).toFormatter();
      UTIL_DATE_TO_STRING = (new DateTimeFormatterBuilder()).parseLenient().appendPattern("EEE").appendLiteral(' ').appendPattern("MMM").appendLiteral(' ').appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE).appendLiteral(' ').appendValue(ChronoField.HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE).appendLiteral(':').appendPattern("mm").optionalStart().appendLiteral(':').appendPattern("ss").optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalEnd().optionalEnd().appendLiteral(' ').appendPattern("zzz").appendLiteral(' ').appendPattern("yyyy").toFormatter();
      UTIL_DATE_WITHOUT_ZONE_TO_STRING = (new DateTimeFormatterBuilder()).parseLenient().appendPattern("EEE").appendLiteral(' ').appendPattern("MMM").appendLiteral(' ').appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE).appendLiteral(' ').appendValue(ChronoField.HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE).appendLiteral(':').appendPattern("mm").optionalStart().appendLiteral(':').appendPattern("ss").optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalEnd().optionalEnd().appendLiteral(' ').appendPattern("yyyy").toFormatter();
      EPOCH_MILLIS_PARSER = (s) -> {
         Long millis = Converter.convert(s).intoLong();
         if (millis > -10000L && millis < 10000L) {
            throw new IllegalArgumentException("Small value '" + s + "' indicates it is not valid epoch millis");
         } else {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(Converter.convert(s).intoLong()), ZoneId.systemDefault());
         }
      };
      Function[] var10000 = new Function[7];
      DateTimeFormatter var10003 = ISO_PERMISSIVE;
      var10000[0] = var10003::parse;
      var10003 = DAY_MONTH_YEAR_PERMISSIVE_DASH;
      var10000[1] = var10003::parse;
      var10003 = DAY_MONTH_YEAR_PERMISSIVE_SLASH;
      var10000[2] = var10003::parse;
      var10003 = UTIL_DATE_TO_STRING;
      var10000[3] = var10003::parse;
      var10003 = UTIL_DATE_WITHOUT_ZONE_TO_STRING;
      var10000[4] = var10003::parse;
      var10003 = ISO_LONESOME_YEAR;
      var10000[5] = var10003::parse;
      var10000[6] = EPOCH_MILLIS_PARSER;
      ALL_PARSER = orderedParseAttempter(var10000);
      var10000 = new Function[7];
      var10003 = ISO_PERMISSIVE_WITH_DEFAULTS;
      var10000[0] = var10003::parse;
      var10003 = DAY_MONTH_YEAR_PERMISSIVE_DASH_WITH_DEFAULTS;
      var10000[1] = var10003::parse;
      var10003 = DAY_MONTH_YEAR_PERMISSIVE_SLASH_WITH_DEFAULTS;
      var10000[2] = var10003::parse;
      var10003 = UTIL_DATE_TO_STRING;
      var10000[3] = var10003::parse;
      var10003 = UTIL_DATE_WITHOUT_ZONE_TO_STRING;
      var10000[4] = var10003::parse;
      var10003 = ISO_LONESOME_YEAR_WITH_DEFAULTS;
      var10000[5] = var10003::parse;
      var10000[6] = EPOCH_MILLIS_PARSER;
      ALL_PARSER_WITH_DEFAULTS = orderedParseAttempter(var10000);
   }
}
