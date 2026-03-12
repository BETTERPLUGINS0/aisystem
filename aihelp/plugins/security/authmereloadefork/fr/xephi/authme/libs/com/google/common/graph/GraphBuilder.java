package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.base.Optional;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;

@DoNotMock
@ElementTypesAreNonnullByDefault
@Beta
public final class GraphBuilder<N> extends AbstractGraphBuilder<N> {
   private GraphBuilder(boolean directed) {
      super(directed);
   }

   public static GraphBuilder<Object> directed() {
      return new GraphBuilder(true);
   }

   public static GraphBuilder<Object> undirected() {
      return new GraphBuilder(false);
   }

   public static <N> GraphBuilder<N> from(Graph<N> graph) {
      return (new GraphBuilder(graph.isDirected())).allowsSelfLoops(graph.allowsSelfLoops()).nodeOrder(graph.nodeOrder()).incidentEdgeOrder(graph.incidentEdgeOrder());
   }

   public <N1 extends N> ImmutableGraph.Builder<N1> immutable() {
      GraphBuilder<N1> castBuilder = this.cast();
      return new ImmutableGraph.Builder(castBuilder);
   }

   public GraphBuilder<N> allowsSelfLoops(boolean allowsSelfLoops) {
      this.allowsSelfLoops = allowsSelfLoops;
      return this;
   }

   public GraphBuilder<N> expectedNodeCount(int expectedNodeCount) {
      this.expectedNodeCount = Optional.of(Graphs.checkNonNegative(expectedNodeCount));
      return this;
   }

   public <N1 extends N> GraphBuilder<N1> nodeOrder(ElementOrder<N1> nodeOrder) {
      GraphBuilder<N1> newBuilder = this.cast();
      newBuilder.nodeOrder = (ElementOrder)Preconditions.checkNotNull(nodeOrder);
      return newBuilder;
   }

   public <N1 extends N> GraphBuilder<N1> incidentEdgeOrder(ElementOrder<N1> incidentEdgeOrder) {
      Preconditions.checkArgument(incidentEdgeOrder.type() == ElementOrder.Type.UNORDERED || incidentEdgeOrder.type() == ElementOrder.Type.STABLE, "The given elementOrder (%s) is unsupported. incidentEdgeOrder() only supports ElementOrder.unordered() and ElementOrder.stable().", (Object)incidentEdgeOrder);
      GraphBuilder<N1> newBuilder = this.cast();
      newBuilder.incidentEdgeOrder = (ElementOrder)Preconditions.checkNotNull(incidentEdgeOrder);
      return newBuilder;
   }

   public <N1 extends N> MutableGraph<N1> build() {
      return new StandardMutableGraph(this);
   }

   GraphBuilder<N> copy() {
      GraphBuilder<N> newBuilder = new GraphBuilder(this.directed);
      newBuilder.allowsSelfLoops = this.allowsSelfLoops;
      newBuilder.nodeOrder = this.nodeOrder;
      newBuilder.expectedNodeCount = this.expectedNodeCount;
      newBuilder.incidentEdgeOrder = this.incidentEdgeOrder;
      return newBuilder;
   }

   private <N1 extends N> GraphBuilder<N1> cast() {
      return this;
   }
}
