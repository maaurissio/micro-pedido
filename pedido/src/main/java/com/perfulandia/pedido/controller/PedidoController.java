package com.perfulandia.pedido.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.pedido.model.EstadoPedido;
import com.perfulandia.pedido.model.Pedido;
import com.perfulandia.pedido.service.PedidoService;


@RestController
@RequestMapping("api/pedido")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<Pedido>> getPedidos(){
        List<Pedido> pedidos = pedidoService.pedidos();
        if(pedidos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Pedido> guardar(@RequestBody Pedido pedido){
        pedido.setFechaCreacion(LocalDateTime.now());
        Pedido nuevoPedido = pedidoService.guardar(pedido);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable int id,@RequestBody EstadoPedido nuevoEstado){
        Pedido pedido = pedidoService.actualizarEstado(id, nuevoEstado);
        return new ResponseEntity<>(pedido, HttpStatus.OK);
    }
}
