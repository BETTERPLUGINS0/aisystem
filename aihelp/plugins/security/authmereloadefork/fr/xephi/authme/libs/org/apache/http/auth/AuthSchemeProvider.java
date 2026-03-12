package fr.xephi.authme.libs.org.apache.http.auth;

import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

public interface AuthSchemeProvider {
   AuthScheme create(HttpContext var1);
}
