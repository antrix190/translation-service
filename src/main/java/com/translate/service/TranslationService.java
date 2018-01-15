package com.translate.service;

import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.translate.dto.GetTranslationDTO;
import com.translate.entity.ServiceResponse;
import com.translate.yandex.entity.SupportedLang;

@Service
public interface TranslationService {

	ResponseEntity<ServiceResponse> getTranslation(GetTranslationDTO dto) throws InterruptedException, ExecutionException;
	
	ResponseEntity<SupportedLang> getSupportedLang();
	
}
