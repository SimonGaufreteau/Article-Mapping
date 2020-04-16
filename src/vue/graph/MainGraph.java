package vue.graph;

import com.mxgraph.layout.*;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;
import parser_utils.FromHTML;
import parser_utils.Pair;
import parser_utils.ParserClass;
import parser_utils.SortingMap;
import vue.help.ArgValidator;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Applet allowing to display the result of the {@link ParserClass} in a graph. Using the {@link ArgValidator} class.
 * @author GAUFRETEAU Simon
 */
public class MainGraph extends JApplet {

	private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
	private static final Dimension DEFAULT_SIZE = new Dimension(800, 800);

	private static int MAP_DIVISOR = 5;
	private static Pair<String,String> params;

	public static void main(String[] args) throws Exception {
		params= ArgValidator.validateArgs(args);
		if(params==null) return;
		MainGraph applet = new MainGraph();
		applet.init();
		JFrame frame = new JFrame();
		frame.getContentPane().add(applet);
		frame.setTitle("Couples display");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void init() {
		// create a JGraphT graph
		ListenableGraph<String, DefaultEdge> g = new DefaultListenableGraph<>(new DefaultDirectedGraph<>(DefaultEdge.class));

		// create a visualization using JGraph, via an adapter
		jgxAdapter = new JGraphXAdapter<>(g);

		// initializing default values
		setPreferredSize(DEFAULT_SIZE);
		mxGraphComponent component = new mxGraphComponent(jgxAdapter);
		component.setConnectable(false);
		component.getGraph().setAllowDanglingEdges(false);
		getContentPane().add(component);
		resize(DEFAULT_SIZE);

		//Setting the stylesheet for the graph
		mxStylesheet mxStylesheet = new mxStylesheet();
		Map<String, Object> vertexStyle = mxStylesheet.getDefaultVertexStyle();
		Map<String, Object> edgeStyle = mxStylesheet.getDefaultEdgeStyle();

		// Vertex style
		vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		vertexStyle.put(mxConstants.STYLE_FILLCOLOR,"#FFDE00");

		// Edges style
		edgeStyle.put(mxConstants.STYLE_SHAPE,mxConstants.SHAPE_CONNECTOR);
		edgeStyle.put(mxConstants.STYLE_NOLABEL,true);
		edgeStyle.put(mxConstants.STYLE_ENDARROW,mxConstants.ARROW_OPEN);


		//Setting the stylesheet
		jgxAdapter.setStylesheet(mxStylesheet);


		// data sample
		String regex = "[\\Q ,\n.:/-+()%$^'’\"&!?;\\E]";
		String cssQuery = "p,h1,h2,h3";
		int n=3;
		String filePath = "Articles/insee-chomage.txt";
		Map<String, Integer> map=null;
		Map<Pair<String, String>, Integer> pairMap=null;
		try {
			switch (params.getKey()){
					case "url":
						String docContent = FromHTML.stringFromHTML(params.getValue(),cssQuery);
						map=ParserClass.getOccurrencesSynonyms(docContent,n,regex);
						pairMap=ParserClass.getConcurrentPairs(docContent,n,regex);
						break;
					case "file":
						map = ParserClass.getOccurrencesSynonymsFromFile(params.getValue(), n, regex);
						pairMap = ParserClass.getConcurrentPairsFromFile(params.getValue(), n, regex);
						break;
					case "example":
						System.out.println("Displaying the sample tests :");
						map = ParserClass.getOccurrencesSynonymsFromFile(filePath, n, regex);
						pairMap = ParserClass.getConcurrentPairsFromFile(filePath, n, regex);
						break;
				}
			}
		catch (IOException ex) {
			ex.printStackTrace();
		}



		//Adding the data for the words
		assert map != null;
		int i=0;
		int k=Math.min(map.size()/MAP_DIVISOR,100);
		SortingMap <String,Integer>sorting = new SortingMap<>();
		Map<String, Integer> sortedMap = sorting.sortByValue(map);

		for(String key : sortedMap.keySet()){
			g.addVertex(key);
			i++;
			if(i>k) break;
		}


		// adding the data for the bounds
		assert pairMap != null;
		i=0;
		k=pairMap.size()/MAP_DIVISOR;
		SortingMap<Pair<String,String>,Integer> sortingPairs = new SortingMap<>();
		Map<Pair<String, String>, Integer> sortedPairMap = sortingPairs.sortByValue(pairMap);
		for(Pair<String,String> key:sortedPairMap.keySet()){
			try{
				g.addEdge(key.getKey(), key.getValue());
			}catch (Exception e){
				//System.out.println("Could not find the words : "+key.getKey()+" / "+key.getValue());
			}
			i++;
			if(i>k) break;
		}


		int basevalue=5;
		int widthValue=10;
		//Changing the style of the edges
		HashMap<mxICell, DefaultEdge> cellToEdgeMap = jgxAdapter.getCellToEdgeMap();
		for (mxICell cell:cellToEdgeMap.keySet()){
			DefaultEdge defaultEdge= cellToEdgeMap.get(cell);
			Pair<String,String> pair =  getPairFromValue(defaultEdge.toString());
			int u=pairMap.get(pair);
			cell.setStyle(mxConstants.STYLE_OPACITY+"="+Math.min(u*basevalue+5,100)+";strokeWidth="+(1+(u*widthValue)/100));
		}

		//Changing the style of the cells to match their occurrences
		HashMap<mxICell, String> cellToVertexMap = jgxAdapter.getCellToVertexMap();
		for(mxICell cell: cellToVertexMap.keySet()){
			if(cell.getEdgeCount()==0){
				jgxAdapter.removeCells(new mxICell[]{cell});
				continue;
			}
			String value = cellToVertexMap.get(cell);
			int u=map.get(value)*basevalue;
			cell.setGeometry(new mxGeometry(u,u,u,u));
		}

		// positioning via jgraphx layouts
		mxFastOrganicLayout layout = new mxFastOrganicLayout(jgxAdapter);

		// executing the layout on the graph
		layout.execute(jgxAdapter.getDefaultParent());
	}

	/**
	 * A simple method used to get the values of a Pair represented as : (word1 : word2)
	 * @param value A String representing the Pair. Example : (hello : world)
	 */
	private Pair<String,String> getPairFromValue(String value){
		String result = value.replaceAll("[()]","");
		String[] prePair = result.split(" : ");
		if(prePair.length!=2) throw new IllegalArgumentException();
		return new Pair<>(prePair[0],prePair[1]);
	}
}
