package com.perfulandia.pedido.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.pedido.model.Detalle;
import com.perfulandia.pedido.repository.DetalleRepository;

@Service
public class DetalleService {
    @Autowired
    private DetalleRepository detalleRepository ;

    public List<Detalle> detalles(){
        return detalleRepository.findAll();
    }

    public Detalle guardar(Detalle detalle){
        return detalleRepository.save(detalle);
    }

}
