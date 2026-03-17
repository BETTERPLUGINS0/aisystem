package me.PM2.infinitevehicles.locales;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

class UTF8Control extends Control {
   public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) {
      String var6 = this.toBundleName(var1, var2);
      String var7 = this.toResourceName(var6, "properties");
      PropertyResourceBundle var8 = null;
      InputStream var9 = null;
      if (var5) {
         URL var10 = var4.getResource(var7);
         if (var10 != null) {
            URLConnection var11 = var10.openConnection();
            if (var11 != null) {
               var11.setUseCaches(false);
               var9 = var11.getInputStream();
            }
         }
      } else {
         var9 = var4.getResourceAsStream(var7);
      }

      if (var9 != null) {
         try {
            var8 = new PropertyResourceBundle(new InputStreamReader(var9, StandardCharsets.UTF_8));
         } finally {
            var9.close();
         }
      }

      return var8;
   }
}
