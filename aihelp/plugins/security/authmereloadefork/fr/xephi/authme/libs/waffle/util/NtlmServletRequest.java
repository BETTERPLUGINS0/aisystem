package fr.xephi.authme.libs.waffle.util;

import javax.servlet.http.HttpServletRequest;

public final class NtlmServletRequest {
   private NtlmServletRequest() {
   }

   public static String getConnectionId(HttpServletRequest request) {
      String remoteHost = getRemoteHost(request);
      return String.join(":", remoteHost == null ? "" : remoteHost, String.valueOf(request.getRemotePort()));
   }

   private static String getRemoteHost(HttpServletRequest request) {
      return request.getRemoteHost() == null ? request.getRemoteAddr() : request.getRemoteHost();
   }
}
