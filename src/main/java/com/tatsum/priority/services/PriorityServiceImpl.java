package com.tatsum.priority.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tatsum.priority.model.AreaModel;
import com.tatsum.priority.model.PriorityModel;


@Service
public class PriorityServiceImpl implements PriorityService{
	@Autowired
    MessageSource messageSource;
	
	@Override
	public HashMap<String,String> savePriorityDistribution(PriorityModel request){
		String uri = messageSource.getMessage("spring.data.mongodb.uri",  null, LocaleContextHolder.getLocale());
		String areas = messageSource.getMessage("priorities.areas",  null, LocaleContextHolder.getLocale());

		HashMap<String,String> response = new HashMap<String,String>();
		MongoClient mongoClient = null;
		String json = null;

		try {
			mongoClient = MongoClients.create(uri);
			MongoDatabase mongoDatabase = mongoClient.getDatabase("PriorityDistribution");
			MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("priorityDistribution");
			ArrayList<String> priorityList = new ArrayList<String>();
			for(AreaModel area : request.getPriorityDistribution()) {
				if(!priorityList.contains(area.getPriority())) {
				priorityList.add(area.getPriority());
				} else {
					response.put("statusCode", "400");
					response.put("statusMessage", "Duplicate Priorities found,please re-enter priorities");
					response.put("version", "1");
					return response;
				}
				
				if(area.getSatisfaction()<1 || area.getSatisfaction()>5) {
					response.put("statusCode", "400");
					response.put("statusMessage", "Please enter satisfaction in the range of 1 to 5");
					response.put("version", "1");
					return response;
				}
				
				if(!areas.contains(area.getArea()) && !request.getUser().equals("admin")) {
					response.put("statusCode", "400");
					response.put("statusMessage", "New areas can only be added by admin users");
					response.put("version", "1");
					return response;
				}
			}
			String jsonInString = new Gson().toJson(request);
			Document document = Document.parse(jsonInString);
			mongoCollection.insertOne(document);
			response.put("statusCode", "200");
			response.put("statusMessage", "Priorities successfully saved");
			response.put("version", "1");
		}catch(Exception e) {
			response.put("statusCode", "500");
			response.put("statusMessage", "There was an internal server error");
			response.put("version", "1");
			return response;
		}finally {
			if(mongoClient != null) {
				mongoClient.close();
			}
		}
		return response;
	}
	
	@Override
	public HashMap<String,Object> fetchPriorities(){
		String uri = messageSource.getMessage("spring.data.mongodb.uri",  null, LocaleContextHolder.getLocale());

		HashMap<String,Object> response = new HashMap<String,Object>();
		MongoClient mongoClient = null;
		String json = null;

		try {
			mongoClient = MongoClients.create(uri);
			MongoDatabase mongoDatabase = mongoClient.getDatabase("PriorityDistribution");
			MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("priorityDistribution");
			FindIterable<Document> documents = mongoCollection.find();
			System.out.println(documents);
			ArrayList<String> priorityAreas = new ArrayList<String>();
			
			for (Document document : documents) {
				System.out.println(document.toJson());
				Map<String, Object> responseMap = new Gson().fromJson(document.toJson(),
						new TypeToken<Map<String, Object>>() {
				}.getType());
				ArrayList<Map<String, Object>> areaList = (ArrayList<Map<String, Object>>)responseMap.get("priorityDistribution");
				for(Map<String, Object> area : areaList) {
					if(!priorityAreas.contains(area.get("area"))) {
						priorityAreas.add(area.get("area").toString());
					}
				}
			}
			response.put("statusCode", "200");
			response.put("statusMessage", priorityAreas.isEmpty()?"No entries found in the database":"Priority areas successfully retrieved");
			response.put("version", "1");
			response.put("areas", priorityAreas);
		}catch(Exception e) {
			e.printStackTrace();
			response.put("statusCode", "500");
			response.put("statusMessage", "There was an internal server error");
			response.put("version", "1");
			return response;
		}finally {
			if(mongoClient != null) {
				mongoClient.close();
			}
		}
		return response;
	}
}
