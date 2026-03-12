package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.PolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.xacml.core.JBossPDP;
import fr.xephi.authme.libs.org.jboss.security.xacml.factories.PolicyFactory;
import fr.xephi.authme.libs.org.jboss.security.xacml.interfaces.XACMLPolicy;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBElement;

public class JBossPolicyRegistration implements PolicyRegistration, Serializable {
   private static final long serialVersionUID = 1L;
   private final Map<String, Set<XACMLPolicy>> contextIdToXACMLPolicy = new HashMap();
   private final Map<String, JBossPDP> contextIDToJBossPDP = new HashMap();

   public void deRegisterPolicy(String contextID, String type) {
      if ("XACML".equalsIgnoreCase(type)) {
         this.contextIdToXACMLPolicy.remove(contextID);
         PicketBoxLogger.LOGGER.traceDeregisterPolicy(contextID, type);
      }

   }

   public <T> T getPolicy(String contextID, String type, Map<String, Object> contextMap) {
      if ("XACML".equalsIgnoreCase(type)) {
         if (contextMap != null) {
            String pdp = (String)contextMap.get("PDP");
            if (pdp != null) {
               return this.contextIDToJBossPDP.get(contextID);
            }
         }

         return this.contextIdToXACMLPolicy.get(contextID);
      } else {
         throw PicketBoxMessages.MESSAGES.invalidPolicyRegistrationType(type);
      }
   }

   public void registerPolicy(String contextID, String type, URL location) {
      InputStream is = null;

      try {
         PicketBoxLogger.LOGGER.traceRegisterPolicy(contextID, type, location != null ? location.getPath() : null);
         is = location.openStream();
         this.registerPolicy(contextID, type, is);
      } catch (Exception var9) {
         PicketBoxLogger.LOGGER.debugIgnoredException(var9);
      } finally {
         this.safeClose(is);
      }

   }

   public void registerPolicy(String contextID, String type, InputStream stream) {
      if ("XACML".equalsIgnoreCase(type)) {
         try {
            XACMLPolicy policy = PolicyFactory.createPolicy(stream);
            Set<XACMLPolicy> policySet = (Set)this.contextIdToXACMLPolicy.get(contextID);
            if (policySet == null) {
               policySet = new HashSet();
            }

            ((Set)policySet).add(policy);
            this.contextIdToXACMLPolicy.put(contextID, policySet);
         } catch (Exception var6) {
            PicketBoxLogger.LOGGER.debugIgnoredException(var6);
         }
      }

   }

   public <P> void registerPolicyConfig(String contextId, String type, P objectModel) {
      if ("XACML".equalsIgnoreCase(type)) {
         if (!(objectModel instanceof JAXBElement)) {
            throw PicketBoxMessages.MESSAGES.invalidType(JAXBElement.class.getName());
         }

         try {
            JAXBElement<?> jaxbModel = (JAXBElement)objectModel;
            JBossPDP pdp = new JBossPDP(jaxbModel);
            this.contextIDToJBossPDP.put(contextId, pdp);
         } catch (Exception var6) {
            throw new RuntimeException(var6);
         }
      }

   }

   public void registerPolicyConfigFile(String contextId, String type, InputStream stream) {
      if ("XACML".equalsIgnoreCase(type)) {
         try {
            JBossPDP pdp = new JBossPDP(stream);
            this.contextIDToJBossPDP.put(contextId, pdp);
         } catch (Exception var5) {
            throw new RuntimeException(var5);
         }
      }

   }

   private void safeClose(InputStream fis) {
      try {
         if (fis != null) {
            fis.close();
         }
      } catch (Exception var3) {
      }

   }
}
