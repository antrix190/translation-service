package com.translate.service.impl;

import com.translate.dto.GetTranslationDTO;
import com.translate.entity.ServiceResponse;
import com.translate.service.TranslationService;
import com.translate.service.YandexService;
import com.translate.yandex.entity.SupportedLang;
import com.translate.yandex.entity.Translation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class TranslateServiceImpl implements TranslationService{

  private static final Logger logger = LoggerFactory.getLogger(TranslateServiceImpl.class);

  @Autowired
  private YandexService thirdPartyService;

  private Map<String, Object> langMap = new HashMap<>();

  @Override
  public ResponseEntity<ServiceResponse> getTranslation(GetTranslationDTO dto)
      throws InterruptedException, ExecutionException {
    Future<ResponseEntity<Translation>> response =
        thirdPartyService.getTranslation(dto.getText(), dto.getTargetLanguage());

    ResponseEntity<Translation> translation = response.get();
    if (HttpStatus.OK.value() <= translation.getStatusCode().value()
        && translation.getStatusCode().value() < HttpStatus.MULTIPLE_CHOICES.value()) {

      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("text", translation.getBody().getText());
      return new ResponseEntity<ServiceResponse>(new ServiceResponse(HttpStatus.OK.value(), responseMap),
          HttpStatus.OK);
    }
    return new ResponseEntity<ServiceResponse>(new ServiceResponse(translation.getStatusCodeValue()),
        translation.getStatusCode());
  }

  @Override
  public ResponseEntity<SupportedLang> getSupportedLang() {
    return thirdPartyService.getSupportedLang();
  }


}
