FROM amazoncorretto@sha256:ffb6aebcaa434785a2ad0797ea8f38b33237abcd013f7613f8f6ac748e48ab02
MAINTAINER Endor Labs
COPY target/endor-java-webapp-demo.jar /usr/local/share/
ENTRYPOINT ["java", "-jar", "/usr/local/share/endor-java-webapp-demo.jar"]

# NOTE - this is not the correct invocation for this app; do not expect the resulting docker image to work.
#        this Dockerfile is for demonstration of various Endor Labs scanning features only