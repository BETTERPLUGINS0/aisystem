package fr.xephi.authme.libs.org.apache.http.impl.execchain;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;

public class TunnelRefusedException extends HttpException {
   private static final long serialVersionUID = -8646722842745617323L;
   private final HttpResponse response;

   public TunnelRefusedException(String message, HttpResponse response) {
      super(message);
      this.response = response;
   }

   public HttpResponse getResponse() {
      return this.response;
   }
}
