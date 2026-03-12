package fr.xephi.authme.libs.waffle.servlet;

import fr.xephi.authme.libs.waffle.windows.auth.IWindowsIdentity;
import fr.xephi.authme.libs.waffle.windows.auth.PrincipalFormat;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class AutoDisposableWindowsPrincipal extends WindowsPrincipal implements HttpSessionBindingListener {
   private static final long serialVersionUID = 1L;

   public AutoDisposableWindowsPrincipal(IWindowsIdentity windowsIdentity) {
      super(windowsIdentity);
   }

   public AutoDisposableWindowsPrincipal(IWindowsIdentity windowsIdentity, PrincipalFormat principalFormat, PrincipalFormat roleFormat) {
      super(windowsIdentity, principalFormat, roleFormat);
   }

   public void valueBound(HttpSessionBindingEvent evt) {
   }

   public void valueUnbound(HttpSessionBindingEvent evt) {
      if (this.getIdentity() != null) {
         this.getIdentity().dispose();
      }

   }
}
