package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.gss.GSSInputStream;
import fr.xephi.authme.libs.org.postgresql.gss.GSSOutputStream;
import fr.xephi.authme.libs.org.postgresql.util.ByteStreamWriter;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.HostSpec;
import fr.xephi.authme.libs.org.postgresql.util.PGPropertyMaxResultBufferParser;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.FilterOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import javax.net.SocketFactory;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.MessageProp;

public class PGStream implements Closeable, Flushable {
   private final SocketFactory socketFactory;
   private final HostSpec hostSpec;
   private final byte[] int4Buf;
   private final byte[] int2Buf;
   private Socket connection;
   private VisibleBufferedInputStream pgInput;
   private OutputStream pgOutput;
   @Nullable
   private byte[] streamBuffer;
   boolean gssEncrypted;
   private long nextStreamAvailableCheckTime;
   private int minStreamAvailableCheckDelay;
   private Encoding encoding;
   private Writer encodingWriter;
   private long maxResultBuffer;
   private long resultBufferByteCount;
   private int maxRowSizeBytes;

   public boolean isGssEncrypted() {
      return this.gssEncrypted;
   }

   public void setSecContext(GSSContext secContext) {
      MessageProp messageProp = new MessageProp(0, true);
      this.pgInput = new VisibleBufferedInputStream(new GSSInputStream(this.pgInput.getWrapped(), secContext, messageProp), 8192);
      this.pgOutput = new GSSOutputStream(this.pgOutput, secContext, messageProp, 16384);
      this.gssEncrypted = true;
   }

   public PGStream(SocketFactory socketFactory, HostSpec hostSpec, int timeout) throws IOException {
      this.minStreamAvailableCheckDelay = 1000;
      this.maxResultBuffer = -1L;
      this.maxRowSizeBytes = -1;
      this.socketFactory = socketFactory;
      this.hostSpec = hostSpec;
      Socket socket = this.createSocket(timeout);
      this.changeSocket(socket);
      this.setEncoding(Encoding.getJVMEncoding("UTF-8"));
      this.int2Buf = new byte[2];
      this.int4Buf = new byte[4];
   }

   public PGStream(PGStream pgStream, int timeout) throws IOException {
      this.minStreamAvailableCheckDelay = 1000;
      this.maxResultBuffer = -1L;
      this.maxRowSizeBytes = -1;
      int sendBufferSize = 1024;
      int receiveBufferSize = 1024;
      int soTimeout = 0;
      boolean keepAlive = false;
      boolean tcpNoDelay = true;

      try {
         sendBufferSize = pgStream.getSocket().getSendBufferSize();
         receiveBufferSize = pgStream.getSocket().getReceiveBufferSize();
         soTimeout = pgStream.getSocket().getSoTimeout();
         keepAlive = pgStream.getSocket().getKeepAlive();
         tcpNoDelay = pgStream.getSocket().getTcpNoDelay();
      } catch (SocketException var9) {
      }

      pgStream.close();
      this.socketFactory = pgStream.socketFactory;
      this.hostSpec = pgStream.hostSpec;
      Socket socket = this.createSocket(timeout);
      this.changeSocket(socket);
      this.setEncoding(Encoding.getJVMEncoding("UTF-8"));
      socket.setReceiveBufferSize(receiveBufferSize);
      socket.setSendBufferSize(sendBufferSize);
      this.setNetworkTimeout(soTimeout);
      socket.setKeepAlive(keepAlive);
      socket.setTcpNoDelay(tcpNoDelay);
      this.int2Buf = new byte[2];
      this.int4Buf = new byte[4];
   }

   /** @deprecated */
   @Deprecated
   public PGStream(SocketFactory socketFactory, HostSpec hostSpec) throws IOException {
      this(socketFactory, hostSpec, 0);
   }

   public HostSpec getHostSpec() {
      return this.hostSpec;
   }

   public Socket getSocket() {
      return this.connection;
   }

   public SocketFactory getSocketFactory() {
      return this.socketFactory;
   }

   public boolean hasMessagePending() throws IOException {
      boolean available = false;
      if (this.pgInput.available() > 0) {
         return true;
      } else {
         long now = System.nanoTime() / 1000000L;
         if (now < this.nextStreamAvailableCheckTime && this.minStreamAvailableCheckDelay != 0) {
            return false;
         } else {
            int soTimeout = this.getNetworkTimeout();
            this.connection.setSoTimeout(1);

            try {
               if (!this.pgInput.ensureBytes(1, false)) {
                  boolean var5 = false;
                  return var5;
               }

               available = this.pgInput.peek() != -1;
            } catch (SocketTimeoutException var10) {
               boolean var6 = false;
               return var6;
            } finally {
               this.connection.setSoTimeout(soTimeout);
            }

            if (!available) {
               this.nextStreamAvailableCheckTime = now + (long)this.minStreamAvailableCheckDelay;
            }

            return available;
         }
      }
   }

   public void setMinStreamAvailableCheckDelay(int delay) {
      this.minStreamAvailableCheckDelay = delay;
   }

   private Socket createSocket(int timeout) throws IOException {
      Socket socket = null;

      try {
         socket = this.socketFactory.createSocket();
         String localSocketAddress = this.hostSpec.getLocalSocketAddress();
         if (localSocketAddress != null) {
            socket.bind(new InetSocketAddress(InetAddress.getByName(localSocketAddress), 0));
         }

         if (!socket.isConnected()) {
            InetSocketAddress address = this.hostSpec.shouldResolve() ? new InetSocketAddress(this.hostSpec.getHost(), this.hostSpec.getPort()) : InetSocketAddress.createUnresolved(this.hostSpec.getHost(), this.hostSpec.getPort());
            socket.connect(address, timeout);
         }

         return socket;
      } catch (Exception var6) {
         if (socket != null) {
            try {
               socket.close();
            } catch (Exception var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }
   }

   public void changeSocket(Socket socket) throws IOException {
      assert this.connection != socket : "changeSocket is called with the current socket as argument. This is a no-op, however, it re-allocates buffered streams, so refrain from excessive changeSocket calls";

      this.connection = socket;
      this.connection.setTcpNoDelay(true);
      this.pgInput = new VisibleBufferedInputStream(this.connection.getInputStream(), 8192);
      this.pgOutput = new BufferedOutputStream(this.connection.getOutputStream(), 8192);
      if (this.encoding != null) {
         this.setEncoding(this.encoding);
      }

   }

   public Encoding getEncoding() {
      return this.encoding;
   }

   public void setEncoding(Encoding encoding) throws IOException {
      if (this.encoding == null || !this.encoding.name().equals(encoding.name())) {
         if (this.encodingWriter != null) {
            this.encodingWriter.close();
         }

         this.encoding = encoding;
         OutputStream interceptor = new FilterOutputStream(this.pgOutput) {
            public void flush() throws IOException {
            }

            public void close() throws IOException {
               super.flush();
            }
         };
         this.encodingWriter = encoding.getEncodingWriter(interceptor);
      }
   }

   public Writer getEncodingWriter() throws IOException {
      if (this.encodingWriter == null) {
         throw new IOException("No encoding has been set on this connection");
      } else {
         return this.encodingWriter;
      }
   }

   public void sendChar(int val) throws IOException {
      this.pgOutput.write(val);
   }

   public void sendInteger4(int val) throws IOException {
      this.int4Buf[0] = (byte)(val >>> 24);
      this.int4Buf[1] = (byte)(val >>> 16);
      this.int4Buf[2] = (byte)(val >>> 8);
      this.int4Buf[3] = (byte)val;
      this.pgOutput.write(this.int4Buf);
   }

   public void sendInteger2(int val) throws IOException {
      if (val >= 0 && val <= 65535) {
         this.int2Buf[0] = (byte)(val >>> 8);
         this.int2Buf[1] = (byte)val;
         this.pgOutput.write(this.int2Buf);
      } else {
         throw new IllegalArgumentException("Tried to send an out-of-range integer as a 2-byte unsigned int value: " + val);
      }
   }

   public void send(byte[] buf) throws IOException {
      this.pgOutput.write(buf);
   }

   public void send(byte[] buf, int siz) throws IOException {
      this.send(buf, 0, siz);
   }

   public void send(byte[] buf, int off, int siz) throws IOException {
      int bufamt = buf.length - off;
      this.pgOutput.write(buf, off, bufamt < siz ? bufamt : siz);

      for(int i = bufamt; i < siz; ++i) {
         this.pgOutput.write(0);
      }

   }

   public void send(ByteStreamWriter writer) throws IOException {
      final FixedLengthOutputStream fixedLengthStream = new FixedLengthOutputStream(writer.getLength(), this.pgOutput);

      try {
         writer.writeTo(new ByteStreamWriter.ByteStreamTarget() {
            public OutputStream getOutputStream() {
               return fixedLengthStream;
            }
         });
      } catch (IOException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new IOException("Error writing bytes to stream", var5);
      }

      for(int i = fixedLengthStream.remaining(); i > 0; --i) {
         this.pgOutput.write(0);
      }

   }

   public int peekChar() throws IOException {
      int c = this.pgInput.peek();
      if (c < 0) {
         throw new EOFException();
      } else {
         return c;
      }
   }

   public int receiveChar() throws IOException {
      int c = this.pgInput.read();
      if (c < 0) {
         throw new EOFException();
      } else {
         return c;
      }
   }

   public int receiveInteger4() throws IOException {
      if (this.pgInput.read(this.int4Buf) != 4) {
         throw new EOFException();
      } else {
         return (this.int4Buf[0] & 255) << 24 | (this.int4Buf[1] & 255) << 16 | (this.int4Buf[2] & 255) << 8 | this.int4Buf[3] & 255;
      }
   }

   public int receiveInteger2() throws IOException {
      if (this.pgInput.read(this.int2Buf) != 2) {
         throw new EOFException();
      } else {
         return (this.int2Buf[0] & 255) << 8 | this.int2Buf[1] & 255;
      }
   }

   public String receiveString(int len) throws IOException {
      if (!this.pgInput.ensureBytes(len)) {
         throw new EOFException();
      } else {
         String res = this.encoding.decode(this.pgInput.getBuffer(), this.pgInput.getIndex(), len);
         this.pgInput.skip((long)len);
         return res;
      }
   }

   public EncodingPredictor.DecodeResult receiveErrorString(int len) throws IOException {
      if (!this.pgInput.ensureBytes(len)) {
         throw new EOFException();
      } else {
         EncodingPredictor.DecodeResult res;
         try {
            String value = this.encoding.decode(this.pgInput.getBuffer(), this.pgInput.getIndex(), len);
            res = new EncodingPredictor.DecodeResult(value, (String)null);
         } catch (IOException var6) {
            res = EncodingPredictor.decode(this.pgInput.getBuffer(), this.pgInput.getIndex(), len);
            if (res == null) {
               Encoding enc = Encoding.defaultEncoding();
               String value = enc.decode(this.pgInput.getBuffer(), this.pgInput.getIndex(), len);
               res = new EncodingPredictor.DecodeResult(value, enc.name());
            }
         }

         this.pgInput.skip((long)len);
         return res;
      }
   }

   public String receiveString() throws IOException {
      int len = this.pgInput.scanCStringLength();
      String res = this.encoding.decode(this.pgInput.getBuffer(), this.pgInput.getIndex(), len - 1);
      this.pgInput.skip((long)len);
      return res;
   }

   public String receiveCanonicalString() throws IOException {
      int len = this.pgInput.scanCStringLength();
      String res = this.encoding.decodeCanonicalized(this.pgInput.getBuffer(), this.pgInput.getIndex(), len - 1);
      this.pgInput.skip((long)len);
      return res;
   }

   public String receiveCanonicalStringIfPresent() throws IOException {
      int len = this.pgInput.scanCStringLength();
      String res = this.encoding.decodeCanonicalizedIfPresent(this.pgInput.getBuffer(), this.pgInput.getIndex(), len - 1);
      this.pgInput.skip((long)len);
      return res;
   }

   public Tuple receiveTupleV3() throws IOException, OutOfMemoryError, SQLException {
      int messageSize = this.receiveInteger4();
      int nf = this.receiveInteger2();
      int dataToReadSize = messageSize - 4 - 2 - 4 * nf;
      this.setMaxRowSizeBytes(dataToReadSize);
      byte[][] answer = new byte[nf][];
      this.increaseByteCounter((long)dataToReadSize);
      OutOfMemoryError oom = null;

      for(int i = 0; i < nf; ++i) {
         int size = this.receiveInteger4();
         if (size != -1) {
            try {
               answer[i] = new byte[size];
               this.receive(answer[i], 0, size);
            } catch (OutOfMemoryError var9) {
               oom = var9;
               this.skip(size);
            }
         }
      }

      if (oom != null) {
         throw oom;
      } else {
         return new Tuple(answer);
      }
   }

   public byte[] receive(int siz) throws IOException {
      byte[] answer = new byte[siz];
      this.receive(answer, 0, siz);
      return answer;
   }

   public void receive(byte[] buf, int off, int siz) throws IOException {
      int w;
      for(int s = 0; s < siz; s += w) {
         w = this.pgInput.read(buf, off + s, siz - s);
         if (w < 0) {
            throw new EOFException();
         }
      }

   }

   public void skip(int size) throws IOException {
      for(long s = 0L; s < (long)size; s += this.pgInput.skip((long)size - s)) {
      }

   }

   public void sendStream(InputStream inStream, int remaining) throws IOException {
      int expectedLength = remaining;
      byte[] streamBuffer = this.streamBuffer;
      if (streamBuffer == null) {
         this.streamBuffer = streamBuffer = new byte[8192];
      }

      while(remaining > 0) {
         int count = remaining > streamBuffer.length ? streamBuffer.length : remaining;

         int readCount;
         try {
            readCount = inStream.read(streamBuffer, 0, count);
            if (readCount < 0) {
               throw new EOFException(GT.tr("Premature end of input stream, expected {0} bytes, but only read {1}.", expectedLength, expectedLength - remaining));
            }
         } catch (IOException var8) {
            while(remaining > 0) {
               this.send(streamBuffer, count);
               remaining -= count;
               count = remaining > streamBuffer.length ? streamBuffer.length : remaining;
            }

            throw new PGBindException(var8);
         }

         this.send(streamBuffer, readCount);
         remaining -= readCount;
      }

   }

   public void flush() throws IOException {
      if (this.encodingWriter != null) {
         this.encodingWriter.flush();
      }

      this.pgOutput.flush();
   }

   public void receiveEOF() throws SQLException, IOException {
      int c = this.pgInput.read();
      if (c >= 0) {
         throw new PSQLException(GT.tr("Expected an EOF from server, got: {0}", c), PSQLState.COMMUNICATION_ERROR);
      }
   }

   public void close() throws IOException {
      if (this.encodingWriter != null) {
         this.encodingWriter.close();
      }

      this.pgOutput.close();
      this.pgInput.close();
      this.connection.close();
   }

   public void setNetworkTimeout(int milliseconds) throws IOException {
      this.connection.setSoTimeout(milliseconds);
      this.pgInput.setTimeoutRequested(milliseconds != 0);
   }

   public int getNetworkTimeout() throws IOException {
      return this.connection.getSoTimeout();
   }

   public void setMaxResultBuffer(@Nullable String value) throws PSQLException {
      this.maxResultBuffer = PGPropertyMaxResultBufferParser.parseProperty(value);
   }

   public long getMaxResultBuffer() {
      return this.maxResultBuffer;
   }

   public void setMaxRowSizeBytes(int rowSizeBytes) {
      if (rowSizeBytes > this.maxRowSizeBytes) {
         this.maxRowSizeBytes = rowSizeBytes;
      }

   }

   public int getMaxRowSizeBytes() {
      return this.maxRowSizeBytes;
   }

   public void clearMaxRowSizeBytes() {
      this.maxRowSizeBytes = -1;
   }

   public void clearResultBufferCount() {
      this.resultBufferByteCount = 0L;
   }

   private void increaseByteCounter(long value) throws SQLException {
      if (this.maxResultBuffer != -1L) {
         this.resultBufferByteCount += value;
         if (this.resultBufferByteCount > this.maxResultBuffer) {
            throw new PSQLException(GT.tr("Result set exceeded maxResultBuffer limit. Received:  {0}; Current limit: {1}", String.valueOf(this.resultBufferByteCount), String.valueOf(this.maxResultBuffer)), PSQLState.COMMUNICATION_ERROR);
         }
      }

   }

   public boolean isClosed() {
      return this.connection.isClosed();
   }
}
