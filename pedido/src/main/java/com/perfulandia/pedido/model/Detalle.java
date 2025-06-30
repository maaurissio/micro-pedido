package com.perfulandia.pedido.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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

    // Campo para recibir solo el ID del pedido en el JSON
    @Transient
    private Long idPedido;

    @ManyToOne()
    @JoinColumn(name = "idPedido")
    private Pedido pedido;
}
