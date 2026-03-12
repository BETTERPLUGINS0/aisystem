package fr.xephi.authme.libs.org.apache.commons.mail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class HtmlEmail extends MultiPartEmail {
   public static final int CID_LENGTH = 10;
   private static final String HTML_MESSAGE_START = "<html><body><pre>";
   private static final String HTML_MESSAGE_END = "</pre></body></html>";
   protected String text;
   protected String html;
   /** @deprecated */
   @Deprecated
   protected List<HtmlEmail.InlineImage> inlineImages;
   protected Map<String, HtmlEmail.InlineImage> inlineEmbeds = new HashMap();

   public HtmlEmail setTextMsg(String aText) throws EmailException {
      if (EmailUtils.isEmpty(aText)) {
         throw new EmailException("Invalid message supplied");
      } else {
         this.text = aText;
         return this;
      }
   }

   public HtmlEmail setHtmlMsg(String aHtml) throws EmailException {
      if (EmailUtils.isEmpty(aHtml)) {
         throw new EmailException("Invalid message supplied");
      } else {
         this.html = aHtml;
         return this;
      }
   }

   public Email setMsg(String msg) throws EmailException {
      if (EmailUtils.isEmpty(msg)) {
         throw new EmailException("Invalid message supplied");
      } else {
         this.setTextMsg(msg);
         StringBuffer htmlMsgBuf = new StringBuffer(msg.length() + "<html><body><pre>".length() + "</pre></body></html>".length());
         htmlMsgBuf.append("<html><body><pre>").append(msg).append("</pre></body></html>");
         this.setHtmlMsg(htmlMsgBuf.toString());
         return this;
      }
   }

   public String embed(String urlString, String name) throws EmailException {
      try {
         return this.embed(new URL(urlString), name);
      } catch (MalformedURLException var4) {
         throw new EmailException("Invalid URL", var4);
      }
   }

   public String embed(URL url, String name) throws EmailException {
      if (EmailUtils.isEmpty(name)) {
         throw new EmailException("name cannot be null or empty");
      } else if (this.inlineEmbeds.containsKey(name)) {
         HtmlEmail.InlineImage ii = (HtmlEmail.InlineImage)this.inlineEmbeds.get(name);
         URLDataSource urlDataSource = (URLDataSource)ii.getDataSource();
         if (url.toExternalForm().equals(urlDataSource.getURL().toExternalForm())) {
            return ii.getCid();
         } else {
            throw new EmailException("embedded name '" + name + "' is already bound to URL " + urlDataSource.getURL() + "; existing names cannot be rebound");
         }
      } else {
         InputStream is = null;

         try {
            is = url.openStream();
         } catch (IOException var12) {
            throw new EmailException("Invalid URL", var12);
         } finally {
            try {
               if (is != null) {
                  is.close();
               }
            } catch (IOException var11) {
            }

         }

         return this.embed((DataSource)(new URLDataSource(url)), name);
      }
   }

   public String embed(File file) throws EmailException {
      String cid = EmailUtils.randomAlphabetic(10).toLowerCase(Locale.ENGLISH);
      return this.embed(file, cid);
   }

   public String embed(File file, String cid) throws EmailException {
      if (EmailUtils.isEmpty(file.getName())) {
         throw new EmailException("file name cannot be null or empty");
      } else {
         String filePath = null;

         try {
            filePath = file.getCanonicalPath();
         } catch (IOException var9) {
            throw new EmailException("couldn't get canonical path for " + file.getName(), var9);
         }

         if (this.inlineEmbeds.containsKey(file.getName())) {
            HtmlEmail.InlineImage ii = (HtmlEmail.InlineImage)this.inlineEmbeds.get(file.getName());
            FileDataSource fileDataSource = (FileDataSource)ii.getDataSource();
            String existingFilePath = null;

            try {
               existingFilePath = fileDataSource.getFile().getCanonicalPath();
            } catch (IOException var8) {
               throw new EmailException("couldn't get canonical path for file " + fileDataSource.getFile().getName() + "which has already been embedded", var8);
            }

            if (filePath.equals(existingFilePath)) {
               return ii.getCid();
            } else {
               throw new EmailException("embedded name '" + file.getName() + "' is already bound to file " + existingFilePath + "; existing names cannot be rebound");
            }
         } else if (!file.exists()) {
            throw new EmailException("file " + filePath + " doesn't exist");
         } else if (!file.isFile()) {
            throw new EmailException("file " + filePath + " isn't a normal file");
         } else if (!file.canRead()) {
            throw new EmailException("file " + filePath + " isn't readable");
         } else {
            return this.embed(new FileDataSource(file), file.getName(), cid);
         }
      }
   }

   public String embed(DataSource dataSource, String name) throws EmailException {
      if (this.inlineEmbeds.containsKey(name)) {
         HtmlEmail.InlineImage ii = (HtmlEmail.InlineImage)this.inlineEmbeds.get(name);
         if (dataSource.equals(ii.getDataSource())) {
            return ii.getCid();
         } else {
            throw new EmailException("embedded DataSource '" + name + "' is already bound to name " + ii.getDataSource().toString() + "; existing names cannot be rebound");
         }
      } else {
         String cid = EmailUtils.randomAlphabetic(10).toLowerCase();
         return this.embed(dataSource, name, cid);
      }
   }

   public String embed(DataSource dataSource, String name, String cid) throws EmailException {
      if (EmailUtils.isEmpty(name)) {
         throw new EmailException("name cannot be null or empty");
      } else {
         MimeBodyPart mbp = new MimeBodyPart();

         try {
            String encodedCid = EmailUtils.encodeUrl(cid);
            mbp.setDataHandler(new DataHandler(dataSource));
            mbp.setFileName(name);
            mbp.setDisposition("inline");
            mbp.setContentID("<" + encodedCid + ">");
            HtmlEmail.InlineImage ii = new HtmlEmail.InlineImage(encodedCid, dataSource, mbp);
            this.inlineEmbeds.put(name, ii);
            return encodedCid;
         } catch (MessagingException var7) {
            throw new EmailException(var7);
         } catch (UnsupportedEncodingException var8) {
            throw new EmailException(var8);
         }
      }
   }

   public void buildMimeMessage() throws EmailException {
      try {
         this.build();
      } catch (MessagingException var2) {
         throw new EmailException(var2);
      }

      super.buildMimeMessage();
   }

   private void build() throws MessagingException, EmailException {
      MimeMultipart rootContainer = this.getContainer();
      MimeMultipart bodyEmbedsContainer = rootContainer;
      MimeMultipart bodyContainer = rootContainer;
      MimeBodyPart msgHtml = null;
      MimeBodyPart msgText = null;
      rootContainer.setSubType("mixed");
      if (EmailUtils.isNotEmpty(this.html) && this.inlineEmbeds.size() > 0) {
         bodyEmbedsContainer = new MimeMultipart("related");
         bodyContainer = bodyEmbedsContainer;
         this.addPart(bodyEmbedsContainer, 0);
         if (EmailUtils.isNotEmpty(this.text)) {
            bodyContainer = new MimeMultipart("alternative");
            BodyPart bodyPart = this.createBodyPart();

            try {
               bodyPart.setContent(bodyContainer);
               bodyEmbedsContainer.addBodyPart(bodyPart, 0);
            } catch (MessagingException var9) {
               throw new EmailException(var9);
            }
         }
      } else if (EmailUtils.isNotEmpty(this.text) && EmailUtils.isNotEmpty(this.html)) {
         if (this.inlineEmbeds.size() <= 0 && !this.isBoolHasAttachments()) {
            rootContainer.setSubType("alternative");
         } else {
            bodyContainer = new MimeMultipart("alternative");
            this.addPart(bodyContainer, 0);
         }
      }

      if (EmailUtils.isNotEmpty(this.html)) {
         msgHtml = new MimeBodyPart();
         bodyContainer.addBodyPart(msgHtml, 0);
         msgHtml.setText(this.html, this.charset, "html");
         String contentType = msgHtml.getContentType();
         if (contentType == null || !contentType.equals("text/html")) {
            if (EmailUtils.isNotEmpty(this.charset)) {
               msgHtml.setContent(this.html, "text/html; charset=" + this.charset);
            } else {
               msgHtml.setContent(this.html, "text/html");
            }
         }

         Iterator var7 = this.inlineEmbeds.values().iterator();

         while(var7.hasNext()) {
            HtmlEmail.InlineImage image = (HtmlEmail.InlineImage)var7.next();
            bodyEmbedsContainer.addBodyPart(image.getMbp());
         }
      }

      if (EmailUtils.isNotEmpty(this.text)) {
         msgText = new MimeBodyPart();
         bodyContainer.addBodyPart(msgText, 0);
         msgText.setText(this.text, this.charset);
      }

   }

   private static class InlineImage {
      private final String cid;
      private final DataSource dataSource;
      private final MimeBodyPart mbp;

      public InlineImage(String cid, DataSource dataSource, MimeBodyPart mbp) {
         this.cid = cid;
         this.dataSource = dataSource;
         this.mbp = mbp;
      }

      public String getCid() {
         return this.cid;
      }

      public DataSource getDataSource() {
         return this.dataSource;
      }

      public MimeBodyPart getMbp() {
         return this.mbp;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof HtmlEmail.InlineImage)) {
            return false;
         } else {
            HtmlEmail.InlineImage that = (HtmlEmail.InlineImage)obj;
            return this.cid.equals(that.cid);
         }
      }

      public int hashCode() {
         return this.cid.hashCode();
      }
   }
}
