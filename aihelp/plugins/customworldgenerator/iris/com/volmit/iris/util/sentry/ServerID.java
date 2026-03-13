package com.volmit.iris.util.sentry;

import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.sentry.protocol.User;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import oshi.SystemInfo;

public class ServerID {
   public static final String ID = calculate();

   public static User asUser() {
      User var0 = new User();
      var0.setId(ID);
      return var0;
   }

   private static String calculate() {
      try {
         ServerID.Digest var0 = new ServerID.Digest();
         var0.update(System.getProperty("java.vm.name"));
         var0.update(System.getProperty("java.vm.version"));
         var0.update((new SystemInfo()).getHardware().getProcessor().toString());
         var0.update(Runtime.getRuntime().maxMemory());
         Plugin[] var1 = Bukkit.getPluginManager().getPlugins();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Plugin var4 = var1[var3];
            var0.update(var4.getName());
         }

         return IO.bytesToHex(var0.digest());
      } catch (Throwable var5) {
         throw var5;
      }
   }

   private static final class Digest {
      private final MessageDigest md = MessageDigest.getInstance("SHA-256");
      private final byte[] buffer = new byte[8];
      private final ByteBuffer wrapped;

      private Digest() {
         this.wrapped = ByteBuffer.wrap(this.buffer);
      }

      public void update(String string) {
         if (var1 != null) {
            this.md.update(var1.getBytes(StandardCharsets.UTF_8));
         }
      }

      public void update(long Long) {
         this.wrapped.putLong(0, var1);
         this.md.update(this.buffer, 0, 8);
      }

      public byte[] digest() {
         return this.md.digest();
      }
   }
}
