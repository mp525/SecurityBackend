/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Post;
import entities.User;
import java.util.List;

/**
 *
 * @author matti
 */
public class UserDTO {
    private String userName;
  
  private String email;
  private String firstName;
  private String lastName;
  private List<Post> posts;

    public UserDTO(String userName, String email, String firstName, String lastName, List<Post> posts) {
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.posts = posts;
    }
public UserDTO(User u) {
        this.userName = u.getUserName();
        this.email = u.getEmail();
        this.firstName = u.getFirstName();
        this.lastName = u.getLastName();
        this.posts = u.getPosts();
    }
    @Override
    public String toString() {
        return "UserDTO{" + "userName=" + userName + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", posts=" + posts + '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
  
}
