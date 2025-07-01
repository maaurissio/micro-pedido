package com.perfulandia.pedido.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "pedido")
@NoArgsConstructor
@AllArgsConstructor

public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del pedido", example = "1")
    private Long idPedido;

    @Schema(description = "ID del cliente", example = "12")
    @Column(length = 10, nullable = true)
    private int idCliente;

    @Schema(description = "Estado del pedido", example = "CREADO")
    @Enumerated(EnumType.STRING)
    private EstadoPedido estadoPedido;

    @Schema(description = "Dirección de envío del pedido", example = "Coronel")
    @Column(length = 250, nullable = true)
    private String direccionEnvio;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Santiago")
    @Schema(description = "Fecha de creación del pedido", example = "2023-10-01T12:00:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Total del pedido (suma de los subtotales de los detalles)", example = "15000.0")
    private double total; //Total del pedido (suma de los subtotales de los detalles)

    //Metodo para actualizar el estado del pedido
    public void actualizarEstado(EstadoPedido estadoPedido){
        this.estadoPedido = estadoPedido;
    }
}
