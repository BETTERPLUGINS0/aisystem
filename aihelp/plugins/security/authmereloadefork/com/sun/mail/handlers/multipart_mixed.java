package com.sun.mail.handlers;

import java.io.IOException;
import java.io.OutputStream;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;

public class multipart_mixed extends handler_base {
   private static ActivationDataFlavor[] myDF = new ActivationDataFlavor[]{new ActivationDataFlavor(Multipart.class, "multipart/mixed", "Multipart")};

   protected ActivationDataFlavor[] getDataFlavors() {
      return myDF;
   }

   public Object getContent(DataSource ds) throws IOException {
      try {
         return new MimeMultipart(ds);
      } catch (MessagingException var4) {
         IOException ioex = new IOException("Exception while constructing MimeMultipart");
         ioex.initCause(var4);
         throw ioex;
      }
   }

   public void writeTo(Object obj, String mimeType, OutputStream os) throws IOException {
      if (obj instanceof Multipart) {
         try {
            ((Multipart)obj).writeTo(os);
         } catch (MessagingException var6) {
            IOException ioex = new IOException("Exception writing Multipart");
            ioex.initCause(var6);
            throw ioex;
         }
      }

   }
}
