/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Post;

/**
 *
 * @author matti
 */
public class PostDTO {
    private int id;
    private String content;
    private String posted;
    private UserDTO user;

    public PostDTO(String content, String posted, UserDTO user) {
        
        this.content = content;
        this.posted = posted;
        this.user = user;
    }
    public PostDTO(Post p) {
        this.id=p.getId();
        this.content = p.getContent();
        this.posted = p.getPosted().toString();
        this.user = new UserDTO(p.getUser());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
}
