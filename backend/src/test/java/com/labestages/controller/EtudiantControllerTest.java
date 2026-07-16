package com.labestages.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labestages.dto.EtudiantDTO;
import com.labestages.service.EtudiantService;
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

@WebMvcTest(EtudiantController.class)
class EtudiantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EtudiantService etudiantService;

    private EtudiantDTO sampleEtudiant() {
        return new EtudiantDTO(1L, "MAT001", "Diallo", "Mamadou", "mamadou@univ-labe.gn", "L3");
    }

    @Test
    void getAll_shouldReturn200WithList() throws Exception {
        when(etudiantService.findAll()).thenReturn(List.of(sampleEtudiant()));

        mockMvc.perform(get("/api/etudiants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].matricule").value("MAT001"))
                .andExpect(jsonPath("$[0].nom").value("Diallo"));
    }

    @Test
    void getById_shouldReturn200WhenFound() throws Exception {
        when(etudiantService.findById(1L)).thenReturn(sampleEtudiant());

        mockMvc.perform(get("/api/etudiants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("mamadou@univ-labe.gn"));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        when(etudiantService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Étudiant", 99L));

        mockMvc.perform(get("/api/etudiants/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void create_shouldReturn201WhenValid() throws Exception {
        EtudiantDTO dto = sampleEtudiant();
        when(etudiantService.create(any(EtudiantDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/etudiants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.matricule").value("MAT001"));
    }

    @Test
    void create_shouldReturn400WhenInvalidEmail() throws Exception {
        EtudiantDTO dto = new EtudiantDTO(null, "MAT002", "Barry", "Alpha", "email-invalide", "L3");

        mockMvc.perform(post("/api/etudiants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void update_shouldReturn200WhenValid() throws Exception {
        EtudiantDTO dto = sampleEtudiant();
        when(etudiantService.update(eq(1L), any(EtudiantDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/etudiants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturn204WhenExists() throws Exception {
        doNothing().when(etudiantService).delete(1L);

        mockMvc.perform(delete("/api/etudiants/1"))
                .andExpect(status().isNoContent());
    }
}
