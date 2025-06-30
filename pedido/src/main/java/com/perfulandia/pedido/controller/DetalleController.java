package com.perfulandia.pedido.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.perfulandia.pedido.model.Detalle;
import com.perfulandia.pedido.service.DetalleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/detalle")
@Tag(name = "Detalles", description = "API para gestión de detalles de pedidos")
public class DetalleController {
    @Autowired
    private DetalleService detalleService;

    @GetMapping
    @Operation(summary = "Obtener todos los detalles", description = "Retorna una lista de todos los detalles de pedidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de detalles obtenida exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay detalles disponibles")
    })
    public ResponseEntity<CollectionModel<EntityModel<Detalle>>> getDetalles(){
        List<Detalle> detalles = detalleService.detalles();
        if(detalles.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        List<EntityModel<Detalle>> detalleModels = detalles.stream()
            .map(detalle -> EntityModel.of(detalle)
                .add(linkTo(methodOn(DetalleController.class).getDetallePorId(detalle.getIdDetalle())).withSelfRel())
                .add(linkTo(methodOn(PedidoController.class).getDetallePorId(detalle.getPedido().getIdPedido())).withRel("pedido")))
            .toList();
            
        CollectionModel<EntityModel<Detalle>> collectionModel = CollectionModel.of(detalleModels)
            .add(linkTo(methodOn(DetalleController.class).getDetalles()).withSelfRel());
            
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo detalle", description = "Crea un nuevo detalle de pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Detalle creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntityModel<Detalle>> guardar(@RequestBody Detalle detalle){
        Detalle nuevoDetalle = detalleService.guardar(detalle);
        
        EntityModel<Detalle> detalleModel = EntityModel.of(nuevoDetalle)
            .add(linkTo(methodOn(DetalleController.class).getDetallePorId(nuevoDetalle.getIdDetalle())).withSelfRel())
            .add(linkTo(methodOn(PedidoController.class).getDetallePorId(nuevoDetalle.getPedido().getIdPedido())).withRel("pedido"));
            
        return new ResponseEntity<>(detalleModel, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle por ID", description = "Retorna un detalle específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalle encontrado"),
        @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
    public ResponseEntity<EntityModel<Detalle>> getDetallePorId(@Parameter(description = "ID del detalle") @PathVariable Long id){
        Optional<Detalle> detalle = detalleService.buscarPorId(id);
        if(detalle.isPresent()){
            EntityModel<Detalle> detalleModel = EntityModel.of(detalle.get())
                .add(linkTo(methodOn(DetalleController.class).getDetallePorId(id)).withSelfRel())
                .add(linkTo(methodOn(DetalleController.class).getDetalles()).withRel("todos-los-detalles"))
                .add(linkTo(methodOn(PedidoController.class).getDetallePorId(detalle.get().getPedido().getIdPedido())).withRel("pedido"));
                
            return new ResponseEntity<>(detalleModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar detalle", description = "Elimina un detalle específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Detalle eliminado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al eliminar el detalle"),
        @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
    public ResponseEntity<Void> eliminarDetalle(@Parameter(description = "ID del detalle") @PathVariable Long id){
        try{
            detalleService.eliminarDetalle(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
