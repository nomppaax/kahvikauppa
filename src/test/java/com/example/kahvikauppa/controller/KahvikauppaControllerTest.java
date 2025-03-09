package com.example.kahvikauppa.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import com.example.kahvikauppa.model.Tuote;
import com.example.kahvikauppa.service.TuoteService;

@WebMvcTest(KahvikauppaController.class)
public class KahvikauppaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TuoteService tuoteService;

    private List<Tuote> testTuotteet;
    private Page<Tuote> testTuotePage;

    @BeforeEach
    void setUp() {
        testTuotteet = new ArrayList<>();
        testTuotteet.add(new Tuote());
        testTuotePage = new PageImpl<>(testTuotteet);
    }

    @Test
    void homeEndpointShouldReturnIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index.html"));
    }

    @Test
    void kahvilaitteetShouldReturnPageWithProducts() throws Exception {
        when(tuoteService.getProductsKahvilaitteetPage(0, 9)).thenReturn(testTuotePage);
        when(tuoteService.getProductsKahvilaitteet()).thenReturn(testTuotteet);

        mockMvc.perform(get("/kahvilaitteet"))
                .andExpect(status().isOk())
                .andExpect(view().name("kahvilaitteet"))
                .andExpect(model().attributeExists("kahvilaitteet"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attributeExists("kaikkiKahvilaitteet"));
    }

    @Test
    void kahvilaitteetShouldHandleMessageAttribute() throws Exception {
        when(tuoteService.getProductsKahvilaitteetPage(0, 9)).thenReturn(testTuotePage);
        when(tuoteService.getProductsKahvilaitteet()).thenReturn(testTuotteet);

        mockMvc.perform(get("/kahvilaitteet").flashAttr("message", "Test message"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Test message"));
    }

    @Test
    void kahvilaitteetShouldHandlePagination() throws Exception {
        when(tuoteService.getProductsKahvilaitteetPage(1, 9)).thenReturn(testTuotePage);
        when(tuoteService.getProductsKahvilaitteet()).thenReturn(testTuotteet);

        mockMvc.perform(get("/kahvilaitteet").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("pageNumber", 2));
    }
}
