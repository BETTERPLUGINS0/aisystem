package fr.xephi.authme.libs.org.postgresql.ssl;

import fr.xephi.authme.libs.org.postgresql.util.GT;
import java.net.IDN;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGjdbcHostnameVerifier implements HostnameVerifier {
   private static final Logger LOGGER = Logger.getLogger(PGjdbcHostnameVerifier.class.getName());
   public static final PGjdbcHostnameVerifier INSTANCE = new PGjdbcHostnameVerifier();
   private static final int TYPE_DNS_NAME = 2;
   private static final int TYPE_IP_ADDRESS = 7;
   public static final Comparator<String> HOSTNAME_PATTERN_COMPARATOR = new Comparator<String>() {
      private int countChars(String value, char ch) {
         int count = 0;
         int pos = -1;

         while(true) {
            pos = value.indexOf(ch, pos + 1);
            if (pos == -1) {
               return count;
            }

            ++count;
         }
      }

      public int compare(String o1, String o2) {
         int d1 = this.countChars(o1, '.');
         int d2 = this.countChars(o2, '.');
         if (d1 != d2) {
            return d1 > d2 ? 1 : -1;
         } else {
            int s1 = this.countChars(o1, '*');
            int s2 = this.countChars(o2, '*');
            if (s1 != s2) {
               return s1 < s2 ? 1 : -1;
            } else {
               int l1 = o1.length();
               int l2 = o2.length();
               if (l1 != l2) {
                  return l1 > l2 ? 1 : -1;
               } else {
                  return 0;
               }
            }
         }
      }
   };

   public boolean verify(String hostname, SSLSession session) {
      X509Certificate[] peerCerts;
      try {
         peerCerts = (X509Certificate[])session.getPeerCertificates();
      } catch (SSLPeerUnverifiedException var14) {
         LOGGER.log(Level.SEVERE, GT.tr("Unable to parse X509Certificate for hostname {0}", hostname), var14);
         return false;
      }

      if (peerCerts != null && peerCerts.length != 0) {
         String canonicalHostname;
         if (hostname.startsWith("[") && hostname.endsWith("]")) {
            canonicalHostname = hostname.substring(1, hostname.length() - 1);
         } else {
            try {
               canonicalHostname = IDN.toASCII(hostname);
               if (LOGGER.isLoggable(Level.FINEST)) {
                  LOGGER.log(Level.FINEST, "Canonical host name for {0} is {1}", new Object[]{hostname, canonicalHostname});
               }
            } catch (IllegalArgumentException var13) {
               LOGGER.log(Level.SEVERE, GT.tr("Hostname {0} is invalid", hostname), var13);
               return false;
            }
         }

         X509Certificate serverCert = peerCerts[0];

         Object subjectAltNames;
         try {
            subjectAltNames = serverCert.getSubjectAlternativeNames();
            if (subjectAltNames == null) {
               subjectAltNames = Collections.emptyList();
            }
         } catch (CertificateParsingException var15) {
            LOGGER.log(Level.SEVERE, GT.tr("Unable to parse certificates for hostname {0}", hostname), var15);
            return false;
         }

         boolean anyDnsSan = false;
         Iterator var8 = ((Collection)subjectAltNames).iterator();

         String san;
         do {
            Integer sanType;
            do {
               List sanItem;
               do {
                  do {
                     do {
                        if (!var8.hasNext()) {
                           if (anyDnsSan) {
                              LOGGER.log(Level.SEVERE, GT.tr("Server name validation failed: certificate for host {0} dNSName entries subjectAltName, but none of them match. Assuming server name validation failed", hostname));
                              return false;
                           }

                           LdapName dn;
                           try {
                              dn = new LdapName(serverCert.getSubjectX500Principal().getName("RFC2253"));
                           } catch (InvalidNameException var12) {
                              LOGGER.log(Level.SEVERE, GT.tr("Server name validation failed: unable to extract common name from X509Certificate for hostname {0}", hostname), var12);
                              return false;
                           }

                           List<String> commonNames = new ArrayList(1);
                           Iterator var18 = dn.getRdns().iterator();

                           while(var18.hasNext()) {
                              Rdn rdn = (Rdn)var18.next();
                              if ("CN".equals(rdn.getType())) {
                                 commonNames.add((String)rdn.getValue());
                              }
                           }

                           if (commonNames.isEmpty()) {
                              LOGGER.log(Level.SEVERE, GT.tr("Server name validation failed: certificate for hostname {0} has no DNS subjectAltNames, and it CommonName is missing as well", hostname));
                              return false;
                           }

                           if (commonNames.size() > 1) {
                              Collections.sort(commonNames, HOSTNAME_PATTERN_COMPARATOR);
                           }

                           String commonName = (String)commonNames.get(commonNames.size() - 1);
                           boolean result = this.verifyHostName(canonicalHostname, commonName);
                           if (!result) {
                              LOGGER.log(Level.SEVERE, GT.tr("Server name validation failed: hostname {0} does not match common name {1}", hostname, commonName));
                           }

                           return result;
                        }

                        sanItem = (List)var8.next();
                     } while(sanItem.size() != 2);

                     sanType = (Integer)sanItem.get(0);
                  } while(sanType == null);
               } while(sanType != 7 && sanType != 2);

               san = (String)sanItem.get(1);
            } while(sanType == 7 && san != null && san.startsWith("*"));

            anyDnsSan |= sanType == 2;
         } while(!this.verifyHostName(canonicalHostname, san));

         if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, GT.tr("Server name validation pass for {0}, subjectAltName {1}", hostname, san));
         }

         return true;
      } else {
         LOGGER.log(Level.SEVERE, GT.tr("No certificates found for hostname {0}", hostname));
         return false;
      }
   }

   public boolean verifyHostName(@Nullable String hostname, @Nullable String pattern) {
      if (hostname != null && pattern != null) {
         int lastStar = pattern.lastIndexOf(42);
         if (lastStar == -1) {
            return hostname.equalsIgnoreCase(pattern);
         } else if (lastStar > 0) {
            return false;
         } else if (pattern.indexOf(46) == -1) {
            return false;
         } else if (hostname.length() < pattern.length() - 1) {
            return false;
         } else {
            boolean ignoreCase = true;
            int toffset = hostname.length() - pattern.length() + 1;
            return hostname.lastIndexOf(46, toffset - 1) >= 0 ? false : hostname.regionMatches(true, toffset, pattern, 1, pattern.length() - 1);
         }
      } else {
         return false;
      }
   }
}
