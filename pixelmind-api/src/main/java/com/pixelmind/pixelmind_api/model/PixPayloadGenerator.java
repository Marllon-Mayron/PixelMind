package com.pixelmind.pixelmind_api.model;

import java.text.Normalizer;

public class PixPayloadGenerator {

    public static String generatePayload(String key, String merchantName, String merchantCity, String amount, String txid) {
        merchantName = normalizeString(merchantName).toUpperCase();
        merchantCity = normalizeString(merchantCity).toUpperCase();

        if (merchantName.length() > 25) {
            merchantName = merchantName.substring(0, 25);
        }
        if (merchantCity.length() > 15) {
            merchantCity = merchantCity.substring(0, 15);
        }

        String payloadFormatIndicator = "000201";

        String guiTag = "00";
        String guiValue = "BR.GOV.BCB.PIX";
        String guiField = guiTag + String.format("%02d", guiValue.length()) + guiValue;

        String pixKeyTag = "01";
        String pixKeyValue = key;
        String pixKeyField = pixKeyTag + String.format("%02d", pixKeyValue.length()) + pixKeyValue;

        String merchantAccountInfo = guiField + pixKeyField;
        String merchantAccountInfoTemplate = "26" + String.format("%02d", merchantAccountInfo.length()) + merchantAccountInfo;

        String merchantCategoryCode = "52040000";
        String transactionCurrency = "5303986";

        String transactionAmount = "";
        if (amount != null && !amount.isEmpty()) {
            amount = amount.replace(',', '.');
            transactionAmount = "54" + String.format("%02d", amount.length()) + amount;
        }

        String countryCode = "5802BR";
        String merchantNameField = "59" + String.format("%02d", merchantName.length()) + merchantName;
        String merchantCityField = "60" + String.format("%02d", merchantCity.length()) + merchantCity;
        String additionalDataFieldTemplate = "62" + String.format("%02d", txid.length() + 4) + "05" + "01" + txid;

        String payloadWithoutCRC = payloadFormatIndicator +
                merchantAccountInfoTemplate +
                merchantCategoryCode +
                transactionCurrency +
                transactionAmount +
                countryCode +
                merchantNameField +
                merchantCityField +
                additionalDataFieldTemplate;

        String payloadToCalculateCRC = payloadWithoutCRC + "6304";
        String crc = calculateCRC(payloadToCalculateCRC);
        return payloadWithoutCRC + "6304" + crc;
    }


    // Função para normalizar string removendo acentos
    private static String normalizeString(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("[^\\p{ASCII}]", "");
    }

    // Cálculo do CRC16-CCITT
    public static String calculateCRC(String payload) {
        int crc = 0xFFFF;
        for (int i = 0; i < payload.length(); i++) {
            crc ^= (payload.charAt(i) << 8);
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc <<= 1;
                }
                crc &= 0xFFFF;
            }
        }
        return String.format("%04X", crc);
    }
}

