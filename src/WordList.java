import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class WordList {
    private final ArrayList<String> easyList = new ArrayList<>();
    //    简单单词库
    private final ArrayList<String> mediumList = new ArrayList<>();
    //    较难单词库
    private final ArrayList<String> difficultList = new ArrayList<>();
    //    困难单词库

    public WordList() {
        try {
            Scanner sc = new Scanner(new FileInputStream("wordlist.txt"));  //加载整个单词库
            while (sc.hasNextLine()) {
                String word = sc.nextLine();
                int length = word.length();
                if (length <= 6) {
                    easyList.add(word); //加载简单单词库
                } else if (length >= 9) {
                    difficultList.add(word);    //加载困难单词库
                } else {
                    mediumList.add(word);   //加载较难单词库
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Failed!");
            System.exit(0); //例外处理,直接退出程序
        }

        Collections.shuffle(easyList);
        Collections.shuffle(mediumList);
        Collections.shuffle(difficultList);
//        分别打乱三个单词库
    }

    public ArrayList<String> getEasyList() {
        return easyList;
    }

    public ArrayList<String> getMediumList() {
        return mediumList;
    }

    public ArrayList<String> getDifficultList() {
        return difficultList;
    }
}