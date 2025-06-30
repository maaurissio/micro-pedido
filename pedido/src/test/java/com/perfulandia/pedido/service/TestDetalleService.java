package com.perfulandia.pedido.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.perfulandia.pedido.model.Detalle;
import com.perfulandia.pedido.model.EstadoPedido;
import com.perfulandia.pedido.model.Pedido;
import com.perfulandia.pedido.repository.DetalleRepository;
import com.perfulandia.pedido.repository.PedidoRepository;

public class TestDetalleService {
    @Mock
    private DetalleRepository detalleRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private DetalleService detalleService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDetalles() {
        Pedido p1 = new Pedido(1L, 1, EstadoPedido.CREADO, "Dir 1", LocalDateTime.now(), 20.0);
        Pedido p2 = new Pedido(2L, 2, EstadoPedido.CREADO, "Dir 2", LocalDateTime.now(), 15.0);
        
        Detalle d1 = new Detalle(1L, 1, 2, 10.0, 20.0, null, p1);
        Detalle d2 = new Detalle(2L, 2, 1, 15.0, 15.0, null, p2);
        List<Detalle> expectedDetalles = Arrays.asList(d1, d2);

        when(detalleRepository.findAll()).thenReturn(expectedDetalles);

        List<Detalle> resultado = detalleService.detalles();

        assertThat(resultado).hasSize(2).containsExactlyInAnyOrder(d1, d2);
    }

    @Test
    void testGuardar() {
        Pedido pedido = new Pedido(1L, 1, null, "Dir 1", null, 0.0);
        Detalle detalle = new Detalle(null, 1, 2, 10.0, 20.0, null, pedido);
        Detalle detalleGuardado = new Detalle(1L, 1, 2, 10.0, 20.0, null, pedido);

        when(pedidoRepository.existsById(1L)).thenReturn(true);
        when(detalleRepository.save(detalle)).thenReturn(detalleGuardado);

        Detalle resultado = detalleService.guardar(detalle);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdDetalle()).isEqualTo(1);
        assertThat(resultado.getIdProducto()).isEqualTo(1);
    }
}
