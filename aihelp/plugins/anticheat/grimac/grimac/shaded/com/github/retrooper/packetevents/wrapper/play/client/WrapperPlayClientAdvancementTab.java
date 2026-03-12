package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Optional;

public class WrapperPlayClientAdvancementTab extends PacketWrapper<WrapperPlayClientAdvancementTab> {
   private WrapperPlayClientAdvancementTab.Action action;
   @Nullable
   private String tabID;

   public WrapperPlayClientAdvancementTab(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientAdvancementTab(WrapperPlayClientAdvancementTab.Action action, @Nullable String tabID) {
      super((PacketTypeCommon)PacketType.Play.Client.ADVANCEMENT_TAB);
      this.action = action;
      this.tabID = tabID;
   }

   public void read() {
      this.action = WrapperPlayClientAdvancementTab.Action.getById(this.readVarInt());
      if (this.action == WrapperPlayClientAdvancementTab.Action.OPENED_TAB) {
         this.tabID = this.readString();
      }

   }

   public void copy(WrapperPlayClientAdvancementTab wrapper) {
      this.action = wrapper.action;
      this.tabID = wrapper.tabID;
   }

   public void write() {
      this.writeVarInt(this.action.ordinal());
      if (this.action == WrapperPlayClientAdvancementTab.Action.OPENED_TAB) {
         this.writeString(this.tabID);
      }

   }

   public WrapperPlayClientAdvancementTab.Action getAction() {
      return this.action;
   }

   public void setAction(WrapperPlayClientAdvancementTab.Action action) {
      this.action = action;
   }

   public Optional<String> getTabId() {
      return Optional.ofNullable(this.tabID);
   }

   public void setTabId(String tabID) {
      this.tabID = tabID;
   }

   public static enum Action {
      OPENED_TAB,
      CLOSED_SCREEN;

      private static final WrapperPlayClientAdvancementTab.Action[] VALUES = values();

      public static WrapperPlayClientAdvancementTab.Action getById(int id) {
         return VALUES[id];
      }

      // $FF: synthetic method
      private static WrapperPlayClientAdvancementTab.Action[] $values() {
         return new WrapperPlayClientAdvancementTab.Action[]{OPENED_TAB, CLOSED_SCREEN};
      }
   }
}
