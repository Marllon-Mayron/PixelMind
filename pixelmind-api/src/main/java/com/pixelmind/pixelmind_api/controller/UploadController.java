package com.pixelmind.pixelmind_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class UploadController {

    private final Path rootLocation = Paths.get("uploads");

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
            String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path destinationFile = rootLocation.resolve(filename).normalize().toAbsolutePath();
            file.transferTo(destinationFile);

            // URL pública para acessar a imagem
            String url = "/uploads/" + filename;

            return ResponseEntity.ok(Collections.singletonMap("url", url));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
