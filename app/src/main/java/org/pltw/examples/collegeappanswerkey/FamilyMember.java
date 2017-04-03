package org.pltw.examples.collegeappanswerkey;

/**
 * Created by simmonsj05 on 1/15/17.
 */
public abstract class FamilyMember {

    public static final String EXTRA_RELATION = "org.pltw.examples.collegeapp.relation";
    public static final String EXTRA_INDEX = "org.pltw.examples.collegeapp.index";
    public FamilyMember() {
        this.firstName = "Default";
        this.lastName = "Constructor";
    }

    public FamilyMember(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private String firstName, lastName;
    private String mEmail;

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if ((o instanceof Guardian) && (this instanceof Guardian)) {

            if (((Guardian)o).getFirstName().equals(this.getFirstName()) && ((Guardian)o).getLastName().equals(this.getLastName())) {
                return true;
            }
        }
        else if ((o instanceof Sibling) && (this instanceof Sibling)) {

            if (((Sibling)o).getFirstName().equals(this.getFirstName())&& ((Sibling)o).getLastName().equals(this.getLastName())) {
                return true;
            }
        }
        return false;
    }

}
