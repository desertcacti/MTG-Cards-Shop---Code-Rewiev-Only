package pl.desertcacti.mtgcardsshopsystem.configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/** GoogleDrive class creates and configures a Google Drive Service instance. */
@Configuration
public class GoogleDriveConfig {
    @Value("${google.drive.api.credentials.location}")
    private Resource credentialsResource;
    @Value("${google.drive.application.name}")
    private String applicationName;

    /** getDriveService()
    /*  Method road map:
          *1* The factory for JSON parsing and serialization.
          *2* The scope for Google Drive API access.
          *3* Loads the Google Drive API credentials from the provided resource and creates a scoped credential.
          *4* Builds and returns the Drive service instance with the configured credentials and application name. */
    @Bean
    public Drive getDriveService() throws IOException, GeneralSecurityException {
        // *1*
        final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        // *2*
        final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
        // *3*
        GoogleCredential credential = GoogleCredential.fromStream(
                credentialsResource.getInputStream()).createScoped(SCOPES);
        // *4*
        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .setApplicationName(applicationName)
                .build();
    }
}