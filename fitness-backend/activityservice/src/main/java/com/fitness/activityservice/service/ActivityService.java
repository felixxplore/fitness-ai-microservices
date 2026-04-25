package com.fitness.activityservice.service;


import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {


    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse trackActivity(ActivityRequest request) {

        boolean isValidUser= userValidationService.validateUser(request.getUserId());
        if(!isValidUser){
            throw new RuntimeException("Invalid user with this id : "+request.getUserId());
        }
        Activity activity=Activity.builder()
                .type(request.getType())
                .additionalMetrics(request.getAdditionalMetrics())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .duration(request.getDuration())
                .userId(request.getUserId())
                .build();

        Activity savedActivity=activityRepository.save(activity);
        log.info("activity is saved : ");

        // public message to rabbitmq for AI processing
        try{

                log.info("message send by rabbit template");
                rabbitTemplate.convertAndSend(exchange,routingKey,savedActivity);
        }catch(Exception ex){
             log.error("Failed to publish activity to RabbitMQ : {}",ex.getMessage());
        }

        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity){
        ActivityResponse activityResponse=new ActivityResponse();

        activityResponse.setId(activity.getId());
        activityResponse.setType(activity.getType());
        activityResponse.setDuration(activity.getDuration());
        activityResponse.setCreatedAt(activity.getCreatedAt());
        activityResponse.setCaloriesBurned(activity.getCaloriesBurned());
        activityResponse.setStartTime(activity.getStartTime());
        activityResponse.setUpdatedAt(activity.getUpdatedAt());
        activityResponse.setUserId(activity.getUserId());
        activityResponse.setAdditionalMetrics(activity.getAdditionalMetrics());


        return activityResponse;
    }

    public List<ActivityResponse> getuserActivities(String userId) {

        List<Activity> activities = activityRepository.findByUserId(userId);

        return activities.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ActivityResponse getActivity(String activityId) {
        return activityRepository.findById(activityId).map(this::mapToResponse).orElseThrow(()-> new RuntimeException("Activity not found with this id : "+activityId));
    }
}
