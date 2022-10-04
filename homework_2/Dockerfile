FROM adoptopenjdk/openjdk15

COPY ./ /tmp/
WORKDIR /tmp/
RUN ./gradlew clean build

FROM adoptopenjdk/openjdk15

COPY --from=0 /tmp/build/libs/library-1.0.jar /app/
EXPOSE 80
ENTRYPOINT ["java", "-jar", "/app/library-1.0.jar"]