package fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl;

import com.sun.jna.platform.win32.Kernel32;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

public class NamedPipeSocket extends Socket {
   private final String host;
   private final String name;
   private RandomAccessFile file;
   private InputStream is;
   private OutputStream os;

   public NamedPipeSocket(String host, String name) {
      this.host = host;
      this.name = name;
   }

   public void close() throws IOException {
      if (this.file != null) {
         this.file.close();
         this.file = null;
      }

   }

   public void connect(SocketAddress endpoint, int timeout) throws IOException {
      String filename = String.format("\\\\%s\\pipe\\%s", this.host != null && !this.host.equals("localhost") ? this.host : ".", this.name);
      int usedTimeout = timeout == 0 ? 100 : timeout;
      long initialNano = System.nanoTime();

      while(true) {
         try {
            this.file = new RandomAccessFile(filename, "rw");
            break;
         } catch (FileNotFoundException var13) {
            try {
               Kernel32.INSTANCE.WaitNamedPipe(filename, timeout);
               this.file = new RandomAccessFile(filename, "rw");
            } catch (Throwable var12) {
               if (System.nanoTime() - initialNano > TimeUnit.MILLISECONDS.toNanos((long)usedTimeout)) {
                  if (timeout == 0) {
                     throw new FileNotFoundException(var13.getMessage() + "\nplease consider set connectTimeout option, so connection can retry having access to named pipe. \n(Named pipe can throw ERROR_PIPE_BUSY error)");
                  }

                  throw var13;
               }

               try {
                  TimeUnit.MILLISECONDS.sleep(5L);
               } catch (InterruptedException var11) {
                  IOException ioException = new IOException("Interruption during connection to named pipe");
                  ioException.initCause(var11);
                  throw ioException;
               }
            }
         }
      }

      this.is = new InputStream() {
         public int read(byte[] bytes, int off, int len) throws IOException {
            return NamedPipeSocket.this.file.read(bytes, off, len);
         }

         public int read() throws IOException {
            return NamedPipeSocket.this.file.read();
         }

         public int read(byte[] bytes) throws IOException {
            return NamedPipeSocket.this.file.read(bytes);
         }
      };
      this.os = new OutputStream() {
         public void write(byte[] bytes, int off, int len) throws IOException {
            NamedPipeSocket.this.file.write(bytes, off, len);
         }

         public void write(int value) {
         }
      };
   }

   public InputStream getInputStream() {
      return this.is;
   }

   public OutputStream getOutputStream() {
      return this.os;
   }

   public void setTcpNoDelay(boolean bool) {
   }

   public void setKeepAlive(boolean bool) {
   }

   public void setSoLinger(boolean bool, int value) {
   }

   public void setSoTimeout(int timeout) {
   }

   public void shutdownOutput() {
   }
}
