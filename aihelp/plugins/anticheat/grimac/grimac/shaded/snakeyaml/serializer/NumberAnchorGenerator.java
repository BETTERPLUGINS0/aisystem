package ac.grim.grimac.shaded.snakeyaml.serializer;

import ac.grim.grimac.shaded.snakeyaml.nodes.Node;
import java.text.NumberFormat;

public class NumberAnchorGenerator implements AnchorGenerator {
   private int lastAnchorId = 0;

   public NumberAnchorGenerator(int lastAnchorId) {
      this.lastAnchorId = lastAnchorId;
   }

   public String nextAnchor(Node node) {
      ++this.lastAnchorId;
      NumberFormat format = NumberFormat.getNumberInstance();
      format.setMinimumIntegerDigits(3);
      format.setMaximumFractionDigits(0);
      format.setGroupingUsed(false);
      String anchorId = format.format((long)this.lastAnchorId);
      return "id" + anchorId;
   }
}
