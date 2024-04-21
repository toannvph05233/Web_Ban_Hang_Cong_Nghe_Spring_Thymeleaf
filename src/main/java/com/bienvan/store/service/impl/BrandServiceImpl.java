package com.bienvan.store.service.impl;

import java.util.List;
import java.util.Optional;

import com.bienvan.store.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bienvan.store.model.Brand;
import com.bienvan.store.repository.BrandRepository;

@Service
public class BrandServiceImpl implements IBrandService {
    @Autowired
    BrandRepository brandRepository;

    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand delete(Brand brand) {
        brandRepository.delete(brand);
        return brand;
    }

    public Brand findByName(String name) {
        return brandRepository.findByName(name).get();
    }

    public Brand findById(Long id) {
        if (id == null) {
            return null;
        }
        Optional<Brand> brandOptional = brandRepository.findById(id);
        if (brandOptional.isPresent()) {
            return brandOptional.get();
        }
        return null;
    }

}
