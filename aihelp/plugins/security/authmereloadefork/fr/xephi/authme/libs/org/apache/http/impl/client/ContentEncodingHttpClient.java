package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.protocol.RequestAcceptEncoding;
import fr.xephi.authme.libs.org.apache.http.client.protocol.ResponseContentEncoding;
import fr.xephi.authme.libs.org.apache.http.conn.ClientConnectionManager;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.BasicHttpProcessor;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.SAFE_CONDITIONAL
)
public class ContentEncodingHttpClient extends DefaultHttpClient {
   public ContentEncodingHttpClient(ClientConnectionManager conman, HttpParams params) {
      super(conman, params);
   }

   public ContentEncodingHttpClient(HttpParams params) {
      this((ClientConnectionManager)null, params);
   }

   public ContentEncodingHttpClient() {
      this((HttpParams)null);
   }

   protected BasicHttpProcessor createHttpProcessor() {
      BasicHttpProcessor result = super.createHttpProcessor();
      result.addRequestInterceptor(new RequestAcceptEncoding());
      result.addResponseInterceptor(new ResponseContentEncoding());
      return result;
   }
}
