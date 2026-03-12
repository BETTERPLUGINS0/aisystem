package fr.xephi.authme.libs.org.apache.commons.mail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;

public class MultiPartEmail extends Email {
   private MimeMultipart container;
   private BodyPart primaryBodyPart;
   private String subType;
   private boolean initialized;
   private boolean boolHasAttachments;

   public void setSubType(String aSubType) {
      this.subType = aSubType;
   }

   public String getSubType() {
      return this.subType;
   }

   public Email addPart(String partContent, String partContentType) throws EmailException {
      BodyPart bodyPart = this.createBodyPart();

      try {
         bodyPart.setContent(partContent, partContentType);
         this.getContainer().addBodyPart(bodyPart);
         return this;
      } catch (MessagingException var5) {
         throw new EmailException(var5);
      }
   }

   public Email addPart(MimeMultipart multipart) throws EmailException {
      try {
         return this.addPart(multipart, this.getContainer().getCount());
      } catch (MessagingException var3) {
         throw new EmailException(var3);
      }
   }

   public Email addPart(MimeMultipart multipart, int index) throws EmailException {
      BodyPart bodyPart = this.createBodyPart();

      try {
         bodyPart.setContent(multipart);
         this.getContainer().addBodyPart(bodyPart, index);
         return this;
      } catch (MessagingException var5) {
         throw new EmailException(var5);
      }
   }

   protected void init() {
      if (this.initialized) {
         throw new IllegalStateException("Already initialized");
      } else {
         this.container = this.createMimeMultipart();
         super.setContent(this.container);
         this.initialized = true;
      }
   }

   public Email setMsg(String msg) throws EmailException {
      if (EmailUtils.isEmpty(msg)) {
         throw new EmailException("Invalid message supplied");
      } else {
         try {
            BodyPart primary = this.getPrimaryBodyPart();
            if (primary instanceof MimePart && EmailUtils.isNotEmpty(this.charset)) {
               ((MimePart)primary).setText(msg, this.charset);
            } else {
               primary.setText(msg);
            }

            return this;
         } catch (MessagingException var3) {
            throw new EmailException(var3);
         }
      }
   }

   public void buildMimeMessage() throws EmailException {
      try {
         if (this.primaryBodyPart != null) {
            BodyPart body = this.getPrimaryBodyPart();

            try {
               body.getContent();
            } catch (IOException var3) {
            }
         }

         if (this.subType != null) {
            this.getContainer().setSubType(this.subType);
         }

         super.buildMimeMessage();
      } catch (MessagingException var4) {
         throw new EmailException(var4);
      }
   }

   public MultiPartEmail attach(File file) throws EmailException {
      String fileName = file.getAbsolutePath();

      try {
         if (!file.exists()) {
            throw new IOException("\"" + fileName + "\" does not exist");
         } else {
            FileDataSource fds = new FileDataSource(file);
            return this.attach((DataSource)fds, file.getName(), (String)null, "attachment");
         }
      } catch (IOException var4) {
         throw new EmailException("Cannot attach file \"" + fileName + "\"", var4);
      }
   }

   public MultiPartEmail attach(EmailAttachment attachment) throws EmailException {
      MultiPartEmail result = null;
      if (attachment == null) {
         throw new EmailException("Invalid attachment supplied");
      } else {
         URL url = attachment.getURL();
         if (url == null) {
            String fileName = null;

            try {
               fileName = attachment.getPath();
               File file = new File(fileName);
               if (!file.exists()) {
                  throw new IOException("\"" + fileName + "\" does not exist");
               }

               result = this.attach((DataSource)(new FileDataSource(file)), attachment.getName(), attachment.getDescription(), attachment.getDisposition());
            } catch (IOException var6) {
               throw new EmailException("Cannot attach file \"" + fileName + "\"", var6);
            }
         } else {
            result = this.attach(url, attachment.getName(), attachment.getDescription(), attachment.getDisposition());
         }

         return result;
      }
   }

   public MultiPartEmail attach(URL url, String name, String description) throws EmailException {
      return this.attach(url, name, description, "attachment");
   }

   public MultiPartEmail attach(URL url, String name, String description, String disposition) throws EmailException {
      try {
         InputStream is = url.openStream();
         is.close();
      } catch (IOException var6) {
         throw new EmailException("Invalid URL set:" + url, var6);
      }

      return this.attach((DataSource)(new URLDataSource(url)), name, description, disposition);
   }

   public MultiPartEmail attach(DataSource ds, String name, String description) throws EmailException {
      try {
         InputStream is = ds != null ? ds.getInputStream() : null;
         if (is != null) {
            is.close();
         }

         if (is == null) {
            throw new EmailException("Invalid Datasource");
         }
      } catch (IOException var5) {
         throw new EmailException("Invalid Datasource", var5);
      }

      return this.attach(ds, name, description, "attachment");
   }

   public MultiPartEmail attach(DataSource ds, String name, String description, String disposition) throws EmailException {
      if (EmailUtils.isEmpty(name)) {
         name = ds.getName();
      }

      BodyPart bodyPart = this.createBodyPart();

      try {
         bodyPart.setDisposition(disposition);
         bodyPart.setFileName(MimeUtility.encodeText(name));
         bodyPart.setDescription(description);
         bodyPart.setDataHandler(new DataHandler(ds));
         this.getContainer().addBodyPart(bodyPart);
      } catch (UnsupportedEncodingException var7) {
         throw new EmailException(var7);
      } catch (MessagingException var8) {
         throw new EmailException(var8);
      }

      this.setBoolHasAttachments(true);
      return this;
   }

   protected BodyPart getPrimaryBodyPart() throws MessagingException {
      if (!this.initialized) {
         this.init();
      }

      if (this.primaryBodyPart == null) {
         this.primaryBodyPart = this.createBodyPart();
         this.getContainer().addBodyPart(this.primaryBodyPart, 0);
      }

      return this.primaryBodyPart;
   }

   protected MimeMultipart getContainer() {
      if (!this.initialized) {
         this.init();
      }

      return this.container;
   }

   protected BodyPart createBodyPart() {
      return new MimeBodyPart();
   }

   protected MimeMultipart createMimeMultipart() {
      return new MimeMultipart();
   }

   public boolean isBoolHasAttachments() {
      return this.boolHasAttachments;
   }

   public void setBoolHasAttachments(boolean b) {
      this.boolHasAttachments = b;
   }

   protected boolean isInitialized() {
      return this.initialized;
   }

   protected void setInitialized(boolean b) {
      this.initialized = b;
   }
}
