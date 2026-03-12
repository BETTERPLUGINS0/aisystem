package fr.xephi.authme.libs.com.google.common.net;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.net.InetAddress;
import java.text.ParseException;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public final class HostSpecifier {
   private final String canonicalForm;

   private HostSpecifier(String canonicalForm) {
      this.canonicalForm = canonicalForm;
   }

   public static HostSpecifier fromValid(String specifier) {
      HostAndPort parsedHost = HostAndPort.fromString(specifier);
      Preconditions.checkArgument(!parsedHost.hasPort());
      String host = parsedHost.getHost();
      InetAddress addr = null;

      try {
         addr = InetAddresses.forString(host);
      } catch (IllegalArgumentException var5) {
      }

      if (addr != null) {
         return new HostSpecifier(InetAddresses.toUriString(addr));
      } else {
         InternetDomainName domain = InternetDomainName.from(host);
         if (domain.hasPublicSuffix()) {
            return new HostSpecifier(domain.toString());
         } else {
            IllegalArgumentException var10000 = new IllegalArgumentException;
            String var10003 = String.valueOf(host);
            String var10002;
            if (var10003.length() != 0) {
               var10002 = "Domain name does not have a recognized public suffix: ".concat(var10003);
            } else {
               String var10004 = new String;
               var10002 = var10004;
               var10004.<init>("Domain name does not have a recognized public suffix: ");
            }

            var10000.<init>(var10002);
            throw var10000;
         }
      }
   }

   @CanIgnoreReturnValue
   public static HostSpecifier from(String specifier) throws ParseException {
      try {
         return fromValid(specifier);
      } catch (IllegalArgumentException var3) {
         ParseException var10000 = new ParseException;
         String var10003 = String.valueOf(specifier);
         String var10002;
         if (var10003.length() != 0) {
            var10002 = "Invalid host specifier: ".concat(var10003);
         } else {
            String var10004 = new String;
            var10002 = var10004;
            var10004.<init>("Invalid host specifier: ");
         }

         var10000.<init>(var10002, 0);
         ParseException parseException = var10000;
         parseException.initCause(var3);
         throw parseException;
      }
   }

   public static boolean isValid(String specifier) {
      try {
         HostSpecifier unused = fromValid(specifier);
         return true;
      } catch (IllegalArgumentException var2) {
         return false;
      }
   }

   public boolean equals(@CheckForNull Object other) {
      if (this == other) {
         return true;
      } else if (other instanceof HostSpecifier) {
         HostSpecifier that = (HostSpecifier)other;
         return this.canonicalForm.equals(that.canonicalForm);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.canonicalForm.hashCode();
   }

   public String toString() {
      return this.canonicalForm;
   }
}
