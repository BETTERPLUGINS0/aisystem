package fr.xephi.authme.libs.org.jboss.security.util.state;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class State {
   private String name;
   private HashMap allowedTransitions;
   private Object data;

   public State(String name) {
      this(name, (Map)null);
   }

   public State(String name, Map transitions) {
      this.allowedTransitions = new HashMap();
      this.name = name;
      if (transitions != null) {
         this.allowedTransitions.putAll(transitions);
      }

   }

   public String getName() {
      return this.name;
   }

   public Object getData() {
      return this.data;
   }

   public void setData(Object data) {
      this.data = data;
   }

   public boolean isAcceptState() {
      return this.allowedTransitions.size() == 0;
   }

   public State addTransition(Transition transition) {
      this.allowedTransitions.put(transition.getName(), transition);
      return this;
   }

   public Transition getTransition(String name) {
      Transition t = (Transition)this.allowedTransitions.get(name);
      return t;
   }

   public Map getTransitions() {
      return this.allowedTransitions;
   }

   public String toString() {
      StringBuffer tmp = new StringBuffer("State(name=");
      tmp.append(this.name);
      Iterator i = this.allowedTransitions.entrySet().iterator();

      while(i.hasNext()) {
         Entry e = (Entry)i.next();
         tmp.append("\n\t on: ");
         tmp.append(e.getKey());
         Transition t = (Transition)e.getValue();
         tmp.append(" go to: ");
         tmp.append(t.getTarget().getName());
      }

      tmp.append(')');
      return tmp.toString();
   }
}
