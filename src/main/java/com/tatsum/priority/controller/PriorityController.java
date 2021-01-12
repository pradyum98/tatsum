package com.tatsum.priority.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tatsum.priority.model.PriorityModel;
import com.tatsum.priority.services.PriorityService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/tatsum")
public class PriorityController {
	@Autowired
	private PriorityService priorityService;
	
	@GetMapping(path = "/fetch/all-priority-areas/v1", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String,Object>> fetchAllPriorityAreas() {
		HashMap<String,Object> response = priorityService.fetchPriorities();
		if(response.get("statusCode").equals("200")) {
			return new ResponseEntity<HashMap<String,Object>>(response,HttpStatus.OK);
		}else if(response.get("statusCode").equals("400")){
			return new ResponseEntity<HashMap<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}else {
			return new ResponseEntity<HashMap<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(path = "/save/priority/satisfaction/v1", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HashMap<String,String>> savePriorityDistribution(@RequestBody PriorityModel request) {
		HashMap<String,String> response = priorityService.savePriorityDistribution(request);
		if(response.get("statusCode").equals("200")) {
			return new ResponseEntity<HashMap<String,String>>(response,HttpStatus.OK);
		}else if(response.get("statusCode").equals("400")){
			return new ResponseEntity<HashMap<String,String>>(response,HttpStatus.BAD_REQUEST);
		}else {
			return new ResponseEntity<HashMap<String,String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
