package advancedplugins.pm2.cv.models.api.utils.data;

import java.util.Locale;

public class ResourceLocation {
   private final String namespace;
   private final String path;

   public ResourceLocation(String var1) {
      var1 = var1.toLowerCase(Locale.ENGLISH);
      String[] var2 = var1.split(":", 2);
      if (var2.length <= 1) {
         this.namespace = "minecraft";
         this.path = var1;
      } else {
         this.namespace = var2[0];
         this.path = var2[1];
      }

   }

   public ResourceLocation(String var1, String var2) {
      this.namespace = var1;
      this.path = var2;
   }

   public static boolean isValidPath(String var0) {
      for(int var1 = 0; var1 < var0.length(); ++var1) {
         if (!validPathChar(var0.charAt(var1))) {
            return false;
         }
      }

      return true;
   }

   public static boolean isValidNamespace(String var0) {
      for(int var1 = 0; var1 < var0.length(); ++var1) {
         if (!validNamespaceChar(var0.charAt(var1))) {
            return false;
         }
      }

      return true;
   }

   private static boolean validPathChar(char var0) {
      return var0 == '_' || var0 == '-' || var0 >= 'a' && var0 <= 'z' || var0 >= '0' && var0 <= '9' || var0 == '/' || var0 == '.';
   }

   private static boolean validNamespaceChar(char var0) {
      return var0 == '_' || var0 == '-' || var0 >= 'a' && var0 <= 'z' || var0 >= '0' && var0 <= '9' || var0 == '.';
   }

   public String toString() {
      return !this.namespace.isBlank() && !"minecraft".equals(this.namespace) ? this.namespace + ":" + this.path : this.path;
   }

   public boolean isValid() {
      return isValidNamespace(this.namespace) && isValidPath(this.path);
   }

   public String getNamespace() {
      return this.namespace;
   }

   public String getPath() {
      return this.path;
   }
}
