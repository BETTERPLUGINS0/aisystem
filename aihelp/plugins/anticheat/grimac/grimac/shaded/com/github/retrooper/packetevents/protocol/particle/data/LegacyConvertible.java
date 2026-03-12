package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;

public interface LegacyConvertible {
   LegacyParticleData toLegacy(ClientVersion version);
}
