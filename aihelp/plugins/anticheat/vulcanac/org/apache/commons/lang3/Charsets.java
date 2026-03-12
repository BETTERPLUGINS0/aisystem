package org.apache.commons.lang3;

import java.nio.charset.Charset;

class Charsets {
   static Charset toCharset(Charset var0) {
      return var0 == null ? Charset.defaultCharset() : var0;
   }

   static Charset toCharset(String var0) {
      return var0 == null ? Charset.defaultCharset() : Charset.forName(var0);
   }

   static String toCharsetName(String var0) {
      return var0 == null ? Charset.defaultCharset().name() : var0;
   }
}
