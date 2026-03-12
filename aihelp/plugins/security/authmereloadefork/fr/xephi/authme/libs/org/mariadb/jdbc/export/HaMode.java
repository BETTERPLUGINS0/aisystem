package fr.xephi.authme.libs.org.mariadb.jdbc.export;

import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

public enum HaMode {
   REPLICATION("replication") {
      public Optional<HostAddress> getAvailableHost(List<HostAddress> hostAddresses, ConcurrentMap<HostAddress, Long> denyList, boolean primary) {
         HostAddress hostWithLessConnection = getHostWithLessConnections(hostAddresses, denyList, primary);
         return hostWithLessConnection != null ? Optional.of(hostWithLessConnection) : HaMode.getAvailableRoundRobinHost(this, hostAddresses, denyList, primary);
      }
   },
   SEQUENTIAL("sequential") {
      public Optional<HostAddress> getAvailableHost(List<HostAddress> hostAddresses, ConcurrentMap<HostAddress, Long> denyList, boolean primary) {
         return getAvailableHostInOrder(hostAddresses, denyList, primary);
      }
   },
   LOADBALANCE("load-balance") {
      public Optional<HostAddress> getAvailableHost(List<HostAddress> hostAddresses, ConcurrentMap<HostAddress, Long> denyList, boolean primary) {
         HostAddress hostWithLessConnection = getHostWithLessConnections(hostAddresses, denyList, primary);
         return hostWithLessConnection != null ? Optional.of(hostWithLessConnection) : HaMode.getAvailableRoundRobinHost(this, hostAddresses, denyList, primary);
      }
   },
   NONE("") {
      public Optional<HostAddress> getAvailableHost(List<HostAddress> hostAddresses, ConcurrentMap<HostAddress, Long> denyList, boolean primary) {
         return hostAddresses.isEmpty() ? Optional.empty() : Optional.of((HostAddress)hostAddresses.get(0));
      }
   };

   private final String value;
   private HostAddress lastRoundRobinPrimaryHost;
   private HostAddress lastRoundRobinSecondaryHost;

   private HaMode(String value) {
      this.lastRoundRobinPrimaryHost = null;
      this.lastRoundRobinSecondaryHost = null;
      this.value = value;
   }

   public static HaMode from(String value) {
      HaMode[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         HaMode haMode = var1[var3];
         if (haMode.value.equalsIgnoreCase(value) || haMode.name().equalsIgnoreCase(value)) {
            return haMode;
         }
      }

      throw new IllegalArgumentException(String.format("Wrong argument value '%s' for HaMode", value));
   }

   public static Optional<HostAddress> getAvailableHostInOrder(List<HostAddress> hostAddresses, ConcurrentMap<HostAddress, Long> denyList, boolean primary) {
      Iterator var3 = hostAddresses.iterator();

      while(var3.hasNext()) {
         HostAddress hostAddress = (HostAddress)var3.next();
         if (hostAddress.primary == primary) {
            if (!denyList.containsKey(hostAddress)) {
               return Optional.of(hostAddress);
            }

            if ((Long)denyList.get(hostAddress) < System.currentTimeMillis()) {
               denyList.remove(hostAddress);
               return Optional.of(hostAddress);
            }
         }
      }

      return Optional.empty();
   }

   public static HostAddress getHostWithLessConnections(List<HostAddress> hostAddresses, ConcurrentMap<HostAddress, Long> denyList, boolean primary) {
      long currentTime = System.currentTimeMillis();
      HostAddress hostAddressWithLessConnections = null;
      Iterator var6 = hostAddresses.iterator();

      while(true) {
         HostAddress hostAddress;
         while(true) {
            do {
               if (!var6.hasNext()) {
                  return hostAddressWithLessConnections;
               }

               hostAddress = (HostAddress)var6.next();
            } while(hostAddress.primary != primary);

            if (!denyList.containsKey(hostAddress)) {
               break;
            }

            if ((Long)denyList.get(hostAddress) <= System.currentTimeMillis()) {
               denyList.remove(hostAddress);
               break;
            }
         }

         if (hostAddress.getThreadConnectedTimeout() == null || hostAddress.getThreadConnectedTimeout() < currentTime) {
            return null;
         }

         if (hostAddressWithLessConnections == null || hostAddressWithLessConnections.getThreadsConnected() > hostAddress.getThreadsConnected()) {
            hostAddressWithLessConnections = hostAddress;
         }
      }
   }

   public static Optional<HostAddress> getAvailableRoundRobinHost(HaMode haMode, List<HostAddress> hostAddresses, ConcurrentMap<HostAddress, Long> denyList, boolean primary) {
      HostAddress lastChosenHost = primary ? haMode.lastRoundRobinPrimaryHost : haMode.lastRoundRobinSecondaryHost;
      Object loopList;
      if (lastChosenHost == null) {
         loopList = hostAddresses;
      } else {
         int lastChosenIndex = hostAddresses.indexOf(lastChosenHost);
         loopList = new ArrayList();
         ((List)loopList).addAll(hostAddresses.subList(lastChosenIndex + 1, hostAddresses.size()));
         ((List)loopList).addAll(hostAddresses.subList(0, lastChosenIndex + 1));
      }

      Iterator var8 = ((List)loopList).iterator();

      HostAddress hostAddress;
      while(true) {
         do {
            if (!var8.hasNext()) {
               return Optional.empty();
            }

            hostAddress = (HostAddress)var8.next();
         } while(hostAddress.primary != primary);

         if (!denyList.containsKey(hostAddress)) {
            break;
         }

         if ((Long)denyList.get(hostAddress) <= System.currentTimeMillis()) {
            denyList.remove(hostAddress);
            break;
         }
      }

      if (primary) {
         haMode.lastRoundRobinPrimaryHost = hostAddress;
      } else {
         haMode.lastRoundRobinSecondaryHost = hostAddress;
      }

      return Optional.of(hostAddress);
   }

   public void resetLast() {
      this.lastRoundRobinPrimaryHost = null;
      this.lastRoundRobinSecondaryHost = null;
   }

   public abstract Optional<HostAddress> getAvailableHost(List<HostAddress> var1, ConcurrentMap<HostAddress, Long> var2, boolean var3);

   // $FF: synthetic method
   HaMode(String x2, Object x3) {
      this(x2);
   }
}
