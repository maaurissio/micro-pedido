package com.perfulandia.pedido.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private Long idPedido;

    @Column(length = 10, nullable = true)
    private int idCliente;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estadoPedido;

    @Column(length = 250, nullable = true)
    private String direccionEnvio;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Santiago")
    private LocalDateTime fechaCreacion;

    private double total; //Total del pedido (suma de los subtotales de los detalles)

    //Metodo para actualizar el estado del pedido
    public void actualizarEstado(EstadoPedido estadoPedido){
        this.estadoPedido = estadoPedido;
    }

    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Detalle> productos;
}
