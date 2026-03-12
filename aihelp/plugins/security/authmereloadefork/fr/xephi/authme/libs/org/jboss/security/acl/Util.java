package fr.xephi.authme.libs.org.jboss.security.acl;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.Resource;
import fr.xephi.authme.libs.org.jboss.security.identity.Identity;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.IdentityFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import javax.persistence.Id;

public class Util {
   public static String getResourceAsString(Resource resource) {
      String resourceString = null;
      if (resource != null) {
         String resourceClass = resource.getClass().getCanonicalName();
         resourceString = resourceClass + ":" + getResourceKey(resource);
      }

      return resourceString;
   }

   public static String getIdentityAsString(Identity identity) {
      String identityString = null;
      if (identity != null) {
         identityString = identity.getClass().getCanonicalName() + ":" + identity.getName();
      }

      return identityString;
   }

   public static Identity getIdentityFromString(String identityString) {
      Identity identity = null;
      if (identityString != null) {
         String[] identityParts = identityString.split(":");
         if (identityParts.length != 2) {
            throw PicketBoxMessages.MESSAGES.malformedIdentityString(identityString);
         }

         try {
            identity = IdentityFactory.createIdentity(identityParts[0], identityParts[1]);
         } catch (Exception var4) {
            throw new RuntimeException(var4);
         }
      }

      return identity;
   }

   private static Object getResourceKey(Resource resource) {
      Class<? extends Resource> resourceClass = resource.getClass();
      Object resourceKey = null;
      Field[] arr$ = resourceClass.getDeclaredFields();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Field field = arr$[i$];
         if (field.getAnnotation(Id.class) != null) {
            try {
               resourceKey = field.get(resource);
            } catch (Exception var10) {
               String fieldName = field.getName();
               String methodName = "get" + fieldName.substring(0, 1).toUpperCase(Locale.ENGLISH) + fieldName.substring(1);
               resourceKey = executeNoArgMethod(resource, methodName);
            }
            break;
         }
      }

      if (resourceKey == null) {
         resourceKey = executeNoArgMethod(resource, "getId");
      }

      return resourceKey;
   }

   private static Object executeNoArgMethod(Object target, String methodName) {
      Class targetClass = target.getClass();

      try {
         Method method = targetClass.getMethod(methodName, (Class[])null);
         return method.invoke(target, (Object[])null);
      } catch (Exception var4) {
         return null;
      }
   }
}
