package fr.xephi.authme.libs.org.postgresql.hostchooser;

import fr.xephi.authme.libs.org.postgresql.util.HostSpec;

public class CandidateHost {
   public final HostSpec hostSpec;
   public final HostRequirement targetServerType;

   public CandidateHost(HostSpec hostSpec, HostRequirement targetServerType) {
      this.hostSpec = hostSpec;
      this.targetServerType = targetServerType;
   }
}
