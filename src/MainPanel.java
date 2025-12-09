import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainPanel extends JPanel {
    private String word;
    //    用以存储某轮游戏的单词
    private int chances;
    //    存储还剩多少次试错机会
    private final JLabel gameLabel;
    private final JLabel tipsLabel;
    //    定义两个标签分别展示游戏信息(比如字母是否正确,还剩多少次试错机会)和提示次数
    private JLabel[] wordLabel;
    //    用于在横线相应位置填写字母
    private ImageIcon thumbsPic, cryPic;
    //    分别存储正确时和错误时展示的图片
    private final JLabel picLabel = new JLabel();
    //    用于展示图片
    private int blanks;
    //    记录还有多少空格没填
    static final int BLANK_LENGTH = 25;
    Game game;
    //    用以记录某轮的游戏

    public MainPanel(Game game) {
        this.game = game;   //明确展示哪轮游戏
        word = game.getWord();
        loadWordInfo(word); //初始化单词信息
        chances = game.getChances();    //初始化试错次数
        gameLabel = new JLabel("这个英文单词总共有" + word.length() + "个字符。" +
                "还可以使用的猜测次数是" + chances);
        gameLabel.setForeground(Color.RED); //初始化游戏信息
        tipsLabel = new JLabel("你总共有" + game.getTipsTimes() + "次提示机会"); //初始化提示信息
        setLayout(null);
        gameLabel.setBounds(0, 0, 800, 30);
        add(gameLabel);
        picLabel.setBounds(100, 100, 200, 200);
        add(picLabel);  //分别为游戏信息标签和图片标签指定大小和位置并加载在指定位置
        this.loadPic(); //初始化图片
        tipsLabel.setBounds(0, 20, 800, 30);
        tipsLabel.setForeground(Color.MAGENTA);
        add(tipsLabel); //为提示信息标签指定大小、位置和颜色并加载在指定位置
    }

    private void loadWordInfo(String word) {
        blanks = word.length(); //按单词长度指定空的长度
        wordLabel = new JLabel[blanks];
        for (int i = 0; i < word.length(); i++) {
            wordLabel[i] = new JLabel(word.toUpperCase().charAt(i) + "");   //将每个字母都装载对应位置的标签上,但是先不展示出来
        }
    }

    private void loadPic() {
        ImageIcon oldThumbs = new ImageIcon("thumbs.jpg");  //加载原始图片
        Image image = oldThumbs.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); //获取并重设图片大小
        thumbsPic = new ImageIcon(image);   //将新图片加载到MainPanel中
        ImageIcon oldCry = new ImageIcon("cry.jpg");
        image = oldCry.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        cryPic = new ImageIcon(image);
    }

    public void paint(Graphics g) {
        super.paint(g); //调用父类paint()方法清除背景并处理双缓冲
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(5));  //设定横线的颜色和宽度
        int x = 5;
        int y = getHeight() - 5;    //设定横线的位置
        for (int i = 0; i < word.length(); i++) {
            g2d.drawLine(x, y, x + BLANK_LENGTH, y);
            x += 2 * BLANK_LENGTH;
        }   //绘制横线

        paintSupport(g2d);  //绘制支架

        g2d.setColor(Color.BLUE);   //设置悬挂小人的颜色
        for (int i = 0; i < 7 - chances; i++) {
            paintMan(i, g2d);
        }   //按照错误的次数绘制悬挂小人
    }

    public String getWord() {
        return word;
    }

    public int getChances() {
        return chances;
    }

    public int getBlanks() {
        return blanks;
    }

    public int getTipsTimes() {
        return game.getTipsTimes();
    }

    public void refreshNewInfo() {
        for (int i = 0; i < word.length(); i++) {
            remove(wordLabel[i]);
        }   //去除原始的游戏信息
        word = this.game.nextWord();
        loadWordInfo(word); //更新新的单词信息
        gameLabel.setText("这个英文单词总共有" + word.length() + "个字符。" +
                "还可以使用的猜测次数是" + chances);   //更新新的游戏信息
        repaint();  //更新横线数量
    }

    public void trueInfo(char option) {
        picLabel.setIcon(thumbsPic);    //显示正确时的图片
        if (blanks != 0) {
            gameLabel.setText("恭喜!" + option +
                    "是单词的组成字母。请选下一个字母。还可以使用的猜测次数是" + chances);  //当单词还未猜完时显示的信息
        } else {
            gameLabel.setText("恭喜!在有限的次数内猜对了!程序将在3秒后退出");   //当单词猜完后显示的信息
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    System.exit(0);
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask, 3000);    //单词猜完后3秒钟程序自动退出
        }
    }

    public void falseInfo(char option) {
        picLabel.setIcon(cryPic);   //显示错误时的图片
        repaint();  //绘制悬挂小人的肢体
        game.falseChoose();
        if (chances != 1) {
            chances--;
            gameLabel.setText("遗憾!" + option +
                    "不是单词的组成字母。请选下一个字母。还可以使用的猜测次数是" + chances); //当试错次数还未到1时时试错次数-1并显示错误时的信息
        } else {
            chances--;
            gameLabel.setText("遗憾!用尽了试错次数,你没有猜中单词。正确答案是:" + word + "。程序将在3秒后退出。");  //当试错次数到1时,试错次数-1并显示最后一次错误时的信息
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    System.exit(0);
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask, 3000);    //试错次数用尽,程序在3秒后自动退出
        }
    }

    private void paintSupport(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(getWidth() - 300, 100, getWidth() - 150, 100);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(getWidth() - 200, 100, getWidth() - 200, 150);
        g2d.setStroke(new BasicStroke(7));
        g2d.drawLine(getWidth() - 300, 75, getWidth() - 300, 500);
        g2d.setStroke(new BasicStroke(10));
        g2d.drawLine(getWidth() - 350, 500, getWidth() - 100, 500);
//        分别设置支架的宽度并在指定位置绘制支架
    }

    private void paintMan(int i, Graphics2D g2d) {
        switch (i) {
            case 0:
                g2d.fillOval(getWidth() - 225, 150, 50, 100);
                break;
            case 1:
                g2d.drawLine(getWidth() - 200, 250, getWidth() - 200, 255);
                break;
            case 2:
                g2d.drawLine(getWidth() - 200, 255, getWidth() - 280, 300);
                break;
            case 3:
                g2d.drawLine(getWidth() - 200, 255, getWidth() - 120, 300);
                break;
            case 4:
                g2d.drawLine(getWidth() - 200, 255, getWidth() - 200, 350);
                break;
            case 5:
                g2d.drawLine(getWidth() - 200, 350, getWidth() - 280, 395);
                break;
            case 6:
                g2d.drawLine(getWidth() - 200, 350, getWidth() - 120, 395);
        }
//        分别在第一、第二、第三、第四、第五、第六和第七次错误时绘制头部、脖子、左手、右手、身体、左脚和右脚
    }

    public void fillBlanks(int position) {
        blanks--;
        add(wordLabel[position]);   //剩余空数-1并在对应位置展示正确的字母
        wordLabel[position].setForeground(Color.BLACK);
        wordLabel[position].setBounds(5 + 2 * position * BLANK_LENGTH + BLANK_LENGTH / 4, getHeight() - 30,
                BLANK_LENGTH / 2, 30);
//        设置展示字母颜色、大小和位置
    }

    public char tipInfo() {
        int i = 0;
        game.tips();    //提示次数-1
        tipsLabel.setText("你还有" + game.getTipsTimes() + "次提示机会。");  //显示提示信息
        for (; i < word.length(); i++) {
            if (!wordLabel[i].isShowing()) {
                for (int j = i; j < word.length(); j++) {
                    if (Objects.equals(wordLabel[j].getText(), wordLabel[i].getText())) {
                        fillBlanks(j);  //找到单词中第一个还未出现的字母并自动帮助玩家填写在正确位置
                    }
                }
                trueInfo(wordLabel[i].getText().charAt(0)); //显示正确时的信息
                break;
            }
        }
        return word.toUpperCase().charAt(i);
    }
}