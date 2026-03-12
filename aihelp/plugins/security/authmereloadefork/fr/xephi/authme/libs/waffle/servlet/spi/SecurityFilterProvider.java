package fr.xephi.authme.libs.waffle.servlet.spi;

import fr.xephi.authme.libs.waffle.windows.auth.IWindowsIdentity;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SecurityFilterProvider {
   void sendUnauthorized(HttpServletResponse var1);

   boolean isPrincipalException(HttpServletRequest var1);

   IWindowsIdentity doFilter(HttpServletRequest var1, HttpServletResponse var2) throws IOException;

   boolean isSecurityPackageSupported(String var1);

   void initParameter(String var1, String var2);
}
