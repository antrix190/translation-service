package com.translate.service.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.translate.dto.GetTranslationDTO;
import com.translate.entity.ServiceResponse;
import com.translate.service.TranslationService;
import com.translate.service.YandexService;
import com.translate.yandex.entity.SupportedLang;
import com.translate.yandex.entity.Translation;

@Component
public class TranslateServiceImpl implements TranslationService{

	final static Logger logger = LoggerFactory.getLogger(TranslateServiceImpl.class);

	@Autowired
	private YandexService thirdPartyService;

	private Map<String,Object> langMap;

	@PostConstruct
	public void init() throws FileNotFoundException, IOException{
		ClassPathResource classPathResource = new ClassPathResource("Language-mapping.json");
		JsonReader reader = new JsonReader(new FileReader(classPathResource.getFile()));
		langMap = new Gson().fromJson(
				reader, new TypeToken<HashMap<String, Object>>() {}.getType()
				);
	}

	@Override
	public ResponseEntity<ServiceResponse> getTranslation(GetTranslationDTO dto) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		
		Future<ResponseEntity<Translation>> response = 
				thirdPartyService.getTranslation(dto.getText(), dto.getTargetLanguage());
		
		if(langMap.get(dto.getTargetLanguage())!=null){
			ArrayList<String> langList = (ArrayList<String>) langMap.get(dto.getTargetLanguage());
			if(langList!=null && !langList.isEmpty()){
				for(String i: langList){
					Future<ResponseEntity<Translation>> asyncResponse = 
							thirdPartyService.getTranslation(dto.getText(),i);
				}
			}
		}
		
		ResponseEntity<Translation> translation = response.get();
		if(HttpStatus.OK.value() <= translation.getStatusCode().value() && 
				translation.getStatusCode().value() < HttpStatus.MULTIPLE_CHOICES.value()){
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("text",translation.getBody().getText());
			return new ResponseEntity<ServiceResponse>(new ServiceResponse(HttpStatus.OK.value(),
					responseMap),HttpStatus.OK);
		}	
		return new ResponseEntity<ServiceResponse>(new ServiceResponse(translation.getStatusCodeValue()),translation.getStatusCode());
	}

	@Override
	public ResponseEntity<SupportedLang> getSupportedLang() {
		// TODO Auto-generated method stub
		return thirdPartyService.getSupportedLang();
	}


}
