package com.hanss.auth.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class JwtResponse {
  private String token;
  private String username;
  private String email;
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime  expires;
  private List<String> roles;

  public JwtResponse(String accessToken, String username, String email, LocalDateTime  expires, List<String> roles) {
    this.token = accessToken;
    this.username = username;
    this.email = email;
    this.expires = expires;
    this.roles = roles;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public LocalDateTime getExpires() {
    return expires;
  }

  public void setExpires(LocalDateTime expires) {
    this.expires = expires;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }


}
