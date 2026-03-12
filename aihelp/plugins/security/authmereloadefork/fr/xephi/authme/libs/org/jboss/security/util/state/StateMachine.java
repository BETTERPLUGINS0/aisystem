package fr.xephi.authme.libs.org.jboss.security.util.state;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StateMachine implements Cloneable {
   private String description;
   private HashSet states;
   private State startState;
   private State currentState;

   public StateMachine(Set states, State startState) {
      this(states, startState, (String)null);
   }

   public StateMachine(Set states, State startState, String description) {
      this.states = new HashSet(states);
      this.startState = startState;
      this.currentState = startState;
      this.description = description;
   }

   public Object clone() {
      StateMachine clone = new StateMachine(this.states, this.startState, this.description);
      clone.currentState = this.currentState;
      return clone;
   }

   public String getDescription() {
      return this.description;
   }

   public State getCurrentState() {
      return this.currentState;
   }

   public State getStartState() {
      return this.startState;
   }

   public Set getStates() {
      return this.states;
   }

   public State nextState(String actionName) throws IllegalTransitionException {
      Transition t = this.currentState.getTransition(actionName);
      if (t == null) {
         throw new IllegalTransitionException(PicketBoxMessages.MESSAGES.invalidTransitionForActionMessage(actionName, this.currentState != null ? this.currentState.getName() : null));
      } else {
         State nextState = t.getTarget();
         PicketBoxLogger.LOGGER.traceStateMachineNextState(actionName, nextState != null ? nextState.getName() : null);
         this.currentState = nextState;
         return this.currentState;
      }
   }

   public State reset() {
      this.currentState = this.startState;
      return this.currentState;
   }

   public String toString() {
      StringBuffer tmp = new StringBuffer("StateMachine[:\n");
      tmp.append("\tCurrentState: " + this.currentState.getName());
      Iterator i = this.states.iterator();

      while(i.hasNext()) {
         tmp.append('\n').append(i.next());
      }

      tmp.append(']');
      return tmp.toString();
   }
}
