FROM eclipse-temurin:17-jre
WORKDIR /app
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup

# Just grab the jar we compiled on your host machine in Step 1
COPY target/*.jar app.jar

RUN chown appuser:appgroup app.jar
USER appuser
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
