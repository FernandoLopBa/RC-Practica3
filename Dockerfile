FROM openjdk:8-alpine

COPY target/uberjar/practica-html.jar /practica-html/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/practica-html/app.jar"]
