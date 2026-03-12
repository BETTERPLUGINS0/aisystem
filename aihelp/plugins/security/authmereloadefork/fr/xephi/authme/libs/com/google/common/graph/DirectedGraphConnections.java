package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.AbstractIterator;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableList;
import fr.xephi.authme.libs.com.google.common.collect.Iterators;
import fr.xephi.authme.libs.com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
final class DirectedGraphConnections<N, V> implements GraphConnections<N, V> {
   private static final Object PRED = new Object();
   private final Map<N, Object> adjacentNodeValues;
   @CheckForNull
   private final List<DirectedGraphConnections.NodeConnection<N>> orderedNodeConnections;
   private int predecessorCount;
   private int successorCount;

   private DirectedGraphConnections(Map<N, Object> adjacentNodeValues, @CheckForNull List<DirectedGraphConnections.NodeConnection<N>> orderedNodeConnections, int predecessorCount, int successorCount) {
      this.adjacentNodeValues = (Map)Preconditions.checkNotNull(adjacentNodeValues);
      this.orderedNodeConnections = orderedNodeConnections;
      this.predecessorCount = Graphs.checkNonNegative(predecessorCount);
      this.successorCount = Graphs.checkNonNegative(successorCount);
      Preconditions.checkState(predecessorCount <= adjacentNodeValues.size() && successorCount <= adjacentNodeValues.size());
   }

   static <N, V> DirectedGraphConnections<N, V> of(ElementOrder<N> incidentEdgeOrder) {
      int initialCapacity = 4;
      ArrayList orderedNodeConnections;
      switch(incidentEdgeOrder.type()) {
      case UNORDERED:
         orderedNodeConnections = null;
         break;
      case STABLE:
         orderedNodeConnections = new ArrayList();
         break;
      default:
         throw new AssertionError(incidentEdgeOrder.type());
      }

      return new DirectedGraphConnections(new HashMap(initialCapacity, 1.0F), orderedNodeConnections, 0, 0);
   }

   static <N, V> DirectedGraphConnections<N, V> ofImmutable(N thisNode, Iterable<EndpointPair<N>> incidentEdges, Function<N, V> successorNodeToValueFn) {
      Preconditions.checkNotNull(thisNode);
      Preconditions.checkNotNull(successorNodeToValueFn);
      Map<N, Object> adjacentNodeValues = new HashMap();
      ImmutableList.Builder<DirectedGraphConnections.NodeConnection<N>> orderedNodeConnectionsBuilder = ImmutableList.builder();
      int predecessorCount = 0;
      int successorCount = 0;
      Iterator var7 = incidentEdges.iterator();

      while(true) {
         while(var7.hasNext()) {
            EndpointPair<N> incidentEdge = (EndpointPair)var7.next();
            if (incidentEdge.nodeU().equals(thisNode) && incidentEdge.nodeV().equals(thisNode)) {
               adjacentNodeValues.put(thisNode, new DirectedGraphConnections.PredAndSucc(successorNodeToValueFn.apply(thisNode)));
               orderedNodeConnectionsBuilder.add((Object)(new DirectedGraphConnections.NodeConnection.Pred(thisNode)));
               orderedNodeConnectionsBuilder.add((Object)(new DirectedGraphConnections.NodeConnection.Succ(thisNode)));
               ++predecessorCount;
               ++successorCount;
            } else {
               Object predecessor;
               Object existingValue;
               if (incidentEdge.nodeV().equals(thisNode)) {
                  predecessor = incidentEdge.nodeU();
                  existingValue = adjacentNodeValues.put(predecessor, PRED);
                  if (existingValue != null) {
                     adjacentNodeValues.put(predecessor, new DirectedGraphConnections.PredAndSucc(existingValue));
                  }

                  orderedNodeConnectionsBuilder.add((Object)(new DirectedGraphConnections.NodeConnection.Pred(predecessor)));
                  ++predecessorCount;
               } else {
                  Preconditions.checkArgument(incidentEdge.nodeU().equals(thisNode));
                  predecessor = incidentEdge.nodeV();
                  existingValue = successorNodeToValueFn.apply(predecessor);
                  Object existingValue = adjacentNodeValues.put(predecessor, existingValue);
                  if (existingValue != null) {
                     Preconditions.checkArgument(existingValue == PRED);
                     adjacentNodeValues.put(predecessor, new DirectedGraphConnections.PredAndSucc(existingValue));
                  }

                  orderedNodeConnectionsBuilder.add((Object)(new DirectedGraphConnections.NodeConnection.Succ(predecessor)));
                  ++successorCount;
               }
            }
         }

         return new DirectedGraphConnections(adjacentNodeValues, orderedNodeConnectionsBuilder.build(), predecessorCount, successorCount);
      }
   }

   public Set<N> adjacentNodes() {
      return (Set)(this.orderedNodeConnections == null ? Collections.unmodifiableSet(this.adjacentNodeValues.keySet()) : new AbstractSet<N>() {
         public UnmodifiableIterator<N> iterator() {
            final Iterator<DirectedGraphConnections.NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
            final Set<N> seenNodes = new HashSet();
            return new AbstractIterator<N>(this) {
               @CheckForNull
               protected N computeNext() {
                  while(true) {
                     if (nodeConnections.hasNext()) {
                        DirectedGraphConnections.NodeConnection<N> nodeConnection = (DirectedGraphConnections.NodeConnection)nodeConnections.next();
                        boolean added = seenNodes.add(nodeConnection.node);
                        if (!added) {
                           continue;
                        }

                        return nodeConnection.node;
                     }

                     return this.endOfData();
                  }
               }
            };
         }

         public int size() {
            return DirectedGraphConnections.this.adjacentNodeValues.size();
         }

         public boolean contains(@CheckForNull Object obj) {
            return DirectedGraphConnections.this.adjacentNodeValues.containsKey(obj);
         }
      });
   }

   public Set<N> predecessors() {
      return new AbstractSet<N>() {
         public UnmodifiableIterator<N> iterator() {
            final Iterator nodeConnections;
            if (DirectedGraphConnections.this.orderedNodeConnections == null) {
               nodeConnections = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
               return new AbstractIterator<N>(this) {
                  @CheckForNull
                  protected N computeNext() {
                     while(true) {
                        if (nodeConnections.hasNext()) {
                           Entry<N, Object> entry = (Entry)nodeConnections.next();
                           if (!DirectedGraphConnections.isPredecessor(entry.getValue())) {
                              continue;
                           }

                           return entry.getKey();
                        }

                        return this.endOfData();
                     }
                  }
               };
            } else {
               nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
               return new AbstractIterator<N>(this) {
                  @CheckForNull
                  protected N computeNext() {
                     while(true) {
                        if (nodeConnections.hasNext()) {
                           DirectedGraphConnections.NodeConnection<N> nodeConnection = (DirectedGraphConnections.NodeConnection)nodeConnections.next();
                           if (!(nodeConnection instanceof DirectedGraphConnections.NodeConnection.Pred)) {
                              continue;
                           }

                           return nodeConnection.node;
                        }

                        return this.endOfData();
                     }
                  }
               };
            }
         }

         public int size() {
            return DirectedGraphConnections.this.predecessorCount;
         }

         public boolean contains(@CheckForNull Object obj) {
            return DirectedGraphConnections.isPredecessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
         }
      };
   }

   public Set<N> successors() {
      return new AbstractSet<N>() {
         public UnmodifiableIterator<N> iterator() {
            final Iterator nodeConnections;
            if (DirectedGraphConnections.this.orderedNodeConnections == null) {
               nodeConnections = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
               return new AbstractIterator<N>(this) {
                  @CheckForNull
                  protected N computeNext() {
                     while(true) {
                        if (nodeConnections.hasNext()) {
                           Entry<N, Object> entry = (Entry)nodeConnections.next();
                           if (!DirectedGraphConnections.isSuccessor(entry.getValue())) {
                              continue;
                           }

                           return entry.getKey();
                        }

                        return this.endOfData();
                     }
                  }
               };
            } else {
               nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
               return new AbstractIterator<N>(this) {
                  @CheckForNull
                  protected N computeNext() {
                     while(true) {
                        if (nodeConnections.hasNext()) {
                           DirectedGraphConnections.NodeConnection<N> nodeConnection = (DirectedGraphConnections.NodeConnection)nodeConnections.next();
                           if (!(nodeConnection instanceof DirectedGraphConnections.NodeConnection.Succ)) {
                              continue;
                           }

                           return nodeConnection.node;
                        }

                        return this.endOfData();
                     }
                  }
               };
            }
         }

         public int size() {
            return DirectedGraphConnections.this.successorCount;
         }

         public boolean contains(@CheckForNull Object obj) {
            return DirectedGraphConnections.isSuccessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
         }
      };
   }

   public Iterator<EndpointPair<N>> incidentEdgeIterator(N thisNode) {
      Preconditions.checkNotNull(thisNode);
      final Iterator resultWithDoubleSelfLoop;
      if (this.orderedNodeConnections == null) {
         resultWithDoubleSelfLoop = Iterators.concat(Iterators.transform(this.predecessors().iterator(), (predecessor) -> {
            return EndpointPair.ordered(predecessor, thisNode);
         }), Iterators.transform(this.successors().iterator(), (successor) -> {
            return EndpointPair.ordered(thisNode, successor);
         }));
      } else {
         resultWithDoubleSelfLoop = Iterators.transform(this.orderedNodeConnections.iterator(), (connection) -> {
            return connection instanceof DirectedGraphConnections.NodeConnection.Succ ? EndpointPair.ordered(thisNode, connection.node) : EndpointPair.ordered(connection.node, thisNode);
         });
      }

      final AtomicBoolean alreadySeenSelfLoop = new AtomicBoolean(false);
      return new AbstractIterator<EndpointPair<N>>(this) {
         @CheckForNull
         protected EndpointPair<N> computeNext() {
            while(true) {
               if (resultWithDoubleSelfLoop.hasNext()) {
                  EndpointPair<N> edge = (EndpointPair)resultWithDoubleSelfLoop.next();
                  if (edge.nodeU().equals(edge.nodeV())) {
                     if (alreadySeenSelfLoop.getAndSet(true)) {
                        continue;
                     }

                     return edge;
                  }

                  return edge;
               }

               return (EndpointPair)this.endOfData();
            }
         }
      };
   }

   @CheckForNull
   public V value(N node) {
      Preconditions.checkNotNull(node);
      Object value = this.adjacentNodeValues.get(node);
      if (value == PRED) {
         return null;
      } else {
         return value instanceof DirectedGraphConnections.PredAndSucc ? ((DirectedGraphConnections.PredAndSucc)value).successorValue : value;
      }
   }

   public void removePredecessor(N node) {
      Preconditions.checkNotNull(node);
      Object previousValue = this.adjacentNodeValues.get(node);
      boolean removedPredecessor;
      if (previousValue == PRED) {
         this.adjacentNodeValues.remove(node);
         removedPredecessor = true;
      } else if (previousValue instanceof DirectedGraphConnections.PredAndSucc) {
         this.adjacentNodeValues.put(node, ((DirectedGraphConnections.PredAndSucc)previousValue).successorValue);
         removedPredecessor = true;
      } else {
         removedPredecessor = false;
      }

      if (removedPredecessor) {
         Graphs.checkNonNegative(--this.predecessorCount);
         if (this.orderedNodeConnections != null) {
            this.orderedNodeConnections.remove(new DirectedGraphConnections.NodeConnection.Pred(node));
         }
      }

   }

   @CheckForNull
   public V removeSuccessor(Object node) {
      Preconditions.checkNotNull(node);
      Object previousValue = this.adjacentNodeValues.get(node);
      Object removedValue;
      if (previousValue != null && previousValue != PRED) {
         if (previousValue instanceof DirectedGraphConnections.PredAndSucc) {
            this.adjacentNodeValues.put(node, PRED);
            removedValue = ((DirectedGraphConnections.PredAndSucc)previousValue).successorValue;
         } else {
            this.adjacentNodeValues.remove(node);
            removedValue = previousValue;
         }
      } else {
         removedValue = null;
      }

      if (removedValue != null) {
         Graphs.checkNonNegative(--this.successorCount);
         if (this.orderedNodeConnections != null) {
            this.orderedNodeConnections.remove(new DirectedGraphConnections.NodeConnection.Succ(node));
         }
      }

      return removedValue == null ? null : removedValue;
   }

   public void addPredecessor(N node, V unused) {
      Object previousValue = this.adjacentNodeValues.put(node, PRED);
      boolean addedPredecessor;
      if (previousValue == null) {
         addedPredecessor = true;
      } else if (previousValue instanceof DirectedGraphConnections.PredAndSucc) {
         this.adjacentNodeValues.put(node, previousValue);
         addedPredecessor = false;
      } else if (previousValue != PRED) {
         this.adjacentNodeValues.put(node, new DirectedGraphConnections.PredAndSucc(previousValue));
         addedPredecessor = true;
      } else {
         addedPredecessor = false;
      }

      if (addedPredecessor) {
         Graphs.checkPositive(++this.predecessorCount);
         if (this.orderedNodeConnections != null) {
            this.orderedNodeConnections.add(new DirectedGraphConnections.NodeConnection.Pred(node));
         }
      }

   }

   @CheckForNull
   public V addSuccessor(N node, V value) {
      Object previousValue = this.adjacentNodeValues.put(node, value);
      Object previousSuccessor;
      if (previousValue == null) {
         previousSuccessor = null;
      } else if (previousValue instanceof DirectedGraphConnections.PredAndSucc) {
         this.adjacentNodeValues.put(node, new DirectedGraphConnections.PredAndSucc(value));
         previousSuccessor = ((DirectedGraphConnections.PredAndSucc)previousValue).successorValue;
      } else if (previousValue == PRED) {
         this.adjacentNodeValues.put(node, new DirectedGraphConnections.PredAndSucc(value));
         previousSuccessor = null;
      } else {
         previousSuccessor = previousValue;
      }

      if (previousSuccessor == null) {
         Graphs.checkPositive(++this.successorCount);
         if (this.orderedNodeConnections != null) {
            this.orderedNodeConnections.add(new DirectedGraphConnections.NodeConnection.Succ(node));
         }
      }

      return previousSuccessor == null ? null : previousSuccessor;
   }

   private static boolean isPredecessor(@CheckForNull Object value) {
      return value == PRED || value instanceof DirectedGraphConnections.PredAndSucc;
   }

   private static boolean isSuccessor(@CheckForNull Object value) {
      return value != PRED && value != null;
   }

   private abstract static class NodeConnection<N> {
      final N node;

      NodeConnection(N node) {
         this.node = Preconditions.checkNotNull(node);
      }

      static final class Succ<N> extends DirectedGraphConnections.NodeConnection<N> {
         Succ(N node) {
            super(node);
         }

         public boolean equals(@CheckForNull Object that) {
            return that instanceof DirectedGraphConnections.NodeConnection.Succ ? this.node.equals(((DirectedGraphConnections.NodeConnection.Succ)that).node) : false;
         }

         public int hashCode() {
            return DirectedGraphConnections.NodeConnection.Succ.class.hashCode() + this.node.hashCode();
         }
      }

      static final class Pred<N> extends DirectedGraphConnections.NodeConnection<N> {
         Pred(N node) {
            super(node);
         }

         public boolean equals(@CheckForNull Object that) {
            return that instanceof DirectedGraphConnections.NodeConnection.Pred ? this.node.equals(((DirectedGraphConnections.NodeConnection.Pred)that).node) : false;
         }

         public int hashCode() {
            return DirectedGraphConnections.NodeConnection.Pred.class.hashCode() + this.node.hashCode();
         }
      }
   }

   private static final class PredAndSucc {
      private final Object successorValue;

      PredAndSucc(Object successorValue) {
         this.successorValue = successorValue;
      }
   }
}
