package com.nisovin.shopkeepers.util.csv;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.Lazy;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.Nullable;

public class CsvFormatter {
   private String fieldSeparator = ",";
   private String recordSeparator = "\n";
   private boolean recordSeparatorContainsNewline = true;
   private String quote = "\"";
   private Pattern quotePattern;
   private String escapedQuote;
   private boolean quoteAllFields;
   private boolean escapeNewlines;
   private boolean warnOnNewlines;
   private String nullField;

   private static String quoteReplacement(String replacement) {
      return Matcher.quoteReplacement(replacement);
   }

   public CsvFormatter() {
      this.quotePattern = Pattern.compile(this.quote, 16);
      this.escapedQuote = quoteReplacement("\"\"");
      this.quoteAllFields = true;
      this.escapeNewlines = true;
      this.warnOnNewlines = false;
      this.nullField = "";
   }

   public CsvFormatter fieldSeparator(String fieldSeparator) {
      Validate.notEmpty(fieldSeparator, "fieldSeparator is null or empty");
      this.fieldSeparator = fieldSeparator;
      return this;
   }

   public CsvFormatter recordSeparator(String recordSeparator) {
      Validate.notEmpty(recordSeparator, "recordSeparator is null or empty");
      this.recordSeparator = recordSeparator;
      this.recordSeparatorContainsNewline = StringUtils.containsNewline(recordSeparator);
      return this;
   }

   public CsvFormatter quote(String quote) {
      Validate.notNull(quote, (String)"quote is null");
      this.quote = quote;
      this.quotePattern = Pattern.compile(quote, 16);
      return this;
   }

   public CsvFormatter escapedQuote(String escapedQuote) {
      Validate.notNull(escapedQuote, (String)"escapedQuote is null");
      this.escapedQuote = quoteReplacement(escapedQuote);
      return this;
   }

   public CsvFormatter quoteAllFields() {
      return this.quoteAllFields(true);
   }

   public CsvFormatter quoteAllFields(boolean quoteAllFields) {
      this.quoteAllFields = quoteAllFields;
      return this;
   }

   public CsvFormatter escapeNewlines() {
      return this.escapeNewlines(true);
   }

   public CsvFormatter escapeNewlines(boolean escapeNewlines) {
      this.escapeNewlines = escapeNewlines;
      return this;
   }

   public CsvFormatter warnOnNewlines() {
      return this.warnOnNewlines(true);
   }

   public CsvFormatter warnOnNewlines(boolean warnOnNewlines) {
      this.warnOnNewlines = warnOnNewlines;
      return this;
   }

   public CsvFormatter nullField(String nullField) {
      Validate.notNull(nullField, (String)"nullField is null");
      this.nullField = nullField;
      return this;
   }

   public String formatFields(Object[] fields) {
      Validate.notNull(fields, (String)"fields is null");
      return this.formatFields(Stream.of(fields));
   }

   public String formatFields(Iterable<?> fields) {
      Validate.notNull(fields, (String)"fields is null");
      return this.formatFields(CollectionUtils.stream((Iterable)Unsafe.castNonNull(fields)));
   }

   private String formatFields(Stream<?> fields) {
      assert fields != null;

      return (String)fields.map(StringUtils::toStringOrNull).map(this::escapeField).collect(Collectors.joining(this.fieldSeparator));
   }

   public String formatRecord(Object[] fields) {
      String var10000 = this.formatFields(fields);
      return var10000 + this.recordSeparator;
   }

   public String formatRecord(Iterable<?> fields) {
      String var10000 = this.formatFields(fields);
      return var10000 + this.recordSeparator;
   }

   public String escapeField(@Nullable String field) {
      String nonNullField = field != null ? field : this.nullField;
      String escaped = nonNullField;
      Lazy<Boolean> containsNewline = new Lazy(() -> {
         return StringUtils.containsNewline(nonNullField);
      });
      if (this.warnOnNewlines && (Boolean)containsNewline.get()) {
         Log.warning("CSV field contains a newline character! " + nonNullField);
      }

      if (this.escapeNewlines && ((Boolean)containsNewline.get() || nonNullField.contains("\\"))) {
         escaped = StringUtils.escapeNewlinesAndBackslash(nonNullField);
      }

      if (!this.quote.isEmpty()) {
         boolean containsQuote = escaped.contains(this.quote);
         if (this.quoteAllFields || containsQuote || !this.escapeNewlines && (Boolean)containsNewline.get() || escaped.contains(this.fieldSeparator) || !this.recordSeparatorContainsNewline && escaped.contains(this.recordSeparator)) {
            if (containsQuote) {
               escaped = this.quotePattern.matcher(escaped).replaceAll(this.escapedQuote);
            }

            escaped = this.quote + escaped + this.quote;
         }
      }

      return escaped;
   }
}
