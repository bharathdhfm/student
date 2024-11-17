package com.bharath.lab8.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @jakarta.persistence.Transient
    private String captcha; // User-provided CAPTCHA input
    @jakarta.persistence.Transient
    private String hidden;  // Expected CAPTCHA answer
    @jakarta.persistence.Transient
    private String image;   // CAPTCHA image in Base64

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCaptcha() { return captcha; }
    public void setCaptcha(String captcha) { this.captcha = captcha; }
    public String getHidden() { return hidden; }
    public void setHidden(String hidden) { this.hidden = hidden; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
