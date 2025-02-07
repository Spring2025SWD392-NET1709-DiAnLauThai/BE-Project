package com.be.back_end.controller;

import com.be.back_end.Exception.BadRequestException;
import com.be.back_end.model.Tshirts;
import com.be.back_end.service.ImplementService.TshirtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tshirt")
public class TshirtController {

    private final TshirtService tshirtService;

    public TshirtController(TshirtService tshirtService) {
        this.tshirtService = tshirtService;
    }

    @GetMapping()
    public List<Tshirts> getAll(){
        return tshirtService.getAll();
    }

    @GetMapping("/{tshirtID}")
    public Tshirts getById(@PathVariable int id){
        return tshirtService.getById(id);
    }

    @PostMapping("")
    public Tshirts add(@RequestBody Tshirts tshirt){
        tshirt.setId(0);
        return  tshirtService.save(tshirt);
    }

    @PutMapping("")
    public Tshirts update(@RequestBody Tshirts tshirt){
        return  tshirtService.save(tshirt);
    }

    @DeleteMapping("/{tshirtID}")
    public String deleteById(@PathVariable int id){
        Tshirts country= tshirtService.getById(id);
        if(country==null){
            throw new BadRequestException("Country "+id+" not found");
        }
        tshirtService.removeById(id);
        return "Country "+id+" deleted!!";
    }
}
