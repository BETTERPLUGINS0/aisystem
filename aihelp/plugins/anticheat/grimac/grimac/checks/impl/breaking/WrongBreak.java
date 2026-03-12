package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.nmsutil.BlockBreakSpeed;

@CheckData(
   name = "WrongBreak"
)
public class WrongBreak extends Check implements BlockBreakCheck {
   private final int exemptedY;
   private boolean lastBlockWasInstantBreak;
   private Vector3i lastBlock;
   private Vector3i lastCancelledBlock;
   private Vector3i lastLastBlock;

   public WrongBreak(GrimPlayer player) {
      super(player);
      this.exemptedY = this.player.getClientVersion().isOlderThan(ClientVersion.V_1_8) ? 255 : (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14) ? -1 : 4095);
      this.lastBlockWasInstantBreak = false;
      this.lastLastBlock = null;
   }

   private boolean shouldExempt(WrappedBlockState block, int yPos) {
      if (this.lastLastBlock == null && this.lastBlock != null) {
         if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_14_4) && yPos != this.exemptedY) {
            return false;
         } else {
            return this.player.getClientVersion().isOlderThan(ClientVersion.V_1_14_4) || BlockBreakSpeed.getBlockDamage(this.player, block) < 1.0D;
         }
      } else {
         return false;
      }
   }

   public void onBlockBreak(BlockBreak blockBreak) {
      Vector3i pos;
      if (blockBreak.action == DiggingAction.START_DIGGING) {
         pos = blockBreak.position;
         this.lastBlockWasInstantBreak = BlockBreakSpeed.getBlockDamage(this.player, blockBreak.block) >= 1.0D;
         this.lastCancelledBlock = null;
         this.lastLastBlock = this.lastBlock;
         this.lastBlock = pos;
      }

      String var10001;
      if (blockBreak.action == DiggingAction.CANCELLED_DIGGING) {
         pos = blockBreak.position;
         if (!this.shouldExempt(blockBreak.block, pos.y) && !pos.equals(this.lastBlock) && (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_14_4) || !this.lastBlockWasInstantBreak && pos.equals(this.lastCancelledBlock))) {
            var10001 = MessageUtil.toUnlabledString(this.lastBlock);
            if (this.flagAndAlert("action=CANCELLED_DIGGING, last=" + var10001 + ", pos=" + MessageUtil.toUnlabledString(pos)) && this.shouldModifyPackets()) {
               blockBreak.cancel();
            }
         }

         this.lastCancelledBlock = pos;
         this.lastLastBlock = null;
         this.lastBlock = null;
      } else {
         if (blockBreak.action == DiggingAction.FINISHED_DIGGING) {
            pos = blockBreak.position;
            if (!pos.equals(this.lastCancelledBlock) && (!this.lastBlockWasInstantBreak || this.player.getClientVersion().isOlderThan(ClientVersion.V_1_14_4)) && !pos.equals(this.lastBlock)) {
               var10001 = MessageUtil.toUnlabledString(this.lastBlock);
               if (this.flagAndAlert("action=FINISHED_DIGGING, last=" + var10001 + ", pos=" + MessageUtil.toUnlabledString(pos)) && this.shouldModifyPackets()) {
                  blockBreak.cancel();
               }
            }

            if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_14_4)) {
               this.lastCancelledBlock = null;
               this.lastLastBlock = null;
               this.lastBlock = null;
            }
         }

      }
   }
}
