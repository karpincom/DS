package com.ims.serviceprojects.api;

import com.ims.serviceprojects.api.dto.ProjectDTO;
import com.ims.serviceprojects.api.dto.SupportDTO;
import com.ims.serviceprojects.repo.model.Project;
import com.ims.serviceprojects.service.ProjectService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Project>> index() {
        final List<Project> projects = projectService.fetchAll();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> show(@PathVariable long id) {
        try {
            final Project project = projectService.fetchById(id);
            return ResponseEntity.ok(project);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/supports")
    public ResponseEntity<List<SupportDTO>> showSupportsById(@PathVariable long id) {
        try {
            final List<SupportDTO> supports = projectService.getprojectSupports(id);

            return ResponseEntity.ok(supports);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<JSONObject> create(@RequestBody ProjectDTO project) {
        final long user_id = project.getUser_id();
        final String name = project.getName();
        final String description = project.getProject_description();
        final float deposits = project.getDeposits();
        final String project_url = project.getProject_url();
        try {
            final long id = projectService.create(user_id, name, description, deposits, project_url);
            final String location = String.format("/projects/%d", id);
            return ResponseEntity.created(URI.create(location)).build();
        } catch (IllegalArgumentException e) {
            JSONObject response = new JSONObject();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable long id, @RequestBody ProjectDTO project) {
        final String description = project.getProject_description();
        final float price = project.getDeposits();
        final String project_url = project.getProject_url();
        try {
            projectService.update(id, description, price, project_url);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}