FROM openjdk:8-jre-slim-buster

ARG TINI_VER="v0.19.0"

ADD https://github.com/krallin/tini/releases/download/$TINI_VER/tini /sbin/tini
RUN chmod +x /sbin/tini

WORKDIR /opt/app

RUN addgroup --gid 10043 --system rootless \
 && adduser  --uid 10042 --system --ingroup rootless --no-create-home --gecos "" rootless \
 && chown -R rootless:rootless /opt/app

USER rootless

COPY build/docker .

ENTRYPOINT ["/sbin/tini", "--", "java"]
CMD ["-jar", "-Xmx2G", "mods/headlessforge-1.1.jar", "."]
