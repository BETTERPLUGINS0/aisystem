package fr.xephi.authme.libs.org.jboss.security;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.LinkedList;

public class NestableGroup extends SimplePrincipal implements Group, Cloneable {
   private static final long serialVersionUID = 1752783303935807441L;
   private LinkedList<Principal> rolesStack = new LinkedList();

   public NestableGroup(String name) {
      super(name);
   }

   public Enumeration<Principal> members() {
      return new NestableGroup.IndexEnumeration();
   }

   public boolean removeMember(Principal user) {
      return this.rolesStack.remove(user);
   }

   public boolean addMember(Principal group) throws IllegalArgumentException {
      if (!(group instanceof Group)) {
         throw PicketBoxMessages.MESSAGES.invalidType(Group.class.getName());
      } else {
         this.rolesStack.addFirst(group);
         return true;
      }
   }

   public boolean isMember(Principal member) {
      if (this.rolesStack.size() == 0) {
         return false;
      } else {
         Group activeGroup = (Group)this.rolesStack.getFirst();
         boolean isMember = activeGroup.isMember(member);
         return isMember;
      }
   }

   public String toString() {
      StringBuffer tmp = new StringBuffer(this.getName());
      tmp.append("(members:");
      Enumeration iter = this.members();

      while(iter.hasMoreElements()) {
         tmp.append(iter.nextElement());
         tmp.append(',');
      }

      tmp.setCharAt(tmp.length() - 1, ')');
      return tmp.toString();
   }

   public synchronized Object clone() throws CloneNotSupportedException {
      NestableGroup clone = (NestableGroup)super.clone();
      if (clone != null) {
         clone.rolesStack = (LinkedList)this.rolesStack.clone();
      }

      return clone;
   }

   private class IndexEnumeration<T extends Principal> implements Enumeration<Principal> {
      private Enumeration<? extends Principal> iter;

      IndexEnumeration() {
         if (NestableGroup.this.rolesStack.size() > 0) {
            Group grp = (Group)NestableGroup.this.rolesStack.get(0);
            this.iter = grp.members();
         }

      }

      public boolean hasMoreElements() {
         boolean hasMore = this.iter != null && this.iter.hasMoreElements();
         return hasMore;
      }

      public Principal nextElement() {
         Principal next = null;
         if (this.iter != null) {
            next = (Principal)this.iter.nextElement();
         }

         return next;
      }
   }
}
