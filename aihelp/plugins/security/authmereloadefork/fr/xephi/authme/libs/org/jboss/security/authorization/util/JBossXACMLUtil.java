package fr.xephi.authme.libs.org.jboss.security.authorization.util;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.PolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.xacml.core.JBossPDP;
import fr.xephi.authme.libs.org.jboss.security.xacml.interfaces.PolicyDecisionPoint;
import fr.xephi.authme.libs.org.jboss.security.xacml.interfaces.PolicyLocator;
import fr.xephi.authme.libs.org.jboss.security.xacml.interfaces.XACMLPolicy;
import fr.xephi.authme.libs.org.jboss.security.xacml.locators.JBossPolicyLocator;
import fr.xephi.authme.libs.org.jboss.security.xacml.locators.JBossPolicySetLocator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JBossXACMLUtil {
   public PolicyDecisionPoint getPDP(PolicyRegistration policyRegistration, String contextID) {
      Map<String, Object> contextMap = new HashMap();
      contextMap.put("PDP", "PDP");
      Object pdp = null;

      try {
         pdp = (PolicyDecisionPoint)policyRegistration.getPolicy(contextID, "XACML", contextMap);
      } catch (Exception var9) {
      }

      if (pdp == null) {
         Set<XACMLPolicy> policies = (Set)policyRegistration.getPolicy(contextID, "XACML", (Map)null);
         if (policies == null) {
            throw PicketBoxMessages.MESSAGES.missingXACMLPolicyForContextId(contextID);
         }

         JBossPolicyLocator jpl = new JBossPolicyLocator(policies);
         JBossPolicySetLocator jpsl = new JBossPolicySetLocator(policies);
         HashSet<PolicyLocator> plset = new HashSet();
         plset.add(jpl);
         plset.add(jpsl);
         pdp = new JBossPDP();
         ((PolicyDecisionPoint)pdp).setPolicies(policies);
         ((PolicyDecisionPoint)pdp).setLocators(plset);
      }

      return (PolicyDecisionPoint)pdp;
   }
}
