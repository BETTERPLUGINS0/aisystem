package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import java.lang.reflect.Method;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import org.jboss.logging.Logger;

public class TransactionManagerLocator {
   private static Logger log = Logger.getLogger(TransactionManagerLocator.class);
   private boolean trace;
   private static TransactionManager transactionManager;

   public TransactionManagerLocator() {
      this.trace = log.isTraceEnabled();
   }

   public TransactionManager getTM(String jndiName) throws NamingException {
      TransactionManager tm = null;
      InitialContext ctx = new InitialContext();

      try {
         tm = (TransactionManager)ctx.lookup(jndiName);
      } catch (NameNotFoundException var7) {
         try {
            tm = this.getJBossTM();
         } catch (Exception var6) {
            PicketBoxLogger.LOGGER.debugIgnoredException(var6);
            if (transactionManager != null) {
               tm = transactionManager;
            }
         }
      }

      return tm;
   }

   private TransactionManager getJBossTM() throws Exception {
      ClassLoader tcl = SubjectActions.getContextClassLoader();
      Class<?> clz = tcl.loadClass("org.jboss.tm.TransactionManagerLocator");
      Method m = clz.getMethod("locate");
      return (TransactionManager)m.invoke((Object)null);
   }

   public static void setTransactionManager(TransactionManager transactionManager) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(TransactionManagerLocator.class.getName() + ".setTransactionManager"));
      }

      TransactionManagerLocator.transactionManager = transactionManager;
   }
}
