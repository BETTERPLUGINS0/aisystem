package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.nmsutil.BlockBreakSpeed;
import ac.grim.grimac.utils.viaversion.ViaVersionUtil;
import java.util.Set;

@CheckData(
   name = "FastBreak",
   description = "Breaking blocks too quickly"
)
public class FastBreak extends Check implements BlockBreakCheck {
   private static final Set<StateType> EXEMPT_STATES = Set.of();
   private final boolean clientOlderThanServer;
   Vector3i targetBlockPosition;
   double maximumBlockDamage;
   long lastFinishBreak;
   long startBreak;
   double blockBreakBalance;
   double blockDelayBalance;

   public FastBreak(GrimPlayer playerData) {
      super(playerData);
      this.clientOlderThanServer = PacketEvents.getAPI().getServerManager().getVersion().getProtocolVersion() > this.player.getClientVersion().getProtocolVersion();
      this.targetBlockPosition = null;
      this.maximumBlockDamage = 0.0D;
      this.lastFinishBreak = 0L;
      this.startBreak = 0L;
      this.blockBreakBalance = 0.0D;
      this.blockDelayBalance = 0.0D;
   }

   public void onBlockBreak(BlockBreak blockBreak) {
      if (blockBreak.action == DiggingAction.START_DIGGING) {
         WrappedBlockState defaultState;
         if (!ViaVersionUtil.isAvailable) {
            defaultState = WrappedBlockState.getDefaultState(this.player.getClientVersion(), blockBreak.block.getType());
            if (defaultState.getType() == StateTypes.AIR || EXEMPT_STATES.contains(defaultState.getType())) {
               return;
            }
         }

         defaultState = this.clientOlderThanServer ? WrappedBlockState.getByGlobalId(this.player.getClientVersion(), this.player.getViaTranslatedClientBlockID(blockBreak.block.getGlobalId())) : blockBreak.block;
         this.startBreak = System.currentTimeMillis() - (long)(this.targetBlockPosition == null ? 50 : 0);
         this.targetBlockPosition = blockBreak.position;
         this.maximumBlockDamage = BlockBreakSpeed.getBlockDamage(this.player, defaultState);
         double breakDelay = (double)(System.currentTimeMillis() - this.lastFinishBreak);
         if (breakDelay >= 275.0D) {
            this.blockDelayBalance *= 0.9D;
         } else {
            this.blockDelayBalance += 300.0D - breakDelay;
         }

         if (this.blockDelayBalance > 1000.0D && this.flagAndAlert("delay=" + breakDelay + "ms, type=" + String.valueOf(blockBreak.block.getType())) && this.shouldModifyPackets()) {
            blockBreak.cancel();
         }

         this.clampBalance();
      }

      if (blockBreak.action == DiggingAction.FINISHED_DIGGING && this.targetBlockPosition != null) {
         double predictedTime = Math.ceil(1.0D / this.maximumBlockDamage) * 50.0D;
         double realTime = (double)(System.currentTimeMillis() - this.startBreak);
         double diff = predictedTime - realTime;
         this.clampBalance();
         if (diff < 25.0D) {
            this.blockBreakBalance *= 0.9D;
         } else {
            this.blockBreakBalance += diff;
         }

         if (this.blockBreakBalance > 1000.0D && this.flagAndAlert("diff=" + diff + "ms, balance=" + this.blockBreakBalance + "ms, type=" + String.valueOf(blockBreak.block.getType())) && this.shouldModifyPackets()) {
            blockBreak.cancel();
         }

         this.lastFinishBreak = this.startBreak = System.currentTimeMillis();
      }

   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
         if (event.getPacketType() != PacketType.Play.Client.ANIMATION) {
            return;
         }
      } else if (!WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
         return;
      }

      if (this.targetBlockPosition != null) {
         this.maximumBlockDamage = Math.max(this.maximumBlockDamage, BlockBreakSpeed.getBlockDamage(this.player, this.player.compensatedWorld.getBlock(this.targetBlockPosition)));
      }

   }

   private void clampBalance() {
      double balance = (double)Math.max(1000, this.player.getTransactionPing());
      this.blockBreakBalance = GrimMath.clamp(this.blockBreakBalance, -balance, balance);
      this.blockDelayBalance = GrimMath.clamp(this.blockDelayBalance, -balance, balance);
   }
}
