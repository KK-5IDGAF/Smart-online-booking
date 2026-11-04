package com.smartbooking.mapper;

import com.smartbooking.dto.resource.ResourceRequest;
import com.smartbooking.dto.resource.ResourceResponse;
import com.smartbooking.entity.Resource;
import org.springframework.stereotype.Component;

@Component
public class ResourceMapper {

    public Resource toEntity(ResourceRequest request) {
        return Resource.builder()
                .name(request.getName())
                .type(request.getType())
                .description(request.getDescription())
                .capacity(request.getCapacity())
                .build();
    }

    public void update(Resource resource, ResourceRequest request) {
        resource.setName(request.getName());
        resource.setType(request.getType());
        resource.setDescription(request.getDescription());
        resource.setCapacity(request.getCapacity());
    }

    public ResourceResponse toResponse(Resource resource) {
        return ResourceResponse.builder()
                .id(resource.getId())
                .name(resource.getName())
                .type(resource.getType())
                .description(resource.getDescription())
                .capacity(resource.getCapacity())
                .build();
    }
}
