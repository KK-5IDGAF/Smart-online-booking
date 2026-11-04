package com.smartbooking.service;

import com.smartbooking.dto.resource.ResourceRequest;
import com.smartbooking.dto.resource.ResourceResponse;
import com.smartbooking.entity.Resource;
import com.smartbooking.exception.ResourceNotFoundException;
import com.smartbooking.mapper.ResourceMapper;
import com.smartbooking.repository.ResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;

    public ResourceService(ResourceRepository resourceRepository, ResourceMapper resourceMapper) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }

    public List<ResourceResponse> findAll(String search) {
        if (search == null || search.isBlank()) {
            return resourceRepository.findAll().stream().map(resourceMapper::toResponse).toList();
        }
        return resourceRepository
                .findByNameContainingIgnoreCaseOrTypeContainingIgnoreCase(search, search)
                .stream()
                .map(resourceMapper::toResponse)
                .toList();
    }

    public ResourceResponse findById(Long id) {
        return resourceMapper.toResponse(getResource(id));
    }

    @Transactional
    public ResourceResponse create(ResourceRequest request) {
        Resource resource = resourceMapper.toEntity(request);
        return resourceMapper.toResponse(resourceRepository.save(resource));
    }

    @Transactional
    public ResourceResponse update(Long id, ResourceRequest request) {
        Resource resource = getResource(id);
        resourceMapper.update(resource, request);
        return resourceMapper.toResponse(resourceRepository.save(resource));
    }

    @Transactional
    public void delete(Long id) {
        Resource resource = getResource(id);
        resourceRepository.delete(resource);
    }

    public Resource getResource(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found: " + id));
    }
}
