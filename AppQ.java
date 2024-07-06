
import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
public class AppQ
{
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
            FileWriter fw = new FileWriter("donatorReportQueue.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            // donator details which will be used only if user want to print their details
            FileWriter fw2 = new FileWriter("donatorDetails.txt");

            Scanner in = new Scanner (System.in);
            String indata;

            // insert candidate checkup data from text file into checkupLL
            LinkedLists checkupLL = new LinkedLists();
            Queue checkupQ = new Queue();
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
                checkupQ.enqueue(checkup);
            }

            LinkedLists centerLL = new LinkedLists();
            Queue centerQ = new Queue();
            Queue temp = new Queue();
            while ((indata = br2.readLine()) != null) {
                StringTokenizer st   = new StringTokenizer(indata, ";");
                String id = st.nextToken();
                String name = st.nextToken();

                Center ce = new Center(id,name);
                centerLL.insertAtFront(ce);
                centerQ.enqueue(ce);
            }
            Checkup c, u = null;
            Checkup cQ, uQ = null;
            Queue temp2 = new Queue();

            // DEBUG ONLY
            System.out.println("Full donator list : ");
            while (!checkupQ.isEmpty()) {
                cQ = (Checkup)checkupQ.dequeue();
                Person p =  cQ.getPerson();
                System.out.println("----------------------------------");
                System.out.println(cQ);
                temp.enqueue(cQ);
            }
            while(!temp.isEmpty())
            {
                checkupQ.enqueue(temp.dequeue());
            }
            System.out.println("\nSimple Donator List unsorted into their categories");
            System.out.printf(("%n%-40s\t%-12s\t%-2s\t%-7s\t\t%5s%n"),"Name","IC Number","BloodType","Weight","Height");
            while (!checkupQ.isEmpty()) {
                cQ = (Checkup)checkupQ.dequeue();
                Person p =  cQ.getPerson();
                System.out.printf(("%n%-40s\t%-12s\t\t%-2s\t\t%5.2f\t\t%5.2f%n"),p.getName(),p.getIcNumber(),p.getBloodType(),p.getWeight(),p.getHeight());
                temp.enqueue(cQ);
            }
            while(!temp.isEmpty())
            {
                checkupQ.enqueue(temp.dequeue());
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
                    while((!checkupQ.isEmpty()) && !found)
                    {
                        cQ = (Checkup) checkupQ.dequeue();
                        temp.enqueue(cQ);
                        if (cQ.getPerson().getIcNumber().equals(search)){
                            found = true;
                            System.out.println("\n\nFound you, " + cQ.getPerson().getName() + " !" );
                            if (checkEligibility(cQ))
                                System.out.println("Status : Eligible");
                            else
                                System.out.println("Status : Ineligible");
                            end = true;
                            end = true;
                            uQ = cQ;
                        }
                        
                    }
                    while(!temp.isEmpty())
                    {
                        checkupQ.enqueue(temp.dequeue());
                    }
                    if (!found)
                    {
                        System.out.println("\n\nNot Found. Maybe check for typing errors?");
                    }
                    // Second Major Loop, for user options
                    if (uQ!=null){
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
                            cQ=uQ;
                            if (choice == '1'){
                                System.out.println("Sure! Here are your details : ");
                                System.out.println(uQ);

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

                                System.out.println(uQ);

                                uQ.getPerson().setHeight(h);
                                uQ.getPerson().setWeight(w);
                                uQ.setCheckup(uQ.getPerson(),pulse,bloodPressure,bodyTemperature,hemoglobinLevel,hasDisease);
                                System.out.println("\n================");
                                System.out.println("New detail:");
                                System.out.println("++++++++++++++++");

                                System.out.println(uQ);

                                resume(in);
                            }
                            else if (choice == '3'){
                                System.out.println("Printed! Check donorDetails.txt .");

                                BufferedWriter bw2 = new BufferedWriter(fw2);
                                PrintWriter pw2 = new PrintWriter(bw2);
                                pw2.println("===============================================");
                                pw2.println("\t\t\tDONOR CANDIDATE DETAILS");
                                pw2.println("+++++++++++++++++++++++++++++++++++++++++++++++");
                                if (checkEligibility(uQ))
                                {
                                    pw2.println("Eligible Candidate");
                                }
                                else
                                {
                                    pw2.println("Ineligible Candidate");
                                }
                                pw2.println(uQ);
                                pw2.println("\n===============================================");
                                pw2.close();

                                resume(in);
                            }
                            else if (choice == '4'){

                                StringBuilder userCenter = new StringBuilder();
                                int i = 0;
                                while (!centerQ.isEmpty()){
                                    Center ceQ = (Center)centerQ.dequeue();
                                    temp.enqueue(ceQ);
                                    if ((cQ.getPerson().getIcNumber().substring(6,8)).equals(ceQ.getId()))
                                       userCenter.append((i + 1)).append(") ").append(ceQ.getName()).append("\n");
                                }
                                while(!temp.isEmpty())
                                {
                                    centerQ.enqueue(temp.dequeue());
                                }

                                System.out.println("\nHere's our list of suggested Donation Centers, based on your IC: \n");
                                System.out.println(userCenter);

                                resume(in);
                            }

                            else if (choice == '5'){
                                System.out.println("Sure! An estimate of your blood volume is : " + df.format(uQ.calcBloodVolume()) + " Litres");
                                System.out.println("Remember, this is a rough estimate based on the average of normal blood volume " +
                                        "according to your gender.");

                                resume(in);
                            }
                            else if (choice == '6'){

                                System.out.println("Sure. We have removed you from our list. ");
                                Queue removeQ = new Queue();
                                boolean delete=false;
                                while(!checkupQ.isEmpty()){
                                     cQ = (Checkup)checkupQ.dequeue();
                                     if(cQ!=uQ)
                                     {
                                         removeQ.enqueue(cQ);
                                     }
                                     
                                }
                                while(!removeQ.isEmpty()){
                                    checkupQ.enqueue(removeQ.dequeue());
                                }
                                
                                
                                System.out.println("Here's the updated List:");
                                int i=0;
                                while(!checkupQ.isEmpty())
                                {
                                    cQ=(Checkup)checkupQ.dequeue();
                                    temp.enqueue(cQ);
                                    System.out.print("\n"+(i+1)+") "+cQ.getPerson().getName());
                                    i++;
                                }
                                while(!temp.isEmpty())
                                {
                                    checkupQ.enqueue(temp.dequeue());
                                }
                                System.out.print("\n");
                                
                                end = true;
                                System.out.println("Sure. See you next time! ");
                            }
                            else if (choice == '7'){
                                System.out.println("Goodbye!");
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
            // 1 . 0 Split Eligible vs Not Eligible vs Has Disease into their LinkedLists
            
            Queue eligibleQ = new Queue();
            Queue ineligibleQ = new Queue();
            Queue diseasedQ = new Queue();

            while (!checkupQ.isEmpty()) {
                cQ = (Checkup) checkupQ.dequeue(); // Get the first node
                temp.enqueue(cQ);// Remove the first node

                if (checkEligibility(cQ)) {
                    eligibleQ.enqueue(cQ);
                } else if (cQ.getHasDisease()) {
                    // insert diseased candidates to ineligible and diseased list
                    diseasedQ.enqueue(cQ);
                    ineligibleQ.enqueue(cQ);
                } else
                    ineligibleQ.enqueue(cQ);
            }
            while(!temp.isEmpty())
            {
                checkupQ.enqueue(temp.dequeue());
            }
            // 2 . 0 Calculation section
            // average age among eligible donors
            double avgAge = 0;
            while (!eligibleQ.isEmpty()){
                cQ = (Checkup) eligibleQ.dequeue();
                temp.enqueue(cQ);
                avgAge += calcAge(cQ);
            }
            while(!temp.isEmpty())
            {
                eligibleQ.enqueue(temp.dequeue());
            }

            avgAge /= eligibleQ.size();
           
            // count blood types among eligible donors
            
            int btA=0,btB=0,btAB=0,btO =0;
            while (!eligibleQ.isEmpty()){
                cQ = (Checkup) eligibleQ.dequeue();
                temp.enqueue(cQ);
               switch (cQ.getPerson().getBloodType()) {
                  case "A" -> btA++;
                  case "AB" -> btAB++;
                  case "B" -> btB++;
                  case "O" -> btO++;
               }
            }
            while(!temp.isEmpty())
            {
                eligibleQ.enqueue(temp.dequeue());
            }

            // average pulse among eligible donors
            double avgP = 0;
            while (!eligibleQ.isEmpty()){
                cQ = (Checkup) eligibleQ.dequeue();
                temp.enqueue(cQ);
                avgP += cQ.getPulse();
            }
            while(!temp.isEmpty())
            {
                eligibleQ.enqueue(temp.dequeue());
            }

            avgP /= eligibleQ.size();

            // average blood pressure among eligible donors
            double avgBP = 0;
            while (!eligibleQ.isEmpty()){
                cQ = (Checkup) eligibleQ.dequeue();
                temp.enqueue(cQ);
                avgBP += cQ.getBloodPressure();
            }
            while(!temp.isEmpty())
            {
                eligibleQ.enqueue(temp.dequeue());
            }

            avgBP /= eligibleQ.size();

            // average body temp among eligible donors
            double avgBT = 0;
            while (!eligibleQ.isEmpty()){
                cQ = (Checkup) eligibleQ.dequeue();
                temp.enqueue(cQ);
                avgBT += cQ.getBodyTemperature();
            }
            while(!temp.isEmpty())
            {
                eligibleQ.enqueue(temp.dequeue());
            }

            avgBT /= eligibleQ.size();

            // average hemoglobin level among eligible donors
            double avgHL = 0;
            while (!eligibleQ.isEmpty()){
                cQ = (Checkup) eligibleQ.dequeue();
                temp.enqueue(cQ);
                avgHL += cQ.getHemoglobinLevel();
            }
            while(!temp.isEmpty())
            {
                eligibleQ.enqueue(temp.dequeue());
            }
            avgHL /= eligibleQ.size();

            // total blood volume among eligible donors
            double totalBV = 0;
            while (!eligibleQ.isEmpty()){
                cQ = (Checkup) eligibleQ.dequeue();
                temp.enqueue(cQ);
                totalBV += cQ.calcBloodVolume();
            }
            while(!temp.isEmpty())
            {
                eligibleQ.enqueue(temp.dequeue());
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
            pw.println("\nValues are in average, * indicates total");
            pw.println("\n===============================================");
            pw.println("\t\t\tStatus of donors:");
            pw.println("+++++++++++++++++++++++++++++++++++++++++++++++");
            pw.println("\tEligible                      : " + eligibleQ.size());
            pw.println("\tIneligible                    : " + ineligibleQ.size());
            pw.println("\tDiseases                      : " + diseasedQ.size());
            pw.println("\tTotal                         : " + (eligibleQ.size()+ineligibleQ.size()));
            pw.println("\tDonors who have diseases are also ineligible.");
            pw.println("\tHence, they are not counted in the total.");
            pw.println("\n+++++++++++++++++++++++++++++++++++++++++++++++");
            pw.println("\n===============================================");

            String headFormat   ="%n%-12s\t%-2s\t%-7s\t\t%-5s\t%-5s\t%-10s\t%-6s\t%-10s\t%-10s";
            String format =      "%n%-12s\t\t%-2s\t\t%-3.2f\t\t%-3.2f\t  %-3d\t  %-3.2f\t%-2.2f\t%-2.2f\t\t%-5b";

            pw.println("\n================================================================================================");
            pw.println("\t\t\t\t\t\t\tELIGIBLE (" + eligibleQ.size() +" People)");
            pw.println("================================================================================================");
            pw.printf(headFormat,"IC Num.","BloodType","Weight","Height","Pulse","B.Pressure","Body","Hemoglobin","Has");
            pw.printf(headFormat,"       ","         ","(KG)  ","(CM)  ","(BMP)","  (mmHG)  ","Temp","Level     ","Disease");
            while (!eligibleQ.isEmpty()){
                cQ = (Checkup) eligibleQ.dequeue();
                temp.enqueue(cQ);
                pw.printf(format,cQ.getPerson().getIcNumber(),cQ.getPerson().getBloodType(),cQ.getPerson().getWeight(),cQ.getPerson().getHeight(),cQ.getPulse(),cQ.getBloodPressure(),cQ.getBodyTemperature(),cQ.getHemoglobinLevel(),cQ.getHasDisease());

            }
            while(!temp.isEmpty())
            {
                eligibleQ.enqueue(temp.dequeue());
            }
            pw.println("\n================================================================================================");
            pw.println("\t\t\t\t\t\t\tNOT ELIGIBLE (" + ineligibleQ.size() +" People)");
            pw.println("================================================================================================");
            pw.printf(headFormat,"IC Num.","BloodType","Weight","Height","Pulse","B.Pressure","Body","Hemoglobin","Has");
            pw.printf(headFormat,"       ","         ","(KG)  ","(CM)  ","(BMP)","  (mmHG)  ","Temp","Level     ","Disease");
            while (!ineligibleQ.isEmpty()){
                cQ = (Checkup) ineligibleQ.dequeue();
                temp.enqueue(cQ);
                pw.printf(format,cQ.getPerson().getIcNumber(),cQ.getPerson().getBloodType(),cQ.getPerson().getWeight(),cQ.getPerson().getHeight(),cQ.getPulse(),cQ.getBloodPressure(),cQ.getBodyTemperature(),cQ.getHemoglobinLevel(),cQ.getHasDisease());

            }
            while(!temp.isEmpty())
            {
                ineligibleQ.enqueue(temp.dequeue());
            }
            pw.println("\n================================================================================================");
            pw.println("\t\t\t\t\t\t\tHAS DISEASE (" + diseasedQ.size() +" People)");
            pw.println("================================================================================================");
            pw.printf(headFormat,"IC Num.","BloodType","Weight","Height","Pulse","B.Pressure","Body","Hemoglobin","Has");
            pw.printf(headFormat,"       ","         ","(KG)  ","(CM)  ","(BMP)","  (mmHG)  ","Temp","Level     ","Disease");
            while (!diseasedQ.isEmpty()){
                cQ = (Checkup) diseasedQ.dequeue();
                temp.enqueue(cQ);
                pw.printf(format,cQ.getPerson().getIcNumber(),cQ.getPerson().getBloodType(),cQ.getPerson().getWeight(),cQ.getPerson().getHeight(),cQ.getPulse(),cQ.getBloodPressure(),cQ.getBodyTemperature(),cQ.getHemoglobinLevel(),cQ.getHasDisease());

            }
            while(!temp.isEmpty())
            {
                diseasedQ.enqueue(temp.dequeue());
            }
            pw.println("\n================================================================================================");
            pw.println("\n================================================================================================");
            pw.println("\n\n\n\n\n\n\n\tDETAILS: A long detail section of the report. Shows all details of donators.");

            int x = 0;
            pw.println("\n=======================================================");
            pw.println("\t\t\tELIGIBLE (" + eligibleQ.size() +" People)");
            pw.println("=======================================================");
            while (!eligibleQ.isEmpty()){
                cQ = (Checkup) eligibleQ.dequeue();
                temp.enqueue(cQ);
                pw.println("\n("+(x+1)+")-------------------------------------------------");

                pw.println(cQ.toString());
                x++;
            }
            while(!temp.isEmpty())
            {
                eligibleQ.enqueue(temp.dequeue());
            }
            x = 0;

            pw.println("\n=======================================================");
            pw.println("\t\t\tNOT ELIGIBLE (" + ineligibleQ.size() +" People)");
            pw.println("=======================================================");
            while (!ineligibleQ.isEmpty()){
                cQ = (Checkup) ineligibleQ.dequeue();
                temp.enqueue(cQ);
                pw.println("\n("+(x+1)+")-------------------------------------------------");

                pw.println(cQ.toString());
                x++;
            }
            while(!temp.isEmpty())
            {
                ineligibleQ.enqueue(temp.dequeue());
            }
            x = 0;

            pw.println("\n=======================================================");
            pw.println("\t\t\tHAS DISEASE (" + diseasedQ.size() +" People)");
            pw.println("=======================================================");
            while (!diseasedQ.isEmpty()){
                cQ = (Checkup) diseasedQ.dequeue();
                temp.enqueue(cQ);
                pw.println("\n("+(x+1)+")-------------------------------------------------");

                pw.println(cQ.toString());
                x++;
            }
            while(!temp.isEmpty())
            {
                diseasedQ.enqueue(temp.dequeue());
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