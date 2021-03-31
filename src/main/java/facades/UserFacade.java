package facades;

import dto.PostsDTO;
import dto.UserDTO;
import entities.Post;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import security.errorhandling.AuthenticationException;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }
    
    public UserDTO getUserData(String username){
        EntityManager em = emf.createEntityManager();

        UserDTO f = null;
        User user = null;
         try {
            user =  em.createQuery(
        "SELECT u FROM User u WHERE u.userName = :userName",
        User.class).setParameter("userName", username).getSingleResult();
            
            f=new UserDTO(user);
            return f;
            
         } finally {
            em.close();
        }
    }
    public PostsDTO getUserPosts(){
        EntityManager em = emf.createEntityManager();
        PostsDTO p;
        try{
            TypedQuery<Post> query = 
                       em.createQuery("Select p from Post p",Post.class);
             p= new PostsDTO((ArrayList<Post>) query.getResultList());
             return p;
        }finally {
            em.close();}
        

        
                
    }
    
    public static void main(String[] args) {
        
    }
}
