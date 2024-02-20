import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TicTacToeSwing extends JPanel {
    private JButton[][] matrix;
    private char currentPlayer = 'X';

    public TicTacToeSwing() {
    
        setLayout(null);
    
        matrix = new JButton[3][3];
    
        int buttonSize = 110; 
        int spacingX = 25;
        int spacingY = 40;
        
    
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.PLAIN, 70));
    
                int x = 100 + col * (buttonSize + spacingX);
                int y = 60 + row * (buttonSize + spacingY);
    
                button.setBounds(x, y, buttonSize, buttonSize);
                button.setForeground(Color.WHITE);
                button.setOpaque(false);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
    
                int finalRow = row;
                int finalCol = col;
    
                button.addActionListener(e -> makeMove(finalRow, finalCol));
    
                matrix[row][col] = button;
                add(button);
            }
        }

}

    private void makeMove(int row, int col) {
        JButton button = matrix[row][col];

        if (button.getText().equals("")) {
            button.setText(Character.toString(currentPlayer));
            if (checkForWin(matrix)) {
                showWinnerDialog();
                restartGame();
            } else if (isBoardFull(matrix)) {
                showDrawDialog();
                restartGame();
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                if (currentPlayer == 'O') {
                    computerMove();
                }
            }
        }
    }

    private void computerMove() {
        int[] bestMove = minimax(matrix);
        matrix[bestMove[0]][bestMove[1]].doClick(); // Apeleaza evenimentul de clic al butonului
    }

    private int[] minimax(JButton[][] currentState) {
        int[] bestMove = new int[]{-1, -1};
        int bestScore = Integer.MIN_VALUE;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (currentState[row][col].getText().equals("")) {
                    currentState[row][col].setText("O");
                    int score = minimaxHelper(currentState, false);
                    currentState[row][col].setText("");

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = row;
                        bestMove[1] = col;
                    }
                }
            }
        }

        return bestMove;
    }

    private boolean checkForWin(JButton[][] currentState) {
    for (int i = 0; i < 3; i++) {
        if (!currentState[i][0].getText().equals("") &&
            currentState[i][0].getText().equals(currentState[i][1].getText()) &&
            currentState[i][1].getText().equals(currentState[i][2].getText())) {
            return true;
        }

        if (!currentState[0][i].getText().equals("") &&
            currentState[0][i].getText().equals(currentState[1][i].getText()) &&
            currentState[1][i].getText().equals(currentState[2][i].getText())) {
            return true;
        }
    }

    if (!currentState[0][0].getText().equals("") &&
        currentState[0][0].getText().equals(currentState[1][1].getText()) &&
        currentState[1][1].getText().equals(currentState[2][2].getText())) {
        return true;
    }

    if (!currentState[0][2].getText().equals("") &&
        currentState[0][2].getText().equals(currentState[1][1].getText()) &&
        currentState[1][1].getText().equals(currentState[2][0].getText())) {
        return true;
    }

    return false;
}

    private int minimaxHelper(JButton[][] currentState, boolean isMaximizing) {
        if (checkForWin(currentState)) {
            return (isMaximizing) ? -1 : 1;
        } else if (isBoardFull(currentState)) {
            return 0;
        }

        int bestScore = (isMaximizing) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (currentState[row][col].getText().equals("")) {
                    currentState[row][col].setText((isMaximizing) ? "O" : "X");
                    int score = minimaxHelper(currentState, !isMaximizing);
                    currentState[row][col].setText("");

                    if (isMaximizing) {
                        bestScore = Math.max(bestScore, score);
                    } else {
                        bestScore = Math.min(bestScore, score);
                    }
                }
            }
        }

        return bestScore;
    }

private boolean isBoardFull(JButton[][] currentState) {
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (currentState[i][j].getText().equals("")) {
                return false;
            }
        }
    }
    return true;
}

    private void showWinnerDialog() {
        JOptionPane.showMessageDialog(this, "Player " + currentPlayer + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showDrawDialog() {
        JOptionPane.showMessageDialog(this, "It's a draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private void restartGame() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                matrix[row][col].setText("");
            }
        }
        currentPlayer = 'X';
    }
    
     @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
         try{
                Image backgroundImage = ImageIO.read(new File("backgorund-mipProject.jpg"));
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        
    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tic-Tac-Toe");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 600);

            TicTacToeSwing ticTacToe = new TicTacToeSwing();
            frame.getContentPane().add(ticTacToe);

            frame.setVisible(true);
        });
    }    
}
