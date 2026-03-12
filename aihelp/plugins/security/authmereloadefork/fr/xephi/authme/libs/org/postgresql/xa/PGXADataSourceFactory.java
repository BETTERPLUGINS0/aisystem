package fr.xephi.authme.libs.org.postgresql.xa;

import fr.xephi.authme.libs.org.postgresql.ds.common.PGObjectFactory;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PGXADataSourceFactory extends PGObjectFactory {
   @Nullable
   public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
      Reference ref = (Reference)obj;
      String className = ref.getClassName();
      return "fr.xephi.authme.libs.org.postgresql.xa.PGXADataSource".equals(className) ? this.loadXADataSource(ref) : null;
   }

   private Object loadXADataSource(Reference ref) {
      PGXADataSource ds = new PGXADataSource();
      return this.loadBaseDataSource(ds, ref);
   }
}
