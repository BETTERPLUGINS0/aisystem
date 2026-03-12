package fr.xephi.authme.libs.org.postgresql.core;

import java.io.IOException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EncodingPredictor {
   private static final EncodingPredictor.Translation[] FATAL_TRANSLATIONS = new EncodingPredictor.Translation[]{new EncodingPredictor.Translation("ВАЖНО", (String[])null, "ru", new String[]{"WIN", "ALT", "KOI8"}), new EncodingPredictor.Translation("致命错误", (String[])null, "zh_CN", new String[]{"EUC_CN", "GBK", "BIG5"}), new EncodingPredictor.Translation("KATASTROFALNY", (String[])null, "pl", new String[]{"LATIN2"}), new EncodingPredictor.Translation("FATALE", (String[])null, "it", new String[]{"LATIN1", "LATIN9"}), new EncodingPredictor.Translation("FATAL", new String[]{"は存在しません", "ロール", "ユーザ"}, "ja", new String[]{"EUC_JP", "SJIS"}), new EncodingPredictor.Translation((String)null, (String[])null, "fr/de/es/pt_BR", new String[]{"LATIN1", "LATIN3", "LATIN4", "LATIN5", "LATIN7", "LATIN9"})};

   @Nullable
   public static EncodingPredictor.DecodeResult decode(byte[] bytes, int offset, int length) {
      Encoding defaultEncoding = Encoding.defaultEncoding();
      EncodingPredictor.Translation[] var4 = FATAL_TRANSLATIONS;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EncodingPredictor.Translation tr = var4[var6];
         String[] var8 = tr.encodings;
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            String encoding = var8[var10];
            Encoding encoder = Encoding.getDatabaseEncoding(encoding);
            if (encoder != defaultEncoding) {
               if (tr.fatalText != null) {
                  byte[] encoded;
                  try {
                     byte[] tmp = encoder.encode(tr.fatalText);
                     encoded = new byte[tmp.length + 2];
                     encoded[0] = 83;
                     encoded[encoded.length - 1] = 0;
                     System.arraycopy(tmp, 0, encoded, 1, tmp.length);
                  } catch (IOException var20) {
                     continue;
                  }

                  if (!arrayContains(bytes, offset, length, encoded, 0, encoded.length)) {
                     continue;
                  }
               }

               if (tr.texts != null) {
                  boolean foundOne = false;
                  String[] var23 = tr.texts;
                  int var15 = var23.length;

                  for(int var16 = 0; var16 < var15; ++var16) {
                     String text = var23[var16];

                     try {
                        byte[] textBytes = encoder.encode(text);
                        if (arrayContains(bytes, offset, length, textBytes, 0, textBytes.length)) {
                           foundOne = true;
                           break;
                        }
                     } catch (IOException var21) {
                     }
                  }

                  if (!foundOne) {
                     continue;
                  }
               }

               try {
                  String decoded = encoder.decode(bytes, offset, length);
                  if (decoded.indexOf(65533) == -1) {
                     return new EncodingPredictor.DecodeResult(decoded, encoder.name());
                  }
               } catch (IOException var19) {
               }
            }
         }
      }

      return null;
   }

   private static boolean arrayContains(byte[] first, int firstOffset, int firstLength, byte[] second, int secondOffset, int secondLength) {
      if (firstLength < secondLength) {
         return false;
      } else {
         for(int i = 0; i < firstLength; ++i) {
            while(i < firstLength && first[firstOffset + i] != second[secondOffset]) {
               ++i;
            }

            int j;
            for(j = 1; j < secondLength && first[firstOffset + i + j] == second[secondOffset + j]; ++j) {
            }

            if (j == secondLength) {
               return true;
            }
         }

         return false;
      }
   }

   static class Translation {
      @Nullable
      public final String fatalText;
      @Nullable
      private final String[] texts;
      public final String language;
      public final String[] encodings;

      Translation(@Nullable String fatalText, @Nullable String[] texts, String language, String... encodings) {
         this.fatalText = fatalText;
         this.texts = texts;
         this.language = language;
         this.encodings = encodings;
      }
   }

   public static class DecodeResult {
      public final String result;
      @Nullable
      public final String encoding;

      DecodeResult(String result, @Nullable String encoding) {
         this.result = result;
         this.encoding = encoding;
      }
   }
}
