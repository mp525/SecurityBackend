/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dto.PostDTO;
import dto.PostsDTO;
import dto.UserDTO;
import entities.Post;
import entities.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import utils.EMF_Creator;

/**
 *
 * @author matti
 */
@Disabled
public class PostFacadeTest {
     private static EntityManagerFactory emf;

    private static PostFacade facade;

    
     private User u1;
    private User u2;
    private User u3;
    private Post p1;
    private Post p2;
    public PostFacadeTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PostFacade.getPostFacade(emf);
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
         EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            u1 = new User("test1","secret1");
            p1= new Post("whuptidup",u1);
            p2= new Post("whuptidup",u1);
            u1.addPost(p1);
            u1.addPost(p2);
            u2 = new User("test2","secret2");
            u3 = new User("test3","secret3");
            em.createNamedQuery("Post.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(u1);
            //em.persist(p1);
            em.persist(u2);
            em.persist(u3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    @AfterEach
    public void tearDown() {
    }

    

    /**
     * Test of getAllButWithDateFirst method, of class PostFacade.
     */
    @Test
    public void testGetAllButWithDateFirst() {
        System.out.println("getAllButWithDateFirst");
        String username = "test1";
        int expResult = 2;
        List<PostDTO> result = facade.getAllButWithDateFirst(username);
        assertEquals(expResult, result.size());
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of delete method, of class PostFacade.
     */
    //@Test
    public void testDelete() {
        System.out.println("delete");
        int id = 2;
        PostFacade instance = null;
        String expResult = "";
        String result = instance.delete(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    

    /**
     * Test of edit method, of class PostFacade.
     */
    @Test
    public void testEdit() {
        System.out.println("edit");
        Post p= p1;
        PostDTO pp=new PostDTO(p);
        
        String result = facade.edit(pp);
        assertTrue(result.contains("Edited the post"));
        
        
    }

   

    /**
     * Test of addPost method, of class PostFacade.
     */
    @Test
    public void testAddPost() {
        System.out.println("addPost");
        UserDTO u= new UserDTO("test1","secro","","");
        PostDTO p1 = new PostDTO("test1","",u);
        PostDTO expResult = p1;
        PostDTO result = facade.addPost(p1);
        assertEquals(expResult, result);        
    }

    

    /**
     * Test of getAllPosts method, of class PostFacade.
     */
    @Test
    public void testGetAllPosts() {
        System.out.println("getAllPosts");
        int expResult = 2;
        PostsDTO result = facade.getAllPosts();
        assertEquals(expResult, result.getList().size());
    }
    
}
