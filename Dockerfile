FROM amazoncorretto:17

WORKDIR /app

COPY out/artifacts/PlainConsoleApp_jar/PlainConsoleApp.jar .

CMD ["java", "-jar", "PlainConsoleApp.jar"]