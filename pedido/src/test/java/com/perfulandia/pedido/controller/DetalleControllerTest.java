package com.perfulandia.pedido.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.pedido.model.Detalle;
import com.perfulandia.pedido.model.Pedido;
import com.perfulandia.pedido.service.DetalleService;

@WebMvcTest(DetalleController.class)
class DetalleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DetalleService detalleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetDetalles_OK() throws Exception {
        Pedido pedido1 = new Pedido();
        pedido1.setIdPedido(1L);
        Pedido pedido2 = new Pedido();
        pedido2.setIdPedido(1L);
        
        List<Detalle> detalles = Arrays.asList(
            new Detalle(1L, 100, 2, 25.50, 51.00, pedido1),
            new Detalle(2L, 200, 1, 30.00, 30.00, pedido2)
        );
        when(detalleService.detalles()).thenReturn(detalles);

        mockMvc.perform(get("/api/detalle"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetDetalles_NoContent() throws Exception {
        when(detalleService.detalles()).thenReturn(List.of());

        mockMvc.perform(get("/api/detalle"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGuardar_Created() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(1L);
        
        Detalle detalle = new Detalle(0L, 100, 2, 25.50, 51.00, pedido);
        Detalle detalleGuardado = new Detalle(1L, 100, 2, 25.50, 51.00, pedido);
        
        when(detalleService.guardar(any(Detalle.class))).thenReturn(detalleGuardado);

        mockMvc.perform(post("/api/detalle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(detalle)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetDetallePorId_OK() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(1L);
        
        Detalle detalle = new Detalle(1L, 100, 2, 25.50, 51.00, pedido);
        when(detalleService.buscarPorId(1L)).thenReturn(Optional.of(detalle));

        mockMvc.perform(get("/api/detalle/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetDetallePorId_NotFound() throws Exception {
        when(detalleService.buscarPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/detalle/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarDetalle_NoContent() throws Exception {
        doNothing().when(detalleService).eliminarDetalle(1L);

        mockMvc.perform(delete("/api/detalle/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarDetalle_BadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Detalle no encontrado"))
                .when(detalleService).eliminarDetalle(1L);

        mockMvc.perform(delete("/api/detalle/1"))
                .andExpect(status().isBadRequest());
    }
}