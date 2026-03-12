package fr.xephi.authme.libs.org.apache.commons.mail;

import fr.xephi.authme.libs.org.apache.commons.mail.util.IDNEmailAddressConverter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public abstract class Email {
   /** @deprecated */
   @Deprecated
   public static final String SENDER_EMAIL = "sender.email";
   /** @deprecated */
   @Deprecated
   public static final String SENDER_NAME = "sender.name";
   /** @deprecated */
   @Deprecated
   public static final String RECEIVER_EMAIL = "receiver.email";
   /** @deprecated */
   @Deprecated
   public static final String RECEIVER_NAME = "receiver.name";
   /** @deprecated */
   @Deprecated
   public static final String EMAIL_SUBJECT = "email.subject";
   /** @deprecated */
   @Deprecated
   public static final String EMAIL_BODY = "email.body";
   /** @deprecated */
   @Deprecated
   public static final String CONTENT_TYPE = "content.type";
   /** @deprecated */
   @Deprecated
   public static final String ATTACHMENTS = "attachments";
   /** @deprecated */
   @Deprecated
   public static final String FILE_SERVER = "file.server";
   /** @deprecated */
   @Deprecated
   public static final String KOI8_R = "koi8-r";
   /** @deprecated */
   @Deprecated
   public static final String ISO_8859_1 = "iso-8859-1";
   /** @deprecated */
   @Deprecated
   public static final String US_ASCII = "us-ascii";
   /** @deprecated */
   @Deprecated
   public static final String MAIL_DEBUG = "mail.debug";
   /** @deprecated */
   @Deprecated
   public static final String MAIL_HOST = "mail.smtp.host";
   /** @deprecated */
   @Deprecated
   public static final String MAIL_PORT = "mail.smtp.port";
   /** @deprecated */
   @Deprecated
   public static final String MAIL_SMTP_FROM = "mail.smtp.from";
   /** @deprecated */
   @Deprecated
   public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
   /** @deprecated */
   @Deprecated
   public static final String MAIL_SMTP_USER = "mail.smtp.user";
   /** @deprecated */
   @Deprecated
   public static final String MAIL_SMTP_PASSWORD = "mail.smtp.password";
   /** @deprecated */
   @Deprecated
   public static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
   /** @deprecated */
   @Deprecated
   public static final String SMTP = "smtp";
   /** @deprecated */
   @Deprecated
   public static final String TEXT_HTML = "text/html";
   /** @deprecated */
   @Deprecated
   public static final String TEXT_PLAIN = "text/plain";
   /** @deprecated */
   @Deprecated
   public static final String MAIL_TRANSPORT_TLS = "mail.smtp.starttls.enable";
   /** @deprecated */
   @Deprecated
   public static final String MAIL_SMTP_SOCKET_FACTORY_FALLBACK = "mail.smtp.socketFactory.fallback";
   /** @deprecated */
   @Deprecated
   public static final String MAIL_SMTP_SOCKET_FACTORY_CLASS = "mail.smtp.socketFactory.class";
   /** @deprecated */
   @Deprecated
   public static final String MAIL_SMTP_SOCKET_FACTORY_PORT = "mail.smtp.socketFactory.port";
   /** @deprecated */
   @Deprecated
   public static final String MAIL_SMTP_CONNECTIONTIMEOUT = "mail.smtp.connectiontimeout";
   /** @deprecated */
   @Deprecated
   public static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
   protected MimeMessage message;
   protected String charset;
   protected InternetAddress fromAddress;
   protected String subject;
   protected MimeMultipart emailBody;
   protected Object content;
   protected String contentType;
   protected boolean debug;
   protected Date sentDate;
   protected Authenticator authenticator;
   protected String hostName;
   protected String smtpPort = "25";
   protected String sslSmtpPort = "465";
   protected List<InternetAddress> toList = new ArrayList();
   protected List<InternetAddress> ccList = new ArrayList();
   protected List<InternetAddress> bccList = new ArrayList();
   protected List<InternetAddress> replyList = new ArrayList();
   protected String bounceAddress;
   protected Map<String, String> headers = new HashMap();
   protected boolean popBeforeSmtp;
   protected String popHost;
   protected String popUsername;
   protected String popPassword;
   /** @deprecated */
   @Deprecated
   protected boolean tls;
   /** @deprecated */
   @Deprecated
   protected boolean ssl;
   protected int socketTimeout = 60000;
   protected int socketConnectionTimeout = 60000;
   private boolean startTlsEnabled;
   private boolean startTlsRequired;
   private boolean sslOnConnect;
   private boolean sslCheckServerIdentity;
   private boolean sendPartial;
   private Session session;

   public void setDebug(boolean d) {
      this.debug = d;
   }

   public void setAuthentication(String userName, String password) {
      this.setAuthenticator(new DefaultAuthenticator(userName, password));
   }

   public void setAuthenticator(Authenticator newAuthenticator) {
      this.authenticator = newAuthenticator;
   }

   public void setCharset(String newCharset) {
      Charset set = Charset.forName(newCharset);
      this.charset = set.name();
   }

   public void setContent(MimeMultipart aMimeMultipart) {
      this.emailBody = aMimeMultipart;
   }

   public void setContent(Object aObject, String aContentType) {
      this.content = aObject;
      this.updateContentType(aContentType);
   }

   public void updateContentType(String aContentType) {
      if (EmailUtils.isEmpty(aContentType)) {
         this.contentType = null;
      } else {
         this.contentType = aContentType;
         String strMarker = "; charset=";
         int charsetPos = aContentType.toLowerCase().indexOf("; charset=");
         if (charsetPos != -1) {
            charsetPos += "; charset=".length();
            int intCharsetEnd = aContentType.toLowerCase().indexOf(" ", charsetPos);
            if (intCharsetEnd != -1) {
               this.charset = aContentType.substring(charsetPos, intCharsetEnd);
            } else {
               this.charset = aContentType.substring(charsetPos);
            }
         } else if (this.contentType.startsWith("text/") && EmailUtils.isNotEmpty(this.charset)) {
            StringBuffer contentTypeBuf = new StringBuffer(this.contentType);
            contentTypeBuf.append("; charset=");
            contentTypeBuf.append(this.charset);
            this.contentType = contentTypeBuf.toString();
         }
      }

   }

   public void setHostName(String aHostName) {
      this.checkSessionAlreadyInitialized();
      this.hostName = aHostName;
   }

   /** @deprecated */
   @Deprecated
   public void setTLS(boolean withTLS) {
      this.setStartTLSEnabled(withTLS);
   }

   public Email setStartTLSEnabled(boolean startTlsEnabled) {
      this.checkSessionAlreadyInitialized();
      this.startTlsEnabled = startTlsEnabled;
      this.tls = startTlsEnabled;
      return this;
   }

   public Email setStartTLSRequired(boolean startTlsRequired) {
      this.checkSessionAlreadyInitialized();
      this.startTlsRequired = startTlsRequired;
      return this;
   }

   public void setSmtpPort(int aPortNumber) {
      this.checkSessionAlreadyInitialized();
      if (aPortNumber < 1) {
         throw new IllegalArgumentException("Cannot connect to a port number that is less than 1 ( " + aPortNumber + " )");
      } else {
         this.smtpPort = Integer.toString(aPortNumber);
      }
   }

   public void setMailSession(Session aSession) {
      EmailUtils.notNull(aSession, "no mail session supplied");
      Properties sessionProperties = aSession.getProperties();
      String auth = sessionProperties.getProperty("mail.smtp.auth");
      if ("true".equalsIgnoreCase(auth)) {
         String userName = sessionProperties.getProperty("mail.smtp.user");
         String password = sessionProperties.getProperty("mail.smtp.password");
         if (EmailUtils.isNotEmpty(userName) && EmailUtils.isNotEmpty(password)) {
            this.authenticator = new DefaultAuthenticator(userName, password);
            this.session = Session.getInstance(sessionProperties, this.authenticator);
         } else {
            this.session = aSession;
         }
      } else {
         this.session = aSession;
      }

   }

   public void setMailSessionFromJNDI(String jndiName) throws NamingException {
      if (EmailUtils.isEmpty(jndiName)) {
         throw new IllegalArgumentException("JNDI name missing");
      } else {
         Context ctx = null;
         if (jndiName.startsWith("java:")) {
            ctx = new InitialContext();
         } else {
            ctx = (Context)(new InitialContext()).lookup("java:comp/env");
         }

         this.setMailSession((Session)((Context)ctx).lookup(jndiName));
      }
   }

   public Session getMailSession() throws EmailException {
      if (this.session == null) {
         Properties properties = new Properties(System.getProperties());
         properties.setProperty("mail.transport.protocol", "smtp");
         if (EmailUtils.isEmpty(this.hostName)) {
            this.hostName = properties.getProperty("mail.smtp.host");
         }

         if (EmailUtils.isEmpty(this.hostName)) {
            throw new EmailException("Cannot find valid hostname for mail session");
         }

         properties.setProperty("mail.smtp.port", this.smtpPort);
         properties.setProperty("mail.smtp.host", this.hostName);
         properties.setProperty("mail.debug", String.valueOf(this.debug));
         properties.setProperty("mail.smtp.starttls.enable", this.isStartTLSEnabled() ? "true" : "false");
         properties.setProperty("mail.smtp.starttls.required", this.isStartTLSRequired() ? "true" : "false");
         properties.setProperty("mail.smtp.sendpartial", this.isSendPartial() ? "true" : "false");
         properties.setProperty("mail.smtps.sendpartial", this.isSendPartial() ? "true" : "false");
         if (this.authenticator != null) {
            properties.setProperty("mail.smtp.auth", "true");
         }

         if (this.isSSLOnConnect()) {
            properties.setProperty("mail.smtp.port", this.sslSmtpPort);
            properties.setProperty("mail.smtp.socketFactory.port", this.sslSmtpPort);
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.smtp.socketFactory.fallback", "false");
         }

         if ((this.isSSLOnConnect() || this.isStartTLSEnabled()) && this.isSSLCheckServerIdentity()) {
            properties.setProperty("mail.smtp.ssl.checkserveridentity", "true");
         }

         if (this.bounceAddress != null) {
            properties.setProperty("mail.smtp.from", this.bounceAddress);
         }

         if (this.socketTimeout > 0) {
            properties.setProperty("mail.smtp.timeout", Integer.toString(this.socketTimeout));
         }

         if (this.socketConnectionTimeout > 0) {
            properties.setProperty("mail.smtp.connectiontimeout", Integer.toString(this.socketConnectionTimeout));
         }

         this.session = Session.getInstance(properties, this.authenticator);
      }

      return this.session;
   }

   public Email setFrom(String email) throws EmailException {
      return this.setFrom(email, (String)null);
   }

   public Email setFrom(String email, String name) throws EmailException {
      return this.setFrom(email, name, this.charset);
   }

   public Email setFrom(String email, String name, String charset) throws EmailException {
      this.fromAddress = this.createInternetAddress(email, name, charset);
      return this;
   }

   public Email addTo(String email) throws EmailException {
      return this.addTo(email, (String)null);
   }

   public Email addTo(String... emails) throws EmailException {
      if (emails != null && emails.length != 0) {
         String[] var2 = emails;
         int var3 = emails.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String email = var2[var4];
            this.addTo(email, (String)null);
         }

         return this;
      } else {
         throw new EmailException("Address List provided was invalid");
      }
   }

   public Email addTo(String email, String name) throws EmailException {
      return this.addTo(email, name, this.charset);
   }

   public Email addTo(String email, String name, String charset) throws EmailException {
      this.toList.add(this.createInternetAddress(email, name, charset));
      return this;
   }

   public Email setTo(Collection<InternetAddress> aCollection) throws EmailException {
      if (aCollection != null && !aCollection.isEmpty()) {
         this.toList = new ArrayList(aCollection);
         return this;
      } else {
         throw new EmailException("Address List provided was invalid");
      }
   }

   public Email addCc(String email) throws EmailException {
      return this.addCc(email, (String)null);
   }

   public Email addCc(String... emails) throws EmailException {
      if (emails != null && emails.length != 0) {
         String[] var2 = emails;
         int var3 = emails.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String email = var2[var4];
            this.addCc(email, (String)null);
         }

         return this;
      } else {
         throw new EmailException("Address List provided was invalid");
      }
   }

   public Email addCc(String email, String name) throws EmailException {
      return this.addCc(email, name, this.charset);
   }

   public Email addCc(String email, String name, String charset) throws EmailException {
      this.ccList.add(this.createInternetAddress(email, name, charset));
      return this;
   }

   public Email setCc(Collection<InternetAddress> aCollection) throws EmailException {
      if (aCollection != null && !aCollection.isEmpty()) {
         this.ccList = new ArrayList(aCollection);
         return this;
      } else {
         throw new EmailException("Address List provided was invalid");
      }
   }

   public Email addBcc(String email) throws EmailException {
      return this.addBcc(email, (String)null);
   }

   public Email addBcc(String... emails) throws EmailException {
      if (emails != null && emails.length != 0) {
         String[] var2 = emails;
         int var3 = emails.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String email = var2[var4];
            this.addBcc(email, (String)null);
         }

         return this;
      } else {
         throw new EmailException("Address List provided was invalid");
      }
   }

   public Email addBcc(String email, String name) throws EmailException {
      return this.addBcc(email, name, this.charset);
   }

   public Email addBcc(String email, String name, String charset) throws EmailException {
      this.bccList.add(this.createInternetAddress(email, name, charset));
      return this;
   }

   public Email setBcc(Collection<InternetAddress> aCollection) throws EmailException {
      if (aCollection != null && !aCollection.isEmpty()) {
         this.bccList = new ArrayList(aCollection);
         return this;
      } else {
         throw new EmailException("Address List provided was invalid");
      }
   }

   public Email addReplyTo(String email) throws EmailException {
      return this.addReplyTo(email, (String)null);
   }

   public Email addReplyTo(String email, String name) throws EmailException {
      return this.addReplyTo(email, name, this.charset);
   }

   public Email addReplyTo(String email, String name, String charset) throws EmailException {
      this.replyList.add(this.createInternetAddress(email, name, charset));
      return this;
   }

   public Email setReplyTo(Collection<InternetAddress> aCollection) throws EmailException {
      if (aCollection != null && !aCollection.isEmpty()) {
         this.replyList = new ArrayList(aCollection);
         return this;
      } else {
         throw new EmailException("Address List provided was invalid");
      }
   }

   public void setHeaders(Map<String, String> map) {
      this.headers.clear();
      Iterator var2 = map.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<String, String> entry = (Entry)var2.next();
         this.addHeader((String)entry.getKey(), (String)entry.getValue());
      }

   }

   public void addHeader(String name, String value) {
      if (EmailUtils.isEmpty(name)) {
         throw new IllegalArgumentException("name can not be null or empty");
      } else if (EmailUtils.isEmpty(value)) {
         throw new IllegalArgumentException("value can not be null or empty");
      } else {
         this.headers.put(name, value);
      }
   }

   public String getHeader(String header) {
      return (String)this.headers.get(header);
   }

   public Map<String, String> getHeaders() {
      return this.headers;
   }

   public Email setSubject(String aSubject) {
      this.subject = EmailUtils.replaceEndOfLineCharactersWithSpaces(aSubject);
      return this;
   }

   public String getBounceAddress() {
      return this.bounceAddress;
   }

   public Email setBounceAddress(String email) {
      this.checkSessionAlreadyInitialized();
      if (email != null && !email.isEmpty()) {
         try {
            this.bounceAddress = this.createInternetAddress(email, (String)null, this.charset).getAddress();
         } catch (EmailException var3) {
            throw new IllegalArgumentException("Failed to set the bounce address : " + email, var3);
         }
      } else {
         this.bounceAddress = email;
      }

      return this;
   }

   public abstract Email setMsg(String var1) throws EmailException;

   public void buildMimeMessage() throws EmailException {
      if (this.message != null) {
         throw new IllegalStateException("The MimeMessage is already built.");
      } else {
         try {
            this.message = this.createMimeMessage(this.getMailSession());
            if (EmailUtils.isNotEmpty(this.subject)) {
               if (EmailUtils.isNotEmpty(this.charset)) {
                  this.message.setSubject(this.subject, this.charset);
               } else {
                  this.message.setSubject(this.subject);
               }
            }

            this.updateContentType(this.contentType);
            if (this.content != null) {
               if ("text/plain".equalsIgnoreCase(this.contentType) && this.content instanceof String) {
                  this.message.setText(this.content.toString(), this.charset);
               } else {
                  this.message.setContent(this.content, this.contentType);
               }
            } else if (this.emailBody != null) {
               if (this.contentType == null) {
                  this.message.setContent(this.emailBody);
               } else {
                  this.message.setContent(this.emailBody, this.contentType);
               }
            } else {
               this.message.setText("");
            }

            if (this.fromAddress != null) {
               this.message.setFrom((Address)this.fromAddress);
            } else if (this.session.getProperty("mail.smtp.from") == null && this.session.getProperty("mail.from") == null) {
               throw new EmailException("From address required");
            }

            if (this.toList.size() + this.ccList.size() + this.bccList.size() == 0) {
               throw new EmailException("At least one receiver address required");
            } else {
               if (this.toList.size() > 0) {
                  this.message.setRecipients(Message.RecipientType.TO, (Address[])this.toInternetAddressArray(this.toList));
               }

               if (this.ccList.size() > 0) {
                  this.message.setRecipients(Message.RecipientType.CC, (Address[])this.toInternetAddressArray(this.ccList));
               }

               if (this.bccList.size() > 0) {
                  this.message.setRecipients(Message.RecipientType.BCC, (Address[])this.toInternetAddressArray(this.bccList));
               }

               if (this.replyList.size() > 0) {
                  this.message.setReplyTo(this.toInternetAddressArray(this.replyList));
               }

               if (this.headers.size() > 0) {
                  Iterator var1 = this.headers.entrySet().iterator();

                  while(var1.hasNext()) {
                     Entry<String, String> entry = (Entry)var1.next();
                     String foldedValue = this.createFoldedHeaderValue((String)entry.getKey(), (String)entry.getValue());
                     this.message.addHeader((String)entry.getKey(), foldedValue);
                  }
               }

               if (this.message.getSentDate() == null) {
                  this.message.setSentDate(this.getSentDate());
               }

               if (this.popBeforeSmtp) {
                  Store store = this.session.getStore("pop3");
                  store.connect(this.popHost, this.popUsername, this.popPassword);
               }

            }
         } catch (MessagingException var4) {
            throw new EmailException(var4);
         }
      }
   }

   public String sendMimeMessage() throws EmailException {
      EmailUtils.notNull(this.message, "MimeMessage has not been created yet");

      try {
         Transport.send(this.message);
         return this.message.getMessageID();
      } catch (Throwable var3) {
         String msg = "Sending the email to the following server failed : " + this.getHostName() + ":" + this.getSmtpPort();
         throw new EmailException(msg, var3);
      }
   }

   public MimeMessage getMimeMessage() {
      return this.message;
   }

   public String send() throws EmailException {
      this.buildMimeMessage();
      return this.sendMimeMessage();
   }

   public void setSentDate(Date date) {
      if (date != null) {
         this.sentDate = new Date(date.getTime());
      }

   }

   public Date getSentDate() {
      return this.sentDate == null ? new Date() : new Date(this.sentDate.getTime());
   }

   public String getSubject() {
      return this.subject;
   }

   public InternetAddress getFromAddress() {
      return this.fromAddress;
   }

   public String getHostName() {
      if (this.session != null) {
         return this.session.getProperty("mail.smtp.host");
      } else {
         return EmailUtils.isNotEmpty(this.hostName) ? this.hostName : null;
      }
   }

   public String getSmtpPort() {
      if (this.session != null) {
         return this.session.getProperty("mail.smtp.port");
      } else {
         return EmailUtils.isNotEmpty(this.smtpPort) ? this.smtpPort : null;
      }
   }

   public boolean isStartTLSRequired() {
      return this.startTlsRequired;
   }

   public boolean isStartTLSEnabled() {
      return this.startTlsEnabled || this.tls;
   }

   /** @deprecated */
   @Deprecated
   public boolean isTLS() {
      return this.isStartTLSEnabled();
   }

   protected InternetAddress[] toInternetAddressArray(List<InternetAddress> list) {
      return (InternetAddress[])list.toArray(new InternetAddress[list.size()]);
   }

   public void setPopBeforeSmtp(boolean newPopBeforeSmtp, String newPopHost, String newPopUsername, String newPopPassword) {
      this.popBeforeSmtp = newPopBeforeSmtp;
      this.popHost = newPopHost;
      this.popUsername = newPopUsername;
      this.popPassword = newPopPassword;
   }

   /** @deprecated */
   @Deprecated
   public boolean isSSL() {
      return this.isSSLOnConnect();
   }

   public boolean isSSLOnConnect() {
      return this.sslOnConnect || this.ssl;
   }

   /** @deprecated */
   @Deprecated
   public void setSSL(boolean ssl) {
      this.setSSLOnConnect(ssl);
   }

   public Email setSSLOnConnect(boolean ssl) {
      this.checkSessionAlreadyInitialized();
      this.sslOnConnect = ssl;
      this.ssl = ssl;
      return this;
   }

   public boolean isSSLCheckServerIdentity() {
      return this.sslCheckServerIdentity;
   }

   public Email setSSLCheckServerIdentity(boolean sslCheckServerIdentity) {
      this.checkSessionAlreadyInitialized();
      this.sslCheckServerIdentity = sslCheckServerIdentity;
      return this;
   }

   public String getSslSmtpPort() {
      if (this.session != null) {
         return this.session.getProperty("mail.smtp.socketFactory.port");
      } else {
         return EmailUtils.isNotEmpty(this.sslSmtpPort) ? this.sslSmtpPort : null;
      }
   }

   public void setSslSmtpPort(String sslSmtpPort) {
      this.checkSessionAlreadyInitialized();
      this.sslSmtpPort = sslSmtpPort;
   }

   public boolean isSendPartial() {
      return this.sendPartial;
   }

   public Email setSendPartial(boolean sendPartial) {
      this.checkSessionAlreadyInitialized();
      this.sendPartial = sendPartial;
      return this;
   }

   public List<InternetAddress> getToAddresses() {
      return this.toList;
   }

   public List<InternetAddress> getCcAddresses() {
      return this.ccList;
   }

   public List<InternetAddress> getBccAddresses() {
      return this.bccList;
   }

   public List<InternetAddress> getReplyToAddresses() {
      return this.replyList;
   }

   public int getSocketConnectionTimeout() {
      return this.socketConnectionTimeout;
   }

   public void setSocketConnectionTimeout(int socketConnectionTimeout) {
      this.checkSessionAlreadyInitialized();
      this.socketConnectionTimeout = socketConnectionTimeout;
   }

   public int getSocketTimeout() {
      return this.socketTimeout;
   }

   public void setSocketTimeout(int socketTimeout) {
      this.checkSessionAlreadyInitialized();
      this.socketTimeout = socketTimeout;
   }

   protected MimeMessage createMimeMessage(Session aSession) {
      return new MimeMessage(aSession);
   }

   private String createFoldedHeaderValue(String name, String value) {
      if (EmailUtils.isEmpty(name)) {
         throw new IllegalArgumentException("name can not be null or empty");
      } else if (value != null && !EmailUtils.isEmpty(value)) {
         try {
            return MimeUtility.fold(name.length() + 2, MimeUtility.encodeText(value, this.charset, (String)null));
         } catch (UnsupportedEncodingException var4) {
            return value;
         }
      } else {
         throw new IllegalArgumentException("value can not be null or empty");
      }
   }

   private InternetAddress createInternetAddress(String email, String name, String charsetName) throws EmailException {
      try {
         InternetAddress address = new InternetAddress((new IDNEmailAddressConverter()).toASCII(email));
         if (EmailUtils.isNotEmpty(name)) {
            if (EmailUtils.isEmpty(charsetName)) {
               address.setPersonal(name);
            } else {
               Charset set = Charset.forName(charsetName);
               address.setPersonal(name, set.name());
            }
         }

         address.validate();
         return address;
      } catch (AddressException var6) {
         throw new EmailException(var6);
      } catch (UnsupportedEncodingException var7) {
         throw new EmailException(var7);
      }
   }

   private void checkSessionAlreadyInitialized() {
      if (this.session != null) {
         throw new IllegalStateException("The mail session is already initialized");
      }
   }
}
