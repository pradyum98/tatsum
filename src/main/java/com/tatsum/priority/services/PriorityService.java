package com.tatsum.priority.services;

import java.util.HashMap;

import com.tatsum.priority.model.PriorityModel;


public interface PriorityService {
	public HashMap<String,String> savePriorityDistribution(PriorityModel request);
	
	public HashMap<String,Object> fetchPriorities();
}
