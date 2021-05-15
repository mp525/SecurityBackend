/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dto.UserDTO;
import entities.User;
import facades.UserFacade;
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
public class UserFacadeTest {
      private static EntityManagerFactory emf;
    private static UserFacade facade;

    

    
     private User u1;
    private User u2;
    private User u3;

    public UserFacadeTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
     emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
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
            u1.setEmail("MattierCool.dk");
            u2 = new User("test2","secret2");
            u3 = new User("test3","secret3");
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(u1);
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
     * Test of getUserData method, of class UserFacade.
     */
    @Test
    public void testGetUserData() {
        System.out.println("getUserData");
        String username = "test1";
        
        UserDTO expResult = new UserDTO("test1","MattierCool.dk","","");
        UserDTO result = facade.getUserData(username);
        assertEquals(expResult.getUserName(), result.getUserName());
        assertEquals(expResult.getEmail(), result.getEmail());
        
    }

   
    /**
     * Test of getUsersData method, of class UserFacade.
     */
    @Test
    public void testGetUsersData() {
        System.out.println("getUsersData");
        int amount=3;
        List<UserDTO> result = facade.getUsersData();
        assertEquals(amount, result.size());
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of registerUser method, of class UserFacade.
     */
    //@Test
//    public void testRegisterUser() {
//        System.out.println("registerUser");
//        String username = "";
//        String password = "";
//        User expResult = null;
//        User result = facade.registerUser(username, password);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    
}
