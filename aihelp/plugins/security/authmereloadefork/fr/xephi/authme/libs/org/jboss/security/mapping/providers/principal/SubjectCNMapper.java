package fr.xephi.authme.libs.org.jboss.security.mapping.providers.principal;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.auth.certs.SubjectCNMapping;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingResult;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.security.auth.x500.X500Principal;

public class SubjectCNMapper extends AbstractPrincipalMappingProvider {
   private MappingResult<Principal> result;

   public void init(Map<String, Object> opt) {
   }

   public void setMappingResult(MappingResult<Principal> res) {
      this.result = res;
   }

   public void performMapping(Map<String, Object> contextMap, Principal principal) {
      if (principal instanceof X500Principal) {
         if (contextMap == null) {
            throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextMap");
         } else {
            X509Certificate[] certs = (X509Certificate[])((X509Certificate[])contextMap.get("X509"));
            if (certs != null) {
               SubjectCNMapping sdn = new SubjectCNMapping();
               principal = sdn.toPrinicipal(certs);
               PicketBoxLogger.LOGGER.traceMappedX500Principal(principal);
            }

            this.result.setMappedObject(principal);
         }
      }
   }
}
