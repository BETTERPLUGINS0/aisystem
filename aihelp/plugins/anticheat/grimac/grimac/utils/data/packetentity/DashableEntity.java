package ac.grim.grimac.utils.data.packetentity;

public interface DashableEntity {
   boolean isDashing();

   void setDashing(boolean var1);

   int getDashCooldown();

   void setDashCooldown(int var1);
}
