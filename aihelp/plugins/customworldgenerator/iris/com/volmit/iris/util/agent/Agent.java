package com.volmit.iris.util.agent;

import com.volmit.iris.Iris;
import java.io.File;
import java.lang.instrument.Instrumentation;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.ByteBuddyAgent.ProcessProvider.ForCurrentVm;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

public class Agent {
   private static final String NAME = "com.volmit.iris.util.agent.Installer";
   public static final File AGENT_JAR;

   public static ClassReloadingStrategy installed() {
      return ClassReloadingStrategy.of(getInstrumentation());
   }

   public static Instrumentation getInstrumentation() {
      Instrumentation var0 = doGetInstrumentation();
      if (var0 == null) {
         throw new IllegalStateException("The agent is not initialized or unavailable");
      } else {
         return var0;
      }
   }

   public static boolean install() {
      if (doGetInstrumentation() != null) {
         return true;
      } else {
         try {
            Files.copy(Iris.instance.getResource("agent.jar"), AGENT_JAR.toPath(), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
            Iris.info("Installing Java Agent...");
            ByteBuddyAgent.attach(AGENT_JAR, ForCurrentVm.INSTANCE);
         } catch (Throwable var1) {
            var1.printStackTrace();
         }

         return doGetInstrumentation() != null;
      }
   }

   private static Instrumentation doGetInstrumentation() {
      try {
         return (Instrumentation)Class.forName("com.volmit.iris.util.agent.Installer", true, ClassLoader.getSystemClassLoader()).getMethod("getInstrumentation").invoke((Object)null);
      } catch (Exception var1) {
         return null;
      }
   }

   static {
      AGENT_JAR = new File(Iris.instance.getDataFolder(), "agent.jar");
   }
}
