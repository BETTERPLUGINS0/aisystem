package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.HttpResponseException;
import fr.xephi.authme.libs.org.apache.http.util.EntityUtils;
import java.io.IOException;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class BasicResponseHandler extends AbstractResponseHandler<String> {
   public String handleEntity(HttpEntity entity) throws IOException {
      return EntityUtils.toString(entity);
   }

   public String handleResponse(HttpResponse response) throws HttpResponseException, IOException {
      return (String)super.handleResponse(response);
   }
}
