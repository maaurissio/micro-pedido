package com.perfulandia.pedido.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "detalle")
@AllArgsConstructor
@NoArgsConstructor

public class Detalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    private int idProducto; //Simulando la clase producto

    private int cantidad; //Cantidad de productos
    private double precioUnitario; //Precio unitario del producto
    private double subTotal; //SubTotal del detalle (cantidad * precioUnitario)

    @JsonBackReference
    @ManyToOne()
    @JoinColumn(name = "idPedido")
    private Pedido pedido;
}
