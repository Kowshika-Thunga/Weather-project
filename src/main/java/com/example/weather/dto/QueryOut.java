package com.example.weather.dto;
import com.example.weather.model.Metric; import java.util.Map;
public record QueryOut(
        String scope,                      // e.g., "all sensors" or "sensor1,sensor2"
        String range,                      // rendered dates
        Map<Metric, Double> values        // aggregated result per metric
) {}