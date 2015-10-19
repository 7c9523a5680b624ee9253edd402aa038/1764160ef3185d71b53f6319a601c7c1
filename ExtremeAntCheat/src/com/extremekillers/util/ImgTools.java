package com.extremekillers.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImgTools extends java.awt.Panel  {  

	private static final long serialVersionUID = 1L;
	private BufferedImage img;  
	
    public ImgTools(InputStream in) {  
        try {  
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);  
            img = decoder.decodeAsBufferedImage();  
        }  
        catch (Exception e) {  
  
        }  
    }  
    
    public ImgTools(String url) {  
        try {  
            InputStream in = new FileInputStream(url);  
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);  
            img = decoder.decodeAsBufferedImage();  
        }  
        catch (Exception e) {  
  
        }  
    }  
    
    public ImgTools(Image imgobj) {  
        try {  
            img = new BufferedImage(imgobj.getWidth(this), imgobj.getHeight(this), BufferedImage.TYPE_INT_RGB);  
            Graphics2D grapImg = img.createGraphics();  
            grapImg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
            grapImg.drawImage(imgobj,0,0,this);  
        }  
        catch (Exception e) {  
  
        }  
    }  
    public int getWidth () {  
            return img.getWidth();  
    }  
    public int getHeight () {  
            return img.getHeight();  
    }  
    public void resize(int width, int height) {   
        if ((width > 0 && height == 0) || (width == 0 && height > 0))  
        {  
            int size = (width > 0) ? width : height;  
            width = img.getWidth();  
            height = img.getHeight();  
            float fRatio = (float)img.getWidth() / (float)img.getHeight();  
            if (width > size) {  
                width = size;  
                height = (int)((float)size / fRatio);  
            }   
            if (height > size) {  
                height = size;  
                width = (int)((float)size * fRatio);  
            }  
        }  
        BufferedImage imgNew = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
        Graphics2D grapImg = imgNew.createGraphics();  
        grapImg.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);  
        AffineTransform xform = AffineTransform.getScaleInstance((double)width/img.getWidth(), (double)height/img.getHeight());  
        grapImg.drawRenderedImage(img, xform);  
        grapImg.dispose();  
        img = imgNew;  
    }  
    public Image getImage() {  
        return img.getScaledInstance(img.getWidth(), img.getHeight(), Image.SCALE_DEFAULT);  
    }  
    public void setOutputImage(OutputStream out, float resolution) {  
        try {         
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
            JPEGEncodeParam jpegParams = encoder.getDefaultJPEGEncodeParam(img);  
            jpegParams.setQuality(resolution, false);  
            encoder.setJPEGEncodeParam(jpegParams);  
            encoder.encode(img);  
        }  
        catch (Exception e) {  
  
        }  
    }  
}