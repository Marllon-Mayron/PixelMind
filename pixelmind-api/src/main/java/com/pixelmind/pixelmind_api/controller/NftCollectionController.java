package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.dto.NftCollectionDTO;
import com.pixelmind.pixelmind_api.model.store.NftCollection;
import com.pixelmind.pixelmind_api.service.NftCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nft-collections")
public class NftCollectionController {

    @Autowired
    private NftCollectionService service;

    @GetMapping
    public List<NftCollectionDTO> list() {
        return service.listAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @PostMapping
    public ResponseEntity<NftCollectionDTO> create(@RequestBody NftCollectionDTO dto) {
        NftCollection created = service.create(dto);
        return ResponseEntity.ok(toDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NftCollectionDTO> update(@PathVariable Long id, @RequestBody NftCollectionDTO dto) {
        NftCollection updated = service.update(id, dto);
        return ResponseEntity.ok(toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private NftCollectionDTO toDTO(NftCollection entity) {
        NftCollectionDTO dto = new NftCollectionDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setImageUrl(entity.getImageUrl());
        return dto;
    }
}
