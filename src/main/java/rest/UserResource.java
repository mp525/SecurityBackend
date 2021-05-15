package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.nimbusds.jose.JOSEException;
import dto.PostDTO;
import dto.UserDTO;
import dto.PostsDTO;
import dto.RecaptchaDTO;
import entities.User;
import facades.PostFacade;
import facades.UserFacade;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.net.ssl.HttpsURLConnection;
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
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;
import utils.HttpUtils;

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
    @Path("profile")
    @RolesAllowed("user")
    public String getFromUserProfile() {
         String name =securityContext.getUserPrincipal().getName();
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
    @Path("allUserPosts")
    @RolesAllowed("user")
    public String getPosts() {
        String name = securityContext.getUserPrincipal().getName();
        List<PostDTO> p= postFacade.getAllButWithDateFirst(name);
        return GSON.toJson(p);
    }
    
     //kun sin egen
    
    @DELETE
    @Path("deletePostUser/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("user")
    public String deletePost(@PathParam("id") int id) {
        String name = securityContext.getUserPrincipal().getName();
        String result=postFacade.saveDelete(id, name);
        
        return GSON.toJson(result);
    }
    
        //kun sin egen
    @PUT
    @Path("editPostUser")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("user")
    public String editPostUser(String post) {
        PostDTO p = GSON.fromJson(post, PostDTO.class);
        String name=securityContext.getUserPrincipal().getName();
        String result=postFacade.editForUser(p,name);
        return GSON.toJson(result);
    }
    
    //admin from down here ----------------------------------------------
    //not work yet
    @DELETE
    @Path("user/deleteUser/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String deleteUser(@PathParam("id") String id) {
        String result=userFacade.deleteUser(id);
        return GSON.toJson(result);
    }
    
     //admin er en boss derfor skal han kunne gøre det her
    //hvis ikke admin så "no can do mate"
    @DELETE
    @Path("deletePost/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String deletePostUser(@PathParam("id") int id) {
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
    @RolesAllowed("user")
    public String addPost(String post) throws IOException {
        PostDTO p = GSON.fromJson(post, PostDTO.class);
        String name = securityContext.getUserPrincipal().getName();
        p.getUser().setUserName(name);
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
    public String registerUser(String givenUser) throws AuthenticationException, JOSEException, IOException {
        UserResource ur= new UserResource();
       
        UserDTO dto = GSON.fromJson(givenUser, UserDTO.class);
        System.out.println(dto.getToken());
        boolean human=ur.verify(dto.getToken());
        if(human){
        String username = dto.getUserName();
        String password = dto.getPassword();
        String firstName=dto.getFirstName();
        String lastName=dto.getLastName();
        String email=dto.getEmail();
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
            User user = userFacade.registerUser(username, password,email,firstName,lastName);
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
        }
        //json = GSON.toJson("{\"msg\": \"Action could not be executed. Something went wrong.\"}");
        //return "{\"msg\": \"Action could not be executed. Something went wrong.\"}";
        //throw new AuthenticationException("Invalid username or password! Please try again");
        return ""+human;
    }
    
    public final String url = "https://www.google.com/recaptcha/api/siteverify";
	public final String SECRET = "6LcIFdAaAAAAAK_Iv5yhQXxqxxQhnESAHCS7UYr9";
	private final static String USER_AGENT = "Mozilla/5.0";

	public boolean verify(String gRecaptchaResponse) throws IOException {
		if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
			return false;
		}
		try{
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		String postParams = "secret=" + SECRET + "&response="
				+ gRecaptchaResponse;
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();		
		//parse JSON response and return 'success' value
		    javax.json.JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
		    javax.json.JsonObject jsonObject = jsonReader.readObject();
		jsonReader.close();
		
		return jsonObject.getBoolean("success");
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
