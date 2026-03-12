package fr.xephi.authme.libs.org.postgresql.core.v3;

import org.checkerframework.checker.nullness.qual.Nullable;

class ExecuteRequest {
   public final SimpleQuery query;
   @Nullable
   public final Portal portal;
   public final boolean asSimple;

   ExecuteRequest(SimpleQuery query, @Nullable Portal portal, boolean asSimple) {
      this.query = query;
      this.portal = portal;
      this.asSimple = asSimple;
   }
}
