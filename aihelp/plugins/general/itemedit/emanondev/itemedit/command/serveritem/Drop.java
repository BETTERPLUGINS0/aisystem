package emanondev.itemedit.command.serveritem;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.Util;
import emanondev.itemedit.UtilsString;
import emanondev.itemedit.command.ServerItemCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.SchedulerUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Drop extends SubCmd {
   public Drop(ServerItemCommand cmd) {
      super("drop", cmd, false, false);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      try {
         if (args.length != 7) {
            throw new IllegalArgumentException("Wrong param number");
         }

         int amount = Integer.parseInt(args[2]);
         if (amount < 1 || amount > 2304) {
            throw new IllegalArgumentException("Wrong amount number");
         }

         ItemStack item = ItemEdit.get().getServerStorage().getItem(args[1]);
         World world = Bukkit.getWorld(args[3]);
         int stackSize = item.getMaxStackSize();
         Location loc = new Location(world, Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]));
         SchedulerUtils.run(this.getPlugin(), (Location)loc, () -> {
            for(int toGive = amount; toGive > 0; toGive -= Math.min(toGive, stackSize)) {
               item.setAmount(Math.min(toGive, stackSize));
               world.dropItem(loc, item.clone());
            }

         });
         if (ItemEdit.get().getConfig().loadBoolean("log.action.drop", true)) {
            String msg = UtilsString.fix((String)this.getConfigString("log", new String[0]), (Player)null, true, "%id%", args[1].toLowerCase(), "%nick%", ItemEdit.get().getServerStorage().getNick(args[1]), "%amount%", String.valueOf(amount), "%world%", world.getName(), "%x%", args[4], "%y%", args[5], "%z%", args[6]);
            if (ItemEdit.get().getConfig().loadBoolean("log.console", true)) {
               Util.sendMessage(Bukkit.getConsoleSender(), (String)msg);
            }

            if (ItemEdit.get().getConfig().loadBoolean("log.file", true)) {
               Util.logToFile(msg);
            }
         }
      } catch (Exception var10) {
         var10.printStackTrace();
         this.onFail(sender, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      if (!(sender instanceof Player)) {
         return Collections.emptyList();
      } else {
         Location loc;
         switch(args.length) {
         case 2:
            return CompleteUtility.complete(args[1], (Collection)ItemEdit.get().getServerStorage().getIds());
         case 3:
            return CompleteUtility.complete(args[2], (Collection)Arrays.asList("1", "10", "64", "576", "2304"));
         case 4:
            List<String> l = new ArrayList();
            Iterator var4 = Bukkit.getWorlds().iterator();

            while(var4.hasNext()) {
               World w = (World)var4.next();
               l.add(w.getName());
            }

            return CompleteUtility.complete(args[3], (Collection)l);
         case 5:
            loc = ((Player)sender).getLocation();
            return CompleteUtility.complete(args[4], (Collection)Arrays.asList(String.valueOf(loc.getBlockX()), String.valueOf(loc.getX())));
         case 6:
            loc = ((Player)sender).getLocation();
            return CompleteUtility.complete(args[5], (Collection)Arrays.asList(String.valueOf(loc.getBlockY()), String.valueOf(loc.getY())));
         case 7:
            loc = ((Player)sender).getLocation();
            return CompleteUtility.complete(args[6], (Collection)Arrays.asList(String.valueOf(loc.getBlockZ()), String.valueOf(loc.getZ())));
         default:
            return Collections.emptyList();
         }
      }
   }
}
