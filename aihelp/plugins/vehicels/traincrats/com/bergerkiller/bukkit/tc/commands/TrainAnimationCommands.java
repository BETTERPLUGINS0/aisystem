package com.bergerkiller.bukkit.tc.commands;

import com.bergerkiller.bukkit.common.Hastebin.DownloadResult;
import com.bergerkiller.bukkit.common.Hastebin.UploadResult;
import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.FlagYielding;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.Greedy;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Flag;
import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationNode;
import com.bergerkiller.bukkit.tc.attachments.ui.AnimationFramesImportExport;
import com.bergerkiller.bukkit.tc.attachments.ui.AttachmentEditor;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandRequiresPermission;
import com.bergerkiller.bukkit.tc.utils.QuoteEscapedString;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Command("train animation")
public class TrainAnimationCommands {
   private AnimationFramesImportExport tryAccessAnimationMenu(Player player) {
      MapDisplay display = MapDisplay.getHeldDisplay(player, AttachmentEditor.class);
      if (display == null) {
         display = MapDisplay.getHeldDisplay(player);
         if (display == null) {
            player.sendMessage(ChatColor.RED + "You do not have an editor menu open");
            return null;
         }
      }

      MapWidget focused = display.getFocusedWidget();
      if (!(focused instanceof AnimationFramesImportExport)) {
         focused = display.getActivatedWidget();
      }

      if (!(focused instanceof AnimationFramesImportExport)) {
         player.sendMessage(ChatColor.RED + "Train attachment animation menu is not open!");
         return null;
      } else {
         AnimationFramesImportExport menu = (AnimationFramesImportExport)focused;
         if (menu.getAnimationName() == null) {
            player.sendMessage(ChatColor.RED + "No animation is selected yet, please create one!");
            return null;
         } else {
            return menu;
         }
      }
   }

   @CommandRequiresPermission(Permission.COMMAND_GIVE_EDITOR)
   @Command("export")
   @CommandDescription("Exports the train animation frames to a hastebin server")
   private void commandTrainAnimationExport(TrainCarts plugin, final Player player) {
      AnimationFramesImportExport menu = this.tryAccessAnimationMenu(player);
      if (menu != null) {
         List<AnimationNode> nodes = menu.exportAnimationFrames();
         if (nodes.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Animation is empty");
         } else {
            final String animationName = menu.getAnimationName();
            ConfigurationNode tmp = new ConfigurationNode();
            tmp.set("nodes", nodes.stream().map(AnimationNode::serializeToString).collect(Collectors.toList()));
            String config = (String)Pattern.compile("\r?\n").splitAsStream(tmp.toString()).skip(1L).map(String::trim).collect(Collectors.joining("\n"));
            TCConfig.hastebin.upload(config).thenAccept(new Consumer<UploadResult>() {
               public void accept(UploadResult t) {
                  if (t.success()) {
                     player.sendMessage(ChatColor.GREEN + "Animation '" + ChatColor.YELLOW + animationName + ChatColor.GREEN + "' exported: " + ChatColor.WHITE + ChatColor.UNDERLINE + t.url());
                  } else {
                     player.sendMessage(ChatColor.RED + "Failed to export animation '" + animationName + "': " + t.error());
                  }

               }
            });
         }
      }
   }

   @CommandRequiresPermission(Permission.COMMAND_GIVE_EDITOR)
   @Command("import <url>")
   @CommandDescription("Imports train attachment animation frames from an online hastebin server by url")
   private void commandTrainAnimationImport(final TrainCarts plugin, final Player player, @Greedy @FlagYielding @Argument(value = "url",description = "The URL to a Hastebin-hosted paste to download from") String url, @Flag("insert") final boolean insert) {
      if (this.tryAccessAnimationMenu(player) != null) {
         TCConfig.hastebin.download(url).thenAccept(new Consumer<DownloadResult>() {
            public void accept(DownloadResult result) {
               if (!result.success()) {
                  Localization.COMMAND_IMPORT_ERROR.message(player, new String[]{result.error()});
               } else {
                  List frames;
                  try {
                     BufferedReader reader = new BufferedReader(new InputStreamReader(result.contentInputStream(), StandardCharsets.UTF_8));

                     try {
                        frames = (List)reader.lines().map(String::trim).map((s) -> {
                           return s.startsWith("-") ? s.substring(1).trim() : s;
                        }).map((s) -> {
                           QuoteEscapedString q = QuoteEscapedString.tryParseQuoted(s);
                           return q.isQuoteEscaped() ? q.getUnescaped() : s;
                        }).map(AnimationNode::parseFromString).filter(AnimationNode::hasValidDuration).collect(Collectors.toList());
                     } catch (Throwable var7) {
                        try {
                           reader.close();
                        } catch (Throwable var6) {
                           var7.addSuppressed(var6);
                        }

                        throw var7;
                     }

                     reader.close();
                  } catch (Throwable var8) {
                     plugin.getLogger().log(Level.WARNING, "Failed to import animation", var8);
                     Localization.COMMAND_IMPORT_ERROR.message(player, new String[]{var8.getMessage()});
                     return;
                  }

                  if (frames.isEmpty()) {
                     player.sendMessage(ChatColor.RED + "No animation frames could be read from the provided url");
                  } else {
                     AnimationFramesImportExport menu = TrainAnimationCommands.this.tryAccessAnimationMenu(player);
                     if (menu != null) {
                        String animationName = menu.getAnimationName();
                        menu.importAnimationFrames(frames, insert);
                        player.sendMessage(ChatColor.GREEN + "Imported " + ChatColor.WHITE + frames.size() + ChatColor.GREEN + " frames into animation '" + ChatColor.YELLOW + animationName + ChatColor.GREEN + "'!");
                     }
                  }
               }
            }
         });
      }
   }
}
