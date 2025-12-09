import javax.swing.*;
import java.util.Objects;

public class Game {
    private final WordList wordList = new WordList();
    //    用以存储某轮游戏的单词库
    private int indexOfWord = 0;
    //    指示猜测的是第几个单词
    private int chances;
    //    存储还可试错的次数
    private int tipsTimes = 3;
    //    存储还可提示的次数
    private String word;
//    用于存储某轮游戏具体的单词

    public Game(String degree) {
        if (Objects.equals(degree, "Easy")) {
            word = wordList.getEasyList().get(indexOfWord);
            chances = 7;
//            当选择简单难度时,加载简单单词库并设置可试错次数
        } else if (Objects.equals(degree, "Medium")) {
            word = wordList.getMediumList().get(indexOfWord);
            chances = 5;
//            当选择较难难度时,加载较难单词库并设置可试错次数
        } else {
            chances = 3;
            word = wordList.getDifficultList().get(indexOfWord);
//            当选择困难难度时,加载困难单词库并设置可试错次数
        }
    }

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, """
                难度说明:
                Easy:单词最短,且有7次试错机会
                Medium:单词较长,且有5次试错机会
                Difficult:单词最长,只有3次试错机会.""");
//        提示玩家难度区分标准
        String[] options = {"Easy", "Medium", "Difficult"};
        String degree;
        degree = (String) JOptionPane.showInputDialog(null, "请选择游戏的难度",
                "Hangman Game难度选择", JOptionPane.QUESTION_MESSAGE,
                null, options, "Easy");
//        玩家选择难度
        if (degree == null) {
            System.exit(0);
        }
//        若玩家没有选择难度则直接退出
        Game game = new Game(degree);
        javax.swing.SwingUtilities.invokeLater(() -> new WholeFrame(game));
//        按指定难度运行程序
    }

    public int getChances() {
        return chances;
    }

    public String getWord() {
        return word;
    }

    public int getTipsTimes() {
        return tipsTimes;
    }

    public String nextWord() {
        int length = word.length();
        indexOfWord++;
//        指示下一个单词
        if (length <= 6) {
            word = wordList.getEasyList().get(indexOfWord);
//            若上个单词是简单单词则下一个单词从简单单词库中选择
        } else if (length >= 9) {
            word = wordList.getDifficultList().get(indexOfWord);
//            若上个单词是较难单词则下一个单词从较难单词库中选择
        } else {
            word = wordList.getMediumList().get(indexOfWord);
//            若上个单词是困难单词则下一个单词从困难单词库中选择
        }
        return word;
    }

    public void tips() {
        tipsTimes--;
    }

    public void falseChoose() {
        chances--;
    }
}