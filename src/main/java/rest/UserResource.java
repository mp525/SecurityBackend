package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.JOSEException;
import dto.PostDTO;
import dto.UserDTO;
import dto.PostsDTO;
import entities.User;
import facades.FetchFacade;
import facades.PostFacade;
import facades.UserFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class UserResource {

    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;
    UserFacade userFacade = UserFacade.getUserFacade(EMF);
    PostFacade postFacade = PostFacade.getPostFacade(EMF);
    FetchFacade f = new FetchFacade();
    @Context
    SecurityContext securityContext;

    //picture upload
    @POST
    @Path("/fileupload")  //Your Path or URL to call this service
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @DefaultValue("true") @FormDataParam("enabled") boolean enabled,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {
        //Your local disk path where you want to store the file
        String uploadedFileLocation = "D://uploadedFiles/" + fileDetail.getFileName();
        System.out.println(uploadedFileLocation);
        // save it
        File objFile = new File(uploadedFileLocation);
        if (objFile.exists()) {
            objFile.delete();

        }

        f.saveToFile(uploadedInputStream, uploadedFileLocation);

        String output = "File uploaded via Jersey based RESTFul Webservice to: " + uploadedFileLocation;

        return Response.status(200).entity(output).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("select u from User u", entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("profile")
    @RolesAllowed("user")
    public String getFromUserProfile() {
        String name = securityContext.getUserPrincipal().getName();
        UserDTO ud = userFacade.getUserData(name);

        return GSON.toJson(ud);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("Users")
    @RolesAllowed("admin")
    public String getAllFromUserProfile() {
        List<UserDTO> listDTO = userFacade.getUsersData();

        return GSON.toJson(listDTO);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("allUserPosts/{userName}")
    @RolesAllowed("user")
    public String getPosts(@PathParam("userName") String userName) {
        List<PostDTO> p = postFacade.getAllButWithDateFirst(userName);
        return GSON.toJson(p);
    }

    //admin from down here ----------------------------------------------
    //not work yet
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Path("Delete/{userName}")
    public String deleteUser(@PathParam("userName") String userName) {
        userFacade.deleteUser(userName);

        return GSON.toJson("Success");
    }

    //admin er en boss derfor skal han kunne gøre det her
    //hvis ikke admin så "no can do mate"
    @DELETE
    @Path("deletePost/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String deletePost(@PathParam("id") int id) {
        String result = postFacade.delete(id);
        return GSON.toJson(result);
    }

    //admin er en boss derfor skal han kunne gøre det her
    //hvis ikke admin så "no can do mate"
    @PUT
    @Path("editPost")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String editPost(String post) {
        PostDTO p = GSON.fromJson(post, PostDTO.class);
        String result = postFacade.edit(p);
        return GSON.toJson(result);
    }

    //Gør så alle ikke bare kan poste som alle
    //find bruger ved hjælp af hvad man er logget ind som
    @POST
    @Path("addPost")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("user")
    public String addPost(String post) {
        PostDTO p = GSON.fromJson(post, PostDTO.class);
        //ide indtil videre
        String name = securityContext.getUserPrincipal().getName();
        p.getUser().setUserName(name);
        //Virker det mon sikkerhedsmæssigt
        PostDTO result = postFacade.addPost(p);
        return GSON.toJson(result);

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("allPosts")
    @RolesAllowed("user")
    public String getAllPosts() {
        PostsDTO posts = postFacade.getAllPosts();
        return GSON.toJson(posts);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("allPostsAdmin")
    @RolesAllowed("admin")
    public String getAllPostsAdmin() {
        PostsDTO posts = postFacade.getAllPosts();
        return GSON.toJson(posts);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String registerUser(String givenUser) throws AuthenticationException, JOSEException {
        UserDTO dto = GSON.fromJson(givenUser, UserDTO.class);
        String username = dto.getUserName();
        String password = dto.getPassword();
        EntityManager em = EMF.createEntityManager();
        List<User> users;
        List<String> usernames = new ArrayList();
        String json;
        try {
            TypedQuery<User> query = em.createQuery("select u from User u", User.class);
            users = query.getResultList();
            for (User user : users) {
                usernames.add(user.getUserName());
            }
        } finally {
            em.close();
        }

        if (username.isEmpty() || password.isEmpty()) {
            json = GSON.toJson("{\"msg\": \"Both boxes must be filled, try again.\"}");
            return "{\"msg\": \"Both boxes must be filled, try again.\"}";
        } else if (usernames.contains(username)) {
            //json = GSON.toJson("{\"msg\": \"Username " + username + " already in use. Try again.\"}");
            return "{\"msg\": \"Username " + username + " already in use. Try again.\"}";
        } else {
            User user = userFacade.registerUser(username, password);
            json = GSON.toJson("{\"msg\": \"User " + username + " registered\"}");
            return json;
//            try {
//                return quickLogin(username, password);
//
//            } catch (JOSEException | AuthenticationException ex) {
//                if (ex instanceof AuthenticationException) {
//                    throw (AuthenticationException) ex;
//                }
//            }

//            if (user != null) {
//                return "{\"msg\": \"User " + username + " registered\"}";
//            }
        }
        //json = GSON.toJson("{\"msg\": \"Action could not be executed. Something went wrong.\"}");
        //return "{\"msg\": \"Action could not be executed. Something went wrong.\"}";
        //throw new AuthenticationException("Invalid username or password! Please try again");
    }

}
