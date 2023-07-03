import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GraphDisplay extends JFrame {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the filename as a command-line argument.");
            return;
        }
        String filename = args[0];
        ArrayList<ArrayList<String>> matrix = MatrixReader(filename);
        SwingUtilities.invokeLater(() -> {
            GraphDisplay graphVisualization = new GraphDisplay(matrix);
            graphVisualization.setVisible(true);
        });
    }

    public GraphDisplay(ArrayList<ArrayList<String>> matrix) {
        setTitle("Graph Visualization");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        GraphPanel graphPanel = new GraphPanel(matrix);
        add(graphPanel, BorderLayout.CENTER);
    }

    private static ArrayList<ArrayList<String>> MatrixReader(String filename) {
        ArrayList<ArrayList<String>> matrix = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ArrayList<String> lineMembers = new ArrayList<>();
                String[] members = line.split(",");
                for (String member : members) {
                    lineMembers.add(member);
                }
                matrix.add(lineMembers);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        return matrix;
    }

    class GraphPanel extends JPanel {
        private ArrayList<ArrayList<String>> matrix;
        private HashMap<String, Point> nodeCoordinates;
        public GraphPanel(ArrayList<ArrayList<String>> matrix) {
            this.matrix = matrix;
            this.nodeCoordinates = new HashMap<>();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int numVertices = matrix.size();
            int circleSize = 100;
            int padding = 20;

            // Calculate the spacing between nodes horizontally and vertically
            int horizontalSpacing = (getWidth() - (numVertices * circleSize + padding)) / (numVertices - 1);
            int verticalSpacing = (getHeight() - circleSize) / 2;

            // Draw nodes
            for (int i = 0; i < numVertices; i++) {
                int centerX = padding + i * (circleSize + horizontalSpacing) + circleSize / 2;
                int centerY = (i % 2 == 0) ? verticalSpacing : verticalSpacing + circleSize + verticalSpacing;

                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillOval(centerX - circleSize / 2, centerY - circleSize / 2, circleSize, circleSize);

                g2d.setColor(Color.BLACK);
                g2d.drawOval(centerX - circleSize / 2, centerY - circleSize / 2, circleSize, circleSize);

                g2d.setColor(Color.BLACK);
                String nodeLabel = matrix.get(i).get(0);
                int labelX = centerX - g2d.getFontMetrics().stringWidth(nodeLabel) / 2;
                int labelY = centerY + g2d.getFontMetrics().getHeight() / 4;
                g2d.drawString(nodeLabel, labelX, labelY);
                
                nodeCoordinates.put(nodeLabel, new Point(centerX, centerY));
                
                
                }
            System.out.println(matrix);
            /*drawing edge lines*/
                for (int i = 0; i < numVertices; i++) {
                        for (int j = i + 1; j < matrix.get(i).size(); j++) {
                            if (!matrix.get(i).get(j).equals("0")) {
                                Point node1 = nodeCoordinates.get(matrix.get(i).get(0));
                                Point node2 = nodeCoordinates.get(matrix.get(j-1).get(0));

                                g2d.setColor(Color.RED);
                                drawArrow(g2d, node1.x, node1.y, node2.x, node2.y);
                                // Calculate the midpoint of the edge
                                int midX = (node1.x + node2.x) / 2;
                                int midY = (node1.y + node2.y) / 2;

                                // Draw the weight label
                                g2d.setColor(Color.BLACK);
                                g2d.drawString(matrix.get(i).get(j), midX, midY);
                             }
                        }
                }

            
           
        }
    }
private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
    double angle = Math.atan2(y2 - y1, x2 - x1);
    int arrowSize = 10;

    g2d.drawLine(x1, y1, x2, y2);
    g2d.fillPolygon(new int[]{(int) (x2 - arrowSize * Math.cos(angle - Math.PI / 6)),(int) (x2 - arrowSize * Math.cos(angle + Math.PI / 6)),x2},
    new int[]{(int) (y2 - arrowSize * Math.sin(angle - Math.PI / 6)),(int) (y2 - arrowSize * Math.sin(angle + Math.PI / 6)),y2},3);
}
}