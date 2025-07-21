package com.pixelmind.pixelmind_api.service;

import com.pixelmind.pixelmind_api.dto.store.NftCollectionDTO;
import com.pixelmind.pixelmind_api.model.store.NftCollection;
import com.pixelmind.pixelmind_api.repository.NftCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NftCollectionService {

    @Autowired
    private NftCollectionRepository repository;

    public List<NftCollection> listAll() {
        return repository.findAll();
    }

    public NftCollection create(NftCollectionDTO dto) {
        NftCollection entity = new NftCollection();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setImageUrl(dto.getImageUrl());
        return repository.save(entity);
    }

    public NftCollection update(Long id, NftCollectionDTO dto) {
        NftCollection entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found"));
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setImageUrl(dto.getImageUrl());
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

