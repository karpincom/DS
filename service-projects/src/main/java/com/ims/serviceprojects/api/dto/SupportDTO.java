package com.ims.serviceprojects.api.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class SupportDTO {
    private long user_id;
    private long project_id;
    private String feedback;

}