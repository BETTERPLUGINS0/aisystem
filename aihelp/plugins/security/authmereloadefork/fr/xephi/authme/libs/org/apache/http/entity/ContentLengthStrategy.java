package fr.xephi.authme.libs.org.apache.http.entity;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpMessage;

public interface ContentLengthStrategy {
   int IDENTITY = -1;
   int CHUNKED = -2;

   long determineLength(HttpMessage var1) throws HttpException;
}
