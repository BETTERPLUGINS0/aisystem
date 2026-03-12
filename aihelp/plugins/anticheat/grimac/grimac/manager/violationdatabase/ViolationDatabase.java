package ac.grim.grimac.manager.violationdatabase;

import ac.grim.grimac.player.GrimPlayer;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface ViolationDatabase {
   void connect() throws SQLException;

   void logAlert(GrimPlayer var1, String var2, String var3, String var4, int var5);

   int getLogCount(UUID var1);

   List<Violation> getViolations(UUID var1, int var2, int var3);

   void disconnect();
}
