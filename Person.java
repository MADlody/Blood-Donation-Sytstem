
public class Person
{
    private String name;
    private String icNumber;
    private String bloodType;
    private double weight;
    private double height;
    private String choice;
    //normal constructor
    public Person(String n,String icn,String bt,double w,double h)
    {
        name=n;
        icNumber=icn;
        bloodType=bt;
        weight=w;
        height=h;
    }
    // setter for person

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    //accessor method for name
    public String getName()
    {
        return name;
    }
    //accessor method for ic number
    public String getIcNumber()
    {
        return icNumber;
    }
    //accessor method for bloodtype
    public String getBloodType()
    {
        return bloodType;
    }
    //accessor method for weight
    public double getWeight()
    {
        return weight;
    }
    //accessor method for height
    public double getHeight()
    {
        return height;
    }
    //diplay method
    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String toString() {
        return "\nPersonal Details :" +
                "\nName         = " + name +
                "\nIC Number    = " + icNumber +
                "\nBlood Type   = " + bloodType +
                "\nWeight (kg)  = " + weight +
                "\nHeight (cm)  = " + height ;
    }
}
