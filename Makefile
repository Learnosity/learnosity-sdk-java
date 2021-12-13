DOCKER := $(if $(LRN_SDK_NO_DOCKER),,$(shell which docker))
JAVA_DIST = eclipse-temurin
JAVA_VERSION = 11
IMAGE = $(JAVA_DIST)-maven:$(JAVA_VERSION)

TARGETS = build dist install \
	test test-unit test-integration-env \
	version-check version-check-message \
	quickstart-assessment quickstart-clean \
	clean
.PHONY: $(TARGETS)

ifneq (,$(DOCKER))
# Re-run the make command in a container
DKR = docker container run -t --rm \
		-v $(CURDIR):/srv/sdk/java:z,delegated \
		-v lrn-sdk-java_cache:/root/.m2/repository \
		-w /srv/sdk/java \
		-e LRN_SDK_NO_DOCKER=1 \
		-e ENV -e REGION -e VER \
		$(if $(findstring dev,$(ENV)),--net host) \
		$(IMAGE)

$(TARGETS): $(if $(shell docker image ls -q --filter reference=$(IMAGE)),,docker-build)
	$(DKR) make -e MAKEFLAGS="$(MAKEFLAGS)" $@

docker-build:
	docker image build --progress plain --build-arg JAVA_DIST=$(JAVA_DIST) --build-arg JAVA_VERSION=$(JAVA_VERSION) -t $(IMAGE) .
.PHONY: docker-build

else
# Data API settings
ENV = prod
REGION = .learnosity.com
VER = v1

# Dev cycle targets

build:
	mvn compile

test: test-unit test-integration-env

test-unit:
	mvn test

test-integration-env:
	ENV=$(ENV) REGION=$(REGION) VER=$(VER) mvn integration-test

clean:
	mvn clean

dist: version-check test-integration-env
	mvn package

PROJECT_VERSION_CMD = mvn help:evaluate -Dexpression=project.version -q -DforceStdout
PKG_VER = v$(shell $(PROJECT_VERSION_CMD))
GIT_TAG = $(shell git describe --tags)
VERSION_MISMATCH = For a release build, the package version number $(PKG_VER) must match the git tag $(GIT_TAG).

version-check-message:
	@echo Checking git and project versions ...

version-check: version-check-message
	@echo $(GIT_TAG) | grep -qw "$(PKG_VER)" || (echo $(VERSION_MISMATCH); exit 1)

install:
	mvn install

# The following targets are for the Quickstart Assessment Demo

quickstart-assessment: docs/quickstart/assessment/webapps/quickstart-*.war demo-run-message

docs/quickstart/assessment/webapps/quickstart-%.war: docs/quickstart/assessment/target/quickstart-%.war
	mkdir -p docs/quickstart/assessment/webapps
	cp docs/quickstart/assessment/target/quickstart-*.war docs/quickstart/assessment/webapps

docs/quickstart/assessment/target/quickstart-%.war: install
	cd docs/quickstart/assessment && mvn package

demo-run-message:
	@echo -e '\033[0;32m** Demo package complete **\033[0m'
	@echo Now copy docs/quickstart/assessment/webapps/quickstart*.war to the webapps
	@echo directory of a servlet container like Jetty or Tomcat, or use Docker:
	@echo
	@echo 'docker container run --rm -d -v $$(pwd)/docs/quickstart/assessment/webapps:/var/lib/jetty/webapps -p 9280:8080 jetty:11.0.7-jdk11'
	@echo
.PHONY: demo-run-message

quickstart-clean:
	cd docs/quickstart/assessment && mvn clean
	rm -rf docs/quickstart/assessment/webapps

endif
