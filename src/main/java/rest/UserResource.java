package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
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
    @RolesAllowed("user")
    public String getFromUserProfile(@PathParam("username")String username) {
        UserDTO ud = userFacade.getUserData(username);
        return GSON.toJson(ud);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("Users")
    @RolesAllowed("admin")
    public String getFromUserProfile() {
        List<UserDTO> listDTO = userFacade.getUsersData();

        return GSON.toJson(listDTO);
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("allUserPosts/{userName}")
    @RolesAllowed("user")
    public String getPosts(@PathParam("userName")String userName) {
        List<PostDTO> p= postFacade.getAllButWithDateFirst(userName);
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
        String result=postFacade.delete(id);
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
        String result=postFacade.edit(p);
        return GSON.toJson(result);
    }
    //Gør så alle ikke bare kan poste som alle
    //find bruger ved hjælp af hvad man er logget ind som
    @POST
    @Path("addPost")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String addPost(String post) {
        PostDTO p = GSON.fromJson(post, PostDTO.class);
        //ide indtil videre
        String name =securityContext.getUserPrincipal().getName();
        p.getUser().setUserName(name);
        //Virker det mon sikkerhedsmæssigt
        PostDTO result=postFacade.addPost(p);
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
    @RolesAllowed("user_admin")
    public String getAllPosts() {
        PostsDTO posts = postFacade.getAllPosts();
        return GSON.toJson(posts);
    }

}
