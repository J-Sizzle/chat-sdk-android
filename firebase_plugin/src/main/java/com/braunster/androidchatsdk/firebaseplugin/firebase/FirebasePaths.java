/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:34 PM
 */

package com.braunster.androidchatsdk.firebaseplugin.firebase;

import com.braunster.chatsdk.network.BDefines;
import co.chatsdk.core.defines.FirebaseDefines;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.braunster.chatsdk.network.BDefines.ServerUrl;

public class FirebasePaths{

    public static final char Separator = '/';

    public static final String UsersPath = "users";
    public static final String MessagesPath = "messages";
    public static final String ThreadsPath = "threads";
    public static final String PublicThreadsPath = "public-threads";
    public static final String DetailsPath = "details";
    public static final String IndexPath = "searchIndex";
    public static final String OnlinePath = "online";
    public static final String MetaPath = "meta";
    public static final String FollowersPath = "followers";
    public static final String FollowingPath = "follows";
    public static final String Image = "imaeg";
    public static final String Thumbnail = "thumbnail";

    private String url;
    private static StringBuilder builder = new StringBuilder();

    private FirebasePaths(String url) {
        this.url = url;
    }

    /* Not sure if this the wanted implementation but its give the same result as the objective-C code.*/
    /** @return The main databse ref.*/
    public static DatabaseReference firebaseRef(){
        if (StringUtils.isBlank(ServerUrl))
            throw new NullPointerException("Please set the server url in BDefines class");

        return fb(ServerUrl);
    }

    /** @return Firebase object for give url.*/
    private static DatabaseReference fb (String url){
        return FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    /* Users */
    /** @return The users main ref.*/
    public static DatabaseReference usersRef(){
        return firebaseRef().child(UsersPath);
    }

    /** @return The user ref for given id.*/
    public static DatabaseReference userRef(String firebaseId){
        return usersRef().child(firebaseId);
    }

    /** @return The user threads ref.*/
    public static DatabaseReference userThreadsRef(String firebaseId){
        return usersRef().child(firebaseId).child(ThreadsPath);
    }

    /** @return The user meta ref for given id.*/
    public static DatabaseReference userMetaRef(String firebaseId){
        return usersRef().child(firebaseId).child(MetaPath);
    }

    public static DatabaseReference userOnlineRef(String firebaseId){
        return userRef(firebaseId).child(OnlinePath);
    }

    public static DatabaseReference userFollowingRef(String firebaseId){
        return userRef(firebaseId).child(FollowingPath);
    }

    public static DatabaseReference userFollowersRef(String firebaseId){
        return userRef(firebaseId).child(FollowersPath);
    }

    /* Threads */
    /** @return The thread main ref.*/
    public static DatabaseReference threadRef(){
        return firebaseRef().child(ThreadsPath);
    }

    /** @return The thread ref for given id.*/
    public static DatabaseReference threadRef(String firebaseId){
        return threadRef().child(firebaseId);
    }

    public static DatabaseReference threadMessagesRef(String firebaseId){
        return threadRef(firebaseId).child(MessagesPath);
    }
    
    public static DatabaseReference publicThreadsRef(){
        return firebaseRef().child(PublicThreadsPath);
    }

    /* Index */
    public static DatabaseReference indexRef(){
        return firebaseRef().child(IndexPath);
    }

    @Deprecated
    public static Map<String, Object> getMap(String[] keys,  Object...values){
        Map<String, Object> map = new HashMap<String, Object>();

        for (int i = 0 ; i < keys.length; i++){

            // More values then keys entered.
            if (i == values.length)
                break;

            map.put(keys[i], values[i]);
        }

        return map;
    }

//    public static int providerToInt(String provider){
//        if (provider.equals(BDefines.ProviderString.Password))
//        {
//            return BDefines.ProviderInt.Password;
//        }
//        else if (provider.equals(BDefines.ProviderString.Facebook))
//        {
//            return BDefines.ProviderInt.Facebook;
//        }
//        else if (provider.equals(BDefines.ProviderString.Google))
//        {
//            return BDefines.ProviderInt.Google;
//        }
//        else if (provider.equals(BDefines.ProviderString.Twitter))
//        {
//            return BDefines.ProviderInt.Twitter;
//        }
//        else if (provider.equals(BDefines.ProviderString.Anonymous))
//        {
//            return BDefines.ProviderInt.Anonymous;
//        }
//        else if (provider.equals(BDefines.ProviderString.Custom))
//        {
//            return BDefines.ProviderInt.Custom;
//        }
//
//        throw new IllegalArgumentException("No provider was found matching requested. Provider: " + provider);
//    }

//    public static String providerToString(int provider){
//
//        switch (provider){
//            case BDefines.ProviderInt.Password:
//                return BDefines.ProviderString.Password;
//            case BDefines.ProviderInt.Facebook:
//                return BDefines.ProviderString.Facebook;
//            case BDefines.ProviderInt.Google:
//                return BDefines.ProviderString.Google;
//            case BDefines.ProviderInt.Twitter:
//                return BDefines.ProviderString.Twitter;
//            case BDefines.ProviderInt.Anonymous:
//                return BDefines.ProviderString.Anonymous;
//            case BDefines.ProviderInt.Custom:
//                return BDefines.ProviderString.Custom;
//
//            default:
//                /*return ProviderString.Password;*/
//                throw new IllegalArgumentException("Np provider was found matching requested.");
//        }
//    }
}
