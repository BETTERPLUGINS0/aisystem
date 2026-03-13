package github.nighter.smartspawner.libs.mariadb.util;

import java.security.PrivilegedExceptionAction;
import java.util.concurrent.Callable;
import javax.security.auth.Subject;

public class ThreadUtils {
   public static long getId(Thread thread) {
      return thread.getId();
   }

   public static void callAs(Subject subject, Callable<PrivilegedExceptionAction<Void>> action) throws Exception {
      Subject.doAs(subject, (PrivilegedExceptionAction)action.call());
   }
}
