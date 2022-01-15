package com.ims.serviceprojects.repo.model;

import javax.persistence.*;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long user_id;
    private String name;
    private String project_description;
    private float deposits;
    private String project_url;

    public Project() {
    }

    public Project(long user_id, String name, String project_description, float deposits, String project_url) {
        this.user_id = user_id;
        this.name = name;
        this.project_description = project_description;
        this.deposits = deposits;
        this.project_url = project_url;
    }



    public long getId() {
        return id;
    }

    public long getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getProject_description() {
        return project_description;
    }

    public float getDeposits() {
        return deposits;
    }

    public String getProject_url() {
        return project_url;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProject_description(String project_description) {
        this.project_description = project_description;
    }

    public void setDeposits(float deposits) {
        this.deposits = deposits;
    }

    public void setProject_url(String project_url) {
        this.project_url = project_url;
    }
}
