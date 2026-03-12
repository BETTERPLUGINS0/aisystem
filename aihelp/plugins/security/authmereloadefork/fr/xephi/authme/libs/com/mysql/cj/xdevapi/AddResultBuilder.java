package fr.xephi.authme.libs.com.mysql.cj.xdevapi;

public class AddResultBuilder extends UpdateResultBuilder<AddResult> {
   public AddResult build() {
      return new AddResultImpl(this.statementExecuteOkBuilder.build());
   }
}
