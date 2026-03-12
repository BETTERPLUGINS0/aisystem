package emanondev.itemtag.activity.arguments;

public class TargetArgument {
   private String value;

   public TargetArgument(String info) {
      this.value = info;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String toString() {
      return this.value;
   }
}
