package com.pixelmind.pixelmind_api.service;

import com.pixelmind.pixelmind_api.dto.NftItemDTO;
import com.pixelmind.pixelmind_api.dto.NftItemWithDateDTO;
import com.pixelmind.pixelmind_api.enums.NftTier;
import com.pixelmind.pixelmind_api.model.store.NftItem;
import com.pixelmind.pixelmind_api.model.User;
import com.pixelmind.pixelmind_api.model.store.UserNft;
import com.pixelmind.pixelmind_api.repository.NftItemRepository;
import com.pixelmind.pixelmind_api.repository.UserNftRepository;
import com.pixelmind.pixelmind_api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NftItemService {

    private final NftItemRepository nftItemRepository;
    private final UserRepository userRepository;
    private final UserNftRepository userNftRepository;

    private final String uploadDir = "uploads"; // pasta local para imagens

    public NftItemService(NftItemRepository nftItemRepository, UserRepository userRepository, UserNftRepository userNftRepository) {
        this.nftItemRepository = nftItemRepository;
        this.userRepository = userRepository;
        this.userNftRepository = userNftRepository;
    }

    // Criar NFT com DTO direto (sem imagem)
    public NftItemDTO create(NftItemDTO dto) {
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        NftItem nft = dto.toEntity(owner);
        NftItem saved = nftItemRepository.save(nft);
        return NftItemDTO.fromEntity(saved);
    }

    public NftItemDTO createWithImage(String title, BigDecimal price, boolean forSale, Long ownerId, Long collectionId, String tier, MultipartFile imageFile) {
        String imageUrl = salvarImagem(imageFile);

        NftItem nft = new NftItem();
        nft.setTitle(title);
        nft.setPrice(price);
        nft.setForSale(forSale);
        nft.setImageUrl(imageUrl);

        // Conversão segura do tier
        if (tier != null) {
            try {
                nft.setTier(NftTier.valueOf(tier));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Tier inválido: " + tier);
            }
        } else {
            nft.setTier(NftTier.COMMON); // padrão
        }

        if (ownerId != null) {
            User owner = userRepository.findById(ownerId)
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            nft.setOwner(owner);
        } else {
            nft.setOwner(null);
        }

        nft.setCollectionId(collectionId);

        NftItem saved = nftItemRepository.save(nft);
        return NftItemDTO.fromEntity(saved);
    }



    public NftItemDTO updateWithImage(Long id, String title, BigDecimal price, boolean forSale, Long ownerId, Long collectionId, String tier, MultipartFile imageFile) {
        NftItem existing = nftItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NFT not found"));

        if (ownerId != null) {
            User owner = userRepository.findById(ownerId)
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            existing.setOwner(owner);
        }

        existing.setTitle(title);
        existing.setPrice(price);
        existing.setForSale(forSale);
        existing.setCollectionId(collectionId);
        if (tier != null) {
            try {
                existing.setTier(NftTier.valueOf(tier)); // Conversão de String para Enum
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Tier inválido: " + tier);
            }
        }


        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = salvarImagem(imageFile);
            existing.setImageUrl(imageUrl);
        }

        NftItem updated = nftItemRepository.save(existing);
        return NftItemDTO.fromEntity(updated);
    }


    public void delete(Long id) {
        nftItemRepository.deleteById(id);
    }

    public Optional<NftItemDTO> findById(Long id) {
        return nftItemRepository.findById(id).map(NftItemDTO::fromEntity);
    }

    public List<NftItemDTO> listAll() {
        return nftItemRepository.findAll().stream()
                .map(NftItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private String salvarImagem(MultipartFile imageFile) {
        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filename = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path filepath = Paths.get(uploadDir, filename);

            Files.write(filepath, imageFile.getBytes());

            return "http://localhost:8080/uploads/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem", e);
        }
    }

    public List<NftItemDTO> listAvailableForSale(boolean forSale) {
        return nftItemRepository.findByForSale(forSale).stream()
                .map(NftItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<NftItemWithDateDTO> findNftsByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<UserNft> userNfts = userNftRepository.findAllByUserIdWithNft(user.getId());

        return userNfts.stream()
                .map(u -> new NftItemWithDateDTO(u.getNftItem(), u.getEarnedAt()))
                .toList();
    }


}
