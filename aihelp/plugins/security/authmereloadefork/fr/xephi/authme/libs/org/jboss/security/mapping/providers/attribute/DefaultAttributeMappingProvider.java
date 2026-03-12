package fr.xephi.authme.libs.org.jboss.security.mapping.providers.attribute;

import fr.xephi.authme.libs.org.jboss.security.identity.Attribute;
import fr.xephi.authme.libs.org.jboss.security.identity.AttributeFactory;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingProvider;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingResult;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultAttributeMappingProvider implements MappingProvider<List<Attribute<String>>> {
   private MappingResult<List<Attribute<String>>> result = new MappingResult();
   private Map<String, Object> options = new HashMap();

   public void init(Map<String, Object> options) {
      this.options.putAll(options);
   }

   public void performMapping(Map<String, Object> map, List<Attribute<String>> mappedObject) {
      List<Attribute<String>> attList = new ArrayList();
      Principal principal = (Principal)map.get("Principal");
      if (principal != null) {
         String principalName = principal.getName();
         String emailAddress = (String)this.options.get(principalName + ".email");
         Attribute<String> att = AttributeFactory.createEmailAddress(emailAddress);
         attList.add(att);
      }

      mappedObject.addAll(attList);
      this.result.setMappedObject(mappedObject);
   }

   public void setMappingResult(MappingResult<List<Attribute<String>>> result) {
      this.result = result;
   }

   public boolean supports(Class<?> clazz) {
      return Attribute.class.isAssignableFrom(clazz);
   }
}
