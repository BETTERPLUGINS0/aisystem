package me.PM2.infinitevehicles.commands;

import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.help.GenericCommandHelpTopic;

public class ACFBukkitHelpTopic extends GenericCommandHelpTopic {
   public ACFBukkitHelpTopic(BukkitCommandManager manager, BukkitRootCommand command) {
      super(var2);
      final ArrayList var3 = new ArrayList();
      BukkitCommandIssuer var4 = new BukkitCommandIssuer(var1, Bukkit.getConsoleSender()) {
         public void sendMessageInternal(String message) {
            var3.add(var1);
         }
      };
      var1.generateCommandHelp(var4, var2).showHelp(var4);
      this.fullText = ACFUtil.join((Collection)var3, "\n");
   }
}
