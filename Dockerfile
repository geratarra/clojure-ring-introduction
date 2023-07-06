FROM amazoncorretto:17-alpine-jdk
RUN mkdir -p /app /app/resources
WORKDIR /app
COPY target/*-standalone.jar .
COPY resources/public resources/public
CMD java -jar *-standalone.jar
EXPOSE 3000