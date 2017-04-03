package org.pltw.examples.collegeappanswerkey;

/**
 * Created by simmonsj05 on 1/15/17.
 */
public class Guardian extends FamilyMember {

    private String occupation;


    public Guardian() {
        super();
        this.occupation = "unknown";
        super.setEmail("jlinburg@doversd.org");
    }

    public Guardian(String firstName, String lastname){
        super(firstName, lastname);
        this.occupation = "unknown";
        super.setEmail("jlinburg@doversd.org");
    }

    public Guardian(String firstName, String lastName, String occupation) {
        super(firstName, lastName);
        this.occupation = occupation;
        super.setEmail("jlinburg@doversd.org");;
    }

    public Guardian(String firstName, String lastName, String occupation, String email) {
        super(firstName, lastName);
        this.occupation = occupation;
        super.setEmail("jlinburg@doversd.org");
    }

    public String toString(){
        String output = "Guardian: " + this.getFirstName() + " " + this.getLastName() + "\n\nOccupation: " + this.occupation;
        return output;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }


    }

