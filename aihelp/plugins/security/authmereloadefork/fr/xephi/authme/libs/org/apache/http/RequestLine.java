package fr.xephi.authme.libs.org.apache.http;

public interface RequestLine {
   String getMethod();

   ProtocolVersion getProtocolVersion();

   String getUri();
}
