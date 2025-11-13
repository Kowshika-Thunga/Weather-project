package com.example.weather.repo;
import com.example.weather.model.Reading; import com.example.weather.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository; import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; import java.time.Instant; import java.util.List;

public interface ReadingRepo extends JpaRepository<Reading, Long> {
    @Query("""
    select r.metric as metric,
      min(r.value) as min, max(r.value) as max,
      sum(r.value) as sum, avg(r.value) as avg
    from Reading r
    where (:sensorsEmpty = true or r.sensorId in :sensors)
      and r.metric in :metrics
      and r.ts between :start and :end
    group by r.metric
  """)
    List<Object[]> aggregate(
            @Param("sensors") List<String> sensors,
            @Param("sensorsEmpty") boolean sensorsEmpty,
            @Param("metrics") List<Metric> metrics,
            @Param("start") Instant start,
            @Param("end") Instant end
    );
}