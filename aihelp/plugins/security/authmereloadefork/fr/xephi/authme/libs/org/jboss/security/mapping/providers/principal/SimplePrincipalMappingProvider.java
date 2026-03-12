package fr.xephi.authme.libs.org.jboss.security.mapping.providers.principal;

import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingResult;
import java.security.Principal;
import java.util.Map;
import java.util.Properties;

public class SimplePrincipalMappingProvider extends AbstractPrincipalMappingProvider {
   private static final String PRINCIPALS_MAP = "principalsMap";
   private MappingResult<Principal> result;
   Properties principalMapProperties = null;

   public void init(Map<String, Object> options) {
      if (options != null && options.containsKey("principalsMap")) {
         this.principalMapProperties = (Properties)options.get("principalsMap");
      }

   }

   public void performMapping(Map<String, Object> map, Principal mappedObject) {
      if (mappedObject instanceof SimplePrincipal) {
         SimplePrincipal simplePrincipal = (SimplePrincipal)mappedObject;
         if (this.principalMapProperties != null) {
            String newPrincipalName = this.principalMapProperties.getProperty(simplePrincipal.getName());
            if (newPrincipalName != null && newPrincipalName.length() > 0) {
               this.result.setMappedObject(new SimplePrincipal(newPrincipalName));
            }
         }

      }
   }

   public void setMappingResult(MappingResult<Principal> result) {
      this.result = result;
   }
}
