package fr.xephi.authme.libs.com.mysql.cj.xdevapi;

import fr.xephi.authme.libs.com.mysql.cj.QueryResult;
import java.util.Iterator;

public interface Result extends QueryResult {
   long getAffectedItemsCount();

   int getWarningsCount();

   Iterator<Warning> getWarnings();
}
