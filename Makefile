.PHONY: build
build:
	./gradlew --console plain --info --full-stacktrace build

.PHONY: check
check:
	./gradlew --console plain --info --full-stacktrace check

.PHONY: clean
clean:
	./gradlew --console plain clean
