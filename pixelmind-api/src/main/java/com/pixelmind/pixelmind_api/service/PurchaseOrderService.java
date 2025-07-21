package com.pixelmind.pixelmind_api.service;

import com.pixelmind.pixelmind_api.model.User;
import com.pixelmind.pixelmind_api.model.store.NftItem;
import com.pixelmind.pixelmind_api.model.store.PurchaseOrder;
import com.pixelmind.pixelmind_api.model.store.UserNft;
import com.pixelmind.pixelmind_api.repository.NftItemRepository;
import com.pixelmind.pixelmind_api.repository.PurchaseOrderRepository;
import com.pixelmind.pixelmind_api.repository.UserNftRepository;
import com.pixelmind.pixelmind_api.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository repository;
    private final UserRepository userRepository;
    private final NftItemRepository nftItemRepository;
    private final UserNftRepository userNftRepository;
    private final RestTemplate restTemplate;

    // 🔐 Substitua com a URL oficial e suas credenciais reais
    private static final String INTER_PIX_API_BASE = "https://cdpj.partners.bancointer.com.br/pix";
    private static final String CHAVE_PIX = "SEU_UUID_PIX_AQUI";

    // ⚠️ Token de acesso OAuth (deve ser obtido via client_credentials)
    private String accessToken = "TOKEN_DE_ACESSO_OAUTH";

    public PurchaseOrderService(PurchaseOrderRepository repository,  UserNftRepository userNftRepository,UserRepository userRepository, NftItemRepository nftItemRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.nftItemRepository = nftItemRepository;
        this.userNftRepository = userNftRepository;
        this.restTemplate = new RestTemplate();
    }

    public PurchaseOrder createOrder(Long userId, Long nftItemId, BigDecimal amount, String transactionId) {
        PurchaseOrder order = new PurchaseOrder();
        order.setUserId(userId);
        order.setNftItemId(nftItemId);
        order.setAmount(amount);
        order.setStatus("PENDING");
        order.setTransactionId(transactionId);
        order.setCreatedAt(LocalDateTime.now());
        return repository.save(order);
    }

    public Optional<PurchaseOrder> findByTransactionId(String transactionId) {
        return repository.findByTransactionId(transactionId);
    }

    public List<PurchaseOrder> listAll() {
        return repository.findAll();
    }

    public PurchaseOrder updateStatus(Long orderId, String status) {
        PurchaseOrder order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        order.setStatus(status);

        if ("PAID".equalsIgnoreCase(status)) {
            order.setPaidAt(LocalDateTime.now());
        }

        return repository.save(order);
    }

    // 🔹 Gera cobrança PIX no Inter
    public String gerarPix(Long orderId) {
        PurchaseOrder order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        // Monta JSON de cobrança
        String payloadJson = """
            {
              "calendario": {
                "expiracao": 3600
              },
              "valor": {
                "original": "%s"
              },
              "chave": "%s",
              "solicitacaoPagador": "Pagamento do pedido %s",
              "txid": "%s"
            }
            """.formatted(
                order.getAmount().toString(),
                CHAVE_PIX,
                order.getId(),
                order.getTransactionId() // precisa ser único
        );

        String endpoint = INTER_PIX_API_BASE + "/cob";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(payloadJson, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // JSON com qrcode ou location
        } else {
            throw new RuntimeException("Erro ao criar cobrança Pix: " + response.getStatusCode());
        }
    }

    // 🔹 Consulta status da cobrança Pix
    public String consultarStatusPix(Long orderId) {
        PurchaseOrder order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        String txid = order.getTransactionId();

        String endpoint = INTER_PIX_API_BASE + "/cob/" + txid;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                endpoint,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // Retorna JSON com status
        } else {
            throw new RuntimeException("Erro ao consultar status Pix: " + response.getStatusCode());
        }
    }

    public boolean verificarPagamento(Long orderId) {
        PurchaseOrder order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        return "PAID".equalsIgnoreCase(order.getStatus());
    }

    public PurchaseOrder updateStatusByTransactionId(String txid, String newStatus) {
        PurchaseOrder order = repository.findByTransactionId(txid)
                .orElseThrow(() -> new RuntimeException("Pedido com txid não encontrado: " + txid));

        order.setStatus(newStatus);

        if ("PAID".equalsIgnoreCase(newStatus)) {
            order.setPaidAt(LocalDateTime.now());

            // Associa NFT ao usuário (se ainda não tiver)
            Optional<User> userOptional = userRepository.findById(order.getUserId());
            Optional<NftItem> nftOptional = nftItemRepository.findById(order.getNftItemId());

            if (userOptional.isPresent() && nftOptional.isPresent()) {
                User user = userOptional.get();
                NftItem nft = nftOptional.get();

                boolean alreadyHas = userNftRepository.existsByUserAndNftItem(user, nft);
                if (!alreadyHas) {
                    UserNft userNft = new UserNft();
                    userNft.setUser(user);
                    userNft.setNftItem(nft);
                    userNftRepository.save(userNft);

                    user.setAge(99);
                    userRepository.save(user);

                }
            } else {
                throw new RuntimeException("Usuário ou NFT não encontrado para associar ao pedido");
            }
        }

        return repository.save(order);
    }


}
