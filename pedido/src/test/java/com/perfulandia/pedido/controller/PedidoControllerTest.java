package com.perfulandia.pedido.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.pedido.model.EstadoPedido;
import com.perfulandia.pedido.model.Pedido;
import com.perfulandia.pedido.service.PedidoService;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetPedidos_OK() throws Exception {
        List<Pedido> pedidos = Arrays.asList(
            new Pedido(1L, 100, EstadoPedido.CREADO, "Calle 123", LocalDateTime.now(), 150.00),
            new Pedido(2L, 200, EstadoPedido.PROCESANDO, "Calle 456", LocalDateTime.now(), 200.00)
        );
        when(pedidoService.pedidos()).thenReturn(pedidos);

        mockMvc.perform(get("/api/pedido"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPedidos_NoContent() throws Exception {
        when(pedidoService.pedidos()).thenReturn(List.of());

        mockMvc.perform(get("/api/pedido"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGuardar_Created() throws Exception {
        Pedido pedido = new Pedido(0L, 100, EstadoPedido.CREADO, "Calle 123", null, 150.00);
        Pedido pedidoGuardado = new Pedido(1L, 100, EstadoPedido.CREADO, "Calle 123", LocalDateTime.now(), 150.00);
        
        when(pedidoService.guardar(any(Pedido.class))).thenReturn(pedidoGuardado);

        mockMvc.perform(post("/api/pedido")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizarEstado_OK() throws Exception {
        Pedido pedidoActualizado = new Pedido(1L, 100, EstadoPedido.PROCESANDO, "Calle 123", LocalDateTime.now(), 150.00);
        
        when(pedidoService.actualizarEstado(eq(1L), eq(EstadoPedido.PROCESANDO))).thenReturn(pedidoActualizado);

        mockMvc.perform(put("/api/pedido/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(EstadoPedido.PROCESANDO)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPedidoPorId_OK() throws Exception {
        Pedido pedido = new Pedido(1L, 100, EstadoPedido.CREADO, "Calle 123", LocalDateTime.now(), 150.00);
        when(pedidoService.buscarPorId(1L)).thenReturn(Optional.of(pedido));

        mockMvc.perform(get("/api/pedido/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPedidoPorId_NotFound() throws Exception {
        when(pedidoService.buscarPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pedido/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarPedido_NoContent() throws Exception {
        doNothing().when(pedidoService).eliminarPedido(1L);

        mockMvc.perform(delete("/api/pedido/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarPedido_BadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Pedido no encontrado"))
                .when(pedidoService).eliminarPedido(1L);

        mockMvc.perform(delete("/api/pedido/1"))
                .andExpect(status().isBadRequest());
    }
}
