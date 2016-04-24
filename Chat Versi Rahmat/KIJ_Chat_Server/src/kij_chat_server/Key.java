/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_server;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 *
 * @author rahmat
 */

// class ini dibuat untuk menampung list user dan public key nya
public class Key {
    // Key list
    private ArrayList<Pair<String,PublicKey>> _keylist = new ArrayList<>();
    
    // konstruktor 1
    Key() {
        _keylist.clear();
//        _keylist.add(new Pair("rahmat", null));
//        _keylist.add(new Pair("gian", null));
//        _keylist.add(new Pair("kevin", null));
    }
    
    // konstruktor 2
    Key(ArrayList<Pair<String,String>> _keylist) {
        this._keylist.clear();
        for (int i = 0; i<_keylist.size(); i++) {
            this._keylist.add(new Pair(_keylist.get(i).getFirst(), null));
        }
    }
    
    public ArrayList<Pair<String,PublicKey>> getKeyList() {
        return _keylist;
    }
    
    public int updateKeyList(String user, KeyPair key, ArrayList<Pair<String,String>> _keylist) {
        _keylist.add(new Pair(user, user));
        return this.countGroup();
    }
    
    private int countGroup() {
        ArrayList<String> listGroup = new ArrayList<>();
        int count = 0;
        for (Pair<String, PublicKey> selGroup : _keylist) {
            if (listGroup.contains(selGroup.getFirst()) == false) {
                count++;
                listGroup.add(selGroup.getFirst());
            }
        }
        
        return count;
    }
}
