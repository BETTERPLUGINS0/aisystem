package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.StatusLine;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.HttpResponseException;
import fr.xephi.authme.libs.org.apache.http.client.ResponseHandler;
import fr.xephi.authme.libs.org.apache.http.util.EntityUtils;
import java.io.IOException;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public abstract class AbstractResponseHandler<T> implements ResponseHandler<T> {
   public T handleResponse(HttpResponse response) throws HttpResponseException, IOException {
      StatusLine statusLine = response.getStatusLine();
      HttpEntity entity = response.getEntity();
      if (statusLine.getStatusCode() >= 300) {
         EntityUtils.consume(entity);
         throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
      } else {
         return entity == null ? null : this.handleEntity(entity);
      }
   }

   public abstract T handleEntity(HttpEntity var1) throws IOException;
}
