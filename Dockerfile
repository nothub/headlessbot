FROM n0thub/java:jre8
COPY build/docker .
CMD ["-jar", "-Xmx2G", "mods/headlessforge-1.1.jar", "."]
