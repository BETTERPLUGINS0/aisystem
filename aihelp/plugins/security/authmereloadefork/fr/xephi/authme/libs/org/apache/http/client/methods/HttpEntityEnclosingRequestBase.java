package fr.xephi.authme.libs.org.apache.http.client.methods;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.HttpEntityEnclosingRequest;
import fr.xephi.authme.libs.org.apache.http.client.utils.CloneUtils;

public abstract class HttpEntityEnclosingRequestBase extends HttpRequestBase implements HttpEntityEnclosingRequest {
   private HttpEntity entity;

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

   public Object clone() throws CloneNotSupportedException {
      HttpEntityEnclosingRequestBase clone = (HttpEntityEnclosingRequestBase)super.clone();
      if (this.entity != null) {
         clone.entity = (HttpEntity)CloneUtils.cloneObject(this.entity);
      }

      return clone;
   }
}
