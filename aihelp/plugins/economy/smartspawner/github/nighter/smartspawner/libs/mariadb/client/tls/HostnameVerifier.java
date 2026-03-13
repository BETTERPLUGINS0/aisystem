package github.nighter.smartspawner.libs.mariadb.client.tls;

import github.nighter.smartspawner.libs.mariadb.util.log.Logger;
import github.nighter.smartspawner.libs.mariadb.util.log.Loggers;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.net.ssl.SSLException;
import javax.security.auth.x500.X500Principal;

public class HostnameVerifier {
   private static final Logger logger = Loggers.getLogger(HostnameVerifier.class);
   private static final Pattern IP_V4 = Pattern.compile("^(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.)(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){2}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
   private static final Pattern IP_V6 = Pattern.compile("^[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){7}$");
   private static final Pattern IP_V6_COMPRESSED = Pattern.compile("^(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)::(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)$");

   private static boolean matchDns(String hostname, String tlsDnsPattern) throws SSLException {
      boolean hostIsIp = isIPv4(hostname) || isIPv6(hostname);
      StringTokenizer hostnameSt = new StringTokenizer(hostname.toLowerCase(Locale.ROOT), ".");
      StringTokenizer templateSt = new StringTokenizer(tlsDnsPattern.toLowerCase(Locale.ROOT), ".");
      if (hostnameSt.countTokens() != templateSt.countTokens()) {
         return false;
      } else {
         try {
            do {
               if (!hostnameSt.hasMoreTokens()) {
                  return true;
               }
            } while(matchWildCards(hostIsIp, hostnameSt.nextToken(), templateSt.nextToken()));

            return false;
         } catch (SSLException var6) {
            throw new SSLException(normalizedHostMsg(hostname) + " doesn't correspond to certificate CN \"" + tlsDnsPattern + "\" : wildcards not possible for IPs");
         }
      }
   }

   private static boolean matchWildCards(boolean hostIsIp, String hostnameToken, String tlsDnsToken) throws SSLException {
      int wildcardIndex = tlsDnsToken.indexOf("*");
      String token = hostnameToken;
      if (wildcardIndex == -1) {
         return hostnameToken.equals(tlsDnsToken);
      } else if (hostIsIp) {
         throw new SSLException("WildCards not possible when using IP's");
      } else {
         boolean first = true;

         String afterWildcard;
         for(afterWildcard = tlsDnsToken; wildcardIndex != -1; wildcardIndex = afterWildcard.indexOf("*")) {
            String beforeWildcard = afterWildcard.substring(0, wildcardIndex);
            afterWildcard = afterWildcard.substring(wildcardIndex + 1);
            int beforeStartIdx = token.indexOf(beforeWildcard);
            if (beforeStartIdx == -1 || first && beforeStartIdx != 0) {
               return false;
            }

            first = false;
            token = token.substring(beforeStartIdx + beforeWildcard.length());
         }

         return token.endsWith(afterWildcard);
      }
   }

   private static String extractCommonName(String principal) throws SSLException {
      if (principal == null) {
         return null;
      } else {
         try {
            LdapName ldapName = new LdapName(principal);
            Iterator var2 = ldapName.getRdns().iterator();

            while(var2.hasNext()) {
               Rdn rdn = (Rdn)var2.next();
               if (rdn.getType().equalsIgnoreCase("CN")) {
                  Object obj = rdn.getValue();
                  if (obj != null) {
                     return obj.toString();
                  }
               }
            }

            return null;
         } catch (InvalidNameException var5) {
            throw new SSLException("DN value \"" + principal + "\" is invalid");
         }
      }
   }

   private static String normaliseAddress(String hostname) {
      try {
         if (hostname == null) {
            return null;
         } else {
            InetAddress inetAddress = InetAddress.getByName(hostname);
            return inetAddress.getHostAddress();
         }
      } catch (UnknownHostException var2) {
         return hostname;
      }
   }

   private static String normalizedHostMsg(String normalizedHost) {
      StringBuilder msg = new StringBuilder();
      if (isIPv4(normalizedHost)) {
         msg.append("IPv4 host \"");
      } else if (isIPv6(normalizedHost)) {
         msg.append("IPv6 host \"");
      } else {
         msg.append("DNS host \"");
      }

      msg.append(normalizedHost).append("\"");
      return msg.toString();
   }

   public static boolean isIPv4(String ip) {
      return IP_V4.matcher(ip).matches();
   }

   public static boolean isIPv6(String ip) {
      return IP_V6.matcher(ip).matches() || IP_V6_COMPRESSED.matcher(ip).matches();
   }

   private static HostnameVerifier.SubjectAltNames getSubjectAltNames(X509Certificate cert) throws CertificateParsingException {
      Collection<List<?>> entries = cert.getSubjectAlternativeNames();
      HostnameVerifier.SubjectAltNames subjectAltNames = new HostnameVerifier.SubjectAltNames();
      if (entries != null) {
         Iterator var3 = entries.iterator();

         while(var3.hasNext()) {
            List<?> entry = (List)var3.next();
            if (entry.size() >= 2) {
               int type = (Integer)entry.get(0);
               String altNameIp;
               if (type == 2) {
                  altNameIp = (String)entry.get(1);
                  if (altNameIp != null) {
                     String normalizedSubjectAlt = altNameIp.toLowerCase(Locale.ROOT);
                     subjectAltNames.add(new HostnameVerifier.GeneralName(normalizedSubjectAlt, HostnameVerifier.Extension.DNS));
                  }
               }

               if (type == 7) {
                  altNameIp = (String)entry.get(1);
                  if (altNameIp != null) {
                     subjectAltNames.add(new HostnameVerifier.GeneralName(altNameIp, HostnameVerifier.Extension.IP));
                  }
               }
            }
         }
      }

      return subjectAltNames;
   }

   public static void verify(String host, X509Certificate cert, long serverThreadId) throws SSLException {
      if (host != null) {
         String lowerCaseHost = host.toLowerCase(Locale.ROOT);

         try {
            HostnameVerifier.SubjectAltNames subjectAltNames = getSubjectAltNames(cert);
            if (!subjectAltNames.isEmpty()) {
               Iterator var6;
               HostnameVerifier.GeneralName entry;
               if (isIPv4(lowerCaseHost)) {
                  var6 = subjectAltNames.getGeneralNames().iterator();

                  while(var6.hasNext()) {
                     entry = (HostnameVerifier.GeneralName)var6.next();
                     if (logger.isTraceEnabled()) {
                        logger.trace("Conn={}. IPv4 verification of hostname : type={} value={} to {}", serverThreadId, entry.extension, entry.value, lowerCaseHost);
                     }

                     if (entry.extension == HostnameVerifier.Extension.IP && lowerCaseHost.equals(entry.value)) {
                        return;
                     }
                  }
               } else if (isIPv6(lowerCaseHost)) {
                  String normalisedHost = normaliseAddress(lowerCaseHost);
                  Iterator var13 = subjectAltNames.getGeneralNames().iterator();

                  while(var13.hasNext()) {
                     HostnameVerifier.GeneralName entry = (HostnameVerifier.GeneralName)var13.next();
                     if (logger.isTraceEnabled()) {
                        logger.trace("Conn={}. IPv6 verification of hostname : type={} value={} to {}", serverThreadId, entry.extension, entry.value, lowerCaseHost);
                     }

                     if (entry.extension == HostnameVerifier.Extension.IP && !isIPv4(entry.value) && normalisedHost.equals(normaliseAddress(entry.value))) {
                        return;
                     }
                  }
               } else {
                  var6 = subjectAltNames.getGeneralNames().iterator();

                  while(var6.hasNext()) {
                     entry = (HostnameVerifier.GeneralName)var6.next();
                     if (logger.isTraceEnabled()) {
                        logger.trace("Conn={}. DNS verification of hostname : type={} value={} to {}", serverThreadId, entry.extension, entry.value, lowerCaseHost);
                     }

                     if (entry.extension == HostnameVerifier.Extension.DNS && matchDns(lowerCaseHost, entry.value.toLowerCase(Locale.ROOT))) {
                        return;
                     }
                  }
               }
            }

            X500Principal subjectPrincipal = cert.getSubjectX500Principal();
            String cn = extractCommonName(subjectPrincipal.getName("RFC2253"));
            if (cn == null) {
               if (subjectAltNames.isEmpty()) {
                  throw new SSLException("CN not found in certificate principal \"" + subjectPrincipal + "\" and certificate doesn't contain SAN");
               } else {
                  throw new SSLException("CN not found in certificate principal \"" + subjectPrincipal + "\" and " + normalizedHostMsg(lowerCaseHost) + " doesn't correspond to " + subjectAltNames);
               }
            } else {
               String normalizedCn = cn.toLowerCase(Locale.ROOT);
               if (logger.isTraceEnabled()) {
                  logger.trace("Conn={}. DNS verification of hostname : CN={} to {}", serverThreadId, normalizedCn, lowerCaseHost);
               }

               if (!matchDns(lowerCaseHost, normalizedCn)) {
                  String errorMsg = normalizedHostMsg(lowerCaseHost) + " doesn't correspond to certificate CN \"" + normalizedCn + "\"";
                  if (!subjectAltNames.isEmpty()) {
                     errorMsg = errorMsg + " and " + subjectAltNames;
                  }

                  throw new SSLException(errorMsg);
               }
            }
         } catch (CertificateParsingException var10) {
            throw new SSLException("certificate parsing error : " + var10.getMessage());
         }
      }
   }

   private static class SubjectAltNames {
      private final List<HostnameVerifier.GeneralName> generalNames;

      private SubjectAltNames() {
         this.generalNames = new ArrayList();
      }

      public String toString() {
         StringBuilder sb = new StringBuilder("SAN[");
         boolean first = true;
         Iterator var3 = this.generalNames.iterator();

         while(var3.hasNext()) {
            HostnameVerifier.GeneralName generalName = (HostnameVerifier.GeneralName)var3.next();
            if (!first) {
               sb.append(",");
            }

            first = false;
            sb.append(generalName.toString());
         }

         sb.append("]");
         return sb.toString();
      }

      public List<HostnameVerifier.GeneralName> getGeneralNames() {
         return this.generalNames;
      }

      public void add(HostnameVerifier.GeneralName generalName) {
         this.generalNames.add(generalName);
      }

      public boolean isEmpty() {
         return this.generalNames.isEmpty();
      }

      // $FF: synthetic method
      SubjectAltNames(Object x0) {
         this();
      }
   }

   private static class GeneralName {
      private final String value;
      private final HostnameVerifier.Extension extension;

      public GeneralName(String value, HostnameVerifier.Extension extension) {
         this.value = value;
         this.extension = extension;
      }

      public String toString() {
         return "{" + this.extension + ":\"" + this.value + "\"}";
      }
   }

   private static enum Extension {
      DNS,
      IP;

      // $FF: synthetic method
      private static HostnameVerifier.Extension[] $values() {
         return new HostnameVerifier.Extension[]{DNS, IP};
      }
   }
}
