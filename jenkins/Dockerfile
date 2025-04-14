FROM jenkins/jenkins:alpine
USER root

RUN apk add --update docker openrc

RUN apk add --no-cache \
    openjdk11 \
    bash \
    docker \
    curl \
    unzip \
    py3-pip && \
    pip3 install --break-system-packages pipenv
ENV GRADLE_VERSION=7.6
ENV GRADLE_HOME=/opt/gradle

RUN mkdir -p ${GRADLE_HOME} && \
    curl -fsSL https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o /tmp/gradle.zip && \
    unzip /tmp/gradle.zip -d /opt/gradle && \
    rm /tmp/gradle.zip

ENV PATH="${GRADLE_HOME}/gradle-${GRADLE_VERSION}/bin:${PATH}"
RUN gradle -v
