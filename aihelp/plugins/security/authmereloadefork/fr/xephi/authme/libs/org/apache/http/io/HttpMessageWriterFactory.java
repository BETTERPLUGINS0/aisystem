package fr.xephi.authme.libs.org.apache.http.io;

import fr.xephi.authme.libs.org.apache.http.HttpMessage;

public interface HttpMessageWriterFactory<T extends HttpMessage> {
   HttpMessageWriter<T> create(SessionOutputBuffer var1);
}
