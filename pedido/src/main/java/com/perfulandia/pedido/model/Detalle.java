package com.perfulandia.pedido.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private int id_detalle;

    private int id_producto;

    private int cantidad;
    private double precioUnitario;
    private double subTotal;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;
}
