package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.PGNotification;

public class Notification implements PGNotification {
   private final String name;
   private final String parameter;
   private final int pid;

   public Notification(String name, int pid) {
      this(name, pid, "");
   }

   public Notification(String name, int pid, String parameter) {
      this.name = name;
      this.pid = pid;
      this.parameter = parameter;
   }

   public String getName() {
      return this.name;
   }

   public int getPID() {
      return this.pid;
   }

   public String getParameter() {
      return this.parameter;
   }
}
