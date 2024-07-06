
public class Checkup {
    private Person person;
    private int pulse;
    private double bloodPressure;
    private double bodyTemperature;
    private double hemoglobinLevel;
    private boolean hasDisease;

    //constructer
    public Checkup(Person person,int pulse,double bloodPressure,double bodyTemperature,double hemoglobinLevel,boolean hasDisease)
    {

        this.person=person;
        this.pulse=pulse;
        this.bloodPressure=bloodPressure;
        this.bodyTemperature=bodyTemperature;
        this.hemoglobinLevel=hemoglobinLevel;
        this.hasDisease=hasDisease;
    }
    public void setCheckup (Person person,int pulse,double bloodPressure,double bodyTemperature,double hemoglobinLevel,boolean hasDisease){
        this.person=person;
        this.pulse=pulse;
        this.bloodPressure=bloodPressure;
        this.bodyTemperature=bodyTemperature;
        this.hemoglobinLevel=hemoglobinLevel;
        this.hasDisease=hasDisease;
    }
    public Person getPerson()
    {
        return person;
    }
    public int getPulse()
    {
        return pulse;
    }
    public double getBloodPressure()
    {
        return bloodPressure;
    }
    public double getBodyTemperature()
    {
        return bodyTemperature;
    }
    public double getHemoglobinLevel()
    {
        return hemoglobinLevel;
    }
    public boolean getHasDisease()
    {
        return hasDisease;
    }
    public void display()
    {
        System.out.print("\n"+person.toString()+String.format("%-3d \t %-4.2f \t %-3.2f \t %-2.2f \t %-3b \t %-7s",pulse,bloodPressure,bodyTemperature,hemoglobinLevel,hasDisease));
    }
    public double calcBloodVolume()
    {
        // blood volume in mL: weight * 70
        // men = weight * 75 (average blood volume in male)
        // women = weight * 65 (average blood volume in females)
        double bloodVolume=0;
        if(Integer.parseInt(person.getIcNumber().substring(11))%2==0)
        {
            //women
            bloodVolume = (person.getWeight() * 65) / 1000;
        }
        else
        {   //men
            bloodVolume = (person.getWeight() * 75) / 1000;
        }
        return bloodVolume;
    }


    public String toString() {
        return  person +
                "\n\nCheckup Details: " +
                "\nPulse            = " + pulse +
                "\nB.Pressure       = " + bloodPressure +
                "\nTemperature      = " + bodyTemperature +
                "\nHemoglobin Level = " + hemoglobinLevel +
                "\nHas Disease      = " + hasDisease;
    }
}
