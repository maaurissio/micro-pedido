package com.perfulandia.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfulandia.pedido.model.Detalle;

@Repository
public interface DetalleRepository extends JpaRepository<Detalle, Integer> {
    
}
