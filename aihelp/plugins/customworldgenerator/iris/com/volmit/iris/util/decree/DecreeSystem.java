package com.volmit.iris.util.decree;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.decree.virtual.VirtualDecreeCommand;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.J;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DecreeSystem extends CommandExecutor, TabCompleter {
   KList<DecreeParameterHandler<?>> handlers = Iris.initialize("com.volmit.iris.util.decree.handlers", (Class)null).convert((i) -> {
      return (DecreeParameterHandler)i;
   });

   static KList<String> enhanceArgs(String[] args) {
      return enhanceArgs(args, true);
   }

   static KList<String> enhanceArgs(String[] args, boolean trim) {
      KList<String> a = new KList();
      if (args.length == 0) {
         return a;
      } else {
         StringBuilder flat = new StringBuilder();
         String[] var4 = args;
         int var5 = args.length;

         int x;
         for(x = 0; x < var5; ++x) {
            String i = var4[x];
            if (trim) {
               if (!i.trim().isEmpty()) {
                  flat.append(" ").append(i.trim());
               }
            } else if (i.endsWith(" ")) {
               flat.append(" ").append(i.trim()).append(" ");
            }
         }

         flat = new StringBuilder((CharSequence)(flat.length() > 0 ? (trim ? (flat.toString().trim().length() > 0 ? flat.substring(1).trim() : flat.toString().trim()) : flat.substring(1)) : flat));
         StringBuilder arg = new StringBuilder();
         boolean quoting = false;

         for(x = 0; x < flat.length(); ++x) {
            char i = flat.charAt(x);
            char j = x < flat.length() - 1 ? flat.charAt(x + 1) : i;
            boolean hasNext = x < flat.length();
            if (i == ' ' && !quoting) {
               if (!arg.toString().trim().isEmpty() && trim) {
                  a.add((Object)arg.toString().trim());
                  arg = new StringBuilder();
               }
            } else if (i == '"') {
               if (!quoting && arg.length() == 0) {
                  quoting = true;
               } else if (quoting) {
                  quoting = false;
                  if (hasNext && j == ' ') {
                     if (!arg.toString().trim().isEmpty() && trim) {
                        a.add((Object)arg.toString().trim());
                        arg = new StringBuilder();
                     }
                  } else if (!hasNext && !arg.toString().trim().isEmpty() && trim) {
                     a.add((Object)arg.toString().trim());
                     arg = new StringBuilder();
                  }
               }
            } else {
               arg.append(i);
            }
         }

         if (!arg.toString().trim().isEmpty() && trim) {
            a.add((Object)arg.toString().trim());
         }

         return a;
      }
   }

   static DecreeParameterHandler<?> getHandler(Class<?> type) {
      Iterator var1 = handlers.iterator();

      DecreeParameterHandler i;
      do {
         if (!var1.hasNext()) {
            Iris.error("Unhandled type in Decree Parameter: " + type.getName() + ". This is bad!");
            return null;
         }

         i = (DecreeParameterHandler)var1.next();
      } while(!i.supports(type));

      return i;
   }

   VirtualDecreeCommand getRoot();

   default boolean call(VolmitSender sender, String[] args) {
      DecreeContext.touch(sender);

      boolean var3;
      try {
         var3 = this.getRoot().invoke(sender, enhanceArgs(args));
      } finally {
         DecreeContext.remove();
      }

      return var3;
   }

   @Nullable
   default List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
      DecreeContext.touch(new VolmitSender(sender));

      KList var7;
      try {
         KList<String> enhanced = new KList(args);
         KList<String> v = this.getRoot().tabComplete(enhanced, enhanced.toString(" "));
         v.removeDuplicates();
         if (sender instanceof Player && IrisSettings.get().getGeneral().isCommandSounds()) {
            ((Player)sender).playSound(((Player)sender).getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 0.25F, RNG.r.f(0.125F, 1.95F));
         }

         var7 = v;
      } finally {
         DecreeContext.remove();
      }

      return var7;
   }

   default boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (!sender.hasPermission("iris.all")) {
         sender.sendMessage("You lack the Permission 'iris.all'");
         return true;
      } else {
         J.aBukkit(() -> {
            VolmitSender volmit = new VolmitSender(sender);
            if (!this.call(volmit, args)) {
               if (IrisSettings.get().getGeneral().isCommandSounds() && sender instanceof Player) {
                  ((Player)sender).playSound(((Player)sender).getLocation(), Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 0.77F, 0.25F);
                  ((Player)sender).playSound(((Player)sender).getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 0.2F, 0.45F);
               }

               volmit.sendMessage(String.valueOf(C.RED) + "Unknown Iris Command");
            } else if (IrisSettings.get().getGeneral().isCommandSounds() && sender instanceof Player) {
               ((Player)sender).playSound(((Player)sender).getLocation(), Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 0.77F, 1.65F);
               ((Player)sender).playSound(((Player)sender).getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.125F, 2.99F);
            }

         });
         return true;
      }
   }
}
