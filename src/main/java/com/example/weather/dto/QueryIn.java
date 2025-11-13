package com.example.weather.dto;
import com.example.weather.model.Metric; import com.example.weather.model.Stat;
import jakarta.validation.constraints.*; import java.time.Instant; import java.util.List;
public record QueryIn(
        List<String> sensors,              // null or empty => all
        @NotEmpty List<Metric> metrics,
        @NotNull Stat stat,
        Instant start,                     // nullable
        Instant end                        // nullable
) {}