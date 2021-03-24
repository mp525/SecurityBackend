/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author Mathias
 */
public class Tester {

    public static void main(String[] args) {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        
        One one = new One(new ArrayList(), "Test1");
        Many many = new Many("Test1");
        
        //Post postTest = new Post("hgierg", )
        
        one.addMany(many);

        em.getTransaction().begin();
        em.persist(one);
        em.getTransaction().commit();
        
    }

}
