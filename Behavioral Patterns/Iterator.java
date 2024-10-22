

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Iterator {
    
    // Iterator is a behavioral design pattern that lets you traverse 
    // elements of a collection without exposing its
    //  underlying representation (list, stack, tree, etc.).

    /*
     * Iterator is a behavioral design pattern that allows 
     * sequential traversal through a complex data structure
     *  without exposing its internal details.
     */

     /*
      * Iterating over social network profiles
In this example, the Iterator pattern is used to go over 
social profiles of a remote social network collection without 
exposing any of the communication details to the client code.
      */

    public interface ProfileIterator {
        boolean hasNext();
    
        Profile getNext();
    
        void reset();
    }



    public class FacebookIterator implements ProfileIterator {
        private Facebook facebook;
        private String type;
        private String email;
        private int currentPosition = 0;
        private List<String> emails = new ArrayList<>();
        private List<Profile> profiles = new ArrayList<>();
    
        public FacebookIterator(Facebook facebook, String type, String email) {
                this.facebook = facebook;
                this.type = type;
                this.email = email;
            }

        private void lazyLoad() {
            if (emails.size() == 0) {
                List<String> profiles = facebook.requestProfileFriendsFromFacebook(this.email, this.type);
                for (String profile : profiles) {
                    this.emails.add(profile);
                    this.profiles.add(null);
                }
            }
        }

        @Override
        public boolean hasNext() {
            lazyLoad();
            return currentPosition < emails.size();
        }

        @Override
        public Profile getNext() {
            if (!hasNext()) {
                return null;
            }

            String friendEmail = emails.get(currentPosition);
            Profile friendProfile = profiles.get(currentPosition);
            if (friendProfile == null) {
                friendProfile = facebook.requestProfileFromFacebook(friendEmail);
                profiles.set(currentPosition, friendProfile);
            }
            currentPosition++;
            return friendProfile;
        }

        @Override
        public void reset() {
            currentPosition = 0;
        }
    }

        
    public class LinkedInIterator implements ProfileIterator {
        private LinkedIn linkedIn;
        private String type;
        private String email;
        private int currentPosition = 0;
        private List<String> emails = new ArrayList<>();
        private List<Profile> contacts = new ArrayList<>();

        public LinkedInIterator(LinkedIn linkedIn, String type, String email) {
            this.linkedIn = linkedIn;
            this.type = type;
            this.email = email;
        }

        private void lazyLoad() {
            if (emails.size() == 0) {
                List<String> profiles = linkedIn.requestRelatedContactsFromLinkedInAPI(this.email, this.type);
                for (String profile : profiles) {
                    this.emails.add(profile);
                    this.contacts.add(null);
                }
            }
        }

        @Override
        public boolean hasNext() {
            lazyLoad();
            return currentPosition < emails.size();
        }

        @Override
        public Profile getNext() {
            if (!hasNext()) {
                return null;
            }

            String friendEmail = emails.get(currentPosition);
            Profile friendContact = contacts.get(currentPosition);
            if (friendContact == null) {
                friendContact = linkedIn.requestContactInfoFromLinkedInAPI(friendEmail);
                contacts.set(currentPosition, friendContact);
            }
            currentPosition++;
            return friendContact;
        }

        @Override
        public void reset() {
            currentPosition = 0;
        }
    }


    public interface SocialNetwork {
        ProfileIterator createFriendsIterator(String profileEmail);
    
        ProfileIterator createCoworkersIterator(String profileEmail);
    }

    

    public class Facebook implements SocialNetwork {
        private List<Profile> profiles;
    
        public Facebook(List<Profile> cache) {
            if (cache != null) {
                this.profiles = cache;
            } else {
                this.profiles = new ArrayList<>();
            }
        }
    
        public Profile requestProfileFromFacebook(String profileEmail) {
            // Here would be a POST request to one of the Facebook API endpoints.
            // Instead, we emulates long network connection, which you would expect
            // in the real life...
            simulateNetworkLatency();
            System.out.println("Facebook: Loading profile '" + profileEmail + "' over the network...");
    
            // ...and return test data.
            return findProfile(profileEmail);
        }
    
        public List<String> requestProfileFriendsFromFacebook(String profileEmail, String contactType) {
            // Here would be a POST request to one of the Facebook API endpoints.
            // Instead, we emulates long network connection, which you would expect
            // in the real life...
            simulateNetworkLatency();
            System.out.println("Facebook: Loading '" + contactType + "' list of '" + profileEmail + "' over the network...");
    
            // ...and return test data.
            Profile profile = findProfile(profileEmail);
            if (profile != null) {
                return profile.getContacts(contactType);
            }
            return null;
        }
    
        private Profile findProfile(String profileEmail) {
            for (Profile profile : profiles) {
                if (profile.getEmail().equals(profileEmail)) {
                    return profile;
                }
            }
            return null;
        }

        private void simulateNetworkLatency() {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    
        @Override
        public ProfileIterator createFriendsIterator(String profileEmail) {
            return new FacebookIterator(this, "friends", profileEmail);
        }
    
        @Override
        public ProfileIterator createCoworkersIterator(String profileEmail) {
            return new FacebookIterator(this, "coworkers", profileEmail);
        }
    
    }
    
    public class LinkedIn implements SocialNetwork {
        private List<Profile> contacts;
    
        public LinkedIn(List<Profile> cache) {
            if (cache != null) {
                this.contacts = cache;
            } else {
                this.contacts = new ArrayList<>();
            }
        }
    
        public Profile requestContactInfoFromLinkedInAPI(String profileEmail) {
            // Here would be a POST request to one of the LinkedIn API endpoints.
            // Instead, we emulates long network connection, which you would expect
            // in the real life...
            simulateNetworkLatency();
            System.out.println("LinkedIn: Loading profile '" + profileEmail + "' over the network...");
    
            // ...and return test data.
            return findContact(profileEmail);
        }
    
        public List<String> requestRelatedContactsFromLinkedInAPI(String profileEmail, String contactType) {
            // Here would be a POST request to one of the LinkedIn API endpoints.
            // Instead, we emulates long network connection, which you would expect
            // in the real life.
            simulateNetworkLatency();
            System.out.println("LinkedIn: Loading '" + contactType + "' list of '" + profileEmail + "' over the network...");
    
            // ...and return test data.
            Profile profile = findContact(profileEmail);
            if (profile != null) {
                return profile.getContacts(contactType);
            }
            return null;
        }
    
        private Profile findContact(String profileEmail) {
            for (Profile profile : contacts) {
                if (profile.getEmail().equals(profileEmail)) {
                    return profile;
                }
            }
            return null;
        }
    
        private void simulateNetworkLatency() {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public ProfileIterator createFriendsIterator(String profileEmail) {
            return new LinkedInIterator(this, "friends", profileEmail);
        }
    
        @Override
        public ProfileIterator createCoworkersIterator(String profileEmail) {
            return new LinkedInIterator(this, "coworkers", profileEmail);
        }
    }
    

    public class Profile {
        private String name;
        private String email;
        private Map<String, List<String>> contacts = new HashMap<>();
    
        public Profile(String email, String name, String... contacts) {
            this.email = email;
            this.name = name;
    
            // Parse contact list from a set of "friend:email@gmail.com" pairs.
            for (String contact : contacts) {
                String[] parts = contact.split(":");
                String contactType = "friend", contactEmail;
                if (parts.length == 1) {
                    contactEmail = parts[0];
                }
                else {
                    contactType = parts[0];
                    contactEmail = parts[1];
                }
                if (!this.contacts.containsKey(contactType)) {
                    this.contacts.put(contactType, new ArrayList<>());
                }
                this.contacts.get(contactType).add(contactEmail);
            }
        }
    
        public String getEmail() {
            return email;
        }
    
        public String getName() {
            return name;
        }
    
        public List<String> getContacts(String contactType) {
            if (!this.contacts.containsKey(contactType)) {
                this.contacts.put(contactType, new ArrayList<>());
            }
            return contacts.get(contactType);
        }
    }

        
    public class SocialSpammer {
        public SocialNetwork network;
        public ProfileIterator iterator;

        public SocialSpammer(SocialNetwork network) {
            this.network = network;
        }

        public void sendSpamToFriends(String profileEmail, String message) {
            System.out.println("\nIterating over friends...\n");
            iterator = network.createFriendsIterator(profileEmail);
            while (iterator.hasNext()) {
                Profile profile = iterator.getNext();
                sendMessage(profile.getEmail(), message);
            }
        }

        public void sendSpamToCoworkers(String profileEmail, String message) {
            System.out.println("\nIterating over coworkers...\n");
            iterator = network.createCoworkersIterator(profileEmail);
            while (iterator.hasNext()) {
                Profile profile = iterator.getNext();
                sendMessage(profile.getEmail(), message);
            }
        }

        public void sendMessage(String email, String message) {
            System.out.println("Sent message to: '" + email + "'. Message body: '" + message + "'");
        }
    }

}
    
    
    
        

    

