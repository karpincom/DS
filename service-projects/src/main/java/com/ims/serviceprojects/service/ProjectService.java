package com.ims.serviceprojects.service;

import com.ims.serviceprojects.api.dto.SupportDTO;
import com.ims.serviceprojects.repo.ProjectRepo;
import com.ims.serviceprojects.repo.model.Project;

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
public final class ProjectService {
    private final ProjectRepo projectRepo;
    private final String supportURL = "http://service-support:8082/supports/";
//    private final String supportURL = "http://localhost/supports/";
    public List<Project> fetchAll() {
        return projectRepo.findAll();
    }

    public Project fetchById(long id) throws IllegalArgumentException {
        final Optional<Project> byId = projectRepo.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException(String.format("Project with id = %d was not found", id));
        return byId.get();
    }
    public long create(long user_id, String name, String project_description, float deposits, String project_url) {
        final Project project = new Project(user_id, name, project_description, deposits, project_url);
        final Project save = projectRepo.save(project);
        return save.getId();
    }
    public List<SupportDTO> getprojectSupports(long id) {
        final Optional<Project> byId = projectRepo.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException(String.format("Project with id = %d was not found", id));
        final Project project = byId.get();
        final RestTemplate restTemplate = new RestTemplate();

        final ResponseEntity<List<SupportDTO>> response = restTemplate.exchange(supportURL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<SupportDTO>>() {});

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new IllegalArgumentException(String.format("Project with id = %d has no supports", project.getId()));
        }
        List<SupportDTO> project_supports = new ArrayList<SupportDTO>();
        for (SupportDTO support : Objects.requireNonNull(response.getBody())) {
            if (support.getProject_id() == project.getId()) {
                project_supports.add(support);
            }
        }
        return project_supports;
    }
    public void update(long id, String project_description, Float deposits, String project_url)
            throws IllegalArgumentException {
        final Optional<Project> byId = projectRepo.findById(id);
        final Project project = byId.get();
        if(project_description != null && !project_description.isEmpty()) project.setProject_description(project_description);
        if(deposits != null) project.setDeposits(deposits);
        if(project_url != null && !project_url.isEmpty()) project.setProject_url(project_url);
        projectRepo.save(project);
    }

    public void delete(long id) {
        projectRepo.deleteById(id);
    }
}
