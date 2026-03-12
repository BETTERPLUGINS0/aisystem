package fr.xephi.authme.libs.org.apache.http.client;

import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.ProtocolException;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.net.URI;

/** @deprecated */
@Deprecated
public interface RedirectHandler {
   boolean isRedirectRequested(HttpResponse var1, HttpContext var2);

   URI getLocationURI(HttpResponse var1, HttpContext var2) throws ProtocolException;
}
