package fr.xephi.authme.libs.org.jboss.security.auth.login;

import fr.xephi.authme.libs.org.jboss.security.util.xml.DOMUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ConfigUtil {
   public static AuthenticationInfo parseAuthentication(Element policy) throws Exception {
      NodeList authentication = policy.getElementsByTagName("authentication");
      if (authentication.getLength() == 0) {
         return null;
      } else {
         Element auth = (Element)authentication.item(0);
         NodeList modules = auth.getElementsByTagName("login-module");
         ArrayList tmp = new ArrayList();

         for(int n = 0; n < modules.getLength(); ++n) {
            Element module = (Element)modules.item(n);
            parseModule(module, tmp);
         }

         AppConfigurationEntry[] entries = new AppConfigurationEntry[tmp.size()];
         tmp.toArray(entries);
         AuthenticationInfo info = new AuthenticationInfo();
         info.setAppConfigurationEntry(entries);
         return info;
      }
   }

   static void parseModule(Element module, ArrayList entries) throws Exception {
      LoginModuleControlFlag controlFlag = LoginModuleControlFlag.REQUIRED;
      String className = DOMUtils.getAttributeValue(module, "code");
      String flag = DOMUtils.getAttributeValue(module, "flag");
      if (flag != null) {
         flag = flag.toLowerCase(Locale.ENGLISH);
         if (LoginModuleControlFlag.REQUIRED.toString().indexOf(flag) > 0) {
            controlFlag = LoginModuleControlFlag.REQUIRED;
         } else if (LoginModuleControlFlag.REQUISITE.toString().indexOf(flag) > 0) {
            controlFlag = LoginModuleControlFlag.REQUISITE;
         } else if (LoginModuleControlFlag.SUFFICIENT.toString().indexOf(flag) > 0) {
            controlFlag = LoginModuleControlFlag.SUFFICIENT;
         } else if (LoginModuleControlFlag.OPTIONAL.toString().indexOf(flag) > 0) {
            controlFlag = LoginModuleControlFlag.OPTIONAL;
         }
      }

      NodeList opts = module.getElementsByTagName("module-option");
      HashMap options = new HashMap();

      for(int n = 0; n < opts.getLength(); ++n) {
         Element opt = (Element)opts.item(n);
         String name = opt.getAttribute("name");
         String value = DOMUtils.getTextContent(opt);
         if (value == null) {
            value = "";
         }

         options.put(name, value);
      }

      AppConfigurationEntry entry = new AppConfigurationEntry(className, controlFlag, options);
      entries.add(entry);
   }
}
