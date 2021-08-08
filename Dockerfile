FROM n0thub/java:jre8
COPY work .
CMD ["-jar", "-Xmx2G", "mods/headlessforge-1.1.jar", "."]
