package com.bienvan.store.service;

import com.bienvan.store.model.Brand;

public interface IBrandService extends ManagerService<Brand> {
    Brand findByName(String name);
}
