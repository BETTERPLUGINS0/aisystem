package fr.xephi.authme.libs.org.apache.http.client;

import fr.xephi.authme.libs.org.apache.http.auth.AuthScope;
import fr.xephi.authme.libs.org.apache.http.auth.Credentials;

public interface CredentialsProvider {
   void setCredentials(AuthScope var1, Credentials var2);

   Credentials getCredentials(AuthScope var1);

   void clear();
}
