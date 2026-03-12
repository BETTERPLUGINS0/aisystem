package me.SuperRonanCraft.BetterRTP.player.commands;

import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdBiome;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdDeveloper;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdEdit;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdHelp;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdInfo;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdLocation;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdLogger;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdPlayer;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdPlayerSudo;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdQueue;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdReload;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdTest;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdVersion;
import me.SuperRonanCraft.BetterRTP.player.commands.types.CmdWorld;

public enum RTPCommandType {
   BIOME(new CmdBiome()),
   EDIT(new CmdEdit()),
   HELP(new CmdHelp()),
   INFO(new CmdInfo()),
   LOCATION(new CmdLocation()),
   PLAYER(new CmdPlayer()),
   PLAYERSUDO(new CmdPlayerSudo()),
   QUEUE(new CmdQueue(), true),
   RELOAD(new CmdReload()),
   TEST(new CmdTest(), true),
   VERSION(new CmdVersion()),
   WORLD(new CmdWorld()),
   DEV(new CmdDeveloper(), true),
   LOGGER(new CmdLogger(), true);

   private final RTPCommand cmd;
   private boolean debugOnly = false;

   private RTPCommandType(RTPCommand cmd) {
      this.cmd = cmd;
   }

   private RTPCommandType(RTPCommand cmd, boolean debugOnly) {
      this.cmd = cmd;
      this.debugOnly = debugOnly;
   }

   public boolean isDebugOnly() {
      return this.debugOnly;
   }

   public RTPCommand getCmd() {
      return this.cmd;
   }

   // $FF: synthetic method
   private static RTPCommandType[] $values() {
      return new RTPCommandType[]{BIOME, EDIT, HELP, INFO, LOCATION, PLAYER, PLAYERSUDO, QUEUE, RELOAD, TEST, VERSION, WORLD, DEV, LOGGER};
   }
}
