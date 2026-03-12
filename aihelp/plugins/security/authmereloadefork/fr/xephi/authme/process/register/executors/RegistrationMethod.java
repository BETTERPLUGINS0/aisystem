package fr.xephi.authme.process.register.executors;

public final class RegistrationMethod<P extends RegistrationParameters> {
   public static final RegistrationMethod<PasswordRegisterParams> PASSWORD_REGISTRATION = new RegistrationMethod(PasswordRegisterExecutor.class);
   public static final RegistrationMethod<TwoFactorRegisterParams> TWO_FACTOR_REGISTRATION = new RegistrationMethod(TwoFactorRegisterExecutor.class);
   public static final RegistrationMethod<EmailRegisterParams> EMAIL_REGISTRATION = new RegistrationMethod(EmailRegisterExecutor.class);
   public static final RegistrationMethod<ApiPasswordRegisterParams> API_REGISTRATION = new RegistrationMethod(ApiPasswordRegisterExecutor.class);
   private final Class<? extends RegistrationExecutor<P>> executorClass;

   private RegistrationMethod(Class<? extends RegistrationExecutor<P>> executorClass) {
      this.executorClass = executorClass;
   }

   public Class<? extends RegistrationExecutor<P>> getExecutorClass() {
      return this.executorClass;
   }
}
