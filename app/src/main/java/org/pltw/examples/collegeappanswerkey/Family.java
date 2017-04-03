package org.pltw.examples.collegeappanswerkey;

import java.util.ArrayList;

/**
 * Created by jlinburg on 3/9/17.
 */
public class Family {
    private final String TAG = "FAMILY";
    private ArrayList<FamilyMember> family;
    private static Family sFamily = new Family();

    private Family(){
        family = new ArrayList<>();
        //family.add(new Guardian("Family", "Member"));
        //family.add(new Guardian());
        //family.add(new Sibling("My", "Brother"));
        //family.add(new Sibling("My", "Sister"));
    }

    public static Family get(){
        if (sFamily == null){
            sFamily = new Family();
        }
        return sFamily;
    }

    public ArrayList<FamilyMember> getFamily(){
        return family;
    }

    public void setFamily(ArrayList<FamilyMember> fmArray){
        family=fmArray;
    }

    public void resetFamily(){
        family = new ArrayList<>();
    }

    public void addFamilyMember(FamilyMember fm){
        family.add(fm);
    }

    public void deleteFamilyMember(FamilyMember fm){
        family.remove(fm);
    }

}
