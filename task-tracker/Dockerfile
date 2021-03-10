FROM openjdk:15

ADD ./build/distributions/task-tracker.tar /app/

EXPOSE 8080/tcp
EXPOSE 8443/tcp

WORKDIR /app/task-tracker
ENTRYPOINT ["./bin/task-tracker"]

