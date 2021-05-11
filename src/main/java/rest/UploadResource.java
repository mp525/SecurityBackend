package rest;

import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;

/**
 * @author jkss
 */
@Path("files")
public class UploadResource {

    @Context
    SecurityContext securityContext;

    //Just to verify if the database is setup
    @POST
    @Path("upload")

    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream
            //@FormDataParam("file") FormDataContentDisposition fileDetail
    ) {
        return "Returned";
    }

}