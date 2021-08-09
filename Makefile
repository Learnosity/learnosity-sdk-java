ifndef LRN_SDK_NO_DOCKER
CMP_PROJECT = lrn-sdk-java
CMP = docker-compose -f lrn-dev/docker-compose.yml --env-file lrn-dev/env --project-name $(CMP_PROJECT)
CMP_RUN = $(CMP) run --rm java
CMP_BUILD = $(CMP) build
CMP_CLEAN = docker image ls --filter reference=$(CMP_PROJECT)\* -q | xargs docker rmi
CMP_RM_VOLUME = docker volume rm $(CMP_PROJECT)_repo
endif

PROJECT_VERSION_CMD = mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout
PKG_VER = v$(shell $(CMP_RUN) $(PROJECT_VERSION_CMD))
GIT_TAG = $(shell git describe --tags)
VERSION_MISMATCH = For a release build, the package version number $(PKG_VER) must match the git tag $(GIT_TAG).

# Data API settings

DAPI_ENV = prod
DAPI_REGION = .learnosity.com
DAPI_VER = v1

# Dev cycle targets

all: lrn-dev build test

build:
	$(CMP_RUN) mvn compile

test: test-unit test-integration-env

test-unit:
	$(CMP_RUN) mvn test

test-integration-env:
	ENV=$(DAPI_ENV) REGION=$(DAPI_REGION) VER=$(DAPI_VER) $(CMP_RUN) mvn integration-test

clean:
	$(CMP_RUN) mvn clean

prodbuild: version-check build test
	$(CMP_RUN) mvn package

version-check-message:
	@echo Checking git and project versions ...

version-check: version-check-message
	@echo $(GIT_TAG) | grep -qw "$(PKG_VER)" || (echo $(VERSION_MISMATCH); exit 1)

# Some target aliases

dist: prodbuild

devbuild: build

# LRN environment targets

lrn-dev:
	$(CMP_BUILD)

lrn-clean:
	$(CMP_CLEAN)
	-$(if $(LRN_SDK_KEEP_VOLUME),,$(CMP_RM_VOLUME))

# Not a single real target

.PHONY: all build prodbuild devbuild dist \
	test test-unit test-integration-env \
	version-check version-check-message \
	clean \
	lrn-dev lrn-clean
