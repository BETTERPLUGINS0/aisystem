package fr.xephi.authme.libs.org.jboss.security.acl;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.identity.Identity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cascade;

@Entity
@Table(
   name = "ACL"
)
public class ACLImpl implements ACL, Serializable {
   private static final long serialVersionUID = -6390609071167528812L;
   @Id
   @GeneratedValue
   private long aclID;
   @Transient
   private Resource resource;
   @Column(
      name = "resource"
   )
   private String resourceAsString;
   @Transient
   private Map<String, ACLEntry> entriesMap;
   @OneToMany(
      mappedBy = "acl",
      fetch = FetchType.EAGER,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST}
   )
   @Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
   private Collection<ACLEntryImpl> entries;

   ACLImpl() {
   }

   public ACLImpl(Resource resource) {
      this((Resource)resource, new ArrayList());
   }

   public ACLImpl(Resource resource, Collection<ACLEntry> entries) {
      this(Util.getResourceAsString(resource), entries);
      this.resource = resource;
   }

   public ACLImpl(String resourceString, Collection<ACLEntry> entries) {
      this.resourceAsString = resourceString;
      this.entries = new ArrayList();
      if (entries != null) {
         Iterator i$ = entries.iterator();

         while(i$.hasNext()) {
            ACLEntry entry = (ACLEntry)i$.next();
            ACLEntryImpl entryImpl = (ACLEntryImpl)entry;
            entryImpl.setAcl(this);
            this.entries.add(entryImpl);
         }
      }

      this.initEntriesMap();
   }

   public long getACLId() {
      return this.aclID;
   }

   public boolean addEntry(ACLEntry entry) {
      if (this.entriesMap == null) {
         this.initEntriesMap();
      }

      if (entry != null && this.entriesMap.get(entry.getIdentityOrRole()) == null) {
         this.entries.add((ACLEntryImpl)entry);
         ((ACLEntryImpl)entry).setAcl(this);
         this.entriesMap.put(entry.getIdentityOrRole(), entry);
         return true;
      } else {
         return false;
      }
   }

   public boolean removeEntry(ACLEntry entry) {
      if (this.entriesMap == null) {
         this.initEntriesMap();
      }

      this.entriesMap.remove(entry.getIdentityOrRole());
      return this.entries.remove(entry);
   }

   public Collection<? extends ACLEntry> getEntries() {
      if (this.entriesMap == null) {
         this.initEntriesMap();
      }

      return Collections.unmodifiableCollection(this.entries);
   }

   public ACLEntry getEntry(Identity identity) {
      if (this.entriesMap == null) {
         this.initEntriesMap();
      }

      return (ACLEntry)this.entriesMap.get(identity.getName());
   }

   public ACLEntry getEntry(String identityOrRole) {
      if (this.entriesMap == null) {
         this.initEntriesMap();
      }

      return (ACLEntry)this.entriesMap.get(identityOrRole);
   }

   public boolean isGranted(ACLPermission permission, Identity identity) {
      if (this.entriesMap == null) {
         this.initEntriesMap();
      }

      ACLEntry entry = (ACLEntry)this.entriesMap.get(identity.getName());
      return entry != null ? entry.checkPermission(permission) : false;
   }

   public String getResourceAsString() {
      return this.resourceAsString;
   }

   public Resource getResource() {
      return this.resource;
   }

   public void setResource(Resource resource) {
      if (this.resource != null) {
         throw PicketBoxMessages.MESSAGES.aclResourceAlreadySet();
      } else {
         this.resource = resource;
      }
   }

   private void initEntriesMap() {
      this.entriesMap = new HashMap();
      Iterator i$ = this.entries.iterator();

      while(i$.hasNext()) {
         ACLEntry entry = (ACLEntry)i$.next();
         this.entriesMap.put(entry.getIdentityOrRole(), entry);
      }

   }
}
