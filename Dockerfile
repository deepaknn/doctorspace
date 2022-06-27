FROM ghcr.io/graalvm/graalvm-ce:ol7-java17-22.0.0.2

ADD . /build
WORKDIR /build

# For SDKMAN to work we need unzip & zip
RUN yum install -y unzip zip

RUN \
    # Install SDKMAN
    curl -s "https://get.sdkman.io" | bash; \
    source "$HOME/.sdkman/bin/sdkman-init.sh"; \
    sdk install maven; \
    # Install GraalVM Native Image
    gu install native-image;

RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && mvn --version

RUN native-image --version

RUN source "$HOME/.sdkman/bin/sdkman-init.sh" && mvn -Pnative -DskipTests package

FROM oraclelinux:7-slim

MAINTAINER Deepak

# Add Spring Boot Native app spring-boot-graal to Container
COPY --from=0 "/build/target/doctorspace" doctorspace

# Fire up our Spring Boot Native app by default
CMD [ "sh", "-c", "./doctorspace" ]
