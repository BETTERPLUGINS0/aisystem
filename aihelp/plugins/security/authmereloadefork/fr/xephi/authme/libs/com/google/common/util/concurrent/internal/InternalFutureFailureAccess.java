package fr.xephi.authme.libs.com.google.common.util.concurrent.internal;

public abstract class InternalFutureFailureAccess {
   protected InternalFutureFailureAccess() {
   }

   protected abstract Throwable tryInternalFastPathGetFailure();
}
