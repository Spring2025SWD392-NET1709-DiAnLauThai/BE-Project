package com.be.back_end.controller;

import com.be.back_end.dto.TShirtSizeDTO;
import com.be.back_end.service.TShirtSizeService.ITShirtSizeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tshirtsizes")
public class TShirtSizeController {
    private final ITShirtSizeService tshirtSizeService;

    public TShirtSizeController(ITShirtSizeService tshirtSizeService) {
        this.tshirtSizeService = tshirtSizeService;
    }

    @PostMapping
    public ResponseEntity<String> addTShirtSize(@RequestBody TShirtSizeDTO dto) {
        return tshirtSizeService.createTShirtSize(dto) ?
                ResponseEntity.ok("TShirt size added") :
                ResponseEntity.badRequest().body("Failed to add TShirt size");
    }
}