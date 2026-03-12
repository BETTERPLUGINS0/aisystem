package fr.xephi.authme.libs.org.jboss.security.acl;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

public class JPAPersistenceStrategy implements ACLPersistenceStrategy {
   private final Map<Resource, ACL> aclMap;
   private final EntityManagerFactory managerFactory;
   private final ACLResourceFactory resourceFactory;

   public JPAPersistenceStrategy() {
      this((ACLResourceFactory)null);
   }

   public JPAPersistenceStrategy(ACLResourceFactory resourceFactory) {
      this.aclMap = new HashMap();
      this.managerFactory = Persistence.createEntityManagerFactory("ACL");
      this.resourceFactory = resourceFactory;
   }

   public ACL createACL(Resource resource) {
      return this.createACL(resource, new ArrayList());
   }

   public ACL createACL(Resource resource, Collection<ACLEntry> entries) {
      if (resource == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("resource");
      } else {
         ACL acl = (ACL)this.aclMap.get(resource);
         if (acl == null) {
            EntityManager entityManager = this.managerFactory.createEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();

            try {
               acl = new ACLImpl(resource, entries);
               entityManager.persist(acl);
               this.aclMap.put(resource, acl);
               transaction.commit();
            } catch (RuntimeException var10) {
               var10.printStackTrace();
               transaction.rollback();
            } finally {
               entityManager.close();
            }
         }

         return (ACL)acl;
      }
   }

   public boolean removeACL(ACL acl) {
      return this.removeACL(acl.getResource());
   }

   public boolean removeACL(Resource resource) {
      boolean result = false;
      EntityManager entityManager = this.managerFactory.createEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();
      transaction.begin();

      try {
         ACL acl = this.findACLByResource(resource, entityManager);
         if (acl != null) {
            entityManager.remove(acl);
            result = this.aclMap.remove(resource) != null;
         }

         transaction.commit();
      } catch (RuntimeException var9) {
         var9.printStackTrace();
         transaction.rollback();
      } finally {
         entityManager.close();
      }

      return result;
   }

   public ACL getACL(Resource resource) {
      ACL acl = (ACL)this.aclMap.get(resource);
      if (acl == null) {
         EntityManager entityManager = this.managerFactory.createEntityManager();

         try {
            acl = this.findACLByResource(resource, entityManager);
            if (acl != null) {
               this.aclMap.put(resource, acl);
            }
         } finally {
            entityManager.close();
         }
      }

      return (ACL)acl;
   }

   public Collection<ACL> getACLs() {
      Collection<ACL> acls = null;
      EntityManager entityManager = this.managerFactory.createEntityManager();

      try {
         acls = entityManager.createQuery("SELECT a FROM ACLImpl a").getResultList();
         if (acls != null && this.resourceFactory != null) {
            Iterator i$ = acls.iterator();

            while(i$.hasNext()) {
               ACL acl = (ACL)i$.next();
               ACLImpl impl = (ACLImpl)acl;
               String[] resourceName = impl.getResourceAsString().split(":");
               impl.setResource(this.resourceFactory.instantiateResource(resourceName[0], resourceName[1]));
            }
         }
      } finally {
         entityManager.close();
      }

      return acls;
   }

   public boolean updateACL(ACL acl) {
      if (((ACLImpl)acl).getACLId() == 0L) {
         return false;
      } else {
         EntityManager entityManager = this.managerFactory.createEntityManager();
         EntityTransaction transaction = entityManager.getTransaction();
         transaction.begin();

         try {
            Iterator i$ = acl.getEntries().iterator();

            while(i$.hasNext()) {
               ACLEntry entry = (ACLEntry)i$.next();
               ACLEntryImpl entryImpl = (ACLEntryImpl)entry;
               if (entryImpl.getACLEntryId() == 0L) {
                  entityManager.persist(entryImpl);
               }
            }

            entityManager.merge(acl);
            this.aclMap.put(acl.getResource(), acl);
            transaction.commit();
            boolean var12 = true;
            return var12;
         } catch (RuntimeException var10) {
            var10.printStackTrace();
            transaction.rollback();
         } finally {
            entityManager.close();
         }

         return false;
      }
   }

   private ACLImpl findACLByResource(Resource resource, EntityManager entityManager) {
      ACLImpl acl = null;

      try {
         acl = (ACLImpl)entityManager.createQuery("SELECT a FROM ACLImpl a WHERE a.resourceAsString LIKE '" + Util.getResourceAsString(resource) + "'").getSingleResult();
         acl.setResource(resource);
      } catch (NoResultException var5) {
      }

      return acl;
   }
}
