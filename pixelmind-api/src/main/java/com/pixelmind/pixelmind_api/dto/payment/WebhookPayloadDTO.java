package com.pixelmind.pixelmind_api.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WebhookPayloadDTO {
    private String action;

    @JsonProperty("api_version")
    private String apiVersion;

    private WebhookDataDTO data;

    @JsonProperty("date_created")
    private String dateCreated;

    private Long id;

    @JsonProperty("live_mode")
    private boolean liveMode;

    private String type;

    @JsonProperty("user_id")
    private String userId;
}
