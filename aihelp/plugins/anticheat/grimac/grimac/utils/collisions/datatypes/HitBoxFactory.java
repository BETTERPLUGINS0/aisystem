package ac.grim.grimac.utils.collisions.datatypes;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;

public interface HitBoxFactory {
   CollisionBox fetch(GrimPlayer var1, StateType var2, ClientVersion var3, WrappedBlockState var4, boolean var5, int var6, int var7, int var8);
}
