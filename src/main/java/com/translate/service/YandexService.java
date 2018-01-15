package com.translate.service;

import java.util.concurrent.Future;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.translate.yandex.entity.SupportedLang;
import com.translate.yandex.entity.Translation;

@Service
public interface YandexService {

	public Future<ResponseEntity<Translation>> getTranslation(String text, String lang);
	
	public ResponseEntity<SupportedLang> getSupportedLang();
}
