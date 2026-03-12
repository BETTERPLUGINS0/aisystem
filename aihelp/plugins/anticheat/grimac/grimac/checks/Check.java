package ac.grim.grimac.checks;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.event.events.FlagEvent;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Objects;
import lombok.Generated;

public class Check extends GrimProcessor implements AbstractCheck {
   @NotNull
   protected final GrimPlayer player;
   public double violations;
   private double decay;
   private double setbackVL;
   private String checkName;
   private String configName;
   private String alternativeName;
   private String displayName;
   private String description;
   private boolean experimental;
   private boolean isEnabled;
   private boolean exemptPermission;
   private boolean noSetbackPermission;
   private boolean noModifyPacketPermission;
   private long lastViolationTime;

   public Check(@NotNull GrimPlayer player) {
      this.player = (GrimPlayer)Objects.requireNonNull(player);
      CheckData checkData = (CheckData)this.getClass().getAnnotation(CheckData.class);
      if (checkData != null) {
         this.checkName = checkData.name();
         this.configName = checkData.configName();
         if (this.configName.equals("DEFAULT")) {
            this.configName = this.checkName;
         }

         this.decay = checkData.decay();
         this.setbackVL = checkData.setback();
         this.alternativeName = checkData.alternativeName();
         this.experimental = checkData.experimental();
         this.description = checkData.description();
         this.displayName = this.checkName;
      }

      this.reload();
   }

   public boolean shouldModifyPackets() {
      return this.isEnabled && !this.player.disableGrim && !this.player.noModifyPacketPermission && !this.noModifyPacketPermission && !this.exemptPermission;
   }

   public final void updatePermissions() {
      if (this.configName != null && this.player.platformPlayer != null) {
         String id = this.configName.toLowerCase();
         this.exemptPermission = this.player.platformPlayer.hasPermission("grim.exempt." + id);
         this.noSetbackPermission = this.player.platformPlayer.hasPermission("grim.nosetback." + id);
         this.noModifyPacketPermission = this.player.platformPlayer.hasPermission("grim.nomodifypacket." + id);
      }
   }

   public final boolean flagAndAlert(String verbose) {
      if (this.flag(verbose)) {
         this.alert(verbose);
         return true;
      } else {
         return false;
      }
   }

   public final boolean flagAndAlert() {
      return this.flagAndAlert("");
   }

   public final boolean flag() {
      return this.flag("");
   }

   public final boolean flag(String verbose) {
      if (!this.player.disableGrim && (!this.experimental || this.player.isExperimentalChecks()) && !this.exemptPermission) {
         FlagEvent event = new FlagEvent(this.player, this, verbose);
         GrimAPI.INSTANCE.getEventBus().post(event);
         if (event.isCancelled()) {
            return false;
         } else {
            this.player.punishmentManager.handleViolation(this);
            this.lastViolationTime = System.currentTimeMillis();
            ++this.violations;
            return true;
         }
      } else {
         return false;
      }
   }

   public final boolean flagWithSetback() {
      return this.flagWithSetback("");
   }

   public final boolean flagWithSetback(String verbose) {
      if (this.flag(verbose)) {
         this.setbackIfAboveSetbackVL();
         return true;
      } else {
         return false;
      }
   }

   public final boolean flagAndAlertWithSetback() {
      return this.flagAndAlertWithSetback("");
   }

   public final boolean flagAndAlertWithSetback(String verbose) {
      if (this.flagAndAlert(verbose)) {
         this.setbackIfAboveSetbackVL();
         return true;
      } else {
         return false;
      }
   }

   public final void reward() {
      this.violations = Math.max(0.0D, this.violations - this.decay);
   }

   public final void reload(ConfigManager configuration) {
      this.decay = configuration.getDoubleElse(this.configName + ".decay", this.decay);
      this.setbackVL = configuration.getDoubleElse(this.configName + ".setbackvl", this.setbackVL);
      this.displayName = configuration.getStringElse(this.configName + ".displayname", this.checkName);
      this.description = configuration.getStringElse(this.configName + ".description", this.description);
      if (this.setbackVL == -1.0D) {
         this.setbackVL = Double.MAX_VALUE;
      }

      this.onReload(configuration);
   }

   public void onReload(ConfigManager config) {
   }

   public boolean alert(String verbose) {
      return this.player.punishmentManager.handleAlert(this.player, verbose, this);
   }

   public boolean setbackIfAboveSetbackVL() {
      return this.shouldSetback() ? this.player.getSetbackTeleportUtil().executeViolationSetback() : false;
   }

   public boolean shouldSetback() {
      return !this.noSetbackPermission && this.violations > this.setbackVL;
   }

   public String formatOffset(double offset) {
      return offset > 0.001D ? String.format("%.5f", offset) : String.format("%.2E", offset);
   }

   public static boolean isTransaction(PacketTypeCommon packetType) {
      return packetType == PacketType.Play.Client.PONG || packetType == PacketType.Play.Client.WINDOW_CONFIRMATION;
   }

   public static boolean isAsync(PacketTypeCommon packetType) {
      return packetType == PacketType.Play.Client.KEEP_ALIVE || packetType == PacketType.Play.Client.CHUNK_BATCH_ACK;
   }

   public boolean isUpdate(PacketTypeCommon packetType) {
      return WrapperPlayClientPlayerFlying.isFlying(packetType) || packetType == PacketType.Play.Client.CLIENT_TICK_END || isTransaction(packetType);
   }

   public boolean isTickPacket(PacketTypeCommon packetType) {
      if (this.isTickPacketIncludingNonMovement(packetType)) {
         if (!WrapperPlayClientPlayerFlying.isFlying(packetType)) {
            return true;
         } else {
            return !this.player.packetStateData.lastPacketWasTeleport && !this.player.packetStateData.lastPacketWasOnePointSeventeenDuplicate;
         }
      } else {
         return false;
      }
   }

   public boolean isTickPacketIncludingNonMovement(PacketTypeCommon packetType) {
      return this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2) && !this.player.packetStateData.didSendMovementBeforeTickEnd && packetType == PacketType.Play.Client.CLIENT_TICK_END ? true : WrapperPlayClientPlayerFlying.isFlying(packetType);
   }

   @NotNull
   @Generated
   public GrimPlayer getPlayer() {
      return this.player;
   }

   @Generated
   public double getViolations() {
      return this.violations;
   }

   @Generated
   public double getDecay() {
      return this.decay;
   }

   @Generated
   public double getSetbackVL() {
      return this.setbackVL;
   }

   @Generated
   public String getCheckName() {
      return this.checkName;
   }

   @Generated
   public String getConfigName() {
      return this.configName;
   }

   @Generated
   public String getAlternativeName() {
      return this.alternativeName;
   }

   @Generated
   public String getDisplayName() {
      return this.displayName;
   }

   @Generated
   public String getDescription() {
      return this.description;
   }

   @Generated
   public boolean isExperimental() {
      return this.experimental;
   }

   @Generated
   public boolean isEnabled() {
      return this.isEnabled;
   }

   @Generated
   public boolean isExemptPermission() {
      return this.exemptPermission;
   }

   @Generated
   public boolean isNoSetbackPermission() {
      return this.noSetbackPermission;
   }

   @Generated
   public boolean isNoModifyPacketPermission() {
      return this.noModifyPacketPermission;
   }

   @Generated
   public long getLastViolationTime() {
      return this.lastViolationTime;
   }

   @Generated
   public void setEnabled(boolean isEnabled) {
      this.isEnabled = isEnabled;
   }
}
