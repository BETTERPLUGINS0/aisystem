package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.gssapi;

import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.ScramStringFormatting;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.AbstractStringWritable;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.StringWritableCsv;

public class Gs2Header extends AbstractStringWritable {
   private final Gs2AttributeValue cbind;
   private final Gs2AttributeValue authzid;

   public Gs2Header(Gs2CbindFlag cbindFlag, String cbName, String authzid) throws IllegalArgumentException {
      Preconditions.checkNotNull(cbindFlag, "cbindFlag");
      if (cbindFlag == Gs2CbindFlag.CHANNEL_BINDING_REQUIRED ^ cbName != null) {
         throw new IllegalArgumentException("Specify channel binding flag and value together, or none");
      } else {
         this.cbind = new Gs2AttributeValue(Gs2Attributes.byGS2CbindFlag(cbindFlag), cbName);
         this.authzid = authzid == null ? null : new Gs2AttributeValue(Gs2Attributes.AUTHZID, ScramStringFormatting.toSaslName(authzid));
      }
   }

   public Gs2Header(Gs2CbindFlag cbindFlag, String cbName) throws IllegalArgumentException {
      this(cbindFlag, cbName, (String)null);
   }

   public Gs2Header(Gs2CbindFlag cbindFlag) {
      this(cbindFlag, (String)null, (String)null);
   }

   public Gs2CbindFlag getChannelBindingFlag() {
      return Gs2CbindFlag.byChar(this.cbind.getChar());
   }

   public String getChannelBindingName() {
      return this.cbind.getValue();
   }

   public String getAuthzid() {
      return this.authzid != null ? this.authzid.getValue() : null;
   }

   public StringBuffer writeTo(StringBuffer sb) {
      return StringWritableCsv.writeTo(sb, this.cbind, this.authzid);
   }

   public static Gs2Header parseFrom(String message) throws IllegalArgumentException {
      Preconditions.checkNotNull(message, "Null message");
      String[] gs2HeaderSplit = StringWritableCsv.parseFrom(message, 2);
      if (gs2HeaderSplit.length == 0) {
         throw new IllegalArgumentException("Invalid number of fields for the GS2 Header");
      } else {
         Gs2AttributeValue gs2cbind = Gs2AttributeValue.parse(gs2HeaderSplit[0]);
         return new Gs2Header(Gs2CbindFlag.byChar(gs2cbind.getChar()), gs2cbind.getValue(), gs2HeaderSplit[1] != null && !gs2HeaderSplit[1].isEmpty() ? Gs2AttributeValue.parse(gs2HeaderSplit[1]).getValue() : null);
      }
   }
}
