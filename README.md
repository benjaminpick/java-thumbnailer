Thumbnailer
===========

This is a Java Plugin for creating Thumbnails of files during the crawling process of regain.<br>
(Regain is a Lucene-based desktop search engine.)

*Status* : Alpha (Creation of Thumbnails working)<br>
*Licence* : GNU GPL 2.1 or later

Roadmap
-------

* Creation of Thumbnails *(DONE 14.06.2011)*
* Add capability for Crawling Plugins to regain (see [forum](http://forum.murfman.de/de/viewtopic.php?f=13&t=1151)) *(ALMOST DONE)*
  * Document in Wiki!
  * Config via XML (DONE)
  * Patch upstream
* Integration into regain (as a Plugin) *(ALMOST DONE)*
* Packaging in .jars / dynamic loading of .jars (DONE)
* Seperation from regain lib-wise (so that it can be used as stand-alone/library) *(ALMOST DONE)*

* Show Thumbnails in results if available
  * Create Thumbnail-Tag

(Should be done by Sept 2011).


Supported Fileformats
---------------------

* Office files (doc, docx, xls, xlsx, ppt, pptx)
* OpenOffice files (all of them)
* Text files (txt, pdf, rtf)
* Image files (jpg, png, bmp, gif, tiff?)
* MIT Scratch files (sb)

(Detection is based on MIME-Type, not filename extension. So files with an incorrect file extension will be treated correctly, not as they deserve.) 

TODO
----

### Thumbnailer:
* Migrate to JODConverter 3beta (reduce hassle of start/stop/document timeout) (DONE)
  * remove log4j info messages
  * Bug: Win: Soffice Process cannot be started (check)
  * soffice hogs cpu 100% once used (only Linux?) (seems ok now)
  * Upgrade to 3beta4 when it appears
  * Find a way to let him fail if he can't convert the file (sb -> png ??)
* PDFBox: Library Conflict with regain. (We need to include this in the plugin, but it is included in a maybe-loaded preparator as well.)
  * That means that both should be updated at the same time!
* Check that all temporary files are deleted during thumbnailing process

### Bugs:

* Image sometimes may need more time (after resize).
  * Never had an exception. I'll leave it in there, though.

### Test: 

* MacOS hasn't been tested yet
* Test on Win again
* MIME Tests aren't correctly working yet

### Nice-to-have:

* .tiff-Support (via ImageMagick?)
* IFilterThumbnailer for Windows
* Run optipng on all pngs (via cron)
* Better performance for PDFBox
* MIME-Detection of Scratch Files (customize mimetypes.xml? or test in MimeTypeDetector?) (let's call it: application/x-mit-scratch)

Author
------

This is a project of the university of Siegen for the benefit of [come_IN Computerclubs](http://come-in.wineme.fb5.uni-siegen.de/index.php?id=en). But of course, if you have patches etc. go ahead!
