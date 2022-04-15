package com.dev.app.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dev.app.dto.AwsSecrets;
import com.google.gson.Gson;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Configuration
public class ApplicationConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);
	@Value("${aws.access_key_id}")
	private String accessKeyId;
	@Value("${aws.secret_key}")
	private String secretKey;
	private Gson gson = new Gson();

	@Bean
	public DataSource dataSource() {
		AwsSecrets secrets = getSecret();
		return DataSourceBuilder.create()
				// .driverClassName("com.mysql.cj.jdbc.driver")
				.url("jdbc:" + secrets.getEngine() + "://" + secrets.getHost() + ":" + secrets.getPort() + "/MyDB")
				.username(secrets.getUsername()).password(secrets.getPassword()).build();
	}

	private AwsSecrets getSecret() {

		String secretName = "db-credentials";
		Region region = Region.of("eu-central-1");

		// Create a Secrets Manager client
		AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyId, secretKey);
		SecretsManagerClient client = SecretsManagerClient.builder()
				.credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials)).region(region).build();

		String secret;

		GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName).build();
		GetSecretValueResponse getSecretValueResponse = null;

		try {
			getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
		} catch (Exception e) {
			LOGGER.info("{exception ->}" + e.getMessage());
			throw e;
		}

		if (getSecretValueResponse.secretString() != null) {
			secret = getSecretValueResponse.secretString();
			AwsSecrets secreteObj = gson.fromJson(secret, AwsSecrets.class);
			System.out.println(secreteObj);
			return secreteObj;
		}
		return null;
	}
}
