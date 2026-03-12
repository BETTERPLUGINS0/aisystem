package fr.xephi.authme.libs.org.jboss.security.util.state;

public class Transition {
   private String name;
   private State target;

   public Transition(String name, State target) {
      this.name = name;
      this.target = target;
   }

   public String getName() {
      return this.name;
   }

   public State getTarget() {
      return this.target;
   }
}
