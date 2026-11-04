package com.smartbooking.dto.resource;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResourceResponse {
    private Long id;
    private String name;
    private String type;
    private String description;
    private Integer capacity;
}
