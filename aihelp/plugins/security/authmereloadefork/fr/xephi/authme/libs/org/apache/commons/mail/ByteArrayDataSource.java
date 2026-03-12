package fr.xephi.authme.libs.org.apache.commons.mail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import javax.activation.DataSource;

/** @deprecated */
@Deprecated
public class ByteArrayDataSource implements DataSource {
   public static final int BUFFER_SIZE = 512;
   private ByteArrayOutputStream baos;
   private final String type;
   private String name = "";

   public ByteArrayDataSource(byte[] data, String aType) throws IOException {
      this.type = aType;
      ByteArrayInputStream bis = null;

      try {
         bis = new ByteArrayInputStream(data);
         this.byteArrayDataSource(bis);
      } finally {
         if (bis != null) {
            bis.close();
         }

      }

   }

   public ByteArrayDataSource(InputStream aIs, String aType) throws IOException {
      this.type = aType;
      this.byteArrayDataSource(aIs);
   }

   public ByteArrayDataSource(String data, String aType) throws IOException {
      this.type = aType;

      try {
         this.baos = new ByteArrayOutputStream();
         this.baos.write(data.getBytes("iso-8859-1"));
         this.baos.flush();
         this.baos.close();
      } catch (UnsupportedEncodingException var7) {
         throw new IOException("The Character Encoding is not supported.");
      } finally {
         if (this.baos != null) {
            this.baos.close();
         }

      }

   }

   private void byteArrayDataSource(InputStream aIs) throws IOException {
      BufferedInputStream bis = null;
      BufferedOutputStream osWriter = null;

      try {
         int length = false;
         byte[] buffer = new byte[512];
         bis = new BufferedInputStream(aIs);
         this.baos = new ByteArrayOutputStream();
         osWriter = new BufferedOutputStream(this.baos);

         int length;
         while((length = bis.read(buffer)) != -1) {
            osWriter.write(buffer, 0, length);
         }

         osWriter.flush();
         osWriter.close();
      } finally {
         if (bis != null) {
            bis.close();
         }

         if (this.baos != null) {
            this.baos.close();
         }

         if (osWriter != null) {
            osWriter.close();
         }

      }
   }

   public String getContentType() {
      return this.type == null ? "application/octet-stream" : this.type;
   }

   public InputStream getInputStream() throws IOException {
      if (this.baos == null) {
         throw new IOException("no data");
      } else {
         return new ByteArrayInputStream(this.baos.toByteArray());
      }
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public OutputStream getOutputStream() {
      this.baos = new ByteArrayOutputStream();
      return this.baos;
   }
}
