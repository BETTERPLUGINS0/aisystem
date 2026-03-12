package fr.xephi.authme.libs.org.apache.http.client;

import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.auth.AuthScheme;

public interface AuthCache {
   void put(HttpHost var1, AuthScheme var2);

   AuthScheme get(HttpHost var1);

   void remove(HttpHost var1);

   void clear();
}
