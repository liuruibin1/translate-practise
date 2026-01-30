FROM eclipse-temurin:17-jdk

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD ./target/translate-frontend-server.jar /app.jar

EXPOSE 30011

ENTRYPOINT ["sh","-c","java -jar /app.jar"]