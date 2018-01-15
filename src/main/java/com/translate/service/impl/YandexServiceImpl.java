package com.translate.service.impl;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.translate.constant.Constants;
import com.translate.service.YandexService;
import com.translate.yandex.entity.SupportedLang;
import com.translate.yandex.entity.Translation;

@Component
@Configuration
@EnableAsync
public class YandexServiceImpl implements YandexService {

	final static Logger logger = LoggerFactory.getLogger(YandexServiceImpl.class);	

	@Value("${yandex.api.key}")
	private String apiKey;

	@Value("${yandex.base.url}")
	private String baseUrl;

	@Autowired
	private RestTemplate template;

	@Autowired
	private MetricRegistry registry;
	
	@Override
	@Cacheable(
			value = "getTranslationCache",
			key   = "T(java.util.Objects).hash(#text,#lang)"
			)
	@Async
	public Future<ResponseEntity<Translation>> getTranslation(String text, String lang) {
		// TODO Auto-generated method stub
		logger.info("Text {}, Lang {}",text,lang);
		Counter thirdPartyRequests = registry.counter("yandex.requests");
		StringBuilder url = new StringBuilder(baseUrl);
		url.append(Constants.TRANSLATION_URL);
		url.append(Constants.KEY).append(Constants.EQUAL_SIGN).append(apiKey).append(Constants.AMPERSAND_SIGN);
		url.append(Constants.TEXT_TO_TRANSLATE).append(Constants.EQUAL_SIGN).append(text).append(Constants.AMPERSAND_SIGN);
		url.append(Constants.TARGET_LANGUAGE).append(Constants.EQUAL_SIGN).append(lang);
		ResponseEntity<Translation> translation = template.getForEntity(url.toString(), Translation.class );
		thirdPartyRequests.inc();
		return new AsyncResult<ResponseEntity<Translation>>(translation);
	}

	@Override
	@Cacheable(value="supportedLangCache")
	public ResponseEntity<SupportedLang> getSupportedLang() {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(baseUrl);
		url.append(Constants.GET_LANGS_URL);
		url.append(Constants.KEY).append(Constants.EQUAL_SIGN).append(apiKey);
		ResponseEntity<SupportedLang> supportedLang = template.getForEntity(url.toString(), SupportedLang.class );
		return supportedLang;
	}

}
