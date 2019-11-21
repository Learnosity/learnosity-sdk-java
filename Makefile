ENV=prod
REGION=.learnosity.com
# Data API
VER=v1

all: build test

dist: prodbuild

test: test-unit test-integration-env

clean: build-clean

dist-check-version: PKG_VER=v$(shell sed -n "s/^.*<version>\([^']\+\)<\/version>.*$$/\1/p;T;q" pom.xml)
dist-check-version: GIT_TAG=$(shell git describe --tags)
dist-check-version:
ifeq ('$(shell echo $(GIT_TAG) | grep -qw "$(PKG_VER)")', '')
	$(error Version number $(PKG_VER) in setup.py does not match git tag $(GIT_TAG))
endif

prodbuild: dist-check-version build test
	mvn package
devbuild: build
build:
	mvn compile

test-unit:
	mvn -q test
test-integration-env:
	mvn -q integration-test

build-clean:
	mvn clean

.PHONY: prodbuild devbuild build \
	test-unit \
	build-clean
