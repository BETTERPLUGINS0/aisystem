package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.score.ScoreFormat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Optional;

public class WrapperPlayServerUpdateScore extends PacketWrapper<WrapperPlayServerUpdateScore> {
   private String entityName;
   private WrapperPlayServerUpdateScore.Action action;
   private String objectiveName;
   private Optional<Integer> value;
   @Nullable
   private Component entityDisplayName;
   @Nullable
   private ScoreFormat scoreFormat;

   public WrapperPlayServerUpdateScore(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerUpdateScore(String entityName, WrapperPlayServerUpdateScore.Action action, String objectiveName, Optional<Integer> value) {
      super((PacketTypeCommon)PacketType.Play.Server.UPDATE_SCORE);
      this.entityName = entityName;
      this.action = action;
      this.objectiveName = objectiveName;
      this.value = value;
   }

   public WrapperPlayServerUpdateScore(String entityName, WrapperPlayServerUpdateScore.Action action, String objectiveName, int value, @Nullable Component entityDisplayName, @Nullable ScoreFormat scoreFormat) {
      super((PacketTypeCommon)PacketType.Play.Server.UPDATE_SCORE);
      this.entityName = entityName;
      this.action = action;
      this.objectiveName = objectiveName;
      this.value = Optional.of(value);
      this.entityDisplayName = entityDisplayName;
      this.scoreFormat = scoreFormat;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         this.entityName = this.readString();
         this.objectiveName = this.readString();
         this.value = Optional.of(this.readVarInt());
         this.entityDisplayName = (Component)this.readOptional(PacketWrapper::readComponent);
         this.scoreFormat = (ScoreFormat)this.readOptional(ScoreFormat::readTyped);
      } else if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.entityName = this.readString(16);
         this.action = WrapperPlayServerUpdateScore.Action.VALUES[this.readByte()];
         if (this.action != WrapperPlayServerUpdateScore.Action.REMOVE_ITEM) {
            this.objectiveName = this.readString(16);
            this.value = Optional.of(this.readInt());
         } else {
            this.objectiveName = "";
            this.value = Optional.empty();
         }
      } else {
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            this.entityName = this.readString();
         } else {
            this.entityName = this.readString(40);
         }

         this.action = WrapperPlayServerUpdateScore.Action.VALUES[this.readByte()];
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            this.objectiveName = this.readString();
         } else {
            this.objectiveName = this.readString(16);
         }

         if (this.action != WrapperPlayServerUpdateScore.Action.REMOVE_ITEM) {
            this.value = Optional.of(this.readVarInt());
         } else {
            this.objectiveName = "";
            this.value = Optional.empty();
         }
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         this.writeString(this.entityName);
         this.writeString(this.objectiveName);
         this.writeVarInt((Integer)this.value.orElse(0));
         this.writeOptional(this.entityDisplayName, PacketWrapper::writeComponent);
         this.writeOptional(this.scoreFormat, ScoreFormat::writeTyped);
      } else if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.writeString(this.entityName, 16);
         this.writeByte(this.action.ordinal());
         if (this.action != WrapperPlayServerUpdateScore.Action.REMOVE_ITEM) {
            this.writeString(this.objectiveName, 16);
            this.writeInt((Integer)this.value.orElse(-1));
         } else {
            this.objectiveName = "";
            this.value = Optional.empty();
         }
      } else {
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            this.writeString(this.entityName);
         } else {
            this.writeString(this.entityName, 40);
         }

         this.writeByte(this.action.ordinal());
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18)) {
            this.writeString(this.objectiveName);
         } else {
            this.writeString(this.objectiveName, 16);
         }

         if (this.action != WrapperPlayServerUpdateScore.Action.REMOVE_ITEM) {
            this.writeVarInt((Integer)this.value.orElse(-1));
         }
      }

   }

   public void copy(WrapperPlayServerUpdateScore wrapper) {
      this.entityName = wrapper.entityName;
      this.action = wrapper.action;
      this.objectiveName = wrapper.objectiveName;
      this.value = wrapper.value;
      this.entityDisplayName = wrapper.entityDisplayName;
      this.scoreFormat = wrapper.scoreFormat;
   }

   public String getEntityName() {
      return this.entityName;
   }

   public void setEntityName(String entityName) {
      this.entityName = entityName;
   }

   public WrapperPlayServerUpdateScore.Action getAction() {
      return this.action;
   }

   public void setAction(WrapperPlayServerUpdateScore.Action action) {
      this.action = action;
   }

   public String getObjectiveName() {
      return this.objectiveName;
   }

   public void setObjectiveName(String objectiveName) {
      this.objectiveName = objectiveName;
   }

   public Optional<Integer> getValue() {
      return this.value;
   }

   public void setValue(Optional<Integer> value) {
      this.value = value;
   }

   @Nullable
   public Component getEntityDisplayName() {
      return this.entityDisplayName;
   }

   public void setEntityDisplayName(@Nullable Component entityDisplayName) {
      this.entityDisplayName = entityDisplayName;
   }

   @Nullable
   public ScoreFormat getScoreFormat() {
      return this.scoreFormat;
   }

   public void setScoreFormat(@Nullable ScoreFormat scoreFormat) {
      this.scoreFormat = scoreFormat;
   }

   public static enum Action {
      CREATE_OR_UPDATE_ITEM,
      REMOVE_ITEM;

      public static final WrapperPlayServerUpdateScore.Action[] VALUES = values();

      // $FF: synthetic method
      private static WrapperPlayServerUpdateScore.Action[] $values() {
         return new WrapperPlayServerUpdateScore.Action[]{CREATE_OR_UPDATE_ITEM, REMOVE_ITEM};
      }
   }
}
