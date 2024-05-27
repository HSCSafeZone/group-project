import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class SafeZoneClient extends JFrame {
    public Container cont;
    public JPanel topPanel, gamePanel, statusPanel; // 새로운 statusPanel 추가
    public JLabel roundLabel, mineLabel, timerLabel, statusLabel;
    public JTextArea statusTextArea;
    public JPopupMenu setupMenu;
    public int width = 10;  // 고정된 맵 크기
    public int num_mine = 10;  // 고정된 지뢰 수
    public int num_round = 1;
    public JButton[] buttons;
    public int UIwidth = 50;  // 가로 크기 조절 변수
    public int UIheight = 100;  // 세로 크기 조절 변수
    public boolean isMyTurn = true;

    public SafeZoneClient() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setTitle("지뢰찾기");
        setSize(400, 200); // 높이를 조정하여 맵 아래 공간 확보
        setLocation(400, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cont = getContentPane();
        cont.setLayout(new BorderLayout());

        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1));
        topPanel.setBackground(Color.DARK_GRAY);

        roundLabel = new JLabel(num_round + "ROUND", SwingConstants.RIGHT);
        roundLabel.setFont(roundLabel.getFont().deriveFont(Font.BOLD));

        mineLabel = new JLabel("MINES: " + num_mine, SwingConstants.RIGHT);
        mineLabel.setFont(mineLabel.getFont().deriveFont(Font.BOLD));

        timerLabel = new JLabel("⏱️00:00", SwingConstants.CENTER);
        
     // SETUP
        JButton setupButton = new JButton("...");
        setupButton.addActionListener(e -> {
            setupMenu.show(setupButton, 0, setupButton.getHeight());
        });
        setupButton.setBackground(Color.LIGHT_GRAY);
        setupButton.setForeground(Color.WHITE);
        setupMenu = new JPopupMenu();
        
        JMenuItem First_option = new JMenuItem("설정1");
        setupMenu.add(First_option);
        First_option.addActionListener(e -> {
            setupButton.setText(First_option.getText());
            switchTurn();
        });
        
        JMenuItem Second_option = new JMenuItem("설정2");
        setupMenu.add(Second_option);
        Second_option.addActionListener(e -> setupButton.setText(Second_option.getText()));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        leftPanel.add(roundLabel);
        centerPanel.add(mineLabel);
        rightPanel.add(timerLabel);
        rightPanel.add(setupButton);

        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        infoPanel.add(leftPanel);
        infoPanel.add(centerPanel);
        infoPanel.add(rightPanel);

        topPanel.add(infoPanel);
        cont.add(topPanel, BorderLayout.NORTH);

        gamePanel = new JPanel();
        gamePanel.setBackground(Color.DARK_GRAY);
        cont.add(gamePanel, BorderLayout.CENTER);

        createMapPanel();  // 프로그램 시작 시 바로 맵 생성
        
        startTimer();

        // 상태창 추가
        statusPanel = new JPanel(new BorderLayout()); // 상태 패널 추가 및 배치 관리자 설정
        statusPanel.setBackground(Color.LIGHT_GRAY);
        statusLabel = new JLabel("내 턴", SwingConstants.LEFT); // 초기 상태 "내 턴"으로 설정
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD));
        statusPanel.add(statusLabel, BorderLayout.CENTER); // 상태 라벨을 중앙에 배치
        cont.add(statusPanel, BorderLayout.SOUTH); // 상태 패널을 남쪽에 배치
        

        // statusLabel의 크기 조절
        Dimension statusLabelSize = new Dimension(UIwidth, UIheight); // 가로, 세로 크기 지정
        statusLabel.setPreferredSize(statusLabelSize); // 라벨의 크기를 설정

        pack();
        setVisible(true);
    }

    private void createMapPanel() {
        JPanel mapPanel = new JPanel(new GridLayout(width, width));
        buttons = new JButton[width * width];
        for (int i = 0; i < width * width; i++) {
            buttons[i] = new JButton("" + i);
            buttons[i].addActionListener(new MyActionListener1());
            mapPanel.add(buttons[i]);
        }

        JPanel wrappedPanel = new JPanel(new BorderLayout());
        wrappedPanel.add(mapPanel, BorderLayout.CENTER);

        Border border = BorderFactory.createLineBorder(new Color(240, 240, 240), 50);
        wrappedPanel.setBorder(border);

        gamePanel.removeAll();
        gamePanel.setLayout(new GridLayout(1, 1));  // 단일 맵을 배치하기 위해 GridLayout 사용
        gamePanel.add(wrappedPanel);

        gamePanel.revalidate();
        gamePanel.repaint();
    }

    class MyActionListener1 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();
            b.setText("❌");
            b.setEnabled(false);
            b.setBackground(Color.ORANGE);
            
            // 스위치 턴 기능 추가
            switchTurn();
        }
    }

    private void switchTurn() {
        isMyTurn = !isMyTurn;
        String turnText = isMyTurn ? "내 턴" : "턴 기다리는 중";
        statusLabel.setText(turnText);
        
        // 내 턴이면 버튼 활성화, 아니면 비활성화
        for (JButton button : buttons) {
            button.setEnabled(isMyTurn);
        }
    }
    
    private void startTimer() {
        long startTimer = System.currentTimeMillis();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - startTimer;
                int minutes = (int) (elapsed / 60000);
                int seconds = (int) ((elapsed / 1000) % 60);
                timerLabel.setText(String.format("⏱️%02d:%02d", minutes, seconds));
            }
        }, 0, 1000);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SafeZoneClient());
    }
}

