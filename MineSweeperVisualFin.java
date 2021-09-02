import java.util.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.Timer;

public class MineSweeperVisualFin extends JFrame {

	private JButton[] buttons;
	private static JPanel panel;
	private ArrayList<String> grid;
	private static int height;
	private static int width;
	private static int bombs;
	private JLabel result;
	private JLabel time;
	private JLabel bombCount;
	private int time2;

	public MineSweeperVisualFin(int height2, int width2, int bombs2) {
		Timer timer;
		height = height2;
		width = width2;
		bombs = bombs2;
		buttons = new JButton[height * width];
		result = new JLabel("Playing");
		time = new JLabel("" + time2);

		ArrayList<String> bms = new ArrayList<String>();
		grid = new ArrayList<String>();

		for (int i = 0; i < height * width; i++) {
			grid.add("");
		}

		for (int i = 0; i < bombs; i++) {
			int x = (int) (Math.random()*width);
			int y = (int) (Math.random()*height);
			if (!bms.contains("" + x + y)) {
				grid.set(x + width * y, "BH");
				bms.add("" + x + y);
			} else {
				i --;
				continue;
			}
		}
		resetGrid(height, width);

		loadButtons();
		createPanel();

		setSize(45 * width + 100, 45 * height + 150);
		setLocationRelativeTo(null);
		setTitle("Minesweeper");
		time2 = 0;
		timer = new Timer();
		timer.schedule(new TimeUpdate(), 0, 1000);
	}
	
	class TimeUpdate extends TimerTask {
		public void run () {
			time2 ++;
			time.setText("" + time2);
		}
	}
	
	public void createPanel() {
		panel = new JPanel();
		for(JButton i: buttons)
			panel.add(i);
		panel.add(result);
		panel.add(time);
	}

	public void loadButtons() {
		for (int i = 0; i < height * width; i ++) {
			buttons[i] = createGameButton(i, width, height);
		}
	}

	public JButton createGameButton(int i, int width, int height) {
		int y = i / width;
		int x = (i) - width * (y);
		JButton button = new JButton("");
		button.setFocusPainted(false);
		button.setBackground(Color.GRAY);
		if (grid.get(x + width * y).substring(1,2).equals("R")) {
			button = new JButton(grid.get(x + width * y).substring(0,1));
		} else if (grid.get(x + width * y).substring(1,2).equals("F")) {
			button = new JButton("F");
		}
		button.setPreferredSize(new Dimension(45,45));


		class RevealListener implements MouseListener {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					if (grid.get(x + width * y).substring(1,2).equals("F")) {
						grid.set(x + width * y, grid.get(x + width * y).substring(0,1) + "H");
						buttons[i].setText("");
						buttons[i].setBackground(Color.GRAY);
					} else if (grid.get(x + width * y).substring(1,2).equals("R")) {
						
					} else {
						grid.set(x + width * y, grid.get(x + width * y).substring(0,1) + "F");
						buttons[i].setText("");
						buttons[i].setBackground(Color.RED);
					}
				} else {
					if (!grid.get(x + width * y).substring(0,1).equals("0") && !grid.get(x + width * y).substring(1,2).equals("F"))
						grid.set(x + width * y, grid.get(x + width * y).substring(0,1) + "R");
					buttons[i].setText(grid.get(x + width * y).substring(0,1));
					buttons[i].setBackground(Color.WHITE);
					if (grid.get(x + width * y).substring(1,2).equals("F")) {
						grid.set(x + width * y, grid.get(x + width * y).substring(0,1) + "F");
						buttons[i].setBackground(Color.RED);
					}
					if (grid.get(x + width * y).substring(0,1).equals("B")) {
						result.setText("You lost due to explosion");
						for (int i1 = 0; i1 < grid.size(); i1++) {
							grid.set(i1, grid.get(i1).substring(0,1) + "R");
							if (grid.get(i1).substring(0,1).equals("B")) {
								buttons[i1].setText("B");
								buttons[i1].setBackground(Color.RED);
							} else if (grid.get(i1).substring(1,2).equals("F")) {
								buttons[i1].setText("F");
								buttons[i1].setBackground(Color.RED);
							} else {
								buttons[i1].setText(grid.get(i1).substring(0,1));
								buttons[i1].setBackground(Color.WHITE);
							}
						}
					}

					if (grid.get(x + width * y).equals("0H")) {
						grid.set(x + width * y, "0Z");
						for (int y1 = -1; y1 < 2; y1 ++) {
							for (int x1 = -1; x1 < 2; x1 ++) {
								if 	(y1 != 0 || x1 != 0)
									zeroChange(height, width, y + y1, x + x1);
							}
						}
					}

					for (int y1 = 0; y1 < height; y1++) {
						for (int x1 = 0; x1 < width; x1++) {
							if (grid.get(x1 + width * y1).substring(1,2).equals("Z")) {
								grid.set(x1 + width * y1, "0R");
							}
						}
					}

					buttons = fixButtons(grid, buttons);

					int bombCount = 0;
					int hiddenCount = 0;
					for (int y = 0; y < height; y ++) {
						for (int x = 0; x < width; x ++) {
							if (grid.get(x + width * y) != null && grid.get(x + width * y).substring(1,2).equals("F"))
								bombCount++;
							if (grid.get(x + width * y) != null && grid.get(x + width * y).substring(1,2).equals("H"))
								hiddenCount++;
						}
					}
					if (bombCount == bombs && hiddenCount == 0)
						result.setText("You won!");
					}
				}
				@Override
				public void mouseExited(MouseEvent e) { }
				@Override
				public void mouseEntered(MouseEvent e) { }
				@Override
				public void mouseReleased(MouseEvent e) { }
				@Override
				public void mouseClicked(MouseEvent e) { }
			}
			
		MouseListener listener = new RevealListener();
    	button.addMouseListener(listener);
    	return button;
	}

	

	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.println("At the moment the grid must be a square");
		System.out.print("Enter the first dimension of the grid: ");
		int width = 10;//in.nextInt();

		System.out.print("Enter the second dimension of the grid: ");
		int height = 10;//in.nextInt();

		System.out.print("Enter the amount of bombs on the grid: ");
		int bombs = 15;//in.nextInt();

		JFrame frame = new MineSweeperVisualFin(height, width, bombs);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.add(panel);
    	frame.setVisible(true);
	}

	public JButton[] fixButtons(ArrayList<String> grid, JButton[] buttons1) {
		JButton[] buttons = buttons1;
		for (int y = 0; y < height; y ++) {
			for (int x = 0; x < width; x++) {
				if(grid.get(x + width * y).substring(1,2).equals("R") ){
					if (grid.get(x + width * y).substring(0,1).equals("0"))
						buttons[width * y + x].setText("");
					else
						buttons[width * y + x].setText(grid.get(x + width * y).substring(0,1));
					buttons[width * y + x].setBackground(Color.WHITE);
				} else if (grid.get(x + width * y).substring(1,2).equals("F")) {
					buttons[width * y + x].setText("");
					buttons[width * y + x].setBackground(Color.RED);
				} else {
					buttons[width * y + x].setText("");
				}
			}
		}
		return buttons;
	}

	public void adjacentToZeroChange( int height, int width, int y, int x) {
			if (y >= 0 && y < height && x >= 0 && x < width && !grid.get(x + width * y).substring(1,2).equals("F") && !grid.get(x + width * y).substring(0,1).equals("0")) {
				grid.set(x + width * y, grid.get(x + width * y).substring(0,1) + "R");
			}
	}

	public void zeroChange(int height, int width, int y, int x) {
			if (y  >= 0 && y < height && x >= 0 && x < width && grid.get(x + width * y).substring(0,2).equals("0H")) {
				grid.set(x + width * y, "0Z");

				for (int y1 = -1; y1 < 2; y1 ++) {
					for (int x1 = -1; x1 < 2; x1 ++) {
						adjacentToZeroChange(height, width, y + y1, x + x1);
					}
				}

				zeroChange(height, width, y + 1, x);
				zeroChange(height, width, y - 1, x);
				zeroChange(height, width, y + 1, x + 1);
				zeroChange(height, width, y, x + 1);
				zeroChange(height, width, y - 1, x + 1);
				zeroChange(height, width, y + 1, x - 1);
				zeroChange(height, width, y, x - 1);
				zeroChange(height, width, y - 1, x - 1);
			}
	}
	public int spotCheck( int x, int y, int width, int height) {
		int bAmount = 0;
		for (int y1 = -1; y1 < 2; y1 ++) {
			for (int x1 = -1; x1 < 2; x1 ++) {
				if (y + y1 >= 0 && y + y1 < height && x + x1 >= 0 && x + x1 < width && grid.get((x + x1) + width * (y + y1)) != "" && grid.get((x + x1) + width * (y + y1)).substring(0,1).equals("B")) {
					bAmount ++;
				}
			}
		}

		return bAmount;
	}
	public void resetGrid(int height, int width) {
		for (int y = 0; y < height; y ++) {
			for (int x = 0; x < width; x ++) {
				if (grid.get(x + width * y) != "" && grid.get(x + width * y).substring(0,1).equals("B")) {
					continue;
				}
				else {
					grid.set(x + width * y, "" + spotCheck(x, y, width, height) + "H");
				}
			}
		}
	}
}