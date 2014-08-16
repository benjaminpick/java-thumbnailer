package jirau;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.uni_siegen.wineme.come_in.thumbnailer.thumbnailers.AbstractThumbnailer;

public class DWGThumbnailer extends AbstractThumbnailer {

	public void generateThumbnail(File input, File output) throws IOException, de.uni_siegen.wineme.come_in.thumbnailer.ThumbnailerException {
		//GENERATE FROM EXISTING BITMAP IN DWG
		byte[] outputByte = new byte[4096];
		FileInputStream fis = new FileInputStream(input);
		fis.skip(0x0D);
		fis.read(outputByte, 0, 4);
		int PosSentinel = (((outputByte[3])&0xFF)*256*256*256)+(((outputByte[2])&0xFF)*256*256)+(((outputByte[1])&0xFF)*256)+((outputByte[0])&0xFF);
		fis.skip(PosSentinel-0x0D-4+30);
		outputByte[1]=0;
		fis.read(outputByte, 0, 1);
		int TypePreview = ((outputByte[0])&0xFF);
		if (TypePreview==2) {
			fis.read(outputByte, 0, 4);
			int PosBMP = (((outputByte[3])&0xFF)*256*256*256)+(((outputByte[2])&0xFF)*256*256)+(((outputByte[1])&0xFF)*256)+((outputByte[0])&0xFF);
			fis.read(outputByte, 0, 4);
			int LenBMP = (((outputByte[3])&0xFF)*256*256*256)+(((outputByte[2])&0xFF)*256*256)+(((outputByte[1])&0xFF)*256)+((outputByte[0])&0xFF);
			fis.skip(PosBMP-(PosSentinel+30)-1-4-4+14);
			fis.read(outputByte, 0, 2);
			int biBitCount = (((outputByte[1])&0xFF)*256)+((outputByte[0])&0xFF);
			fis.skip(-16);
			int bisSize=0;
			int bfSize = 0;
			if (biBitCount<9)
				bfSize = 54 + 4 * ((int) (Math.pow(2,biBitCount))) + LenBMP ;
			else bfSize = 54 + LenBMP ;
			//WORD "BM"
			outputByte[0]=0x42;outputByte[1]=0x4D;
			//DWORD bfSize
			outputByte[2]=(byte)(bfSize&0xff);outputByte[3]=(byte)(bfSize>>8&0xff);
			outputByte[4]=(byte)(bfSize>>16&0xff);outputByte[5]=(byte)(bfSize>>>24);
			//WORD bfReserved1
			outputByte[6]=0x00;outputByte[7]=0x00;
			//WORD bfReserved2
			outputByte[8]=0x00;outputByte[9]=0x00;
			//DWORD bfOffBits
			outputByte[10]=0x36;outputByte[11]=0x04;outputByte[12]=0x00;outputByte[13]=0x00;

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(outputByte,0,14);
			while((LenBMP>0) && ((bisSize=fis.read(outputByte, 0, (LenBMP>4096?4096:LenBMP))) != -1)) {
				baos.write(outputByte, 0, bisSize);
				LenBMP-=bisSize;
			}
			
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

			try {
				BufferedImage originalImage = ImageIO.read(bais);
			    float scaleWidth = ((float) this.thumbWidth) / originalImage.getWidth();
			    float scaleHeight = ((float) this.thumbHeight) / originalImage.getHeight();
			    int newWidth = this.thumbWidth;
			    int newHeight = this.thumbHeight;
			    //PRESERVE ASPECT RATIO
			    if (scaleWidth<scaleHeight)
			    	newHeight=Math.round(newHeight*scaleWidth);
			    else newWidth=Math.round(newWidth*scaleHeight);
			    BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			    Graphics g = newImage.createGraphics();
			    g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
			    g.dispose();
			    
				ImageIO.write(newImage,"jpg",output);
			} catch (Exception e) {
				//STILL TO DO: support 32bit bitmap rescaling
				bais.reset();
				FileOutputStream fos = new FileOutputStream(output);
				while ((bisSize=bais.read(outputByte, 0, 4096)) != -1) 
					fos.write(outputByte, 0, bisSize);
				fos.close();
			}
		}
		fis.close();
	}

	public String[] getAcceptedMIMETypes() {
		return new String[]{ "image/x-dwg" };
	}
		
}
