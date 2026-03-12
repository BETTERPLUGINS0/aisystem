package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.ReflectionException;
import javax.security.auth.login.Configuration;

/** @deprecated */
@Deprecated
public class DefaultLoginConfig implements DynamicMBean {
   private String authConfig = "auth.conf";
   private Configuration theConfig;

   public String getAuthConfig() {
      return this.authConfig;
   }

   public void setAuthConfig(String authConfURL) throws MalformedURLException {
      this.authConfig = authConfURL;
      ClassLoader loader = SubjectActions.getContextClassLoader();
      URL loginConfig = loader.getResource(this.authConfig);
      if (loginConfig != null) {
         System.setProperty("java.security.auth.login.config", loginConfig.toExternalForm());
      }

   }

   public Configuration getConfiguration(Configuration currentConfig) {
      if (this.theConfig == null) {
         this.theConfig = Configuration.getConfiguration();
      }

      return this.theConfig;
   }

   public Object getAttribute(String name) throws AttributeNotFoundException, MBeanException, ReflectionException {
      if (name.equals("AuthConfig")) {
         return this.getAuthConfig();
      } else {
         throw PicketBoxMessages.MESSAGES.invalidMBeanAttribute(name);
      }
   }

   public AttributeList getAttributes(String[] names) {
      AttributeList list = new AttributeList();

      for(int n = 0; n < names.length; ++n) {
         String name = names[n];

         try {
            Object value = this.getAttribute(name);
            Attribute attr = new Attribute(name, value);
            list.add(attr);
         } catch (Exception var7) {
         }
      }

      return list;
   }

   public MBeanInfo getMBeanInfo() {
      Class<?> c = this.getClass();
      MBeanAttributeInfo[] attrInfo = new MBeanAttributeInfo[]{new MBeanAttributeInfo("AuthConfig", "java.lang.String", "", true, true, false)};
      Constructor ctor = null;

      try {
         Class<?>[] sig = new Class[0];
         ctor = c.getDeclaredConstructor(sig);
      } catch (Exception var9) {
      }

      MBeanConstructorInfo[] ctorInfo = new MBeanConstructorInfo[]{new MBeanConstructorInfo("Default ctor", ctor)};
      Method getConfiguration = null;

      try {
         Class<?>[] sig = new Class[]{Configuration.class};
         getConfiguration = c.getDeclaredMethod("getConfiguration", sig);
      } catch (Exception var8) {
      }

      MBeanOperationInfo[] opInfo = new MBeanOperationInfo[]{new MBeanOperationInfo("Access the LoginConfiguration", getConfiguration)};
      MBeanInfo info = new MBeanInfo(c.getName(), "Default JAAS LoginConfig", attrInfo, ctorInfo, opInfo, (MBeanNotificationInfo[])null);
      return info;
   }

   public Object invoke(String method, Object[] args, String[] signature) throws MBeanException, ReflectionException {
      Object value = null;
      if (method.equals("getConfiguration")) {
         Configuration currentConfig = (Configuration)args[0];
         value = this.getConfiguration(currentConfig);
      }

      return value;
   }

   public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
      String name = attribute.getName();
      String value = (String)attribute.getValue();
      if (name.equals("AuthConfig")) {
         try {
            this.setAuthConfig(value);
         } catch (Exception var5) {
            throw new MBeanException(var5);
         }
      } else {
         throw PicketBoxMessages.MESSAGES.invalidMBeanAttribute(name);
      }
   }

   public AttributeList setAttributes(AttributeList attributeList) {
      AttributeList list = new AttributeList();

      for(int n = 0; n < attributeList.size(); ++n) {
         Attribute attr = (Attribute)attributeList.get(n);

         try {
            this.setAttribute(attr);
            list.add(attr);
         } catch (Exception var6) {
         }
      }

      return list;
   }
}
