package ac.grim.grimac.checks.type;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.collisions.HitboxData;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import java.util.ArrayList;
import java.util.List;

public class BlockPlaceCheck extends Check implements RotationCheck, BlockBreakCheck {
   private static final List<StateType> weirdBoxes = new ArrayList();
   private static final List<StateType> buggyBoxes = new ArrayList();
   private final SimpleCollisionBox[] boxes = new SimpleCollisionBox[15];
   protected int cancelVL;

   public BlockPlaceCheck(GrimPlayer player) {
      super(player);
   }

   public void onBlockPlace(BlockPlace place) {
   }

   public void onPostFlyingBlockPlace(BlockPlace place) {
   }

   public void onReload(ConfigManager config) {
      this.cancelVL = config.getIntElse(this.getConfigName() + ".cancelVL", 5);
   }

   protected boolean shouldCancel() {
      return this.cancelVL >= 0 && this.violations >= (double)this.cancelVL;
   }

   protected SimpleCollisionBox getCombinedBox(BlockPlace place) {
      Vector3i clicked = place.position;
      if (!weirdBoxes.contains(place.getPlacedAgainstMaterial()) && !buggyBoxes.contains(place.getPlacedAgainstMaterial())) {
         int size = HitboxData.getBlockHitbox(this.player, place.material, this.player.getClientVersion(), this.player.compensatedWorld.getBlock(clicked), true, clicked.getX(), clicked.getY(), clicked.getZ()).downCast(this.boxes);
         SimpleCollisionBox combined = new SimpleCollisionBox((double)clicked.getX(), (double)clicked.getY(), (double)clicked.getZ());

         for(int i = 0; i < size; ++i) {
            SimpleCollisionBox box = this.boxes[i];
            combined = new SimpleCollisionBox(Math.max(box.minX, combined.minX), Math.max(box.minY, combined.minY), Math.max(box.minZ, combined.minZ), Math.min(box.maxX, combined.maxX), Math.min(box.maxY, combined.maxY), Math.min(box.maxZ, combined.maxZ));
         }

         return combined;
      } else {
         return new SimpleCollisionBox((double)(clicked.getX() + 1), (double)(clicked.getY() + 1), (double)(clicked.getZ() + 1), (double)clicked.getX(), (double)clicked.getY(), (double)clicked.getZ());
      }
   }

   static {
      weirdBoxes.addAll(new ArrayList(BlockTags.FENCES.getStates()));
      weirdBoxes.addAll(new ArrayList(BlockTags.WALLS.getStates()));
      weirdBoxes.add(StateTypes.LECTERN);
      buggyBoxes.addAll(new ArrayList(BlockTags.DOORS.getStates()));
      buggyBoxes.addAll(new ArrayList(BlockTags.STAIRS.getStates()));
      buggyBoxes.add(StateTypes.CHEST);
      buggyBoxes.add(StateTypes.TRAPPED_CHEST);
      buggyBoxes.add(StateTypes.CHORUS_PLANT);
      buggyBoxes.add(StateTypes.KELP);
      buggyBoxes.add(StateTypes.KELP_PLANT);
      buggyBoxes.add(StateTypes.TWISTING_VINES);
      buggyBoxes.add(StateTypes.TWISTING_VINES_PLANT);
      buggyBoxes.add(StateTypes.WEEPING_VINES);
      buggyBoxes.add(StateTypes.WEEPING_VINES_PLANT);
      buggyBoxes.add(StateTypes.REDSTONE_WIRE);
   }
}
