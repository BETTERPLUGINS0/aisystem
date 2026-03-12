package fr.xephi.authme.libs.org.postgresql.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;

public abstract class WrappedFactory extends SSLSocketFactory {
   protected SSLSocketFactory factory;

   public Socket createSocket(InetAddress host, int port) throws IOException {
      return this.factory.createSocket(host, port);
   }

   public Socket createSocket(String host, int port) throws IOException {
      return this.factory.createSocket(host, port);
   }

   public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
      return this.factory.createSocket(host, port, localHost, localPort);
   }

   public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
      return this.factory.createSocket(address, port, localAddress, localPort);
   }

   public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
      return this.factory.createSocket(socket, host, port, autoClose);
   }

   public String[] getDefaultCipherSuites() {
      return this.factory.getDefaultCipherSuites();
   }

   public String[] getSupportedCipherSuites() {
      return this.factory.getSupportedCipherSuites();
   }
}
