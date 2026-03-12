package fr.xephi.authme.libs.org.apache.http.conn;

import fr.xephi.authme.libs.org.apache.http.HttpClientConnection;
import fr.xephi.authme.libs.org.apache.http.concurrent.Cancellable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public interface ConnectionRequest extends Cancellable {
   HttpClientConnection get(long var1, TimeUnit var3) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException;
}
