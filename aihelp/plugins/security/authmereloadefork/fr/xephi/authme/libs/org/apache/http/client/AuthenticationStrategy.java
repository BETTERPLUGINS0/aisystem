package fr.xephi.authme.libs.org.apache.http.client;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.auth.AuthOption;
import fr.xephi.authme.libs.org.apache.http.auth.AuthScheme;
import fr.xephi.authme.libs.org.apache.http.auth.MalformedChallengeException;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.util.Map;
import java.util.Queue;

public interface AuthenticationStrategy {
   boolean isAuthenticationRequested(HttpHost var1, HttpResponse var2, HttpContext var3);

   Map<String, Header> getChallenges(HttpHost var1, HttpResponse var2, HttpContext var3) throws MalformedChallengeException;

   Queue<AuthOption> select(Map<String, Header> var1, HttpHost var2, HttpResponse var3, HttpContext var4) throws MalformedChallengeException;

   void authSucceeded(HttpHost var1, AuthScheme var2, HttpContext var3);

   void authFailed(HttpHost var1, AuthScheme var2, HttpContext var3);
}
