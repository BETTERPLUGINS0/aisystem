package me.SuperRonanCraft.BetterRTP.player.commands.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommand;
import me.SuperRonanCraft.BetterRTP.player.commands.RTPCommandHelpable;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_SHAPE;
import me.SuperRonanCraft.BetterRTP.references.PermissionNode;
import me.SuperRonanCraft.BetterRTP.references.helpers.HelperRTP_EditWorlds;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesCore;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesHelp;
import me.SuperRonanCraft.BetterRTP.references.messages.MessagesUsage;
import me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds.WORLD_TYPE;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdEdit implements RTPCommand, RTPCommandHelpable {
   public String getName() {
      return "edit";
   }

   public void execute(CommandSender sendi, String label, String[] args) {
      CmdEdit.RTP_CMD_EDIT[] var4;
      int var5;
      int var6;
      CmdEdit.RTP_CMD_EDIT cmd;
      if (args.length >= 4) {
         var4 = CmdEdit.RTP_CMD_EDIT.values();
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            cmd = var4[var6];
            if (cmd.name().equalsIgnoreCase(args[1])) {
               Iterator var8;
               World world;
               CmdEdit.RTP_CMD_EDIT_SUB[] var21;
               int var23;
               int var24;
               CmdEdit.RTP_CMD_EDIT_SUB sub_cmd;
               switch(cmd) {
               case CUSTOMWORLD:
                  if (args.length >= 5) {
                     var8 = Bukkit.getWorlds().iterator();

                     do {
                        if (!var8.hasNext()) {
                           MessagesCore.NOTEXIST.send(sendi, (Object)args[2]);
                           return;
                        }

                        world = (World)var8.next();
                     } while(!world.getName().equals(args[2]));

                     var21 = CmdEdit.RTP_CMD_EDIT_SUB.values();
                     var23 = var21.length;

                     for(var24 = 0; var24 < var23; ++var24) {
                        sub_cmd = var21[var24];
                        if (sub_cmd.name().equalsIgnoreCase(args[3])) {
                           HelperRTP_EditWorlds.editCustomWorld(sendi, sub_cmd, world.getName(), args[4]);
                           return;
                        }
                     }

                     this.usage(sendi, label, cmd);
                     return;
                  }

                  this.usage(sendi, label, cmd);
                  return;
               case LOCATION:
                  if (args.length >= 5) {
                     var8 = BetterRTP.getInstance().getRTP().getRTPworldLocations().entrySet().iterator();

                     Entry location;
                     do {
                        if (!var8.hasNext()) {
                           this.usage(sendi, label, cmd);
                           return;
                        }

                        location = (Entry)var8.next();
                     } while(!((String)location.getKey()).equals(args[2]));

                     var21 = CmdEdit.RTP_CMD_EDIT_SUB.values();
                     var23 = var21.length;

                     for(var24 = 0; var24 < var23; ++var24) {
                        sub_cmd = var21[var24];
                        if (sub_cmd.name().equalsIgnoreCase(args[3])) {
                           HelperRTP_EditWorlds.editLocation(sendi, sub_cmd, (String)location.getKey(), args[4]);
                           return;
                        }
                     }

                     this.usage(sendi, label, cmd);
                     return;
                  }

                  this.usage(sendi, label, cmd);
                  return;
               case PERMISSION_GROUP:
                  if (BetterRTP.getInstance().getSettings().isPermissionGroupEnabled() && args.length >= 6) {
                     var8 = BetterRTP.getInstance().getRTP().getPermissionGroups().keySet().iterator();

                     while(var8.hasNext()) {
                        String group = (String)var8.next();
                        if (group.equals(args[2])) {
                           Iterator var20 = Bukkit.getWorlds().iterator();
                           if (var20.hasNext()) {
                              World world = (World)var20.next();
                              if (world.getName().equals(args[3])) {
                                 CmdEdit.RTP_CMD_EDIT_SUB[] var12 = CmdEdit.RTP_CMD_EDIT_SUB.values();
                                 int var13 = var12.length;

                                 for(int var14 = 0; var14 < var13; ++var14) {
                                    CmdEdit.RTP_CMD_EDIT_SUB sub_cmd = var12[var14];
                                    if (sub_cmd.name().toLowerCase().startsWith(args[4].toLowerCase())) {
                                       HelperRTP_EditWorlds.editPermissionGroup(sendi, sub_cmd, group, world.getName(), args[5]);
                                       return;
                                    }
                                 }

                                 this.usage(sendi, label, cmd);
                                 return;
                              }

                              MessagesCore.NOTEXIST.send(sendi, (Object)args[3]);
                              return;
                           }
                        }
                     }
                  }

                  this.usage(sendi, label, cmd);
                  return;
               case DEFAULT:
                  CmdEdit.RTP_CMD_EDIT_SUB[] var16 = CmdEdit.RTP_CMD_EDIT_SUB.values();
                  int var17 = var16.length;

                  for(int var10 = 0; var10 < var17; ++var10) {
                     CmdEdit.RTP_CMD_EDIT_SUB sub_cmd = var16[var10];
                     if (sub_cmd.name().equalsIgnoreCase(args[2].toLowerCase())) {
                        HelperRTP_EditWorlds.editDefault(sendi, sub_cmd, args[3]);
                        return;
                     }
                  }

                  this.usage(sendi, label, cmd);
                  return;
               case WORLD_TYPE:
                  var8 = Bukkit.getWorlds().iterator();

                  do {
                     if (!var8.hasNext()) {
                        MessagesCore.NOTEXIST.send(sendi, (Object)args[2]);
                        return;
                     }

                     world = (World)var8.next();
                  } while(!world.getName().equals(args[2]));

                  HelperRTP_EditWorlds.editWorldtype(sendi, args[2], args[3]);
                  return;
               case OVERRIDE:
                  var8 = Bukkit.getWorlds().iterator();

                  do {
                     if (!var8.hasNext()) {
                        MessagesCore.NOTEXIST.send(sendi, (Object)args[2]);
                        return;
                     }

                     world = (World)var8.next();
                  } while(!world.getName().equals(args[2]));

                  HelperRTP_EditWorlds.editOverride(sendi, args[2], args[3]);
                  return;
               case BLACKLISTEDBLOCKS:
                  if (args[2].equalsIgnoreCase("add")) {
                     HelperRTP_EditWorlds.editBlacklisted(sendi, args[3], true);
                  } else if (args[2].equalsIgnoreCase("remove")) {
                     HelperRTP_EditWorlds.editBlacklisted(sendi, args[3], false);
                  } else {
                     this.usage(sendi, label, cmd);
                  }

                  return;
               }
            }
         }
      } else if (args.length >= 2) {
         var4 = CmdEdit.RTP_CMD_EDIT.values();
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            cmd = var4[var6];
            if (cmd.name().toLowerCase().startsWith(args[1].toLowerCase())) {
               this.usage(sendi, label, cmd);
               return;
            }
         }
      }

      this.usage(sendi, label, (CmdEdit.RTP_CMD_EDIT)null);
   }

   public List<String> tabComplete(CommandSender sendi, String[] args) {
      List<String> list = new ArrayList();
      CmdEdit.RTP_CMD_EDIT[] var4;
      int var5;
      int var6;
      CmdEdit.RTP_CMD_EDIT cmd;
      if (args.length == 2) {
         var4 = CmdEdit.RTP_CMD_EDIT.values();
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            cmd = var4[var6];
            if (cmd.name().toLowerCase().startsWith(args[1].toLowerCase())) {
               list.add(cmd.name().toLowerCase());
            }
         }
      } else {
         Iterator var8;
         String block;
         World world;
         if (args.length == 3) {
            var4 = CmdEdit.RTP_CMD_EDIT.values();
            var5 = var4.length;

            label234:
            for(var6 = 0; var6 < var5; ++var6) {
               cmd = var4[var6];
               if (cmd.name().equalsIgnoreCase(args[1])) {
                  switch(cmd) {
                  case CUSTOMWORLD:
                  case WORLD_TYPE:
                  case OVERRIDE:
                     var8 = Bukkit.getWorlds().iterator();

                     while(true) {
                        if (!var8.hasNext()) {
                           continue label234;
                        }

                        world = (World)var8.next();
                        if (world.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
                           list.add(world.getName());
                        }
                     }
                  case LOCATION:
                     var8 = BetterRTP.getInstance().getRTP().getRTPworldLocations().keySet().iterator();

                     while(true) {
                        if (!var8.hasNext()) {
                           continue label234;
                        }

                        block = (String)var8.next();
                        if (block.toLowerCase().startsWith(args[2].toLowerCase())) {
                           list.add(block);
                        }
                     }
                  case PERMISSION_GROUP:
                     var8 = BetterRTP.getInstance().getRTP().getPermissionGroups().keySet().iterator();

                     while(true) {
                        if (!var8.hasNext()) {
                           continue label234;
                        }

                        block = (String)var8.next();
                        if (block.toLowerCase().startsWith(args[2].toLowerCase())) {
                           list.add(block);
                        }
                     }
                  case DEFAULT:
                     list.addAll(this.tabCompleteSub(args, cmd));
                     break;
                  case BLACKLISTEDBLOCKS:
                     list.add("add");
                     list.add("remove");
                  }
               }
            }
         } else {
            int var10;
            int var15;
            if (args.length == 4) {
               var4 = CmdEdit.RTP_CMD_EDIT.values();
               var5 = var4.length;

               label209:
               for(var6 = 0; var6 < var5; ++var6) {
                  cmd = var4[var6];
                  if (cmd.name().equalsIgnoreCase(args[1])) {
                     switch(cmd) {
                     case CUSTOMWORLD:
                     case LOCATION:
                        list.addAll(this.tabCompleteSub(args, cmd));
                        break;
                     case PERMISSION_GROUP:
                        var8 = Bukkit.getWorlds().iterator();

                        while(true) {
                           if (!var8.hasNext()) {
                              continue label209;
                           }

                           world = (World)var8.next();
                           if (world.getName().toLowerCase().startsWith(args[3].toLowerCase())) {
                              list.add(world.getName());
                           }
                        }
                     case DEFAULT:
                        if (args[2].equalsIgnoreCase(CmdEdit.RTP_CMD_EDIT_SUB.CENTER_X.name())) {
                           list.add(String.valueOf(((Player)sendi).getLocation().getBlockX()));
                        } else if (args[2].equalsIgnoreCase(CmdEdit.RTP_CMD_EDIT_SUB.CENTER_Z.name())) {
                           list.add(String.valueOf(((Player)sendi).getLocation().getBlockZ()));
                        }
                        break;
                     case WORLD_TYPE:
                        WORLD_TYPE[] var14 = WORLD_TYPE.values();
                        var15 = var14.length;
                        var10 = 0;

                        while(true) {
                           if (var10 >= var15) {
                              continue label209;
                           }

                           WORLD_TYPE _type = var14[var10];
                           list.add(_type.name());
                           ++var10;
                        }
                     case OVERRIDE:
                        var8 = Bukkit.getWorlds().iterator();

                        while(var8.hasNext()) {
                           world = (World)var8.next();
                           if (world.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
                              list.add(world.getName());
                           }
                        }

                        list.add("REMOVE_OVERRIDE");
                        break;
                     case BLACKLISTEDBLOCKS:
                        if (args[2].equalsIgnoreCase("add")) {
                           Material[] var12 = Material.values();
                           var15 = var12.length;

                           for(var10 = 0; var10 < var15; ++var10) {
                              Material block = var12[var10];
                              if (list.size() > 20) {
                                 break;
                              }

                              if (block.name().startsWith(args[3].toUpperCase())) {
                                 list.add(block.name());
                              }
                           }
                        } else if (args[2].equalsIgnoreCase("remove")) {
                           var8 = BetterRTP.getInstance().getRTP().getBlockList().iterator();

                           while(var8.hasNext()) {
                              block = (String)var8.next();
                              if (block.startsWith(args[3])) {
                                 list.add(block);
                              }
                           }
                        }
                     }
                  }
               }
            } else {
               RTP_SHAPE[] var16;
               RTP_SHAPE shape;
               if (args.length == 5) {
                  var4 = CmdEdit.RTP_CMD_EDIT.values();
                  var5 = var4.length;

                  for(var6 = 0; var6 < var5; ++var6) {
                     cmd = var4[var6];
                     if (cmd.name().equalsIgnoreCase(args[1])) {
                        switch(cmd) {
                        case CUSTOMWORLD:
                        case LOCATION:
                           if (args[3].equalsIgnoreCase(CmdEdit.RTP_CMD_EDIT_SUB.CENTER_X.name())) {
                              list.add(String.valueOf(((Player)sendi).getLocation().getBlockX()));
                           } else if (args[3].equalsIgnoreCase(CmdEdit.RTP_CMD_EDIT_SUB.CENTER_Z.name())) {
                              list.add(String.valueOf(((Player)sendi).getLocation().getBlockZ()));
                           } else {
                              if (!args[3].equalsIgnoreCase(CmdEdit.RTP_CMD_EDIT_SUB.SHAPE.name())) {
                                 continue;
                              }

                              var16 = RTP_SHAPE.values();
                              var15 = var16.length;
                              var10 = 0;

                              while(true) {
                                 if (var10 >= var15) {
                                    break;
                                 }

                                 shape = var16[var10];
                                 list.add(shape.name().toLowerCase());
                                 ++var10;
                              }
                           }
                           break;
                        case PERMISSION_GROUP:
                           list.addAll(this.tabCompleteSub(args, cmd));
                        }
                     }
                  }
               } else if (args.length == 6) {
                  var4 = CmdEdit.RTP_CMD_EDIT.values();
                  var5 = var4.length;

                  for(var6 = 0; var6 < var5; ++var6) {
                     cmd = var4[var6];
                     if (cmd.name().equalsIgnoreCase(args[1])) {
                        switch(cmd) {
                        case PERMISSION_GROUP:
                           if (args[4].equalsIgnoreCase(CmdEdit.RTP_CMD_EDIT_SUB.CENTER_X.name())) {
                              list.add(String.valueOf(((Player)sendi).getLocation().getBlockX()));
                           } else if (args[4].equalsIgnoreCase(CmdEdit.RTP_CMD_EDIT_SUB.CENTER_Z.name())) {
                              list.add(String.valueOf(((Player)sendi).getLocation().getBlockZ()));
                           } else if (args[4].equalsIgnoreCase(CmdEdit.RTP_CMD_EDIT_SUB.SHAPE.name())) {
                              var16 = RTP_SHAPE.values();
                              var15 = var16.length;

                              for(var10 = 0; var10 < var15; ++var10) {
                                 shape = var16[var10];
                                 list.add(shape.name().toLowerCase());
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return list;
   }

   private List<String> tabCompleteSub(String[] args, CmdEdit.RTP_CMD_EDIT cmd) {
      List<String> list = new ArrayList();
      CmdEdit.RTP_CMD_EDIT_SUB[] var4 = CmdEdit.RTP_CMD_EDIT_SUB.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         CmdEdit.RTP_CMD_EDIT_SUB sub_cmd = var4[var6];
         if (sub_cmd.name().toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
            list.add(sub_cmd.name().toLowerCase());
         }
      }

      return list;
   }

   @NotNull
   public PermissionNode permission() {
      return PermissionNode.EDIT;
   }

   private void usage(CommandSender sendi, String label, CmdEdit.RTP_CMD_EDIT type) {
      if (type != null) {
         switch(type) {
         case CUSTOMWORLD:
            MessagesUsage.EDIT_WORLD.send(sendi, label);
            break;
         case LOCATION:
            MessagesUsage.EDIT_LOCATION.send(sendi, label);
            break;
         case PERMISSION_GROUP:
            MessagesUsage.EDIT_PERMISSIONGROUP.send(sendi, label);
            break;
         case DEFAULT:
            MessagesUsage.EDIT_DEFAULT.send(sendi, label);
            break;
         case WORLD_TYPE:
            MessagesUsage.EDIT_WORLDTYPE.send(sendi, label);
            break;
         case OVERRIDE:
            MessagesUsage.EDIT_OVERRIDE.send(sendi, label);
            break;
         case BLACKLISTEDBLOCKS:
            MessagesUsage.EDIT_BLACKLISTEDBLLOCKS.send(sendi, label);
         }
      } else {
         MessagesUsage.EDIT_BASE.send(sendi, label);
      }

   }

   public String getHelp() {
      return MessagesHelp.EDIT.get();
   }

   public static enum RTP_CMD_EDIT {
      CUSTOMWORLD,
      PERMISSION_GROUP,
      LOCATION,
      DEFAULT,
      WORLD_TYPE,
      OVERRIDE,
      BLACKLISTEDBLOCKS;

      // $FF: synthetic method
      private static CmdEdit.RTP_CMD_EDIT[] $values() {
         return new CmdEdit.RTP_CMD_EDIT[]{CUSTOMWORLD, PERMISSION_GROUP, LOCATION, DEFAULT, WORLD_TYPE, OVERRIDE, BLACKLISTEDBLOCKS};
      }
   }

   public static enum RTP_CMD_EDIT_SUB {
      CENTER_X("CenterX", "INT"),
      CENTER_Z("CenterZ", "INT"),
      MAXRAD("MaxRadius", "INT"),
      MINRAD("MinRadius", "INT"),
      MAXY("MaxY", "INT"),
      MINY("MinY", "INT"),
      PRICE("Price", "INT"),
      SHAPE("Shape", "SHAPE"),
      USEWORLDBORDER("UseWorldBorder", "BOL");

      private final String type;
      private final String str;

      private RTP_CMD_EDIT_SUB(String str, String type) {
         this.str = str;
         this.type = type;
      }

      public String get() {
         return this.str;
      }

      public Object getResult(String input) {
         if (this.type.equalsIgnoreCase("INT")) {
            return Integer.parseInt(input);
         } else if (this.type.equalsIgnoreCase("BOL")) {
            return Boolean.valueOf(input);
         } else if (this.type.equalsIgnoreCase("STR")) {
            return Collections.singletonList(input);
         } else if (this.type.equalsIgnoreCase("SHAPE")) {
            try {
               return RTP_SHAPE.valueOf(input.toUpperCase()).name();
            } catch (IllegalArgumentException var3) {
               return null;
            }
         } else {
            return null;
         }
      }

      // $FF: synthetic method
      private static CmdEdit.RTP_CMD_EDIT_SUB[] $values() {
         return new CmdEdit.RTP_CMD_EDIT_SUB[]{CENTER_X, CENTER_Z, MAXRAD, MINRAD, MAXY, MINY, PRICE, SHAPE, USEWORLDBORDER};
      }
   }
}
