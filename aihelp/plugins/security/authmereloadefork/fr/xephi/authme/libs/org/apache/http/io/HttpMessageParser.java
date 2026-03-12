package fr.xephi.authme.libs.org.apache.http.io;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpMessage;
import java.io.IOException;

public interface HttpMessageParser<T extends HttpMessage> {
   T parse() throws IOException, HttpException;
}
