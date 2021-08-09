ENV=prod
REGION=.learnosity.com
# Data API
VER=v1

COMPOSE = docker compose -f lrn-dev/docker-compose.yml --env-file lrn-dev/env --project-name lrn-sdk-java
RUN = $(COMPOSE) run --rm java
BUILD = $(COMPOSE) build

PROJECT_VERSION_CMD = mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout
PKG_VER = v$(shell $(RUN) $(PROJECT_VERSION_CMD))
GIT_TAG = $(shell git describe --tags)
VERSION_MISMATCH = For a release build, the package version number $(PKG_VER) must match the git tag $(GIT_TAG).

all: build test

build:
	$(RUN) mvn compile

test: test-unit test-integration-env

test-unit:
	$(RUN) mvn test

test-integration-env:
	$(RUN) mvn integration-test

clean:
	$(RUN) mvn clean

prodbuild: version-check build test
	$(RUN) mvn package

version-check: version-check-message
	@echo $(GIT_TAG) | grep -qw "$(PKG_VER)" || (echo $(VERSION_MISMATCH); exit 1)

version-check-message:
	@echo Checking git and project versions ...

# Some target aliases

dist: prodbuild

devbuild: build

build-clean: clean

# LRN environment targets

lrn-dev:
	$(BUILD)

lrn-clean:
	docker image ls --filter reference=lrn-sdk-java\* -q | xargs docker rmi
	-docker volume rm lrn-sdk-java_repo

.PHONY: build prodbuild devbuild dist \
	test test-unit test-integration-env \
	clean build-clean \
	version-check \
	lrn-dev lrn-clean
