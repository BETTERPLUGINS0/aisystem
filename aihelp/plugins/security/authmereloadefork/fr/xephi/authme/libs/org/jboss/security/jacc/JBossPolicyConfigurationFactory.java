package fr.xephi.authme.libs.org.jboss.security.jacc;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.util.state.StateMachine;
import fr.xephi.authme.libs.org.jboss.security.util.state.xml.StateMachineParser;
import java.net.URL;
import java.security.Policy;
import java.util.concurrent.ConcurrentHashMap;
import javax.security.jacc.PolicyConfiguration;
import javax.security.jacc.PolicyConfigurationFactory;
import javax.security.jacc.PolicyContextException;

public class JBossPolicyConfigurationFactory extends PolicyConfigurationFactory {
   private StateMachine configStateMachine;
   private ConcurrentHashMap<String, JBossPolicyConfiguration> policyConfigMap = new ConcurrentHashMap();
   private DelegatingPolicy policy;

   public JBossPolicyConfigurationFactory() {
      try {
         ClassLoader loader = SecurityActions.getContextClassLoader();
         URL states = SecurityActions.getResource(loader, "fr/xephi/authme/libs/org/jboss/security/jacc/jacc-policy-config-states.xml");
         StateMachineParser smp = new StateMachineParser();
         this.configStateMachine = smp.parse(states);
      } catch (Exception var4) {
         throw PicketBoxMessages.MESSAGES.failedToParseJACCStatesConfigFile(var4);
      }

      Policy p = SecurityActions.getPolicy();
      if (!(p instanceof DelegatingPolicy)) {
         p = DelegatingPolicy.getInstance();
      }

      this.policy = (DelegatingPolicy)p;
   }

   public PolicyConfiguration getPolicyConfiguration(String contextID, boolean remove) throws PolicyContextException {
      JBossPolicyConfiguration pc = (JBossPolicyConfiguration)this.policyConfigMap.get(contextID);
      if (pc == null) {
         StateMachine sm = (StateMachine)this.configStateMachine.clone();
         pc = new JBossPolicyConfiguration(contextID, this.policy, sm);
         this.policyConfigMap.put(contextID, pc);
      }

      pc.initPolicyConfiguration(remove);
      return pc;
   }

   public boolean inService(String contextID) throws PolicyContextException {
      boolean inService = false;
      JBossPolicyConfiguration pc = (JBossPolicyConfiguration)this.policyConfigMap.get(contextID);
      if (pc != null) {
         inService = pc.inService();
      }

      return inService;
   }
}
