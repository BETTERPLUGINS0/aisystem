package fr.xephi.authme.libs.org.jboss.security.mapping;

public enum MappingType {
   CREDENTIAL("credential"),
   PRINCIPAL("principal"),
   ROLE("role"),
   ATTRIBUTE("attribute");

   private String name;

   private MappingType(String name) {
      this.name = name;
   }

   public String toString() {
      return this.name;
   }
}
