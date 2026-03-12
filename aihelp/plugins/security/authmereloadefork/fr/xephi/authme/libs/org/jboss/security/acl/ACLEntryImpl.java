package fr.xephi.authme.libs.org.jboss.security.acl;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.identity.Identity;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(
   name = "ACL_ENTRY"
)
public class ACLEntryImpl implements ACLEntry, Serializable {
   private static final long serialVersionUID = -2985214023383451768L;
   @Id
   @GeneratedValue
   private long entryID;
   @Transient
   private BitMaskPermission permission;
   private int bitMask;
   @Transient
   private Identity identity;
   private String identityOrRole;
   @ManyToOne
   private ACLImpl acl;

   ACLEntryImpl() {
   }

   public ACLEntryImpl(BitMaskPermission permission, Identity identity) {
      this.permission = permission;
      this.identity = identity;
      this.identityOrRole = identity.getName();
   }

   public ACLEntryImpl(BitMaskPermission permission, String identityOrRole) {
      this.permission = permission;
      this.identityOrRole = identityOrRole;
   }

   public long getACLEntryId() {
      return this.entryID;
   }

   @PrePersist
   private void setPersistentFields() {
      if (this.permission != null) {
         this.bitMask = this.permission.getMaskValue();
      }

   }

   @PostLoad
   private void loadState() {
      if (this.permission != null) {
         throw PicketBoxMessages.MESSAGES.aclEntryPermissionAlreadySet();
      } else {
         this.permission = new CompositeACLPermission(this.bitMask);
      }
   }

   public ACLImpl getAcl() {
      return this.acl;
   }

   public void setAcl(ACLImpl acl) {
      this.acl = acl;
   }

   public String getIdentityOrRole() {
      return this.identityOrRole;
   }

   public Identity getIdentity() {
      return this.identity;
   }

   public ACLPermission getPermission() {
      return this.permission;
   }

   public boolean checkPermission(ACLPermission permission) {
      if (!(permission instanceof BitMaskPermission)) {
         return false;
      } else {
         BitMaskPermission bitmaskPermission = (BitMaskPermission)permission;
         if (bitmaskPermission.getMaskValue() == 0) {
            return true;
         } else {
            return (this.permission.getMaskValue() & bitmaskPermission.getMaskValue()) == bitmaskPermission.getMaskValue();
         }
      }
   }

   public boolean equals(Object obj) {
      if (obj instanceof ACLEntryImpl) {
         ACLEntryImpl entry = (ACLEntryImpl)obj;
         return this.identityOrRole.equals(entry.identityOrRole);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.identityOrRole.hashCode();
   }
}
