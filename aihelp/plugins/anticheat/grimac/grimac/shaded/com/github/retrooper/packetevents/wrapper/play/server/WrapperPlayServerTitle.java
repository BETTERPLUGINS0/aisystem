package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.gson.GsonComponentSerializer;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Objects;

public class WrapperPlayServerTitle extends PacketWrapper<WrapperPlayServerTitle> {
   /** @deprecated */
   @Deprecated
   public static boolean HANDLE_JSON = true;
   private WrapperPlayServerTitle.TitleAction action;
   @Nullable
   private Component title;
   @Nullable
   private Component subtitle;
   @Nullable
   private Component actionBar;
   private int fadeInTicks;
   private int stayTicks;
   private int fadeOutTicks;

   public WrapperPlayServerTitle(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerTitle(WrapperPlayServerTitle.TitleAction action, @Nullable Component title, @Nullable Component subtitle, @Nullable Component actionBar, int fadeInTicks, int stayTicks, int fadeOutTicks) {
      super((PacketTypeCommon)PacketType.Play.Server.TITLE);
      this.action = action;
      this.title = title;
      this.subtitle = subtitle;
      this.actionBar = actionBar;
      this.fadeInTicks = fadeInTicks;
      this.stayTicks = stayTicks;
      this.fadeOutTicks = fadeOutTicks;
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayServerTitle(WrapperPlayServerTitle.TitleAction action, @Nullable String titleJson, @Nullable String subtitleJson, @Nullable String actionBarJson, int fadeInTicks, int stayTicks, int fadeOutTicks) {
      super((PacketTypeCommon)PacketType.Play.Server.TITLE);
      this.action = action;
      GsonComponentSerializer gson = this.getSerializers().gson();
      this.title = gson.deserializeOrNull(titleJson);
      this.subtitle = gson.deserializeOrNull(subtitleJson);
      this.actionBar = gson.deserializeOrNull(actionBarJson);
      this.fadeInTicks = fadeInTicks;
      this.stayTicks = stayTicks;
      this.fadeOutTicks = fadeOutTicks;
   }

   public void read() {
      boolean modern = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11);
      int id = this.readVarInt();
      if (modern) {
         this.action = WrapperPlayServerTitle.TitleAction.fromId(id);
      } else {
         this.action = WrapperPlayServerTitle.TitleAction.fromLegacyId(id);
      }

      switch(((WrapperPlayServerTitle.TitleAction)Objects.requireNonNull(this.action)).ordinal()) {
      case 0:
         this.title = this.readComponent();
         break;
      case 1:
         this.subtitle = this.readComponent();
         break;
      case 2:
         this.actionBar = this.readComponent();
         break;
      case 3:
         this.fadeInTicks = this.readInt();
         this.stayTicks = this.readInt();
         this.fadeOutTicks = this.readInt();
      }

   }

   public void copy(WrapperPlayServerTitle wrapper) {
      this.action = wrapper.action;
      this.title = wrapper.title;
      this.subtitle = wrapper.subtitle;
      this.actionBar = wrapper.actionBar;
      this.fadeInTicks = wrapper.fadeInTicks;
      this.stayTicks = wrapper.stayTicks;
      this.fadeOutTicks = wrapper.fadeOutTicks;
   }

   public void write() {
      boolean modern = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11);
      int id = modern ? this.action.getId() : this.action.getLegacyId();
      this.writeVarInt(id);
      switch(this.action.ordinal()) {
      case 0:
         this.writeComponent(this.title);
         break;
      case 1:
         this.writeComponent(this.subtitle);
         break;
      case 2:
         this.writeComponent(this.actionBar);
         break;
      case 3:
         this.writeInt(this.fadeInTicks);
         this.writeInt(this.stayTicks);
         this.writeInt(this.fadeOutTicks);
      }

   }

   public WrapperPlayServerTitle.TitleAction getAction() {
      return this.action;
   }

   public void setAction(WrapperPlayServerTitle.TitleAction action) {
      this.action = action;
   }

   @Nullable
   public Component getTitle() {
      return this.title;
   }

   public void setTitle(@Nullable Component title) {
      this.title = title;
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public String getTitleJson() {
      return this.getSerializers().asJson(this.getTitle());
   }

   /** @deprecated */
   @Deprecated
   public void setTitleJson(@Nullable String titleJson) {
      this.setTitle(this.getSerializers().fromJson(titleJson));
   }

   @Nullable
   public Component getSubtitle() {
      return this.subtitle;
   }

   public void setSubtitle(@Nullable Component subtitle) {
      this.subtitle = subtitle;
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public String getSubtitleJson() {
      return this.getSerializers().asJson(this.getSubtitle());
   }

   /** @deprecated */
   @Deprecated
   public void setSubtitleJson(@Nullable String subtitleJson) {
      this.setSubtitle(this.getSerializers().fromJson(subtitleJson));
   }

   @Nullable
   public Component getActionBar() {
      return this.actionBar;
   }

   public void setActionBar(@Nullable Component actionBar) {
      this.actionBar = actionBar;
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public String getActionBarJson() {
      return this.getSerializers().asJson(this.getActionBar());
   }

   /** @deprecated */
   @Deprecated
   public void setActionBarJson(@Nullable String actionBarJson) {
      this.setActionBar(this.getSerializers().fromJson(actionBarJson));
   }

   public int getFadeInTicks() {
      return this.fadeInTicks;
   }

   public void setFadeInTicks(int fadeInTicks) {
      this.fadeInTicks = fadeInTicks;
   }

   public int getStayTicks() {
      return this.stayTicks;
   }

   public void setStayTicks(int stayTicks) {
      this.stayTicks = stayTicks;
   }

   public int getFadeOutTicks() {
      return this.fadeOutTicks;
   }

   public void setFadeOutTicks(int fadeOutTicks) {
      this.fadeOutTicks = fadeOutTicks;
   }

   public static enum TitleAction {
      SET_TITLE(0),
      SET_SUBTITLE(1),
      SET_ACTION_BAR,
      SET_TIMES_AND_DISPLAY(2),
      HIDE(3),
      RESET(4);

      private final int legacyId;

      private TitleAction() {
         this(-1);
      }

      private TitleAction(int legacyId) {
         this.legacyId = legacyId;
      }

      public static WrapperPlayServerTitle.TitleAction fromId(int id) {
         return values()[id];
      }

      public static WrapperPlayServerTitle.TitleAction fromLegacyId(int legacyId) {
         WrapperPlayServerTitle.TitleAction[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            WrapperPlayServerTitle.TitleAction action = var1[var3];
            if (action.legacyId == legacyId) {
               return action;
            }
         }

         return null;
      }

      public int getId() {
         return this.ordinal();
      }

      public int getLegacyId() {
         return this.legacyId;
      }

      // $FF: synthetic method
      private static WrapperPlayServerTitle.TitleAction[] $values() {
         return new WrapperPlayServerTitle.TitleAction[]{SET_TITLE, SET_SUBTITLE, SET_ACTION_BAR, SET_TIMES_AND_DISPLAY, HIDE, RESET};
      }
   }
}
