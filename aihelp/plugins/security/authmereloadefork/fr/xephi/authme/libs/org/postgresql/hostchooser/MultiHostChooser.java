package fr.xephi.authme.libs.org.postgresql.hostchooser;

import fr.xephi.authme.libs.org.postgresql.PGProperty;
import fr.xephi.authme.libs.org.postgresql.util.HostSpec;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

class MultiHostChooser implements HostChooser {
   private final HostSpec[] hostSpecs;
   private final HostRequirement targetServerType;
   private int hostRecheckTime;
   private boolean loadBalance;

   MultiHostChooser(HostSpec[] hostSpecs, HostRequirement targetServerType, Properties info) {
      this.hostSpecs = hostSpecs;
      this.targetServerType = targetServerType;

      try {
         this.hostRecheckTime = PGProperty.HOST_RECHECK_SECONDS.getInt(info) * 1000;
         this.loadBalance = PGProperty.LOAD_BALANCE_HOSTS.getBoolean(info);
      } catch (PSQLException var5) {
         throw new RuntimeException(var5);
      }
   }

   public Iterator<CandidateHost> iterator() {
      Iterator<CandidateHost> res = this.candidateIterator();
      if (!res.hasNext()) {
         List<HostSpec> allHosts = Arrays.asList(this.hostSpecs);
         if (this.loadBalance) {
            allHosts = new ArrayList((Collection)allHosts);
            Collections.shuffle((List)allHosts);
         }

         res = this.withReqStatus(this.targetServerType, (List)allHosts).iterator();
      }

      return res;
   }

   private Iterator<CandidateHost> candidateIterator() {
      if (this.targetServerType != HostRequirement.preferSecondary && this.targetServerType != HostRequirement.preferPrimary) {
         return this.getCandidateHosts(this.targetServerType).iterator();
      } else {
         HostRequirement preferredServerType = this.targetServerType == HostRequirement.preferSecondary ? HostRequirement.secondary : HostRequirement.primary;
         List<CandidateHost> preferred = this.getCandidateHosts(preferredServerType);
         List<CandidateHost> any = this.getCandidateHosts(HostRequirement.any);
         if (!preferred.isEmpty() && !any.isEmpty() && ((CandidateHost)preferred.get(preferred.size() - 1)).hostSpec.equals(((CandidateHost)any.get(0)).hostSpec)) {
            preferred = this.rtrim(1, preferred);
         }

         return this.append(preferred, any).iterator();
      }
   }

   private List<CandidateHost> getCandidateHosts(HostRequirement hostRequirement) {
      List<HostSpec> candidates = GlobalHostStatusTracker.getCandidateHosts(this.hostSpecs, hostRequirement, (long)this.hostRecheckTime);
      if (this.loadBalance) {
         Collections.shuffle(candidates);
      }

      return this.withReqStatus(hostRequirement, candidates);
   }

   private List<CandidateHost> withReqStatus(final HostRequirement requirement, final List<HostSpec> hosts) {
      return new AbstractList<CandidateHost>() {
         public CandidateHost get(int index) {
            return new CandidateHost((HostSpec)hosts.get(index), requirement);
         }

         public int size() {
            return hosts.size();
         }
      };
   }

   private <T> List<T> append(final List<T> a, final List<T> b) {
      return new AbstractList<T>() {
         public T get(int index) {
            return index < a.size() ? a.get(index) : b.get(index - a.size());
         }

         public int size() {
            return a.size() + b.size();
         }
      };
   }

   private <T> List<T> rtrim(final int size, final List<T> a) {
      return new AbstractList<T>() {
         public T get(int index) {
            return a.get(index);
         }

         public int size() {
            return Math.max(0, a.size() - size);
         }
      };
   }
}
