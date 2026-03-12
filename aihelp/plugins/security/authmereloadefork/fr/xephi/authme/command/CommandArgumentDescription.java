package fr.xephi.authme.command;

public class CommandArgumentDescription {
   private final String name;
   private final String description;
   private final boolean isOptional;

   public CommandArgumentDescription(String name, String description, boolean isOptional) {
      this.name = name;
      this.description = description;
      this.isOptional = isOptional;
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }

   public boolean isOptional() {
      return this.isOptional;
   }
}
