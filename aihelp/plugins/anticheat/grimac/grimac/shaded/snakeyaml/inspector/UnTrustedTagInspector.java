package ac.grim.grimac.shaded.snakeyaml.inspector;

import ac.grim.grimac.shaded.snakeyaml.nodes.Tag;

public final class UnTrustedTagInspector implements TagInspector {
   public boolean isGlobalTagAllowed(Tag tag) {
      return false;
   }
}
