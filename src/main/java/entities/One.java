/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Mathias
 */
@Entity
public class One implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    
    @OneToMany(mappedBy = "one", cascade = CascadeType.PERSIST)
    private List<Many> many;

    public One(List<Many> many, String name) {
        this.many = new ArrayList();
        this.name = name;
    }

    public One() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    

    public List<Many> getMany() {
        return many;
    }
    
    public void addMany(Many many) {
        this.many.add(many);
        if(many != null){
            many.setOne(this);
        }
    }
    
        public void removeMany(Many many) {
        if(many != null){
            this.many.remove(many);
        }
    }
    
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof One)) {
            return false;
        }
        One other = (One) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.One[ id=" + id + " ]";
    }
    
}
