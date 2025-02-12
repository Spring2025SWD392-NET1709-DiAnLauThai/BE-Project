package com.be.back_end.controller;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.TshirtsDTO;
import com.be.back_end.model.Tshirts;
import com.be.back_end.service.AccountService.IAccountService;
import com.be.back_end.service.TshirtsService.ITshirtsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tshirts")
public class TshirtsController {
    private final ITshirtsService tshirtsService;
    public TshirtsController(ITshirtsService tshirtsService) {
        this.tshirtsService = tshirtsService;

    }
    @PostMapping
    public ResponseEntity<TshirtsDTO> createTshirt(@RequestBody TshirtsDTO tshirt) {
        TshirtsDTO createdTshirt = tshirtsService.createTshirt(tshirt);
        System.out.println("Tshirt created successfully.");
        return ResponseEntity.ok(createdTshirt);
    }
    @GetMapping
    public ResponseEntity<List<TshirtsDTO>> getAllTshirt() {
        List<TshirtsDTO> tshirts = tshirtsService.getAllTshirts();
        if (tshirts.isEmpty()) {
            System.out.println("No tshirts found.");
        }
        return ResponseEntity.ok(tshirts);
    }

}