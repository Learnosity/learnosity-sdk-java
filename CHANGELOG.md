# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project now adheres to [Semantic
Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Fixed
* Upgrade dependencies for housekeeping (`commons-codec` to 1.13, `commons-lang3` to 3.9, `commons-io` to 2.6, `commons-logging` to 1.2, `httpclient` to 4.5.10, `httpcore` to 4.4.12, and `org.json` to 20190722).
* Updated the test suite to use `JUnit 5`.

## [v0.16.1] - 2019-10-31
### Fixed
* Fixed a bug where forward slashes were being escaped incorrectly, which could cause a signature mismatch to occur.

## [v0.16.0] - 2019-05-06
### Security
* Upgrade dependency `org.apache.httpcomponents:httpclient` to v4.5.8 to patch vulnerabilities.

### Fixed
* Fixed an issue where the `DataApi` class's `requestRecursive` method would throw an exception upon receiving a response from Data API endpoints that set the "data" field of the response to an object (like the `itembank/questions` endpoint when `item_references` is included in the request).
* Upgrade additional dependencies for housekeeping (`commons-codec` to 1.11, `commons-logging` to 1.2, `org.apache.httpcomponents-httpcore` to 4.4.11, and `org.json` to 20180813).

### Added
* Telemetry data (basic information about the execution environment) is now added to the to the request objects being signed which is later read and logged internally by our APIs when the request is received. This allows us to better support our various SDKs and does not send any additional network requests. More information can be found in README.md.

## [v0.15] - 2017-08-28

* Don't Unescape escaped sequences not created by JSONObject.toString()
* Fix != on Strings
* Remove call to nonexistent Remote.getError()

## [v0.14] - 2017-04-18

* Unescape escaped sequences created by JSONObject.toString()

## [v0.13] - 2017-03-13

* Replace escaped forward slashes with unescaped forward slashes after stringifying the json object

## [v0.12] - 2015-12-04

* Making exceptions in the request visible to users

## [v0.11] - 2015-11-21

* Setting timeout defaults for requests and allowing user to set custom request configuration

## [v0.10] - 2015-06-05

* Fixed a bug in hashing users for events api

## [v0.9] - 2015-06-05

* Added support for events api

## [v0.8] - 2015-05-13

* Changed the constructor signature from public Init (String service, Object securityPacket, String secret, Object requestPacket, String action) to public Init (String service, Object securityPacket, String secret, Object requestPacket) and added a public method setAction
