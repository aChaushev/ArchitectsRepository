package aChaushev.architects.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity{

  private String username;

  @Column(unique = true)
  private String email;

  private String password;

  @OneToMany(mappedBy = "architect", fetch = FetchType.EAGER)
  private Set<Project> projects;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private Set<Event> events;


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "user_favorite_projects",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "project_id")
  )
  private Set<Project> favouriteProjects;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "users_roles",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  private List<UserRole> roles = new ArrayList<>();

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<UserRole> getRoles() {
    return roles;
  }

  public void setRoles(List<UserRole> roles) {
    this.roles = roles;
  }

  public Set<Project> getProjects() {
    return projects;
  }

  public void setProjects(Set<Project> projects) {
    this.projects = projects;
  }

  public Set<Project> getFavouriteProjects() {
    return favouriteProjects;
  }

  public void setFavouriteProjects(Set<Project> favouriteProjects) {
    this.favouriteProjects = favouriteProjects;
  }

  public void addFavouriteProject(Project project) {
    this.favouriteProjects.add(project);
  }

  public void removeFavouriteProject(Project project) {
    this.favouriteProjects.remove(project);
  }

  public Set<Event> getEvents() {
    return events;
  }

  public void setEvents(Set<Event> events) {
    this.events = events;
  }

}

