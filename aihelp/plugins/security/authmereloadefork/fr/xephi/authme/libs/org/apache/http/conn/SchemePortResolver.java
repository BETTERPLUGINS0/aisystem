package fr.xephi.authme.libs.org.apache.http.conn;

import fr.xephi.authme.libs.org.apache.http.HttpHost;

public interface SchemePortResolver {
   int resolve(HttpHost var1) throws UnsupportedSchemeException;
}
