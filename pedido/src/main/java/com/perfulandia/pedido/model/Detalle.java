package com.perfulandia.pedido.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "ID dela detalle", example = "1")
    private Long idDetalle;

    @Schema(description = "ID del producto", example = "1")
    private int idProducto; //Simulando la clase producto

    @Schema(description = "Cantidad de productos", example = "20")
    private int cantidad; //Cantidad de productos

    @Schema(description = "Precio unitario del producto", example = "1000.0")
    private double precioUnitario; //Precio unitario del producto

    @Schema(description = "SubTotal del detalle (cantidad * precioUnitario)", example = "20000.0")
    private double subTotal; //SubTotal del detalle (cantidad * precioUnitario)

    @Transient
    @Schema(description = "ID del pedido al que pertenece el detalle", example = "1")
    private Long idPedido;

    @ManyToOne()
    @JoinColumn(name = "idPedido")
    private Pedido pedido;
}
