package com.ims.serviceprojects.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class ProjectDTO {
    private long user_id;
    private String name;
    private String project_description;
    private float deposits;
    private String project_url;
}