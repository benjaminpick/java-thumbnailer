Thumbnailer
===========

This is a Java Plugin for creating Thumbnails of files during the crawling process of regain.<br>
It can also be used as a standalone library to create thumbnails of different file types (image, text).<br>
(Regain is a Lucene-based desktop search engine.)

*Current Version* : 0.4 (Beta)<br>
*Licence* : GNU GPL 2.1 or later

Requirements
------------

* Java JRE 1.6
* (optional) OpenOffice 3.x/LibreOffice
* Tested in Windows/Linux/Mac

Supported Fileformats
---------------------

* Office files (doc, docx, xls, xlsx, ppt, pptx)
* OpenOffice files (all of them)
* Text files (txt, pdf, rtf, html)
* Image files (jpg, png, bmp, gif)
* MIT Scratch files (sb)

(Detection is based on MIME-Type, not filename extension. So files with an incorrect file extension will be treated correctly, not as they deserve.) 

Output are always PNG files. The dimension of this Thumbnail can be configured.

TODO
----

### Thumbnailer:
* JODConverter 3beta
  * Upgrade to 3beta5 when it appears
  * remove log4j info messages
  * Find a way to let him fail if he can't convert the file (sb is a binary format and really shouldn't be treated as plain text.)
* PDFBox: Library Conflict with regain. (We need to include this in the plugin, but it is included in a maybe-loaded preparator as well.)
  * That means that both should be updated at the same time!

### Test: 

* Java 1.7

Author
------

This is a project of the university of Siegen for the benefit of [come_IN Computerclubs](http://www.computerclub-comein.de). But of course, if you have patches etc. go ahead!
