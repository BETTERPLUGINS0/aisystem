package fr.xephi.authme.libs.org.apache.commons.mail.util;

import java.net.IDN;
import javax.mail.internet.InternetAddress;

public class IDNEmailAddressConverter {
   public String toASCII(String email) {
      int idx = this.findAtSymbolIndex(email);
      return idx < 0 ? email : this.getLocalPart(email, idx) + '@' + IDN.toASCII(this.getDomainPart(email, idx));
   }

   String toUnicode(InternetAddress address) {
      return address != null ? this.toUnicode(address.getAddress()) : null;
   }

   String toUnicode(String email) {
      int idx = this.findAtSymbolIndex(email);
      return idx < 0 ? email : this.getLocalPart(email, idx) + '@' + IDN.toUnicode(this.getDomainPart(email, idx));
   }

   private String getLocalPart(String email, int idx) {
      return email.substring(0, idx);
   }

   private String getDomainPart(String email, int idx) {
      return email.substring(idx + 1);
   }

   private int findAtSymbolIndex(String value) {
      return value == null ? -1 : value.indexOf(64);
   }
}
