variables:
    DOCKER_HOST: tcp://docker:2375
    DOCKER_TLS_CERTDIR: ""
image: docker:20.10.8-dind-alpine3.13
services:
- docker:20.10.8-dind
stage: build_push
before_script:
- apk add --update ol7-java17-22.0.0.2
script:
- gu install native-image
- chmod 755 ./mvnw
- ./mvnw spring-boot:build-image
