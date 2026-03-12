package fr.xephi.authme.libs.org.apache.http.protocol;

import fr.xephi.authme.libs.org.apache.http.HttpRequest;

public interface HttpRequestHandlerMapper {
   HttpRequestHandler lookup(HttpRequest var1);
}
