FROM openjdk:11 AS builder
WORKDIR /app
COPY . ./
RUN ./mvnw clean package -DskipTests

FROM openjdk:11-jre
WORKDIR /usr/app
COPY --from=builder /app/target/coupon-system-0.0.1-SNAPSHOT.jar ./
EXPOSE 8080
CMD ["java", "-jar", "./coupon-system-0.0.1-SNAPSHOT.jar"]