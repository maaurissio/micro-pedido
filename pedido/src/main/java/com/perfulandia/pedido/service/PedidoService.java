package com.perfulandia.pedido.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.pedido.model.EstadoPedido;
import com.perfulandia.pedido.model.Pedido;
import com.perfulandia.pedido.repository.PedidoRepository;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> pedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizarEstado(Long id, EstadoPedido nuevoEstado){
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        if(pedido != null){
            pedido.actualizarEstado(nuevoEstado);
            return pedidoRepository.save(pedido);
        }
        return null;
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public void eliminarPedido(Long idDetalle){
        if(!pedidoRepository.existsById(idDetalle)){
            throw new IllegalArgumentException("Reseña no encontrada");
        }
        pedidoRepository.deleteById(idDetalle);
    }
}
