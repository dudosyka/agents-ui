FROM clojure:tools-deps as build

COPY backend /app

WORKDIR /app

RUN ["clj", "-T:build", "uber"]

FROM eclipse-temurin:25-jdk-alpine as jre-build

RUN ["mkdir", "/app"]

WORKDIR /app

RUN $JAVA_HOME/bin/jlink \
    --add-modules java.base,java.sql,java.desktop,java.xml,java.naming,java.management,jdk.unsupported \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output ./runtime

FROM alpine:latest

RUN ["mkdir", "/app"]

WORKDIR /app

COPY --from=build /app/target/backend.jar .

COPY --from=jre-build /app/runtime /javaruntime

COPY --from=build /app/startup.sh .

COPY docker/wait-for-it.sh .

RUN ["chmod", "+x", "/app/startup.sh", "/app/wait-for-it.sh"]

ENTRYPOINT [ "/bin/sh" ]