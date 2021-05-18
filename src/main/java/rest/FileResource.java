package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PictureDTO;
import facades.PostFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.util.List;

/**
 * @author jkss
 */
@Path("files")
public class FileResource {

    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;
    UserFacade userFacade = UserFacade.getUserFacade(EMF);
    PostFacade postFacade = PostFacade.getPostFacade(EMF);

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    @POST
    @Path("addPicture")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("user")
    public String addPicture(String picture) throws IOException {
        PictureDTO p = GSON.fromJson(picture, PictureDTO.class);
        String name = securityContext.getUserPrincipal().getName();
        p.getUser().setUserName(name);
        PictureDTO result=postFacade.addPicture(p);
        return GSON.toJson(result);

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAllUserPictures")
    @RolesAllowed("user")
    public String getPictures() {
        String name = securityContext.getUserPrincipal().getName();
        List<PictureDTO> p= postFacade.getAllUserPicturesByDate(name);
        return GSON.toJson(p);
    }
}

