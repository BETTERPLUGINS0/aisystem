package fr.xephi.authme.libs.org.postgresql.hostchooser;

import fr.xephi.authme.libs.org.postgresql.jdbc.ResourceLock;
import fr.xephi.authme.libs.org.postgresql.util.HostSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

public class GlobalHostStatusTracker {
   private static final Map<HostSpec, GlobalHostStatusTracker.HostSpecStatus> hostStatusMap = new HashMap();
   private static final ResourceLock lock = new ResourceLock();

   public static void reportHostStatus(HostSpec hostSpec, HostStatus hostStatus) {
      long now = System.nanoTime() / 1000000L;
      ResourceLock ignore = lock.obtain();

      try {
         GlobalHostStatusTracker.HostSpecStatus hostSpecStatus = (GlobalHostStatusTracker.HostSpecStatus)hostStatusMap.get(hostSpec);
         if (hostSpecStatus == null) {
            hostSpecStatus = new GlobalHostStatusTracker.HostSpecStatus(hostSpec);
            hostStatusMap.put(hostSpec, hostSpecStatus);
         }

         hostSpecStatus.status = hostStatus;
         hostSpecStatus.lastUpdated = now;
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   static List<HostSpec> getCandidateHosts(HostSpec[] hostSpecs, HostRequirement targetServerType, long hostRecheckMillis) {
      List<HostSpec> candidates = new ArrayList(hostSpecs.length);
      long latestAllowedUpdate = System.nanoTime() / 1000000L - hostRecheckMillis;
      ResourceLock ignore = lock.obtain();

      try {
         HostSpec[] var8 = hostSpecs;
         int var9 = hostSpecs.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            HostSpec hostSpec = var8[var10];
            GlobalHostStatusTracker.HostSpecStatus hostInfo = (GlobalHostStatusTracker.HostSpecStatus)hostStatusMap.get(hostSpec);
            if (hostInfo == null || hostInfo.lastUpdated < latestAllowedUpdate || targetServerType.allowConnectingTo(hostInfo.status)) {
               candidates.add(hostSpec);
            }
         }
      } catch (Throwable var14) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var13) {
               var14.addSuppressed(var13);
            }
         }

         throw var14;
      }

      if (ignore != null) {
         ignore.close();
      }

      return candidates;
   }

   static class HostSpecStatus {
      final HostSpec host;
      @Nullable
      HostStatus status;
      long lastUpdated;

      HostSpecStatus(HostSpec host) {
         this.host = host;
      }

      public String toString() {
         return this.host.toString() + '=' + this.status;
      }
   }
}
