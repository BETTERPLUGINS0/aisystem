package fr.xephi.authme.libs.org.apache.http.protocol;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;

public interface HttpExpectationVerifier {
   void verify(HttpRequest var1, HttpResponse var2, HttpContext var3) throws HttpException;
}
