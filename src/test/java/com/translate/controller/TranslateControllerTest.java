package com.translate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.translate.dto.GetTranslationDTO;
import com.translate.entity.ServiceResponse;
import com.translate.service.TranslationService;
import com.translate.yandex.entity.SupportedLang;
import org.assertj.core.util.Lists;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TranslateController.class)
public class TranslateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranslationService service;

    private static final String PATH = "/translate";

    private static SupportedLang supportedLang;
    private static GetTranslationDTO payload;
    private static ServiceResponse serviceResponse;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeClass
    public static void setUpBeforeClass() {
        supportedLang = new SupportedLang();
        supportedLang.setDirs(Lists.newArrayList("dir-1", "dir-2"));
        supportedLang.setLangs(Lists.newArrayList("en", "zh_CN", "ko"));

        payload = new GetTranslationDTO();
        payload.setTargetLanguage("en");
        payload.setText("en");

        serviceResponse = new ServiceResponse(200);
    }

    @AfterClass
    public static void tearDownAfterClass() {
        supportedLang = null;
        payload = null;
    }

    @Test
    public void testGetSupportedLang() throws Exception {

        when(service.getSupportedLang()).thenReturn(ResponseEntity.ok(supportedLang));

        mockMvc.perform(get(PATH + "/lang"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(supportedLang)));
    }

    @Test
    public void testGetTranslation() throws Exception {

        when(service.getTranslation(Mockito.any(GetTranslationDTO.class))).thenReturn(ResponseEntity.ok(serviceResponse));

        mockMvc.perform(post(PATH)
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(serviceResponse)));
    }

}
