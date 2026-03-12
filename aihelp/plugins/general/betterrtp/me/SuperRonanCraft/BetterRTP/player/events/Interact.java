package me.SuperRonanCraft.BetterRTP.player.events;

import java.util.Arrays;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommandType;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.file.FileOther;
import me.SuperRonanCraft.BetterRTP.references.messages.Message;
import me.SuperRonanCraft.BetterRTP.references.messages.Message_RTP;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

class Interact {
   private boolean enabled;
   private String title;
   private String coloredTitle;

   void load() {
      String pre = "Settings.";
      FileOther.FILETYPE file = BetterRTP.getInstance().getFiles().getType(FileOther.FILETYPE.SIGNS);
      this.enabled = file.getBoolean(pre + "Enabled");
      this.title = file.getString(pre + "Title");
      this.coloredTitle = Message.color(this.title);
   }

   void event(PlayerInteractEvent e) {
      if (this.enabled && e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && isSign(e.getClickedBlock())) {
         Sign sign = (Sign)e.getClickedBlock().getState();
         if (sign.getLine(0).equals(this.coloredTitle)) {
            String command = sign.getLine(1).split(" ")[0];
            if (cmd(sign.getLines()).split(" ")[0].equalsIgnoreCase("") || cmd(sign.getLines()).split(" ")[0].equalsIgnoreCase("rtp")) {
               this.action(e.getPlayer(), (String[])null);
               return;
            }

            RTPCommandType[] var4 = RTPCommandType.values();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               RTPCommandType cmd = var4[var6];
               if (command.equalsIgnoreCase(cmd.name())) {
                  this.action(e.getPlayer(), cmd(sign.getLines()).split(" "));
                  return;
               }
            }

            Message_RTP.sms(e.getPlayer(), "&cError! &7Command &a" + Arrays.toString(cmd(sign.getLines()).split(" ")) + "&7 does not exist! Defaulting command to /rtp!");
         }
      }

   }

   void createSign(SignChangeEvent e) {
      if (this.enabled && PermissionNode.SIGN_CREATE.check(e.getPlayer())) {
         String line = e.getLine(0);
         if (line != null && (line.equalsIgnoreCase(this.title) || line.equalsIgnoreCase("[RTP]"))) {
            e.setLine(0, this.coloredTitle != null ? this.coloredTitle : "[RTP]");
            MessagesCore.SIGN.send(e.getPlayer(), (Object)cmd(e.getLines()));
         }
      }

   }

   private void action(Player p, String[] line) {
      BetterRTP.getInstance().getCmd().commandExecuted(p, "rtp", line);
   }

   private static String cmd(String[] signArray) {
      String actions = "";

      for(int i = 1; i < signArray.length; ++i) {
         String line = signArray[i];
         if (line != null && !line.equals("")) {
            if (actions.equals("")) {
               actions = line;
            } else {
               actions = actions.concat(" " + line);
            }
         }
      }

      return actions;
   }

   private static boolean isSign(Block block) {
      return block.getState() instanceof Sign;
   }
}
