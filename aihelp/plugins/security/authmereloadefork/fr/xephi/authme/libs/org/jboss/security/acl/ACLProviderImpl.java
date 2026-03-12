package fr.xephi.authme.libs.org.jboss.security.acl;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationException;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.identity.Identity;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ACLProviderImpl implements ACLProvider {
   private static final String PERSISTENCE_STRATEGY_OPTION = "persistenceStrategy";
   private static final String CHECK_PARENT_ACL_OPTION = "checkParentACL";
   protected ACLPersistenceStrategy strategy;
   private boolean checkParentACL;

   public void initialize(Map<String, Object> sharedState, Map<String, Object> options) {
      String strategyClassName = (String)options.get("persistenceStrategy");
      if (strategyClassName == null) {
         strategyClassName = "fr.xephi.authme.libs.org.jboss.security.acl.JPAPersistenceStrategy";
      }

      this.checkParentACL = Boolean.valueOf((String)options.get("checkParentACL"));

      try {
         Class<?> strategyClass = this.loadClass(strategyClassName);
         this.strategy = (ACLPersistenceStrategy)strategyClass.newInstance();
      } catch (Exception var5) {
         throw PicketBoxMessages.MESSAGES.unableToCreateACLPersistenceStrategy(var5);
      }
   }

   public <T> Set<T> getEntitlements(Class<T> clazz, Resource resource, Identity identity) throws AuthorizationException {
      if (!EntitlementEntry.class.equals(clazz)) {
         return null;
      } else {
         Set<EntitlementEntry> entitlements = new HashSet();
         ACLPermission permission = this.getInitialPermissions(resource, identity.getName());
         if (permission != null) {
            this.fillEntitlements(entitlements, resource, identity.getName(), permission);
         }

         return entitlements;
      }
   }

   protected void fillEntitlements(Set<EntitlementEntry> entitlements, Resource resource, String identityName, ACLPermission permission) {
      ACLPermission currentPermission = permission;
      ACL acl = this.strategy.getACL(resource);
      if (acl != null) {
         ACLEntry entry = acl.getEntry(identityName);
         if (entry == null) {
            return;
         }

         currentPermission = entry.getPermission();
         entitlements.add(new EntitlementEntry(resource, currentPermission, identityName));
      } else {
         entitlements.add(new EntitlementEntry(resource, permission, identityName));
      }

      Collection<Resource> childResources = (Collection)resource.getMap().get("childResources");
      if (childResources != null) {
         Iterator i$ = childResources.iterator();

         while(i$.hasNext()) {
            Resource childResource = (Resource)i$.next();
            this.fillEntitlements(entitlements, childResource, identityName, currentPermission);
         }
      }

   }

   protected ACLPermission getInitialPermissions(Resource resource, String identityName) {
      ACL acl = this.strategy.getACL(resource);
      if (acl == null) {
         Resource parent = (Resource)resource.getMap().get("parentResource");
         return (ACLPermission)(parent != null ? this.getInitialPermissions(parent, identityName) : new CompositeACLPermission(BasicACLPermission.values()));
      } else {
         ACLEntry entry = acl.getEntry(identityName);
         return entry != null ? entry.getPermission() : null;
      }
   }

   public ACLPersistenceStrategy getPersistenceStrategy() {
      return this.strategy;
   }

   public void setPersistenceStrategy(ACLPersistenceStrategy persistenceStrategy) {
      if (persistenceStrategy == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("persistenceStrategy");
      } else {
         this.strategy = persistenceStrategy;
      }
   }

   public boolean isAccessGranted(Resource resource, Identity identity, ACLPermission permission) throws AuthorizationException {
      ACL acl = this.retrieveACL(resource);
      if (acl != null) {
         ACLEntry entry = acl.getEntry(identity);
         return entry != null ? entry.checkPermission(permission) : false;
      } else {
         throw new AuthorizationException(PicketBoxMessages.MESSAGES.unableToLocateACLForResourceMessage(resource != null ? resource.toString() : null));
      }
   }

   private ACL retrieveACL(Resource resource) {
      ACL acl = this.strategy.getACL(resource);
      if (acl == null && this.checkParentACL) {
         Resource parent = (Resource)resource.getMap().get("parentResource");
         if (parent != null) {
            acl = this.retrieveACL(parent);
         }
      }

      return acl;
   }

   public boolean tearDown() {
      return true;
   }

   protected Class<?> loadClass(final String name) throws PrivilegedActionException {
      return (Class)AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
         public Class<?> run() throws PrivilegedActionException {
            try {
               ClassLoader loader = Thread.currentThread().getContextClassLoader();
               return loader.loadClass(name);
            } catch (Exception var2) {
               throw new PrivilegedActionException(var2);
            }
         }
      });
   }
}
