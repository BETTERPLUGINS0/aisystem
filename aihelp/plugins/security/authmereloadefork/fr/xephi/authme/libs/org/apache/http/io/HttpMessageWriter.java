package fr.xephi.authme.libs.org.apache.http.io;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpMessage;
import java.io.IOException;

public interface HttpMessageWriter<T extends HttpMessage> {
   void write(T var1) throws IOException, HttpException;
}
