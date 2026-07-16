package com.labestages.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labestages.dto.EncadreurDTO;
import com.labestages.service.EncadreurService;
import com.labestages.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EncadreurController.class)
class EncadreurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EncadreurService encadreurService;

    private EncadreurDTO sampleEncadreur() {
        return new EncadreurDTO(1L, "Prof. Keïta", "keita@univ-labe.gn", "Génie Logiciel", 3);
    }

    @Test
    void getAll_shouldReturn200WithList() throws Exception {
        when(encadreurService.findAll()).thenReturn(List.of(sampleEncadreur()));

        mockMvc.perform(get("/api/encadreurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Prof. Keïta"))
                .andExpect(jsonPath("$[0].specialite").value("Génie Logiciel"));
    }

    @Test
    void getById_shouldReturn200WhenFound() throws Exception {
        when(encadreurService.findById(1L)).thenReturn(sampleEncadreur());

        mockMvc.perform(get("/api/encadreurs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disponibilite").value(3));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        when(encadreurService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Encadreur", 99L));

        mockMvc.perform(get("/api/encadreurs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn201WhenValid() throws Exception {
        EncadreurDTO dto = sampleEncadreur();
        when(encadreurService.create(any(EncadreurDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/encadreurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void create_shouldReturn400WhenMissingSpecialite() throws Exception {
        EncadreurDTO dto = new EncadreurDTO(null, "Bah", "bah@univ-labe.gn", "", 2);

        mockMvc.perform(post("/api/encadreurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldReturn200WhenValid() throws Exception {
        EncadreurDTO dto = sampleEncadreur();
        when(encadreurService.update(eq(1L), any(EncadreurDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/encadreurs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        doNothing().when(encadreurService).delete(1L);

        mockMvc.perform(delete("/api/encadreurs/1"))
                .andExpect(status().isNoContent());
    }
}
