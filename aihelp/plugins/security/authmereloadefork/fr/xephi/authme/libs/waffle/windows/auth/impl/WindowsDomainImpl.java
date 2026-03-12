package fr.xephi.authme.libs.waffle.windows.auth.impl;

import com.sun.jna.platform.win32.Netapi32Util;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsDomain;

public class WindowsDomainImpl implements IWindowsDomain {
   private String fqn;
   private WindowsDomainImpl.TrustDirection trustDirection;
   private WindowsDomainImpl.TrustType trustType;

   public WindowsDomainImpl(String newFqn) {
      this.trustDirection = WindowsDomainImpl.TrustDirection.BIDIRECTIONAL;
      this.trustType = WindowsDomainImpl.TrustType.UNKNOWN;
      this.fqn = newFqn;
   }

   public WindowsDomainImpl(Netapi32Util.DomainTrust trust) {
      this.trustDirection = WindowsDomainImpl.TrustDirection.BIDIRECTIONAL;
      this.trustType = WindowsDomainImpl.TrustType.UNKNOWN;
      this.fqn = trust.DnsDomainName;
      if (this.fqn == null || this.fqn.length() == 0) {
         this.fqn = trust.NetbiosDomainName;
      }

      if (trust.isInbound() && trust.isOutbound()) {
         this.trustDirection = WindowsDomainImpl.TrustDirection.BIDIRECTIONAL;
      } else if (trust.isOutbound()) {
         this.trustDirection = WindowsDomainImpl.TrustDirection.OUTBOUND;
      } else if (trust.isInbound()) {
         this.trustDirection = WindowsDomainImpl.TrustDirection.INBOUND;
      }

      if (trust.isInForest()) {
         this.trustType = WindowsDomainImpl.TrustType.FOREST;
      } else if (trust.isRoot()) {
         this.trustType = WindowsDomainImpl.TrustType.TREE_ROOT;
      }

   }

   public String getFqn() {
      return this.fqn;
   }

   public String getTrustDirectionString() {
      return this.trustDirection.toString();
   }

   public String getTrustTypeString() {
      return this.trustType.toString();
   }

   private static enum TrustDirection {
      INBOUND,
      OUTBOUND,
      BIDIRECTIONAL;

      // $FF: synthetic method
      private static WindowsDomainImpl.TrustDirection[] $values() {
         return new WindowsDomainImpl.TrustDirection[]{INBOUND, OUTBOUND, BIDIRECTIONAL};
      }
   }

   private static enum TrustType {
      TREE_ROOT,
      PARENT_CHILD,
      CROSS_LINK,
      EXTERNAL,
      FOREST,
      KERBEROS,
      UNKNOWN;

      // $FF: synthetic method
      private static WindowsDomainImpl.TrustType[] $values() {
         return new WindowsDomainImpl.TrustType[]{TREE_ROOT, PARENT_CHILD, CROSS_LINK, EXTERNAL, FOREST, KERBEROS, UNKNOWN};
      }
   }
}
