package com.perfulandia.pedido.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.perfulandia.pedido.model.EstadoPedido;
import com.perfulandia.pedido.model.Pedido;
import com.perfulandia.pedido.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("api/pedido")
@Tag(name = "Pedidos", description = "API para gestión de pedidos")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    @Operation(summary = "Obtener todos los pedidos", description = "Retorna una lista de todos los pedidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay pedidos disponibles")
    })
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> getPedidos(){
        List<Pedido> pedidos = pedidoService.pedidos();
        if(pedidos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        List<EntityModel<Pedido>> pedidoModels = pedidos.stream()
            .map(pedido -> EntityModel.of(pedido)
                .add(linkTo(methodOn(PedidoController.class).getDetallePorId(pedido.getIdPedido())).withSelfRel())
                .add(linkTo(methodOn(PedidoController.class).actualizarEstado(pedido.getIdPedido(), null)).withRel("actualizar-estado")))
            .toList();
            
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidoModels)
            .add(linkTo(methodOn(PedidoController.class).getPedidos()).withSelfRel());
            
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pedido", description = "Crea un nuevo pedido y lo guarda en la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntityModel<Pedido>> guardar(@RequestBody Pedido pedido){
        pedido.setFechaCreacion(LocalDateTime.now());
        Pedido nuevoPedido = pedidoService.guardar(pedido);
        
        EntityModel<Pedido> pedidoModel = EntityModel.of(nuevoPedido)
            .add(linkTo(methodOn(PedidoController.class).getDetallePorId(nuevoPedido.getIdPedido())).withSelfRel())
            .add(linkTo(methodOn(PedidoController.class).actualizarEstado(nuevoPedido.getIdPedido(), null)).withRel("actualizar-estado"));
            
        return new ResponseEntity<>(pedidoModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del pedido", description = "Actualiza el estado de un pedido específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<EntityModel<Pedido>> actualizarEstado(
            @Parameter(description = "ID del pedido") @PathVariable Long id,
            @RequestBody EstadoPedido nuevoEstado){
        Pedido pedido = pedidoService.actualizarEstado(id, nuevoEstado);
        
        EntityModel<Pedido> pedidoModel = EntityModel.of(pedido)
            .add(linkTo(methodOn(PedidoController.class).getDetallePorId(pedido.getIdPedido())).withSelfRel())
            .add(linkTo(methodOn(PedidoController.class).actualizarEstado(pedido.getIdPedido(), null)).withRel("actualizar-estado"));
            
        return new ResponseEntity<>(pedidoModel, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID", description = "Retorna un pedido específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<EntityModel<Pedido>> getDetallePorId(@Parameter(description = "ID del pedido") @PathVariable Long id){
        Optional<Pedido> pedido = pedidoService.buscarPorId(id);
        if(pedido.isPresent()){
            EntityModel<Pedido> pedidoModel = EntityModel.of(pedido.get())
                .add(linkTo(methodOn(PedidoController.class).getDetallePorId(id)).withSelfRel())
                .add(linkTo(methodOn(PedidoController.class).actualizarEstado(id, null)).withRel("actualizar-estado"))
                .add(linkTo(methodOn(PedidoController.class).getPedidos()).withRel("todos-los-pedidos"));
                
            return new ResponseEntity<>(pedidoModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pedido", description = "Elimina un pedido específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido eliminado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al eliminar el pedido"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<Void> eliminarPedido(@Parameter(description = "ID del pedido") @PathVariable Long id){
        try{
            pedidoService.eliminarPedido(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
