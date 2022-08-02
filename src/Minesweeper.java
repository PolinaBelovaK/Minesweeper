import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import sweeper.Box;
import sweeper.Coord;
import sweeper.Game;
import sweeper.Ranges;

public class Minesweeper extends JFrame
{
    private Game game;
    private JPanel panel;
    private JLabel label;
    private final int COLS = 9;//столбцы
    private final int ROWS = 9;//строки
    private final int IMAGE_SIZE = 50; //размер картинок
    private final int BOMBS= 10;

    public static void main(String[] args)
    {
        new Minesweeper(); //создание сессии
    }

    private Minesweeper()
    {
        game = new Game(COLS, ROWS, BOMBS);
        game.start();
        Ranges.setSize (new Coord(COLS, ROWS));
        setImages();// из enum
        initLabel();//состояние игры
        initPanel();//создание панели
        initFrame();//создание игрового окна
    }

    private void initLabel ()
    {
        label = new JLabel("Welcome!");
        add(label, BorderLayout.SOUTH);
    }

    private void initPanel() //параметры игровой панели
    {
        panel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                for (Coord coord:Ranges.getAllCoords())
                    g.drawImage((Image) game.getBox(coord).image,
                            coord.x*IMAGE_SIZE, coord.y* IMAGE_SIZE, this);


            }
        };

        panel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                int x = e.getX() / IMAGE_SIZE;
                int y = e.getY() / IMAGE_SIZE;
                Coord coord = new Coord(x,y);
                if (e.getButton() == MouseEvent.BUTTON1)
                    game.pressLeftButton (coord);
                if (e.getButton() == MouseEvent.BUTTON3)
                    game.pressRightButton(coord);
                if (e.getButton() == MouseEvent.BUTTON2)
                    game.start();
                label.setText(getMessage ());
                panel.repaint();
            }
        });
        panel.setPreferredSize(new Dimension(
                Ranges.getSize().x * IMAGE_SIZE,
                Ranges.getSize().y * IMAGE_SIZE));//размер
        add(panel);//иниц. добавления самой панели
    }

    private String getMessage()
    {
        switch (game.getState())
        {
            case PLAYED : return "Good luck!";
            case BOMBED : return "You lose! Try again";
            case WINNER : return "CONGRATULATIONS!";
            default: return "";
        }
    }

    private void initFrame() //параметры игрового окна
    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//команда, обсулавливающая прерывание хода проги(при нажатии закрытия окна)
        setTitle("Minesweeper");//заголовок
        setResizable(false);//можно ли изменять размер окна
        setVisible(true);//видимость окна
        pack();//устанавливает такой миним. размер контейнера, к-й достаточен для отображения всех компонентов
        setLocationRelativeTo(null);//окно по центру
        setIconImage(getImage("icon"));//иконка
    }
    private void setImages()
    {
        for (Box box:Box.values())
            box.image = getImage(box.name().toLowerCase());
    }

    private Image getImage(String name)
    {
        String filename = "img/" + name.toLowerCase(Locale.ROOT) + ".png"; //находит картинку среди загруженных в сурс
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }

}
