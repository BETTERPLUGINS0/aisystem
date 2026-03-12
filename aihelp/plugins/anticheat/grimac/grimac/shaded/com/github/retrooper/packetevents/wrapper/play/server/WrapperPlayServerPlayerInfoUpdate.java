package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.RemoteChatSession;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.TextureProperty;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.UserProfile;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class WrapperPlayServerPlayerInfoUpdate extends PacketWrapper<WrapperPlayServerPlayerInfoUpdate> {
   private EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> actions;
   private List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> entries;

   public WrapperPlayServerPlayerInfoUpdate(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerPlayerInfoUpdate(EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> actions, List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> entries) {
      super((PacketTypeCommon)PacketType.Play.Server.PLAYER_INFO_UPDATE);
      this.actions = actions;
      this.entries = entries;
   }

   public WrapperPlayServerPlayerInfoUpdate(EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> actions, WrapperPlayServerPlayerInfoUpdate.PlayerInfo... entries) {
      super((PacketTypeCommon)PacketType.Play.Server.PLAYER_INFO_UPDATE);
      this.actions = actions;
      this.entries = new ArrayList();
      Collections.addAll(this.entries, entries);
   }

   public WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action action, List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> entries) {
      this(EnumSet.of(action), entries);
   }

   public WrapperPlayServerPlayerInfoUpdate(WrapperPlayServerPlayerInfoUpdate.Action action, WrapperPlayServerPlayerInfoUpdate.PlayerInfo... entries) {
      this(EnumSet.of(action), entries);
   }

   public void read() {
      this.actions = this.readEnumSet(WrapperPlayServerPlayerInfoUpdate.Action.class);
      this.entries = this.readList((wrapper) -> {
         UUID uuid = wrapper.readUUID();
         UserProfile gameProfile = new UserProfile(uuid, (String)null);
         GameMode gameMode = GameMode.defaultGameMode();
         boolean listed = false;
         int latency = 0;
         RemoteChatSession chatSession = null;
         Component displayName = null;
         int listOrder = 0;
         boolean showHat = false;
         Iterator var11 = this.actions.iterator();

         while(true) {
            label36:
            while(var11.hasNext()) {
               WrapperPlayServerPlayerInfoUpdate.Action action = (WrapperPlayServerPlayerInfoUpdate.Action)var11.next();
               switch(action.ordinal()) {
               case 0:
                  gameProfile.setUUID(uuid);
                  gameProfile.setName(wrapper.readString(16));
                  int propertyCount = wrapper.readVarInt();
                  int j = 0;

                  while(true) {
                     if (j >= propertyCount) {
                        continue label36;
                     }

                     String propertyName = wrapper.readString();
                     String propertyValue = wrapper.readString();
                     String propertySignature = (String)wrapper.readOptional(PacketWrapper::readString);
                     TextureProperty textureProperty = new TextureProperty(propertyName, propertyValue, propertySignature);
                     gameProfile.getTextureProperties().add(textureProperty);
                     ++j;
                  }
               case 1:
                  chatSession = (RemoteChatSession)wrapper.readOptional(PacketWrapper::readRemoteChatSession);
                  break;
               case 2:
                  gameMode = GameMode.getById(wrapper.readVarInt());
                  break;
               case 3:
                  listed = wrapper.readBoolean();
                  break;
               case 4:
                  latency = wrapper.readVarInt();
                  break;
               case 5:
                  displayName = (Component)wrapper.readOptional(PacketWrapper::readComponent);
                  break;
               case 6:
                  if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
                     listOrder = wrapper.readVarInt();
                  }
                  break;
               case 7:
                  if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
                     showHat = wrapper.readBoolean();
                  }
               }
            }

            return new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(gameProfile, listed, latency, gameMode, displayName, chatSession, listOrder, showHat);
         }
      });
   }

   public void write() {
      this.writeEnumSet(this.actions, WrapperPlayServerPlayerInfoUpdate.Action.class);
      this.writeList(this.entries, (wrapper, playerInfo) -> {
         wrapper.writeUUID(playerInfo.getProfileId());
         Iterator var3 = this.actions.iterator();

         while(var3.hasNext()) {
            WrapperPlayServerPlayerInfoUpdate.Action action = (WrapperPlayServerPlayerInfoUpdate.Action)var3.next();
            switch(action.ordinal()) {
            case 0:
               wrapper.writeString(playerInfo.getGameProfile().getName(), 16);
               this.writeList(playerInfo.getGameProfile().getTextureProperties(), (w, textureProperty) -> {
                  w.writeString(textureProperty.getName());
                  w.writeString(textureProperty.getValue());
                  w.writeOptional(textureProperty.getSignature(), PacketWrapper::writeString);
               });
               break;
            case 1:
               wrapper.writeOptional(playerInfo.getChatSession(), PacketWrapper::writeRemoteChatSession);
               break;
            case 2:
               wrapper.writeVarInt(playerInfo.getGameMode().getId());
               break;
            case 3:
               wrapper.writeBoolean(playerInfo.isListed());
               break;
            case 4:
               wrapper.writeVarInt(playerInfo.getLatency());
               break;
            case 5:
               wrapper.writeOptional(playerInfo.getDisplayName(), PacketWrapper::writeComponent);
               break;
            case 6:
               if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
                  wrapper.writeVarInt(playerInfo.getListOrder());
               }
               break;
            case 7:
               if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
                  wrapper.writeBoolean(playerInfo.isShowHat());
               }
            }
         }

      });
   }

   public void copy(WrapperPlayServerPlayerInfoUpdate wrapper) {
      this.actions = wrapper.actions;
      this.entries = wrapper.entries;
   }

   public EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> getActions() {
      return this.actions;
   }

   public void setActions(EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> actions) {
      this.actions = actions;
   }

   public List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> getEntries() {
      return this.entries;
   }

   public void setEntries(List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> entries) {
      this.entries = entries;
   }

   public static class PlayerInfo {
      private UserProfile profile;
      private boolean listed;
      private int latency;
      private GameMode gameMode;
      @Nullable
      private Component displayName;
      @Nullable
      private RemoteChatSession chatSession;
      private int listOrder;
      private boolean showHat;

      public PlayerInfo(UUID profileId) {
         this(new UserProfile(profileId, ""));
      }

      public PlayerInfo(UserProfile profile) {
         this.listed = true;
         this.profile = profile;
      }

      public PlayerInfo(UserProfile profile, boolean listed, int latency, GameMode gameMode, @Nullable Component displayName, @Nullable RemoteChatSession chatSession) {
         this(profile, listed, latency, gameMode, displayName, chatSession, 0);
      }

      public PlayerInfo(UserProfile profile, boolean listed, int latency, GameMode gameMode, @Nullable Component displayName, @Nullable RemoteChatSession chatSession, int listOrder) {
         this(profile, listed, latency, gameMode, displayName, chatSession, listOrder, false);
      }

      public PlayerInfo(UserProfile profile, boolean listed, int latency, GameMode gameMode, @Nullable Component displayName, @Nullable RemoteChatSession chatSession, int listOrder, boolean showHat) {
         this.listed = true;
         this.profile = profile;
         this.listed = listed;
         this.latency = latency;
         this.gameMode = gameMode;
         this.displayName = displayName;
         this.chatSession = chatSession;
         this.listOrder = listOrder;
         this.showHat = showHat;
      }

      public UUID getProfileId() {
         return this.profile.getUUID();
      }

      public UserProfile getGameProfile() {
         return this.profile;
      }

      public boolean isListed() {
         return this.listed;
      }

      public int getLatency() {
         return this.latency;
      }

      public GameMode getGameMode() {
         return this.gameMode;
      }

      @Nullable
      public Component getDisplayName() {
         return this.displayName;
      }

      @Nullable
      public RemoteChatSession getChatSession() {
         return this.chatSession;
      }

      public int getListOrder() {
         return this.listOrder;
      }

      public boolean isShowHat() {
         return this.showHat;
      }

      public void setGameProfile(UserProfile gameProfile) {
         this.profile = gameProfile;
      }

      public void setListed(boolean listed) {
         this.listed = listed;
      }

      public void setLatency(int latency) {
         this.latency = latency;
      }

      public void setGameMode(GameMode gameMode) {
         this.gameMode = gameMode;
      }

      public void setDisplayName(@Nullable Component displayName) {
         this.displayName = displayName;
      }

      public void setChatSession(@Nullable RemoteChatSession chatSession) {
         this.chatSession = chatSession;
      }

      public void setListOrder(int listOrder) {
         this.listOrder = listOrder;
      }

      public void setShowHat(boolean showHat) {
         this.showHat = showHat;
      }
   }

   public static enum Action {
      ADD_PLAYER,
      INITIALIZE_CHAT,
      UPDATE_GAME_MODE,
      UPDATE_LISTED,
      UPDATE_LATENCY,
      UPDATE_DISPLAY_NAME,
      UPDATE_LIST_ORDER,
      UPDATE_HAT;

      public static final WrapperPlayServerPlayerInfoUpdate.Action[] VALUES = values();

      // $FF: synthetic method
      private static WrapperPlayServerPlayerInfoUpdate.Action[] $values() {
         return new WrapperPlayServerPlayerInfoUpdate.Action[]{ADD_PLAYER, INITIALIZE_CHAT, UPDATE_GAME_MODE, UPDATE_LISTED, UPDATE_LATENCY, UPDATE_DISPLAY_NAME, UPDATE_LIST_ORDER, UPDATE_HAT};
      }
   }
}
