package fr.xephi.authme.libs.org.postgresql.util;

import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGPropertyMaxResultBufferParser {
   private static final Logger LOGGER = Logger.getLogger(PGPropertyMaxResultBufferParser.class.getName());
   private static final String[] PERCENT_PHRASES = new String[]{"p", "pct", "percent"};

   public static long parseProperty(@Nullable String value) throws PSQLException {
      long result = -1L;
      if (value != null) {
         if (checkIfValueContainsPercent(value)) {
            result = parseBytePercentValue(value);
         } else if (!value.isEmpty()) {
            result = parseByteValue(value);
         }
      }

      result = adjustResultSize(result);
      return result;
   }

   private static boolean checkIfValueContainsPercent(String value) {
      return getPercentPhraseLengthIfContains(value) != -1;
   }

   private static long parseBytePercentValue(String value) throws PSQLException {
      long result = -1L;
      if (!value.isEmpty()) {
         int length = getPercentPhraseLengthIfContains(value);
         if (length == -1) {
            throwExceptionAboutParsingError("Received MaxResultBuffer parameter can't be parsed. Value received to parse: {0}", value);
         }

         result = calculatePercentOfMemory(value, length);
      }

      return result;
   }

   private static int getPercentPhraseLengthIfContains(String valueToCheck) {
      int result = -1;
      String[] var2 = PERCENT_PHRASES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String phrase = var2[var4];
         int indx = getPhraseLengthIfContains(valueToCheck, phrase);
         if (indx != -1) {
            result = indx;
         }
      }

      return result;
   }

   private static int getPhraseLengthIfContains(String valueToCheck, String phrase) {
      int searchValueLength = phrase.length();
      if (valueToCheck.length() > searchValueLength) {
         String subValue = valueToCheck.substring(valueToCheck.length() - searchValueLength);
         if (subValue.equals(phrase)) {
            return searchValueLength;
         }
      }

      return -1;
   }

   private static long calculatePercentOfMemory(String value, int percentPhraseLength) {
      String realValue = value.substring(0, value.length() - percentPhraseLength);
      double percent = Double.parseDouble(realValue) / 100.0D;
      return (long)(percent * (double)ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax());
   }

   private static long parseByteValue(String value) throws PSQLException {
      long result = -1L;
      long multiplier = 1L;
      long mul = 1000L;
      char sign = value.charAt(value.length() - 1);
      switch(sign) {
      case '%':
         return result;
      case 'T':
      case 't':
         multiplier *= mul;
      case 'G':
      case 'g':
         multiplier *= mul;
      case 'M':
      case 'm':
         multiplier *= mul;
      case 'K':
      case 'k':
         multiplier *= mul;
         String realValue = value.substring(0, value.length() - 1);
         result = (long)Integer.parseInt(realValue) * multiplier;
         break;
      default:
         if (sign >= '0' && sign <= '9') {
            result = Long.parseLong(value);
         } else {
            throwExceptionAboutParsingError("Received MaxResultBuffer parameter can't be parsed. Value received to parse: {0}", value);
         }
      }

      return result;
   }

   private static long adjustResultSize(long value) {
      if ((double)value > 0.9D * (double)ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax()) {
         long newResult = (long)(0.9D * (double)ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax());
         LOGGER.log(Level.WARNING, GT.tr("WARNING! Required to allocate {0} bytes, which exceeded possible heap memory size. Assigned {1} bytes as limit.", String.valueOf(value), String.valueOf(newResult)));
         value = newResult;
      }

      return value;
   }

   private static void throwExceptionAboutParsingError(String message, Object... values) throws PSQLException {
      throw new PSQLException(GT.tr(message, values), PSQLState.SYNTAX_ERROR);
   }
}
