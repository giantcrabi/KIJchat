/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_server;

import java.util.ArrayList;

/**
 *
 * @author santen-suru
 */
public class Group {
    // Group-User list
    private ArrayList<Pair<String,String>> _grouplist = new ArrayList<>();
    
    Group() {
        _grouplist.add(new Pair("Admin", "Admin"));
        _grouplist.add(new Pair("Admin", "Andi"));
        _grouplist.add(new Pair("User", "Budi"));
        _grouplist.add(new Pair("User", "Rudi"));
        _grouplist.add(new Pair("User", "Luci"));
    }
    
    Group(ArrayList<Pair<String,String>> _grouplist) {
        this._grouplist.clear();
        for (int i = 0; i<_grouplist.size(); i++) {
            this._grouplist.add(new Pair(_grouplist.get(i).getFirst(), _grouplist.get(i).getSecond()));
        }
    }
    
    public ArrayList<Pair<String,String>> getGroupList() {
        return _grouplist;
    }
    
    public int updateGroup(String groupName, String user, ArrayList<Pair<String,String>> _grouplist) {
        _grouplist.add(new Pair(groupName, user));
        
        return this.countGroup();
    }
    
    private int countGroup() {
        ArrayList<String> listGroup = new ArrayList<>();
        int count = 0;
        for (Pair<String, String> selGroup : _grouplist) {
            if (listGroup.contains(selGroup.getFirst()) == false) {
                count++;
                listGroup.add(selGroup.getFirst());
            }
        }
        
        return count;
    }
}
