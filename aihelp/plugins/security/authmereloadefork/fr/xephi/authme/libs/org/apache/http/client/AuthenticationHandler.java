package fr.xephi.authme.libs.org.apache.http.client;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.auth.AuthScheme;
import fr.xephi.authme.libs.org.apache.http.auth.AuthenticationException;
import fr.xephi.authme.libs.org.apache.http.auth.MalformedChallengeException;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.util.Map;

/** @deprecated */
@Deprecated
public interface AuthenticationHandler {
   boolean isAuthenticationRequested(HttpResponse var1, HttpContext var2);

   Map<String, Header> getChallenges(HttpResponse var1, HttpContext var2) throws MalformedChallengeException;

   AuthScheme selectScheme(Map<String, Header> var1, HttpResponse var2, HttpContext var3) throws AuthenticationException;
}
