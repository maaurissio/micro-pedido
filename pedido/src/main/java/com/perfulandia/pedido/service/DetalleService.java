package com.perfulandia.pedido.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.pedido.model.Detalle;
import com.perfulandia.pedido.repository.DetalleRepository;
import com.perfulandia.pedido.repository.PedidoRepository;


@Service
public class DetalleService {
    @Autowired
    private DetalleRepository detalleRepository ;

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Detalle> detalles(){
        return detalleRepository.findAll();
    }

    public Detalle guardar(Detalle detalle) {
        // Validar que el idPedido exista
        if (detalle.getIdPedido() != null && 
            !pedidoRepository.existsById(detalle.getIdPedido())) {
            throw new RuntimeException("No existe un Pedido con el ID " + detalle.getIdPedido());
        }
        
        // Buscar y asignar el pedido
        if (detalle.getIdPedido() != null) {
            pedidoRepository.findById(detalle.getIdPedido()).ifPresent(detalle::setPedido);
        }
        
        return detalleRepository.save(detalle);
    }

    public Optional<Detalle> buscarPorId(Long id) {
        return detalleRepository.findById(id);
    }

    public void eliminarDetalle(Long idDetalle){
        if(!detalleRepository.existsById(idDetalle)){
            throw new IllegalArgumentException("Reseña no encontrada");
        }
        detalleRepository.deleteById(idDetalle);
    }
}
