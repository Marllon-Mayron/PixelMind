package com.pixelmind.pixelmind_api.service;

import com.pixelmind.pixelmind_api.dto.StoreItemDTO;
import com.pixelmind.pixelmind_api.dto.StorePromotionDTO;
import com.pixelmind.pixelmind_api.model.store.NftItem;
import com.pixelmind.pixelmind_api.model.store.StoreItem;
import com.pixelmind.pixelmind_api.model.store.StorePromotion;
import com.pixelmind.pixelmind_api.repository.NftItemRepository;
import com.pixelmind.pixelmind_api.repository.StoreItemRepository;
import com.pixelmind.pixelmind_api.repository.StorePromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreService {

    private final StoreItemRepository storeItemRepository;
    private final StorePromotionRepository storePromotionRepository;
    private final NftItemRepository nftItemRepository;

    public StoreService(StoreItemRepository storeItemRepository,
                        StorePromotionRepository storePromotionRepository,
                        NftItemRepository nftItemRepository) {
        this.storeItemRepository = storeItemRepository;
        this.storePromotionRepository = storePromotionRepository;
        this.nftItemRepository = nftItemRepository;
    }

    // Listar todos StoreItems
    public List<StoreItemDTO> listStoreItems() {
        return storeItemRepository.findAll().stream()
                .map(this::toStoreItemDTO)
                .collect(Collectors.toList());
    }

    // Criar ou atualizar StoreItem
    @Transactional
    public StoreItemDTO saveStoreItem(StoreItemDTO dto) {
        Optional<NftItem> nftOpt = nftItemRepository.findById(dto.getNftItemId());
        if (nftOpt.isEmpty()) {
            throw new RuntimeException("NFT item não encontrado");
        }
        NftItem nftItem = nftOpt.get();

        StoreItem item = dto.getId() != null
                ? storeItemRepository.findById(dto.getId()).orElse(new StoreItem())
                : new StoreItem();

        item.setNftItem(nftItem);
        item.setPrice(dto.getPrice());
        item.setOnPromotion(dto.isOnPromotion());
        item.setPromotionalPrice(dto.getPromotionalPrice());
        item.setPromotionStart(dto.getPromotionStart());
        item.setPromotionEnd(dto.getPromotionEnd());
        item.setStatus(dto.getStatus());
        // Os campos createdAt e updatedAt já são definidos na entidade com valor padrão

        StoreItem saved = storeItemRepository.save(item);
        return toStoreItemDTO(saved);
    }

    // Listar todas promoções
    public List<StorePromotionDTO> listStorePromotions() {
        return storePromotionRepository.findAll().stream()
                .map(this::toStorePromotionDTO)
                .collect(Collectors.toList());
    }

    // Criar ou atualizar promoção
    @Transactional
    public StorePromotionDTO saveStorePromotion(StorePromotionDTO dto) {
        StorePromotion promo = dto.getId() != null
                ? storePromotionRepository.findById(dto.getId()).orElse(new StorePromotion())
                : new StorePromotion();

        promo.setName(dto.getName());
        promo.setDescription(dto.getDescription());
        promo.setType(dto.getType());
        promo.setMinQuantity(dto.getMinQuantity());
        promo.setDiscountPercentage(dto.getDiscountPercentage());
        promo.setDiscountValue(dto.getDiscountValue());
        promo.setStartDate(dto.getStartDate());
        promo.setEndDate(dto.getEndDate());
        promo.setActive(dto.isActive());

        // Atualiza lista de itens
        if (dto.getStoreItemIds() != null) {
            List<StoreItem> items = storeItemRepository.findAllById(dto.getStoreItemIds());
            promo.setItems(items);
        }

        StorePromotion saved = storePromotionRepository.save(promo);
        return toStorePromotionDTO(saved);
    }

    // Mapeamentos básicos

    private StoreItemDTO toStoreItemDTO(StoreItem item) {
        StoreItemDTO dto = new StoreItemDTO();
        dto.setId(item.getId());
        dto.setNftItemId(item.getNftItem().getId());
        dto.setPrice(item.getPrice());
        dto.setOnPromotion(item.isOnPromotion());
        dto.setPromotionalPrice(item.getPromotionalPrice());
        dto.setPromotionStart(item.getPromotionStart());
        dto.setPromotionEnd(item.getPromotionEnd());
        dto.setStatus(item.getStatus());
        dto.setCreatedAt(item.getCreatedAt());
        return dto;
    }

    private StorePromotionDTO toStorePromotionDTO(StorePromotion promo) {
        StorePromotionDTO dto = new StorePromotionDTO();
        dto.setId(promo.getId());
        dto.setName(promo.getName());
        dto.setDescription(promo.getDescription());
        dto.setType(promo.getType());
        dto.setMinQuantity(promo.getMinQuantity());
        dto.setDiscountPercentage(promo.getDiscountPercentage());
        dto.setDiscountValue(promo.getDiscountValue());
        dto.setStartDate(promo.getStartDate());
        dto.setEndDate(promo.getEndDate());
        dto.setActive(promo.isActive());
        dto.setStoreItemIds(promo.getItems() != null
                ? promo.getItems().stream().map(i -> i.getId()).toList()
                : List.of());
        return dto;
    }

    public void deleteStoreItem(Long id) {
        storeItemRepository.deleteById(id);
    }

    public void deleteStorePromotion(Long id) {
        storePromotionRepository.deleteById(id);
    }
}
