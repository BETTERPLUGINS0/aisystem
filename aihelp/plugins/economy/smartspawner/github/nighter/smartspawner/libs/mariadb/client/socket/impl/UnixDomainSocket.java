package github.nighter.smartspawner.libs.mariadb.client.socket.impl;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Structure;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class UnixDomainSocket extends Socket {
   private static final int AF_UNIX = 1;
   private static final int SOCK_STREAM = 1;
   private static final int PROTOCOL = 0;
   private final AtomicBoolean closeLock = new AtomicBoolean();
   private final UnixDomainSocket.SockAddr sockaddr;
   private final int fd;
   private InputStream is;
   private OutputStream os;
   private boolean connected;

   public UnixDomainSocket(String path) throws IOException {
      if (Platform.isWindows()) {
         throw new IOException("Unix domain sockets are not supported on Windows");
      } else {
         this.sockaddr = new UnixDomainSocket.SockAddr(path);
         this.closeLock.set(false);

         try {
            this.fd = socket(1, 1, 0);
         } catch (LastErrorException var3) {
            throw new IOException("native socket() failed : " + formatError(var3));
         }
      }
   }

   public static native int socket(int var0, int var1, int var2) throws LastErrorException;

   public static native int connect(int var0, UnixDomainSocket.SockAddr var1, int var2) throws LastErrorException;

   public static native int recv(int var0, byte[] var1, int var2, int var3) throws LastErrorException;

   public static native int send(int var0, byte[] var1, int var2, int var3) throws LastErrorException;

   public static native int close(int var0) throws LastErrorException;

   public static native String strerror(int var0);

   private static String formatError(LastErrorException lee) {
      try {
         return strerror(lee.getErrorCode());
      } catch (Throwable var2) {
         return lee.getMessage();
      }
   }

   public boolean isConnected() {
      return this.connected;
   }

   public void close() throws IOException {
      if (!this.closeLock.getAndSet(true)) {
         try {
            close(this.fd);
         } catch (LastErrorException var2) {
            throw new IOException("native close() failed : " + formatError(var2));
         }

         this.connected = false;
      }

   }

   public void connect(SocketAddress endpoint, int timeout) throws IOException {
      try {
         int ret = connect(this.fd, this.sockaddr, this.sockaddr.size());
         if (ret != 0) {
            throw new IOException(strerror(Native.getLastError()));
         }

         this.connected = true;
      } catch (LastErrorException var6) {
         try {
            this.close();
         } catch (IOException var5) {
         }

         throw new IOException("native connect() failed : " + formatError(var6));
      }

      this.is = new UnixDomainSocket.UnixSocketInputStream();
      this.os = new UnixDomainSocket.UnixSocketOutputStream();
   }

   public InputStream getInputStream() {
      return this.is;
   }

   public OutputStream getOutputStream() {
      return this.os;
   }

   public void setTcpNoDelay(boolean b) {
   }

   public void setKeepAlive(boolean b) {
   }

   public void setSoLinger(boolean b, int i) {
   }

   public void setSoTimeout(int timeout) {
   }

   public void shutdownInput() {
   }

   public void shutdownOutput() {
   }

   static {
      if (!Platform.isWindows() && !Platform.isWindowsCE()) {
         Native.register("c");
      }

   }

   public static class SockAddr extends Structure {
      public short sun_family = 1;
      public byte[] sun_path;

      public SockAddr(String sunPath) {
         byte[] arr = sunPath.getBytes();
         this.sun_path = new byte[arr.length + 1];
         System.arraycopy(arr, 0, this.sun_path, 0, Math.min(this.sun_path.length - 1, arr.length));
         this.allocateMemory();
      }

      protected List<String> getFieldOrder() {
         return Arrays.asList("sun_family", "sun_path");
      }
   }

   class UnixSocketInputStream extends InputStream {
      public int read(byte[] bytesEntry, int off, int len) throws IOException {
         try {
            if (off == 0) {
               return UnixDomainSocket.recv(UnixDomainSocket.this.fd, bytesEntry, len, 0);
            } else {
               byte[] tmp = new byte[len];
               int reads = UnixDomainSocket.recv(UnixDomainSocket.this.fd, tmp, len, 0);
               System.arraycopy(tmp, 0, bytesEntry, off, reads);
               return reads;
            }
         } catch (LastErrorException var6) {
            throw new IOException("native read() failed : " + UnixDomainSocket.formatError(var6));
         }
      }

      public int read() throws IOException {
         byte[] bytes = new byte[1];
         int bytesRead = this.read(bytes);
         return bytesRead == 0 ? -1 : bytes[0] & 255;
      }

      public int read(byte[] bytes) throws IOException {
         return this.read(bytes, 0, bytes.length);
      }
   }

   class UnixSocketOutputStream extends OutputStream {
      public void write(byte[] bytesEntry, int off, int len) throws IOException {
         try {
            int bytes = UnixDomainSocket.send(UnixDomainSocket.this.fd, bytesEntry, len, 0);
            if (bytes != len) {
               throw new IOException("can't write " + len + "bytes");
            }
         } catch (LastErrorException var6) {
            throw new IOException("native write() failed : " + UnixDomainSocket.formatError(var6));
         }
      }

      public void write(int value) throws IOException {
      }
   }
}
