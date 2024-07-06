import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
public class AppLL{
    public static int calcAge (Checkup c){
        int cAge = Integer.parseInt(c.getPerson().getIcNumber().substring(0,2));
        if (cAge > 24)
            cAge += 1900;
        else
            cAge += 2000;
        cAge = 2024 - cAge;
        return cAge;
    }
    public static void printSimpleList (LinkedLists o) {
        Checkup cur;
        cur = (Checkup) o.getHead();
        int i = 0;
        while (cur!=null){
            System.out.println((i+1) + ")" + cur.getPerson().getName());
            cur = (Checkup) o.getNext();
            i++;
        }
    }

    public static boolean checkEligibility (Checkup c){
        boolean eligible = false;
        int age = calcAge(c);
        int gender = Integer.parseInt(c.getPerson().getIcNumber().substring(9,10));
        if ((gender & 2) == 0)
            gender = 1; // female
        else
            gender = 2; // male
        int pulse = c.getPulse();
        double bP = c.getBloodPressure();
        double bT = c.getBodyTemperature();
        double hL = c.getHemoglobinLevel();
                                                        //Both males and females can be blood donors.
        if ((age >= 17) && (age <= 60)) {               //The age range of potential blood donors is between 17 to 60.
            if (pulse >= 50 && pulse <= 100){           //Pulse rate should be between 50 and 100 beats per minute.
                if (bP >= 110 && bP <= 180){            //Blood pressure should be between 110 and 180 mmHg.
                    if (bT >= 36 && bT <= 37){          //Body temperature should be between 36 and 37 degrees Celsius.
                        if (!c.getHasDisease()){        //They should not have any diseases, indicated by a boolean.
                            if (gender == 1){           //For females, the hemoglobin level should be between 12.0 and 15.5 g/dL.
                                if (hL >= 12 && hL <= 15.5){
                                    eligible = true;    //When all conditions are met, candidate are eligible to donate blood.
                                }
                            }
                            else {      //For males, the hemoglobin level should be between 13.5 and 17.5 g/dL.
                                if (hL >= 13.5 && hL <= 17.5){
                                    eligible = true;    //When all conditions are met, candidate are eligible to donate blood.
                                }
                            }

                        }
                    }
                }
            }
        }
        return eligible;
    }

    public static void resume (Scanner in){
        System.out.println("Enter 'ok' to continue");
        in.next();
    }

    public static void printFormat(){

    }
    
    public static void main (String [] args){
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            // input files :
            // donator info, containing all necessary informations of them
            FileReader fr = new FileReader("donatorInfo.txt");
            BufferedReader br = new BufferedReader(fr);
            // donation centers, containing all availables centers
            FileReader fr2 = new FileReader("donationCenters.txt");
            BufferedReader br2 = new BufferedReader(fr2);

            // output files:
            // donatorReport , containing all findings and reports informations after processing through data
            FileWriter fw = new FileWriter("donatorReport.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            // donator details which will be used only if user want to print their details
            FileWriter fw2 = new FileWriter("donatorDetails.txt");

            Scanner in = new Scanner (System.in);
            String indata;

            // insert candidate checkup data from text file into checkupLL
            LinkedLists checkupLL = new LinkedLists();

            while ((indata = br.readLine()) != null) {
                StringTokenizer st   = new StringTokenizer(indata, ";");
                String name          = st.nextToken();  // Danish Iman bin Sharifuddin
                String icNumber      = st.nextToken();  // 951027031313
                String bloodType     = st.nextToken();  // B
                double weight        = Double.parseDouble(st.nextToken());      // 72.50
                double height        = Double.parseDouble(st.nextToken());      // 168.2
                int pulse            = Integer.parseInt(st.nextToken());        // 92
                double bloodPressure = Double.parseDouble(st.nextToken());      // 115.0
                double bodyTemperature = Double.parseDouble(st.nextToken());    // 36.9
                double hemoglobinLevel = Double.parseDouble(st.nextToken());    // 14.5
                boolean hasDisease     = Boolean.parseBoolean(st.nextToken());  // false
                Person person       = new Person(name,icNumber,bloodType,weight,height);
                Checkup checkup     = new Checkup(person,pulse,bloodPressure,bodyTemperature,hemoglobinLevel,hasDisease);
                checkupLL.insertAtFront(checkup);
            }

            LinkedLists centerLL = new LinkedLists();

            while ((indata = br2.readLine()) != null) {
                StringTokenizer st   = new StringTokenizer(indata, ";");
                String id = st.nextToken();
                String name = st.nextToken();

                Center ce = new Center(id,name);
                centerLL.insertAtFront(ce);
            }
            Checkup c, u = null;
            c = (Checkup)checkupLL.getHead();
            // DEBUG ONLY

            System.out.println("Full donator list : ");
            checkupLL.displayElements();
            System.out.println("Simple Donator List unsorted into their categories");
            System.out.printf(("%n%-40s\t%-12s\t%-2s\t%-7s\t\t%5s%n"),"Name","icNumber","bloodType","weight","height");
            while (c!=null) {
                Person p =  c.getPerson();
                System.out.printf(("%n%-40s\t%-12s\t\t%-2s\t\t%5.2f\t\t%5.2f%n"),p.getName(),p.getIcNumber(),p.getBloodType(),p.getWeight(),p.getHeight());

                c = (Checkup)checkupLL.getNext();
            }


            System.out.println("\n============================================================================");

            System.out.println("\t\t\t\tWelcome to our Blood Donation Program");

            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

            // Major Loop for Search
            boolean end = false;
            while (!end)
            {
                System.out.println("To Continue, Enter your IC number. To exit, type EXIT");
                System.out.print("Enter Here :");
                String search = in.nextLine();
                if (!search.equals("EXIT")){
                    boolean found = false;
                    c = (Checkup) checkupLL.getHead();
                    while(c!=null && !found)
                    {
                        if (c.getPerson().getIcNumber().equals(search)){
                            found = true;
                            System.out.println("\n\nFound you, " + c.getPerson().getName() + " !" );
                            if (checkEligibility(c))
                                System.out.println("Status : Eligible");
                            else
                                System.out.println("Status : Ineligible");
                            end = true;
                            u = c;
                        }
                        else{
                            c = (Checkup)checkupLL.getNext();
                        }
                    }
                    if (!found)
                    {
                        System.out.println("\n\nNot Found. Maybe check for typing errors?");
                    }
                    // Second Major Loop, for user options
                    if (u!=null){
                        end = false;
                        char choice ;
                        while (!end){
                            System.out.println("\n" +
                                               "Would you like to: \n" +
                                               "1.See your details\n" +
                                               "2.Update your checkup details\n" +
                                               "3.Print your details\n" +
                                               "4.Check Available Donation Centers\n" +
                                               "5.Get an estimate of your blood volume Level\n" +
                                               "6.Opt-out from donating\n" +
                                               "7.Exit");
                            System.out.print("Enter [1-7] :");
                            choice = in.next().charAt(0);
                            if (choice == '1'){


                                System.out.println("Sure! Here are your details : ");
                                System.out.println(u);
                                resume(in);
                            }
                            else if (choice == '2'){


                                System.out.println("Sure! Enter your new details:");
                                System.out.print("Weight (kg)           :");
                                double  w=in.nextDouble();
                                System.out.print("Height (cm)           :");
                                double  h=in.nextDouble();
                                System.out.print("Pulse                 :");
                                int pulse = in.nextInt();
                                System.out.print("Blood Pressure        :");
                                double bloodPressure = in.nextDouble();
                                System.out.print("Body Temperature      :");
                                double bodyTemperature = in.nextDouble();
                                System.out.print("Hemoglobin Level      :");
                                double hemoglobinLevel = in.nextDouble();
                                System.out.print("Disease [Y/N]         : ");
                                boolean hasDisease = in.next().charAt(0) == 'Y' || in.next().charAt(0) == 'y';
                                System.out.println("\n================");
                               System.out.println("Old detail: ");
                                System.out.println("++++++++++++++++");

                                System.out.println(u);

                                u.getPerson().setHeight(h);
                                u.getPerson().setWeight(w);
                                u.setCheckup(u.getPerson(),pulse,bloodPressure,bodyTemperature,hemoglobinLevel,hasDisease);
                                System.out.println("\n================");
                                System.out.println("New detail:");
                                System.out.println("++++++++++++++++");

                                System.out.println(u);

                                resume(in);
                            }
                            else if (choice == '3'){

                                System.out.println("Printed! Check donorDetails.txt .");
                                BufferedWriter bw2 = new BufferedWriter(fw2);
                                PrintWriter pw2 = new PrintWriter(bw2);
                                pw2.println("===============================================");
                                pw2.println("\t\t\tDONOR CANDIDATE DETAILS");
                                pw2.println("+++++++++++++++++++++++++++++++++++++++++++++++");
                                if (checkEligibility(u)){
                                    pw2.println("Eligible Candidate");

                                }
                                else
                                    pw2.println("Ineligible Candidate");
                                pw2.println(u);
                                pw2.println("\n===============================================");
                                pw2.close();

                                resume(in);
                            }
                            else if (choice == '4'){



                                StringBuilder userCenter = new StringBuilder();
                                Center ce = (Center) centerLL.getHead();
                                int i = 0;
                                while (ce != null){
                                    if ((c.getPerson().getIcNumber().substring(6,8)).equals(ce.getId()))
                                       userCenter.append((i + 1)).append(") ").append(ce.getName()).append("\n");
                                    ce = (Center) centerLL.getNext();
                                }

                                System.out.println("\nHere's our list of suggested Donation Centers, based on your IC: \n");
                                System.out.println(userCenter);

                                resume(in);
                            }

                            else if (choice == '5'){


                                System.out.println("Sure! An estimate of your blood volume is : " + df.format(u.calcBloodVolume()) + " Litres");
                                System.out.println("Remember, this is a rough estimate based on the average of normal blood volume " +
                                        "according to your gender.");

                                resume(in);
                            }
                            else if (choice == '6'){


                                System.out.println("Sure. We have removed you from our list. ");
                                LinkedLists temp = new LinkedLists();
                                c = (Checkup) checkupLL.getHead();
                                checkupLL.removeNode(c);

                                while(checkupLL.isEmpty()){
                                    if (c!=u){
                                        temp.insertAtFront(c);
                                    }
                                    if (checkupLL.isEmpty()){
                                        break;
                                    }
                                    c = (Checkup) checkupLL.getHead();
                                    checkupLL.removeNode(c);
                                }
                                //restore
                                while(!temp.isEmpty()){

                                    checkupLL.insertAtFront(temp.removeNode(temp.getHead()));
                                }
                                System.out.println("Here's the updated List:");
                                printSimpleList(checkupLL);
                                System.out.println("Goodbye, See you next time! ");
                                end = true;

                            }
                            else if (choice == '7'){
                                System.out.println("Goodbye !");
                                end = true;

                            }
                        }
                    }
                }
                else
                    end=true;
            }


            // System Segment : Calculate Everything to display in report
            // Focus on calculating only the eligible donors
            //  1 . 0 Split Eligible vs Not Eligible vs Has Disease into their LinkedLists

            LinkedLists eligibleList = new LinkedLists();
            LinkedLists ineligibleList = new LinkedLists();
            LinkedLists diseasedList = new LinkedLists();

            while (!checkupLL.isEmpty()) {
                c = (Checkup) checkupLL.getHead(); // Get the first node
                checkupLL.removeNode(c); // Remove the first node

                if (checkEligibility(c)) {
                    eligibleList.insertAtFront(c);
                } else if (c.getHasDisease()) {
                    // insert diseased candidates to ineligible and diseased list
                    diseasedList.insertAtFront(c);
                    ineligibleList.insertAtFront(c);
                } else
                    ineligibleList.insertAtFront(c);
            }
            // 2 . 0 Calculation section
            // average age among eligible donors
            c = (Checkup) eligibleList.getHead();
            double avgAge = 0;
            while (c!=null){
                avgAge += calcAge(c);
                c = (Checkup) eligibleList.getNext();
            }

            avgAge /= eligibleList.size();

            // count blood types among eligible donors
            c = (Checkup) eligibleList.getHead();
            int btA=0,btB=0,btAB=0,btO =0;
            while (c!=null){
               switch (c.getPerson().getBloodType()) {
                  case "A" -> btA++;
                  case "AB" -> btAB++;
                  case "B" -> btB++;
                  case "O" -> btO++;
               }
                c = (Checkup) eligibleList.getNext();
            }

            // average pulse among eligible donors
            c = (Checkup) eligibleList.getHead();
            double avgP = 0;
            while (c!=null){
                avgP += c.getPulse();
                c = (Checkup) eligibleList.getNext();
            }

            avgP /= eligibleList.size();

            // average blood pressure among eligible donors
            c = (Checkup) eligibleList.getHead();
            double avgBP = 0;
            while (c!=null){
                avgBP += c.getBloodPressure();
                c = (Checkup) eligibleList.getNext();
            }

            avgBP /= eligibleList.size();

            // average body temp among eligible donors
            c = (Checkup) eligibleList.getHead();
            double avgBT = 0;
            while (c!=null){
                avgBT += c.getBodyTemperature();
                c = (Checkup) eligibleList.getNext();
            }

            avgBT /= eligibleList.size();

            // average hemoglobin level among eligible donors
            c = (Checkup) eligibleList.getHead();
            double avgHL = 0;
            while (c!=null){
                avgHL += c.getHemoglobinLevel();
                c = (Checkup) eligibleList.getNext();
            }

            avgHL /= eligibleList.size();

            // total blood volume among eligible donors
            c = (Checkup) eligibleList.getHead();
            double totalBV = 0;
            while (c!=null){
                totalBV += c.calcBloodVolume();
                c = (Checkup) eligibleList.getNext();
            }



            // 3 . 0 Printing Section of the report
            pw.println("\n===============================================");
            pw.println("\t\t\t\tREPORT");
            pw.println("+++++++++++++++++++++++++++++++++++++++++++++++");

            pw.println("\n===============================================");
            pw.println("\tStatistics of eligible donors");
            pw.println("+++++++++++++++++++++++++++++++++++++++++++++++");
            pw.println("\tVariables*\t\t\t\t\t\tValue \n");
            pw.println("\tAge                           : " + df.format(avgAge));
            pw.println("\tPulse (bpm)                   : " + df.format(avgP));
            pw.println("\tBlood Pressure (mmHg)         : " + df.format(avgBP));
            pw.println("\tBody Temperature (Celcius)    : " + df.format(avgBT));
            pw.println("\tHemoglobin Level (g/dL)       : " + df.format(avgHL));
            pw.println("\tBlood volume (litres)*        : " + df.format(totalBV));
            pw.println("\tBlood Type A*                 : " + btA);
            pw.println("\tBlood Type B*                 : " + btB);
            pw.println("\tBlood Type AB*                : " + btAB);
            pw.println("\tBlood Type O*                 : " + btO);
            pw.println("\t\nValues are in average, * indicates total");
            pw.println("\t\n===============================================");
            pw.println("\t\t\t\tStatus of donors:");
            pw.println("+++++++++++++++++++++++++++++++++++++++++++++++");
            pw.println("\tEligible                      : " + eligibleList.size());
            pw.println("\tIneligible                    : " + ineligibleList.size());
            pw.println("\tDiseases                      : " + diseasedList.size());
            pw.println("\tTotal                         : " + (eligibleList.size()+ineligibleList.size()));
            pw.println("\t\nDonors who have diseases are also ineligible.");
            pw.println("\tHence, they are not counted in the total.");
            pw.println("+++++++++++++++++++++++++++++++++++++++++++++++");
            pw.println("\n===============================================");

            String headFormat   ="%n%-12s\t%-2s\t%-7s\t\t%-5s\t%-5s\t%-10s\t%-6s\t%-10s\t%-10s";
            String format =      "%n%-12s\t\t%-2s\t\t%-3.2f\t\t%-3.2f\t  %-3d\t  %-3.2f\t%-2.2f\t%-2.2f\t\t%-5b";

            pw.println("\n\n================================================================================================");
            pw.println("\t\t\t\t\t\t\tELIGIBLE (" + eligibleList.size() +" People)");
            pw.println("================================================================================================");
            pw.printf(headFormat,"IC Num.","BloodType","Weight","Height","Pulse","B.Pressure","Body","Hemoglobin","Has");
            pw.printf(headFormat,"       ","         ","(KG)  ","(CM)  ","(BMP)","  (mmHG)  ","Temp","Level     ","Disease");
            c = (Checkup) eligibleList.getHead();
            while (c!= null){
                pw.printf(format,c.getPerson().getIcNumber(),c.getPerson().getBloodType(),c.getPerson().getWeight(),c.getPerson().getHeight(),c.getPulse(),c.getBloodPressure(),c.getBodyTemperature(),c.getHemoglobinLevel(),c.getHasDisease());
                c = (Checkup) eligibleList.getNext();
            }

            pw.println("\n\n================================================================================================");
            pw.println("\t\t\t\t\t\t\tNOT ELIGIBLE (" + ineligibleList.size() +" People)");
            pw.println("================================================================================================");
            pw.printf(headFormat,"IC Num.","BloodType","Weight","Height","Pulse","B.Pressure","Body","Hemoglobin","Has");
            pw.printf(headFormat,"       ","         ","(KG)  ","(CM)  ","(BMP)","  (mmHG)  ","Temp","Level     ","Disease");
            c = (Checkup) ineligibleList.getHead();
            while (c!= null){
                pw.printf(format,c.getPerson().getIcNumber(),c.getPerson().getBloodType(),c.getPerson().getWeight(),c.getPerson().getHeight(),c.getPulse(),c.getBloodPressure(),c.getBodyTemperature(),c.getHemoglobinLevel(),c.getHasDisease());
                c = (Checkup) ineligibleList.getNext();
            }

            pw.println("\n\n================================================================================================");
            pw.println("\t\t\t\t\t\t\tHAS DISEASE (" + diseasedList.size() +" People)");
            pw.println("================================================================================================");
            pw.printf(headFormat,"IC Num.","BloodType","Weight","Height","Pulse","B.Pressure","Body","Hemoglobin","Has");
            pw.printf(headFormat,"       ","         ","(KG)  ","(CM)  ","(BMP)","  (mmHG)  ","Temp","Level     ","Disease");
            c = (Checkup) diseasedList.getHead();
            while (c!= null){
                pw.printf(format,c.getPerson().getIcNumber(),c.getPerson().getBloodType(),c.getPerson().getWeight(),c.getPerson().getHeight(),c.getPulse(),c.getBloodPressure(),c.getBodyTemperature(),c.getHemoglobinLevel(),c.getHasDisease());
                c = (Checkup) diseasedList.getNext();
            }

            pw.println("\n\n================================================================================================");
            pw.println("\n================================================================================================");
            pw.println("\n\n\n\n\n\n\n\tDETAILS: A long detail section of the report. Shows all details of donators.");

            int x = 0;
            pw.println("\n=======================================================");
            pw.println("\t\t\tELIGIBLE (" + eligibleList.size() +" People)");
            pw.println("=======================================================");

            c = (Checkup) eligibleList.getHead();
            while (c!= null){
                pw.println("\n("+(x+1)+")-------------------------------------------------");

                pw.println(c);
                c = (Checkup) eligibleList.getNext();
                x++;
            }

            x = 0;
            pw.println("\n\n=======================================================");
            pw.println("\t\t\tNOT ELIGIBLE (" + ineligibleList.size() +" People)");
            pw.println("=======================================================");
            x=0;
            c = (Checkup) ineligibleList.getHead();
            while (c!= null){
                pw.println("\n("+(x+1)+")-------------------------------------------------");

                pw.println(c);
                c = (Checkup) ineligibleList.getNext();
                x++;
            }
            x = 0;
            pw.println("\n\n=======================================================");
            pw.println("\t\t\tHAS DISEASE (" + diseasedList.size() +" People)");
            pw.println("=======================================================");


            c = (Checkup) diseasedList.getHead();
            while (c!= null){
                pw.println("\n("+(x+1)+")-------------------------------------------------");
                pw.println(c);
                c = (Checkup) diseasedList.getNext();
                x++;
            }
            pw.println("\n\n=======================================================");
            pw.println("\n\t\t\t Date Generated : " + LocalDate.now());
            pw.println("\n=======================================================");
            br.close();
            pw.close();
        }
        catch (Exception e)
        {System.out.println(e.getMessage());}
    }
}