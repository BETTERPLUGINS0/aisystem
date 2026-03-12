package fr.xephi.authme.libs.org.postgresql.hostchooser;

import fr.xephi.authme.libs.org.postgresql.util.HostSpec;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

class SingleHostChooser implements HostChooser {
   private final Collection<CandidateHost> candidateHost;

   SingleHostChooser(HostSpec hostSpec, HostRequirement targetServerType) {
      this.candidateHost = Collections.singletonList(new CandidateHost(hostSpec, targetServerType));
   }

   public Iterator<CandidateHost> iterator() {
      return this.candidateHost.iterator();
   }
}
