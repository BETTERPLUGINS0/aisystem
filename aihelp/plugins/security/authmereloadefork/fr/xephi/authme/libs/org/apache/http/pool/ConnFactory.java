package fr.xephi.authme.libs.org.apache.http.pool;

import java.io.IOException;

public interface ConnFactory<T, C> {
   C create(T var1) throws IOException;
}
