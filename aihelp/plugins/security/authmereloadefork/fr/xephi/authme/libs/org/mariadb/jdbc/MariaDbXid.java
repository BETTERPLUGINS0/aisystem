package fr.xephi.authme.libs.org.mariadb.jdbc;

import java.util.Arrays;
import java.util.Objects;
import javax.transaction.xa.Xid;

public class MariaDbXid implements Xid {
   private final int formatId;
   private final byte[] globalTransactionId;
   private final byte[] branchQualifier;

   public MariaDbXid(int formatId, byte[] globalTransactionId, byte[] branchQualifier) {
      this.formatId = formatId;
      this.globalTransactionId = globalTransactionId;
      this.branchQualifier = branchQualifier;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof Xid)) {
         return false;
      } else {
         Xid other = (Xid)obj;
         return this.formatId == other.getFormatId() && Arrays.equals(this.globalTransactionId, other.getGlobalTransactionId()) && Arrays.equals(this.branchQualifier, other.getBranchQualifier());
      }
   }

   public int hashCode() {
      int result = Objects.hash(new Object[]{this.formatId});
      result = 31 * result + Arrays.hashCode(this.globalTransactionId);
      result = 31 * result + Arrays.hashCode(this.branchQualifier);
      return result;
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
}
