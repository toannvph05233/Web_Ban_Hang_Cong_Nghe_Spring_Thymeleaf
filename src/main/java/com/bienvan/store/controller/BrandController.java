package com.bienvan.store.controller;

import com.bienvan.store.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bienvan.store.model.Brand;
import com.bienvan.store.payload.response.MessageResponse;
import com.bienvan.store.service.impl.BrandServiceImpl;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    IBrandService brandServiceImpl;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        Brand brand = brandServiceImpl.findById(id);
        if (brand != null) {
            return ResponseEntity.ok(new MessageResponse(0, "Success", brand));
        }
        return ResponseEntity.ok(new MessageResponse(1, "Fail", null));
    }

}
