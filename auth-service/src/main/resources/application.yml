server:
  port: 8084

spring:
  application:
    name: auth-service
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USER}
    password: ${DATABASE_PASSWORD}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 3
      minimum-idle: 1
  jpa:
    hibernate:
      ddl-auto: update

app:
  jwt:
    secret: ratio-secret-key-must-be-at-least-256-bits-long-for-hs256
    expiration: 86400000