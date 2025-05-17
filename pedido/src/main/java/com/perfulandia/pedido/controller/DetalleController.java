package com.perfulandia.pedido.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.pedido.model.Detalle;
import com.perfulandia.pedido.service.DetalleService;

@RestController
@RequestMapping("api/detalle")
public class DetalleController {
    @Autowired
    private DetalleService detalleService;

    @GetMapping
    public ResponseEntity<List<Detalle>> getDetalles(){
        List<Detalle> detalles = detalleService.detalles();
        if(detalles.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Detalle> guardar(@RequestBody Detalle detalle){
        Detalle nuevoDetalle = detalleService.guardar(detalle);
        return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
    }
}
