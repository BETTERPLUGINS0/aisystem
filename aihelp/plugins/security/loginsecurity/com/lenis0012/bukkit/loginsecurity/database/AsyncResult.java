package com.lenis0012.bukkit.loginsecurity.database;

public final class AsyncResult<T> {
   private final boolean success;
   private final T result;
   private final Exception error;

   public AsyncResult(boolean success, T result, Exception error) {
      this.success = success;
      this.result = result;
      this.error = error;
   }

   public boolean isSuccess() {
      return this.success;
   }

   public T getResult() {
      return this.result;
   }

   public Exception getError() {
      return this.error;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof AsyncResult)) {
         return false;
      } else {
         AsyncResult<?> other = (AsyncResult)o;
         if (this.isSuccess() != other.isSuccess()) {
            return false;
         } else {
            Object this$result = this.getResult();
            Object other$result = other.getResult();
            if (this$result == null) {
               if (other$result != null) {
                  return false;
               }
            } else if (!this$result.equals(other$result)) {
               return false;
            }

            Object this$error = this.getError();
            Object other$error = other.getError();
            if (this$error == null) {
               if (other$error != null) {
                  return false;
               }
            } else if (!this$error.equals(other$error)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      int result = result * 59 + (this.isSuccess() ? 79 : 97);
      Object $result = this.getResult();
      result = result * 59 + ($result == null ? 43 : $result.hashCode());
      Object $error = this.getError();
      result = result * 59 + ($error == null ? 43 : $error.hashCode());
      return result;
   }

   public String toString() {
      return "AsyncResult(success=" + this.isSuccess() + ", result=" + this.getResult() + ", error=" + this.getError() + ")";
   }
}
