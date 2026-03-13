package org.apache.commons.io;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

public class Charsets {
   private static final SortedMap<String, Charset> STANDARD_CHARSET_MAP;
   /** @deprecated */
   @Deprecated
   public static final Charset ISO_8859_1;
   /** @deprecated */
   @Deprecated
   public static final Charset US_ASCII;
   /** @deprecated */
   @Deprecated
   public static final Charset UTF_16;
   /** @deprecated */
   @Deprecated
   public static final Charset UTF_16BE;
   /** @deprecated */
   @Deprecated
   public static final Charset UTF_16LE;
   /** @deprecated */
   @Deprecated
   public static final Charset UTF_8;

   public static SortedMap<String, Charset> requiredCharsets() {
      return STANDARD_CHARSET_MAP;
   }

   public static Charset toCharset(Charset var0) {
      return var0 == null ? Charset.defaultCharset() : var0;
   }

   public static Charset toCharset(String var0) {
      return var0 == null ? Charset.defaultCharset() : Charset.forName(var0);
   }

   static {
      TreeMap var0 = new TreeMap(String.CASE_INSENSITIVE_ORDER);
      var0.put(StandardCharsets.ISO_8859_1.name(), StandardCharsets.ISO_8859_1);
      var0.put(StandardCharsets.US_ASCII.name(), StandardCharsets.US_ASCII);
      var0.put(StandardCharsets.UTF_16.name(), StandardCharsets.UTF_16);
      var0.put(StandardCharsets.UTF_16BE.name(), StandardCharsets.UTF_16BE);
      var0.put(StandardCharsets.UTF_16LE.name(), StandardCharsets.UTF_16LE);
      var0.put(StandardCharsets.UTF_8.name(), StandardCharsets.UTF_8);
      STANDARD_CHARSET_MAP = Collections.unmodifiableSortedMap(var0);
      ISO_8859_1 = StandardCharsets.ISO_8859_1;
      US_ASCII = StandardCharsets.US_ASCII;
      UTF_16 = StandardCharsets.UTF_16;
      UTF_16BE = StandardCharsets.UTF_16BE;
      UTF_16LE = StandardCharsets.UTF_16LE;
      UTF_8 = StandardCharsets.UTF_8;
   }
}
