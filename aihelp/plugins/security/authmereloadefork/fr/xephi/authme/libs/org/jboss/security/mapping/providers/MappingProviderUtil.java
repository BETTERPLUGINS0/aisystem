package fr.xephi.authme.libs.org.jboss.security.mapping.providers;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import java.lang.reflect.Constructor;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

public class MappingProviderUtil {
   public static Group addPrincipals(Group grp, Enumeration<? extends Principal> en) {
      while(en.hasMoreElements()) {
         grp.addMember((Principal)en.nextElement());
      }

      return grp;
   }

   public static Group addRoles(Group roles, String[] addRoles) {
      Class<?> pClass = getPrincipalClass(roles);
      String[] arr$ = addRoles;
      int len$ = addRoles.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String str = arr$[i$];
         roles.addMember(instantiatePrincipal(pClass, str));
      }

      return roles;
   }

   public static String[] getRolesFromCommaSeparatedString(String str) {
      if (str == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("str");
      } else {
         StringTokenizer st = new StringTokenizer(str, ",");
         int numTokens = st != null ? st.countTokens() : 0;
         String[] tokens = new String[numTokens];

         for(int i = 0; i < numTokens; ++i) {
            tokens[i] = st.nextToken();
         }

         return tokens;
      }
   }

   public static Principal instantiatePrincipal(Class<?> cls, String role) {
      Principal p = null;

      try {
         Constructor<?> ctr = cls.getConstructor(String.class);
         p = (Principal)ctr.newInstance(role);
      } catch (Exception var4) {
         PicketBoxLogger.LOGGER.debugIgnoredException(var4);
      }

      return p;
   }

   public static Group removePrincipals(Group grp) {
      HashSet<Principal> removeset = new HashSet();
      Enumeration en = grp.members();

      while(en.hasMoreElements()) {
         removeset.add(en.nextElement());
      }

      Iterator i$ = removeset.iterator();

      while(i$.hasNext()) {
         Principal p = (Principal)i$.next();
         grp.removeMember(p);
      }

      return grp;
   }

   public static Group removeRoles(Group roles, String[] removeRoles) {
      Class<?> pClass = getPrincipalClass(roles);
      String[] arr$ = removeRoles;
      int len$ = removeRoles.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String str = arr$[i$];
         roles.removeMember(instantiatePrincipal(pClass, str));
      }

      return roles;
   }

   public static Group replacePrincipals(Group fg, Group sg) {
      return addPrincipals(removePrincipals(fg), sg.members());
   }

   private static Class<?> getPrincipalClass(Group roles) {
      Class<?> principalClass = SimplePrincipal.class;
      Enumeration<? extends Principal> en = roles.members();
      if (en.hasMoreElements()) {
         principalClass = ((Principal)roles.members().nextElement()).getClass();
      }

      return principalClass;
   }
}
