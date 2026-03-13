package ch.jalu.configme.resource.yaml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;

public class SnakeYamlNodeContainerImpl implements SnakeYamlNodeContainer {
   private final List<String> comments;
   private final Map<String, Object> values = new LinkedHashMap();

   public SnakeYamlNodeContainerImpl(@NotNull List<String> comments) {
      this.comments = comments;
   }

   @NotNull
   public SnakeYamlNodeContainer getOrCreateChildContainer(@NotNull String name, @NotNull Supplier<List<String>> commentsSupplier) {
      Object value = this.values.computeIfAbsent(name, (k) -> {
         return new SnakeYamlNodeContainerImpl((List)commentsSupplier.get());
      });
      if (!(value instanceof SnakeYamlNodeContainer)) {
         throw new IllegalStateException("Unexpectedly found " + value.getClass().getName() + " in '" + name + "'");
      } else {
         return (SnakeYamlNodeContainer)value;
      }
   }

   @NotNull
   public Node getRootValueNode() {
      Object rootValue = this.values.get("");
      if (rootValue == null) {
         throw new IllegalStateException("No value was stored for the root path ''");
      } else {
         return (Node)rootValue;
      }
   }

   public void putNode(@NotNull String name, @NotNull Node node) {
      if (this.values.containsKey(name)) {
         throw new IllegalStateException("Container unexpectedly already contains entry for '" + name + "'");
      } else {
         this.values.put(name, node);
      }
   }

   @NotNull
   public Node convertToNode(@NotNull SnakeYamlNodeBuilder nodeBuilder) {
      List<NodeTuple> entryNodes = new ArrayList(this.values.size());
      Iterator var3 = this.values.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, Object> entry = (Entry)var3.next();
         Node keyNode = nodeBuilder.createKeyNode((String)entry.getKey());
         Node valueNode = entry.getValue() instanceof SnakeYamlNodeContainer ? ((SnakeYamlNodeContainer)entry.getValue()).convertToNode(nodeBuilder) : (Node)entry.getValue();
         nodeBuilder.transferComments(valueNode, keyNode);
         entryNodes.add(new NodeTuple(keyNode, valueNode));
      }

      Node mappingNode = this.createRootNode(entryNodes);
      Stream var10000 = this.comments.stream();
      nodeBuilder.getClass();
      List<CommentLine> commentLines = (List)var10000.flatMap(nodeBuilder::createCommentLines).collect(Collectors.toList());
      mappingNode.setBlockComments(commentLines);
      return mappingNode;
   }

   protected Node createRootNode(List<NodeTuple> entryNodes) {
      return new MappingNode(Tag.MAP, entryNodes, FlowStyle.BLOCK);
   }

   protected final List<String> getComments() {
      return this.comments;
   }

   protected final Map<String, Object> getValues() {
      return this.values;
   }
}
