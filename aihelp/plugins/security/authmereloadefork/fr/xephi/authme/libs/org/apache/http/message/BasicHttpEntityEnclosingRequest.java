package fr.xephi.authme.libs.org.apache.http.message;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.HttpEntityEnclosingRequest;
import fr.xephi.authme.libs.org.apache.http.ProtocolVersion;
import fr.xephi.authme.libs.org.apache.http.RequestLine;

public class BasicHttpEntityEnclosingRequest extends BasicHttpRequest implements HttpEntityEnclosingRequest {
   private HttpEntity entity;

   public BasicHttpEntityEnclosingRequest(String method, String uri) {
      super(method, uri);
   }

   public BasicHttpEntityEnclosingRequest(String method, String uri, ProtocolVersion ver) {
      super(method, uri, ver);
   }

   public BasicHttpEntityEnclosingRequest(RequestLine requestline) {
      super(requestline);
   }

   public HttpEntity getEntity() {
      return this.entity;
   }

   public void setEntity(HttpEntity entity) {
      this.entity = entity;
   }

   public boolean expectContinue() {
      Header expect = this.getFirstHeader("Expect");
      return expect != null && "100-continue".equalsIgnoreCase(expect.getValue());
   }
}
