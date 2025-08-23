package com.hoovejd.phototx;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.UserCredentials;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.library.v1.proto.ListAlbumsRequest;
import com.google.photos.library.v1.proto.ListAlbumsResponse;
import com.google.common.collect.ImmutableList;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class PhototxApplication implements CommandLineRunner {

	private static final List<String> REQUIRED_SCOPES = ImmutableList.of(
			"https://www.googleapis.com/auth/photoslibrary.readonly",
			"https://www.googleapis.com/auth/photoslibrary.appendonly");

	public static void main(String[] args) {
		SpringApplication.run(PhototxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.debug("running!");

		Credentials credentials = UserCredentials.newBuilder()
				.setClientId("xxxx")
				.setClientSecret("xxxx").build();

		PhotosLibrarySettings settings = PhotosLibrarySettings.newBuilder()
				.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();

		PhotosLibraryClient client = PhotosLibraryClient.initialize(settings);

		ListAlbumsRequest request = ListAlbumsRequest.getDefaultInstance();

		ListAlbumsResponse response = client.listAlbumsCallable().call(request);

		response.getAlbumsList();
	}

}