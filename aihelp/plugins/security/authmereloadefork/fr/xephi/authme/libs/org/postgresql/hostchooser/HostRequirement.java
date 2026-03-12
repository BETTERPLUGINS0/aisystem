package fr.xephi.authme.libs.org.postgresql.hostchooser;

import org.checkerframework.checker.nullness.qual.Nullable;

public enum HostRequirement {
   any {
      public boolean allowConnectingTo(@Nullable HostStatus status) {
         return status != HostStatus.ConnectFail;
      }
   },
   /** @deprecated */
   @Deprecated
   master {
      public boolean allowConnectingTo(@Nullable HostStatus status) {
         return primary.allowConnectingTo(status);
      }
   },
   primary {
      public boolean allowConnectingTo(@Nullable HostStatus status) {
         return status == HostStatus.Primary || status == HostStatus.ConnectOK;
      }
   },
   secondary {
      public boolean allowConnectingTo(@Nullable HostStatus status) {
         return status == HostStatus.Secondary || status == HostStatus.ConnectOK;
      }
   },
   preferSecondary {
      public boolean allowConnectingTo(@Nullable HostStatus status) {
         return status != HostStatus.ConnectFail;
      }
   },
   preferPrimary {
      public boolean allowConnectingTo(@Nullable HostStatus status) {
         return status != HostStatus.ConnectFail;
      }
   };

   private HostRequirement() {
   }

   public abstract boolean allowConnectingTo(@Nullable HostStatus var1);

   public static HostRequirement getTargetServerType(String targetServerType) {
      String allowSlave = targetServerType.replace("lave", "econdary").replace("master", "primary");
      return valueOf(allowSlave);
   }

   // $FF: synthetic method
   private static HostRequirement[] $values() {
      return new HostRequirement[]{any, master, primary, secondary, preferSecondary, preferPrimary};
   }

   // $FF: synthetic method
   HostRequirement(Object x2) {
      this();
   }
}
