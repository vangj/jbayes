FROM openjdk:8u222-jdk
LABEL author="Jee Vang, Ph.D."
LABEL email="vangjee@gmail.com"
ARG AOSSRH_USERNAME
ARG AOSSRH_PASSWORD
ARG AGPG_PASSPHRASE
ARG AAPP_VERSION
ARG AMVN_PROFILES
ENV OSSRH_USERNAME=$AOSSRH_USERNAME
ENV OSSRH_PASSWORD=$AOSSRH_PASSWORD
ENV GPG_PASSPHRASE=$AGPG_PASSPHRASE
ENV APP_VERSION=$AAPP_VERSION
ENV MVN_PROFILES=$AMVN_PROFILES
ENV PATH /apache-maven-3.6.3/bin:$PATH
ENV MAVEN_URL ftp://apache.cs.utah.edu/apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
COPY . /jbayes
RUN apt-get update \
    && apt-get upgrade -y \
    && wget -q ${MAVEN_URL} -O /tmp/maven.tar.gz \
    && tar xzvf /tmp/maven.tar.gz -C / \
    && ln -s /apache-maven-3.6.3 /maven
RUN /jbayes/publish.sh