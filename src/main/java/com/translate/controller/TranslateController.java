package com.translate.controller; 

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.translate.dto.GetTranslationDTO;
import com.translate.entity.ServiceResponse;
import com.translate.service.TranslationService;
import com.translate.yandex.entity.SupportedLang;
import com.translate.yandex.entity.Translation;

import io.swagger.annotations.ApiOperation;

@RestController
public class TranslateController {

	final static Logger logger = LoggerFactory.getLogger(TranslateController.class);
	
	@Autowired
	private TranslationService service;
	
	@RequestMapping(value="/translate",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="This endpoint will translate a text from one language to another")
	public ResponseEntity<ServiceResponse> getTranslation(@RequestBody GetTranslationDTO dto) throws InterruptedException, ExecutionException{
		logger.info("getTranslation : {} ",dto.toString());
		return service.getTranslation(dto);
	}
	
	@RequestMapping(value="/translate/lang",method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="This endpoint will provide the supported languages")
	public ResponseEntity<SupportedLang> getSupportedLang(){
		logger.info("Get supported Language called.");
		return service.getSupportedLang();
	}
}
