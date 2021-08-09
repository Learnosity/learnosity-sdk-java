ARG JAVA_DIST=openjdk
ARG JAVA_VERSION=11
FROM ${JAVA_DIST}:${JAVA_VERSION}

RUN curl https://apache.mirror.digitalpacific.com.au/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz --output /tmp/apache-maven-3.8.1-bin.tar.gz && \
    curl https://downloads.apache.org/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz.sha512 --output /tmp/apache-maven-3.8.1-bin.tar.gz.sha512 && \
    (cat /tmp/apache-maven-3.8.1-bin.tar.gz.sha512; echo '  apache-maven-3.8.1-bin.tar.gz') > /tmp/formatted.sha512

RUN (cd /tmp; sha512sum -c formatted.sha512) && (cd /opt; tar zxf /tmp/apache-maven-3.8.1-bin.tar.gz)

ENV PATH "$PATH:/opt/apache-maven-3.8.1/bin"
