# Liferay RSS-Web Module Ext for Maintainable Plugins Challenge

In order to customize one option in the RSS-Feed Widget, 
I've created this module that changes the default
implementation's behavior.

The default implementation can only display the 
"description" of an RSS-Feed, while I wanted the full 
content to be displayable as well.

This is the quickest way I found to implement the feature,
but due to being an ext it's quite high-maintenance and
I'm using it as the basis for a "maintainable plugin 
challenge", in search for a better implementation. 
"Better" in the sense of "better maintainable", e.g. 
without intrusive code changes, through interfaces only.

## How to build

I've taken this module straight from a Liferay 
Workspace's `ext` directory. 

The workspace's `gradle.properties` sets the target
platform to 

    liferay.workspace.target.platform.version = 7.2.10.1

(e.g. DXP 7.2 SP1, this probably needs adaption for CE, 
I'm guessing 7.2.1 would have a comparable API level) 

And due to earlier problems with configurable plugins, my 
`settings.gradle` utilize an older version of Liferay 
Workspace (not sure if this is necessary or not): 

	buildscript {
		dependencies {
			classpath group: "com.liferay", name: "com.liferay.gradle.plugins.workspace", version: "2.1.5"
			classpath group: "net.saliman", name: "gradle-properties-plugin", version: "1.4.6"
		}
		repositories {
			maven {
				url "https://repository-cdn.liferay.com/nexus/content/groups/public"
			}
		}
	}
	apply plugin: "net.saliman.properties"
	apply plugin: "com.liferay.workspace"

## The challenge

* Implement the requirement to show an RSS-Feed's full
  content instead of just the description.
* Do that in the most maintainable way, preferably 
  without using an ext plugin
* Document your thought process, and why you believe
  that your version is more maintainable or what you 
  tried to achieve - in case you don't think your version  
  is easier to maintain.
  
## Let me know...

Either way:

* Add a link to your implementation to this README (send a 
  pull request)
* Comment on the [Blog Article](https://liferay.dev/blogs/-/blogs/maintainable-plugins-challenge) 
  publishing this challenge
* Tweet at [olafk](https://twitter.com/olafk)

## And, of course, ...

* subscribe to [Radio Liferay](http://radioliferay.com/) ;)  
