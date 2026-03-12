package fr.xephi.authme.libs.org.apache.commons.mail.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public final class MimeMessageUtils {
   private MimeMessageUtils() {
   }

   public static MimeMessage createMimeMessage(Session session, byte[] source) throws MessagingException, IOException {
      ByteArrayInputStream is = null;

      MimeMessage var3;
      try {
         is = new ByteArrayInputStream(source);
         var3 = new MimeMessage(session, is);
      } finally {
         if (is != null) {
            is.close();
         }

      }

      return var3;
   }

   public static MimeMessage createMimeMessage(Session session, File source) throws MessagingException, IOException {
      FileInputStream is = null;

      MimeMessage var3;
      try {
         is = new FileInputStream(source);
         var3 = createMimeMessage(session, (InputStream)is);
      } finally {
         if (is != null) {
            is.close();
         }

      }

      return var3;
   }

   public static MimeMessage createMimeMessage(Session session, InputStream source) throws MessagingException {
      return new MimeMessage(session, source);
   }

   public static MimeMessage createMimeMessage(Session session, String source) throws MessagingException, IOException {
      ByteArrayInputStream is = null;

      MimeMessage var4;
      try {
         byte[] byteSource = source.getBytes();
         is = new ByteArrayInputStream(byteSource);
         var4 = createMimeMessage(session, (InputStream)is);
      } finally {
         if (is != null) {
            is.close();
         }

      }

      return var4;
   }

   public static void writeMimeMessage(MimeMessage mimeMessage, File resultFile) throws MessagingException, IOException {
      FileOutputStream fos = null;

      try {
         if (!resultFile.getParentFile().exists() && !resultFile.getParentFile().mkdirs()) {
            throw new IOException("Failed to create the following parent directories: " + resultFile.getParentFile());
         }

         fos = new FileOutputStream(resultFile);
         mimeMessage.writeTo(fos);
         fos.flush();
         fos.close();
         fos = null;
      } finally {
         if (fos != null) {
            fos.close();
         }

      }

   }
}
