package facades;

import dto.UserDTO;
import static dto.UserDTO.toDTO;
import entities.Role;
import entities.User;
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

    public UserDTO getUserData(String username) {
        EntityManager em = emf.createEntityManager();

        UserDTO f = null;
        User user = null;
        try {
            user = em.createQuery(
                    "SELECT u FROM User u WHERE u.userName = :userName",
                    User.class).setParameter("userName", username).getSingleResult();

            f = new UserDTO(user);
            return f;

        } finally {
            em.close();
        }
    }

    //not work yet
    public String deleteUser(String userName) {
        EntityManager em = emf.createEntityManager();
        try {
                em.getTransaction().begin();
                User u = null;  
            if(u==null){
                System.out.println("No User named that");
                return "It did not work";
            }else{
                em.remove(u);
                em.getTransaction().commit();
                return "worked";
            }
        } finally {
            em.close();
        }   
    }

    public List<UserDTO> getUsersData() {
        EntityManager em = emf.createEntityManager();

        UserDTO f = null;
        User user = null;
        try {
            TypedQuery<User> query
                    = em.createQuery("Select u from User u", User.class);

            List<UserDTO> listDTO = toDTO(query.getResultList());
            return listDTO;
        } finally {
            em.close();
        }
    }
    
    public User registerUser(String username, String password,String email,String firstName,String lastName) {
        EntityManager em = emf.createEntityManager();
        User user;
        Role userRole = new Role("user");
        try {
            em.getTransaction().begin();
            user = new User(username, password,email,firstName,lastName);
            user.addRole(userRole);
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return user;
    }

    

    public static void main(String[] args) {

    }
}
