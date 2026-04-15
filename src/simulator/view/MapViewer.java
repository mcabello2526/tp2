package simulator.view;

import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.MapInfo;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class MapViewer extends AbstractMapViewer {

	private int width;
	private int height;

	private int rows;
	private int cols;

	int rWidth;
	int rHeight;

	Animal.State currState;

	volatile private Collection<AnimalInfo> objs;
	volatile private Double time;

	private static class SpeciesInfo {
		private Integer count;
		private Color color;

		SpeciesInfo(Color color) {
			count = 0;
			this.color = color;
		}
	}

	Map<String, SpeciesInfo> kindsInfo = new HashMap<>();

	private Font textFont = new Font("Arial", Font.BOLD, 12);

	private boolean showHelp;

	public MapViewer() {
		initGUI();
	}

	private void initGUI() {

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case 'h':
					showHelp = !showHelp;
					repaint();
					break;
				case 's':
					Animal.State[] states = Animal.State.values();
					if (currState == null)
						currState = states[0];
					else {
						int index = 0;
						while (index < states.length && states[index] != currState) {
							index++;
						}
						currState = (index >= states.length - 1) ? null : states[index + 1];
					}
					repaint();
				default:
				}
			}

		});

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {

				requestFocus();
			}
		});

		currState = null;

		showHelp = true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g.setFont(textFont);

		gr.setBackground(Color.WHITE);
		gr.clearRect(0, 0, width, height);

		if (objs != null)
			drawObjects(gr, objs, time);

		if (showHelp) {
			g.setColor(Color.RED);
			g.drawString("h: toggle help", 12, 10);
			g.drawString("h: toggle help", 12, 20);
		}

	}

	private boolean visible(AnimalInfo a) {

		return (currState == null || currState.equals(a.getState())) ? true : false;
	}

	private void drawObjects(Graphics2D g, Collection<AnimalInfo> animals, Double time) {

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				g.setColor(new Color(128, 128, 128, 50));
				g.drawRect(j * rWidth, i * rHeight, rWidth, rHeight);
			}
		}

		for (AnimalInfo a : animals) {

			if (!visible(a))
				continue;

			SpeciesInfo speciesInfo = kindsInfo.get(a.getGeneticCode());

			if (speciesInfo == null) {
				kindsInfo.put(a.getGeneticCode(), new SpeciesInfo(ViewUtils.getColor(a.getGeneticCode())));
				speciesInfo = kindsInfo.get(a.getGeneticCode());
			}

			speciesInfo.count++;

			g.setColor(speciesInfo.color);
			g.fillRoundRect((int) (a.getPosition().getX()), (int) (a.getPosition().getY()), (int) a.getAge() / 2 + 2,
					(int) a.getAge() / 2 + 2, 2, 2);
		}

		int posX = rWidth;
		int posY = height - rHeight / 2;
		if (currState != null) {
			g.setColor(Color.BLUE);
			drawStringWithRect(g, posX, posY, "State: " + currState.toString());
			posY -= g.getFontMetrics().getStringBounds("", g).getHeight() + 5;
		}

		g.setColor(Color.MAGENTA);
		drawStringWithRect(g, posX, posY, "Time: " + String.format("%.3f", time));

		posY -= g.getFontMetrics().getStringBounds("", g).getHeight() + 5;
		for (Entry<String, SpeciesInfo> e : kindsInfo.entrySet()) {
			g.setColor(ViewUtils.getColor(e.getKey()));
			drawStringWithRect(g, posX, posY, e.getKey() + ": " + e.getValue().count);
			e.getValue().count = 0;
			posY -= g.getFontMetrics().getStringBounds("", g).getHeight() + 5;
		}
	}

	void drawStringWithRect(Graphics2D g, int x, int y, String s) {
		Rectangle2D rect = g.getFontMetrics().getStringBounds(s, g);
		g.drawString(s, x, y);
		g.drawRect(x - 1, y - (int) rect.getHeight(), (int) rect.getWidth() + 1, (int) rect.getHeight() + 5);
	}

	@Override
	public void update(List<AnimalInfo> objs, Double time) {
		this.objs = objs;
		this.time = time;
		repaint();
	}

	@Override
	public void reset(double time, MapInfo map, List<AnimalInfo> animals) {
		this.width = map.getWidth();
		this.height = map.getHeight();
		this.cols = map.getCols();
		this.rows = map.getRows();

		setPreferredSize(new Dimension(map.getWidth(), map.getHeight()));

		update(animals, time);
	}

}
