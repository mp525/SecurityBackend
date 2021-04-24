package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jose.JOSEException;
import dto.PostDTO;
import dto.UserDTO;
import dto.PostsDTO;
import entities.User;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
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

    @Context
    SecurityContext securityContext;

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
    @Path("profile/{username}")
    public String getFromUserProfile(@PathParam("username") String username) {
        UserDTO ud = userFacade.getUserData(username);
        return GSON.toJson(ud);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("Users")
    public String getFromUserProfile() {
        List<UserDTO> listDTO = userFacade.getUsersData();

        return GSON.toJson(listDTO);
    }

    //not work yet
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Path("Delete/{userName}")
    public String deleteUser(@PathParam("userName") String userName) {
        userFacade.deleteUser(userName);

        return GSON.toJson("Success");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("allUserPosts/{userName}")
    public String getPosts(@PathParam("userName") String userName) {
        List<PostDTO> p = postFacade.getAllFromUser(userName);
        return GSON.toJson(p);
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
