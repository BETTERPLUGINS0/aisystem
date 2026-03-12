package fr.xephi.authme.libs.org.jboss.security;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Util {
   private static PasswordCache externalPasswordCache;

   public static char[] loadPassword(String passwordCmd) throws Exception {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(Util.class.getName() + ".loadPassword"));
      }

      char[] password = null;
      String passwordCmdType = null;
      if (passwordCmd.charAt(0) == '{') {
         StringTokenizer tokenizer = new StringTokenizer(passwordCmd, "{}");
         passwordCmdType = tokenizer.nextToken();
         passwordCmd = tokenizer.nextToken();
      } else {
         password = passwordCmd.toCharArray();
      }

      if (password == null) {
         if (!passwordCmdType.startsWith("EXTC") && !passwordCmdType.startsWith("CMDC")) {
            if (!passwordCmdType.startsWith("EXT") && !passwordCmdType.startsWith("CMD")) {
               if (!passwordCmdType.equals("CLASS")) {
                  throw PicketBoxMessages.MESSAGES.invalidPasswordCommandType(passwordCmdType);
               }

               password = invokePasswordClass(passwordCmd);
            } else {
               password = switchCommandExecution(passwordCmdType, passwordCmd);
            }
         } else {
            long timeOut = 0L;
            if (passwordCmdType.indexOf(58) > -1) {
               try {
                  String[] token = passwordCmdType.split(":");
                  timeOut = Long.parseLong(token[1]);
               } catch (Throwable var7) {
                  PicketBoxLogger.LOGGER.errorParsingTimeoutNumber();
               }
            }

            if (externalPasswordCache == null) {
               externalPasswordCache = ExternalPasswordCache.getExternalPasswordCacheInstance();
            }

            if (externalPasswordCache.contains(passwordCmd, timeOut)) {
               password = externalPasswordCache.getPassword(passwordCmd);
            } else {
               password = switchCommandExecution(passwordCmdType, passwordCmd);
               if (password != null) {
                  externalPasswordCache.storePassword(passwordCmd, password);
               }
            }
         }
      }

      return password;
   }

   private static char[] switchCommandExecution(String passwordCmdType, String passwordCmd) throws Exception {
      if (passwordCmdType.startsWith("EXT")) {
         return execPasswordCmd(passwordCmd);
      } else if (passwordCmdType.startsWith("CMD")) {
         return execPBBasedPasswordCommand(passwordCmd);
      } else {
         throw PicketBoxMessages.MESSAGES.invalidPasswordCommandType(passwordCmdType);
      }
   }

   private static char[] execPasswordCmd(String passwordCmd) throws Exception {
      PicketBoxLogger.LOGGER.traceBeginExecPasswordCmd(passwordCmd);
      String password = execCmd(passwordCmd);
      return password.toCharArray();
   }

   private static char[] invokePasswordClass(String passwordCmd) throws Exception {
      char[] password = null;
      String classname = passwordCmd;
      String ctorArgs = null;
      int colon = passwordCmd.indexOf(58);
      if (colon > 0) {
         classname = passwordCmd.substring(0, colon);
         ctorArgs = passwordCmd.substring(colon + 1);
      }

      ClassLoader loader = (ClassLoader)AccessController.doPrivileged(Util.GetTCLAction.ACTION);
      Class<?> c = loader.loadClass(classname);
      Object instance = null;
      if (ctorArgs != null) {
         Object[] args = ctorArgs.split(",");
         Class<?>[] sig = new Class[args.length];
         ArrayList<Class<?>> sigl = new ArrayList();

         for(int n = 0; n < args.length; ++n) {
            sigl.add(String.class);
         }

         sigl.toArray(sig);
         Constructor<?> ctor = c.getConstructor(sig);
         instance = ctor.newInstance(args);
      } else {
         instance = c.newInstance();
      }

      try {
         Class<?>[] sig = new Class[0];
         Method toCharArray = c.getMethod("toCharArray", sig);
         Object[] args = new Object[0];
         password = (char[])((char[])toCharArray.invoke(instance, args));
      } catch (NoSuchMethodException var12) {
         String tmp = instance.toString();
         if (tmp != null) {
            password = tmp.toCharArray();
         }
      }

      return password;
   }

   private static String execCmd(String cmd) throws Exception {
      SecurityManager sm = System.getSecurityManager();
      String line;
      if (sm != null) {
         line = Util.RuntimeActions.PRIVILEGED.execCmd(cmd);
      } else {
         line = Util.RuntimeActions.NON_PRIVILEGED.execCmd(cmd);
      }

      return line;
   }

   private static char[] execPBBasedPasswordCommand(String passwordCmd) throws Exception {
      PicketBoxLogger.LOGGER.traceBeginExecPasswordCmd(passwordCmd);
      SecurityManager sm = System.getSecurityManager();
      String password;
      if (sm != null) {
         password = Util.RuntimeActions.PB_BASED_PRIVILEGED.execCmd(passwordCmd);
      } else {
         password = Util.RuntimeActions.PB_BASED_NON_PRIVILEGED.execCmd(passwordCmd);
      }

      return password.toCharArray();
   }

   public static boolean isPasswordCommand(String passwordCmd) {
      return passwordCmd != null && (passwordCmd.startsWith("{EXT}") || passwordCmd.startsWith("{EXTC") || passwordCmd.startsWith("{CMD}") || passwordCmd.startsWith("{CMDC") || passwordCmd.startsWith("{CLASS}"));
   }

   public static boolean isPasswordCommand(char[] passwordCmd) {
      return passwordCmd != null && isPasswordCommand(new String(passwordCmd));
   }

   interface RuntimeActions {
      Util.RuntimeActions PRIVILEGED = new Util.RuntimeActions() {
         public String execCmd(final String cmd) throws Exception {
            try {
               String line = (String)AccessController.doPrivileged(new PrivilegedExceptionAction<String>() {
                  public String run() throws Exception {
                     return Util.RuntimeActions.NON_PRIVILEGED.execCmd(cmd);
                  }
               });
               return line;
            } catch (PrivilegedActionException var3) {
               throw var3.getException();
            }
         }
      };
      Util.RuntimeActions NON_PRIVILEGED = new Util.RuntimeActions() {
         public String execCmd(String cmd) throws Exception {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(cmd);
            InputStream stdin = null;
            BufferedReader reader = null;

            String line;
            try {
               stdin = p.getInputStream();
               reader = new BufferedReader(new InputStreamReader(stdin));
               line = reader.readLine();
            } finally {
               if (reader != null) {
                  reader.close();
               }

               if (stdin != null) {
                  stdin.close();
               }

            }

            int exitCode = p.waitFor();
            PicketBoxLogger.LOGGER.traceEndExecPasswordCmd(exitCode);
            return line;
         }
      };
      Util.RuntimeActions PB_BASED_PRIVILEGED = new Util.RuntimeActions() {
         public String execCmd(final String command) throws Exception {
            try {
               String password = (String)AccessController.doPrivileged(new PrivilegedExceptionAction<String>() {
                  public String run() throws Exception {
                     return Util.RuntimeActions.PB_BASED_NON_PRIVILEGED.execCmd(command);
                  }
               });
               return password;
            } catch (PrivilegedActionException var3) {
               throw var3.getException();
            }
         }
      };
      Util.RuntimeActions PB_BASED_NON_PRIVILEGED = new Util.RuntimeActions() {
         public String execCmd(String command) throws Exception {
            String[] parsedCommand = this.parseCommand(command);
            ProcessBuilder builder = new ProcessBuilder(parsedCommand);
            Process process = builder.start();
            BufferedReader reader = null;

            String line;
            try {
               reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
               line = reader.readLine();
            } finally {
               if (reader != null) {
                  reader.close();
               }

            }

            int exitCode = process.waitFor();
            PicketBoxLogger.LOGGER.traceEndExecPasswordCmd(exitCode);
            return line;
         }

         protected String[] parseCommand(String command) {
            String[] parsedCommand = command.split("(?<!\\\\),");

            for(int k = 0; k < parsedCommand.length; ++k) {
               if (parsedCommand[k].indexOf(92) != -1) {
                  parsedCommand[k] = parsedCommand[k].replaceAll("\\\\,", ",");
               }
            }

            return parsedCommand;
         }
      };

      String execCmd(String var1) throws Exception;
   }

   private static class GetTCLAction implements PrivilegedAction<ClassLoader> {
      static PrivilegedAction<ClassLoader> ACTION = new Util.GetTCLAction();

      public ClassLoader run() {
         return Thread.currentThread().getContextClassLoader();
      }
   }
}
