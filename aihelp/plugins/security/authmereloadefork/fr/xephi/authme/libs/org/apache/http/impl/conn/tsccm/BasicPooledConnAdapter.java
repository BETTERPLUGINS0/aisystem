package fr.xephi.authme.libs.org.apache.http.impl.conn.tsccm;

import fr.xephi.authme.libs.org.apache.http.conn.ClientConnectionManager;
import fr.xephi.authme.libs.org.apache.http.impl.conn.AbstractPoolEntry;
import fr.xephi.authme.libs.org.apache.http.impl.conn.AbstractPooledConnAdapter;

/** @deprecated */
@Deprecated
public class BasicPooledConnAdapter extends AbstractPooledConnAdapter {
   protected BasicPooledConnAdapter(ThreadSafeClientConnManager tsccm, AbstractPoolEntry entry) {
      super(tsccm, entry);
      this.markReusable();
   }

   protected ClientConnectionManager getManager() {
      return super.getManager();
   }

   protected AbstractPoolEntry getPoolEntry() {
      return super.getPoolEntry();
   }

   protected void detach() {
      super.detach();
   }
}
