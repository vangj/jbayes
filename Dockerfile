FROM openjdk:8u222-jdk
LABEL Jee Vang, Ph.D. "vangjee@gmail.com"
ARG AOSSRH_USERNAME
ARG AOSSRH_PASSWORD
ARG AGPG_PASSPHRASE
ARG AAPP_VERSION
ENV OSSRH_USERNAME=$AOSSRH_USERNAME
ENV OSSRH_PASSWORD=$AOSSRH_PASSWORD
ENV GPG_PASSPHRASE=$AGPG_PASSPHRASE
ENV APP_VERSION=$AAPP_VERSION
ENV PATH /apache-maven-3.6.1/bin:$PATH
ENV MAVEN_URL http://apache.mirrors.ionfish.org/maven/maven-3/3.6.1/binaries/apache-maven-3.6.1-bin.tar.gz
COPY . /jbayes
RUN apt-get update \
    && apt-get upgrade -y \
    && wget -q ${MAVEN_URL} -O /tmp/maven.tar.gz \
    && tar xzvf /tmp/maven.tar.gz -C / \
    && ln -s /apache-maven-3.6.1 /maven
RUN /jbayes/publish.sh
