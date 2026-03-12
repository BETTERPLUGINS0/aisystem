package fr.xephi.authme.libs.org.postgresql.osgi;

import fr.xephi.authme.libs.org.postgresql.Driver;
import java.util.Dictionary;
import java.util.Hashtable;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.jdbc.DataSourceFactory;

public class PGBundleActivator implements BundleActivator {
   @Nullable
   private ServiceRegistration<?> registration;

   public void start(BundleContext context) throws Exception {
      if (!Driver.isRegistered()) {
         Driver.register();
      }

      if (dataSourceFactoryExists()) {
         this.registerDataSourceFactory(context);
      }

   }

   private static boolean dataSourceFactoryExists() {
      try {
         Class.forName("org.osgi.service.jdbc.DataSourceFactory");
         return true;
      } catch (ClassNotFoundException var1) {
         return false;
      }
   }

   private void registerDataSourceFactory(BundleContext context) {
      Dictionary<String, Object> properties = new Hashtable();
      properties.put("osgi.jdbc.driver.class", Driver.class.getName());
      properties.put("osgi.jdbc.driver.name", "PostgreSQL JDBC Driver");
      properties.put("osgi.jdbc.driver.version", "42.7.3");
      this.registration = context.registerService(DataSourceFactory.class, new PGDataSourceFactory(), properties);
   }

   public void stop(BundleContext context) throws Exception {
      if (this.registration != null) {
         this.registration.unregister();
         this.registration = null;
      }

      if (Driver.isRegistered()) {
         Driver.deregister();
      }

   }
}
