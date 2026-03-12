package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class WrapperPlayServerTabComplete extends PacketWrapper<WrapperPlayServerTabComplete> {
   private Optional<Integer> transactionID;
   private Optional<WrapperPlayServerTabComplete.CommandRange> commandRange;
   private List<WrapperPlayServerTabComplete.CommandMatch> commandMatches;

   public WrapperPlayServerTabComplete(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerTabComplete(@Nullable Integer transactionID, @NotNull WrapperPlayServerTabComplete.CommandRange commandRange, List<WrapperPlayServerTabComplete.CommandMatch> commandMatches) {
      super((PacketTypeCommon)PacketType.Play.Server.TAB_COMPLETE);
      this.setTransactionId(transactionID);
      this.setCommandRange(commandRange);
      this.commandMatches = commandMatches;
   }

   public void read() {
      int matchLength;
      int i;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
         this.transactionID = Optional.of(this.readVarInt());
         matchLength = this.readVarInt();
         i = this.readVarInt();
         int matchLength = this.readVarInt();
         this.commandRange = Optional.of(new WrapperPlayServerTabComplete.CommandRange(matchLength, matchLength + i));
         this.commandMatches = new ArrayList(matchLength);

         for(int i = 0; i < matchLength; ++i) {
            String text = this.readString();
            Component tooltip = (Component)this.readOptional(PacketWrapper::readComponent);
            WrapperPlayServerTabComplete.CommandMatch commandMatch = new WrapperPlayServerTabComplete.CommandMatch(text, tooltip);
            this.commandMatches.add(commandMatch);
         }
      } else {
         matchLength = this.readVarInt();
         this.commandMatches = new ArrayList(matchLength);

         for(i = 0; i < matchLength; ++i) {
            String text = this.readString();
            WrapperPlayServerTabComplete.CommandMatch commandMatch = new WrapperPlayServerTabComplete.CommandMatch(text, (Component)null);
            this.commandMatches.add(commandMatch);
         }
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
         this.writeVarInt((Integer)this.transactionID.orElse(-1));
         WrapperPlayServerTabComplete.CommandRange commandRange = (WrapperPlayServerTabComplete.CommandRange)this.commandRange.get();
         this.writeVarInt(commandRange.getBegin());
         this.writeVarInt(commandRange.getLength());
         this.writeVarInt(this.commandMatches.size());
         Iterator var2 = this.commandMatches.iterator();

         while(var2.hasNext()) {
            WrapperPlayServerTabComplete.CommandMatch match = (WrapperPlayServerTabComplete.CommandMatch)var2.next();
            this.writeString(match.getText());
            boolean hasTooltip = match.getTooltip().isPresent();
            this.writeBoolean(hasTooltip);
            if (hasTooltip) {
               this.writeComponent((Component)match.getTooltip().get());
            }
         }
      } else {
         this.writeVarInt(this.commandMatches.size());
         Iterator var5 = this.commandMatches.iterator();

         while(var5.hasNext()) {
            WrapperPlayServerTabComplete.CommandMatch match = (WrapperPlayServerTabComplete.CommandMatch)var5.next();
            this.writeString(match.getText());
         }
      }

   }

   public void copy(WrapperPlayServerTabComplete wrapper) {
      this.transactionID = wrapper.transactionID;
      this.commandRange = wrapper.commandRange;
      this.commandMatches = wrapper.commandMatches;
   }

   public Optional<Integer> getTransactionId() {
      return this.transactionID;
   }

   public void setTransactionId(@Nullable Integer transactionID) {
      this.transactionID = Optional.ofNullable(transactionID);
   }

   public Optional<WrapperPlayServerTabComplete.CommandRange> getCommandRange() {
      return this.commandRange;
   }

   public void setCommandRange(@Nullable WrapperPlayServerTabComplete.CommandRange commandRange) {
      this.commandRange = Optional.ofNullable(commandRange);
   }

   public List<WrapperPlayServerTabComplete.CommandMatch> getCommandMatches() {
      return this.commandMatches;
   }

   public void setCommandMatches(List<WrapperPlayServerTabComplete.CommandMatch> commandMatches) {
      this.commandMatches = commandMatches;
   }

   public static class CommandRange {
      private int begin;
      private int end;

      public CommandRange(int begin, int end) {
         this.begin = begin;
         this.end = end;
      }

      public int getBegin() {
         return this.begin;
      }

      public void setBegin(int begin) {
         this.begin = begin;
      }

      public int getEnd() {
         return this.end;
      }

      public void setEnd(int end) {
         this.end = end;
      }

      public int getLength() {
         return this.end - this.begin;
      }
   }

   public static class CommandMatch {
      private String text;
      private Optional<Component> tooltip;

      public CommandMatch(String text, @Nullable Component tooltip) {
         this.text = text;
         this.setTooltip(tooltip);
      }

      public CommandMatch(String text) {
         this.text = text;
         this.tooltip = Optional.empty();
      }

      public String getText() {
         return this.text;
      }

      public void setText(String text) {
         this.text = text;
      }

      public Optional<Component> getTooltip() {
         return this.tooltip;
      }

      public void setTooltip(@Nullable Component tooltip) {
         if (tooltip != null) {
            this.tooltip = Optional.of(tooltip);
         } else {
            this.tooltip = Optional.empty();
         }

      }
   }
}
