package ac.grim.grimac.predictionengine.blockeffects;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;

@FunctionalInterface
public interface BlockStepVisitor {
   boolean visit(Vector3i var1, int var2);
}
