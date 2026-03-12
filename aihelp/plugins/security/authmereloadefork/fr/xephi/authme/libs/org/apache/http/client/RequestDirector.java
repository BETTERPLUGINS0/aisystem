package fr.xephi.authme.libs.org.apache.http.client;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.io.IOException;

/** @deprecated */
@Deprecated
public interface RequestDirector {
   HttpResponse execute(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException, IOException;
}
