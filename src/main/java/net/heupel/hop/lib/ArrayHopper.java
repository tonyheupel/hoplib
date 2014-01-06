package net.heupel.hop.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class ArrayHopper {
	final static public Object[] FAILURE_ROUTE = new Object[0];
	
	private int[] array;
	private int[] distances;
	private Boolean[] visited;
	private int[] previous;

	public ArrayHopper(int[] array) {
		this.array = array;
		this.initializeTrackingArrays();
	}

	
	private void initializeTrackingArrays() {
		final int TRACKING_ARRAY_SIZE = array.length + 1;
		
		this.distances = new int[TRACKING_ARRAY_SIZE];
		this.visited = new Boolean[TRACKING_ARRAY_SIZE];
		this.previous = new int[TRACKING_ARRAY_SIZE];
		
		setInitialTrackingArrayValues(TRACKING_ARRAY_SIZE);
	}

	
	private void setInitialTrackingArrayValues(int size) {
		final int UNSET_DISTANCE = Integer.MAX_VALUE;
		final int UNSET_PREVIOUS_NODE_INDEX = Integer.MIN_VALUE;
		
		for (int i = 0; i < size; i++) {
			setDistanceToNode(i, UNSET_DISTANCE);
			setNodeAsUnvisited(i);
			setPreviousNodeIndexForNode(i, UNSET_PREVIOUS_NODE_INDEX);
		}
	}

	
	public Object[] shortestPathOut() {
		final int START_INDEX = 0;
		final int JUST_OUTSIDE_ARRAY_INDEX = this.array.length;

		return findShortestPathToTarget(START_INDEX, JUST_OUTSIDE_ARRAY_INDEX);
	}
	
	
	private Object[] findShortestPathToTarget(int sourceIndex, int targetIndex) {		
		
		this.setDistanceToNode(sourceIndex, 0); // distance from source to self is zero
		
		Stack<Integer> stateTracking = new Stack<Integer>();
		stateTracking.push(sourceIndex);
				
		while (!stateTracking.isEmpty()) {
			processNode(targetIndex, stateTracking);
		}
		
		if (this.isNodeAlreadyVisited(targetIndex)) {
			return buildRoute(sourceIndex, targetIndex);
		}
		
		return FAILURE_ROUTE;
	}


	private void processNode(int targetIndex, Stack<Integer> stateTracking) {
		int currentIndex = stateTracking.pop();

		if (isTarget(currentIndex, targetIndex)) {
			this.setNodeAsVisited(currentIndex);
		}

		if (this.isNodeAlreadyVisited(currentIndex)) return;
		
		updatePathsIfNodeIsShortestPathToNeighbors(currentIndex, targetIndex, stateTracking);
	}
	
	private void updatePathsIfNodeIsShortestPathToNeighbors(int currentIndex, int targetIndex, Stack<Integer> stateTracking) {
	
		// TODO: Can consider bringing the loop back in here to only loop once through the neighbor node work
		int[] neighborIndexes = getNeighborNodes(currentIndex);
		
		for (int neighborIndex : neighborIndexes) {			
			neighborIndex = adjustNeighborIndexWhenOutOfBounds(neighborIndex, targetIndex);
			updatePathIfNodeIsShortestPathToNeighbor(currentIndex, neighborIndex, stateTracking);
		}
	}

	
	private int[] getNeighborNodes(int index) {
		int nodeValue = this.getValueAtNode(index);
		
		int[] neighbors = new int[nodeValue];
		
		for (int neighborNumber = 1; neighborNumber <= nodeValue; neighborNumber++) {
			int neighborIndex = index + neighborNumber;
			neighbors[neighborNumber - 1] = neighborIndex;
		}
		
		return neighbors;
	}
	
	private void updatePathIfNodeIsShortestPathToNeighbor(int currentIndex, int neighborIndex, Stack<Integer> stateTracking) {
		final int COST_OF_HOP = 1;
		
		int possibleNeighborDistance = this.getDistanceToNode(currentIndex) + COST_OF_HOP;
		
		if (possibleNeighborDistance < this.getDistanceToNode(neighborIndex)) {
			updateNeighborWithNewShortestDistance(currentIndex, neighborIndex, possibleNeighborDistance);
			addNeighborToBeProcessedWhenNotYetProcessed(neighborIndex, stateTracking);
		}
	}


	private void addNeighborToBeProcessedWhenNotYetProcessed(int neighborIndex, Stack<Integer> stateTracking) {
		if (this.isNodeNotYetVisited(neighborIndex)) {
			stateTracking.push(neighborIndex);
		}
	}


	private void updateNeighborWithNewShortestDistance(int currentIndex, int neighborIndex, int possibleNeighborDistance) {
		this.setDistanceToNode(neighborIndex, possibleNeighborDistance);
		this.setPreviousNodeIndexForNode(neighborIndex, currentIndex);
	}
	
	
	private int adjustNeighborIndexWhenOutOfBounds(int neighborIndex, int maxIndex) {
		return (neighborIndex >= maxIndex) ? maxIndex : neighborIndex;
	}
	
	
	private static Boolean isTarget(int currentIndex, int targetIndex) {
		return currentIndex == targetIndex;
	}
	
	private int getValueAtNode(int index) {
		return this.array[index];
	}
	
	private int getPreviousNodeIndexForNode(int index) {
		return this.previous[index];
	}
	
	private void setPreviousNodeIndexForNode(int nodeIndex, int previousNodeIndex) {
		this.previous[nodeIndex] = previousNodeIndex;
	}
	
	private void setDistanceToNode(int index, int distance) {
		this.distances[index] = distance;
	}
	

	private int getDistanceToNode(int index) {
		return this.distances[index];
	}
	
	
	private void setNodeAsVisited(int index) {
		setNodeVisited(index, true);
	}
	
	
	private void setNodeAsUnvisited(int index) {
		setNodeVisited(index, false);
	}
	
	
	private void setNodeVisited(int index, Boolean visited) {
		this.visited[index] = visited;
	}
	
	
	private Boolean isNodeAlreadyVisited(int index) {
		return this.visited[index];
	}
	
	
	private Boolean isNodeNotYetVisited(int index) {
		return !isNodeAlreadyVisited(index);
	}
	
	
	private Object[] buildRoute(int sourceIndex, int targetIndex) {
		ArrayList<Object> route = new ArrayList<Object>();
		
		route.add("out");
		int currentIndex = getPreviousNodeIndexForNode(targetIndex);
		
		do {
			route.add(currentIndex);
			currentIndex = getPreviousNodeIndexForNode(currentIndex);
		} while (currentIndex != sourceIndex);

		route.add(sourceIndex);
		
		// TODO: Can consider iterating once through this route to build the array
		Collections.reverse(route);
		return route.toArray();
	}
}
