package module.workflow.ui;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.fenixedu.bennu.WorkflowConfiguration;
import org.fenixedu.bennu.core.rest.JsonBodyReaderWriter;
import org.fenixedu.bennu.io.domain.GenericFile;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
@Service
public class CertifierService {

    private final Client client;

    public CertifierService() {
        client = ClientBuilder.newClient();
        client.register(MultiPartFeature.class);
        client.register(JsonBodyReaderWriter.class);
    }

    public void sendDocumentToBeCertified(String filename, GenericFile file, String uniqueIdentifier, boolean alreadyCertified) {
        String compactJws = Jwts.builder().setSubject(uniqueIdentifier)
                .signWith(SignatureAlgorithm.HS512, WorkflowConfiguration.getConfiguration().certifierJwtSecret().getBytes()).compact();

        try (final FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
                InputStream fileStream = file.getStream()) {
            final StreamDataBodyPart streamDataBodyPart =
                    new StreamDataBodyPart("file", fileStream, filename, new MediaType("application", "pdf"));
            formDataMultiPart.bodyPart(streamDataBodyPart);
            formDataMultiPart.bodyPart(new FormDataBodyPart("filename", filename));
            formDataMultiPart.bodyPart(new FormDataBodyPart("mimeType", "application/json"));
            formDataMultiPart.bodyPart(new FormDataBodyPart("identifier", uniqueIdentifier));
            formDataMultiPart.bodyPart(new FormDataBodyPart("alreadyCertified", Boolean.valueOf(alreadyCertified).toString()));

            String result = client.target(WorkflowConfiguration.getConfiguration().certifierUrl())
                    .path("api/documents/certify").request().header("Authorization", "Bearer " + compactJws)
                    .post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA_TYPE), String.class);
        } catch (final IOException e) {
            throw new Error(e);
        }
    }
}
