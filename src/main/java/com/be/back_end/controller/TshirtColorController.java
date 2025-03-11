package com.be.back_end.controller;

import com.be.back_end.dto.request.TshirtColorDTO;
import com.be.back_end.service.TshirtColorService.ITshirtColorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tshirtcolors")
public class TshirtColorController {
    private final ITshirtColorService tshirtColorService;

    public TshirtColorController(ITshirtColorService tshirtColorService) {
        this.tshirtColorService = tshirtColorService;
    }

    @PostMapping
    public ResponseEntity<String> addTshirtColor(@RequestBody TshirtColorDTO dto) {
        return tshirtColorService.createtshirtcolor(dto) ?
                ResponseEntity.ok("Tshirt color added") :
                ResponseEntity.badRequest().body("Failed to add tshirt color");
    }
}