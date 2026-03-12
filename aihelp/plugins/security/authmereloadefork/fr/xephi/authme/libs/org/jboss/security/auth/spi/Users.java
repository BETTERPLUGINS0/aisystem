package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.SimpleGroup;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import java.security.Principal;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

public class Users {
   private HashMap<String, Users.User> users = new HashMap();

   public void addUser(Users.User user) {
      this.users.put(user.getName(), user);
   }

   public Iterator<Users.User> getUsers() {
      return this.users.values().iterator();
   }

   public Users.User getUser(String name) {
      Users.User find = (Users.User)this.users.get(name);
      return find;
   }

   public int size() {
      return this.users.size();
   }

   public String toString() {
      return "Users(" + System.identityHashCode(this) + "){" + "users=" + this.users + "}";
   }

   public static class User implements Comparable<Users.User> {
      private String name;
      private String password;
      private String encoding;
      private HashMap<String, Group> roleGroups = new HashMap();

      public User() {
      }

      public User(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getPassword() {
         return this.password;
      }

      public void setPassword(String password) {
         this.password = password;
      }

      public String getEncoding() {
         return this.encoding;
      }

      public void setEncoding(String encoding) {
         this.encoding = encoding;
      }

      public Group[] getRoleSets() {
         Group[] roleSets = new Group[this.roleGroups.size()];
         this.roleGroups.values().toArray(roleSets);
         return roleSets;
      }

      public String[] getRoleNames() {
         return this.getRoleNames("Roles");
      }

      public String[] getRoleNames(String roleGroup) {
         Group group = (Group)this.roleGroups.get(roleGroup);
         String[] names = new String[0];
         if (group != null) {
            ArrayList<String> tmp = new ArrayList();
            Enumeration iter = group.members();

            while(iter.hasMoreElements()) {
               Principal p = (Principal)iter.nextElement();
               tmp.add(p.getName());
            }

            names = new String[tmp.size()];
            tmp.toArray(names);
         }

         return names;
      }

      public void addRole(String roleName, String roleGroup) {
         Group group = (Group)this.roleGroups.get(roleGroup);
         if (group == null) {
            group = new SimpleGroup(roleGroup);
            this.roleGroups.put(roleGroup, group);
         }

         SimplePrincipal role = new SimplePrincipal(roleName);
         ((Group)group).addMember(role);
      }

      public int compareTo(Users.User obj) {
         return this.name.compareTo(obj.name);
      }

      public String toString() {
         return "User{name='" + this.name + "'" + ", password=*" + ", encoding='" + this.encoding + "'" + ", roleGroups=" + this.roleGroups + "}";
      }
   }
}
