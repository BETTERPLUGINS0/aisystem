package github.nighter.smartspawner.libs.mariadb.util;

public final class Security {
   public static String parseSessionVariables(String sessionVariable) {
      StringBuilder out = new StringBuilder();
      StringBuilder sb = new StringBuilder();
      Security.Parse state = Security.Parse.Normal;
      boolean iskey = true;
      boolean singleQuotes = true;
      boolean first = true;
      String key = null;
      char[] chars = sessionVariable.toCharArray();
      char[] var9 = chars;
      int var10 = chars.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         char car = var9[var11];
         if (state == Security.Parse.Escape) {
            sb.append(car);
            state = Security.Parse.String;
         } else {
            switch(car) {
            case '"':
               if (state == Security.Parse.Normal) {
                  state = Security.Parse.String;
                  singleQuotes = false;
               } else if (!singleQuotes) {
                  state = Security.Parse.Normal;
               }
               break;
            case '\'':
               if (state == Security.Parse.Normal) {
                  state = Security.Parse.String;
                  singleQuotes = true;
               } else if (singleQuotes) {
                  state = Security.Parse.Normal;
               }
               break;
            case ',':
            case ';':
               if (state == Security.Parse.Normal) {
                  if (!iskey) {
                     if (!first) {
                        out.append(",");
                     }

                     out.append(key);
                     out.append(sb);
                     first = false;
                  } else {
                     key = sb.toString().trim();
                     if (!key.isEmpty()) {
                        if (!first) {
                           out.append(",");
                        }

                        out.append(key);
                        first = false;
                     }
                  }

                  iskey = true;
                  key = null;
                  sb = new StringBuilder();
                  continue;
               }
               break;
            case '=':
               if (state == Security.Parse.Normal && iskey) {
                  key = sb.toString().trim();
                  iskey = false;
                  sb = new StringBuilder();
               }
               break;
            case '\\':
               if (state == Security.Parse.String) {
                  state = Security.Parse.Escape;
               }
            }

            sb.append(car);
         }
      }

      if (!iskey) {
         if (!first) {
            out.append(",");
         }

         out.append(key);
         out.append(sb);
      } else {
         String tmpkey = sb.toString().trim();
         if (!tmpkey.isEmpty() && !first) {
            out.append(",");
         }

         out.append(tmpkey);
      }

      return out.toString();
   }

   private static enum Parse {
      Normal,
      String,
      Escape;

      // $FF: synthetic method
      private static Security.Parse[] $values() {
         return new Security.Parse[]{Normal, String, Escape};
      }
   }
}
