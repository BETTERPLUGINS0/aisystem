package ac.grim.grimac.manager.init.start;

public class ViaBackwardsManager implements StartableInitable {
   public void start() {
      System.setProperty("com.viaversion.handlePingsAsInvAcknowledgements", "true");
   }
}
