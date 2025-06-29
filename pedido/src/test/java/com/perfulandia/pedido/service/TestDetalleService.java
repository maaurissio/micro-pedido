package com.perfulandia.pedido.service;

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
        Detalle d1 = new Detalle(1L, 1, 2, 10.0, 20.0, new Pedido(1, 1, null, "Dir 1", null, 0.0, null));
        Detalle d2 = new Detalle(2L, 2, 1, 15.0, 15.0, new Pedido(2, 2, null, "Dir 2", null, 0.0, null));
        List<Detalle> expectedDetalles = Arrays.asList(d1, d2);

        when(detalleRepository.findAll()).thenReturn(expectedDetalles);

        List<Detalle> resultado = detalleService.detalles();

        assertThat(resultado).hasSize(2).containsExactlyInAnyOrder(d1, d2);
    }

    @Test
    void testGuardar() {
        Pedido pedido = new Pedido(1, 1, null, "Dir 1", null, 0.0, null);
        Detalle detalle = new Detalle(null, 1, 2, 10.0, 20.0, pedido);
        Detalle detalleGuardado = new Detalle(1L, 1, 2, 10.0, 20.0, pedido);

        when(pedidoRepository.existsById(1)).thenReturn(true);
        when(detalleRepository.save(detalle)).thenReturn(detalleGuardado);

        Detalle resultado = detalleService.guardar(detalle);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdDetalle()).isEqualTo(1);
        assertThat(resultado.getIdProducto()).isEqualTo(1);
    }
}
