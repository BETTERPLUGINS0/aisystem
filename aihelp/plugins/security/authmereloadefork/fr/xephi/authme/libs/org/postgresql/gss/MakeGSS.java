package fr.xephi.authme.libs.org.postgresql.gss;

import fr.xephi.authme.libs.org.postgresql.PGProperty;
import fr.xephi.authme.libs.org.postgresql.core.PGStream;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.PrivilegedAction;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.ietf.jgss.GSSCredential;

public class MakeGSS {
   private static final Logger LOGGER = Logger.getLogger(MakeGSS.class.getName());
   @Nullable
   private static final MethodHandle SUBJECT_CURRENT;
   @Nullable
   private static final MethodHandle ACCESS_CONTROLLER_GET_CONTEXT;
   @Nullable
   private static final MethodHandle SUBJECT_GET_SUBJECT;
   @Nullable
   private static final MethodHandle SUBJECT_DO_AS;
   @Nullable
   private static final MethodHandle SUBJECT_CALL_AS;

   @Nullable
   private static Subject getCurrentSubject() {
      try {
         if (SUBJECT_CURRENT != null) {
            return SUBJECT_CURRENT.invokeExact();
         } else {
            return SUBJECT_GET_SUBJECT != null && ACCESS_CONTROLLER_GET_CONTEXT != null ? SUBJECT_GET_SUBJECT.invoke(ACCESS_CONTROLLER_GET_CONTEXT.invoke()) : null;
         }
      } catch (Throwable var1) {
         if (var1 instanceof RuntimeException) {
            throw (RuntimeException)var1;
         } else if (var1 instanceof Error) {
            throw (Error)var1;
         } else {
            throw new RuntimeException(var1);
         }
      }
   }

   public static void authenticate(boolean encrypted, PGStream pgStream, String host, String user, char[] password, String jaasApplicationName, String kerberosServerName, boolean useSpnego, boolean jaasLogin, boolean logServerErrorDetail) throws IOException, PSQLException {
      LOGGER.log(Level.FINEST, " <=BE AuthenticationReqGSS");
      if (jaasApplicationName == null) {
         jaasApplicationName = PGProperty.JAAS_APPLICATION_NAME.getDefaultValue();
      }

      if (kerberosServerName == null) {
         kerberosServerName = "postgres";
      }

      Exception result;
      try {
         boolean performAuthentication = jaasLogin;
         Subject sub = getCurrentSubject();
         if (sub != null) {
            Set<GSSCredential> gssCreds = sub.getPrivateCredentials(GSSCredential.class);
            if (gssCreds != null && !gssCreds.isEmpty()) {
               performAuthentication = false;
            }
         }

         if (performAuthentication) {
            LoginContext lc = new LoginContext((String)Nullness.castNonNull(jaasApplicationName), new GSSCallbackHandler(user, password));
            lc.login();
            sub = lc.getSubject();
         }

         Object action;
         if (encrypted) {
            action = new GssEncAction(pgStream, sub, host, user, kerberosServerName, useSpnego, logServerErrorDetail);
         } else {
            action = new GssAction(pgStream, sub, host, user, kerberosServerName, useSpnego, logServerErrorDetail);
         }

         if (SUBJECT_DO_AS != null) {
            result = SUBJECT_DO_AS.invoke(sub, (PrivilegedAction)action);
         } else {
            if (SUBJECT_CALL_AS == null) {
               throw new PSQLException(GT.tr("Neither Subject.doAs (Java before 18) nor Subject.callAs (Java 18+) method found"), PSQLState.OBJECT_NOT_IN_STATE);
            }

            result = SUBJECT_CALL_AS.invoke(sub, (PrivilegedAction)action);
         }
      } catch (Throwable var15) {
         throw new PSQLException(GT.tr("GSS Authentication failed"), PSQLState.CONNECTION_FAILURE, var15);
      }

      if (result instanceof IOException) {
         throw (IOException)result;
      } else if (result instanceof PSQLException) {
         throw (PSQLException)result;
      } else if (result != null) {
         throw new PSQLException(GT.tr("GSS Authentication failed"), PSQLState.CONNECTION_FAILURE, result);
      }
   }

   static {
      MethodHandle subjectCurrent = null;

      try {
         subjectCurrent = MethodHandles.lookup().findStatic(Subject.class, "current", MethodType.methodType(Subject.class));
      } catch (IllegalAccessException | NoSuchMethodException var9) {
      }

      SUBJECT_CURRENT = subjectCurrent;
      MethodHandle accessControllerGetContext = null;
      MethodHandle subjectGetSubject = null;

      try {
         Class<?> accessControllerClass = Class.forName("java.security.AccessController");
         Class<?> accessControlContextClass = Class.forName("java.security.AccessControlContext");
         accessControllerGetContext = MethodHandles.lookup().findStatic(accessControllerClass, "getContext", MethodType.methodType(accessControlContextClass));
         subjectGetSubject = MethodHandles.lookup().findStatic(Subject.class, "getSubject", MethodType.methodType(Subject.class, accessControlContextClass));
      } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException var8) {
      }

      ACCESS_CONTROLLER_GET_CONTEXT = accessControllerGetContext;
      SUBJECT_GET_SUBJECT = subjectGetSubject;
      MethodHandle subjectDoAs = null;

      try {
         subjectDoAs = MethodHandles.lookup().findStatic(Subject.class, "doAs", MethodType.methodType(Object.class, Subject.class, PrivilegedAction.class));
      } catch (IllegalAccessException | NoSuchMethodException var7) {
      }

      SUBJECT_DO_AS = subjectDoAs;
      MethodHandle subjectCallAs = null;

      try {
         subjectCallAs = MethodHandles.lookup().findStatic(Subject.class, "callAs", MethodType.methodType(Object.class, Subject.class, Callable.class));
      } catch (IllegalAccessException | NoSuchMethodException var6) {
      }

      SUBJECT_CALL_AS = subjectCallAs;
   }
}
