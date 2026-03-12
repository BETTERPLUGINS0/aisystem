package fr.xephi.authme.libs.org.apache.http.io;

public interface HttpTransportMetrics {
   long getBytesTransferred();

   void reset();
}
