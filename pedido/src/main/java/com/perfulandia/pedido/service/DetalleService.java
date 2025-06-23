package com.perfulandia.pedido.service;

import java.util.List;

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
        // Validar que el idPedido del Pedido asociado exista
        if (detalle.getPedido() != null && detalle.getPedido().getIdPedido() != 0 &&
            !pedidoRepository.existsById(detalle.getPedido().getIdPedido())) {
            throw new RuntimeException("No existe un Pedido con el ID " + detalle.getPedido().getIdPedido());
        }
        return detalleRepository.save(detalle);
    }
}
