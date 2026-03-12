package fr.xephi.authme.libs.org.jboss.security.mapping.providers.principal;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.auth.certs.SubjectDNMapping;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingResult;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Map;

public class SubjectDNMapper extends AbstractPrincipalMappingProvider {
   private MappingResult<Principal> result;

   public void init(Map<String, Object> opt) {
   }

   public void setMappingResult(MappingResult<Principal> res) {
      this.result = res;
   }

   public void performMapping(Map<String, Object> contextMap, Principal principal) {
      if (contextMap == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextMap");
      } else {
         X509Certificate[] certs = (X509Certificate[])((X509Certificate[])contextMap.get("X509"));
         if (certs != null) {
            SubjectDNMapping sdn = new SubjectDNMapping();
            principal = sdn.toPrinicipal(certs);
            PicketBoxLogger.LOGGER.traceMappedX500Principal(principal);
         }

         this.result.setMappedObject(principal);
      }
   }
}
