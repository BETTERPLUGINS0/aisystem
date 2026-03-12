package fr.xephi.authme.libs.waffle.util;

import fr.xephi.authme.libs.org.slf4j.Logger;
import fr.xephi.authme.libs.org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public final class CorsPreFlightCheck {
   private static final Logger LOGGER = LoggerFactory.getLogger(CorsPreFlightCheck.class);
   private static final String PRE_FLIGHT_ATTRIBUTE_VALUE = "PRE_FLIGHT";
   private static final List<String> CORS_PRE_FLIGHT_HEADERS = new ArrayList(Arrays.asList("Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin"));

   private CorsPreFlightCheck() {
   }

   public static boolean isPreflight(HttpServletRequest request) {
      String corsRequestType = (String)request.getAttribute("cors.request.type");
      LOGGER.debug("[waffle.util.CorsPreflightCheck] Request is CORS preflight; continue filter chain");
      String method = request.getMethod();
      if (method != null && method.equalsIgnoreCase("OPTIONS")) {
         LOGGER.debug("[waffle.util.CorsPreflightCheck] check for PRE_FLIGHT Attribute");
         if (corsRequestType != null && corsRequestType.equalsIgnoreCase("PRE_FLIGHT")) {
            return true;
         } else {
            LOGGER.debug("[waffle.util.CorsPreflightCheck] check headers");
            Iterator var3 = CORS_PRE_FLIGHT_HEADERS.iterator();

            String headerValue;
            do {
               if (!var3.hasNext()) {
                  LOGGER.debug("[waffle.util.CorsPreflightCheck] is preflight");
                  return true;
               }

               String header = (String)var3.next();
               headerValue = request.getHeader(header);
               LOGGER.debug((String)"[waffle.util.CorsPreflightCheck] {}", (Object)header);
            } while(headerValue != null);

            return false;
         }
      } else {
         return false;
      }
   }
}
