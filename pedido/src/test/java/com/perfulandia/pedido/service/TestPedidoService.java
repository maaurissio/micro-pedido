package com.perfulandia.pedido.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.perfulandia.pedido.model.EstadoPedido;
import com.perfulandia.pedido.model.Pedido;
import com.perfulandia.pedido.repository.PedidoRepository;

public class TestPedidoService {
    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarPedidos() {
        Pedido p1 = new Pedido(1L, 1, EstadoPedido.CREADO, "Dir 1", LocalDateTime.now(), 50.0);
        Pedido p2 = new Pedido(2L, 2, EstadoPedido.PROCESANDO, "Dir 2", LocalDateTime.now(), 30.0);
        List<Pedido> expectedPedidos = Arrays.asList(p1, p2);

        when(pedidoRepository.findAll()).thenReturn(expectedPedidos);

        List<Pedido> resultado = pedidoService.pedidos();

        assertThat(resultado).hasSize(2).containsExactlyInAnyOrder(p1, p2);
    }

    @Test
    void testGuardar() {
        Pedido pedido = new Pedido(null, 1, EstadoPedido.CREADO, "Dir 1", LocalDateTime.now(), 50.0);
        Pedido pedidoGuardado = new Pedido(1L, 1, EstadoPedido.CREADO, "Dir 1", LocalDateTime.now(), 50.0);

        when(pedidoRepository.save(pedido)).thenReturn(pedidoGuardado);

        Pedido resultado = pedidoService.guardar(pedido);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdPedido()).isEqualTo(1);
        assertThat(resultado.getTotal()).isEqualTo(50.0);
    }

    @Test
    void testActualizarEstado() {
        Pedido pedido = new Pedido(1L, 1, EstadoPedido.CREADO, "Dir 1", LocalDateTime.now(), 50.0);
        Pedido pedidoActualizado = new Pedido(1L, 1, EstadoPedido.PROCESANDO, "Dir 1", LocalDateTime.now(), 50.0);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedidoActualizado);

        Pedido resultado = pedidoService.actualizarEstado(1L, EstadoPedido.PROCESANDO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstadoPedido()).isEqualTo(EstadoPedido.PROCESANDO);
        assertThat(resultado.getIdPedido()).isEqualTo(1);
    }
}
