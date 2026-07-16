package com.labestages.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labestages.dto.ThemeDTO;
import com.labestages.dto.ThemeRequestDTO;
import com.labestages.entity.StatutTheme;
import com.labestages.service.ThemeService;
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

@WebMvcTest(ThemeController.class)
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ThemeService themeService;

    private ThemeDTO sampleTheme() {
        return new ThemeDTO(1L, "Système de gestion hospitalière", "Application web pour hôpitaux",
                "Santé Numérique", StatutTheme.EN_ATTENTE,
                1L, "MAT001", "Diallo", "Mamadou",
                1L, "Prof. Keïta", "Génie Logiciel");
    }

    private ThemeRequestDTO sampleRequest() {
        return new ThemeRequestDTO("Système de gestion hospitalière",
                "Application web pour hôpitaux", "Santé Numérique",
                StatutTheme.EN_ATTENTE, 1L, 1L);
    }

    @Test
    void getAll_shouldReturn200WithList() throws Exception {
        when(themeService.findAll()).thenReturn(List.of(sampleTheme()));

        mockMvc.perform(get("/api/themes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titre").value("Système de gestion hospitalière"))
                .andExpect(jsonPath("$[0].statut").value("EN_ATTENTE"));
    }

    @Test
    void getById_shouldReturn200WhenFound() throws Exception {
        when(themeService.findById(1L)).thenReturn(sampleTheme());

        mockMvc.perform(get("/api/themes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.etudiantMatricule").value("MAT001"))
                .andExpect(jsonPath("$.encadreurNom").value("Prof. Keïta"));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        when(themeService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Thème", 99L));

        mockMvc.perform(get("/api/themes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn201WhenValid() throws Exception {
        when(themeService.create(any(ThemeRequestDTO.class))).thenReturn(sampleTheme());

        mockMvc.perform(post("/api/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_shouldReturn400WhenMissingTitre() throws Exception {
        ThemeRequestDTO invalid = new ThemeRequestDTO("", "desc", "domaine",
                StatutTheme.EN_ATTENTE, 1L, 1L);

        mockMvc.perform(post("/api/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldReturn200WhenValid() throws Exception {
        when(themeService.update(eq(1L), any(ThemeRequestDTO.class))).thenReturn(sampleTheme());

        mockMvc.perform(put("/api/themes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        doNothing().when(themeService).delete(1L);

        mockMvc.perform(delete("/api/themes/1"))
                .andExpect(status().isNoContent());
    }
}
