package com.tallerbd.backend.firstdb.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.tallerbd.backend.firstdb.domain.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="login")
public class Login{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //ESTO VA A FUNCIONAR COMO TOKEN

    @Column(name = "timestamp")
    private String timestamp;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "loginStatus")
    private Boolean loginStatus;

    // public Login(){
    //     super();
    //  }

    public Login(String timestamp, User user, Boolean loginStatus){
        this.timestamp = timestamp;
        this.user = user;
        this.loginStatus = loginStatus;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setLoginStatus(Boolean loginStatus){
        this.loginStatus = loginStatus;
    }

    public User getUser(){
        return this.user;
    }

    public Boolean getLoginStatus(){
        return this.loginStatus;
    }

    
}