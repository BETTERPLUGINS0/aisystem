package advancedplugins.pm2.cv.models.api.utils.state;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

public class StateMachine<T> {
   protected StateNode<T> currentNode;

   public void setEntryNode(StateNode<T> var1) {
      this.currentNode = var1;
   }

   public StateNode<T> createNode() {
      return new StateNode(this);
   }

   public void execute(T var1) {
      boolean var2 = false;
      Iterator var3 = this.currentNode.getForceConnected().entrySet().iterator();

      Entry var4;
      while(var3.hasNext()) {
         var4 = (Entry)var3.next();
         if (((Predicate)var4.getKey()).test(var1)) {
            this.currentNode.acceptExit(var1);
            this.currentNode = (StateNode)((Function)var4.getValue()).apply(var1);
            if (this.currentNode != null) {
               this.currentNode.acceptEntry(var1);
            }

            var2 = true;
            break;
         }
      }

      if (!var2 && this.currentNode.testCommonPredicate(var1)) {
         var3 = this.currentNode.getConnected().entrySet().iterator();

         while(var3.hasNext()) {
            var4 = (Entry)var3.next();
            if (((Predicate)var4.getKey()).test(var1)) {
               this.currentNode.acceptExit(var1);
               this.currentNode = (StateNode)((Function)var4.getValue()).apply(var1);
               if (this.currentNode != null) {
                  this.currentNode.acceptEntry(var1);
               }
               break;
            }
         }
      }

      if (this.currentNode != null) {
         this.currentNode.acceptAction(var1);
      }

   }

   public StateNode<T> getCurrentNode() {
      return this.currentNode;
   }
}
