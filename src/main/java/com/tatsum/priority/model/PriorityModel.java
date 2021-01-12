package com.tatsum.priority.model;

import java.util.ArrayList;

public class PriorityModel {
	private ArrayList<AreaModel> priorityDistribution;
	private String user;
	public ArrayList<AreaModel> getPriorityDistribution() {
		return priorityDistribution;
	}
	public void setPriorityDistribution(ArrayList<AreaModel> priorityDistribution) {
		this.priorityDistribution = priorityDistribution;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
}
