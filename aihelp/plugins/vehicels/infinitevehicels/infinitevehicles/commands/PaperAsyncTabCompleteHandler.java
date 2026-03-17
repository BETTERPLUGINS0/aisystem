package me.PM2.infinitevehicles.commands;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

class PaperAsyncTabCompleteHandler implements Listener {
   private final PaperCommandManager manager;

   PaperAsyncTabCompleteHandler(PaperCommandManager manager) {
      this.manager = var1;
      var1.log(LogLevel.INFO, "Enabled Asynchronous Tab Completion Support!");
   }

   @EventHandler(
      ignoreCancelled = true
   )
   public void onAsyncTabComplete(AsyncTabCompleteEvent event) {
      String var2 = var1.getBuffer();
      if ((var1.isCommand() || var2.startsWith("/")) && var2.indexOf(32) != -1) {
         try {
            List var3 = this.getCompletions(var2, var1.getCompletions(), var1.getSender(), true);
            if (var3 != null) {
               if (var3.size() == 1 && ((String)var3.get(0)).equals("")) {
                  var3.set(0, " ");
               }

               var1.setCompletions(var3);
               var1.setHandled(true);
            }
         } catch (Exception var4) {
            if (!(var4 instanceof CommandCompletions.SyncCompletionRequired)) {
               throw var4;
            }
         }

      }
   }

   private List<String> getCompletions(String buffer, List<String> existingCompletions, CommandSender sender, boolean async) {
      String[] var5 = ACFPatterns.SPACE.split(var1, -1);
      String var6 = stripLeadingSlash(var5[0]);
      var5 = var5.length > 1 ? (String[])Arrays.copyOfRange(var5, 1, var5.length) : new String[]{""};
      RootCommand var7 = this.manager.getRootCommand(var6);
      if (var7 == null) {
         return null;
      } else {
         BukkitCommandIssuer var8 = this.manager.getCommandIssuer(var3);
         List var9 = var7.getTabCompletions(var8, var6, var5, false, var4);
         return ACFUtil.preformOnImmutable(var2, (var1x) -> {
            var1x.addAll(var9);
         });
      }
   }

   private static String stripLeadingSlash(String arg) {
      return var0.startsWith("/") ? var0.substring(1) : var0;
   }
}
