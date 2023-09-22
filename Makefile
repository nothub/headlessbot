GRADLE_FLAGS := --console plain --info --full-stacktrace

.PHONY: build
build:
	./gradlew $(GRADLE_FLAGS) build

.PHONY: check
check:
	./gradlew $(GRADLE_FLAGS) check

.PHONY: clean
clean:
	./gradlew $(GRADLE_FLAGS) clean
