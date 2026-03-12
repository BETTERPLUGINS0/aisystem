package fr.xephi.authme.libs.org.apache.http.auth;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

public interface ContextAwareAuthScheme extends AuthScheme {
   Header authenticate(Credentials var1, HttpRequest var2, HttpContext var3) throws AuthenticationException;
}
