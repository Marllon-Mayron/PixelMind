package com.pixelmind.pixelmind_api.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WebhookService {

    private final MercadoPagoService mercadoPagoClient;

    public WebhookService(MercadoPagoService mercadoPagoClient) {
        this.mercadoPagoClient = mercadoPagoClient;
    }

    public void processWebhook(Map<String, Object> payload) {
        System.out.println("Recebeu webhook: " + payload);

        String type = (String) payload.get("type");

        if ("payment".equals(type)) {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            Long paymentId = Long.valueOf(data.get("id").toString());

            Map<String, Object> paymentDetails = mercadoPagoClient.getPaymentDetails(paymentId);
            String status = (String) paymentDetails.get("status");

            System.out.println("Pagamento ID " + paymentId + " com status: " + status);

            // Aqui você pode usar o ID do pedido (talvez esteja em "external_reference")
            // e atualizar o banco de dados, por exemplo:
            // if (status.equals("approved")) { ... atualizar pedido como pago ... }
        }
    }
}
