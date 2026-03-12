package fr.xephi.authme.libs.org.apache.http;

import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

public interface HttpResponseFactory {
   HttpResponse newHttpResponse(ProtocolVersion var1, int var2, HttpContext var3);

   HttpResponse newHttpResponse(StatusLine var1, HttpContext var2);
}
