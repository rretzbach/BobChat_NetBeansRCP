BobChat
=======

An IRC Desktop Client using Netbeans' sophisticated Window System

![](https://raw.github.com/rretzbach/BobChat/master/branding/core/core.jar/org/netbeans/core/startup/splash.gif)

How to build
============

1. Clone
1. Open project in Netbeans
1. Mark project as main project
1. Run main project

Testmode
========

To simplify testing the client a mock object was created which allows running the application without actually connecting to any IRC network.

To switch to the real thing change the following statement in the constructor of the Network class:

    this.bot = new TestBot(identity)
    
to

    this.bot = new MainBot(identity)
    
Current version
===============

This bot is primarily used only by me and is still in an early alpha version. Though I will accept pull requests if you are willing to contribute.

The following actions are currently possible:

* Connect to multiple networks
* Join multiple channels
* Display channel join/part/notice/me/msg events
* Display query msg/me events
* Issue the /msg command
* Persistent window layout between sessions
* Persistent networks/channel/query navigation tree (explorer)
