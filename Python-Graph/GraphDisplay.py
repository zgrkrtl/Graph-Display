import sys
import pandas as pd
import networkx as nx
import matplotlib.pyplot as plt

if len(sys.argv) < 2:
    print("Please provide the matrix CSV file")
    sys.exit(1)

file_path = sys.argv[1]
adjacency_matrix = pd.read_csv(file_path, header=None)
adjacency_matrix = pd.read_csv("adjacencymatrix.csv", header=None)

graph = nx.DiGraph()
nodes = adjacency_matrix.iloc[:, 0]
graph.add_nodes_from(nodes)

# Add edges to the graph
for i, row in adjacency_matrix.iterrows():
    node = row[0]
    edges = row[1:]
    for j, weight in enumerate(edges):
        if weight != 0:
            graph.add_edge(node, nodes[j], weight=weight)

# Draw the graph
pos = nx.spring_layout(graph)  # Choose a layout algorithm
labels = {node: node for node in graph.nodes()}  # Use node names as labels
nx.draw(graph, pos, with_labels=True)
nx.draw_networkx_edge_labels(
    graph, pos, edge_labels=nx.get_edge_attributes(graph, "weight")
)
plt.show()
