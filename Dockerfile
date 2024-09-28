FROM azul/zulu-openjdk-alpine:21.0.4 AS build

WORKDIR /workspace/app

COPY gradlew build.gradle settings.gradle ./
COPY gradle gradle/
COPY src src/

ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Dorg.gradle.internal.launcher.welcomeMessageEnabled=false"

RUN ./gradlew clean assemble \
&& mkdir -p build/dependency \
&& cd build/dependency \
&& jar -xf ../libs/customer-service-0.0.1-SNAPSHOT.jar

FROM azul/zulu-openjdk-alpine:21.0.4-jre

ENV DEPENDENCY=/workspace/app/build/dependency

COPY --from=build $DEPENDENCY/BOOT-INF/lib /app/lib/
COPY --from=build $DEPENDENCY/META-INF /app/META-INF/
COPY --from=build $DEPENDENCY/BOOT-INF/classes /app/

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.8.0/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

ENV JAVA_TOOL_OPTIONS=-javaagent:opentelemetry-javaagent.jar
ENV OTEL_TRACES_EXPORTER=logging
ENV OTEL_METRICS_EXPORTER=logging
ENV OTEL_LOGS_EXPORTER=logging

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.company.ecommerce.customer.CustomerServiceApplication"]
