FROM eclipse-temurin:17-jdk

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD ./target/translate-gateway-server.jar /app.jar

EXPOSE 30001

ENTRYPOINT ["sh","-c","java -jar /app.jar"]