package fr.xephi.authme.libs.waffle.servlet.spi;

import com.sun.jna.platform.win32.Win32Exception;
import fr.xephi.authme.libs.org.slf4j.Logger;
import fr.xephi.authme.libs.org.slf4j.LoggerFactory;
import fr.xephi.authme.libs.waffle.util.AuthorizationHeader;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsAuthProvider;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsIdentity;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityFilterProviderCollection {
   private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilterProviderCollection.class);
   private final List<SecurityFilterProvider> providers = new ArrayList();

   public SecurityFilterProviderCollection(SecurityFilterProvider[] providerArray) {
      SecurityFilterProvider[] var2 = providerArray;
      int var3 = providerArray.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         SecurityFilterProvider provider = var2[var4];
         LOGGER.info((String)"using '{}'", (Object)provider.getClass().getName());
         this.providers.add(provider);
      }

   }

   public SecurityFilterProviderCollection(String[] providerNames, IWindowsAuthProvider auth) {
      String[] var5 = providerNames;
      int var6 = providerNames.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String providerName = var5[var7];
         providerName = providerName.trim();
         LOGGER.info((String)"loading '{}'", (Object)providerName);

         try {
            Class<SecurityFilterProvider> providerClass = Class.forName(providerName);
            Constructor<SecurityFilterProvider> providerConstructor = providerClass.getConstructor(IWindowsAuthProvider.class);
            SecurityFilterProvider provider = (SecurityFilterProvider)providerConstructor.newInstance(auth);
            this.providers.add(provider);
         } catch (ClassNotFoundException var10) {
            LOGGER.error((String)"error loading '{}'", (Object)providerName);
            throw new RuntimeException(var10);
         } catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException var11) {
            LOGGER.error((String)"error loading '{}': {}", (Object)providerName, (Object)var11.getMessage());
            LOGGER.trace((String)"", (Throwable)var11);
         }
      }

   }

   public SecurityFilterProviderCollection(IWindowsAuthProvider auth) {
      this.providers.add(new NegotiateSecurityFilterProvider(auth));
      this.providers.add(new BasicSecurityFilterProvider(auth));
   }

   public boolean isSecurityPackageSupported(String securityPackage) {
      return this.get(securityPackage) != null;
   }

   private SecurityFilterProvider get(String securityPackage) {
      Iterator var2 = this.providers.iterator();

      SecurityFilterProvider provider;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         provider = (SecurityFilterProvider)var2.next();
      } while(!provider.isSecurityPackageSupported(securityPackage));

      return provider;
   }

   public IWindowsIdentity doFilter(HttpServletRequest request, HttpServletResponse response) throws IOException {
      AuthorizationHeader authorizationHeader = new AuthorizationHeader(request);
      SecurityFilterProvider provider = this.get(authorizationHeader.getSecurityPackage());
      if (provider == null) {
         throw new RuntimeException("Unsupported security package: " + authorizationHeader.getSecurityPackage());
      } else {
         try {
            return provider.doFilter(request, response);
         } catch (Win32Exception var6) {
            throw new IOException(var6);
         }
      }
   }

   public boolean isPrincipalException(HttpServletRequest request) {
      Iterator var2 = this.providers.iterator();

      SecurityFilterProvider provider;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         provider = (SecurityFilterProvider)var2.next();
      } while(!provider.isPrincipalException(request));

      return true;
   }

   public void sendUnauthorized(HttpServletResponse response) {
      Iterator var2 = this.providers.iterator();

      while(var2.hasNext()) {
         SecurityFilterProvider provider = (SecurityFilterProvider)var2.next();
         provider.sendUnauthorized(response);
      }

   }

   public int size() {
      return this.providers.size();
   }

   public SecurityFilterProvider getByClassName(String name) throws ClassNotFoundException {
      Iterator var2 = this.providers.iterator();

      SecurityFilterProvider provider;
      do {
         if (!var2.hasNext()) {
            throw new ClassNotFoundException(name);
         }

         provider = (SecurityFilterProvider)var2.next();
      } while(!provider.getClass().getName().equals(name));

      return provider;
   }
}
