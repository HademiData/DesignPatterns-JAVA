
import java.util.HashMap;




public class Proxy {
    
    /*
     * Proxy is a structural design pattern that lets you provide a 
     * substitute or placeholder for another object. A proxy controls 
     * access to the original object, allowing you to perform something 
     * either before or after the request gets through to the original object.
     */

     /*
      * The Proxy pattern suggests that you create a new proxy class with the same 
      interface as an original service object. Then you update your app so that 
      it passes the proxy object to all of the original object’s clients. Upon
       receiving a request from a client, the proxy creates a real service object 
       and delegates all the work to it.
      */

      /*
       * Proxy is a structural design pattern that provides an object that acts as a
       *  substitute for a real service object used by a client. A proxy receives client
       *  requests, does some work (access control, caching, etc.) and then
       *  passes the request to a service object. The proxy object has the 
       * same interface as a service, which makes 
       * it interchangeable with a real object when passed to a client.
       * 
       */


       /*
        * Caching proxy
        In this example, the Proxy pattern helps to implement the lazy initialization 
        and caching to an inefficient 3rd-party YouTube integration library.

        Proxy is invaluable when you have to add some additional 
        behaviors to a class which code you can’t change.
        */

    public interface ThirdPartyYouTubeLib {
        HashMap<String, Video> popularVideos();
        
        Video getVideo(String videoId);
    }
            
    public class ThirdPartyYouTubeClass implements ThirdPartyYouTubeLib {

        @Override
        public HashMap<String, Video> popularVideos() {
            connectToServer("http://www.youtube.com");
            return getRandomVideos();
        }

        @Override
        public Video getVideo(String videoId) {
            connectToServer("http://www.youtube.com/" + videoId);
            return getSomeVideo(videoId);
        }

        // -----------------------------------------------------------------------
        // Fake methods to simulate network activity. They as slow as a real life.

        private int random(int min, int max) {
            return min + (int) (Math.random() * ((max - min) + 1));
        }

        private void experienceNetworkLatency() {
            int randomLatency = random(5, 10);
            for (int i = 0; i < randomLatency; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void connectToServer(String server) {
            System.out.print("Connecting to " + server + "... ");
            experienceNetworkLatency();
            System.out.print("Connected!" + "\n");
        }

        private HashMap<String, Video> getRandomVideos() {
            System.out.print("Downloading populars... ");

            experienceNetworkLatency();
            HashMap<String, Video> hmap = new HashMap<String, Video>();
            hmap.put("catzzzzzzzzz", new Video("sadgahasgdas", "Catzzzz.avi"));
            hmap.put("mkafksangasj", new Video("mkafksangasj", "Dog play with ball.mp4"));
            hmap.put("dancesvideoo", new Video("asdfas3ffasd", "Dancing video.mpq"));
            hmap.put("dlsdk5jfslaf", new Video("dlsdk5jfslaf", "Barcelona vs RealM.mov"));
            hmap.put("3sdfgsd1j333", new Video("3sdfgsd1j333", "Programing lesson#1.avi"));

            System.out.print("Done!" + "\n");
            return hmap;
        }

        private Video getSomeVideo(String videoId) {
            System.out.print("Downloading video... ");

            experienceNetworkLatency();
            Video video = new Video(videoId, "Some video title");

            System.out.print("Done!" + "\n");
            return video;
        }

    }

    public class Video {
        public String id;
        public String title;
        public String data;
    
        Video(String id, String title) {
            this.id = id;
            this.title = title;
            this.data = "Random video.";
        }
    }

    public class YouTubeCacheProxy implements ThirdPartyYouTubeLib {
        private ThirdPartyYouTubeLib youtubeService;
        private HashMap<String, Video> cachePopular = new HashMap<String, Video>();
        private HashMap<String, Video> cacheAll = new HashMap<String, Video>();
    
        public YouTubeCacheProxy() {
            this.youtubeService = new ThirdPartyYouTubeClass();
        }
    
        @Override
        public HashMap<String, Video> popularVideos() {
            if (cachePopular.isEmpty()) {
                cachePopular = youtubeService.popularVideos();
            } else {
                System.out.println("Retrieved list from cache.");
            }
            return cachePopular;
        }
    
        @Override
        public Video getVideo(String videoId) {
            Video video = cacheAll.get(videoId);
            if (video == null) {
                video = youtubeService.getVideo(videoId);
                cacheAll.put(videoId, video);
            } else {
                System.out.println("Retrieved video '" + videoId + "' from cache.");
            }
            return video;
        }
    
        public void reset() {
            cachePopular.clear();
            cacheAll.clear();
        }
    }

    public class YouTubeDownloader {
        private ThirdPartyYouTubeLib api;
    
        public YouTubeDownloader(ThirdPartyYouTubeLib api) {
            this.api = api;
        }
    
        public void renderVideoPage(String videoId) {
            Video video = api.getVideo(videoId);
            System.out.println("\n-------------------------------");
            System.out.println("Video page (imagine fancy HTML)");
            System.out.println("ID: " + video.id);
            System.out.println("Title: " + video.title);
            System.out.println("Video: " + video.data);
            System.out.println("-------------------------------\n");
        }
    
        public void renderPopularVideos() {
            HashMap<String, Video> list = api.popularVideos();
            System.out.println("\n-------------------------------");
            System.out.println("Most popular videos on YouTube (imagine fancy HTML)");
            for (Video video : list.values()) {
                System.out.println("ID: " + video.id + " / Title: " + video.title);
            }
            System.out.println("-------------------------------\n");
        }
    }

    public class Demo {

        public static void main(String[] args) {
            Proxy proxy = new Proxy();
            YouTubeDownloader naiveDownloader = proxy.new YouTubeDownloader(proxy.new ThirdPartyYouTubeClass());
            YouTubeDownloader smartDownloader = proxy.new YouTubeDownloader(proxy.new YouTubeCacheProxy());
    
            long naive = test(naiveDownloader);
            long smart = test(smartDownloader);
            System.out.print("Time saved by caching proxy: " + (naive - smart) + "ms");
    
        }
    
        private static long test(YouTubeDownloader downloader) {
            long startTime = System.currentTimeMillis();
    
            // User behavior in our app:
            downloader.renderPopularVideos();
            downloader.renderVideoPage("catzzzzzzzzz");
            downloader.renderPopularVideos();
            downloader.renderVideoPage("dancesvideoo");
            // Users might visit the same page quite often.
            downloader.renderVideoPage("catzzzzzzzzz");
            downloader.renderVideoPage("someothervid");
    
            long estimatedTime = System.currentTimeMillis() - startTime;
            System.out.print("Time elapsed: " + estimatedTime + "ms\n");
            return estimatedTime;
        }
    }

}
