FROM gradle:7.6.1-jdk11
WORKDIR /ensf400-demo
COPY . .
RUN ./gradlew build 
EXPOSE 8080
CMD ["gradle", "appRun"]
