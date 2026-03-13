package github.nighter.smartspawner.libs.mariadb.plugin.credential.aws;

import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.plugin.Credential;
import java.util.Properties;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.rds.RdsUtilities;

public class AwsCredentialGenerator {
   private final String authenticationToken;
   private final String userName;

   public AwsCredentialGenerator(Properties nonMappedOptions, String userName, HostAddress hostAddress) {
      this.userName = userName;
      String accessKeyId = nonMappedOptions.getProperty("accessKeyId");
      String secretKey = nonMappedOptions.getProperty("secretKey");
      String region = nonMappedOptions.getProperty("region");
      Object awsCredentialsProvider;
      if (accessKeyId != null && secretKey != null) {
         awsCredentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretKey));
      } else {
         awsCredentialsProvider = DefaultCredentialsProvider.builder().build();
      }

      RdsUtilities utilities = RdsUtilities.builder().credentialsProvider((AwsCredentialsProvider)awsCredentialsProvider).region(region != null ? Region.of(region) : (new DefaultAwsRegionProviderChain()).getRegion()).build();
      this.authenticationToken = utilities.generateAuthenticationToken((builder) -> {
         builder.username(userName).hostname(hostAddress.host).port(hostAddress.port).credentialsProvider(awsCredentialsProvider);
      });
   }

   public Credential getToken() {
      return new Credential(this.userName, this.authenticationToken);
   }
}
