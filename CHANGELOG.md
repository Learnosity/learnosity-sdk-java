## v0.15 (2017-08-28)

* Don't Unescape escaped sequences not created by JSONObject.toString()
* Fix != on Strings
* Remove call to nonexistent Remote.getError()

## v0.14 (2017-04-18)

* Unescape escaped sequences created by JSONObject.toString()

## v0.13 (2017-03-13)

* Replace escaped forward slashes with unescaped forward slashes after stringifying the json object

## v0.12 (2015-12-04)

* Making exceptions in the request visible to users

## v0.11 (2015-11-21)

* Setting timeout defaults for requests and allowing user to set custom request configuration

## v0.10 (2015-06-05)

* Fixed a bug in hashing users for events api

## v0.9 (2015-06-05)

* Added support for events api

## v0.8 (2015-05-13)

* Changed the constructor signature from public Init (String service, Object securityPacket, String secret, Object requestPacket, String action) to public Init (String service, Object securityPacket, String secret, Object requestPacket) and added a public method setAction
