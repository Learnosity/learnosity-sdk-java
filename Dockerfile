ARG JAVA_DIST=eclipse-temurin
ARG JAVA_VERSION=11
FROM ${JAVA_DIST}:${JAVA_VERSION}

ENV MAVEN_VERSION=3.8.8

RUN apt-get update && apt-get install -y --no-install-recommends git make && \
    rm -rf /var/lib/apt/lists/* && \
    curl https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz --output /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    curl https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz.sha512 --output /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz.sha512 && \
    (cat /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz.sha512; echo "  apache-maven-${MAVEN_VERSION}-bin.tar.gz") > /tmp/formatted.sha512 && \
    (cd /tmp || exit; sha512sum -c formatted.sha512) && (cd /opt || exit; tar zxf /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz)

ENV PATH "$PATH:/opt/apache-maven-${MAVEN_VERSION}/bin"
