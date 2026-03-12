package fr.xephi.authme.libs.org.postgresql.xa;

import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javax.transaction.xa.Xid;
import org.checkerframework.checker.nullness.qual.Nullable;

class RecoveredXid implements Xid {
   int formatId;
   byte[] globalTransactionId;
   byte[] branchQualifier;

   RecoveredXid(int formatId, byte[] globalTransactionId, byte[] branchQualifier) {
      this.formatId = formatId;
      this.globalTransactionId = globalTransactionId;
      this.branchQualifier = branchQualifier;
   }

   public int getFormatId() {
      return this.formatId;
   }

   public byte[] getGlobalTransactionId() {
      return this.globalTransactionId;
   }

   public byte[] getBranchQualifier() {
      return this.branchQualifier;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + Arrays.hashCode(this.branchQualifier);
      result = 31 * result + this.formatId;
      result = 31 * result + Arrays.hashCode(this.globalTransactionId);
      return result;
   }

   public boolean equals(@Nullable Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Xid)) {
         return false;
      } else {
         Xid other = (Xid)o;
         return this.formatId == other.getFormatId() && Arrays.equals(this.globalTransactionId, other.getGlobalTransactionId()) && Arrays.equals(this.branchQualifier, other.getBranchQualifier());
      }
   }

   public String toString() {
      return xidToString(this);
   }

   static String xidToString(Xid xid) {
      byte[] globalTransactionId = xid.getGlobalTransactionId();
      byte[] branchQualifier = xid.getBranchQualifier();
      StringBuilder sb = new StringBuilder((int)(16.0D + (double)globalTransactionId.length * 1.5D + (double)branchQualifier.length * 1.5D));
      sb.append(xid.getFormatId()).append('_').append(Base64.getEncoder().encodeToString(globalTransactionId)).append('_').append(Base64.getEncoder().encodeToString(branchQualifier));
      return sb.toString();
   }

   @Nullable
   static Xid stringToXid(String s) {
      int a = s.indexOf(95);
      int b = s.lastIndexOf(95);
      if (a == b) {
         return null;
      } else {
         try {
            int formatId = Integer.parseInt(s.substring(0, a));
            byte[] globalTransactionId = Base64.getMimeDecoder().decode(s.substring(a + 1, b));
            byte[] branchQualifier = Base64.getMimeDecoder().decode(s.substring(b + 1));
            return new RecoveredXid(formatId, globalTransactionId, branchQualifier);
         } catch (Exception var6) {
            LogRecord logRecord = new LogRecord(Level.FINE, "XID String is invalid: [{0}]");
            logRecord.setParameters(new Object[]{s});
            logRecord.setThrown(var6);
            Logger.getLogger(RecoveredXid.class.getName()).log(logRecord);
            return null;
         }
      }
   }
}
