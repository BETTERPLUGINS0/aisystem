package fr.xephi.authme.libs.org.apache.http;

import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.io.IOException;

public interface HttpResponseInterceptor {
   void process(HttpResponse var1, HttpContext var2) throws HttpException, IOException;
}
