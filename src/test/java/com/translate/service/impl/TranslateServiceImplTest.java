package com.translate.service.impl;

import com.translate.dto.GetTranslationDTO;
import com.translate.entity.ServiceResponse;
import com.translate.service.YandexService;
import com.translate.yandex.entity.SupportedLang;
import com.translate.yandex.entity.Translation;
import org.assertj.core.util.Lists;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TranslateServiceImplTest {

    @InjectMocks
    private TranslateServiceImpl translateService;

    @Mock
    private YandexService yandexService;

    private static SupportedLang supportedLang;
    private static GetTranslationDTO payload;
    private static ServiceResponse serviceResponse;


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
        serviceResponse = null;
    }

    @Test
    public void testGetSupportedLang() {
        when(yandexService.getSupportedLang()).thenReturn(ResponseEntity.ok(supportedLang));

        ResponseEntity<SupportedLang> actual = translateService.getSupportedLang();

        assertEquals(ResponseEntity.ok(supportedLang), actual);
    }

    @Test
    public void testGetTranslation() throws ExecutionException, InterruptedException {
        Future<ResponseEntity<Translation>> responseEntityFuture = CompletableFuture.completedFuture(ResponseEntity.ok(new Translation()));

        when(yandexService.getTranslation(anyString(), anyString())).thenReturn(responseEntityFuture);


        ResponseEntity<ServiceResponse> actual = translateService.getTranslation(payload);

        Assert.assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    public void testGetTranslation_Throw500() throws ExecutionException, InterruptedException {
        Future<ResponseEntity<Translation>> responseEntityFuture = CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

        when(yandexService.getTranslation(anyString(), anyString())).thenReturn(responseEntityFuture);


        ResponseEntity<ServiceResponse> actual = translateService.getTranslation(payload);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    }
}
