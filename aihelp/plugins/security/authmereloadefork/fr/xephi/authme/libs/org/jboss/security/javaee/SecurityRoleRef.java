package fr.xephi.authme.libs.org.jboss.security.javaee;

public class SecurityRoleRef {
   private String name;
   private String link;
   private String description;

   public SecurityRoleRef() {
   }

   public SecurityRoleRef(String name, String link) {
      this.name = name;
      this.link = link;
   }

   public SecurityRoleRef(String name, String link, String description) {
      this.name = name;
      this.link = link;
      this.description = description;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String desc) {
      this.description = desc;
   }

   public String getLink() {
      return this.link;
   }

   public void setLink(String l) {
      this.link = l;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String n) {
      this.name = n;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder(super.toString());
      builder.append("[");
      builder.append(this.name).append(";").append(this.link);
      builder.append("]");
      return builder.toString();
   }
}
