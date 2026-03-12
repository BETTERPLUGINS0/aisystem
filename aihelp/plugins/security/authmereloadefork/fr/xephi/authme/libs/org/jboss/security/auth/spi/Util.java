package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.crypto.digest.DigestCallback;
import fr.xephi.authme.libs.org.jboss.security.Base64Encoder;
import fr.xephi.authme.libs.org.jboss.security.Base64Utils;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SimpleGroup;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.MessageDigest;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.security.auth.login.LoginException;

public class Util {
   public static final String BASE64_ENCODING = "BASE64";
   public static final String BASE16_ENCODING = "HEX";
   public static final String RFC2617_ENCODING = "RFC2617";
   private static char[] MD5_HEX = "0123456789abcdef".toCharArray();

   static Group[] getRoleSets(String targetUser, Properties roles, char roleGroupSeperator, AbstractServerLoginModule aslm) {
      Enumeration<?> users = roles.propertyNames();
      SimpleGroup rolesGroup = new SimpleGroup("Roles");
      ArrayList<Group> groups = new ArrayList();
      groups.add(rolesGroup);

      while(users.hasMoreElements() && targetUser != null) {
         String user = (String)users.nextElement();
         String value = roles.getProperty(user);
         int index = user.indexOf(roleGroupSeperator, targetUser.length());
         boolean isRoleGroup = false;
         boolean userMatch = false;
         if (index > 0 && targetUser.regionMatches(0, user, 0, index)) {
            isRoleGroup = true;
         } else {
            userMatch = targetUser.equals(user);
         }

         String groupName = "Roles";
         if (isRoleGroup) {
            groupName = user.substring(index + 1);
            PicketBoxLogger.LOGGER.traceAdditionOfRoleToGroup(value, groupName);
            if (groupName.equals("Roles")) {
               parseGroupMembers(rolesGroup, value, aslm);
            } else {
               SimpleGroup group = new SimpleGroup(groupName);
               parseGroupMembers(group, value, aslm);
               groups.add(group);
            }
         } else if (userMatch) {
            PicketBoxLogger.LOGGER.traceAdditionOfRoleToGroup(value, groupName);
            parseGroupMembers(rolesGroup, value, aslm);
         }
      }

      Group[] roleSets = new Group[groups.size()];
      groups.toArray(roleSets);
      return roleSets;
   }

   static Group[] getRoleSets(String username, String dsJndiName, String txManagerJndiName, String rolesQuery, AbstractServerLoginModule aslm) throws LoginException {
      return getRoleSets(username, dsJndiName, txManagerJndiName, rolesQuery, aslm, false);
   }

   static Group[] getRoleSets(String username, String dsJndiName, String txManagerJndiName, String rolesQuery, AbstractServerLoginModule aslm, boolean suspendResume) throws LoginException {
      return DbUtil.getRoleSets(username, dsJndiName, txManagerJndiName, rolesQuery, aslm, suspendResume);
   }

   static Properties loadProperties(String defaultsName, String propertiesName) throws IOException {
      Properties bundle = new Properties();
      ClassLoader loader = SecurityActions.getContextClassLoader();
      URL defaultUrl = null;
      URL url = null;
      URLClassLoader ucl;
      if (loader instanceof URLClassLoader) {
         ucl = (URLClassLoader)loader;
         defaultUrl = SecurityActions.findResource(ucl, defaultsName);
         url = SecurityActions.findResource(ucl, propertiesName);
         PicketBoxLogger.LOGGER.traceAttemptToLoadResource(propertiesName);
      }

      File tmp;
      if (defaultUrl == null) {
         defaultUrl = loader.getResource(defaultsName);
         if (defaultUrl == null) {
            try {
               defaultUrl = new URL(defaultsName);
            } catch (MalformedURLException var27) {
               PicketBoxLogger.LOGGER.debugFailureToOpenPropertiesFromURL(var27);
               tmp = new File(defaultsName);
               if (tmp.exists()) {
                  defaultUrl = tmp.toURI().toURL();
               }
            }
         }
      }

      if (url == null) {
         url = loader.getResource(propertiesName);
         if (url == null) {
            try {
               url = new URL(propertiesName);
            } catch (MalformedURLException var26) {
               PicketBoxLogger.LOGGER.debugFailureToOpenPropertiesFromURL(var26);
               tmp = new File(propertiesName);
               if (tmp.exists()) {
                  url = tmp.toURI().toURL();
               }
            }
         }
      }

      if (url == null && defaultUrl == null) {
         String propertiesFiles = propertiesName + "/" + defaultsName;
         throw PicketBoxMessages.MESSAGES.unableToFindPropertiesFile(propertiesFiles);
      } else {
         InputStream is;
         if (url != null) {
            ucl = null;

            try {
               is = SecurityActions.openStream(url);
            } catch (PrivilegedActionException var23) {
               throw new IOException(var23.getLocalizedMessage());
            }

            if (is == null) {
               throw PicketBoxMessages.MESSAGES.unableToLoadPropertiesFile(propertiesName);
            }

            try {
               bundle.load(is);
            } finally {
               safeClose(is);
            }

            PicketBoxLogger.LOGGER.tracePropertiesFileLoaded(propertiesName, bundle.keySet());
         } else {
            is = null;

            try {
               is = defaultUrl.openStream();
               bundle.load(is);
               PicketBoxLogger.LOGGER.tracePropertiesFileLoaded(defaultsName, bundle.keySet());
            } catch (Throwable var24) {
               PicketBoxLogger.LOGGER.debugFailureToLoadPropertiesFile(defaultsName, var24);
            } finally {
               safeClose(is);
            }
         }

         return bundle;
      }
   }

   static Properties loadProperties(String propertiesName) throws IOException {
      Properties bundle = null;
      ClassLoader loader = SecurityActions.getContextClassLoader();
      URL url = null;
      if (loader instanceof URLClassLoader) {
         URLClassLoader ucl = (URLClassLoader)loader;
         url = SecurityActions.findResource(ucl, propertiesName);
         PicketBoxLogger.LOGGER.traceAttemptToLoadResource(propertiesName);
      }

      File tmp;
      if (url == null) {
         url = loader.getResource(propertiesName);
         if (url == null) {
            try {
               url = new URL(propertiesName);
            } catch (MalformedURLException var13) {
               PicketBoxLogger.LOGGER.debugFailureToOpenPropertiesFromURL(var13);
               tmp = new File(propertiesName);
               if (tmp.exists()) {
                  url = tmp.toURI().toURL();
               }
            }
         }
      }

      if (url == null) {
         throw PicketBoxMessages.MESSAGES.unableToFindPropertiesFile(propertiesName);
      } else {
         Properties defaults = new Properties();
         bundle = new Properties(defaults);
         if (url != null) {
            tmp = null;

            InputStream is;
            try {
               is = SecurityActions.openStream(url);
            } catch (PrivilegedActionException var12) {
               throw new IOException(var12.getLocalizedMessage());
            }

            if (is == null) {
               throw PicketBoxMessages.MESSAGES.unableToLoadPropertiesFile(propertiesName);
            }

            try {
               bundle.load(is);
            } finally {
               safeClose(is);
            }

            PicketBoxLogger.LOGGER.tracePropertiesFileLoaded(propertiesName, bundle.keySet());
         }

         return bundle;
      }
   }

   static void parseGroupMembers(Group group, String roles, AbstractServerLoginModule aslm) {
      StringTokenizer tokenizer = new StringTokenizer(roles, ",");

      while(tokenizer.hasMoreTokens()) {
         String token = tokenizer.nextToken();

         try {
            Principal p = aslm.createIdentity(token);
            group.addMember(p);
         } catch (Exception var6) {
            PicketBoxLogger.LOGGER.debugFailureToCreatePrincipal(token, var6);
         }
      }

   }

   public static String createPasswordHash(String hashAlgorithm, String hashEncoding, String hashCharset, String username, String password) {
      return createPasswordHash(hashAlgorithm, hashEncoding, hashCharset, username, password, (DigestCallback)null);
   }

   public static String createPasswordHash(String hashAlgorithm, String hashEncoding, String hashCharset, String username, String password, DigestCallback callback) {
      String passwordHash = null;

      byte[] passBytes;
      try {
         if (hashCharset == null) {
            passBytes = password.getBytes();
         } else {
            passBytes = password.getBytes(hashCharset);
         }
      } catch (UnsupportedEncodingException var11) {
         PicketBoxLogger.LOGGER.errorFindingCharset(hashCharset, var11);
         passBytes = password.getBytes();
      }

      try {
         MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
         if (callback != null) {
            callback.preDigest(md);
         }

         md.update(passBytes);
         if (callback != null) {
            callback.postDigest(md);
         }

         byte[] hash = md.digest();
         if (hashEncoding.equalsIgnoreCase("BASE64")) {
            passwordHash = encodeBase64(hash);
         } else if (hashEncoding.equalsIgnoreCase("HEX")) {
            passwordHash = encodeBase16(hash);
         } else if (hashEncoding.equalsIgnoreCase("RFC2617")) {
            passwordHash = encodeRFC2617(hash);
         } else {
            PicketBoxLogger.LOGGER.unsupportedHashEncodingFormat(hashEncoding);
         }
      } catch (Exception var10) {
         PicketBoxLogger.LOGGER.errorCalculatingPasswordHash(var10);
      }

      return passwordHash;
   }

   public static String encodeRFC2617(byte[] data) {
      char[] hash = new char[32];

      for(int i = 0; i < 16; ++i) {
         int j = data[i] >> 4 & 15;
         hash[i * 2] = MD5_HEX[j];
         j = data[i] & 15;
         hash[i * 2 + 1] = MD5_HEX[j];
      }

      return new String(hash);
   }

   public static String encodeBase16(byte[] bytes) {
      StringBuffer sb = new StringBuffer(bytes.length * 2);

      for(int i = 0; i < bytes.length; ++i) {
         byte b = bytes[i];
         char c = (char)(b >> 4 & 15);
         if (c > '\t') {
            c = (char)(c - 10 + 97);
         } else {
            c = (char)(c + 48);
         }

         sb.append(c);
         c = (char)(b & 15);
         if (c > '\t') {
            c = (char)(c - 10 + 97);
         } else {
            c = (char)(c + 48);
         }

         sb.append(c);
      }

      return sb.toString();
   }

   public static String encodeBase64(byte[] bytes) {
      String base64 = null;

      try {
         base64 = Base64Encoder.encode(bytes);
      } catch (Exception var3) {
      }

      return base64;
   }

   public static String tob64(byte[] buffer) {
      return Base64Utils.tob64(buffer);
   }

   public static byte[] fromb64(String str) throws NumberFormatException {
      return Base64Utils.fromb64(str);
   }

   private static void safeClose(InputStream fis) {
      try {
         if (fis != null) {
            fis.close();
         }
      } catch (Exception var2) {
      }

   }
}
