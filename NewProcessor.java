import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.IOException;

public class NewProcessor {
    Scanner in;
    FileWriter dataWriter, answerWriter;

    public NewProcessor(String input, String dataset, String answer) throws IOException{
        in = new Scanner(new File(input));
        dataWriter = new FileWriter(dataset);
        answerWriter = new FileWriter(answer);

        if(in == null)
            return;
        if(dataWriter == null || answerWriter == null)
            return;

        if(in.hasNextLine())
            in.nextLine();

        while(in.hasNextLine()){
            String tokens[] = in.nextLine().split(",");
            int index = 0;

            // age
            dataWriter.write(scaleDown(tokens[index++], 100.0) + ' ');
            // job
            dataWriter.write(job(tokens[index++]) + ' ');
            // marital
            dataWriter.write(marital(tokens[index++]) + ' ');
            // education
            dataWriter.write(education(tokens[index++]) + ' ');
            // default
            dataWriter.write(binary(tokens[index++]) + ' ');
            // balance
            dataWriter.write(scaleDown(tokens[index++], 75000.0) + ' ');
            // housing
            dataWriter.write(binary(tokens[index++]) + ' ');
            // loan
            dataWriter.write(binary(tokens[index++]) + ' ');
            // contact
            dataWriter.write(contact(tokens[index++]) + ' ');
            // day
            dataWriter.write(scaleDown(tokens[index++], 31.0) + ' ');
            // month
            dataWriter.write(month(tokens[index++]) + ' ');
            // duration
            dataWriter.write(scaleDown(tokens[index++], 4000.0) + ' ');
            // campaign
            dataWriter.write(scaleDown(tokens[index++], 50.0) + ' ');
            // pdays
            dataWriter.write(scaleDown(tokens[index++], 1000.0) + ' ');
            // previous
            dataWriter.write(scaleDown(tokens[index++], 30.0) + ' ');
            // poutcome
            dataWriter.write(poutcome(tokens[index++]) + ' ');

            dataWriter.write('\n');

            // y
            answerWriter.write(binary(tokens[index]));
            answerWriter.write('\n');
        }

        in.close();
        dataWriter.close();
        answerWriter.close();
    }

    String scaleDown(String token, double scale){
        return String.format("%.6f", Integer.parseInt(token)/scale);
    }

    String binary(String token){
        if(token.equals("yes"))
            return "1";
        if(token.equals("no"))
            return "0";
        // class label can be missing
        if(token.equals("?"))
            return "?";
        System.exit(0);
        return "false";
    }

    String job(String token){
        if(token.equals("admin."))
            return "1 0 0 0 0 0 0 0 0 0 0 0";
        if(token.equals("unknown"))
            return "0 1 0 0 0 0 0 0 0 0 0 0";
        if(token.equals("unemployed"))
            return "0 0 1 0 0 0 0 0 0 0 0 0";
        if(token.equals("management"))
            return "0 0 0 1 0 0 0 0 0 0 0 0";
        if(token.equals("housemaid"))
            return "0 0 0 0 1 0 0 0 0 0 0 0";
        if(token.equals("entrepreneur"))
            return "0 0 0 0 0 1 0 0 0 0 0 0";
        if(token.equals("student"))
            return "0 0 0 0 0 0 1 0 0 0 0 0";
        if(token.equals("blue-collar"))
            return "0 0 0 0 0 0 0 1 0 0 0 0";
        if(token.equals("self-employed"))
            return "0 0 0 0 0 0 0 0 1 0 0 0";
        if(token.equals("retired"))
            return "0 0 0 0 0 0 0 0 0 1 0 0";
        if(token.equals("technician"))
            return "0 0 0 0 0 0 0 0 0 0 1 0";
        if(token.equals("services"))
            return "0 0 0 0 0 0 0 0 0 0 0 1";
        System.exit(0);
        return "false";
    }
    String marital(String token){
        if(token.equals("married"))
            return "1 0 0";
        if(token.equals("divorced"))
            return "0 1 0";
        if(token.equals("single"))
            return "0 0 1";
        System.exit(0);
        return "false";
    }
    String education(String token){
        if(token.equals("unknown"))
            return "1 0 0 0";
        if(token.equals("secondary"))
            return "0 1 0 0";
        if(token.equals("primary"))
            return "0 0 1 0";
        if(token.equals("tertiary"))
            return "0 0 0 1";
        System.exit(0);
        return "false";
    }
    String contact(String token){
        if(token.equals("unknown"))
            return "1 0 0";
        if(token.equals("telephone"))
            return "0 1 0";
        if(token.equals("cellular"))
            return "0 0 1";
        System.exit(0);
        return "false";
    }
    String month(String token){
        if(token.equals("jan"))
            return "1 0 0 0 0 0 0 0 0 0 0 0";
        if(token.equals("feb"))
            return "0 1 0 0 0 0 0 0 0 0 0 0";
        if(token.equals("mar"))
            return "0 0 1 0 0 0 0 0 0 0 0 0";
        if(token.equals("apr"))
            return "0 0 0 1 0 0 0 0 0 0 0 0";
        if(token.equals("may"))
            return "0 0 0 0 1 0 0 0 0 0 0 0";
        if(token.equals("jun"))
            return "0 0 0 0 0 1 0 0 0 0 0 0";
        if(token.equals("jul"))
            return "0 0 0 0 0 0 1 0 0 0 0 0";
        if(token.equals("aug"))
            return "0 0 0 0 0 0 0 1 0 0 0 0";
        if(token.equals("sep"))
            return "0 0 0 0 0 0 0 0 1 0 0 0";
        if(token.equals("oct"))
            return "0 0 0 0 0 0 0 0 0 1 0 0";
        if(token.equals("nov"))
            return "0 0 0 0 0 0 0 0 0 0 1 0";
        if(token.equals("dec"))
            return "0 0 0 0 0 0 0 0 0 0 0 1";
        System.exit(0);
        return "false";
    }
    String poutcome(String token){
        if(token.equals("unknown"))
            return "1 0 0 0";
        if(token.equals("other"))
            return "0 1 0 0";
        if(token.equals("failure"))
            return "0 0 1 0";
        if(token.equals("success"))
            return "0 0 0 1";
        System.exit(0);
        return "false";
    }


    public static void main(String[] args) throws Exception{
        new NewProcessor(args[0], args[1], args[2]);
    }
}
