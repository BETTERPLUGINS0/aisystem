package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.core.JavaVersion;
import fr.xephi.authme.libs.org.postgresql.core.Provider;
import fr.xephi.authme.libs.org.postgresql.util.ByteConverter;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.chrono.IsoEra;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;

public class TimestampUtils {
   private static final int ONEDAY = 86400000;
   private static final char[] ZEROS = new char[]{'0', '0', '0', '0', '0', '0', '0', '0', '0'};
   private static final char[][] NUMBERS;
   private static final HashMap<String, TimeZone> GMT_ZONES = new HashMap();
   private static final int MAX_NANOS_BEFORE_WRAP_ON_ROUND = 999999500;
   private static final Duration ONE_MICROSECOND = Duration.ofNanos(1000L);
   private static final LocalTime MAX_TIME;
   private static final OffsetDateTime MAX_OFFSET_DATETIME;
   private static final LocalDateTime MAX_LOCAL_DATETIME;
   private static final LocalDate MIN_LOCAL_DATE;
   private static final LocalDateTime MIN_LOCAL_DATETIME;
   private static final OffsetDateTime MIN_OFFSET_DATETIME;
   private static final Duration PG_EPOCH_DIFF;
   @Nullable
   private static final Field DEFAULT_TIME_ZONE_FIELD;
   private static final TimeZone UTC_TIMEZONE;
   @Nullable
   private TimeZone prevDefaultZoneFieldValue;
   @Nullable
   private TimeZone defaultTimeZoneCache;
   private final StringBuilder sbuf = new StringBuilder();
   private final Calendar calendarWithUserTz = new GregorianCalendar();
   @Nullable
   private Calendar calCache;
   @Nullable
   private ZoneOffset calCacheZone;
   private final boolean usesDouble;
   private final Provider<TimeZone> timeZoneProvider;
   private final ResourceLock lock = new ResourceLock();

   public TimestampUtils(boolean usesDouble, Provider<TimeZone> timeZoneProvider) {
      this.usesDouble = usesDouble;
      this.timeZoneProvider = timeZoneProvider;
   }

   private Calendar getCalendar(ZoneOffset offset) {
      if (this.calCache != null && Objects.equals(offset, this.calCacheZone)) {
         return this.calCache;
      } else {
         String tzid = offset.getTotalSeconds() == 0 ? "UTC" : "GMT".concat(offset.getId());
         TimeZone syntheticTZ = new SimpleTimeZone(offset.getTotalSeconds() * 1000, tzid);
         this.calCache = new GregorianCalendar(syntheticTZ);
         this.calCacheZone = offset;
         return this.calCache;
      }
   }

   private TimestampUtils.ParsedTimestamp parseBackendTimestamp(String str) throws SQLException {
      char[] s = str.toCharArray();
      int slen = s.length;
      TimestampUtils.ParsedTimestamp result = new TimestampUtils.ParsedTimestamp();

      try {
         int start = skipWhitespace(s, 0);
         int end = firstNonDigit(s, start);
         char sep;
         if (charAt(s, end) == '-') {
            result.hasDate = true;
            result.year = number(s, start, end);
            start = end + 1;
            end = firstNonDigit(s, start);
            result.month = number(s, start, end);
            sep = charAt(s, end);
            if (sep != '-') {
               throw new NumberFormatException("Expected date to be dash-separated, got '" + sep + "'");
            }

            start = end + 1;
            end = firstNonDigit(s, start);
            result.day = number(s, start, end);
            start = skipWhitespace(s, end);
         }

         int numlength;
         if (Character.isDigit(charAt(s, start))) {
            result.hasTime = true;
            end = firstNonDigit(s, start);
            result.hour = number(s, start, end);
            sep = charAt(s, end);
            if (sep != ':') {
               throw new NumberFormatException("Expected time to be colon-separated, got '" + sep + "'");
            }

            start = end + 1;
            end = firstNonDigit(s, start);
            result.minute = number(s, start, end);
            sep = charAt(s, end);
            if (sep != ':') {
               throw new NumberFormatException("Expected time to be colon-separated, got '" + sep + "'");
            }

            start = end + 1;
            end = firstNonDigit(s, start);
            result.second = number(s, start, end);
            start = end;
            if (charAt(s, end) == '.') {
               end = firstNonDigit(s, end + 1);
               int num = number(s, start + 1, end);

               for(numlength = end - (start + 1); numlength < 9; ++numlength) {
                  num *= 10;
               }

               result.nanos = num;
               start = end;
            }

            start = skipWhitespace(s, start);
         }

         sep = charAt(s, start);
         if (sep == '-' || sep == '+') {
            result.hasOffset = true;
            numlength = sep == '-' ? -1 : 1;
            end = firstNonDigit(s, start + 1);
            int tzhr = number(s, start + 1, end);
            start = end;
            sep = charAt(s, end);
            int tzmin;
            if (sep == ':') {
               end = firstNonDigit(s, end + 1);
               tzmin = number(s, start + 1, end);
               start = end;
            } else {
               tzmin = 0;
            }

            int tzsec = 0;
            sep = charAt(s, start);
            if (sep == ':') {
               end = firstNonDigit(s, start + 1);
               tzsec = number(s, start + 1, end);
               start = end;
            }

            result.offset = ZoneOffset.ofHoursMinutesSeconds(numlength * tzhr, numlength * tzmin, numlength * tzsec);
            start = skipWhitespace(s, start);
         }

         if (result.hasDate && start < slen) {
            String eraString = new String(s, start, slen - start);
            if (eraString.startsWith("AD")) {
               result.era = 1;
               start += 2;
            } else if (eraString.startsWith("BC")) {
               result.era = 0;
               start += 2;
            }
         }

         if (start < slen) {
            throw new NumberFormatException("Trailing junk on timestamp: '" + new String(s, start, slen - start) + "'");
         } else if (!result.hasTime && !result.hasDate) {
            throw new NumberFormatException("Timestamp has neither date nor time");
         } else {
            return result;
         }
      } catch (NumberFormatException var13) {
         throw new PSQLException(GT.tr("Bad value for type timestamp/date/time: {0}", str), PSQLState.BAD_DATETIME_FORMAT, var13);
      }
   }

   @PolyNull
   public Timestamp toTimestamp(@Nullable Calendar cal, @PolyNull String s) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      Timestamp var8;
      label75: {
         Object var11;
         label76: {
            Timestamp var5;
            label77: {
               try {
                  if (s == null) {
                     var11 = null;
                     break label76;
                  }

                  int slen = s.length();
                  if (slen == 8 && "infinity".equals(s)) {
                     var5 = new Timestamp(9223372036825200000L);
                     break label77;
                  }

                  if (slen != 9 || !"-infinity".equals(s)) {
                     TimestampUtils.ParsedTimestamp ts = this.parseBackendTimestamp(s);
                     Calendar useCal = ts.hasOffset ? this.getCalendar(ts.offset) : this.setupCalendar(cal);
                     useCal.set(0, ts.era);
                     useCal.set(1, ts.year);
                     useCal.set(2, ts.month - 1);
                     useCal.set(5, ts.day);
                     useCal.set(11, ts.hour);
                     useCal.set(12, ts.minute);
                     useCal.set(13, ts.second);
                     useCal.set(14, 0);
                     Timestamp result = new Timestamp(useCal.getTimeInMillis());
                     result.setNanos(ts.nanos);
                     var8 = result;
                     break label75;
                  }

                  var5 = new Timestamp(-9223372036832400000L);
               } catch (Throwable var10) {
                  if (ignore != null) {
                     try {
                        ignore.close();
                     } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                     }
                  }

                  throw var10;
               }

               if (ignore != null) {
                  ignore.close();
               }

               return var5;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var5;
         }

         if (ignore != null) {
            ignore.close();
         }

         return (Timestamp)var11;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var8;
   }

   @PolyNull
   public LocalTime toLocalTime(@PolyNull String s) throws SQLException {
      if (s == null) {
         return null;
      } else if ("24:00:00".equals(s)) {
         return LocalTime.MAX;
      } else {
         try {
            return LocalTime.parse(s);
         } catch (DateTimeParseException var3) {
            throw new PSQLException(GT.tr("Bad value for type timestamp/date/time: {0}", s), PSQLState.BAD_DATETIME_FORMAT, var3);
         }
      }
   }

   public OffsetTime toOffsetTimeBin(byte[] bytes) throws PSQLException {
      if (bytes.length != 12) {
         throw new PSQLException(GT.tr("Unsupported binary encoding of {0}.", "time"), PSQLState.BAD_DATETIME_FORMAT);
      } else {
         long micros;
         if (this.usesDouble) {
            double seconds = ByteConverter.float8(bytes, 0);
            micros = (long)(seconds * 1000000.0D);
         } else {
            micros = ByteConverter.int8(bytes, 0);
         }

         ZoneOffset timeOffset = ZoneOffset.ofTotalSeconds(-ByteConverter.int4(bytes, 8));
         return OffsetTime.of(LocalTime.ofNanoOfDay(Math.multiplyExact(micros, 1000L)), timeOffset);
      }
   }

   @PolyNull
   public OffsetTime toOffsetTime(@PolyNull String s) throws SQLException {
      if (s == null) {
         return null;
      } else if (s.startsWith("24:00:00")) {
         return OffsetTime.MAX;
      } else {
         TimestampUtils.ParsedTimestamp ts = this.parseBackendTimestamp(s);
         return OffsetTime.of(ts.hour, ts.minute, ts.second, ts.nanos, ts.offset);
      }
   }

   @PolyNull
   public LocalDateTime toLocalDateTime(@PolyNull String s) throws SQLException {
      if (s == null) {
         return null;
      } else {
         int slen = s.length();
         if (slen == 8 && "infinity".equals(s)) {
            return LocalDateTime.MAX;
         } else if (slen == 9 && "-infinity".equals(s)) {
            return LocalDateTime.MIN;
         } else {
            TimestampUtils.ParsedTimestamp ts = this.parseBackendTimestamp(s);
            LocalDateTime result = LocalDateTime.of(ts.year, ts.month, ts.day, ts.hour, ts.minute, ts.second, ts.nanos);
            return ts.era == 0 ? result.with(ChronoField.ERA, (long)IsoEra.BCE.getValue()) : result;
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public OffsetDateTime toOffsetDateTime(Time t) {
      return t.toLocalTime().atDate(LocalDate.of(1970, 1, 1)).atOffset(ZoneOffset.UTC);
   }

   @PolyNull
   public OffsetDateTime toOffsetDateTime(@PolyNull String s) throws SQLException {
      if (s == null) {
         return null;
      } else {
         int slen = s.length();
         if (slen == 8 && "infinity".equals(s)) {
            return OffsetDateTime.MAX;
         } else if (slen == 9 && "-infinity".equals(s)) {
            return OffsetDateTime.MIN;
         } else {
            TimestampUtils.ParsedTimestamp ts = this.parseBackendTimestamp(s);
            OffsetDateTime result = OffsetDateTime.of(ts.year, ts.month, ts.day, ts.hour, ts.minute, ts.second, ts.nanos, ts.offset);
            return ts.era == 0 ? result.with(ChronoField.ERA, (long)IsoEra.BCE.getValue()) : result;
         }
      }
   }

   public OffsetDateTime toOffsetDateTimeBin(byte[] bytes) throws PSQLException {
      TimestampUtils.ParsedBinaryTimestamp parsedTimestamp = this.toProlepticParsedTimestampBin(bytes);
      if (parsedTimestamp.infinity == TimestampUtils.Infinity.POSITIVE) {
         return OffsetDateTime.MAX;
      } else if (parsedTimestamp.infinity == TimestampUtils.Infinity.NEGATIVE) {
         return OffsetDateTime.MIN;
      } else {
         Instant instant = Instant.ofEpochSecond(parsedTimestamp.millis / 1000L, (long)parsedTimestamp.nanos);
         return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
      }
   }

   @PolyNull
   public Time toTime(@Nullable Calendar cal, @PolyNull String s) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      TimestampUtils.ParsedTimestamp ts;
      label70: {
         Time var8;
         label71: {
            try {
               if (s == null) {
                  ts = null;
                  break label70;
               }

               ts = this.parseBackendTimestamp(s);
               Calendar useCal = ts.hasOffset ? this.getCalendar(ts.offset) : this.setupCalendar(cal);
               if (!ts.hasOffset) {
                  useCal.set(0, ts.era);
                  useCal.set(1, ts.year);
                  useCal.set(2, ts.month - 1);
                  useCal.set(5, ts.day);
               } else {
                  useCal.set(0, 1);
                  useCal.set(1, 1970);
                  useCal.set(2, 0);
                  useCal.set(5, 1);
               }

               useCal.set(11, ts.hour);
               useCal.set(12, ts.minute);
               useCal.set(13, ts.second);
               useCal.set(14, 0);
               long timeMillis = useCal.getTimeInMillis() + (long)(ts.nanos / 1000000);
               if (!ts.hasOffset && (ts.year != 1970 || ts.era != 1)) {
                  var8 = this.convertToTime(timeMillis, useCal.getTimeZone());
                  break label71;
               }

               var8 = new Time(timeMillis);
            } catch (Throwable var10) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var8;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var8;
      }

      if (ignore != null) {
         ignore.close();
      }

      return ts;
   }

   @PolyNull
   public Date toDate(@Nullable Calendar cal, @PolyNull String s) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      Date var5;
      label48: {
         try {
            Timestamp timestamp = this.toTimestamp(cal, s);
            if (timestamp == null) {
               var5 = null;
               break label48;
            }

            var5 = this.convertToDate(timestamp.getTime(), cal == null ? null : cal.getTimeZone());
         } catch (Throwable var7) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var5;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var5;
   }

   private Calendar setupCalendar(@Nullable Calendar cal) {
      TimeZone timeZone = cal == null ? null : cal.getTimeZone();
      return this.getSharedCalendar(timeZone);
   }

   public Calendar getSharedCalendar(@Nullable TimeZone timeZone) {
      if (timeZone == null) {
         timeZone = this.getDefaultTz();
      }

      Calendar tmp = this.calendarWithUserTz;
      tmp.setTimeZone(timeZone);
      return tmp;
   }

   private static boolean nanosExceed499(int nanos) {
      return nanos % 1000 > 499;
   }

   public String toString(@Nullable Calendar cal, Timestamp x) {
      return this.toString(cal, x, true);
   }

   public String toString(@Nullable Calendar cal, Timestamp x, boolean withTimeZone) {
      ResourceLock ignore = this.lock.obtain();

      String var11;
      label67: {
         label68: {
            String var8;
            try {
               if (x.getTime() == 9223372036825200000L) {
                  var11 = "infinity";
                  break label67;
               }

               if (x.getTime() == -9223372036832400000L) {
                  var11 = "-infinity";
                  break label68;
               }

               cal = this.setupCalendar(cal);
               long timeMillis = x.getTime();
               int nanos = x.getNanos();
               if (nanos >= 999999500) {
                  nanos = 0;
                  ++timeMillis;
               } else if (nanosExceed499(nanos)) {
                  nanos += 1000 - nanos % 1000;
               }

               cal.setTimeInMillis(timeMillis);
               this.sbuf.setLength(0);
               appendDate(this.sbuf, cal);
               this.sbuf.append(' ');
               appendTime(this.sbuf, cal, nanos);
               if (withTimeZone) {
                  this.appendTimeZone(this.sbuf, cal);
               }

               appendEra(this.sbuf, cal);
               var8 = this.sbuf.toString();
            } catch (Throwable var10) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var8;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var11;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var11;
   }

   public String toString(@Nullable Calendar cal, Date x) {
      return this.toString(cal, x, true);
   }

   public String toString(@Nullable Calendar cal, Date x, boolean withTimeZone) {
      ResourceLock ignore = this.lock.obtain();

      String var5;
      label59: {
         label60: {
            try {
               if (x.getTime() == 9223372036825200000L) {
                  var5 = "infinity";
                  break label59;
               }

               if (x.getTime() == -9223372036832400000L) {
                  var5 = "-infinity";
                  break label60;
               }

               cal = this.setupCalendar(cal);
               cal.setTime(x);
               this.sbuf.setLength(0);
               appendDate(this.sbuf, cal);
               appendEra(this.sbuf, cal);
               if (withTimeZone) {
                  this.sbuf.append(' ');
                  this.appendTimeZone(this.sbuf, cal);
               }

               var5 = this.sbuf.toString();
            } catch (Throwable var8) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }
               }

               throw var8;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var5;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var5;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var5;
   }

   public String toString(@Nullable Calendar cal, Time x) {
      return this.toString(cal, x, true);
   }

   public String toString(@Nullable Calendar cal, Time x, boolean withTimeZone) {
      ResourceLock ignore = this.lock.obtain();

      String var5;
      try {
         cal = this.setupCalendar(cal);
         cal.setTime(x);
         this.sbuf.setLength(0);
         appendTime(this.sbuf, cal, cal.get(14) * 1000000);
         if (withTimeZone) {
            this.appendTimeZone(this.sbuf, cal);
         }

         var5 = this.sbuf.toString();
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var5;
   }

   private static void appendDate(StringBuilder sb, Calendar cal) {
      int year = cal.get(1);
      int month = cal.get(2) + 1;
      int day = cal.get(5);
      appendDate(sb, year, month, day);
   }

   private static void appendDate(StringBuilder sb, int year, int month, int day) {
      int prevLength = sb.length();
      sb.append(year);
      int leadingZerosForYear = 4 - (sb.length() - prevLength);
      if (leadingZerosForYear > 0) {
         sb.insert(prevLength, ZEROS, 0, leadingZerosForYear);
      }

      sb.append('-');
      sb.append(NUMBERS[month]);
      sb.append('-');
      sb.append(NUMBERS[day]);
   }

   private static void appendTime(StringBuilder sb, Calendar cal, int nanos) {
      int hours = cal.get(11);
      int minutes = cal.get(12);
      int seconds = cal.get(13);
      appendTime(sb, hours, minutes, seconds, nanos);
   }

   private static void appendTime(StringBuilder sb, int hours, int minutes, int seconds, int nanos) {
      sb.append(NUMBERS[hours]);
      sb.append(':');
      sb.append(NUMBERS[minutes]);
      sb.append(':');
      sb.append(NUMBERS[seconds]);
      if (nanos >= 1000) {
         sb.append('.');
         int len = sb.length();
         sb.append(nanos / 1000);
         int needZeros = 6 - (sb.length() - len);
         if (needZeros > 0) {
            sb.insert(len, ZEROS, 0, needZeros);
         }

         for(int end = sb.length() - 1; sb.charAt(end) == '0'; --end) {
            sb.deleteCharAt(end);
         }

      }
   }

   private void appendTimeZone(StringBuilder sb, Calendar cal) {
      int offset = (cal.get(15) + cal.get(16)) / 1000;
      this.appendTimeZone(sb, offset);
   }

   private void appendTimeZone(StringBuilder sb, int offset) {
      int absoff = Math.abs(offset);
      int hours = absoff / 60 / 60;
      int mins = (absoff - hours * 60 * 60) / 60;
      int secs = absoff - hours * 60 * 60 - mins * 60;
      sb.append(offset >= 0 ? "+" : "-");
      sb.append(NUMBERS[hours]);
      if (mins != 0 || secs != 0) {
         sb.append(':');
         sb.append(NUMBERS[mins]);
         if (secs != 0) {
            sb.append(':');
            sb.append(NUMBERS[secs]);
         }

      }
   }

   private static void appendEra(StringBuilder sb, Calendar cal) {
      if (cal.get(0) == 0) {
         sb.append(" BC");
      }

   }

   public String toString(LocalDate localDate) {
      ResourceLock ignore = this.lock.obtain();

      String var3;
      label55: {
         label56: {
            try {
               if (!LocalDate.MAX.equals(localDate)) {
                  if (localDate.isBefore(MIN_LOCAL_DATE)) {
                     var3 = "-infinity";
                     break label55;
                  }

                  this.sbuf.setLength(0);
                  appendDate(this.sbuf, localDate);
                  appendEra(this.sbuf, localDate);
                  var3 = this.sbuf.toString();
                  break label56;
               }

               var3 = "infinity";
            } catch (Throwable var6) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }
               }

               throw var6;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var3;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var3;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var3;
   }

   public String toString(LocalTime localTime) {
      ResourceLock ignore = this.lock.obtain();

      String var7;
      label48: {
         String var4;
         try {
            this.sbuf.setLength(0);
            if (localTime.isAfter(MAX_TIME)) {
               var7 = "24:00:00";
               break label48;
            }

            int nano = localTime.getNano();
            if (nanosExceed499(nano)) {
               localTime = localTime.plus(ONE_MICROSECOND);
            }

            appendTime(this.sbuf, localTime);
            var4 = this.sbuf.toString();
         } catch (Throwable var6) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var4;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var7;
   }

   public String toString(OffsetTime offsetTime) {
      ResourceLock ignore = this.lock.obtain();

      String var8;
      label48: {
         String var5;
         try {
            this.sbuf.setLength(0);
            LocalTime localTime = offsetTime.toLocalTime();
            if (localTime.isAfter(MAX_TIME)) {
               this.sbuf.append("24:00:00");
               this.appendTimeZone(this.sbuf, offsetTime.getOffset());
               var8 = this.sbuf.toString();
               break label48;
            }

            int nano = offsetTime.getNano();
            if (nanosExceed499(nano)) {
               offsetTime = offsetTime.plus(ONE_MICROSECOND);
            }

            appendTime(this.sbuf, localTime);
            this.appendTimeZone(this.sbuf, offsetTime.getOffset());
            var5 = this.sbuf.toString();
         } catch (Throwable var7) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var5;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var8;
   }

   public String toStringOffsetTimeBin(byte[] value) throws PSQLException {
      OffsetTime offsetTimeBin = this.toOffsetTimeBin(value);
      return this.toString(this.withClientOffsetSameInstant(offsetTimeBin));
   }

   public OffsetTime withClientOffsetSameInstant(OffsetTime input) {
      if (input != OffsetTime.MAX && input != OffsetTime.MIN) {
         TimeZone timeZone = (TimeZone)this.timeZoneProvider.get();
         int offsetMillis = timeZone.getRawOffset();
         return input.withOffsetSameInstant(offsetMillis == 0 ? ZoneOffset.UTC : ZoneOffset.ofTotalSeconds(offsetMillis / 1000));
      } else {
         return input;
      }
   }

   public String toString(OffsetDateTime offsetDateTime) {
      ResourceLock ignore = this.lock.obtain();

      String var9;
      label59: {
         label60: {
            String var6;
            try {
               if (offsetDateTime.isAfter(MAX_OFFSET_DATETIME)) {
                  var9 = "infinity";
                  break label59;
               }

               if (offsetDateTime.isBefore(MIN_OFFSET_DATETIME)) {
                  var9 = "-infinity";
                  break label60;
               }

               this.sbuf.setLength(0);
               int nano = offsetDateTime.getNano();
               if (nanosExceed499(nano)) {
                  offsetDateTime = offsetDateTime.plus(ONE_MICROSECOND);
               }

               LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();
               LocalDate localDate = localDateTime.toLocalDate();
               appendDate(this.sbuf, localDate);
               this.sbuf.append(' ');
               appendTime(this.sbuf, localDateTime.toLocalTime());
               this.appendTimeZone(this.sbuf, offsetDateTime.getOffset());
               appendEra(this.sbuf, localDate);
               var6 = this.sbuf.toString();
            } catch (Throwable var8) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }
               }

               throw var8;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var6;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var9;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var9;
   }

   public String toStringOffsetDateTime(byte[] value) throws PSQLException {
      OffsetDateTime offsetDateTime = this.toOffsetDateTimeBin(value);
      return this.toString(this.withClientOffsetSameInstant(offsetDateTime));
   }

   public OffsetDateTime withClientOffsetSameInstant(OffsetDateTime input) {
      if (input != OffsetDateTime.MAX && input != OffsetDateTime.MIN) {
         TimeZone timeZone = (TimeZone)this.timeZoneProvider.get();
         int offsetMillis;
         if (isSimpleTimeZone(timeZone.getID())) {
            offsetMillis = timeZone.getRawOffset();
         } else {
            offsetMillis = timeZone.getOffset(input.toEpochSecond() * 1000L);
         }

         return input.withOffsetSameInstant(offsetMillis == 0 ? ZoneOffset.UTC : ZoneOffset.ofTotalSeconds(offsetMillis / 1000));
      } else {
         return input;
      }
   }

   public String toString(LocalDateTime localDateTime) {
      ResourceLock ignore = this.lock.obtain();

      String var7;
      label59: {
         label60: {
            String var4;
            try {
               if (localDateTime.isAfter(MAX_LOCAL_DATETIME)) {
                  var7 = "infinity";
                  break label59;
               }

               if (localDateTime.isBefore(MIN_LOCAL_DATETIME)) {
                  var7 = "-infinity";
                  break label60;
               }

               this.sbuf.setLength(0);
               if (nanosExceed499(localDateTime.getNano())) {
                  localDateTime = localDateTime.plus(ONE_MICROSECOND);
               }

               LocalDate localDate = localDateTime.toLocalDate();
               appendDate(this.sbuf, localDate);
               this.sbuf.append(' ');
               appendTime(this.sbuf, localDateTime.toLocalTime());
               appendEra(this.sbuf, localDate);
               var4 = this.sbuf.toString();
            } catch (Throwable var6) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }
               }

               throw var6;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var4;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var7;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var7;
   }

   private static void appendDate(StringBuilder sb, LocalDate localDate) {
      int year = localDate.get(ChronoField.YEAR_OF_ERA);
      int month = localDate.getMonthValue();
      int day = localDate.getDayOfMonth();
      appendDate(sb, year, month, day);
   }

   private static void appendTime(StringBuilder sb, LocalTime localTime) {
      int hours = localTime.getHour();
      int minutes = localTime.getMinute();
      int seconds = localTime.getSecond();
      int nanos = localTime.getNano();
      appendTime(sb, hours, minutes, seconds, nanos);
   }

   private void appendTimeZone(StringBuilder sb, ZoneOffset offset) {
      int offsetSeconds = offset.getTotalSeconds();
      this.appendTimeZone(sb, offsetSeconds);
   }

   private static void appendEra(StringBuilder sb, LocalDate localDate) {
      if (localDate.get(ChronoField.ERA) == IsoEra.BCE.getValue()) {
         sb.append(" BC");
      }

   }

   private static int skipWhitespace(char[] s, int start) {
      int slen = s.length;

      for(int i = start; i < slen; ++i) {
         if (!Character.isSpace(s[i])) {
            return i;
         }
      }

      return slen;
   }

   private static int firstNonDigit(char[] s, int start) {
      int slen = s.length;

      for(int i = start; i < slen; ++i) {
         if (!Character.isDigit(s[i])) {
            return i;
         }
      }

      return slen;
   }

   private static int number(char[] s, int start, int end) {
      if (start >= end) {
         throw new NumberFormatException();
      } else {
         int n = 0;

         for(int i = start; i < end; ++i) {
            n = 10 * n + (s[i] - 48);
         }

         return n;
      }
   }

   private static char charAt(char[] s, int pos) {
      return pos >= 0 && pos < s.length ? s[pos] : '\u0000';
   }

   public Date toDateBin(@Nullable TimeZone tz, byte[] bytes) throws PSQLException {
      if (bytes.length != 4) {
         throw new PSQLException(GT.tr("Unsupported binary encoding of {0}.", "date"), PSQLState.BAD_DATETIME_FORMAT);
      } else {
         int days = ByteConverter.int4(bytes, 0);
         if (tz == null) {
            tz = this.getDefaultTz();
         }

         long secs = toJavaSecs((long)days * 86400L);
         long millis = secs * 1000L;
         if (millis <= -185543533774800000L) {
            millis = -9223372036832400000L;
         } else if (millis >= 185543533774800000L) {
            millis = 9223372036825200000L;
         } else {
            millis = this.guessTimestamp(millis, tz);
         }

         return new Date(millis);
      }
   }

   private TimeZone getDefaultTz() {
      TimeZone defaultTimeZone;
      if (DEFAULT_TIME_ZONE_FIELD != null) {
         try {
            defaultTimeZone = (TimeZone)DEFAULT_TIME_ZONE_FIELD.get((Object)null);
            if (defaultTimeZone == this.prevDefaultZoneFieldValue) {
               return (TimeZone)Nullness.castNonNull(this.defaultTimeZoneCache);
            }

            this.prevDefaultZoneFieldValue = defaultTimeZone;
         } catch (Exception var2) {
         }
      }

      defaultTimeZone = TimeZone.getDefault();
      this.defaultTimeZoneCache = defaultTimeZone;
      return defaultTimeZone;
   }

   public boolean hasFastDefaultTimeZone() {
      return DEFAULT_TIME_ZONE_FIELD != null;
   }

   public Time toTimeBin(@Nullable TimeZone tz, byte[] bytes) throws PSQLException {
      if (bytes.length != 8 && bytes.length != 12) {
         throw new PSQLException(GT.tr("Unsupported binary encoding of {0}.", "time"), PSQLState.BAD_DATETIME_FORMAT);
      } else {
         long millis;
         if (this.usesDouble) {
            double time = ByteConverter.float8(bytes, 0);
            millis = (long)(time * 1000.0D);
         } else {
            long time = ByteConverter.int8(bytes, 0);
            millis = time / 1000L;
         }

         if (bytes.length == 12) {
            int timeOffset = ByteConverter.int4(bytes, 8);
            timeOffset *= -1000;
            millis -= (long)timeOffset;
            return new Time(millis);
         } else {
            if (tz == null) {
               tz = this.getDefaultTz();
            }

            millis = this.guessTimestamp(millis, tz);
            return this.convertToTime(millis, tz);
         }
      }
   }

   public LocalTime toLocalTimeBin(byte[] bytes) throws PSQLException {
      if (bytes.length != 8) {
         throw new PSQLException(GT.tr("Unsupported binary encoding of {0}.", "time"), PSQLState.BAD_DATETIME_FORMAT);
      } else {
         long micros;
         if (this.usesDouble) {
            double seconds = ByteConverter.float8(bytes, 0);
            micros = (long)(seconds * 1000000.0D);
         } else {
            micros = ByteConverter.int8(bytes, 0);
         }

         return LocalTime.ofNanoOfDay(Math.multiplyExact(micros, 1000L));
      }
   }

   public Timestamp toTimestampBin(@Nullable TimeZone tz, byte[] bytes, boolean timestamptz) throws PSQLException {
      TimestampUtils.ParsedBinaryTimestamp parsedTimestamp = this.toParsedTimestampBin(tz, bytes, timestamptz);
      if (parsedTimestamp.infinity == TimestampUtils.Infinity.POSITIVE) {
         return new Timestamp(9223372036825200000L);
      } else if (parsedTimestamp.infinity == TimestampUtils.Infinity.NEGATIVE) {
         return new Timestamp(-9223372036832400000L);
      } else {
         Timestamp ts = new Timestamp(parsedTimestamp.millis);
         ts.setNanos(parsedTimestamp.nanos);
         return ts;
      }
   }

   private TimestampUtils.ParsedBinaryTimestamp toParsedTimestampBinPlain(byte[] bytes) throws PSQLException {
      if (bytes.length != 8) {
         throw new PSQLException(GT.tr("Unsupported binary encoding of {0}.", "timestamp"), PSQLState.BAD_DATETIME_FORMAT);
      } else {
         long secs;
         int nanos;
         TimestampUtils.ParsedBinaryTimestamp ts;
         long millis;
         if (this.usesDouble) {
            double time = ByteConverter.float8(bytes, 0);
            if (time == Double.POSITIVE_INFINITY) {
               ts = new TimestampUtils.ParsedBinaryTimestamp();
               ts.infinity = TimestampUtils.Infinity.POSITIVE;
               return ts;
            }

            if (time == Double.NEGATIVE_INFINITY) {
               ts = new TimestampUtils.ParsedBinaryTimestamp();
               ts.infinity = TimestampUtils.Infinity.NEGATIVE;
               return ts;
            }

            secs = (long)time;
            nanos = (int)((time - (double)secs) * 1000000.0D);
         } else {
            millis = ByteConverter.int8(bytes, 0);
            if (millis == Long.MAX_VALUE) {
               ts = new TimestampUtils.ParsedBinaryTimestamp();
               ts.infinity = TimestampUtils.Infinity.POSITIVE;
               return ts;
            }

            if (millis == Long.MIN_VALUE) {
               ts = new TimestampUtils.ParsedBinaryTimestamp();
               ts.infinity = TimestampUtils.Infinity.NEGATIVE;
               return ts;
            }

            secs = millis / 1000000L;
            nanos = (int)(millis - secs * 1000000L);
         }

         if (nanos < 0) {
            --secs;
            nanos += 1000000;
         }

         nanos *= 1000;
         millis = secs * 1000L;
         ts = new TimestampUtils.ParsedBinaryTimestamp();
         ts.millis = millis;
         ts.nanos = nanos;
         return ts;
      }
   }

   private TimestampUtils.ParsedBinaryTimestamp toParsedTimestampBin(@Nullable TimeZone tz, byte[] bytes, boolean timestamptz) throws PSQLException {
      TimestampUtils.ParsedBinaryTimestamp ts = this.toParsedTimestampBinPlain(bytes);
      if (ts.infinity != null) {
         return ts;
      } else {
         long secs = ts.millis / 1000L;
         secs = toJavaSecs(secs);
         long millis = secs * 1000L;
         if (!timestamptz) {
            millis = this.guessTimestamp(millis, tz);
         }

         ts.millis = millis;
         return ts;
      }
   }

   private TimestampUtils.ParsedBinaryTimestamp toProlepticParsedTimestampBin(byte[] bytes) throws PSQLException {
      TimestampUtils.ParsedBinaryTimestamp ts = this.toParsedTimestampBinPlain(bytes);
      if (ts.infinity != null) {
         return ts;
      } else {
         long secs = ts.millis / 1000L;
         secs += PG_EPOCH_DIFF.getSeconds();
         long millis = secs * 1000L;
         ts.millis = millis;
         return ts;
      }
   }

   public LocalDateTime toLocalDateTimeBin(byte[] bytes) throws PSQLException {
      TimestampUtils.ParsedBinaryTimestamp parsedTimestamp = this.toProlepticParsedTimestampBin(bytes);
      if (parsedTimestamp.infinity == TimestampUtils.Infinity.POSITIVE) {
         return LocalDateTime.MAX;
      } else {
         return parsedTimestamp.infinity == TimestampUtils.Infinity.NEGATIVE ? LocalDateTime.MIN : LocalDateTime.ofEpochSecond(parsedTimestamp.millis / 1000L, parsedTimestamp.nanos, ZoneOffset.UTC);
      }
   }

   public LocalDate toLocalDateBin(byte[] bytes) throws PSQLException {
      if (bytes.length != 4) {
         throw new PSQLException(GT.tr("Unsupported binary encoding of {0}.", "date"), PSQLState.BAD_DATETIME_FORMAT);
      } else {
         int days = ByteConverter.int4(bytes, 0);
         if (days == Integer.MAX_VALUE) {
            return LocalDate.MAX;
         } else {
            return days == Integer.MIN_VALUE ? LocalDate.MIN : LocalDate.ofEpochDay(PG_EPOCH_DIFF.toDays() + (long)days);
         }
      }
   }

   private long guessTimestamp(long millis, @Nullable TimeZone tz) {
      if (tz == null) {
         tz = this.getDefaultTz();
      }

      if (isSimpleTimeZone(tz.getID())) {
         return millis - (long)tz.getRawOffset();
      } else {
         Calendar cal = this.calendarWithUserTz;
         cal.setTimeZone(UTC_TIMEZONE);
         cal.setTimeInMillis(millis);
         int era = cal.get(0);
         int year = cal.get(1);
         int month = cal.get(2);
         int day = cal.get(5);
         int hour = cal.get(11);
         int min = cal.get(12);
         int sec = cal.get(13);
         int ms = cal.get(14);
         cal.setTimeZone(tz);
         cal.set(0, era);
         cal.set(1, year);
         cal.set(2, month);
         cal.set(5, day);
         cal.set(11, hour);
         cal.set(12, min);
         cal.set(13, sec);
         cal.set(14, ms);
         return cal.getTimeInMillis();
      }
   }

   private static boolean isSimpleTimeZone(String id) {
      return id.startsWith("GMT") || id.startsWith("UTC");
   }

   public Date convertToDate(long millis, @Nullable TimeZone tz) {
      if (millis > -9223372036832400000L && millis < 9223372036825200000L) {
         if (tz == null) {
            tz = this.getDefaultTz();
         }

         if (isSimpleTimeZone(tz.getID())) {
            int offset = tz.getRawOffset();
            millis += (long)offset;
            millis = floorDiv(millis, 86400000L) * 86400000L;
            millis -= (long)offset;
            return new Date(millis);
         } else {
            Calendar cal = this.calendarWithUserTz;
            cal.setTimeZone(tz);
            cal.setTimeInMillis(millis);
            cal.set(11, 0);
            cal.set(12, 0);
            cal.set(13, 0);
            cal.set(14, 0);
            return new Date(cal.getTimeInMillis());
         }
      } else {
         return new Date(millis);
      }
   }

   public Time convertToTime(long millis, TimeZone tz) {
      if (tz == null) {
         tz = this.getDefaultTz();
      }

      if (isSimpleTimeZone(tz.getID())) {
         int offset = tz.getRawOffset();
         millis += (long)offset;
         millis = floorMod(millis, 86400000L);
         millis -= (long)offset;
         return new Time(millis);
      } else {
         Calendar cal = this.calendarWithUserTz;
         cal.setTimeZone(tz);
         cal.setTimeInMillis(millis);
         cal.set(0, 1);
         cal.set(1, 1970);
         cal.set(2, 0);
         cal.set(5, 1);
         return new Time(cal.getTimeInMillis());
      }
   }

   public String timeToString(java.util.Date time, boolean withTimeZone) {
      Calendar cal = null;
      if (withTimeZone) {
         cal = this.calendarWithUserTz;
         cal.setTimeZone((TimeZone)this.timeZoneProvider.get());
      }

      if (time instanceof Timestamp) {
         return this.toString(cal, (Timestamp)time, withTimeZone);
      } else {
         return time instanceof Time ? this.toString(cal, (Time)time, withTimeZone) : this.toString(cal, (Date)time, withTimeZone);
      }
   }

   private static long toJavaSecs(long secs) {
      secs += PG_EPOCH_DIFF.getSeconds();
      if (secs < -12219292800L) {
         secs += 864000L;
         if (secs < -14825808000L) {
            int extraLeaps = (int)((secs + 14825808000L) / 3155760000L);
            --extraLeaps;
            extraLeaps -= extraLeaps / 4;
            secs += (long)extraLeaps * 86400L;
         }
      }

      return secs;
   }

   private static long toPgSecs(long secs) {
      secs -= PG_EPOCH_DIFF.getSeconds();
      if (secs < -13165977600L) {
         secs -= 864000L;
         if (secs < -15773356800L) {
            int years = (int)((secs + 15773356800L) / -3155823050L);
            ++years;
            years -= years / 4;
            secs += (long)years * 86400L;
         }
      }

      return secs;
   }

   public void toBinDate(@Nullable TimeZone tz, byte[] bytes, Date value) throws PSQLException {
      long millis = value.getTime();
      if (tz == null) {
         tz = this.getDefaultTz();
      }

      millis += (long)tz.getOffset(millis);
      long secs = toPgSecs(millis / 1000L);
      ByteConverter.int4(bytes, 0, (int)(secs / 86400L));
   }

   public static TimeZone parseBackendTimeZone(String timeZone) {
      if (timeZone.startsWith("GMT")) {
         TimeZone tz = (TimeZone)GMT_ZONES.get(timeZone);
         if (tz != null) {
            return tz;
         }
      }

      return TimeZone.getTimeZone(timeZone);
   }

   private static long floorDiv(long x, long y) {
      long r = x / y;
      if ((x ^ y) < 0L && r * y != x) {
         --r;
      }

      return r;
   }

   private static long floorMod(long x, long y) {
      return x - floorDiv(x, y) * y;
   }

   static {
      MAX_TIME = LocalTime.MAX.minus(Duration.ofNanos(500L));
      MAX_OFFSET_DATETIME = OffsetDateTime.MAX.minus(Duration.ofMillis(500L));
      MAX_LOCAL_DATETIME = LocalDateTime.MAX.minus(Duration.ofMillis(500L));
      MIN_LOCAL_DATE = LocalDate.of(4713, 1, 1).with(ChronoField.ERA, (long)IsoEra.BCE.getValue());
      MIN_LOCAL_DATETIME = MIN_LOCAL_DATE.atStartOfDay();
      MIN_OFFSET_DATETIME = MIN_LOCAL_DATETIME.atOffset(ZoneOffset.UTC);
      PG_EPOCH_DIFF = Duration.between(Instant.EPOCH, LocalDate.of(2000, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC));
      UTC_TIMEZONE = TimeZone.getTimeZone(ZoneOffset.UTC);
      NUMBERS = new char[64][];

      int i;
      for(i = 0; i < NUMBERS.length; ++i) {
         NUMBERS[i] = ((i < 10 ? "0" : "") + Integer.toString(i)).toCharArray();
      }

      TimeZone timeZone;
      for(i = -12; i <= 14; ++i) {
         String pgZoneName;
         if (i == 0) {
            timeZone = TimeZone.getTimeZone("GMT");
            pgZoneName = "GMT";
         } else {
            timeZone = TimeZone.getTimeZone("GMT" + (i <= 0 ? "+" : "-") + Math.abs(i));
            pgZoneName = "GMT" + (i >= 0 ? "+" : "-");
         }

         if (i == 0) {
            GMT_ZONES.put(pgZoneName, timeZone);
         } else {
            GMT_ZONES.put(pgZoneName + Math.abs(i), timeZone);
            GMT_ZONES.put(pgZoneName + new String(NUMBERS[Math.abs(i)]), timeZone);
         }
      }

      Field tzField;
      try {
         tzField = null;
         if (JavaVersion.getRuntimeVersion().compareTo(JavaVersion.v1_8) <= 0) {
            tzField = TimeZone.class.getDeclaredField("defaultTimeZone");
            tzField.setAccessible(true);
            timeZone = TimeZone.getDefault();
            Object tzFromField = tzField.get((Object)null);
            if (timeZone == null || !timeZone.equals(tzFromField)) {
               tzField = null;
            }
         }
      } catch (Exception var3) {
         tzField = null;
      }

      DEFAULT_TIME_ZONE_FIELD = tzField;
   }

   private static class ParsedTimestamp {
      boolean hasDate;
      int era;
      int year;
      int month;
      boolean hasTime;
      int day;
      int hour;
      int minute;
      int second;
      int nanos;
      boolean hasOffset;
      ZoneOffset offset;

      private ParsedTimestamp() {
         this.era = 1;
         this.year = 1970;
         this.month = 1;
         this.day = 1;
         this.offset = ZoneOffset.UTC;
      }

      // $FF: synthetic method
      ParsedTimestamp(Object x0) {
         this();
      }
   }

   private static class ParsedBinaryTimestamp {
      @Nullable
      TimestampUtils.Infinity infinity;
      long millis;
      int nanos;

      private ParsedBinaryTimestamp() {
      }

      // $FF: synthetic method
      ParsedBinaryTimestamp(Object x0) {
         this();
      }
   }

   static enum Infinity {
      POSITIVE,
      NEGATIVE;

      // $FF: synthetic method
      private static TimestampUtils.Infinity[] $values() {
         return new TimestampUtils.Infinity[]{POSITIVE, NEGATIVE};
      }
   }
}
