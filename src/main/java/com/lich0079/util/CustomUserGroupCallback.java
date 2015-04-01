package com.lich0079.util;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.task.UserGroupCallback;

public class CustomUserGroupCallback extends BaseLogAble  implements UserGroupCallback {

    public CustomUserGroupCallback() {
//        System.out.println("CustomUserGroupCallback init");
    }
	public boolean existsGroup(String groupId) {
	    log("existsGroup:"+groupId);
		return groupId.equals("PM") || groupId.equals("HR") || groupId.equals("PD") || groupId.equals("Administrators");
	}

	public boolean existsUser(String userId) {
	    log("existsUser:"+userId);
		return userId.equals("tom") || userId.equals("jerry") || userId.equals("Administrator") || userId.equals("jack");
	}

	public List<String> getGroupsForUser(String userId, List<String> groupIds,
			List<String> allExistingGroupIds) {
	    log("getGroupsForUser:"+userId+",groupIds:"+groupIds+",allExistingGroupIds:"+allExistingGroupIds);
		List<String> groups = new ArrayList<String>();
        if (userId.equals("jerry"))
            groups.add("PM");
        else if (userId.equals("tom"))
            groups.add("HR");
        else if (userId.equals("jack"))
            groups.add("PD");
        else if (userId.equals("Administrator"))
            groups.add("Administrators");
        return groups;
	}

}
