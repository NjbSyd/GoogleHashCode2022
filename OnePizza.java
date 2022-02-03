import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class OnePizza {
    public static void main(String[] args) throws IOException {
        Scanner Input = new Scanner(System.in);
        System.out.print("File Directory:   ");
        String dir = Input.nextLine();
        File file = new File(dir);
        String[] Lines = Functions.FileProcessor(file);
        String[][] Words = Functions.SeparateIngredients(Lines);
        String[] Variables = Functions.RetrieveVars(Words);
        int[] Likes = new int[Variables.length];
        int[] DisLikes = new int[Variables.length];
        Arrays.fill(Likes, 0);
        Arrays.fill(DisLikes, 0);
        boolean Like_Dislike_Switch = true;
        for (int i = 1; i < Words.length; i++) {
            if (Integer.parseInt(Words[i][0]) > 0) {
                if (Like_Dislike_Switch) {
                    for (int j = 1; j < Words[i].length; j++) {
                        int index = Functions.FindIndexOf(Words[i][j], Variables);
                        Likes[index]++;
                    }
                    Like_Dislike_Switch = false;
                } else {
                    for (int j = 1; j < Words[i].length; j++) {
                        int index = Functions.FindIndexOf(Words[i][j], Variables);
                        DisLikes[index]++;
                    }
                    Like_Dislike_Switch = true;
                }
            } else {
                Like_Dislike_Switch = true;
            }
        }

        System.out.print("Output file Name: ");
        String OutputFileName = Input.nextLine();
        String OutputDir = dir.replace(dir.substring(dir.lastIndexOf('\\')), "\\" + OutputFileName + ".txt");
        FileWriter fileWriter = new FileWriter(OutputDir);
        int IngredientsCount = 0;
        String[] ChosenIngredients = new String[1];
        for (int i = 0; i < Variables.length; i++) {
            if (Likes[i] >= DisLikes[i]) {
                int Size = ChosenIngredients.length;
                IngredientsCount += 1;
                ChosenIngredients[Size - 1] = Variables[i];
                if (i <= (Variables.length - 2)) {
                    ChosenIngredients = Arrays.copyOf(ChosenIngredients, (Size + 1));
                }
            }
        }
        String FinalResult = IngredientsCount + " ";
        for (String chosenIngredient : ChosenIngredients) {
            FinalResult = FinalResult.concat(chosenIngredient + " ");
        }
        FinalResult=FinalResult.replace("null","");
        fileWriter.write(FinalResult);
        fileWriter.close();
        System.out.println("File saved in " + OutputDir);


    }//main ends here
}//OnePizza class ends here

/*
 * All used Functions are
 * Inside the following Functions
 * class.
 * */

class Functions {
    public static String[] FileProcessor(File a) {
        String[] temp = new String[1];
        try {
            Scanner fileReader = new Scanner(a);
            int i = 0;
            while (fileReader.hasNextLine()) {
                int size = temp.length;
                temp[i] = fileReader.nextLine();
                if (fileReader.hasNextLine()) {
                    temp = Arrays.copyOf(temp, (size + 1));
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found!!");
        }
        return temp;
    }


    public static String[][] SeparateIngredients(String[] a) {
        String[][] temp = new String[a.length][];
        for (int i = 0; i < a.length; i++) {
            temp[i] = a[i].split(" ");
        }
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                temp[i][j] = temp[i][j].trim();
            }
        }

        return temp;
    }


    public static String[] RetrieveVars(String[][] a) {
        String[] temp = new String[1];
        for (int i = 1; i < a.length; i++) {
            if (Integer.parseInt(a[i][0]) > 0) {
                for (int j = 1; j < a[i].length; j++) {
                    int size = temp.length;
                    if (i <= (a.length - 2)) {
                        temp = Arrays.copyOf(temp, (size + 1));
                    }
                    temp[(size - 1)] = a[i][j];
                }
            }
        }
        return ClearDuplicateAndSort(temp);
    }

    public static String[] ClearDuplicateAndSort(String[] a) {
        Set<String> SetOfStrings = new HashSet<>(Arrays.asList(a));
        a = SetOfStrings.toArray(new String[0]);
        List<String> stringList = new ArrayList<>();
        for (String y : a) {
            if (y != null && y.length() > 0) {
                stringList.add(y);
            }
        }
        a = stringList.toArray(new String[0]);
        Arrays.sort(a, Comparator.naturalOrder());
        return a;
    }

    public static int FindIndexOf(String s, String[] variables) {
        int index = 0;
        for (int i = 0; i < variables.length; i++) {
            if (s.equals(variables[i])) {
                index = i;
                break;
            }
        }
        return index;
    }
}
