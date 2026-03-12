package fr.xephi.authme.libs.org.jboss.security.jacc;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.util.state.IllegalTransitionException;
import fr.xephi.authme.libs.org.jboss.security.util.state.State;
import fr.xephi.authme.libs.org.jboss.security.util.state.StateMachine;
import java.security.Permission;
import java.security.PermissionCollection;
import javax.security.jacc.PolicyConfiguration;
import javax.security.jacc.PolicyContextException;

public class JBossPolicyConfiguration implements PolicyConfiguration {
   private String contextID;
   private DelegatingPolicy policy;
   private StateMachine configStateMachine;

   protected JBossPolicyConfiguration(String contextID, DelegatingPolicy policy, StateMachine configStateMachine) throws PolicyContextException {
      this.contextID = contextID;
      this.policy = policy;
      this.configStateMachine = configStateMachine;
      if (contextID == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextID");
      } else if (policy == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("policy");
      } else if (configStateMachine == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("configStateMachine");
      } else {
         this.validateState("getPolicyConfiguration");
         PicketBoxLogger.LOGGER.debugJBossPolicyConfigurationConstruction(contextID);
      }
   }

   void initPolicyConfiguration(boolean remove) throws PolicyContextException {
      this.validateState("getPolicyConfiguration");
      this.policy.initPolicyConfiguration(this.contextID, remove);
   }

   public void addToExcludedPolicy(Permission permission) throws PolicyContextException {
      PicketBoxLogger.LOGGER.traceAddPermissionToExcludedPolicy(permission);
      this.validateState("addToExcludedPolicy");
      this.policy.addToExcludedPolicy(this.contextID, permission);
   }

   public void addToExcludedPolicy(PermissionCollection permissions) throws PolicyContextException {
      PicketBoxLogger.LOGGER.traceAddPermissionsToExcludedPolicy(permissions);
      this.validateState("addToExcludedPolicy");
      this.policy.addToExcludedPolicy(this.contextID, permissions);
   }

   public void addToRole(String roleName, Permission permission) throws PolicyContextException {
      PicketBoxLogger.LOGGER.traceAddPermissionToRole(permission);
      this.validateState("addToRole");
      this.policy.addToRole(this.contextID, roleName, permission);
   }

   public void addToRole(String roleName, PermissionCollection permissions) throws PolicyContextException {
      PicketBoxLogger.LOGGER.traceAddPermissionsToRole(permissions);
      this.validateState("addToRole");
      this.policy.addToRole(this.contextID, roleName, permissions);
   }

   public void addToUncheckedPolicy(Permission permission) throws PolicyContextException {
      PicketBoxLogger.LOGGER.traceAddPermissionToUncheckedPolicy(permission);
      this.validateState("addToUncheckedPolicy");
      this.policy.addToUncheckedPolicy(this.contextID, permission);
   }

   public void addToUncheckedPolicy(PermissionCollection permissions) throws PolicyContextException {
      PicketBoxLogger.LOGGER.traceAddPermissionsToUncheckedPolicy(permissions);
      this.validateState("addToUncheckedPolicy");
      this.policy.addToUncheckedPolicy(this.contextID, permissions);
   }

   public void commit() throws PolicyContextException {
      PicketBoxLogger.LOGGER.tracePolicyConfigurationCommit(this.contextID);
      this.validateState("commit");
      this.policy.commit(this.contextID);
   }

   public void delete() throws PolicyContextException {
      PicketBoxLogger.LOGGER.tracePolicyConfigurationDelete(this.contextID);
      this.validateState("delete");
      this.policy.delete(this.contextID);
   }

   public String getContextID() throws PolicyContextException {
      this.validateState("getContextID");
      return this.contextID;
   }

   public boolean inService() throws PolicyContextException {
      this.validateState("inService");
      State state = this.configStateMachine.getCurrentState();
      boolean inService = state.getName().equals("inService");
      return inService;
   }

   public void linkConfiguration(PolicyConfiguration link) throws PolicyContextException {
      PicketBoxLogger.LOGGER.traceLinkConfiguration(link.getContextID());
      this.validateState("linkConfiguration");
      this.policy.linkConfiguration(this.contextID, link);
   }

   public void removeExcludedPolicy() throws PolicyContextException {
      PicketBoxLogger.LOGGER.traceRemoveExcludedPolicy(this.contextID);
      this.validateState("removeExcludedPolicy");
      this.policy.removeExcludedPolicy(this.contextID);
   }

   public void removeRole(String roleName) throws PolicyContextException {
      PicketBoxLogger.LOGGER.traceRemoveRole(roleName, this.contextID);
      this.validateState("removeRole");
      this.policy.removeRole(this.contextID, roleName);
   }

   public void removeUncheckedPolicy() throws PolicyContextException {
      PicketBoxLogger.LOGGER.traceRemoveUncheckedPolicy(this.contextID);
      this.validateState("removeUncheckedPolicy");
      this.policy.removeUncheckedPolicy(this.contextID);
   }

   protected void validateState(String action) throws PolicyContextException {
      try {
         this.configStateMachine.nextState(action);
      } catch (IllegalTransitionException var3) {
         throw new PolicyContextException(PicketBoxMessages.MESSAGES.operationNotAllowedMessage(), var3);
      }
   }
}
