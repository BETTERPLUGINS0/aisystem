package fr.xephi.authme.libs.org.apache.http.client.methods;

import fr.xephi.authme.libs.org.apache.http.concurrent.Cancellable;

public interface HttpExecutionAware {
   boolean isAborted();

   void setCancellable(Cancellable var1);
}
