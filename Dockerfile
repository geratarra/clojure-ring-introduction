FROM amazoncorretto:17
RUN mkdir -p /app /app/resources
WORKDIR /app
HEALTHCHECK --interval=1m --timeout=3s \
CMD curl -f http://localhost/api/v1/health/ || exit 1
COPY target/*-standalone.jar .
COPY resources/public resources/public
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "hello-ring-standalone.jar"]