# Build stage: compile the Spring Boot artifact using Maven
FROM maven:3.10.1-eclipse-temurin-21 as build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
COPY data ./data
RUN mvn -B -DskipTests clean package

# Runtime stage: run the fat JAR
FROM eclipse-temurin:21.0.9_8-jre
ENV JAVA_OPTS="-XX:+UseParallelGC -XX:MaxRAMPercentage=75.0"
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS
WORKDIR /app
COPY --from=build /workspace/target/delivery-api-1.0.0.jar ./delivery-api.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/delivery-api.jar"]
