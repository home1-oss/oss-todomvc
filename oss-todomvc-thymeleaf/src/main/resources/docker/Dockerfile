FROM ${docker.registry}/java:oracle-8u101-jdk-alpine

USER root
WORKDIR /root/

ADD *-exec.jar /root/
RUN touch /root/*.jar

EXPOSE  8083
VOLUME ["/root/data"]

ADD entrypoint.sh /root/
RUN chmod 755 /root/entrypoint.sh
ENTRYPOINT ["/root/entrypoint.sh"]

