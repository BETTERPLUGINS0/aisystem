package org.terraform.command.contants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Stack;
import java.util.UUID;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;

public abstract class TerraCommand {
   @NotNull
   public final ArrayList<String> aliases = new ArrayList();
   @NotNull
   public final ArrayList<TerraCommandArgument<?>> parameters = new ArrayList();
   public final TerraformGeneratorPlugin plugin;

   public TerraCommand(TerraformGeneratorPlugin plugin, @NotNull String... aliases) {
      this.plugin = plugin;
      this.aliases.addAll(Arrays.asList(aliases));
   }

   public abstract String getDefaultDescription();

   public boolean isInAcceptedParamRange(@NotNull Stack<String> args) {
      if (args.size() > this.parameters.size()) {
         return false;
      } else if (this.parameters.isEmpty()) {
         return true;
      } else {
         int lowerBound = 0;
         Iterator var3 = this.parameters.iterator();

         while(var3.hasNext()) {
            TerraCommandArgument<?> arg = (TerraCommandArgument)var3.next();
            if (!arg.isOptional()) {
               ++lowerBound;
            }
         }

         return args.size() >= lowerBound;
      }
   }

   @NotNull
   public String getLangPath() {
      return "command." + (String)this.aliases.get(0) + ".desc";
   }

   public abstract boolean canConsoleExec();

   public abstract boolean hasPermission(CommandSender var1);

   public abstract void execute(CommandSender var1, Stack<String> var2) throws InvalidArgumentException;

   @NotNull
   public ArrayList<Object> parseArguments(CommandSender sender, @NotNull Stack<String> args) throws InvalidArgumentException {
      ArrayList<Object> items = new ArrayList(args.size());

      for(int i = 0; !args.isEmpty(); ++i) {
         String arg = (String)args.pop();
         TerraCommandArgument<?> parser = (TerraCommandArgument)this.parameters.get(i);
         Object parsed = parser.parse(sender, arg);
         String val = parser.validate(sender, arg);
         if (parsed == null) {
            throw new InvalidArgumentException(val);
         }

         if (!val.isEmpty()) {
            throw new InvalidArgumentException(val);
         }

         items.add(i, parsed);
      }

      return items;
   }

   @Nullable
   public String getNextArg(@NotNull Stack<String> args) {
      return args.empty() ? null : (String)args.pop();
   }

   public boolean matchCommand(String command) {
      command = command.toLowerCase(Locale.ENGLISH);
      return this.aliases.contains(command);
   }

   protected void syncSendMessage(@NotNull UUID uuid, @NotNull String prefix, @NotNull String message) {
      Iterator var4 = Bukkit.getOnlinePlayers().iterator();

      while(var4.hasNext()) {
         Player p = (Player)var4.next();
         if (p.getUniqueId().equals(uuid)) {
            p.sendMessage(message);
            break;
         }
      }

      TerraformGeneratorPlugin.logger.info("[" + prefix + "] " + message);
   }

   protected void syncSendMessageTP(@NotNull UUID uuid, @NotNull String prefix, @NotNull String message, int x, int y, int z) {
      TextComponent chatMsg = new TextComponent(message);
      chatMsg.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tp " + x + " " + y + " " + z));
      Iterator var8 = Bukkit.getOnlinePlayers().iterator();

      while(var8.hasNext()) {
         Player p = (Player)var8.next();
         if (p.getUniqueId().equals(uuid)) {
            p.spigot().sendMessage(chatMsg);
            break;
         }
      }

      TerraformGeneratorPlugin.logger.info("[" + prefix + "] " + message);
   }

   protected int getHighestY(@NotNull TerraformWorld tw, int x, int z) {
      World world = tw.getWorld();

      for(int y = tw.maxY; y > tw.minY; --y) {
         if (!world.getBlockAt(x, y, z).getType().isAir()) {
            return y + 1;
         }
      }

      return tw.minY;
   }
}
