package ac.grim.grimac.checks.impl.chat;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.impl.multiactions.MultiActionsC;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatCommandUnsigned;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@CheckData(
   name = "ChatC",
   description = "Moving while chatting",
   experimental = true
)
public class ChatC extends Check implements PacketCheck {
   @Nullable
   private Predicate<String> exemptRegex;

   public ChatC(GrimPlayer player) {
      super(player);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.CHAT_MESSAGE) {
         this.check((new WrapperPlayClientChatMessage(event)).getMessage(), event);
      }

      if (event.getPacketType() == PacketType.Play.Client.CHAT_COMMAND_UNSIGNED) {
         this.check("/" + (new WrapperPlayClientChatCommandUnsigned(event)).getCommand(), event);
      }

      if (event.getPacketType() == PacketType.Play.Client.CHAT_COMMAND) {
         this.check("/" + (new WrapperPlayClientChatCommand(event)).getCommand(), event);
      }

   }

   private void check(String message, PacketReceiveEvent event) {
      if (this.exemptRegex == null || !this.exemptRegex.test(message)) {
         String verbose = MultiActionsC.getVerbose(this.player);
         if (!verbose.isEmpty() && this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
            event.setCancelled(true);
            this.player.onPacketCancel();
         }

      }
   }

   public void onReload(ConfigManager config) {
      String regexString = config.getStringElse(this.getConfigName() + ".exempt-regex", (String)null);
      this.exemptRegex = regexString == null ? null : Pattern.compile(regexString).asMatchPredicate();
   }
}
