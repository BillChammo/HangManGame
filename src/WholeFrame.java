import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WholeFrame extends JFrame {
    private final JButton[] letterBtn;
    //    定义字母按钮
    private final JMenuItem nextWord;
    private final JMenuItem quitGuess;
    private final JMenuItem quitGame;
    //    分别定义菜单成员:下一个单词、放弃猜测和推出游戏
    private final JButton tipsBtn;
    //    定义提示按钮
    private final MainPanel mainPanel;
//    定义主要面板,即区域②

    public WholeFrame(Game game) {
        setTitle("Hangman Game");   //设置标题
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置点击关闭按钮时要做的动作
        setSize(800, 800);  //设置窗口大小
        setVisible(true);   //设置窗口可见
        mainPanel = new MainPanel(game);    //为mainPanel创制一个对象,放置在区域②中
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");//定义菜单栏及其内容
        menuBar.add(menu);
        setJMenuBar(menuBar);   //将菜单装进菜单栏,将菜单栏装进窗口中

        JPanel letterBtnPanel = new JPanel();   //为字母按钮定义一个板块
        LetterChooseHandler letterChooseHandler = new LetterChooseHandler();    //定义字母按钮监听器
        letterBtnPanel.setLayout(new GridLayout(2, 13));    //字母板块的排版方式为GridLayout,为2行13列
        letterBtn = new JButton[26];    //定义一个数组方便创制字母按钮
        char temp = 'A';
        for (int i = 0; i < 26; i++) {
            letterBtn[i] = new JButton(temp + "");  //设置字母按钮的文案
            letterBtnPanel.add(letterBtn[i]);   //将字母按钮放进板块中
            letterBtn[i].addActionListener(letterChooseHandler);    //为每个字母按钮注册监听器
            temp++;
        }

        MenuChooseHandler menuChooseHandler = new MenuChooseHandler();  //定义菜单的监听器
        nextWord = new JMenuItem("下一个单词");
        quitGuess = new JMenuItem("放弃猜测");
        quitGame = new JMenuItem("退出"); //分别为下一个单词、放弃猜测和退出按钮创制对象
        nextWord.addActionListener(menuChooseHandler);
        quitGuess.addActionListener(menuChooseHandler);
        quitGame.addActionListener(menuChooseHandler);  //分别为三者注册监听器
        menu.add(nextWord);
        menu.add(quitGuess);
        menu.add(quitGame); //分别将三者装入菜单中

        JPanel tipsBtnPanel = new JPanel(); //为提示按钮定义一个板块
        TipsHandler tipsHandler = new TipsHandler();    //为提示按钮定义一个监听器
        tipsBtn = new JButton("提示");    //为提示按钮创制一个对象
        tipsBtnPanel.add(tipsBtn);
        tipsBtn.addActionListener(tipsHandler); //将提示按钮装进板块中,为提示按钮注册监听器

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());   //为整个面板定义一个容器并设置为BoarderLayout
        cp.add(letterBtnPanel, BorderLayout.NORTH);
        cp.add(tipsBtnPanel, BorderLayout.SOUTH);
        cp.add(mainPanel, BorderLayout.CENTER); //字母按钮放置在北端,提示按钮放置在南端,区域②放置在中间
    }

    private class LetterChooseHandler implements ActionListener {
        char option;

        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            option = button.getText().charAt(0);    //读取字母按钮上的文案以此来知晓玩家选择的字母
            button.setEnabled(false);//将读取过的按钮设置为不可用
            boolean flag = false;   //设置一个指标观察玩家是否选择了正确的字母
            for (int i = 0; i < mainPanel.getWord().length(); i++) {
                if (option == mainPanel.getWord().toUpperCase().charAt(i)) {
                    flag = true;    //将指标设为true
                    mainPanel.fillBlanks(i);    //在横线上相应的位置填写字母
                }   //当玩家正确选择了字母
            }

            if (mainPanel.getBlanks() == 0) {
                for (int i = 0; i < 26; i++) {
                    letterBtn[i].setEnabled(false); //将所有字母按钮设为不可用,避免玩家继续选择
                }
            }

            if (flag) {
                mainPanel.trueInfo(option); //如果玩家选择了正确的字母,区域②左上角显示相应的信息
            } else {
                mainPanel.falseInfo(option);    //如果玩家选择了错误的字母,区域②左上角显示相应的信息
                if (mainPanel.getChances() == 0) {
                    for (int i = 0; i < 26; i++) {
                        letterBtn[i].setEnabled(false); //将所有字母按钮设为不可用,避免玩家继续选择
                    }
                }   //当玩家的试错次数用完
            }
        }
    }

    private class MenuChooseHandler implements ActionListener {
        JMenuItem menuItem;

        public void actionPerformed(ActionEvent e) {
            menuItem = (JMenuItem) e.getSource();
            if (menuItem == quitGame) {
                System.exit(0); //当玩家选择了退出,游戏直接关闭
            } else if (menuItem == nextWord) {
                for (int i = 0; i < 26; i++) {
                    letterBtn[i].setEnabled(true);
                }   //当玩家选择了下一个单词,将所有字母按钮重新设为可用
                mainPanel.refreshNewInfo(); //横线上字母清除,左上角信息更新
            } else if (menuItem == quitGuess) {
                for (int i = 0; i < 26; i++) {
                    letterBtn[i].setEnabled(false);
                }   //当玩家选择了放弃猜测,所有的字母按钮设为不可用
            }
        }
    }

    private class TipsHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if (button == tipsBtn) {
                char tip = mainPanel.tipInfo(); //读取单词中第一个还未出现的字母,在横线相应位置上填写该字母(tipInfo()完成填写)
                for (int i = 0; i < 26; i++) {
                    if (tip == letterBtn[i].getText().charAt(0)) {
                        letterBtn[i].setEnabled(false); //将提示的字母的按钮设为不可用,避免玩家再次选择
                        break;
                    }
                }
                if (mainPanel.getTipsTimes() == 0) {
                    tipsBtn.setEnabled(false);  //提示按钮设为不可用
                }   //当提示次数用完

                if (mainPanel.getBlanks() == 0) {
                    for (int i = 0; i < 26; i++) {
                        letterBtn[i].setEnabled(false); //将所有字母按钮设为不可用,避免玩家继续选择
                    }
                }

            }   //当玩家选择了提示按钮
        }
    }
}