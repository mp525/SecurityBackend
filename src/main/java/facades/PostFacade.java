package facades;

import dto.PictureDTO;
import dto.PostDTO;
import dto.PostsDTO;
import entities.Picture;
import entities.Post;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import utils.InputSanitiser;

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



    public List<PostDTO> getAllButWithDateFirst(String username){
          List<PostDTO> list = new ArrayList();
        EntityManager em = emf.createEntityManager();

        TypedQuery query = em.createQuery("SELECT p FROM Post p WHERE p.user.userName = :user ORDER BY p.posted DESC", Post.class);
        query.setParameter("user", username);
        List<Post> posts = query.getResultList();

        if (!(posts.isEmpty())) {
            for (Post p1 : posts) {
                list.add(new PostDTO(p1));
            }
        }
        return list;

    }
    
    public String delete(int id){
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Post p= em.find(Post.class, id);
//        User u =p.getUser();
        em.remove(p);
        
        em.getTransaction().commit();
        return "Deleted the post";
    }
    public String saveDelete(int id, String username){
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Post p= em.find(Post.class, id);
//        User u =p.getUser();
        if(p.getUser().getUserName().equals(username)){
        em.remove(p);    
        em.getTransaction().commit();
        return "Deleted the post";
        
        }else{
        return "You cant delete other peoples post you nophead";    
        }
        
        
        
        
        
    }
    public String edit(PostDTO p1){
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Post p= em.find(Post.class, p1.getId());
        p.setContent(p1.getContent());
        em.persist(p);
        
        em.getTransaction().commit();
        return "Edited the post";
    }
    
    public String editForUser(PostDTO p1,String username){
        EntityManager em = emf.createEntityManager();
        InputSanitiser inputS = new InputSanitiser();
        em.getTransaction().begin();
        Post p= em.find(Post.class, p1.getId()); 
        if(p.getUser().getUserName().equals(username)){
            String sanitised = inputS.sanitiser(p1.getContent());
            p.setContent(sanitised);
            em.persist(p);
            em.getTransaction().commit();
            return "Edited the post";
        

        }else{
        return "You cant edit other peoples post you nophead";    

        }
    }

    public PostDTO addPost(PostDTO p1){
        EntityManager em = emf.createEntityManager();
        InputSanitiser inputS = new InputSanitiser();

        String sanitisedInput = inputS.sanitiser(p1.getContent());
        p1.setContent(sanitisedInput);

        em.getTransaction().begin();
        Post p = new Post(p1);

        em.persist(p);

        em.getTransaction().commit();
        return p1;
    }
    

    public PostsDTO getAllPosts() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Post> query = em.createQuery("Select p from Post p", Post.class);
            List<Post> posts = query.getResultList();
            PostsDTO postsDTO = new PostsDTO(posts);
            return postsDTO;
        } finally {
            em.close();
        }

    }

//TODO Pictures
    public PictureDTO addPicture(PictureDTO p1){
        EntityManager em = emf.createEntityManager();
        InputSanitiser inputS = new InputSanitiser();


        //TODO muligt at sanitseren kan skabe issues med base64 encryption
        String sanitisedInput = inputS.sanitiser(p1.getContent());
        p1.setContent(sanitisedInput);

        em.getTransaction().begin();
        Picture p = new Picture(p1);

        em.persist(p);

        em.getTransaction().commit();
        return p1;
    }

    public List<PictureDTO> getAllUserPicturesByDate(String username){
        List<PictureDTO> list = new ArrayList();
        EntityManager em = emf.createEntityManager();

        TypedQuery query = em.createQuery("SELECT p FROM Picture p WHERE p.user.userName = :user ORDER BY p.posted DESC", Picture.class);
        query.setParameter("user", username);
        List<Picture> pictures = query.getResultList();

        if (!(pictures.isEmpty())) {
            for (Picture p1 : pictures) {
                list.add(new PictureDTO(p1));
            }
        }
        return list;


    }
}
