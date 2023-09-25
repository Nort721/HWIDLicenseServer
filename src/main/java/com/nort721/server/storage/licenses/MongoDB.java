package com.nort721.server.storage.licenses;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.nort721.server.Main;
import org.bson.Document;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MongoDB {

    /**
     * Get count of hwids under a license
     * @param license license id
     * @return the amount of hwids under the license id
     */
    public static int getHWIDCount(String license) {
        Document document = (Document) Main.getMongoManager().getLicenseCollection().find(Filters.eq("license", license)).first();

        if (document == null) {
            return 0;
        }

        int count = 0;

        for (String key : document.keySet())
            if (key.contains("hwid"))
                count++;

        return count;
    }

    /**
     * Get a list of the hwids under the license
     * @param license the license
     * @return a list of all the hwids under the license
     */
    public static List<String> getHWIDs(String license) {
        Document document = (Document) Main.getMongoManager().getLicenseCollection().find(Filters.eq("license", license)).first();

        if (document == null) {
            return null;
        }

        List<String> ids = new LinkedList<>();

        for (String key : document.keySet())
            if (key.contains("hwid"))
                ids.add(document.getString(key));

        return ids;
    }

    /**
     * Use to remove a specific hwid from a license
     * @param license the license that the document belongs to
     * @param key the specific data you want to change
     */
    public static void removeHWIDFromLicense(String license, String key) {

        FindIterable iterable = Main.getMongoManager().getLicenseCollection().find(Filters.eq("license", license));

        // update the datatbase
        if (iterable.first() != null) {
            // Update the values in existing document
            Document document = (Document) Main.getMongoManager().getLicenseCollection().find(Filters.eq("license", license)).first();

            if (document.containsKey(key))
                document.remove(key);

            Main.getMongoManager().getLicenseCollection().replaceOne(Filters.eq("license", license), document);
        }

    }

    /**
     * Use to remove a specific hwid from a license
     * @param license the license that the document blongs to
     */
    public static void resetHWIDFromLicense(String license) {

        FindIterable iterable = Main.getMongoManager().getLicenseCollection().find(Filters.eq("license", license));

        // update the datatbase
        if (iterable.first() != null) {
            // Update the values in existing document
            Document document = (Document) Main.getMongoManager().getLicenseCollection().find(Filters.eq("license", license)).first();

            ArrayList<String> ipKeys = new ArrayList<>();

            for (String key : document.keySet()) {
                if (key.contains("hwid"))
                    ipKeys.add(key);
            }

            for (String key : ipKeys) {
                document.remove(key);
            }

            document.replace("count", 0);

            Main.getMongoManager().getLicenseCollection().replaceOne(Filters.eq("license", license), document);
        }

    }

    /**
     * Use to delete a license from the database
     * @param license the license that the document belongs to
     */
    public static void removeLicense(String license) {

        FindIterable iterable = Main.getMongoManager().getLicenseCollection().find(Filters.eq("license", license));

        // update the datatbase
        if (iterable.first() != null) {
            // Update the values in existing document
            Document document = (Document) Main.getMongoManager().getLicenseCollection().find(Filters.eq("license", license)).first();

            Main.getMongoManager().getLicenseCollection().deleteOne(document);
        }

    }


    /**
     * Use this to add a new hwid to a license, or just save a new license with one hwid
     * this method will also check for doubles
     * @param license the license that the document belongs to
     */
    public static void saveLicenseWithHWID(String license, String hwid) {

        FindIterable iterable = Main.getMongoManager().getLicenseCollection().find(Filters.eq("license", license));

        // update the datatbase
        if (iterable.first() == null) {
            // Create a new document and insert the value
            Document document = new Document();
            document.put("license", license);

            // set all the values from the object
            document.put("hwid0", hwid);
            document.put("count", 0);

            Main.getMongoManager().getLicenseCollection().insertOne(document);
        } else {
            // Update the values in existing document
            Document document = (Document) Main.getMongoManager().getLicenseCollection().find(Filters.eq("license", license)).first();

            int count = document.getInteger("count", 0);

            boolean ipAlreadySaved = false;

            // check if license already saved
            for (int i = 0; i < count; i++) {
                if (document.getString("hwid" + i).equals(hwid)) {
                    ipAlreadySaved = true;
                    break;
                }
            }

            // if ip is not saved already, save it
            if (!ipAlreadySaved) {
                count++;
                document.replace("count", count);
                document.put("hwid" + count, hwid);
            }

            Main.getMongoManager().getLicenseCollection().replaceOne(Filters.eq("license", license), document);

        }

    }

    /**
     * Add a license to database
     * @param license the license that the document blongs to
     */
    public static void addLicenseToDatabase(String license) {

        FindIterable iterable = Main.getMongoManager().getLicenseCollection().find(Filters.eq("license", license));

        // update the datatbase
        if (iterable.first() == null) {
            // Create a new document and insert the value
            Document document = new Document();
            document.put("license", license);

            Main.getMongoManager().getLicenseCollection().insertOne(document);
        }

    }

    /**
     * Does license exist in database
     * @param license the license that the document blongs to
     */
    public static boolean containsLicense(String license) {
        FindIterable iterable = Main.getMongoManager().getLicenseCollection().find(Filters.eq("license", license));
        return iterable.first() != null;
    }
}
