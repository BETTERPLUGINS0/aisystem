package fr.xephi.authme.libs.org.jboss.security.config;

public class ModuleOption {
   String name;
   Object value = "";

   public ModuleOption(String name) {
      this.name = name;
   }

   public ModuleOption(String name, Object value) {
      this.name = name;
      this.value = value;
   }

   public String getName() {
      return this.name;
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object value) {
      if (value != null) {
         this.value = value;
      }

   }
}
