package fr.xephi.authme.libs.org.postgresql.ds.common;

import fr.xephi.authme.libs.org.postgresql.ds.PGConnectionPoolDataSource;
import fr.xephi.authme.libs.org.postgresql.ds.PGPoolingDataSource;
import fr.xephi.authme.libs.org.postgresql.ds.PGSimpleDataSource;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGObjectFactory implements ObjectFactory {
   @Nullable
   public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
      Reference ref = (Reference)obj;
      String className = ref.getClassName();
      if (!"fr.xephi.authme.libs.org.postgresql.ds.PGSimpleDataSource".equals(className) && !"fr.xephi.authme.libs.org.postgresql.jdbc2.optional.SimpleDataSource".equals(className) && !"fr.xephi.authme.libs.org.postgresql.jdbc3.Jdbc3SimpleDataSource".equals(className)) {
         if (!"fr.xephi.authme.libs.org.postgresql.ds.PGConnectionPoolDataSource".equals(className) && !"fr.xephi.authme.libs.org.postgresql.jdbc2.optional.ConnectionPool".equals(className) && !"fr.xephi.authme.libs.org.postgresql.jdbc3.Jdbc3ConnectionPool".equals(className)) {
            return !"fr.xephi.authme.libs.org.postgresql.ds.PGPoolingDataSource".equals(className) && !"fr.xephi.authme.libs.org.postgresql.jdbc2.optional.PoolingDataSource".equals(className) && !"fr.xephi.authme.libs.org.postgresql.jdbc3.Jdbc3PoolingDataSource".equals(className) ? null : this.loadPoolingDataSource(ref);
         } else {
            return this.loadConnectionPool(ref);
         }
      } else {
         return this.loadSimpleDataSource(ref);
      }
   }

   private Object loadPoolingDataSource(Reference ref) {
      String name = (String)Nullness.castNonNull(this.getProperty(ref, "dataSourceName"));
      PGPoolingDataSource pds = PGPoolingDataSource.getDataSource(name);
      if (pds != null) {
         return pds;
      } else {
         pds = new PGPoolingDataSource();
         pds.setDataSourceName(name);
         this.loadBaseDataSource(pds, ref);
         String min = this.getProperty(ref, "initialConnections");
         if (min != null) {
            pds.setInitialConnections(Integer.parseInt(min));
         }

         String max = this.getProperty(ref, "maxConnections");
         if (max != null) {
            pds.setMaxConnections(Integer.parseInt(max));
         }

         return pds;
      }
   }

   private Object loadSimpleDataSource(Reference ref) {
      PGSimpleDataSource ds = new PGSimpleDataSource();
      return this.loadBaseDataSource(ds, ref);
   }

   private Object loadConnectionPool(Reference ref) {
      PGConnectionPoolDataSource cp = new PGConnectionPoolDataSource();
      return this.loadBaseDataSource(cp, ref);
   }

   protected Object loadBaseDataSource(BaseDataSource ds, Reference ref) {
      ds.setFromReference(ref);
      return ds;
   }

   @Nullable
   protected String getProperty(Reference ref, String s) {
      RefAddr addr = ref.get(s);
      return addr == null ? null : (String)addr.getContent();
   }
}
