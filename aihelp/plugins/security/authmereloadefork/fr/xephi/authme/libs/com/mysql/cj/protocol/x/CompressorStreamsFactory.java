package fr.xephi.authme.libs.com.mysql.cj.protocol.x;

import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.CJException;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.util.Util;
import java.io.InputStream;
import java.io.OutputStream;

public class CompressorStreamsFactory {
   private CompressionAlgorithm compressionAlgorithm;
   private InputStream compressorInputStreamInstance = null;
   private ContinuousInputStream underlyingInputStream = null;
   private OutputStream compressorOutputStreamInstance = null;
   private ReusableOutputStream underlyingOutputStream = null;

   public CompressorStreamsFactory(CompressionAlgorithm algorithm) {
      this.compressionAlgorithm = algorithm;
   }

   public CompressionMode getCompressionMode() {
      return this.compressionAlgorithm.getCompressionMode();
   }

   public boolean areCompressedStreamsContinuous() {
      return this.getCompressionMode() == CompressionMode.STREAM;
   }

   public InputStream getInputStreamInstance(InputStream in) {
      InputStream underlyingIn = in;
      if (this.areCompressedStreamsContinuous()) {
         if (this.compressorInputStreamInstance != null) {
            this.underlyingInputStream.addInputStream(in);
            return this.compressorInputStreamInstance;
         }

         this.underlyingInputStream = new ContinuousInputStream(in);
         underlyingIn = this.underlyingInputStream;
      }

      InputStream compressionIn;
      try {
         compressionIn = (InputStream)Util.getInstance(InputStream.class, this.compressionAlgorithm.getInputStreamClassName(), new Class[]{InputStream.class}, new Object[]{underlyingIn}, (ExceptionInterceptor)null);
      } catch (CJException var5) {
         throw (WrongArgumentException)ExceptionFactory.createException((Class)WrongArgumentException.class, (String)Messages.getString("Protocol.Compression.IoFactory.0", new Object[]{this.compressionAlgorithm.getInputStreamClassName(), this.compressionAlgorithm.getAlgorithmIdentifier()}), (Throwable)var5);
      }

      if (this.areCompressedStreamsContinuous()) {
         this.compressorInputStreamInstance = compressionIn;
      }

      return compressionIn;
   }

   public OutputStream getOutputStreamInstance(OutputStream out) {
      OutputStream underlyingOut = out;
      if (this.areCompressedStreamsContinuous()) {
         if (this.compressorOutputStreamInstance != null) {
            this.underlyingOutputStream.setOutputStream(out);
            return this.compressorOutputStreamInstance;
         }

         this.underlyingOutputStream = new ReusableOutputStream(out);
         underlyingOut = this.underlyingOutputStream;
      }

      Object compressionOut;
      try {
         compressionOut = (OutputStream)Util.getInstance(OutputStream.class, this.compressionAlgorithm.getOutputStreamClassName(), new Class[]{OutputStream.class}, new Object[]{underlyingOut}, (ExceptionInterceptor)null);
      } catch (CJException var5) {
         throw (WrongArgumentException)ExceptionFactory.createException((Class)WrongArgumentException.class, (String)Messages.getString("Protocol.Compression.IoFactory.1", new Object[]{this.compressionAlgorithm.getOutputStreamClassName(), this.compressionAlgorithm.getAlgorithmIdentifier()}), (Throwable)var5);
      }

      if (this.areCompressedStreamsContinuous()) {
         compressionOut = new ContinuousOutputStream((OutputStream)compressionOut);
         this.compressorOutputStreamInstance = (OutputStream)compressionOut;
      }

      return (OutputStream)compressionOut;
   }
}
