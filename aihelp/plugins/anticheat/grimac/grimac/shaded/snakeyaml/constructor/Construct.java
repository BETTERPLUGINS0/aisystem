package ac.grim.grimac.shaded.snakeyaml.constructor;

import ac.grim.grimac.shaded.snakeyaml.nodes.Node;

public interface Construct {
   Object construct(Node var1);

   void construct2ndStep(Node var1, Object var2);
}
