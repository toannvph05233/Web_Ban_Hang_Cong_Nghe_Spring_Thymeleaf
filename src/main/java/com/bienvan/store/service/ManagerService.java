package com.bienvan.store.service;

import java.util.List;

public interface ManagerService<E>{
    E findById(Long id);
    List<E> findAll();
    E save(E e);
    E delete(E e);
}
