package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.AbstractIterator;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableSet;
import fr.xephi.authme.libs.com.google.common.collect.UnmodifiableIterator;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;

@DoNotMock("Call forGraph or forTree, passing a lambda or a Graph with the desired edges (built with GraphBuilder)")
@ElementTypesAreNonnullByDefault
@Beta
public abstract class Traverser<N> {
   private final SuccessorsFunction<N> successorFunction;

   private Traverser(SuccessorsFunction<N> successorFunction) {
      this.successorFunction = (SuccessorsFunction)Preconditions.checkNotNull(successorFunction);
   }

   public static <N> Traverser<N> forGraph(final SuccessorsFunction<N> graph) {
      return new Traverser<N>(graph) {
         Traverser.Traversal<N> newTraversal() {
            return Traverser.Traversal.inGraph(graph);
         }
      };
   }

   public static <N> Traverser<N> forTree(final SuccessorsFunction<N> tree) {
      if (tree instanceof BaseGraph) {
         Preconditions.checkArgument(((BaseGraph)tree).isDirected(), "Undirected graphs can never be trees.");
      }

      if (tree instanceof Network) {
         Preconditions.checkArgument(((Network)tree).isDirected(), "Undirected networks can never be trees.");
      }

      return new Traverser<N>(tree) {
         Traverser.Traversal<N> newTraversal() {
            return Traverser.Traversal.inTree(tree);
         }
      };
   }

   public final Iterable<N> breadthFirst(N startNode) {
      return this.breadthFirst((Iterable)ImmutableSet.of(startNode));
   }

   public final Iterable<N> breadthFirst(Iterable<? extends N> startNodes) {
      final ImmutableSet<N> validated = this.validate(startNodes);
      return new Iterable<N>() {
         public Iterator<N> iterator() {
            return Traverser.this.newTraversal().breadthFirst(validated.iterator());
         }
      };
   }

   public final Iterable<N> depthFirstPreOrder(N startNode) {
      return this.depthFirstPreOrder((Iterable)ImmutableSet.of(startNode));
   }

   public final Iterable<N> depthFirstPreOrder(Iterable<? extends N> startNodes) {
      final ImmutableSet<N> validated = this.validate(startNodes);
      return new Iterable<N>() {
         public Iterator<N> iterator() {
            return Traverser.this.newTraversal().preOrder(validated.iterator());
         }
      };
   }

   public final Iterable<N> depthFirstPostOrder(N startNode) {
      return this.depthFirstPostOrder((Iterable)ImmutableSet.of(startNode));
   }

   public final Iterable<N> depthFirstPostOrder(Iterable<? extends N> startNodes) {
      final ImmutableSet<N> validated = this.validate(startNodes);
      return new Iterable<N>() {
         public Iterator<N> iterator() {
            return Traverser.this.newTraversal().postOrder(validated.iterator());
         }
      };
   }

   abstract Traverser.Traversal<N> newTraversal();

   private ImmutableSet<N> validate(Iterable<? extends N> startNodes) {
      ImmutableSet<N> copy = ImmutableSet.copyOf(startNodes);
      UnmodifiableIterator var3 = copy.iterator();

      while(var3.hasNext()) {
         N node = var3.next();
         this.successorFunction.successors(node);
      }

      return copy;
   }

   // $FF: synthetic method
   Traverser(SuccessorsFunction x0, Object x1) {
      this(x0);
   }

   private static enum InsertionOrder {
      FRONT {
         <T> void insertInto(Deque<T> deque, T value) {
            deque.addFirst(value);
         }
      },
      BACK {
         <T> void insertInto(Deque<T> deque, T value) {
            deque.addLast(value);
         }
      };

      private InsertionOrder() {
      }

      abstract <T> void insertInto(Deque<T> var1, T var2);

      // $FF: synthetic method
      private static Traverser.InsertionOrder[] $values() {
         return new Traverser.InsertionOrder[]{FRONT, BACK};
      }

      // $FF: synthetic method
      InsertionOrder(Object x2) {
         this();
      }
   }

   private abstract static class Traversal<N> {
      final SuccessorsFunction<N> successorFunction;

      Traversal(SuccessorsFunction<N> successorFunction) {
         this.successorFunction = successorFunction;
      }

      static <N> Traverser.Traversal<N> inGraph(SuccessorsFunction<N> graph) {
         final Set<N> visited = new HashSet();
         return new Traverser.Traversal<N>(graph) {
            @CheckForNull
            N visitNext(Deque<Iterator<? extends N>> horizon) {
               Iterator top = (Iterator)horizon.getFirst();

               Object element;
               do {
                  if (!top.hasNext()) {
                     horizon.removeFirst();
                     return null;
                  }

                  element = top.next();
                  Objects.requireNonNull(element);
               } while(!visited.add(element));

               return element;
            }
         };
      }

      static <N> Traverser.Traversal<N> inTree(SuccessorsFunction<N> tree) {
         return new Traverser.Traversal<N>(tree) {
            @CheckForNull
            N visitNext(Deque<Iterator<? extends N>> horizon) {
               Iterator<? extends N> top = (Iterator)horizon.getFirst();
               if (top.hasNext()) {
                  return Preconditions.checkNotNull(top.next());
               } else {
                  horizon.removeFirst();
                  return null;
               }
            }
         };
      }

      final Iterator<N> breadthFirst(Iterator<? extends N> startNodes) {
         return this.topDown(startNodes, Traverser.InsertionOrder.BACK);
      }

      final Iterator<N> preOrder(Iterator<? extends N> startNodes) {
         return this.topDown(startNodes, Traverser.InsertionOrder.FRONT);
      }

      private Iterator<N> topDown(Iterator<? extends N> startNodes, final Traverser.InsertionOrder order) {
         final Deque<Iterator<? extends N>> horizon = new ArrayDeque();
         horizon.add(startNodes);
         return new AbstractIterator<N>() {
            @CheckForNull
            protected N computeNext() {
               do {
                  N next = Traversal.this.visitNext(horizon);
                  if (next != null) {
                     Iterator<? extends N> successors = Traversal.this.successorFunction.successors(next).iterator();
                     if (successors.hasNext()) {
                        order.insertInto(horizon, successors);
                     }

                     return next;
                  }
               } while(!horizon.isEmpty());

               return this.endOfData();
            }
         };
      }

      final Iterator<N> postOrder(Iterator<? extends N> startNodes) {
         final Deque<N> ancestorStack = new ArrayDeque();
         final Deque<Iterator<? extends N>> horizon = new ArrayDeque();
         horizon.add(startNodes);
         return new AbstractIterator<N>() {
            @CheckForNull
            protected N computeNext() {
               for(Object next = Traversal.this.visitNext(horizon); next != null; next = Traversal.this.visitNext(horizon)) {
                  Iterator<? extends N> successors = Traversal.this.successorFunction.successors(next).iterator();
                  if (!successors.hasNext()) {
                     return next;
                  }

                  horizon.addFirst(successors);
                  ancestorStack.push(next);
               }

               if (!ancestorStack.isEmpty()) {
                  return ancestorStack.pop();
               } else {
                  return this.endOfData();
               }
            }
         };
      }

      @CheckForNull
      abstract N visitNext(Deque<Iterator<? extends N>> var1);
   }
}
