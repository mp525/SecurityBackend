/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Post;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matti
 */
public class PostsDTO {
    ArrayList<PostDTO>list;

    
    public PostsDTO(ArrayList<Post> p) {
        p.forEach(post -> {
            list.add(new PostDTO(post));
        });
        
    }
    
    public PostsDTO(List<Post> posts) {
        this.list = new ArrayList();
        for (Post post : posts) {
            this.list.add(new PostDTO(post));
        }
    }
    public ArrayList<PostDTO> getList() {
        return list;
        
    }

    public void setList(ArrayList<PostDTO> list) {
        this.list = list;
    }
    
    public void add(PostDTO p){
        this.list.add(p);
    }
}
