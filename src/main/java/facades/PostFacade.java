package facades;

import dto.PostDTO;
import entities.Post;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PostFacade {

    
    private static EntityManagerFactory emf;
    private static PostFacade instance;

    private PostFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static PostFacade getPostFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PostFacade();
        }
        return instance;
    }
    
    public List<PostDTO> getAllFromUser(String username){
          List<PostDTO> list = new ArrayList();
        EntityManager em = emf.createEntityManager();

        TypedQuery query = em.createQuery("SELECT p FROM Post p WHERE p.user.userName LIKE :user", Post.class);
        query.setParameter("user", "%" + username + "%");
        List<Post> posts = query.getResultList();

        if (!(posts.isEmpty())) {
            for (Post p1 : posts) {
                list.add(new PostDTO(p1));
            }
        }
        return list;
        
    }
    
    
    

}
