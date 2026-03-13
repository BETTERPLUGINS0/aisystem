package com.volmit.iris.util.slimjar.resolver.pinger;

import com.volmit.iris.util.slimjar.logging.LogDispatcher;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public final class HttpURLPinger implements URLPinger {
   @NotNull
   private static final ProcessLogger LOGGER = LogDispatcher.getMediatingLogger();
   @NotNull
   private static final String SLIMJAR_USER_AGENT = "SlimjarApplication/* URL Validation Ping";
   @NotNull
   private static final Collection<String> SUPPORTED_PROTOCOLS = Arrays.asList("HTTP", "HTTPS");

   public boolean ping(@NotNull URL var1) {
      String var2 = var1.toString();
      LOGGER.debug("Pinging %s", var2);
      if (!this.isSupported(var1)) {
         LOGGER.error("Unsupported protocol for %s", var2);
         return false;
      } else {
         HttpURLConnection var3 = null;

         try {
            var3 = (HttpURLConnection)var1.openConnection();
            var3.addRequestProperty("User-Agent", "SlimjarApplication/* URL Validation Ping");
            var3.connect();
            boolean var4 = var3.getResponseCode() == 200;
            LOGGER.debug("Ping %s for %s", var4 ? "successful" : "failed", var2);
            boolean var5 = var4;
            return var5;
         } catch (IOException var9) {
            LOGGER.error("Ping failed for %s", var2);
         } finally {
            if (var3 != null) {
               var3.disconnect();
            }

         }

         return false;
      }
   }

   public boolean isSupported(@NotNull URL var1) {
      String var2 = var1.getProtocol().toUpperCase(Locale.ENGLISH);
      return SUPPORTED_PROTOCOLS.contains(var2);
   }
}
