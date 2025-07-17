package com.pixelmind.pixelmind_api.controller;

import com.pixelmind.pixelmind_api.dto.NftItemDTO;
import com.pixelmind.pixelmind_api.dto.NftItemWithDateDTO;
import com.pixelmind.pixelmind_api.model.User;
import com.pixelmind.pixelmind_api.model.store.NftItem;
import com.pixelmind.pixelmind_api.model.store.UserNft;
import com.pixelmind.pixelmind_api.repository.UserRepository;
import com.pixelmind.pixelmind_api.service.NftItemService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/nftitems")
public class NftItemController {

    private final NftItemService nftItemService;
    private final UserRepository userRepository;

    public NftItemController(NftItemService nftItemService, UserRepository userRepository) {
        this.nftItemService = nftItemService;
        this.userRepository = userRepository;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NftItemDTO> createNft(
            @RequestParam("title") String title,
            @RequestParam("price") BigDecimal price,
            @RequestParam("forSale") boolean forSale,
            @RequestParam(value = "ownerId", required = false) Long ownerId,
            @RequestParam(value = "collectionId", required = false) Long collectionId,
            @RequestParam(value = "tier", required = false) String tier, // NOVO
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        NftItemDTO saved = nftItemService.createWithImage(title, price, forSale, ownerId, collectionId, tier, imageFile);
        return ResponseEntity.ok(saved);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NftItemDTO> update(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("price") BigDecimal price,
            @RequestParam("forSale") boolean forSale,
            @RequestParam(value = "ownerId", required = false) Long ownerId,
            @RequestParam(value = "collectionId", required = false) Long collectionId,
            @RequestParam(value = "tier", required = false) String tier, // NOVO
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        NftItemDTO updated = nftItemService.updateWithImage(id, title, price, forSale, ownerId, collectionId, tier, imageFile);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<NftItemDTO>> listAll() {
        return ResponseEntity.ok(nftItemService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NftItemDTO> getById(@PathVariable Long id) {
        return nftItemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(params = "forSale")
    public ResponseEntity<List<NftItemDTO>> listAvailableForSale(@RequestParam boolean forSale) {
        return ResponseEntity.ok(nftItemService.listAvailableForSale(forSale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        nftItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/nfts")
    public ResponseEntity<List<NftItemWithDateDTO>> getUserNfts(Principal principal) {
        String email = principal.getName();
        List<NftItemWithDateDTO> nfts = nftItemService.findNftsByUserEmail(email);
        return ResponseEntity.ok(nfts);
    }




}
