# What's this?
Liferay Cloning Tool is an OSGi based application that offers administrators an easy way to prepare a Liferay environment for development or test purposes after duplicating it from a production environment. The goal is to avoid any unintentional effects on production systems caused by a development/test environment, to hide potentially sensitive data from developers and also to make the system easier to use for them.

By default it has 5 built-in steps:
* Updating user passwords to let developers use specific accounts (especially Administrator)
* Deleting password policies so e.g. they won’t expire
* Updating remote staging data to avoid publishing to an actual remote live server from a development environment
* Updating user data to prevent developers from seeing e.g. the Skype ID or the address of the users of the production portal
* Updating virtual hosts to avoid collisions with the actual production system’s virtual hosts

Being a modular OSGi application, the Cloning Tool can be easily customized or extended by custom processes besides the built-in ones.

A technical documentation can be found in the documentation folder of the master branch: https://github.com/giros/liferay-cloning/blob/master/documentation/LiferayCloningTool-TechnicalDocumentation.pdf

# Versioning

The latest version is 1.0.0. Versioning follows the guidelines of semantic versioning: http://semver.org

# Planned features

* A simple UI for setting up the configuration instead of manually editing the .cfg file
* An initial database export/import step that creates a dump of the production database and imports it in the development database
* An option to automatically set up the target development portal in WeDeploy

# Contribute

All contributions and bug fixes are welcome! Pull requests can be sent for the master branch of https://github.com/giros/liferay-cloning