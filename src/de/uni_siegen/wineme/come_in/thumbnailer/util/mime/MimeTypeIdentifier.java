package de.uni_siegen.wineme.come_in.thumbnailer.util.mime;

import java.io.File;
import java.util.List;

public interface MimeTypeIdentifier {
	public String identify(String mimeType, byte[] bytes, File file);
	public List<String> getExtensionsFor(String mimeType);
}
