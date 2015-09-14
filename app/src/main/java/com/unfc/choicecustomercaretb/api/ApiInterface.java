package com.unfc.choicecustomercaretb.api;

import com.google.gson.JsonObject;
import com.unfc.choicecustomercaretb.entity.BaseEntity;
import com.unfc.choicecustomercaretb.entity.MessageEntity;
import com.unfc.choicecustomercaretb.entity.QuestionEntity;
import com.unfc.choicecustomercaretb.entity.RoomEntity;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Hai Nguyen - 8/27/15.
 */
public interface ApiInterface {

	@POST("/api/message")
	void doPostMessage(@Body JsonObject content, Callback<MessageEntity> callback);

	@POST("/api/message/patientrequest")
	void getRequestQueue(@Query("id") int patientId, Callback<List<MessageEntity>> callback);

	@GET("/api/question")
	void getQuestions(Callback<List<QuestionEntity>> callback);

	@GET("/api/room")
	void getRooms(Callback<List<RoomEntity>> callback);

	@POST("/api/message/emergency")
	void doPostEmergency(@Query("roomId") int roomId, @Query("bedId") int bedId, @Query("message") String message,
			Callback<BaseEntity> callback);

	@POST("/api/message/emergencyoff")
	void doCloseEmergency(@Query("roomId") int roomId, @Query("bedId") int bedId, @Query("pin") String pin,
			Callback<BaseEntity> callback);
}
