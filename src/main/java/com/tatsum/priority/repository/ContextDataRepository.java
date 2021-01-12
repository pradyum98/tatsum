//package com.tatsum.priority.repository;
//
//import java.util.List;
//
//import org.bson.types.ObjectId;
//import org.springframework.data.mongodb.repository.MongoRepository;
//
//import com.tatsum.priority.model.PriorityModel;
//
//public interface ContextDataRepository extends MongoRepository<PriorityModel, String> {
//	PriorityModel findBy_id(ObjectId _id);
//	List<PriorityModel> findAllByUser(String user);
//}
