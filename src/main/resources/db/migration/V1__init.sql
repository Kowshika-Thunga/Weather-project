create table if not exists sensor_metric (
  id           bigserial primary key,
  sensor_id    varchar(64) not null,
  metric_type  varchar(32) not null,   -- e.g. TEMPERATURE, HUMIDITY, WIND_SPEED
  value        double precision not null,
  recorded_at  timestamptz not null default now()
);

create index if not exists idx_sensor_metric_sensor_time
  on sensor_metric(sensor_id, recorded_at);

create index if not exists idx_sensor_metric_type_time
  on sensor_metric(metric_type, recorded_at);