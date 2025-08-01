package com.pixelmind.pixelmind_api.service;

import com.pixelmind.pixelmind_api.dto.payment.CreatePixPaymentRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    private final RestTemplate restTemplate;
    private final PurchaseOrderService purchaseOrderService;

    public MercadoPagoService(RestTemplate restTemplate, PurchaseOrderService purchaseOrderService) {
        this.restTemplate = restTemplate;
        this.purchaseOrderService = purchaseOrderService;
    }

    // Método genérico para criar pagamento
    public Map<String, Object> criarPagamento(Map<String, Object> payload) {
        String url = "https://api.mercadopago.com/v1/payments";
        HttpHeaders headers = new HttpHeaders();
        String externalReference = (String) payload.get("external_reference");
        headers.set("X-Idempotency-Key", externalReference);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);


        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            System.out.println("🔄 Enviando payload para Mercado Pago:");
            System.out.println(payload);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

            System.out.println("✅ Resposta recebida com status: " + response.getStatusCode());
            System.out.println("📦 Corpo da resposta: " + response.getBody());

            return response.getBody();

        } catch (Exception e) {
            System.out.println("❌ Erro ao tentar criar pagamento no Mercado Pago");
            e.printStackTrace();

            // Opcional: lançar como RuntimeException para aparecer no log geral do Spring
            throw new RuntimeException("Erro ao tentar criar pagamento no Mercado Pago", e);
        }
    }


    public Map<String, Object> criarPagamentoPix(CreatePixPaymentRequestDTO dto) {
        Map<String, Object> payload = Map.of(
                "transaction_amount", dto.getTransaction_amount(),
                "payment_method_id", dto.getPayment_method_id(),
                "payer", Map.of("email", dto.getEmail()),
                "description", dto.getDescription(),
                "external_reference", dto.getExternal_reference()
        );
        return criarPagamento(payload);
    }

    // Consulta detalhes do pagamento
    public Map<String, Object> getPaymentDetails(Long paymentId) {
        String url = "https://api.mercadopago.com/v1/payments/" + paymentId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set("Accept", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> responseBody = response.getBody();

        if (responseBody != null && responseBody.containsKey("external_reference") && responseBody.containsKey("status")) {
            String externalReference = responseBody.get("external_reference").toString();
            String status = responseBody.get("status").toString();

            System.out.println("🟢 external_reference (txid): " + externalReference);
            System.out.println("📦 Status do pagamento: " + status);

            if ("approved".equalsIgnoreCase(status)) {
                purchaseOrderService.updateStatusByTransactionId(externalReference, "PAID");

            } else {
                System.out.println("⛔ Pagamento ainda não aprovado. Nenhuma ação tomada.");
            }
        }

        return responseBody;
    }

}
