import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.lang.StringBuffer;
public final class Trainer {

    class FileOpeningException extends Exception{ }
    class DataFormatException extends Exception{ }
    static boolean closeIsCalled = false;

    static int numberofhiddenlayer;
    static double[] answer;
    static double[] result;
    static double[][] input;
    static double[][] inputforthefirsthiddenlayer;
    static double[] inputfortheoutputlayer;
    static double[][] weightoffirstlayer;
    static double[] weightofsecondlayer;
    static double learningrate;
    static Random randomGenerator;
    static double[] otriangle;
    static double[] bias1;
    static double bias;

    static DataInputStream in1;
    static DataInputStream in2;

    static BufferedReader datasetReader;
    static BufferedReader answerReader;
    static BufferedWriter best_nnWriter;
    static int totol_number_of_input;
    static int totalnumberofiterationrequired;
    static public int smallmode;

    public Trainer(String datasetPath, String answerPath, String best_nn)
            throws FileOpeningException{
        if(!openFiles(datasetPath, answerPath, best_nn))
            throw new FileOpeningException();
        input = new double[10000][48];
        numberofhiddenlayer = 20;
        weightoffirstlayer = new double[48][numberofhiddenlayer];
        weightofsecondlayer = new double[numberofhiddenlayer];
        inputforthefirsthiddenlayer = new double[10000][48];
        inputfortheoutputlayer= new double[10000];
        otriangle = new double[48];
        learningrate = 0.875;
        randomGenerator = new Random();
        bias1 = new double[numberofhiddenlayer];
    }

    static boolean openFiles(String datasetPath, String answerPath, String best_nn){
        try{
            in1 = new DataInputStream( new FileInputStream(datasetPath) );
        } catch (IOException e){
            System.err.println("Trainer.openFiles : Error opening the file " + datasetPath + " for reading.--" + e.getMessage());
            return false;
        }
        datasetReader = new BufferedReader(new InputStreamReader(in1));

        try{
            in2 = new DataInputStream( new FileInputStream(answerPath) );
        } catch (IOException e){
            System.err.println("Trainer.openFiles : Error opening the file " + answerPath + " for reading.--" + e.getMessage());
            return false;
        }
        answerReader = new BufferedReader(new InputStreamReader(in2));

        try{
            best_nnWriter = new BufferedWriter( new FileWriter(best_nn) );
        } catch(IOException e){
            System.err.println("Trainer.openFiles : Error opening the file " + best_nn + " for writing.");
            return false;
        }

        return true;
    }

    static void closeFiles(){
        if(closeIsCalled)
            return;
        try{
            if(in1 != null)
                in1.close();
            if(in2 != null)
                in2.close();
            if(best_nnWriter != null)
                best_nnWriter.close();
        } catch (IOException e){
            System.err.println("Preprocessor.closeFiles : Error closing files.");
        }
        closeIsCalled = true;
    }

    public static void receivethedatafromdataset()
            throws IOException, DataFormatException
    {
        int count = 0;
        int i = 0;
        int j = 0;
        int k = 0;
        String temp;
        String line;
        while( (line = datasetReader.readLine()) != null)
        {
            String[] tokens = line.split(" ");
            for(String token: tokens)
            {
                if(token.equals("")) continue;
/*
if(count == 0)
{
input[j][i] = Double.parseDouble(token);
i++;
count++;
}
else if(count == 1)
{
// System.out.println(token);
for(k = 0; k < 12; k++)
{
temp = token.substring(k,k+1);
// System.out.println(temp);
input[j][i] = Double.parseDouble(temp);
i++;
}
count++;
}
else if(count == 2)
{
for(k = 0; k < 3; k++)
{
temp = token.substring(k,k+1);
input[j][i] = Double.parseDouble(temp);
i++;
}
count++;
}
else if(count == 3)
{
for(k = 0; k < 4; k++)
{
temp = token.substring(k,k+1);
input[j][i] = Double.parseDouble(temp);
i++;
}
count++;
}
else if(count == 4)
{
input[j][i] = Double.parseDouble(token);
i++;
count++;
}
else if(count == 5)
{
input[j][i] = Double.parseDouble(token);
i++;
count++;
}
else if(count == 6)
{
input[j][i] = Double.parseDouble(token);
i++;
count++;
}
else if(count == 7)
{
input[j][i] = Double.parseDouble(token);
i++;
count++;
}
else if(count ==
{
for(k = 0; k < 3; k++)
{
temp = token.substring(k,k+1);
input[j][i] = Double.parseDouble(temp);
i++;
}
count++;
}
else if(count == 9)
{
input[j][i] = Double.parseDouble(token);
i++;
count++;
}
else if(count == 10)
{
for(k = 0; k < 12; k++)
{
temp = token.substring(k,k+1);
input[j][i] = Double.parseDouble(temp);
i++;
}
count++;
}
else if(count == 11)
{
input[j][i] = Double.parseDouble(token);
i++;
count++;
}
else if(count == 12)
{
input[j][i] = Double.parseDouble(token);
i++;
count++;
}
else if(count == 13)
{
input[j][i] = Double.parseDouble(token);
i++;
count++;
}
else if(count == 14)
{
input[j][i] = Double.parseDouble(token);
i++;
count++;
}
else if(count == 15)
{
for(k = 0; k < 4; k++)
{
temp = token.substring(k,k+1);
input[j][i] = Double.parseDouble(temp);
i++;
}
count++;
}
*/
                input[j][i] = Double.parseDouble(token);
                i++;
            }
            j++;
            i = 0;
            count = 0;
        }
        totol_number_of_input = j;
        i = 0;
        while( (line = answerReader.readLine()) != null)
        {
            answer[i] = Double.parseDouble(line);
            i++;
        }
        for(i = 0; i < 48; i++)
        {
            for(j = 0; j < numberofhiddenlayer; j++)
            {
                weightoffirstlayer[i][j] = (double)randomGenerator.nextInt(100)/ 100.0 - 0.5;
            }
        }
        for(j = 0; j < numberofhiddenlayer; j++)
        {
            weightofsecondlayer[j] = (double)randomGenerator.nextInt(100)/ 100.0 - 0.5;
        }
        for(j = 0; j < numberofhiddenlayer; j++)
        {
            bias1[j] = (double)randomGenerator.nextInt(100)/ 100.0 - 0.5;
        }
        bias = (double) randomGenerator.nextInt(100)/ 100.0 - 0.5;

        System.out.println(bias);
// (new java.util.Scanner(System.in)).nextLine();

    }

    static double giveaccuracy()
    {
        double TP = 0;
        double TN = 0;
        double FP = 0;
        double FN = 0;
        double fm = 0;
        for(int i = 0; i < totol_number_of_input; i++)
        {
            if(answer[i] == 1 && result[i] == 1)
                TP++;
            else if( answer[i] == 0 && result[i] == 1)
                FP++;
            else if( answer[i] == 1 && result[i] == 0)
                FN++;
            else if(answer[i] == 0 && result[i] == 0)
                TN++;
        }
        System.out.print(TP+" ");
        System.out.print(TN+" ");
        System.out.print(FP+" ");
        System.out.print(FN+" ");
        System.out.println();
        fm = (TP + TN)/(TP+FP+FN+TN);
        return fm;
    }


    static double g(double a)
    {
        double temp;
        temp = 1 / (1 + Math.exp(-a));
// System.out.println("temp = "+ temp);
        return temp;
    }

    static double gfe(double a)
    {
        double temp;
        temp = g(a)*(1 - g(a));
        return temp;
    }
    /*
    input = new double[20000][48];
    numberofhiddenlayer = 10;
    weightoffirstlayer = new double[48][numberofhiddenlayer];
    weightofsecondlayer = new double[numberofhiddenlayer];
    inputforthefirsthiddenlayer = new double[20000][48];
    inputfortheoutputlayer= new double[20000];
    result = new double[20000];
    answer = new double[20000];
    otriangle = new double[48];
    learningrate = 0.875;
    randomGenerator = new Random();
    bias1 = new double[numberofhiddenlayer];
    */
    static void giveoutput()
    {
        int i;
        int j;
        int z;
        int m;
        int n;
        for(i = 0; i < totol_number_of_input; i++)
        {
            result[i] = 0;
            inputfortheoutputlayer[i] = 0;
            for(j = 0; j < numberofhiddenlayer; j++)
            {
                inputforthefirsthiddenlayer[i][j] = 0;
                for(z = 0; z < 48; z++)
                {
                    inputforthefirsthiddenlayer[i][j] = inputforthefirsthiddenlayer[i][j]
                            + weightoffirstlayer[z][j]*input[i][z];
                }
                inputforthefirsthiddenlayer[i][j] = inputforthefirsthiddenlayer[i][j] + bias1[j];
                inputfortheoutputlayer[i] = inputfortheoutputlayer[i]
                        + g(inputforthefirsthiddenlayer[i][j])*weightofsecondlayer[j];
            }
            inputfortheoutputlayer[i] = inputfortheoutputlayer[i] + bias;
            result[i] = g(inputfortheoutputlayer[i]);
            for( m = 0; m < numberofhiddenlayer; m++)
            {
                weightofsecondlayer[m] = weightofsecondlayer[m] - learningrate*g(inputforthefirsthiddenlayer[i][m])
                        *gfe(inputfortheoutputlayer[i])*(result[i]-answer[i]);

                otriangle[m] = gfe(inputforthefirsthiddenlayer[i][m])*weightofsecondlayer[m]*gfe(inputfortheoutputlayer[i])*(result[i]-answer[i]);

                for(n = 0; n < 48;n++)
                {
                    weightoffirstlayer[n][m] = weightoffirstlayer[n][m] - learningrate*input[i][n]*otriangle[m];
                }
            }
// System.out.println("result = "+ result[i]+","+i);
// (new java.util.Scanner(System.in)).nextLine();
            if(result[i] >= 0.5)
                result[i] = 1.0;
            else
                result[i] = 0.0;
        }
        return;
    }

    public static void givefinaloutput()
            throws IOException, DataFormatException
    {
        StringBuffer temp = new StringBuffer();
        best_nnWriter.write(String.valueOf(giveaccuracy()));
        best_nnWriter.newLine();
        best_nnWriter.write("48 20 1");
        best_nnWriter.newLine();

        best_nnWriter.write("I 48 H 20");
        best_nnWriter.newLine();
        int i, j;
        for(j = 0; j < numberofhiddenlayer; j++)
        {
            for(i = 0; i < 48 ; i++)
            {
                temp = temp.append(weightoffirstlayer[i][j] + " ");
                if(i == (48 - 1) )
                    temp = temp.append(bias1[j]);
            }
            best_nnWriter.write(temp.toString());
            best_nnWriter.newLine();
            temp.delete(0, temp.length());
        }
        best_nnWriter.write("H 20 O 1");
        best_nnWriter.newLine();
        System.out.println("H 20 O 1");
        for(i = 0; i < numberofhiddenlayer; i++)
        {
            temp = temp.append(weightofsecondlayer[i] + " ");
            if(i == (numberofhiddenlayer -1))
                temp = temp.append(bias);
        }
        best_nnWriter.write(temp.toString());
        best_nnWriter.newLine();
    }

    public static void run()
    {
        int i;
        for(i = 0; i < totalnumberofiterationrequired ; i++)
        {
            giveoutput();
            giveaccuracy();
        }
    }

    public static void main(String[] args)throws IOException, DataFormatException{
        Trainer trainer = null;
/*
try{
trainer = new Trainer(args[0], args[1], args[2]);
} catch (FileOpeningException e){
System.err.println("main : Error opening files, halting system...");
System.exit(-1);
} catch (ArrayIndexOutOfBoundsException e){
System.err.println("main : Error reading arguments halting system...");
System.exit(-1);
}
*/
        openFiles(args[0], args[1], args[2]);
        input = new double[10000][48];
        numberofhiddenlayer = 20;
        weightoffirstlayer = new double[48][numberofhiddenlayer];
        weightofsecondlayer = new double[numberofhiddenlayer];
        inputforthefirsthiddenlayer = new double[10000][48];
        inputfortheoutputlayer= new double[10000];
        result = new double[10000];
        answer = new double[10000];
        otriangle = new double[48];
        learningrate = 0.875;
        randomGenerator = new Random();
        bias1 = new double[numberofhiddenlayer];

        int temp = Integer.parseInt(args[3]);
        totalnumberofiterationrequired = 500;
        temp = Integer.parseInt(args[4]);
        smallmode = temp;
        receivethedatafromdataset();
        run();
        System.out.println("after run");
        givefinaloutput();
        closeFiles();
    }
}