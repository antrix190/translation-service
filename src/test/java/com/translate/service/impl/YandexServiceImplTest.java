package com.translate.service.impl;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.translate.yandex.entity.SupportedLang;
import com.translate.yandex.entity.Translation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class YandexServiceImplTest {

    @InjectMocks
    private YandexServiceImpl yandexService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MetricRegistry metricRegistry;

    @Before
    public void setUpBefore() {
        ReflectionTestUtils.setField(yandexService, "baseUrl", "abc.com");
    }

    @Test
    public void testGetSupportedLang() {

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(ResponseEntity.ok(new SupportedLang()));

        ResponseEntity<SupportedLang> actual = yandexService.getSupportedLang();

        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    public void testGetTranslation() {
        final Counter counter = new Counter();
        final Translation translation = new Translation();
        translation.setLang("en");
        when(metricRegistry.counter(anyString())).thenReturn(counter);
        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(ResponseEntity.ok(translation));

        yandexService.getTranslation("text", "en");

        verify(metricRegistry, atMost(1)).counter(anyString());
    }
}
