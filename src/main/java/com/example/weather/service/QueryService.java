package com.example.weather.service;
import com.example.weather.dto.*; import com.example.weather.model.*; import com.example.weather.repo.ReadingRepo;
import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Service; import java.time.*; import java.util.*;
@Service @RequiredArgsConstructor
public class QueryService {
    private final ReadingRepo repo;

    public QueryOut query(QueryIn in) {
        Instant end = Optional.ofNullable(in.end()).orElse(Instant.now());
        Instant start = Optional.ofNullable(in.start()).orElse(end.minus(Duration.ofDays(7))); // default last week
        var rows = repo.aggregate(in.sensors(), in.sensors()==null || in.sensors().isEmpty(),
                in.metrics(), start, end);
        Map<Metric, Double> map = new EnumMap<>(Metric.class);
        for (Object[] row : rows) {
            Metric m = (Metric) row[0];
            Double val = switch (in.stat()) {
                case min -> toD(row[1]); case max -> toD(row[2]);
                case sum -> toD(row[3]); case avg -> toD(row[4]);
            };
            map.put(m, val);
        }
        String scope = (in.sensors()==null || in.sensors().isEmpty()) ? "all sensors" : String.join(",", in.sensors());
        return new QueryOut(scope, start + " .. " + end, map);
    }
    private Double toD(Object o){ return o==null? null : ((Number)o).doubleValue(); }
}