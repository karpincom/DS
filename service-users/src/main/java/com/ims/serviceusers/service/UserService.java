package com.ims.serviceusers.service;

import com.ims.serviceusers.api.dto.ProjectDTO;
import com.ims.serviceusers.api.dto.SupportDTO;
import com.ims.serviceusers.repo.model.User;
import com.ims.serviceusers.repo.UserRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public final class UserService {
    private final UserRepo userRepo;
//    private final String projectURL = "http://localhost:8081/projects/";
//    private final String supportURL = "http://localhost:8082/supports/";
    private final String projectURL = "http://service-projects:8081/projects/";
    private final String supportURL = "http://service-support:8082/supports/";
    public List<User> fetchAll() {
        return userRepo.findAll();
    }

    public User fetchById(long id) throws IllegalArgumentException {
        final Optional<User> byId = userRepo.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException(String.format("User with id = %d was not found", id));
        return byId.get();
    }
    public long create(String username, String password, String email, String phone) {
        final User user = new User(username, password, email, phone);
        final User save = userRepo.save(user);
        return save.getId();
    }
    public List<ProjectDTO> getUserProjects(long id) {
        final Optional<User> byId = userRepo.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException(String.format("User with id = %d was not found", id));
        final User user = byId.get();
        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<List<ProjectDTO>> response = restTemplate.exchange(projectURL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ProjectDTO>>() {});
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new IllegalArgumentException(String.format("User with id = %d has no projects", user.getId()));
        }
        // response содержит JSON со всеми заказами, нужно отфильтровать те, у которых user_id = id
        List<ProjectDTO> user_projects = new ArrayList<ProjectDTO>();
        for (ProjectDTO project : Objects.requireNonNull(response.getBody())) {
            if (project.getUser_id() == user.getId()) {
                user_projects.add(project);
            }
        }
        return user_projects;
    }
    public List<SupportDTO> getUserSupports(long id) {
        final Optional<User> byId = userRepo.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException(String.format("project with id = %d was not found", id));
        final User user = byId.get();
        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<List<SupportDTO>> response = restTemplate.exchange(supportURL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<SupportDTO>>() {});
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new IllegalArgumentException(String.format("User with id = %d has no supports", user.getId()));
        }
        List<SupportDTO> user_supports = new ArrayList<SupportDTO>();
        for (SupportDTO support : Objects.requireNonNull(response.getBody())) {
            if (support.getUser_id() == user.getId()) {
                user_supports.add(support);
            }
        }
        return user_supports;
    }
    public void update(long id, String username, String password, String email, String phone)
            throws IllegalArgumentException {
        final Optional<User> byId = userRepo.findById(id);
        final User user = byId.get();
        if(username != null && !username.isEmpty()) user.setUsername(username);
        if(password != null && !password.isEmpty()) user.setPassword(password);
        if(email != null && !email.isEmpty()) user.setEmail(email);
        if(phone != null && !phone.isEmpty()) user.setPhone(phone);
        userRepo.save(user);
    }

    public void delete(long id) {
        userRepo.deleteById(id);
    }
}
