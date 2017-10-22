package com.example.skarwa.letmeethappen.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by skarwa on 10/12/17.
 */

@IgnoreExtraProperties
@Parcel
public class Group {
    String mid;
    String mName;
    String mCreatedDate;
    String mExpiredDate;
    UserGroupStatus mGroupStatus;
    Map<String,Boolean> mMembers;
    Map<String,String> mTokens;

    public Group() {
        //empty constructor needed
    }

    public void setId(String mid) {
        this.mid = mid;
    } 
    
    public String getId() {
        return mid;
    }

    public Map<String, String> getTokenSet() {
            return mTokens;
        }

     public void setTokenSet(Map<String, String> tokens) {
            this.mTokens = tokens;
     }

     public void addToken(String userId, String tokenId) {
         if(this.mTokens == null){
             this.mTokens = new HashMap<>();
         }
         this.mTokens.put(userId,tokenId);
     }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setCreatedDate(String mCreatedDate) {
        this.mCreatedDate = mCreatedDate;
    }

    public void setExpiredDate(String mExpiredDate) {
        this.mExpiredDate = mExpiredDate;
    }



    public String getName() {
        return mName;
    }

    public String getCreatedDate() {
        return mCreatedDate;
    }

    public String getExpiredDate() {
        return mExpiredDate;
    }

    @Exclude
    public UserGroupStatus getGroupStatusAsEnum() {
        return mGroupStatus;
    }

    public String getGroupStatus() {
        // Convert enum to string
        if (mGroupStatus == null) {
            return null;
        } else {
            return mGroupStatus.name();
        }
    }

    public void setGroupStatus(String status) {
        // Get enum from string
        if (status == null) {
            this.mGroupStatus = null;
        } else {
            this.mGroupStatus = UserGroupStatus.valueOf(status);
        }
    }

    public Map<String, Boolean> getMembers() {
        return mMembers;
    }

    public void setMembers(Map<String, Boolean> groups) {
        this.mMembers = groups;
    }

    public void addMember(String userId,boolean isMember) {
        if(this.mMembers == null){
            this.mMembers = new HashMap<>();
        }
        this.mMembers.put(userId,isMember);
    }
}
