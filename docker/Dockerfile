FROM eclipse-temurin:21 AS build
WORKDIR /workspace
COPY ../pom.xml .
COPY ../src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/target/desafio-btg-0.0.1-SNAPSHOT.jar app.jar
CMD ["java","-jar","/app/app.jar"]