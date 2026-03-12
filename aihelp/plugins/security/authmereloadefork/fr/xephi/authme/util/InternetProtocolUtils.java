package fr.xephi.authme.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class InternetProtocolUtils {
   private InternetProtocolUtils() {
   }

   public static boolean isLocalAddress(String address) {
      try {
         InetAddress inetAddress = InetAddress.getByName(address);
         return isLoopbackAddress(address) || inetAddress.isSiteLocalAddress() || inetAddress.isLinkLocalAddress() || isIPv6UniqueSiteLocal(inetAddress);
      } catch (UnknownHostException var2) {
         return false;
      }
   }

   public static boolean isLoopbackAddress(String address) {
      try {
         InetAddress inetAddress = InetAddress.getByName(address);
         return inetAddress.isLoopbackAddress();
      } catch (UnknownHostException var2) {
         return false;
      }
   }

   private static boolean isLoopbackAddress(InetAddress address) {
      return address.isLoopbackAddress();
   }

   private static boolean isIPv6UniqueSiteLocal(InetAddress address) {
      return (address.getAddress()[0] & 255) == 252 || (address.getAddress()[0] & 255) == 253;
   }
}
