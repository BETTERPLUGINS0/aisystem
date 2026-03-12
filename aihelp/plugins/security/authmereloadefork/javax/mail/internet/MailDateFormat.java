package javax.mail.internet;

import com.sun.mail.util.MailLogger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;

public class MailDateFormat extends SimpleDateFormat {
   private static final long serialVersionUID = -8148227605210628779L;
   private static final String PATTERN = "EEE, d MMM yyyy HH:mm:ss Z (z)";
   private static final MailLogger LOGGER;
   private static final int UNKNOWN_DAY_NAME = -1;
   private static final TimeZone UTC;
   private static final int LEAP_SECOND = 60;

   public MailDateFormat() {
      super("EEE, d MMM yyyy HH:mm:ss Z (z)", Locale.US);
   }

   private Object writeReplace() throws ObjectStreamException {
      MailDateFormat fmt = new MailDateFormat();
      fmt.superApplyPattern("EEE, d MMM yyyy HH:mm:ss 'XXXXX' (z)");
      fmt.setTimeZone(this.getTimeZone());
      return fmt;
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      super.applyPattern("EEE, d MMM yyyy HH:mm:ss Z (z)");
   }

   public MailDateFormat clone() {
      return (MailDateFormat)super.clone();
   }

   public StringBuffer format(Date date, StringBuffer dateStrBuf, FieldPosition fieldPosition) {
      return super.format(date, dateStrBuf, fieldPosition);
   }

   public Date parse(String text, ParsePosition pos) {
      if (text != null && pos != null) {
         if (0 <= pos.getIndex() && pos.getIndex() < text.length()) {
            return this.isLenient() ? (new MailDateFormat.Rfc2822LenientParser(text, pos)).parse() : (new MailDateFormat.Rfc2822StrictParser(text, pos)).parse();
         } else {
            return null;
         }
      } else {
         throw new NullPointerException();
      }
   }

   public void setCalendar(Calendar newCalendar) {
      throw new UnsupportedOperationException("Method setCalendar() shouldn't be called");
   }

   public void setNumberFormat(NumberFormat newNumberFormat) {
      throw new UnsupportedOperationException("Method setNumberFormat() shouldn't be called");
   }

   public void applyLocalizedPattern(String pattern) {
      throw new UnsupportedOperationException("Method applyLocalizedPattern() shouldn't be called");
   }

   public void applyPattern(String pattern) {
      throw new UnsupportedOperationException("Method applyPattern() shouldn't be called");
   }

   private void superApplyPattern(String pattern) {
      super.applyPattern(pattern);
   }

   public Date get2DigitYearStart() {
      throw new UnsupportedOperationException("Method get2DigitYearStart() shouldn't be called");
   }

   public void set2DigitYearStart(Date startDate) {
      throw new UnsupportedOperationException("Method set2DigitYearStart() shouldn't be called");
   }

   public void setDateFormatSymbols(DateFormatSymbols newFormatSymbols) {
      throw new UnsupportedOperationException("Method setDateFormatSymbols() shouldn't be called");
   }

   private Date toDate(int dayName, int day, int month, int year, int hour, int minute, int second, int zone) {
      if (second == 60) {
         second = 59;
      }

      TimeZone tz = this.calendar.getTimeZone();

      Date var10;
      try {
         this.calendar.setTimeZone(UTC);
         this.calendar.clear();
         this.calendar.set(year, month, day, hour, minute, second);
         if (dayName != -1 && dayName != this.calendar.get(7)) {
            throw new IllegalArgumentException("Inconsistent day-name");
         }

         this.calendar.add(12, zone);
         var10 = this.calendar.getTime();
      } finally {
         this.calendar.setTimeZone(tz);
      }

      return var10;
   }

   static {
      LOGGER = new MailLogger(MailDateFormat.class, "DEBUG", false, System.out);
      UTC = TimeZone.getTimeZone("UTC");
   }

   private class Rfc2822LenientParser extends MailDateFormat.Rfc2822StrictParser {
      private Boolean hasDefaultFws;

      Rfc2822LenientParser(String text, ParsePosition pos) {
         super(text, pos);
      }

      int parseOptionalBegin() {
         while(this.pos.getIndex() < this.text.length() && !this.peekAsciiDigit()) {
            this.pos.setIndex(this.pos.getIndex() + 1);
         }

         return -1;
      }

      int parseDay() throws java.text.ParseException {
         this.skipFoldingWhiteSpace();
         return this.parseAsciiDigits(1, 3);
      }

      void parseFwsInMonth() throws java.text.ParseException {
         if (this.hasDefaultFws == null) {
            this.hasDefaultFws = !this.skipChar('-');
            this.skipFoldingWhiteSpace();
         } else if (this.hasDefaultFws) {
            this.skipFoldingWhiteSpace();
         } else {
            this.parseChar('-');
         }

      }

      boolean isMonthNameCaseSensitive() {
         return false;
      }

      int parseYear() throws java.text.ParseException {
         int year = this.parseAsciiDigits(1, 8);
         if (year >= 1000) {
            return year;
         } else {
            return year >= 50 ? year + 1900 : year + 2000;
         }
      }

      int parseHour() throws java.text.ParseException {
         return this.parseAsciiDigits(1, 2);
      }

      int parseMinute() throws java.text.ParseException {
         return this.parseAsciiDigits(1, 2);
      }

      int parseSecond() throws java.text.ParseException {
         return this.parseAsciiDigits(1, 2);
      }

      void parseFwsBetweenTimeOfDayAndZone() throws java.text.ParseException {
         this.skipFoldingWhiteSpace();
      }

      int parseZone() throws java.text.ParseException {
         try {
            if (this.pos.getIndex() >= this.text.length()) {
               throw new java.text.ParseException("Missing zone", this.pos.getIndex());
            } else if (!this.peekChar('+') && !this.peekChar('-')) {
               if (this.skipAlternativePair('U', 'u', 'T', 't')) {
                  return 0;
               } else if (this.skipAlternativeTriple('G', 'g', 'M', 'm', 'T', 't')) {
                  return 0;
               } else {
                  int hoursOffset;
                  if (this.skipAlternative('E', 'e')) {
                     hoursOffset = 4;
                  } else if (this.skipAlternative('C', 'c')) {
                     hoursOffset = 5;
                  } else if (this.skipAlternative('M', 'm')) {
                     hoursOffset = 6;
                  } else {
                     if (!this.skipAlternative('P', 'p')) {
                        throw new java.text.ParseException("Invalid zone", this.pos.getIndex());
                     }

                     hoursOffset = 7;
                  }

                  if (this.skipAlternativePair('S', 's', 'T', 't')) {
                     ++hoursOffset;
                  } else if (!this.skipAlternativePair('D', 'd', 'T', 't')) {
                     this.pos.setIndex(this.pos.getIndex() - 1);
                     throw new java.text.ParseException("Invalid zone", this.pos.getIndex());
                  }

                  return hoursOffset * 60;
               }
            } else {
               return this.parseZoneOffset();
            }
         } catch (java.text.ParseException var2) {
            if (MailDateFormat.LOGGER.isLoggable(Level.FINE)) {
               MailDateFormat.LOGGER.log(Level.FINE, "No timezone? : '" + this.text + "'", (Throwable)var2);
            }

            return 0;
         }
      }

      boolean isValidZoneOffset(int offset) {
         return true;
      }

      boolean skipFoldingWhiteSpace() {
         boolean result = this.peekFoldingWhiteSpace();

         while(this.pos.getIndex() < this.text.length()) {
            switch(this.text.charAt(this.pos.getIndex())) {
            case '\t':
            case '\n':
            case '\r':
            case ' ':
               this.pos.setIndex(this.pos.getIndex() + 1);
               break;
            default:
               return result;
            }
         }

         return result;
      }

      boolean peekFoldingWhiteSpace() {
         return super.peekFoldingWhiteSpace() || this.pos.getIndex() < this.text.length() && this.text.charAt(this.pos.getIndex()) == '\n';
      }
   }

   private class Rfc2822StrictParser extends MailDateFormat.AbstractDateParser {
      Rfc2822StrictParser(String text, ParsePosition pos) {
         super(text, pos);
      }

      Date tryParse() throws java.text.ParseException {
         int dayName = this.parseOptionalBegin();
         int day = this.parseDay();
         int month = this.parseMonth();
         int year = this.parseYear();
         this.parseFoldingWhiteSpace();
         int hour = this.parseHour();
         this.parseChar(':');
         int minute = this.parseMinute();
         int second = this.skipChar(':') ? this.parseSecond() : 0;
         this.parseFwsBetweenTimeOfDayAndZone();
         int zone = this.parseZone();

         try {
            return MailDateFormat.this.toDate(dayName, day, month, year, hour, minute, second, zone);
         } catch (IllegalArgumentException var10) {
            throw new java.text.ParseException("Invalid input: some of the calendar fields have invalid values, or day-name is inconsistent with date", this.pos.getIndex());
         }
      }

      int parseOptionalBegin() throws java.text.ParseException {
         int dayName;
         if (!this.peekAsciiDigit()) {
            this.skipFoldingWhiteSpace();
            dayName = this.parseDayName();
            this.parseChar(',');
         } else {
            dayName = -1;
         }

         return dayName;
      }

      int parseDay() throws java.text.ParseException {
         this.skipFoldingWhiteSpace();
         return this.parseAsciiDigits(1, 2);
      }

      int parseMonth() throws java.text.ParseException {
         this.parseFwsInMonth();
         int month = this.parseMonthName(this.isMonthNameCaseSensitive());
         this.parseFwsInMonth();
         return month;
      }

      void parseFwsInMonth() throws java.text.ParseException {
         this.parseFoldingWhiteSpace();
      }

      boolean isMonthNameCaseSensitive() {
         return true;
      }

      int parseYear() throws java.text.ParseException {
         int year = this.parseAsciiDigits(4, 8);
         if (year >= 1900) {
            return year;
         } else {
            this.pos.setIndex(this.pos.getIndex() - 4);

            while(this.text.charAt(this.pos.getIndex() - 1) == '0') {
               this.pos.setIndex(this.pos.getIndex() - 1);
            }

            throw new java.text.ParseException("Invalid year", this.pos.getIndex());
         }
      }

      int parseHour() throws java.text.ParseException {
         return this.parseAsciiDigits(2);
      }

      int parseMinute() throws java.text.ParseException {
         return this.parseAsciiDigits(2);
      }

      int parseSecond() throws java.text.ParseException {
         return this.parseAsciiDigits(2);
      }

      void parseFwsBetweenTimeOfDayAndZone() throws java.text.ParseException {
         this.parseFoldingWhiteSpace();
      }

      int parseZone() throws java.text.ParseException {
         return this.parseZoneOffset();
      }
   }

   private abstract static class AbstractDateParser {
      static final int INVALID_CHAR = -1;
      static final int MAX_YEAR_DIGITS = 8;
      final String text;
      final ParsePosition pos;

      AbstractDateParser(String text, ParsePosition pos) {
         this.text = text;
         this.pos = pos;
      }

      final Date parse() {
         int startPosition = this.pos.getIndex();

         try {
            return this.tryParse();
         } catch (Exception var3) {
            if (MailDateFormat.LOGGER.isLoggable(Level.FINE)) {
               MailDateFormat.LOGGER.log(Level.FINE, "Bad date: '" + this.text + "'", (Throwable)var3);
            }

            this.pos.setErrorIndex(this.pos.getIndex());
            this.pos.setIndex(startPosition);
            return null;
         }
      }

      abstract Date tryParse() throws java.text.ParseException;

      final int parseDayName() throws java.text.ParseException {
         switch(this.getChar()) {
         case -1:
            throw new java.text.ParseException("Invalid day-name", this.pos.getIndex());
         case 70:
            if (this.skipPair('r', 'i')) {
               return 6;
            }
            break;
         case 77:
            if (this.skipPair('o', 'n')) {
               return 2;
            }
            break;
         case 83:
            if (this.skipPair('u', 'n')) {
               return 1;
            }

            if (this.skipPair('a', 't')) {
               return 7;
            }
            break;
         case 84:
            if (this.skipPair('u', 'e')) {
               return 3;
            }

            if (this.skipPair('h', 'u')) {
               return 5;
            }
            break;
         case 87:
            if (this.skipPair('e', 'd')) {
               return 4;
            }
         }

         this.pos.setIndex(this.pos.getIndex() - 1);
         throw new java.text.ParseException("Invalid day-name", this.pos.getIndex());
      }

      final int parseMonthName(boolean caseSensitive) throws java.text.ParseException {
         switch(this.getChar()) {
         case -1:
            throw new java.text.ParseException("Invalid month", this.pos.getIndex());
         case 97:
            if (caseSensitive) {
               break;
            }
         case 65:
            if (this.skipPair('u', 'g') || !caseSensitive && this.skipAlternativePair('u', 'U', 'g', 'G')) {
               return 7;
            }

            if (this.skipPair('p', 'r') || !caseSensitive && this.skipAlternativePair('p', 'P', 'r', 'R')) {
               return 3;
            }
            break;
         case 100:
            if (caseSensitive) {
               break;
            }
         case 68:
            if (this.skipPair('e', 'c') || !caseSensitive && this.skipAlternativePair('e', 'E', 'c', 'C')) {
               return 11;
            }
            break;
         case 102:
            if (caseSensitive) {
               break;
            }
         case 70:
            if (this.skipPair('e', 'b') || !caseSensitive && this.skipAlternativePair('e', 'E', 'b', 'B')) {
               return 1;
            }
            break;
         case 106:
            if (caseSensitive) {
               break;
            }
         case 74:
            if (!this.skipChar('u') && (caseSensitive || !this.skipChar('U'))) {
               if (this.skipPair('a', 'n') || !caseSensitive && this.skipAlternativePair('a', 'A', 'n', 'N')) {
                  return 0;
               }
            } else {
               if (this.skipChar('l') || !caseSensitive && this.skipChar('L')) {
                  return 6;
               }

               if (this.skipChar('n') || !caseSensitive && this.skipChar('N')) {
                  return 5;
               }

               this.pos.setIndex(this.pos.getIndex() - 1);
            }
            break;
         case 109:
            if (caseSensitive) {
               break;
            }
         case 77:
            if (!this.skipChar('a') && (caseSensitive || !this.skipChar('A'))) {
               break;
            }

            if (this.skipChar('r') || !caseSensitive && this.skipChar('R')) {
               return 2;
            }

            if (this.skipChar('y') || !caseSensitive && this.skipChar('Y')) {
               return 4;
            }

            this.pos.setIndex(this.pos.getIndex() - 1);
            break;
         case 110:
            if (caseSensitive) {
               break;
            }
         case 78:
            if (this.skipPair('o', 'v') || !caseSensitive && this.skipAlternativePair('o', 'O', 'v', 'V')) {
               return 10;
            }
            break;
         case 111:
            if (caseSensitive) {
               break;
            }
         case 79:
            if (this.skipPair('c', 't') || !caseSensitive && this.skipAlternativePair('c', 'C', 't', 'T')) {
               return 9;
            }
            break;
         case 115:
            if (caseSensitive) {
               break;
            }
         case 83:
            if (this.skipPair('e', 'p') || !caseSensitive && this.skipAlternativePair('e', 'E', 'p', 'P')) {
               return 8;
            }
         }

         this.pos.setIndex(this.pos.getIndex() - 1);
         throw new java.text.ParseException("Invalid month", this.pos.getIndex());
      }

      final int parseZoneOffset() throws java.text.ParseException {
         int sign = this.getChar();
         if (sign != 43 && sign != 45) {
            if (sign != -1) {
               this.pos.setIndex(this.pos.getIndex() - 1);
            }

            throw new java.text.ParseException("Invalid zone", this.pos.getIndex());
         } else {
            int offset = this.parseAsciiDigits(4, 4, true);
            if (!this.isValidZoneOffset(offset)) {
               this.pos.setIndex(this.pos.getIndex() - 5);
               throw new java.text.ParseException("Invalid zone", this.pos.getIndex());
            } else {
               return (sign == 43 ? -1 : 1) * (offset / 100 * 60 + offset % 100);
            }
         }
      }

      boolean isValidZoneOffset(int offset) {
         return offset % 100 < 60;
      }

      final int parseAsciiDigits(int count) throws java.text.ParseException {
         return this.parseAsciiDigits(count, count);
      }

      final int parseAsciiDigits(int min, int max) throws java.text.ParseException {
         return this.parseAsciiDigits(min, max, false);
      }

      final int parseAsciiDigits(int min, int max, boolean isEOF) throws java.text.ParseException {
         int result = 0;

         int nbDigitsParsed;
         for(nbDigitsParsed = 0; nbDigitsParsed < max && this.peekAsciiDigit(); ++nbDigitsParsed) {
            result = result * 10 + this.getAsciiDigit();
         }

         if (nbDigitsParsed >= min && (nbDigitsParsed != max || isEOF || !this.peekAsciiDigit())) {
            return result;
         } else {
            this.pos.setIndex(this.pos.getIndex() - nbDigitsParsed);
            String range = min == max ? Integer.toString(min) : "between " + min + " and " + max;
            throw new java.text.ParseException("Invalid input: expected " + range + " ASCII digits", this.pos.getIndex());
         }
      }

      final void parseFoldingWhiteSpace() throws java.text.ParseException {
         if (!this.skipFoldingWhiteSpace()) {
            throw new java.text.ParseException("Invalid input: expected FWS", this.pos.getIndex());
         }
      }

      final void parseChar(char ch) throws java.text.ParseException {
         if (!this.skipChar(ch)) {
            throw new java.text.ParseException("Invalid input: expected '" + ch + "'", this.pos.getIndex());
         }
      }

      final int getAsciiDigit() {
         int ch = this.getChar();
         if (48 <= ch && ch <= 57) {
            return Character.digit((char)ch, 10);
         } else {
            if (ch != -1) {
               this.pos.setIndex(this.pos.getIndex() - 1);
            }

            return -1;
         }
      }

      final int getChar() {
         if (this.pos.getIndex() < this.text.length()) {
            char ch = this.text.charAt(this.pos.getIndex());
            this.pos.setIndex(this.pos.getIndex() + 1);
            return ch;
         } else {
            return -1;
         }
      }

      boolean skipFoldingWhiteSpace() {
         if (this.skipChar(' ')) {
            if (!this.peekFoldingWhiteSpace()) {
               return true;
            }

            this.pos.setIndex(this.pos.getIndex() - 1);
         } else if (!this.peekFoldingWhiteSpace()) {
            return false;
         }

         int startIndex = this.pos.getIndex();
         if (this.skipWhiteSpace()) {
            do {
               if (!this.skipNewline()) {
                  return true;
               }
            } while(this.skipWhiteSpace());

            this.pos.setIndex(startIndex);
            return false;
         } else if (this.skipNewline() && this.skipWhiteSpace()) {
            return true;
         } else {
            this.pos.setIndex(startIndex);
            return false;
         }
      }

      final boolean skipWhiteSpace() {
         int startIndex = this.pos.getIndex();

         while(this.skipAlternative(' ', '\t')) {
         }

         return this.pos.getIndex() > startIndex;
      }

      final boolean skipNewline() {
         return this.skipPair('\r', '\n');
      }

      final boolean skipAlternativeTriple(char firstStandard, char firstAlternative, char secondStandard, char secondAlternative, char thirdStandard, char thirdAlternative) {
         if (this.skipAlternativePair(firstStandard, firstAlternative, secondStandard, secondAlternative)) {
            if (this.skipAlternative(thirdStandard, thirdAlternative)) {
               return true;
            }

            this.pos.setIndex(this.pos.getIndex() - 2);
         }

         return false;
      }

      final boolean skipAlternativePair(char firstStandard, char firstAlternative, char secondStandard, char secondAlternative) {
         if (this.skipAlternative(firstStandard, firstAlternative)) {
            if (this.skipAlternative(secondStandard, secondAlternative)) {
               return true;
            }

            this.pos.setIndex(this.pos.getIndex() - 1);
         }

         return false;
      }

      final boolean skipAlternative(char standard, char alternative) {
         return this.skipChar(standard) || this.skipChar(alternative);
      }

      final boolean skipPair(char first, char second) {
         if (this.skipChar(first)) {
            if (this.skipChar(second)) {
               return true;
            }

            this.pos.setIndex(this.pos.getIndex() - 1);
         }

         return false;
      }

      final boolean skipChar(char ch) {
         if (this.pos.getIndex() < this.text.length() && this.text.charAt(this.pos.getIndex()) == ch) {
            this.pos.setIndex(this.pos.getIndex() + 1);
            return true;
         } else {
            return false;
         }
      }

      final boolean peekAsciiDigit() {
         return this.pos.getIndex() < this.text.length() && '0' <= this.text.charAt(this.pos.getIndex()) && this.text.charAt(this.pos.getIndex()) <= '9';
      }

      boolean peekFoldingWhiteSpace() {
         return this.pos.getIndex() < this.text.length() && (this.text.charAt(this.pos.getIndex()) == ' ' || this.text.charAt(this.pos.getIndex()) == '\t' || this.text.charAt(this.pos.getIndex()) == '\r');
      }

      final boolean peekChar(char ch) {
         return this.pos.getIndex() < this.text.length() && this.text.charAt(this.pos.getIndex()) == ch;
      }
   }
}
