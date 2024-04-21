package com.bienvan.store.payload.request;

import java.util.Set;

public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String gender;
    private Set<String> listRoles;

    public SignupRequest(String email, String password, String name, String gender, Set<String> listRoles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.listRoles = listRoles;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setListRoles(Set<String> listRoles) {
        this.listRoles = listRoles;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Set<String> getListRoles() {
        return listRoles;
    }

}
