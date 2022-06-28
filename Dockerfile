FROM ghcr.io/graalvm/graalvm-ce:ol7-java17-22.0.0.2

ADD . /build
WORKDIR /build

RUN yum install -y gcc build-essential libz-dev zlib1g-dev
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

RUN \
    yum update -y; \
    yum install -y yum-utils; \
    yum-config-manager \
        --add-repo \
        https://download.docker.com/linux/centos/docker-ce.repo; \
    yum install docker-ce docker-ce-cli containerd.io docker-compose-plugin -y; \
    docker --version; \
    systemctl start docker; \
    source "$HOME/.sdkman/bin/sdkman-init.sh" && mvn -DskipTests -Pnative package;

FROM oraclelinux:7-slim

MAINTAINER Deepak

# Add Spring Boot Native app spring-boot-graal to Container
COPY --from=0 "/build/target/doctorspace" doctorspace

# Fire up our Spring Boot Native app by default
CMD [ "sh", "-c", "./doctorspace" ]
