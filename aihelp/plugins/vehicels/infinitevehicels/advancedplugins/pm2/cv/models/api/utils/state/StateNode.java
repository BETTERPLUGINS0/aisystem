package advancedplugins.pm2.cv.models.api.utils.state;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class StateNode<T> {
   private final StateMachine<T> machine;
   private final Map<Predicate<T>, Function<T, StateNode<T>>> forceConnected = new LinkedHashMap();
   private final Map<Predicate<T>, Function<T, StateNode<T>>> connected = new LinkedHashMap();
   private Consumer<T> entryAction;
   private Consumer<T> action;
   private Consumer<T> exitAction;
   private Predicate<T> commonPredicate;

   public StateNode(StateMachine<T> var1) {
      this.machine = var1;
   }

   public void addForceConnectedNode(Predicate<T> var1, StateNode<T> var2) {
      this.forceConnected.put(var1, (var1x) -> {
         return var2;
      });
   }

   public void addForceConnectedNode(Predicate<T> var1, Function<T, StateNode<T>> var2) {
      this.forceConnected.put(var1, var2);
   }

   public void clearForceConnectedNodes() {
      this.forceConnected.clear();
   }

   public void addConnectedNode(Predicate<T> var1, StateNode<T> var2) {
      this.connected.put(var1, (var1x) -> {
         return var2;
      });
   }

   public void addConnectedNode(Predicate<T> var1, Function<T, StateNode<T>> var2) {
      this.connected.put(var1, var2);
   }

   public void clearConnectedNodes() {
      this.connected.clear();
   }

   public void acceptAction(T var1) {
      if (this.action != null) {
         this.action.accept(var1);
      }

   }

   public void acceptEntry(T var1) {
      if (this.entryAction != null) {
         this.entryAction.accept(var1);
      }

   }

   public void acceptExit(T var1) {
      if (this.exitAction != null) {
         this.exitAction.accept(var1);
      }

   }

   public boolean testCommonPredicate(T var1) {
      return this.commonPredicate == null || this.commonPredicate.test(var1);
   }

   public StateMachine<T> getMachine() {
      return this.machine;
   }

   public Map<Predicate<T>, Function<T, StateNode<T>>> getForceConnected() {
      return this.forceConnected;
   }

   public Map<Predicate<T>, Function<T, StateNode<T>>> getConnected() {
      return this.connected;
   }

   public Consumer<T> getEntryAction() {
      return this.entryAction;
   }

   public void setEntryAction(Consumer<T> var1) {
      this.entryAction = var1;
   }

   public Consumer<T> getAction() {
      return this.action;
   }

   public void setAction(Consumer<T> var1) {
      this.action = var1;
   }

   public Consumer<T> getExitAction() {
      return this.exitAction;
   }

   public void setExitAction(Consumer<T> var1) {
      this.exitAction = var1;
   }

   public Predicate<T> getCommonPredicate() {
      return this.commonPredicate;
   }

   public void setCommonPredicate(Predicate<T> var1) {
      this.commonPredicate = var1;
   }
}
