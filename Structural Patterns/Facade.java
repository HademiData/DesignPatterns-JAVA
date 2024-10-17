 import java.io.File;

public class Facade {

    /*
     * Facade is a structural design pattern that provides a
     *  simplified (but limited) interface to a complex system 
     * of classes, library or framework.

    While Facade decreases the overall 
    complexity of the application, it also 
    helps to move unwanted dependencies to one place.
     */


     //The Facade pattern is commonly used in apps written in Java. 
     //It’s especially handy when working with complex libraries and APIs.


     /*
      * In this example, the Facade simplifies communication with 
      a complex video conversion framework.

        The Facade provides a single class with a single 
        method that handles all the complexity of configuring the 
        right classes of the framework and retrieving 
        the result in a correct format.
      */

      public class VideoFile {
        private String name;
        private String codecType;
    
        public VideoFile(String name) {
            this.name = name;
            this.codecType = name.substring(name.indexOf(".") + 1);
        }
    
        public String getCodecType() {
            return codecType;
        }
    
        public String getName() {
            return name;
        }
    }

    public interface Codec {
    }

        
    public class MPEG4CompressionCodec implements Codec {
        public String type = "mp4";

    }
    
    public class OggCompressionCodec implements Codec {
        public String type = "ogg";
    }

        
    public class CodecFactory {
        public static Codec extract(VideoFile file) {
            Facade facade = new Facade();
            String type = file.getCodecType();
            if (type.equals("mp4")) {
                System.out.println("CodecFactory: extracting mpeg audio...");
                return facade.new MPEG4CompressionCodec();
            }
            else {
                System.out.println("CodecFactory: extracting ogg audio...");
                return facade.new OggCompressionCodec();
            }
        }
    }


    public class BitrateReader {
        public static VideoFile read(VideoFile file, Codec codec) {
            System.out.println("BitrateReader: reading file...");
            return file;
        }
    
        public static VideoFile convert(VideoFile buffer, Codec codec) {
            System.out.println("BitrateReader: writing file...");
            return buffer;
        }
    }

   

    public class AudioMixer {
        public File fix(VideoFile result){
            System.out.println("AudioMixer: fixing audio...");
            return new File("tmp");
        }
    }

    public class VideoConversionFacade {
        public File convertVideo(String fileName, String format) {
            System.out.println("VideoConversionFacade: conversion started.");
            VideoFile file = new VideoFile(fileName);
            Codec sourceCodec = CodecFactory.extract(file);
            Codec destinationCodec;
            if (format.equals("mp4")) {
                destinationCodec = new MPEG4CompressionCodec();
            } else {
                destinationCodec = new OggCompressionCodec();
            }
            VideoFile buffer = BitrateReader.read(file, sourceCodec);
            VideoFile intermediateResult = BitrateReader.convert(buffer, destinationCodec);
            File result = (new AudioMixer()).fix(intermediateResult);
            System.out.println("VideoConversionFacade: conversion completed.");
            return result;
        }
    }

    public class Demo {
        public static void main(String[] args) {
            Facade facade = new Facade();
            VideoConversionFacade converter = facade.new VideoConversionFacade();
            File mp4Video = converter.convertVideo("youtubevideo.ogg", "mp4");
            // ...
        }
    }
    
}
