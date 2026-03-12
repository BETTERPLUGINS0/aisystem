package fr.xephi.authme.libs.org.apache.http.client;

import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.io.IOException;

public interface HttpRequestRetryHandler {
   boolean retryRequest(IOException var1, int var2, HttpContext var3);
}
