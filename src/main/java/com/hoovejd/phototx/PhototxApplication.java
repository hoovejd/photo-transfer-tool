package com.hoovejd.phototx;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.UserCredentials;
import com.google.common.collect.ImmutableList;
import com.google.photos.library.v1.PhotosLibraryClient;
import com.google.photos.library.v1.PhotosLibrarySettings;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient.ListAlbumsPagedResponse;
import com.google.photos.library.v1.proto.CreateAlbumRequest;
import com.google.photos.library.v1.proto.ListAlbumsRequest;
import com.google.photos.library.v1.proto.ListAlbumsResponse;
import com.google.photos.types.proto.Album;

public class PhototxApplication {

	private static final String credentialsPath = "/home/hoovejd/workspace/photo-transfer-tool/my_oauth_creds.json";

	private static final List<String> REQUIRED_SCOPES = ImmutableList.of(
			"https://www.googleapis.com/auth/photoslibrary.appendonly",
			"https://www.googleapis.com/auth/photoslibrary.readonly.appcreateddata",
			"https://www.googleapis.com/auth/photoslibrary.edit.appcreateddata");

	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			PhototxApplication.class.getResource("/").getPath(), "credentials");

	private static final int LOCAL_RECEIVER_PORT = 61984;

	public static void main(String[] args) {

		System.out.println("running");

		GoogleClientSecrets clientSecrets;
		try {
			clientSecrets = GoogleClientSecrets.load(
					GsonFactory.getDefaultInstance(), new InputStreamReader(new FileInputStream(credentialsPath)));

			String clientId = clientSecrets.getDetails().getClientId();
			String clientSecret = clientSecrets.getDetails().getClientSecret();
			System.out.println("clientId: " + clientId);
			System.out.println("clientSecret: " + clientSecret);

			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
					GoogleNetHttpTransport.newTrustedTransport(),
					GsonFactory.getDefaultInstance(),
					clientSecrets,
					REQUIRED_SCOPES)
					.setDataStoreFactory(new FileDataStoreFactory(DATA_STORE_DIR))
					.setAccessType("offline")
					.build();

			LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(LOCAL_RECEIVER_PORT).build();

			Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("hoov85@gmail.com");

			System.out.println("RefreshToken: " + credential.getRefreshToken());

			Credentials credentials = UserCredentials.newBuilder()
					.setClientId(clientId)
					.setClientSecret(clientSecret)
					.setRefreshToken(credential.getRefreshToken())
					.build();

			System.out.println("creds: " + credentials.getRequestMetadata().toString());

			PhotosLibrarySettings settings = PhotosLibrarySettings.newBuilder()
					.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();

			PhotosLibraryClient client = PhotosLibraryClient.initialize(settings);

			Album album1 = client.createAlbum("TestAlbum1");

			ListAlbumsPagedResponse response = client.listAlbums(true);

			if (response.getNextPageToken().isEmpty()) {
				System.out.println("empty yo");
			} else {
				System.out.println("getNextPageToken: " + response.getNextPageToken());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}