package com.bienvan.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bienvan.store.model.Color;
import com.bienvan.store.repository.ColorRepository;

@Service
public class ColorService {
    @Autowired
    ColorRepository colorRepository;

    public Color findByName(String name){
        return colorRepository.findByName(name).get();
    }

    public List<Color> getColors(){
        return colorRepository.findAll();
    }

    public Color findById(Long id){
        if(id == null){
            return null;
        }
        return colorRepository.findById(id).orElse(null);
    }
}
