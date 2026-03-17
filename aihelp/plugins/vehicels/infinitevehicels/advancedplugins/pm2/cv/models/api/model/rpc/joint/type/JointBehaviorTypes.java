package advancedplugins.pm2.cv.models.api.model.rpc.joint.type;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;

public final class JointBehaviorTypes {
   public static JointActionType<? extends Head> HEAD;
   public static JointActionType<? extends Ghost> GHOST;
   public static JointActionType<? extends Mount> MOUNT;
   public static JointActionType<? extends SubHitbox> SUB_HITBOX;
   public static JointActionType<? extends NameTag> NAMETAG;
   public static JointActionType<? extends HeldItem> ITEM;
   public static JointActionType<? extends Segment> SEGMENT;
   public static JointActionType<? extends Segment> TAIL;
   public static JointActionType<? extends Leash> LEASH;
   public static JointActionType<? extends PlayerLimb> PLAYER_LIMB;
}
