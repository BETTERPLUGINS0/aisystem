package ac.grim.grimac.manager.violationdatabase;

import ac.grim.grimac.player.GrimPlayer;
import java.util.List;
import java.util.UUID;

public final class NoOpViolationDatabase implements ViolationDatabase {
   public static final NoOpViolationDatabase INSTANCE = new NoOpViolationDatabase();

   private NoOpViolationDatabase() {
   }

   public void connect() {
   }

   public void disconnect() {
   }

   public void logAlert(GrimPlayer p, String grimVersion, String v, String c, int vl) {
   }

   public int getLogCount(UUID player) {
      return 0;
   }

   public List<Violation> getViolations(UUID p, int page, int lim) {
      return List.of();
   }
}
