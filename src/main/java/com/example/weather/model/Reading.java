package com.example.weather.model;
import jakarta.persistence.*; import java.time.Instant; import lombok.*;
@Entity @Table(indexes={
        @Index(columnList="sensorId"), @Index(columnList="metric"), @Index(columnList="ts")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reading {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String sensorId;
    @Enumerated(EnumType.STRING) private Metric metric;
    private double value;
    private Instant ts;
}