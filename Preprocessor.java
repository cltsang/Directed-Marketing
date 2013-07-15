import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public final class Preprocessor {

    class FileOpeningException extends Exception{ }
    class DataFormatException extends Exception{ }

    private int max_iteration;
    private int small_sample_mode;

    private boolean closeIsCalled = false;

    DataInputStream in;
    BufferedReader rawReader;

    BufferedWriter datasetWriter;
    BufferedWriter answerWriter;

    public Preprocessor(String rawTrainingDataPath, String datasetPath, String answerPath)
            throws FileOpeningException{
        if(!openFiles(rawTrainingDataPath, datasetPath, answerPath))
            throw new FileOpeningException();
    }

    public void run()
            throws IOException, DataFormatException{
        String line;
        // the first line is a list of argument names
        if( (line = rawReader.readLine()) == null)
            throw new DataFormatException();

        while( (line = rawReader.readLine()) != null){
            String[] tokens = line.split(",");
            if(tokens.length != 17){
                System.out.println(tokens.length);
                throw new DataFormatException();
            }
            CustomerData customer = new CustomerData(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7], tokens[8],
                    tokens[9], tokens[10], tokens[11], tokens[12], tokens[13], tokens[14], tokens[15], tokens[16]);
			/*
			for(int i=0; i<tokens.length; i++)
				System.out.print(tokens + " | ");
			System.out.println();
			*/
            datasetWriter.write(customer.toWrittenDatasetString());
            datasetWriter.newLine();
            answerWriter.write(customer.toWrittenAnswerString());
            answerWriter.newLine();
        }
    }

    private boolean openFiles(String rawTrainingDataPath, String datasetPath, String answerPath){
        try{
            in = new DataInputStream( new FileInputStream(rawTrainingDataPath) );
        } catch (IOException e){
            System.err.println("Preprocessor.openFiles : Error opening the file " + rawTrainingDataPath + " for reading.--" + e.getMessage());
            return false;
        }
        rawReader = new BufferedReader(new InputStreamReader(in));

        try{
            datasetWriter = new BufferedWriter( new FileWriter(datasetPath) );
        } catch(IOException e){
            System.err.println("Preprocessor.openFiles : Error opening the file " + datasetPath + " for writing.");
            return false;
        }
        try{
            answerWriter = new BufferedWriter( new FileWriter(answerPath) );
        } catch(IOException e){
            System.err.println("Preprocessor.openFiles : Error opening the file " + answerPath + " for writing.");
            return false;
        }

        return true;
    }

    private void closeFiles(){
        if(closeIsCalled)
            return;
        try{
            if(in != null)
                in.close();
            if(datasetWriter != null)
                datasetWriter.close();
            if(answerWriter != null)
                answerWriter.close();
        } catch (IOException e){
            System.err.println("Preprocessor.closeFiles : Error closing files.");
        }
        closeIsCalled = true;
    }

    protected void finialize() throws Throwable{
        closeFiles();
        super.finalize();
    }

    public static void main(String[] args){
        Preprocessor preprocessor = null;

        try{
            preprocessor = new Preprocessor(args[0], args[1], args[2]);
        } catch (FileOpeningException e){
            System.err.println("main : Error opening files, halting system...");
            System.exit(-1);
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("main : Error reading arguments halting system...");
            System.exit(-1);
        }

        try {
            preprocessor.run();
        } catch (IOException e) {
            System.err.println("main : Error reading the file, halting system...");
            System.exit(-1);
        } catch (DataFormatException e){
            System.err.println("main : Error data format of file not correct, halting system...");
            System.exit(-1);
        }
        //preprocessor.max_iteration = Interger.parseInt(args[3]);
        //preprocessor.small_sample_mode = Interger.parseInt(args[4]);

        preprocessor.closeFiles();

        //System.out.println("preprocessing completed.");
    }

}

final class CustomerData {
    private final int age;
    private final Job job;
    private final MaritalStatus marital;
    private final Education education;
    private final boolean mdefault;;
    private final int balance;
    private final boolean housing;
    private final boolean loan;

    private final Contact contact;;
    private final int day;
    private final Month month;;
    private final int duration;

    private final int campaign;
    private final int pdays;;
    private final int previous;
    private final Poutcome poutcome;

    private boolean outcomeInitialized;
    private boolean y;

    public CustomerData(String age, String job, String marital, String education, String mdefault, String balance, String housing, String loan,
                        String contact, String day, String month, String duration, String campaign, String pdays, String previous, String poutcome){
        this.age = Integer.parseInt(age);
        if(job.equalsIgnoreCase("BLUE-COLLAR"))
            this.job = Job.BLUECOLLAR;
        else if(job.equalsIgnoreCase("SELF-EMPLOYED"))
            this.job = Job.SELFEMPLOYED;
        else if(job.equalsIgnoreCase("ADMIN."))
            this.job = Job.ADMIN;
        else
            this.job = Job.valueOf(job.toUpperCase());
        this.marital = MaritalStatus.valueOf(marital.toUpperCase());
        this.education = Education.valueOf(education.toUpperCase());
        this.mdefault = mdefault.equalsIgnoreCase("yes");
        this.balance = Integer.parseInt(balance);
        this.housing = housing.equalsIgnoreCase("yes");
        this.loan = loan.equalsIgnoreCase(loan);
        this.contact = Contact.valueOf(contact.toUpperCase());
        this.day = Integer.parseInt(day);
        this.month = Month.valueOf(month.toUpperCase());
        this.duration = Integer.parseInt(duration);
        this.campaign = Integer.parseInt(campaign);
        this.pdays = Integer.parseInt(pdays);
        this.previous = Integer.parseInt(previous);
        this.poutcome = Poutcome.valueOf(poutcome.toUpperCase());
    }

    public CustomerData(String age, String job, String marital, String education, String mdefault, String balance, String housing, String loan,
                        String contact, String day, String month, String duration, String campaign, String pdays, String previous, String poutcome, String y){
        this(age, job, marital, education, mdefault, balance, housing, loan, contact, day, month, duration, campaign, pdays, previous, poutcome);
        if(y.equalsIgnoreCase("?")){
            this.outcomeInitialized = false;
            return;
        }
        this.outcomeInitialized = true;
        this.y = y.equalsIgnoreCase("yes");
    }

    private static String fieldWrittenValue(double d){
        return String.format("%.6f", d);
    }

    private static String fieldWrittenValue(int i){
        //return Integer.toString(i);
        return String.format("%.5f", i/10000.0);
    }

    private static String fieldWrittenValue(boolean b){
        if(b)
            return "1";
        else
            return "0";
    }

    private static <E extends Enum<E>> String fieldWrittenValue(E e){
        java.lang.reflect.Method m = null;
        try {
            m = e.getClass().getDeclaredMethod("values");
        } catch (Exception ex){
            ex.printStackTrace();
        }

        String s = Long.toString((long)Math.pow(10, e.ordinal()));
        int length = 0;
        try {
            length = ((Object [])m.invoke(e)).length;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int numPrecedingZeros = length - s.length();
        StringBuilder zero = new StringBuilder();
        for(int i=0; i<numPrecedingZeros; i++)
            zero.append('0');
        String withoutSpace = zero.append(s).toString();
        StringBuilder withSpace = new StringBuilder();
        for(int i = 0; i < withoutSpace.length(); i++){
            withSpace.append(withoutSpace.charAt(i));
            withSpace.append(' ');
        }
        withSpace.deleteCharAt(withSpace.length()-1);
        //System.out.println(withoutSpace + " : " + withSpace.toString());
        return withSpace.toString();
    }

    public String toWrittenDatasetString(){
        // catch divided by zero
        return fieldWrittenValue(age/100.0) + " " + fieldWrittenValue(job) + " " + fieldWrittenValue(marital) + " " + fieldWrittenValue(education) + " " +
                fieldWrittenValue(mdefault) + " " + fieldWrittenValue(balance/75000.0) + " " + fieldWrittenValue(housing) + " " + fieldWrittenValue(loan) + " " +
                fieldWrittenValue(contact) + " " + fieldWrittenValue(day/31.0) + " " + fieldWrittenValue(month) + " " + fieldWrittenValue(duration/4000.0) + " " +
                fieldWrittenValue(campaign/50.0) + " " + fieldWrittenValue(pdays/1000.0) + " " + fieldWrittenValue(previous/30.0) + " " + fieldWrittenValue(poutcome);
    }

    public String toWrittenAnswerString(){
        if(outcomeInitialized)
            return fieldWrittenValue(y);
        else
            return null;
    }
}

enum Job{
    //ADMIN, UNKNOWN, UNEMPLOYED, MANAGEMENT, HOUSEMAID, ENTREPRENEUR, STUDENT, BLUECOLLAR, SELFEMPLOYED, RETIRED, TECHNICIAN, SERVICES;
    SERVICES, TECHNICIAN, RETIRED, SELFEMPLOYED, BLUECOLLAR, STUDENT, ENTREPRENEUR, HOUSEMAID, MANAGEMENT, UNEMPLOYED, UNKNOWN, ADMIN;
}

enum MaritalStatus{
    //MARRIED, DIVORCED, SINGLE;
    SINGLE, DIVORCED, MARRIED;
}

enum Education{
    //UNKNOWN, SECONDARY, PRIMARY, TERTIARY;
    TERTIARY, PRIMARY, SECONDARY, UNKNOWN;
}

enum Contact{
    //UNKNOWN, TELEPHONE, CELLULAR;
    CELLULAR, TELEPHONE, UNKNOWN;
}

enum Month{
    //JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC;
    DEC, NOV, OCT, SEP, AUG, JUL, JUN, MAY, APR, MAR, FEB, JAN;
}

enum Poutcome{
    //UNKNOWN, OTHER, FAILURE, SUCCESS;
    SUCCESS, FAILURE, OTHER, UNKNOWN;
}