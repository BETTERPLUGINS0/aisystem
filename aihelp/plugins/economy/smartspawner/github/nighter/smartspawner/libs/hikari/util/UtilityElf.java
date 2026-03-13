package github.nighter.smartspawner.libs.hikari.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UtilityElf {
   private static final Logger LOGGER = LoggerFactory.getLogger(UtilityElf.class);
   private static final Pattern PASSWORD_MASKING_PATTERN = Pattern.compile("([?&;][^&#;=]*[pP]assword=)[^&#;]*");

   private UtilityElf() {
   }

   public static String maskPasswordInJdbcUrl(String jdbcUrl) {
      return PASSWORD_MASKING_PATTERN.matcher(jdbcUrl).replaceAll("$1<masked>");
   }

   public static String getNullIfEmpty(String text) {
      return text == null ? null : (text.trim().isEmpty() ? null : text.trim());
   }

   public static void quietlySleep(long millis) {
      try {
         Thread.sleep(millis);
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
      }

   }

   public static boolean safeIsAssignableFrom(Object obj, String className) {
      try {
         Class<?> clazz = Class.forName(className);
         return clazz.isAssignableFrom(obj.getClass());
      } catch (ClassNotFoundException var3) {
         return false;
      }
   }

   public static <T> T createInstance(String className, Class<T> clazz) {
      return createInstance(className, clazz);
   }

   public static <T> T createInstance(String className, Class<T> clazz, Object... args) {
      if (className == null) {
         return null;
      } else {
         try {
            Class<?> loaded = attemptFromContextLoader(className);
            if (loaded == null) {
               loaded = UtilityElf.class.getClassLoader().loadClass(className);
               LOGGER.debug("Class {} loaded from classloader {}", className, UtilityElf.class.getClassLoader());
            }

            int totalArgs = args.length;
            if (totalArgs == 0) {
               return clazz.cast(loaded.getDeclaredConstructor().newInstance());
            } else {
               Class<?>[] argClasses = new Class[totalArgs];

               for(int i = 0; i < totalArgs; ++i) {
                  argClasses[i] = args[i].getClass();
               }

               Constructor<?> constructor = (Constructor)Arrays.stream(loaded.getConstructors()).filter((c) -> {
                  if (c.getParameterCount() != totalArgs) {
                     return false;
                  } else {
                     Class<?>[] params = c.getParameterTypes();
                     return IntStream.range(0, totalArgs).allMatch((i) -> {
                        return params[i].isAssignableFrom(argClasses[i]);
                     });
                  }
               }).findFirst().orElseThrow(() -> {
                  return new RuntimeException("No suitable constructor found for class " + className + " with arguments " + Arrays.toString(args));
               });
               return clazz.cast(constructor.newInstance(args));
            }
         } catch (Exception var7) {
            throw new RuntimeException("Failed to load class " + className, var7);
         }
      }
   }

   public static ThreadPoolExecutor createThreadPoolExecutor(int queueSize, String threadName, ThreadFactory threadFactory, RejectedExecutionHandler policy) {
      return createThreadPoolExecutor(new LinkedBlockingQueue(queueSize), threadName, threadFactory, policy);
   }

   public static ThreadPoolExecutor createThreadPoolExecutor(BlockingQueue<Runnable> queue, String threadName, ThreadFactory threadFactory, RejectedExecutionHandler policy) {
      if (threadFactory == null) {
         threadFactory = new UtilityElf.DefaultThreadFactory(threadName);
      }

      ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, queue, (ThreadFactory)threadFactory, policy);
      executor.allowCoreThreadTimeOut(true);
      return executor;
   }

   public static int getTransactionIsolation(String transactionIsolationName) {
      if (transactionIsolationName != null) {
         try {
            String upperCaseIsolationLevelName = transactionIsolationName.toUpperCase(Locale.ENGLISH);
            return IsolationLevel.valueOf(upperCaseIsolationLevelName).getLevelId();
         } catch (IllegalArgumentException var8) {
            try {
               int level = Integer.parseInt(transactionIsolationName);
               IsolationLevel[] var3 = IsolationLevel.values();
               int var4 = var3.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  IsolationLevel iso = var3[var5];
                  if (iso.getLevelId() == level) {
                     return iso.getLevelId();
                  }
               }

               throw new IllegalArgumentException("Invalid transaction isolation value: " + transactionIsolationName);
            } catch (NumberFormatException var7) {
               throw new IllegalArgumentException("Invalid transaction isolation value: " + transactionIsolationName, var7);
            }
         }
      } else {
         return -1;
      }
   }

   private static Class<?> attemptFromContextLoader(String className) {
      ClassLoader threadContextClassLoader = Thread.currentThread().getContextClassLoader();
      if (threadContextClassLoader != null) {
         try {
            Class<?> clazz = threadContextClassLoader.loadClass(className);
            LOGGER.debug("Class {} found in Thread context class loader {}", className, threadContextClassLoader);
            return clazz;
         } catch (ClassNotFoundException var3) {
            LOGGER.debug("Class {} not found in Thread context class loader {}, trying classloader {}", new Object[]{className, threadContextClassLoader, UtilityElf.class.getClassLoader()});
         }
      }

      return null;
   }

   public static final class DefaultThreadFactory implements ThreadFactory {
      private final String threadName;
      private final boolean daemon;

      public DefaultThreadFactory(String threadName) {
         this.threadName = threadName;
         this.daemon = true;
      }

      public Thread newThread(Runnable r) {
         Thread thread = new Thread(r, this.threadName);
         thread.setDaemon(this.daemon);
         return thread;
      }
   }

   public static class CustomDiscardPolicy implements RejectedExecutionHandler {
      public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
      }
   }
}
