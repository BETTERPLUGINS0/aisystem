package fr.xephi.authme.libs.org.apache.http.pool;

import fr.xephi.authme.libs.org.apache.http.concurrent.FutureCallback;
import java.util.concurrent.Future;

public interface ConnPool<T, E> {
   Future<E> lease(T var1, Object var2, FutureCallback<E> var3);

   void release(E var1, boolean var2);
}
