package fr.xephi.authme.libs.org.jboss.security;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.LinkedList;

public class NestablePrincipal extends SimplePrincipal implements Group, Cloneable {
   private static final long serialVersionUID = 4628473920470890923L;
   private LinkedList<Principal> principalStack = new LinkedList();

   public NestablePrincipal(String name) {
      super(name);
   }

   public Enumeration<Principal> members() {
      return new NestablePrincipal.IndexEnumeration();
   }

   public boolean removeMember(Principal user) {
      return this.principalStack.remove(user);
   }

   public boolean addMember(Principal user) {
      this.principalStack.addFirst(user);
      return true;
   }

   public boolean isMember(Principal member) {
      if (this.principalStack.size() == 0) {
         return false;
      } else {
         Object activePrincipal = this.principalStack.getFirst();
         return member.equals(activePrincipal);
      }
   }

   public synchronized Object clone() throws CloneNotSupportedException {
      NestablePrincipal clone = (NestablePrincipal)super.clone();
      if (clone != null) {
         clone.principalStack = (LinkedList)this.principalStack.clone();
      }

      return clone;
   }

   private class IndexEnumeration<T extends Principal> implements Enumeration<Principal> {
      private boolean hasMoreElements;

      IndexEnumeration() {
         this.hasMoreElements = NestablePrincipal.this.principalStack.size() > 0;
      }

      public boolean hasMoreElements() {
         return this.hasMoreElements;
      }

      public Principal nextElement() {
         Principal next = (Principal)NestablePrincipal.this.principalStack.getFirst();
         this.hasMoreElements = false;
         return next;
      }
   }
}
