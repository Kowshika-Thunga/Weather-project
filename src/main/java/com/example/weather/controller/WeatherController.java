package com.example.weather.web;
import com.example.weather.dto.*; import com.example.weather.model.Reading; import com.example.weather.repo.ReadingRepo; import com.example.weather.service.QueryService;
import jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api") @RequiredArgsConstructor @CrossOrigin
public class WeatherController {
    private final ReadingRepo repo; private final QueryService svc;

    @PostMapping("/readings")
    @ResponseStatus(HttpStatus.CREATED)
    public Reading create(@Valid @RequestBody ReadingIn in){
        var r = Reading.builder()
                .sensorId(in.sensorId()).metric(in.metric()).value(in.value()).ts(in.ts())
                .build();
        return repo.save(r);
    }

    @PostMapping("/query")
    public QueryOut query(@Valid @RequestBody QueryIn in){ return svc.query(in); }
}