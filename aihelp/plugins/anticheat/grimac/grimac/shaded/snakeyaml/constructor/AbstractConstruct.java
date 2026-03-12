package ac.grim.grimac.shaded.snakeyaml.constructor;

import ac.grim.grimac.shaded.snakeyaml.error.YAMLException;
import ac.grim.grimac.shaded.snakeyaml.nodes.Node;

public abstract class AbstractConstruct implements Construct {
   public void construct2ndStep(Node node, Object data) {
      if (node.isTwoStepsConstruction()) {
         throw new IllegalStateException("Not Implemented in " + this.getClass().getName());
      } else {
         throw new YAMLException("Unexpected recursive structure for Node: " + node);
      }
   }
}
