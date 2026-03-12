package fr.xephi.authme.libs.org.apache.http.client;

import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import java.io.IOException;

public interface ResponseHandler<T> {
   T handleResponse(HttpResponse var1) throws ClientProtocolException, IOException;
}
